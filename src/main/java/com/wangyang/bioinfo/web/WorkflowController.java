package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.trem.DataCategory;
import com.wangyang.bioinfo.service.IDataCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    @Autowired
    IDataCategoryService experimentalStrategyService;

    @PostMapping
    public DataCategory add(@RequestBody BaseTermParam baseTermParam){
        return  experimentalStrategyService.save(baseTermParam);
    }
}
