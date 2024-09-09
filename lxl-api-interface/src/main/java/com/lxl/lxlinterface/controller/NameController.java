package com.lxl.lxlinterface.controller;

import cn.hutool.http.HttpRequest;
import com.lxl.lxlinterface.model.User;
import com.lxl.lxlinterface.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/name")
    public String getNameByGet(@RequestParam String name){

        return "Get 你的名字是"+name;
    }

    @PostMapping ("/")
    public String getNameByPost(@RequestParam String name){
        return "Post 你的名字是"+name;
    }

    @PostMapping("/user")
    public String getUserNameByGet(@RequestBody User user, HttpServletRequest request) {

        //校验
        //ak
        String accessKey = request.getHeader("accessKey");
        // 时间戳
        String timestamp = request.getHeader("timestamp");
        // 随机数
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        // 用户
        String us = request.getHeader("user");

        //todo 检查用户是否有ak

        System.out.println(request);
        return "Post 你的名字是" + user.getName();
    }

}
