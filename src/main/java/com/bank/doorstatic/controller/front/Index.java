package com.bank.doorstatic.controller.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@Slf4j
@Controller
public class Index {





    @GetMapping("/index")
    public String index() {
        log.info("访问了index");
        return "index";
    }






}
