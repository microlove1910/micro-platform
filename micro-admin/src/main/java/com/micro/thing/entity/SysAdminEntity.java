package com.micro.thing.entity;

import com.micro.thing.mybatis.model.SysAdmin;
import com.micro.thing.mybatis.model.SysRole;

import java.util.List;

public class SysAdminEntity extends SysAdmin {

    public List<SysRole> roles;

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

}
