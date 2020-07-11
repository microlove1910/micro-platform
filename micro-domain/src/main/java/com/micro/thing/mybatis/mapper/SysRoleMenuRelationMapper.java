package com.micro.thing.mybatis.mapper;

import com.micro.thing.mybatis.model.SysRoleMenuRelation;
import com.micro.thing.mybatis.model.SysRoleMenuRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysRoleMenuRelationMapper {
    long countByExample(SysRoleMenuRelationExample example);

    int deleteByExample(SysRoleMenuRelationExample example);

    int insert(SysRoleMenuRelation record);

    int insertSelective(SysRoleMenuRelation record);

    List<SysRoleMenuRelation> selectByExample(SysRoleMenuRelationExample example);

    int updateByExampleSelective(@Param("record") SysRoleMenuRelation record, @Param("example") SysRoleMenuRelationExample example);

    int updateByExample(@Param("record") SysRoleMenuRelation record, @Param("example") SysRoleMenuRelationExample example);
}