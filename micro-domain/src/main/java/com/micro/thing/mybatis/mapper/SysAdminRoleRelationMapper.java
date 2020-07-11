package com.micro.thing.mybatis.mapper;

import com.micro.thing.mybatis.model.SysAdminRoleRelation;
import com.micro.thing.mybatis.model.SysAdminRoleRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysAdminRoleRelationMapper {
    long countByExample(SysAdminRoleRelationExample example);

    int deleteByExample(SysAdminRoleRelationExample example);

    int insert(SysAdminRoleRelation record);

    int insertSelective(SysAdminRoleRelation record);

    List<SysAdminRoleRelation> selectByExample(SysAdminRoleRelationExample example);

    int updateByExampleSelective(@Param("record") SysAdminRoleRelation record, @Param("example") SysAdminRoleRelationExample example);

    int updateByExample(@Param("record") SysAdminRoleRelation record, @Param("example") SysAdminRoleRelationExample example);
}