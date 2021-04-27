package com.bank.doorstatic.controller.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin("*")
@Slf4j
@Controller
public class Contact {





    @GetMapping("/contact")
    public String contact() {
        log.info("访问了 contact");
        return "contact";
    }






}
