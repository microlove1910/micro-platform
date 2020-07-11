package com.micro.thing.security;

import com.micro.thing.mybatis.model.SysAdmin;
import com.micro.thing.mybatis.model.SysRole;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SysAdminDetails extends SysAdmin implements UserDetails {

    private final List<SysRole> sysRoles;

    public SysAdminDetails(SysAdmin sysAdmin, List<SysRole> sysRoles) {
        BeanUtils.copyProperties(sysAdmin, this);
        this.sysRoles = sysRoles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        if (this.sysRoles != null) {
            sysRoles.forEach(v -> list.add(new SimpleGrantedAuthority(v.getCode())));
        }
        return list;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
