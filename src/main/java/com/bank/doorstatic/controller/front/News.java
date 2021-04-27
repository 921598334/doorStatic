package com.bank.doorstatic.controller.front;

import com.bank.doorstatic.entity.MyPageInfo;
import com.bank.doorstatic.repository.basicList.NewsRepository;
import com.bank.doorstatic.service.newList.NewListService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@CrossOrigin("*")
@Slf4j
@Controller
public class News {



    @Autowired
    NewListService newListService;

    @Autowired
    NewsRepository newsRepository;


    @GetMapping("/news")
    public String news(Model model,
                       @RequestParam(name="page",defaultValue="" ) String page
                       ) {
        log.info("访问了 news");


        Integer pageNum = 1;
        if(!page.equals("")){
            pageNum = Integer.parseInt(page);
        }
        Integer perPage = 5;


        List<com.bank.doorstatic.entity.newList.News> result = newListService.queryFlows(pageNum, perPage, null,  null,  null,  null);
        Integer count = newListService.getCounts(null,  null,  null,  null);


        MyPageInfo myPageInfo = new MyPageInfo();

        myPageInfo.setPage(result);
        myPageInfo.setTotal(count);
        myPageInfo.setPerPage(perPage);
        Integer totalPage = (int) (Math.ceil(count/(perPage+0.0)));
        myPageInfo.setTotalPage(totalPage);
        myPageInfo.setCurrentPage(pageNum);
        //判断有没有下一页
        if(pageNum<totalPage){
            myPageInfo.setHasNext(true);
        }else {
            myPageInfo.setHasNext(false);
        }
        //判断有没有上一页
        if(pageNum>1){
            myPageInfo.setHasPre(true);
        }else {
            myPageInfo.setHasPre(false);
        }



        //读取p标签中的内容
        for(com.bank.doorstatic.entity.newList.News news:result){
            String content="";
            Document doc = Jsoup.parseBodyFragment(news.getContent());
            Elements element =doc.getElementsByTag("p");//得到第一个a标签内容

            for(Element e:element){
                content = content+e.ownText();
            }


            Integer size = 130;
            //如果长度超过100，需要截取前100
            if(content.length()>=size){
                content = content.substring(0,size-1)+"......";
            }

            news.setContent(content);

        }



        model.addAttribute("pageInfo",myPageInfo);



        return "news";
    }




    @GetMapping("/news_show")
    public String news_show(Model model,
                            @RequestParam(name="id",defaultValue="" ) String id) {
        log.info("访问了 news_show");


        com.bank.doorstatic.entity.newList.News result = newListService.queryFlows(0, 0, null, id, null, null).get(0);


        model.addAttribute("newPage",result);

        return "news_show";
    }



}
