package com.bank.doorstatic.model;

import lombok.Data;

@Data
public class ErrorInfoStructure {

    boolean success;
    String errorCode;
    String errorMessage;
    Integer showType; // error display type： 0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
    String traceId; // Convenient for back-end Troubleshooting: unique request ID
    String host;
}
