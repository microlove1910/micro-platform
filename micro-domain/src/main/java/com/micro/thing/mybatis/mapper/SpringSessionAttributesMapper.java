package com.micro.thing.mybatis.mapper;

import com.micro.thing.mybatis.model.SpringSessionAttributes;
import com.micro.thing.mybatis.model.SpringSessionAttributesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpringSessionAttributesMapper {
    long countByExample(SpringSessionAttributesExample example);

    int deleteByExample(SpringSessionAttributesExample example);

    int deleteByPrimaryKey(@Param("sessionPrimaryId") String sessionPrimaryId, @Param("attributeName") String attributeName);

    int insert(SpringSessionAttributes record);

    int insertSelective(SpringSessionAttributes record);

    List<SpringSessionAttributes> selectByExampleWithBLOBs(SpringSessionAttributesExample example);

    List<SpringSessionAttributes> selectByExample(SpringSessionAttributesExample example);

    SpringSessionAttributes selectByPrimaryKey(@Param("sessionPrimaryId") String sessionPrimaryId, @Param("attributeName") String attributeName);

    int updateByExampleSelective(@Param("record") SpringSessionAttributes record, @Param("example") SpringSessionAttributesExample example);

    int updateByExampleWithBLOBs(@Param("record") SpringSessionAttributes record, @Param("example") SpringSessionAttributesExample example);

    int updateByExample(@Param("record") SpringSessionAttributes record, @Param("example") SpringSessionAttributesExample example);

    int updateByPrimaryKeySelective(SpringSessionAttributes record);

    int updateByPrimaryKeyWithBLOBs(SpringSessionAttributes record);
}