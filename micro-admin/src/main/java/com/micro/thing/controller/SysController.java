package com.micro.thing.controller;

import com.github.pagehelper.PageHelper;
import com.micro.thing.dao.*;
import com.micro.thing.entity.SysAdminEntity;
import com.micro.thing.entity.SysRoleEntity;
import com.micro.thing.mybatis.mapper.SysAdminRoleRelationMapper;
import com.micro.thing.mybatis.model.*;
import com.micro.thing.service.SysAdminService;
import com.micro.thing.util.Msg;
import com.micro.thing.util.MsgResponse;
import com.micro.thing.util.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@MsgResponse
public class SysController extends BaseController {

    @Autowired
    SysAdminService sysAdminService;

    @Resource
    SysMenuDao sysMenuDao;

    @Resource
    SysRoleDao sysRoleDao;

    @Resource
    SysAdminRoleRelationMapper sysAdminRoleRelationMapper;

    @Resource
    SysRoleMenuRelationDao sysRoleMenuRelationDao;

    @Resource
    SysAdminDao sysAdminDao;

    @Resource
    SysAdminRoleRelationDao sysAdminRoleRelationDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public Msg info(@SessionAttribute("adminId") Integer adminId) {
        SysAdmin sysAdmin = sysAdminService.loadById(adminId);
        final List<SysRole> sysRoles = sysAdminService.loadRolesByAdminId(adminId);

        Map<String, Object> data = new HashMap<>();
        data.put("username", sysAdmin.getUsername());
        data.put("nickname", sysAdmin.getNickname());
        data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        data.put("roles", sysRoles);
        data.put("menus", sysMenuDao.loadByAdminId(adminId));
        data.put("permissions", null);

        return Msg.success(data);
    }

    @GetMapping("/roles/exists")
    public Object rolesExists(@RequestParam String code, @RequestParam(required = false) Integer id) {
        final SysRoleExample roleExample = new SysRoleExample();
        final SysRoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andCodeEqualTo(code);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        boolean exists = sysRoleDao.countByExample(roleExample) > 0;
        return Msg.success(exists);
    }

    @GetMapping("/roles/all")
    public Object roles() {
        return sysRoleDao.selectByExample(new SysRoleExample());
    }

    @GetMapping("/roles/list")
    public Object roles(@RequestParam(required = false) Integer page,
                        @RequestParam(required = false) Integer size) {
        PageHelper.startPage(page, size);
        final List<SysRole> sysRoles = sysRoleDao.selectByExample(new SysRoleExample());
        return PageVo.of(sysRoles);
    }

    @GetMapping("/roles/{id}")
    public Object roles(@PathVariable Integer id) {
        final SysRole sysRole = sysRoleDao.selectByPrimaryKey(id);
        if (sysRole == null) {
            return Msg.error(404, "id错误，没有该角色");
        }
        SysRoleEntity sysRoleEntity = new SysRoleEntity();
        BeanUtils.copyProperties(sysRole, sysRoleEntity);
        if ("root".equalsIgnoreCase(sysRole.getCode())) {
            final List<SysMenu> sysMenus = sysMenuDao.selectByExample(new SysMenuExample());
            sysRoleEntity.setMenus(sysMenus);
        } else {
            final List<SysMenu> sysMenus = sysMenuDao.loadByRoleId(id);
            sysRoleEntity.setMenus(sysMenus);
        }
        return sysRoleEntity;
    }

    @PostMapping("/roles")
    public Object roles(@RequestBody SysRoleEntity sysRoleEntity) {
        int result = 0;
        if (sysRoleEntity.getId() != null) {
            result = sysRoleDao.updateByPrimaryKeySelective(sysRoleEntity);
        } else {
            result = sysRoleDao.insertSelective(sysRoleEntity);
        }
        if (sysRoleEntity.getMenus() != null) {
            final Integer id = sysRoleEntity.getId();
            final List<SysMenu> sysMenus = sysRoleEntity.getMenus();
            final List<SysRoleMenuRelation> sysRoleMenuRelations = sysMenus.stream().map(v -> {
                SysRoleMenuRelation sysRoleMenuRelation = new SysRoleMenuRelation();
                sysRoleMenuRelation.setRoleId(id);
                sysRoleMenuRelation.setMenuId(v.getId());
                return sysRoleMenuRelation;
            }).collect(Collectors.toList());

            final SysRoleMenuRelationExample sysRoleMenuRelationExample = new SysRoleMenuRelationExample();
            sysRoleMenuRelationExample.or().andRoleIdEqualTo(id);
            sysRoleMenuRelationDao.deleteByExample(sysRoleMenuRelationExample);

            if (!sysRoleMenuRelations.isEmpty()) {
                sysRoleMenuRelationDao.insertBatch(sysRoleMenuRelations);
            }
        }
        return Msg.success(result > 0);
    }

    @DeleteMapping("/roles")
    public Object roles(@RequestParam Integer[] ids) {
        final List<Integer> values = Arrays.asList(ids);

        // 删除角色跟菜单关联
        SysRoleMenuRelationExample sysRoleMenuRelationExample = new SysRoleMenuRelationExample();
        sysRoleMenuRelationExample.or().andRoleIdIn(values);
        sysRoleMenuRelationDao.deleteByExample(sysRoleMenuRelationExample);

        // 删除用户跟角色关联
        SysAdminRoleRelationExample sysAdminRoleRelationExample = new SysAdminRoleRelationExample();
        sysAdminRoleRelationExample.or().andRoleIdIn(values);
        sysAdminRoleRelationMapper.deleteByExample(sysAdminRoleRelationExample);

        // 删除角色
        SysRoleExample roleExample = new SysRoleExample();
        roleExample.or().andIdIn(values);
        sysRoleDao.deleteByExample(roleExample);

        return true;
    }

