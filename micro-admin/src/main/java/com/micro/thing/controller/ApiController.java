package com.micro.thing.controller;

import com.micro.thing.mybatis.mapper.SysAdminMapper;
import com.micro.thing.mybatis.model.SysAdmin;
import com.micro.thing.utils.CommonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource
    SysAdminMapper sysAdminMapper;

    @GetMapping("/test")
    public String test() {
        final SysAdmin sysAdmin = sysAdminMapper.selectByPrimaryKey(1);
        return "托尔斯泰";
    }

    @GetMapping("/hello")
    public String hello() {
        return CommonUtils.hello();
    }

}
