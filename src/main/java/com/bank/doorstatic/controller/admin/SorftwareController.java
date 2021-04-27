package com.bank.doorstatic.controller.admin;

import com.alibaba.fastjson.JSON;
import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.entity.sorftware.Sorftware;
import com.bank.doorstatic.entity.sorftware.SorftwareType;
import com.bank.doorstatic.model.*;
import com.bank.doorstatic.repository.basicList.NewsRepository;
import com.bank.doorstatic.repository.sorftware.SorftwareRepository;
import com.bank.doorstatic.repository.sorftware.SorftwareTypeRepository;
import com.bank.doorstatic.service.newList.NewListService;
import com.bank.doorstatic.service.sorftware.FileService;
import com.bank.doorstatic.service.sorftware.SorftwareListService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@Slf4j
@RestController
public class SorftwareController {



    @Autowired
    SorftwareRepository sorftwareRepository;

    @Autowired
    SorftwareListService sorftwareListService;

    @Autowired
    SorftwareTypeRepository sorftwareTypeRepository;

    @Autowired
    FileService fileService;


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @GetMapping("/sorftwareList/api/getPage")
    public Object getSorftwarePage(@RequestParam(name="page",defaultValue="" ) String page,
                               @RequestParam(name="per_page",defaultValue="" ) String per_page,
                              @RequestParam(name="id",defaultValue="" ) String id,
                              @RequestParam(name="title",defaultValue="" ) String title,
                              @RequestParam(name="create_time",defaultValue="" ) String create_time,
                              @RequestParam(name="name",defaultValue="" ) String sorftwareTypeid


    ) throws ParseException {
        log.info("访问了 getSorftwarePage");

        Integer pageNum = 1;
        if(!page.equals("")){
            pageNum = Integer.parseInt(page);
        }
        Integer perPage = Integer.MAX_VALUE;
        if(!per_page.equals("")){
            perPage = Integer.parseInt(per_page);
        }


        Date createTimeStart = null;
        Date createTimeEnd = null;

        if(!create_time.equals("")){
            createTimeStart =   simpleDateFormat.parse(create_time.split(",")[0]) ;
            createTimeEnd =   simpleDateFormat.parse(create_time.split(",")[1]) ;

        }


        SorftwareType softType = null;

        if(sorftwareTypeid!=null && !sorftwareTypeid.equals("")){
            softType =  sorftwareTypeRepository.findById(Integer.parseInt(sorftwareTypeid)).get();
        }



        List<Sorftware> resultTmp = sorftwareListService.queryFlows(pageNum, perPage, title,  id, softType ,createTimeStart,  createTimeEnd);

        Integer count = sorftwareListService.getCounts(title,  id, softType, createTimeStart,  createTimeEnd);



        List<SorftwareDao> result = new ArrayList<>();
        for(Sorftware sort : resultTmp){
            SorftwareDao sorftwareDao = new SorftwareDao();
            SorftwareTypeDAO sorftwareTypeDAO = new SorftwareTypeDAO();

            BeanUtils.copyProperties(sort.getSorftwareType(),sorftwareTypeDAO);
            BeanUtils.copyProperties(sort,sorftwareDao);

            sorftwareDao.setSorftwareType(sorftwareTypeDAO);
            result.add(sorftwareDao);
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
    @PostMapping("/sorftwareList/api/add")
    public Object addSorftware(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 addSorftware");

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


        SorftwareType softType = null;


        Object p = params.get("name");

        if(p==null || p.toString().equals("")){
            throw  new Exception("name 不能为空");
        }
        softType =  sorftwareTypeRepository.findById(Integer.parseInt(p.toString())).get();


        Sorftware sorftware  = new Sorftware();
        sorftware.setTitle(params.get("title"));
        sorftware.setContent(params.get("content"));
        sorftware.setCreate_time(new Date(System.currentTimeMillis()));
        sorftware.setSorftwareType(softType);


        //先保存一次得到id
        Integer key = sorftwareRepository.save(sorftware).getId();


        //文件是一个json [{},{},{}...]
        Object filesObject = params.get("files");
        List<String> fileList = (ArrayList<String>)filesObject;

        //需要把文件移动到相应到目录下
        String fromPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"upload"+File.separator;
        //文件保存路径
        String fullPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"upload"+File.separator+key+File.separator;

        String returnPath = File.separator+"upload"+File.separator+key+File.separator;

        //fileList得到的是map类型，需要转换为list,同时需要设置文件访问路径
        List<String> fileListNameArray = new ArrayList<>();
        List<String> fileListPathArray = new ArrayList<>();
        List<String> retrunPathArray = new ArrayList<>();



        List<FileInfo> fileInfos = new ArrayList<>();


        for(int i=0;i<fileList.size();i++) {

            fileListNameArray.add(fileList.get(i));

            fileListPathArray.add(fullPath+fileList.get(i));

            retrunPathArray.add(returnPath+fileList.get(i));

            //把文件从upload移动到fullPath路径下
            File target = new File(fullPath);
            if (!target.exists()) { //判断文件父目录是否存在
                target.mkdirs();
            }

            File startFile = new File(fromPath+ fileList.get(i));
            if(startFile.exists()){
                File toFile = new File(fullPath+ fileList.get(i));
                startFile.renameTo(toFile);
            }else{
                log.info(startFile.getName()+"不存在");
            }


            FileInfo fileInfo = new FileInfo();
            //设置文件名称
            fileInfo.setName(fileList.get(i));
            fileInfo.setPath(returnPath+fileList.get(i));
            fileInfos.add(fileInfo);
        }


        String fileInfoStr = JSON.toJSONString(fileInfos);


        sorftware.setFiles(fileInfoStr);





        sorftwareRepository.save(sorftware);

        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("成功添加");
        returnData.setData(dataSource);

        return returnData;
    }


    @PostMapping("/sorftwareList/api/edit")
    public ReturnData editSorftware(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 editSorftware");

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


        Sorftware oldSorftware = sorftwareRepository.findById(Integer.parseInt(params.get("id")+"")).get();



        Object p = params.get("name");

        if(p==null || p.toString().equals("")){
            throw  new Exception("name 不能为空");
        }
        SorftwareType softType =  sorftwareTypeRepository.findById(Integer.parseInt(p.toString())).get();




        oldSorftware.setTitle(params.get("title")+"");
        oldSorftware.setContent(params.get("content")+"");
        oldSorftware.setSorftwareType(softType);

        //文件是一个json [{},{},{}...]
        Object filesObject = params.get("files");
        List<String> fileList = (ArrayList<String>)filesObject;

        //需要把文件移动到相应到目录下
        String fromPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"upload"+File.separator;
        //文件保存路径
        String fullPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"upload"+File.separator+oldSorftware.getId()+File.separator;

        String returnPath = File.separator+"upload"+File.separator+oldSorftware.getId()+File.separator;

        //fileList得到的是map类型，需要转换为list,同时需要设置文件访问路径
        List<String> fileListNameArray = new ArrayList<>();
        List<String> fileListPathArray = new ArrayList<>();
        List<String> retrunPathArray = new ArrayList<>();


        List<FileInfo> fileInfos = new ArrayList<>();


        for(int i=0;i<fileList.size();i++) {

            fileListNameArray.add(fileList.get(i));

            fileListPathArray.add(fullPath+fileList.get(i));

            retrunPathArray.add(returnPath+fileList.get(i));

            //把文件从upload移动到fullPath路径下
            File target = new File(fullPath);
            if (!target.exists()) { //判断文件父目录是否存在
                target.mkdirs();
            }

            File startFile = new File(fromPath+ fileList.get(i));
            if(startFile.exists()){
                File toFile = new File(fullPath+ fileList.get(i));
                startFile.renameTo(toFile);
            }else{
                log.info(startFile.getName()+"不存在");
            }


            FileInfo fileInfo = new FileInfo();
            //设置文件名称
            fileInfo.setName(fileList.get(i));
            fileInfo.setPath(returnPath+fileList.get(i));
            fileInfos.add(fileInfo);

        }


        String fileInfoStr = JSON.toJSONString(fileInfos);


        oldSorftware.setFiles(fileInfoStr);


        sorftwareRepository.save(oldSorftware);


        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("更新成功");
        returnData.setData(dataSource);

        return returnData;
    }








    private   boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }


    @PostMapping("/sorftwareList/api/delete")
    public ReturnData deleteSorftware(@RequestBody Object paramTmp) throws Exception {
        log.info("访问了 deleteSorftware");

        Map<String, Object> params = (Map<String, Object>) paramTmp;

        List<Integer> ids = (List<Integer>)params.get("ids");


        sorftwareRepository.deleteBatch(ids);


        //同时清理文件夹
        for(Integer id: ids){
            String fullPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"upload"+File.separator+id+File.separator;

            File target = new File(fullPath);
            if (target.exists()) { //判断文件父目录是否存在
//                target.delete();
                if(deleteFile(target)){
                    log.info("成功删除目录："+fullPath);
                }else {
                    log.info("失败删除目录："+fullPath);
                }
            }
        }




        ReturnData returnData = new ReturnData();
        DataSource dataSource = new DataSource();
        dataSource.setMessage("删除成功");
        returnData.setData(dataSource);

        return returnData;

    }






    @PostMapping("/sorftwareList/api/uploadFile")
    public Object uploadSorftware(MultipartFile multipartFile) throws Exception {
        log.info("访问了 uploadSorftware");


        String path = fileService.UploadFile(multipartFile);




        return path;

    }







}
