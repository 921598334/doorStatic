package com.bank.doorstatic.service.sorftware;


import com.bank.doorstatic.tool.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {



    @Autowired
    FileUpload fileUpload;





    public String UploadFile(MultipartFile multipartFile){


        return fileUpload.uploadFile(multipartFile);

    }

}
