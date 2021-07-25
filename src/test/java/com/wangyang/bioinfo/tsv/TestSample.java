package com.wangyang.bioinfo.tsv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wangyang.bioinfo.pojo.entity.Sample;
import com.wangyang.bioinfo.service.ISampleService;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/28
 */
///@SpringBootTest(classes = BioinfoApplication.class)
public class TestSample {

  //  @Autowired
    ISampleService sampleService;

    //@Test
    public void testAdd(){
        Sample sample = new Sample();
        List<Integer[]> list = new ArrayList<>();
//        list.add();
//        sample.setGeneData(Arrays.array(1,2,3,4));
        sampleService.add(sample);
    }

    //@Test
    public void testListAll(){
        List<Sample> samples = sampleService.listAll();
        System.out.println();
    }
}