    @GetMapping("/admins/list")
    public Object admins(@RequestParam(required = false) Integer page,
                         @RequestParam(required = false) Integer size) {
        PageHelper.startPage(page, size);
        final List<SysAdmin> sysAdmins = sysAdminDao.loadList();
        return PageVo.of(sysAdmins);
    }

    @GetMapping("/admins/exists")
    public Object admins(@RequestParam String username, @RequestParam(required = false) Integer id) {
        final SysAdminExample sysAdminExample = new SysAdminExample();
        final SysAdminExample.Criteria criteria = sysAdminExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        return sysAdminDao.countByExample(sysAdminExample) > 0;
    }

    @GetMapping("/admins/{id}")
    public Object admins(@PathVariable Integer id) {
        final SysAdmin sysAdmin = sysAdminDao.selectByPrimaryKey(id);
        if (sysAdmin == null) {
            return Msg.error(404, "该账号已删除");
        }
        SysAdminEntity sysAdminEntity = new SysAdminEntity();
        BeanUtils.copyProperties(sysAdmin, sysAdminEntity);
        sysAdminEntity.setPassword(null);
        final List<SysRole> sysRoles = sysRoleDao.loadByAdminId(sysAdmin.getId());
        sysAdminEntity.setRoles(sysRoles);
        return sysAdminEntity;
    }

    @PostMapping("/admins")
    public Object admins(@RequestBody SysAdminEntity sysAdminEntity) {
        if (sysAdminEntity.getId() != null) {
            sysAdminEntity.setCreateAt(null);
            if (sysAdminEntity.getPassword() != null && !sysAdminEntity.getPassword().isBlank()) {
                sysAdminEntity.setPassword(passwordEncoder.encode(sysAdminEntity.getPassword()));
            }
            sysAdminDao.updateByPrimaryKeySelective(sysAdminEntity);
        } else {
            sysAdminDao.insertSelective(sysAdminEntity);
        }

        if (sysAdminEntity.getRoles() != null) {
            // 更新用户角色关联表
            final List<SysAdminRoleRelation> sysAdminRoleRelations = sysAdminEntity.getRoles().stream().map(v -> {
                SysAdminRoleRelation sysAdminRoleRelation = new SysAdminRoleRelation();
                sysAdminRoleRelation.setRoleId(v.getId());
                sysAdminRoleRelation.setAdminId(sysAdminEntity.getId());
                return sysAdminRoleRelation;
            }).collect(Collectors.toList());

            // 1.删除原有关联
            final SysAdminRoleRelationExample sysAdminRoleRelationExample = new SysAdminRoleRelationExample();
            sysAdminRoleRelationExample.or().andAdminIdEqualTo(sysAdminEntity.getId());
            sysAdminRoleRelationDao.deleteByExample(sysAdminRoleRelationExample);

            // 2.插入新的关联
            if (!sysAdminRoleRelations.isEmpty()) {
                sysAdminRoleRelationDao.insertBatch(sysAdminRoleRelations);
            }
        }
        return true;
    }

    @DeleteMapping("/admins")
    public Object admins(@RequestParam Integer[] ids) {
        SysAdminExample sysAdminExample = new SysAdminExample();
        final SysAdminExample.Criteria or = sysAdminExample.or();
        or.andIdIn(Arrays.asList(ids));
        return sysAdminDao.deleteByExample(sysAdminExample);
    }

    @GetMapping("/menus/all")
    public Object allMenus() {
        return sysMenuDao.loadAllMenus();
    }

    @PostMapping("/menus")
    public Object menus(@RequestBody SysMenu sysMenu) {
        if (sysMenu.getId() != null) {
            sysMenuDao.updateByPrimaryKey(sysMenu);
        } else {
            sysMenuDao.insertSelective(sysMenu);
        }
        return true;
    }

    @DeleteMapping("/menus")
    public Object menus(@RequestParam Integer id) {
        SysMenu sysMenu = sysMenuDao.selectByPrimaryKey(id);
        if (sysMenu == null) {
            return Msg.error(404, "该菜单不存在");
        }

        // 删除角色关联的菜单
        SysRoleMenuRelationExample sysRoleMenuRelationExample = new SysRoleMenuRelationExample();
        sysRoleMenuRelationExample.or().andMenuIdEqualTo(id);
        sysRoleMenuRelationDao.deleteByExample(sysRoleMenuRelationExample);

        // 删除二级菜单
        if (sysMenu.getParentId() == null) {
            // 查找二级菜单
            SysMenuExample sysMenuExample = new SysMenuExample();
            sysMenuExample.or().andParentIdEqualTo(sysMenu.getId());

            // 二级菜单
            final List<SysMenu> sysMenus = sysMenuDao.selectByExample(sysMenuExample);
            final List<Integer> ids = sysMenus.stream().map(SysMenu::getId).collect(Collectors.toList());

            // 删除角色关联的二级菜单
            sysRoleMenuRelationExample.clear();
            sysRoleMenuRelationExample.or().andMenuIdIn(ids);
            sysRoleMenuRelationDao.deleteByExample(sysRoleMenuRelationExample);

            // 正式删除二级菜单
            sysMenuExample.clear();
            sysMenuExample.or().andIdIn(ids);
            sysMenuDao.deleteByExample(sysMenuExample);
        }
        // 删除自己
        sysMenuDao.deleteByPrimaryKey(sysMenu.getId());
        return true;
    }
}
