package com.bank.doorstatic.controller.admin;

import com.bank.doorstatic.entity.CurrentUser;
import com.bank.doorstatic.entity.LoginResult;
import com.bank.doorstatic.model.ErrorInfoStructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.spi.http.HttpContext;
import java.util.Map;

@CrossOrigin("*")
@Slf4j
@RestController
public class Login {


    private String userName=null;

    @PostMapping("/api/login/account")
    public Object login(@RequestBody Object paramTmp,HttpServletResponse response,HttpServletRequest request,HttpSession session) {
        log.info("访问了 login");


        //判断用户名密码是否存在
        Map<String, String> params = (Map<String, String>) paramTmp;


        if(params.get("password")==null || params.get("password").equals("")){


            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setErrorCode("500");
            errorInfoStructure.setErrorMessage("密码不能为空");
            errorInfoStructure.setShowType(2);
            errorInfoStructure.setSuccess(false);

            return  errorInfoStructure;
        }
        if(params.get("username")==null || params.get("username").equals("")){

            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setErrorCode("500");
            errorInfoStructure.setErrorMessage("用户名不能为空");
            errorInfoStructure.setShowType(2);
            errorInfoStructure.setSuccess(false);

            return  errorInfoStructure;
        }


        if(params.get("username").equals("admin") && params.get("password").equals("123")){
            LoginResult loginResult = new LoginResult();
            loginResult.setCurrentAuthority("CurrentAuthority");
            loginResult.setStatus("ok");
            loginResult.setType("admin");

//            request.getSession().setAttribute("userInfo",params.get("username"));
//            session.setAttribute("xxxx","xxxx");

            userName = params.get("username");

            return loginResult;
        }else{
            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setErrorCode("500");
            errorInfoStructure.setErrorMessage("密码不正确");
            errorInfoStructure.setShowType(2);
            errorInfoStructure.setSuccess(false);

            return  errorInfoStructure;
        }


    }





    @PostMapping("/api/login/outLogin")
    public void outLogin(HttpServletResponse response,HttpServletRequest request,HttpSession session) {
        log.info("访问了 outLogin");

        userName = null;

        //request.getSession().removeAttribute("userInfo");

    }





    @GetMapping("/api/currentUser")
    public Object currentUser(HttpServletResponse response,HttpServletRequest request,HttpSession session) {
        log.info("访问了 currentUser");


        if(userName!=null){
            CurrentUser currentUser = new CurrentUser();
            //currentUser.setName(request.getSession().getAttribute("userInfo").toString());
            currentUser.setName(userName);

            currentUser.setAddress("重庆");
            currentUser.setUserid("1");

            return currentUser;
        }else{
            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setErrorCode("500");
            errorInfoStructure.setErrorMessage("当前用户session过期，请重新登录");
            errorInfoStructure.setShowType(2);
            errorInfoStructure.setSuccess(false);

            return errorInfoStructure;
        }
    }









}
