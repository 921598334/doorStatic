/**
 * Copyright 2021 bejson.com
 */
package com.bank.doorstatic.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
public class
NewsDAO {


    private int id;
    private String title;

    private String content;
    private Date create_time;
    private String imgCode;

}