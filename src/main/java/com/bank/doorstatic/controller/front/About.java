package com.bank.doorstatic.controller.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin("*")
@Slf4j
@Controller
public class About {





    @GetMapping("/about")
    public String about() {
        log.info("访问了 about");
        return "about";
    }


    @GetMapping("/honor")
    public String honor() {
        log.info("访问了 honor");
        return "honor";
    }






}
