package com.micro.thing.mybatis.mapper;

import com.micro.thing.mybatis.model.SpringSession;
import com.micro.thing.mybatis.model.SpringSessionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpringSessionMapper {
    long countByExample(SpringSessionExample example);

    int deleteByExample(SpringSessionExample example);

    int deleteByPrimaryKey(String primaryId);

    int insert(SpringSession record);

    int insertSelective(SpringSession record);

    List<SpringSession> selectByExample(SpringSessionExample example);

    SpringSession selectByPrimaryKey(String primaryId);

    int updateByExampleSelective(@Param("record") SpringSession record, @Param("example") SpringSessionExample example);

    int updateByExample(@Param("record") SpringSession record, @Param("example") SpringSessionExample example);

    int updateByPrimaryKeySelective(SpringSession record);

    int updateByPrimaryKey(SpringSession record);
}