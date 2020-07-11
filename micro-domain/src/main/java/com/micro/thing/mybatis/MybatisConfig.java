package com.micro.thing.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.micro.thing.mybatis", "com.micro.thing.dao"})
public class MybatisConfig {
}
