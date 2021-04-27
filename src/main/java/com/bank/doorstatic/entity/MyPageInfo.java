package com.bank.doorstatic.entity;

import lombok.Data;

import java.util.List;

@Data
public class MyPageInfo {

    private List page;
    private Integer total;
    private Integer perPage;
    private Integer totalPage;
    private Integer currentPage;
    private boolean hasNext;
    private boolean hasPre;
}
