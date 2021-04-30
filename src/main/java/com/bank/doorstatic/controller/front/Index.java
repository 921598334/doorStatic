package com.bank.doorstatic.controller.front;

import com.bank.doorstatic.entity.CurrentUser;
import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.model.ErrorInfoStructure;
import com.bank.doorstatic.model.NewsDAO;
import com.bank.doorstatic.service.newList.NewListService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@Slf4j
@Controller
public class Index {


    @Autowired
    NewListService newListService;


    @GetMapping("/index")
    public String index(Model model) {
        log.info("访问了index");


//        获取新闻简介
        List<News> resultTmp = newListService.queryFlows(1, 3, null,  null,  null,  null);

        List<NewsDAO> result = new ArrayList<>();


        //读取p标签中的内容
        for(News news:resultTmp){

            NewsDAO newsDAO = new NewsDAO();

            BeanUtils.copyProperties(news,newsDAO);

            String content="";
            Document doc = Jsoup.parseBodyFragment(news.getContent());
            Elements element =doc.getElementsByTag("p");

            Elements elementImgs =doc.getElementsByTag("img");

            //如果存在图像
            if(elementImgs!=null && elementImgs.size()>0){
                newsDAO.setImgCode(elementImgs.get(0).attr("src"));
            }

            //获取p标签
            for(Element e:element){
                content = content+e.ownText();
            }


            Integer size = 130;
            //如果长度超过100，需要截取前100
            if(content.length()>=size){
                content = content.substring(0,size-1)+"......";
            }

            newsDAO.setContent(content);

            result.add(newsDAO);
        }







        model.addAttribute("newList",result);


        return "index";
    }







    @GetMapping("/")
    public Object admin() {
        log.info("访问了 admin");

        return "admin/index";

    }




}
