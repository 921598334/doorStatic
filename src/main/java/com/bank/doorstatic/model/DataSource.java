package com.bank.doorstatic.model;


import com.bank.doorstatic.entity.newList.News;
import lombok.Data;

import java.util.List;

@Data
public class DataSource {

    private List datasource;

    private MetaDAO meta;

    private String message;
}
