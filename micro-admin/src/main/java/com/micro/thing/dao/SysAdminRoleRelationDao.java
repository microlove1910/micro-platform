package com.micro.thing.dao;

import com.micro.thing.mybatis.mapper.SysAdminRoleRelationMapper;
import com.micro.thing.mybatis.model.SysAdminRoleRelation;

import java.util.List;

public interface SysAdminRoleRelationDao extends SysAdminRoleRelationMapper {

    int insertBatch(List<SysAdminRoleRelation> sysAdminRoleRelations);

}
