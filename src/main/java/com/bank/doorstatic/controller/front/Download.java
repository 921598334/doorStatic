package com.bank.doorstatic.controller.front;

import com.alibaba.fastjson.JSON;
import com.bank.doorstatic.entity.MyPageInfo;
import com.bank.doorstatic.entity.sorftware.Sorftware;
import com.bank.doorstatic.entity.sorftware.SorftwareType;
import com.bank.doorstatic.model.FileInfo;
import com.bank.doorstatic.model.SorftwareDao;
import com.bank.doorstatic.model.SorftwareTypeDAO;
import com.bank.doorstatic.repository.sorftware.SorftwareRepository;
import com.bank.doorstatic.repository.sorftware.SorftwareTypeRepository;
import com.bank.doorstatic.service.sorftware.SorftwareListService;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@Slf4j
@Controller
public class Download {

    @Autowired
    SorftwareListService sorftwareListService;


    @Autowired
    SorftwareRepository sorftwareRepository;


    @Autowired
    SorftwareTypeRepository sorftwareTypeRepository;



    @GetMapping("/download")
    public String download(Model model,
                           @RequestParam(name="page",defaultValue="" ) String page,
                           @RequestParam(name="title",defaultValue="" ) String title,
                           @RequestParam(name="xxx",defaultValue="" ) String xxx,
                           @RequestParam(name="name",defaultValue="" ) String sorftwareTypeid) {

        log.info("访问了 download");

        Integer pageNum = 1;
        if(!page.equals("")){
            pageNum = Integer.parseInt(page);
        }
        Integer perPage = 10;




        List<Sorftware> resultTmp = sorftwareListService.queryFlows(pageNum, perPage, title,  null, null ,null,  null);

        Integer count = sorftwareListService.getCounts(title,  null, null, null,  null);



        MyPageInfo myPageInfo = new MyPageInfo();

        myPageInfo.setPage(resultTmp);
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


        model.addAttribute("pageInfo",myPageInfo);

        model.addAttribute("searchTitle",title);

        return "download";
    }








    @GetMapping("/download_show")
    public String download_show(Model model, @RequestParam(name="id",defaultValue="" ) String id ) {

        log.info("访问了 download_show");


        Sorftware resultTmp = sorftwareListService.queryFlows(1, 999, null,  id, null ,null,  null).get(0);

        List<FileInfo> fileInfo = JSON.parseArray(resultTmp.getFiles(),FileInfo.class);





        model.addAttribute("result",resultTmp);
        model.addAttribute("fileInfo",fileInfo);


        return "download_show";
    }



}
