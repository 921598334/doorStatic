package com.bank.doorstatic.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class FileUpload {
    //如果返回null表明上传失败，成功会返回路径
    public String uploadFile(MultipartFile multipartFile ){

        if (multipartFile == null || multipartFile.isEmpty()){
            return null;
        }

        //生成新的文件名及存储位置
        String fileName = multipartFile.getOriginalFilename();


        String newFileName = System.currentTimeMillis()+"_"+fileName;

        String fullPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"upload"+File.separator+fileName;

        try {
            File target = new File(fullPath);
            if (!target.getParentFile().exists()) { //判断文件父目录是否存在
                target.getParentFile().mkdirs();
            }
            multipartFile.transferTo(target);

            String fileUrl = "/upload/".concat(newFileName);

            return null;

        } catch (IOException ex) {
            return null;

        }
    }
}
