package com.micro.thing.dao;

import com.micro.thing.mybatis.mapper.SysRoleMenuRelationMapper;
import com.micro.thing.mybatis.model.SysRoleMenuRelation;

import java.util.List;

public interface SysRoleMenuRelationDao extends SysRoleMenuRelationMapper {

     int insertBatch(List<SysRoleMenuRelation> sysRoleMenuRelations);

}

