package com.example.RolebaseAuth.payloads;

import lombok.Data;

@Data
public class BaseServerResponse<T> {
    private boolean success;
    private String responseCode;
    private String responseMessage;
    private T responseData;

}
