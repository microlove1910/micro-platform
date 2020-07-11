package com.micro.thing.entity;

import com.micro.thing.mybatis.model.SysMenu;
import com.micro.thing.mybatis.model.SysRole;

import java.util.List;

public class SysRoleEntity extends SysRole {

    private List<SysMenu> menus;

    public List<SysMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<SysMenu> menus) {
        this.menus = menus;
    }
}
