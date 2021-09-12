package com.wangyang.bioinfo.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.univocity.parsers.conversions.Conversion;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UnivocityConverter  implements Conversion<String, Set<Integer>> {


    @Override
    public Set<Integer> execute(String s) {
        if(s==null){
            return null;
        }
        s = s.replace("\"","");
        JSONArray jsonArray = JSON.parseArray(s);
        List<Integer> list = jsonArray.toJavaList(Integer.class);
        Set<Integer> collect = list.stream().collect(Collectors.toSet());
        return collect;
    }

    @Override
    public String revert(Set<Integer> integers) {
        if(integers==null)return null;
        return  JSON.toJSONString(integers);
    }


}
