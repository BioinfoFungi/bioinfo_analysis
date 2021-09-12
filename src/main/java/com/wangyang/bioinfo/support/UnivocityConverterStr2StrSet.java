package com.wangyang.bioinfo.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.univocity.parsers.conversions.Conversion;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UnivocityConverterStr2StrSet implements Conversion<String, Set<String>> {
    @Override
    public Set<String> execute(String s) {
        if(s==null){
            return null;
        }
        s = s.replace("\"","");
        JSONArray jsonArray = JSON.parseArray(s);
        List<String> list = jsonArray.toJavaList(String.class);
        Set<String> collect = list.stream().collect(Collectors.toSet());
        return collect;
    }

    @Override
    public String revert(Set<String> strings) {
        if(strings==null)return null;
        return  JSON.toJSONString(strings);
    }

}
