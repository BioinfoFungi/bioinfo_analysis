package com.wangyang.bioinfo.pojo.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskDto {
    private Map<String,String> map;
    private String metadata;
    private List<MetadataGroup> groups;
}
