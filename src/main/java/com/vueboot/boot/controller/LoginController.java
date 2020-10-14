package com.vueboot.boot.controller;

import com.vueboot.boot.pojo.User;
import com.vueboot.boot.result.Result;
import com.vueboot.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Objects;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "api/login")
    public Result login(@RequestBody User requestUser) {
        // 对html标签进行逐一，放置XSS攻击
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

//        if(!Objects.equals("admin",username)||!Objects.equals("123456",requestUser.getPassword())){
//            String message = "账号密码错误";
//            System.out.println("test");
//            return new Result(400);
        User user = userService.get(username, requestUser.getPassword());
        if(null == user){
            return new Result(400);
        }else{
            return new Result(200);
        }
    }
}
