package com.micro.thing.mybatis.mapper;

import com.micro.thing.mybatis.model.SysRolePermissionRelation;
import com.micro.thing.mybatis.model.SysRolePermissionRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysRolePermissionRelationMapper {
    long countByExample(SysRolePermissionRelationExample example);

    int deleteByExample(SysRolePermissionRelationExample example);

    int insert(SysRolePermissionRelation record);

    int insertSelective(SysRolePermissionRelation record);

    List<SysRolePermissionRelation> selectByExample(SysRolePermissionRelationExample example);

    int updateByExampleSelective(@Param("record") SysRolePermissionRelation record, @Param("example") SysRolePermissionRelationExample example);

    int updateByExample(@Param("record") SysRolePermissionRelation record, @Param("example") SysRolePermissionRelationExample example);
}