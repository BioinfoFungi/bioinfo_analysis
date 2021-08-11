package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.trem.Workflow;
import com.wangyang.bioinfo.service.IWorkflowService;
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
    IWorkflowService experimentalStrategyService;

    @PostMapping
    public Workflow add(@RequestBody BaseTermParam baseTermParam){
        return  experimentalStrategyService.save(baseTermParam);
    }
}
