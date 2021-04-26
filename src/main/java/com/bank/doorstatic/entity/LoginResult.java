package com.bank.doorstatic.entity;


import lombok.Data;

@Data
public class LoginResult {

    private String  status;
    private String  type;
    private String  currentAuthority;
}
