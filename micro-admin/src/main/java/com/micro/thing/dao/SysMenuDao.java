package com.micro.thing.dao;

import com.micro.thing.entity.SysMenuEntity;
import com.micro.thing.mybatis.mapper.SysMenuMapper;
import com.micro.thing.mybatis.model.SysMenu;

import java.util.List;

public interface SysMenuDao extends SysMenuMapper {

    List<SysMenu> loadByRoleId(Integer roleId);

    List<SysMenuEntity> loadByAdminId(Integer adminId);

    List<SysMenuEntity> loadAllMenus();

}
