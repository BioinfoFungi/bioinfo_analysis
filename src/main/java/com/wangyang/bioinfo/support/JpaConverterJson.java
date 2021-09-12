package com.wangyang.bioinfo.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.univocity.parsers.conversions.Conversion;

import javax.persistence.AttributeConverter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaConverterJson implements AttributeConverter<Set<Integer>, String> {
    @Override
    public Set<Integer> convertToEntityAttribute(String s) {
        if(s==null){
            return null;
        }
        JSONArray jsonArray = JSON.parseArray(s);
        List<Integer> list = jsonArray.toJavaList(Integer.class);
        Set<Integer> collect = list.stream().collect(Collectors.toSet());
        return collect;
    }

    @Override
    public String convertToDatabaseColumn(Set<Integer> integers) {
        if(integers==null)return null;
        return  JSON.toJSONString(integers);
    }


}