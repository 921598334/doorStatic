/**
 * Copyright 2021 bejson.com
 */
package com.bank.doorstatic.entity.sorftware;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sorftware")
public class Sorftware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(name = "id", nullable = false)
    private int id;
    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private Date create_time;

    //文件路径
    private String files;



    @ManyToOne(cascade = CascadeType.MERGE,optional = false,fetch = FetchType.LAZY)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name="sorftwareType_id")
    private SorftwareType sorftwareType;




    @Override
    public String toString() {
        return "Sorftware{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                ", files='" + files + '\'' +
                ", sorftwareType=" + sorftwareType.getName() +
                '}';
    }
}