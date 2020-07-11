package com.micro.thing.entity;

import com.micro.thing.mybatis.model.SysMenu;

import java.util.List;

public class SysMenuEntity extends SysMenu {

    private List<SysMenuEntity> children;

    public List<SysMenuEntity> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuEntity> children) {
        this.children = children;
    }
}
