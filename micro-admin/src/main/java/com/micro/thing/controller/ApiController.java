package com.micro.thing.controller;

import com.micro.thing.utils.CommonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/test")
    public String test() {
        return "{\"code\": 200, \"msg\": \"ok\"}";
    }

    @GetMapping("/hello")
    public String hello() {
        return CommonUtils.hello();
    }

}
