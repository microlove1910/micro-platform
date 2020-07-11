package com.micro.thing.mybatis.mapper;

import com.micro.thing.mybatis.model.SysAdminPermissionRelation;
import com.micro.thing.mybatis.model.SysAdminPermissionRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysAdminPermissionRelationMapper {
    long countByExample(SysAdminPermissionRelationExample example);

    int deleteByExample(SysAdminPermissionRelationExample example);

    int insert(SysAdminPermissionRelation record);

    int insertSelective(SysAdminPermissionRelation record);

    List<SysAdminPermissionRelation> selectByExample(SysAdminPermissionRelationExample example);

    int updateByExampleSelective(@Param("record") SysAdminPermissionRelation record, @Param("example") SysAdminPermissionRelationExample example);

    int updateByExample(@Param("record") SysAdminPermissionRelation record, @Param("example") SysAdminPermissionRelationExample example);
}