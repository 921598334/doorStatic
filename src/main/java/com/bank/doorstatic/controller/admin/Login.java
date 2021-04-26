package com.bank.doorstatic.controller.admin;

import com.bank.doorstatic.entity.CurrentUser;
import com.bank.doorstatic.entity.LoginResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Slf4j
@RestController
public class Login {



    @PostMapping("/api/login/account")
    public LoginResult login(@RequestParam(name="username",defaultValue="" ) String username,
                             @RequestParam(name="password",defaultValue="" ) String password,
                             @RequestParam(name="type",defaultValue="" ) String type) {
        log.info("访问了 login");


        //判断用户名密码是否存在


        LoginResult loginResult = new LoginResult();
        loginResult.setCurrentAuthority("CurrentAuthority");
        loginResult.setStatus("ok");
        loginResult.setType("admin");



        //{"status":"error","type":"account","currentAuthority":"guest"}


        return loginResult;
    }





    @PostMapping("/api/login/outLogin")
    public void outLogin() {
        log.info("访问了 outLogin");


    }





    @GetMapping("/api/currentUser")
    public CurrentUser currentUser() {
        log.info("访问了 currentUser");


        CurrentUser currentUser = new CurrentUser();

        currentUser.setName("admin");
        currentUser.setAddress("重庆");
        currentUser.setUserid("1");


        return currentUser;
    }



}
