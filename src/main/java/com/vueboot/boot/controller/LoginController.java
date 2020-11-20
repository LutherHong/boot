package com.vueboot.boot.controller;

import com.vueboot.boot.pojo.User;
import com.vueboot.boot.result.Result;
import com.vueboot.boot.result.ResultFactory;
import com.vueboot.boot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping(value = "api/login")
    public Result login(@RequestBody User requestUser, HttpSession session) {
        // 对html标签进行转义，防止XSS攻击（评论的时候写成恶意js，然后后端不处理返回前端）
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

////        if(!Objects.equals("admin",username)||!Objects.equals("123456",requestUser.getPassword())){
////            String message = "账号密码错误";
////            System.out.println("test");
////            return new Result(400);
//        User user = userService.get(username, requestUser.getPassword());
//        if(null == user){
//            return new Result(400);
//        }else{
//            session.setAttribute("user",user);
//            return new Result(200);
//        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());

        try {
            subject.login(usernamePasswordToken);
            return ResultFactory.buildSuccessResult(username);
        }catch (AuthenticationException e){
            return ResultFactory.buildFailResult("账号密码错误");
        }

    }

    @ResponseBody
    @GetMapping("api/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessResult(message);
    }

    @PostMapping("api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "用户名已被使用";
            return ResultFactory.buildFailResult(message);
        }

        // 生成盐,默认长度 16 位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);

        return ResultFactory.buildSuccessResult(user);
    }
}
