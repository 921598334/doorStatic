/**
 * Copyright 2021 bejson.com
 */
package com.bank.doorstatic.entity.newList;
import java.util.Date;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(name = "id", nullable = false)
    private int id;
    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private Date create_time;


}