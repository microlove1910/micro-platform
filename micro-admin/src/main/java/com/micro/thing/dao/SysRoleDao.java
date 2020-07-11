package com.micro.thing.dao;

import com.micro.thing.mybatis.mapper.SysRoleMapper;
import com.micro.thing.mybatis.model.SysRole;

import java.util.List;

public interface SysRoleDao extends SysRoleMapper {

    List<SysRole> loadByAdminId(Integer adminId);

}
