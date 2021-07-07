package com.wangyang.bioinfo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.persistence.AttributeConverter;

/**
 * @author wangyang
 * @date 2021/6/28
 */
public class JpaConverterListJson implements AttributeConverter<Integer[], String> {
    @Override
    public String convertToDatabaseColumn(Integer[] o) {
         return JSON.toJSONString(o);
    }

    @Override
    public Integer[] convertToEntityAttribute(String s) {
        JSONArray jsonArray = JSON.parseArray(s);
        Integer[] array = jsonArray.toArray(new Integer[jsonArray.size()]);
        return array;
    }
}
