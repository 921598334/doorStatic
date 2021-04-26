package com.bank.doorstatic.controller.admin;

import com.alibaba.fastjson.JSON;
import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.entity.sorftware.SorftwareType;
import com.bank.doorstatic.model.*;
import com.bank.doorstatic.repository.basicList.NewsRepository;
import com.bank.doorstatic.repository.sorftware.SorftwareTypeRepository;
import com.bank.doorstatic.service.newList.NewListService;
import com.bank.doorstatic.service.sorftware.SorftwareTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin("*")
@Slf4j
@RestController
public class TypeListController {



    @Autowired
    SorftwareTypeService sorftwareTypeService;


    @Autowired
    SorftwareTypeRepository sorftwareTypeRepository ;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @GetMapping("/typeList/api/getPage")
    public Object getSoftwareTypePage(@RequestParam(name="page",defaultValue="" ) String page,
                               @RequestParam(name="per_page",defaultValue="" ) String per_page,
                              @RequestParam(name="id",defaultValue="" ) String id,
                              @RequestParam(name="name",defaultValue="" ) String name



    ) throws ParseException {
        log.info("访问了 getSoftwareTypePage");

        Integer pageNum = 0;
        if(!page.equals("")){
            pageNum = Integer.parseInt(page);
        }
        Integer perPage = 0;
        if(!per_page.equals("")){
            perPage = Integer.parseInt(per_page);
        }



        List<SorftwareType> resultTmp = sorftwareTypeService.queryFlows(pageNum, perPage, name,  id);
        Integer count = sorftwareTypeService.getCounts(name,  id);

        List<SorftwareTypeDAO> result = new ArrayList<>();
        for(SorftwareType sorftwareType:resultTmp){
            SorftwareTypeDAO sorftwareTypeDAO = new SorftwareTypeDAO();
            BeanUtils.copyProperties(sorftwareType,sorftwareTypeDAO);
            result.add(sorftwareTypeDAO);
        }



        MetaDAO metaDAO = new MetaDAO();
        metaDAO.setPage(pageNum);
        metaDAO.setPer_page(perPage);
        metaDAO.setTotal(count);

        DataSource dataSource = new DataSource();
        dataSource.setMeta(metaDAO);
        dataSource.setDatasource(result);


        ReturnData returnData = new ReturnData();
        returnData.setData(dataSource);



        //不知道为什么如果不手动转为json，前端会出错
        String str = JSON.toJSONString(returnData);

        return str;
    }





    //不知道为什么post请求用通常方法无法接收参数
    @PostMapping("/typeList/api/add")
    public Object addSoftwareTyp(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 addSoftwareTyp");

        Map<String, String> params = (Map<String, String>) paramTmp;


        if(params.get("name")==null || params.get("name").equals("")){



            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setErrorCode("500");
            errorInfoStructure.setErrorMessage("类型名称不能为空");
            errorInfoStructure.setShowType(2);
            errorInfoStructure.setSuccess(false);

            return  errorInfoStructure;
        }





        SorftwareType sorftwareType = new SorftwareType();
        sorftwareType.setName(params.get("name"));

        sorftwareTypeRepository.save(sorftwareType);

        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("成功添加");
        returnData.setData(dataSource);

        return returnData;
    }


    @PostMapping("/typeList/api/edit")
    public Object editSoftwareTyp(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 editSoftwareTyp");

        Map<String, Object> params = (Map<String, Object>) paramTmp;



        if(params.get("id")==null || (params.get("id")+"").equals("")){
            throw  new Exception("id不能为空");
        }
        if(params.get("name")==null || (params.get("name")+"").equals("")){
            throw  new Exception("标题不能为空");
        }



        SorftwareType oldSorftwareType = sorftwareTypeRepository.findById(Integer.parseInt(params.get("id")+"")).get();

        oldSorftwareType.setName(params.get("name")+"");

        sorftwareTypeRepository.save(oldSorftwareType);



        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("更新成功");
        returnData.setData(dataSource);

        return returnData;
    }



    @PostMapping("/typeList/api/delete")
    public Object deleteSoftwareTyp(@RequestBody Object paramTmp) throws Exception {

        log.info("访问了 deleteSoftwareTyp");

        Map<String, Object> params = (Map<String, Object>) paramTmp;

        List<Integer> ids = (List<Integer>)params.get("ids");


        try{

            sorftwareTypeRepository.deleteBatch(ids);

            ReturnData returnData = new ReturnData();
            DataSource dataSource = new DataSource();
            returnData.setData(dataSource);
            dataSource.setMessage("删除成功");

            return returnData;

        }catch (Exception e){

            e.printStackTrace();

            ErrorInfoStructure errorInfoStructure = new ErrorInfoStructure();
            errorInfoStructure.setSuccess(false);
            errorInfoStructure.setErrorMessage("删除失败，该类型下存在其他软件，请删除该类型下的所有软件后在尝试删除");
            return errorInfoStructure;
        }



    }






}
