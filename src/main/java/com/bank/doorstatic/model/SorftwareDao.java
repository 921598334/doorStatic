
package com.bank.doorstatic.model;
import com.bank.doorstatic.entity.sorftware.SorftwareType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class SorftwareDao {


    private int id;
    private String title;
    private String content;
    private Date create_time;

    //文件路径
    private String files;


    private SorftwareTypeDAO sorftwareType;




}