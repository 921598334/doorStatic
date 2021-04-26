/**
 * Copyright 2021 bejson.com
 */
package com.bank.doorstatic.entity.sorftware;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2021-04-22 6:56:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Data
@Entity
@Table(name = "sorftwareType")
public class SorftwareType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(name = "id", nullable = false)
    private int id;
    private String name;


    @OneToMany(mappedBy = "sorftwareType",cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
    private List<Sorftware> sorftwares;



}