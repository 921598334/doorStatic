package com.bank.doorstatic.controller.admin;

import com.bank.doorstatic.model.DataSource;
import com.bank.doorstatic.model.ErrorInfoStructure;
import com.bank.doorstatic.model.MetaDAO;
import com.bank.doorstatic.model.ReturnData;
import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.repository.basicList.NewsRepository;
import com.bank.doorstatic.service.newList.NewListService;
import com.sun.tools.javac.comp.Flow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin("*")
@Slf4j
@RestController
public class NewListController {



    @Autowired
    NewListService newListService;

    @Autowired
    NewsRepository newsRepository;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @GetMapping("/newList/api/getPage")
    public ReturnData getPage(@RequestParam(name="page",defaultValue="" ) String page,
                               @RequestParam(name="per_page",defaultValue="" ) String per_page,
                              @RequestParam(name="id",defaultValue="" ) String id,
                              @RequestParam(name="title",defaultValue="" ) String title,
                              @RequestParam(name="create_time",defaultValue="" ) String create_time


    ) throws ParseException {
        log.info("访问了 getPage");

        Integer pageNum = 0;
        if(!page.equals("")){
            pageNum = Integer.parseInt(page);
        }
        Integer perPage = 0;
        if(!per_page.equals("")){
            perPage = Integer.parseInt(per_page);
        }


        Date createTimeStart = null;
        Date createTimeEnd = null;

        if(!create_time.equals("")){
            createTimeStart =   simpleDateFormat.parse(create_time.split(",")[0]) ;
            createTimeEnd =   simpleDateFormat.parse(create_time.split(",")[1]) ;

        }


        List<News> result = newListService.queryFlows(pageNum, perPage, title,  id,  createTimeStart,  createTimeEnd);



        Integer count = newListService.getCounts(title,  id,  createTimeStart,  createTimeEnd);


        MetaDAO metaDAO = new MetaDAO();
        metaDAO.setPage(pageNum);
        metaDAO.setPer_page(perPage);
        metaDAO.setTotal(count);

        DataSource dataSource = new DataSource();
        dataSource.setMeta(metaDAO);
        dataSource.setDatasource(result);


        ReturnData returnData = new ReturnData();
        returnData.setData(dataSource);


        return returnData;
    }





    //不知道为什么post请求用通常方法无法接收参数
    @PostMapping("/newList/api/add")
    public Object addPage(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 addPage");

        Map<String, String> params = (Map<String, String>) paramTmp;


        if(params.get("title")==null || params.get("title").equals("")){



            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setErrorCode("500");
            errorInfoStructure.setErrorMessage("标题不能为空");
            errorInfoStructure.setShowType(2);
            errorInfoStructure.setSuccess(false);

            return  errorInfoStructure;
        }
        if(params.get("content")==null || params.get("content").equals("")){

            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setErrorCode("500");
            errorInfoStructure.setErrorMessage("内容不能为空");
            errorInfoStructure.setShowType(2);
            errorInfoStructure.setSuccess(false);

        }


        News news = new News();
        news.setTitle(params.get("title"));
        news.setContent(params.get("content"));
        news.setCreate_time(new Date(System.currentTimeMillis()));

        newsRepository.save(news);

        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("成功添加");
        returnData.setData(dataSource);

        return returnData;
    }


    @PostMapping("/newList/api/edit")
    public ReturnData editPage(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 editPage");

        Map<String, Object> params = (Map<String, Object>) paramTmp;



        if(params.get("id")==null || (params.get("id")+"").equals("")){
            throw  new Exception("id不能为空");
        }
        if(params.get("title")==null || (params.get("title")+"").equals("")){
            throw  new Exception("标题不能为空");
        }
        if(params.get("content")==null || (params.get("content")+"").equals("")){
            throw  new Exception("内容不能为空");
        }


        News oldNiews = newsRepository.findById(Integer.parseInt(params.get("id")+"")).get();

        oldNiews.setTitle(params.get("title")+"");
        oldNiews.setContent(params.get("content")+"");


        newsRepository.save(oldNiews);



        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("更新成功");
        returnData.setData(dataSource);

        return returnData;
    }






    @PostMapping("/newList/api/delete")
    public ReturnData deletePage(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 deletePage");

        Map<String, Object> params = (Map<String, Object>) paramTmp;

        List<Integer> ids = (List<Integer>)params.get("ids");


        newsRepository.deleteBatch(ids);

       // newsRepository.deleteById(Integer.parseInt(params.get("id")+""));


        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("删除成功");
        returnData.setData(dataSource);

        return returnData;

    }






}
