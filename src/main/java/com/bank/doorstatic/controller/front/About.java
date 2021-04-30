package com.bank.doorstatic.controller.front;

import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.service.newList.NewListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@CrossOrigin("*")
@Slf4j
@Controller
public class About {


    @Autowired
    NewListService newListService;



    @GetMapping("/about")
    public String about(Model model) {
        log.info("访问了 about");



//        获取新闻简介
        Integer pageNum = 1;
        Integer perPage = 5;
        List<News> result = newListService.queryFlows(pageNum, perPage, null,  null,  null,  null);
        model.addAttribute("newList",result);



        return "about";
    }


    @GetMapping("/honor")
    public String honor(Model model) {
        log.info("访问了 honor");

        Integer pageNum = 1;
        Integer perPage = 5;
        List<News> result = newListService.queryFlows(pageNum, perPage, null,  null,  null,  null);
        model.addAttribute("newList",result);

        return "honor";
    }






}
