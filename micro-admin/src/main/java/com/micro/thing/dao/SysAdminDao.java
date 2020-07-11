package com.micro.thing.dao;

import com.micro.thing.mybatis.mapper.SysAdminMapper;
import com.micro.thing.mybatis.model.SysAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAdminDao extends SysAdminMapper {

    List<SysAdmin> loadRootSysAdmin(@Param("roleId") Integer roleId, @Param("username") String username);

    List<SysAdmin> loadList();
}
