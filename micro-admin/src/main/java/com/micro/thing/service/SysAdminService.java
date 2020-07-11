package com.micro.thing.service;

import com.micro.thing.dao.SysAdminDao;
import com.micro.thing.dao.SysRoleDao;
import com.micro.thing.mybatis.mapper.SysAdminRoleRelationMapper;
import com.micro.thing.mybatis.mapper.SysRoleMapper;
import com.micro.thing.mybatis.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SysAdminService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(SysAdminService.class);

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysRoleDao sysRoleDao;

    @Resource
    SysAdminDao sysAdminDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Resource
    SysAdminRoleRelationMapper sysAdminRoleRelationMapper;

    public SysAdmin loadById(Integer id) {
        return sysAdminDao.selectByPrimaryKey(id);
    }

    public SysAdmin loadByUsername(String username) {
        final SysAdminExample example = new SysAdminExample();
        example.or().andUsernameEqualTo(username);
        final List<SysAdmin> sysAdmins = sysAdminDao.selectByExample(example);
        if (sysAdmins != null && !sysAdmins.isEmpty()) {
            return sysAdmins.get(0);
        }
        return null;
    }

    public List<SysRole> loadRolesByAdminId(Integer adminId) {
        return sysRoleDao.loadByAdminId(adminId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始系统角色
        SysRoleExample roleExample = new SysRoleExample();
        roleExample.or().andCodeEqualTo("root");
        final List<SysRole> sysRoles = sysRoleMapper.selectByExample(roleExample);
        SysRole sysRole = null;
        if (sysRoles == null || sysRoles.isEmpty()) {
            sysRole = new SysRole();
            sysRole.setCode("root");
            sysRole.setName("超级管理员");
            sysRole.setDescription("超级管理员");
            sysRole.setStatus(1);
            sysRole.setPriority(0);
            sysRole.setCreateAt(new Date());
            sysRoleMapper.insert(sysRole);
        } else {
            sysRole = sysRoles.stream().filter(v -> v.getStatus().equals(1)).findAny().orElse(sysRoles.get(0));
        }

        // 初始系统用户
        List<SysAdmin> sysAdmins = sysAdminDao.loadRootSysAdmin(sysRole.getId(), "root");
        if (sysAdmins == null || sysAdmins.isEmpty()) {
            SysAdmin sysAdmin = new SysAdmin();
            sysAdmin.setUsername("root");
            sysAdmin.setNickname("Root");
            sysAdmin.setPassword(passwordEncoder.encode("root123"));
            sysAdminDao.insert(sysAdmin);

            SysAdminRoleRelation sysAdminRoleRelation = new SysAdminRoleRelation();
            sysAdminRoleRelation.setAdminId(sysAdmin.getId());
            sysAdminRoleRelation.setRoleId(sysRole.getId());
            sysAdminRoleRelationMapper.insert(sysAdminRoleRelation);
        }

        logger.info(" ***************** 系统用户检查完成 ***************** ");
    }
}
