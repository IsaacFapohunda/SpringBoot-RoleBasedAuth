package com.example.RolebaseAuth.config;

import lombok.Data;

@Data
public class RequestBody {
    private  String long_url;
    private String domain = "bit.ly";
    private String group_guid = "BobtcTVHnAH";


    public RequestBody(String long_url) {
        this.long_url = long_url;
    }
}
