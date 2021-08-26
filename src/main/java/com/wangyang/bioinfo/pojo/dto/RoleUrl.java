package com.wangyang.bioinfo.pojo.dto;

import lombok.Data;

@Data
public class RoleUrl {
    private String role;
    private String url;

    public RoleUrl(String role, String url) {
        this.role = role;
        this.url = url;
    }
}
