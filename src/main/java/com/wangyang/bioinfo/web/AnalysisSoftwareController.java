package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.base.BaseTerm;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.CancerParam;
import com.wangyang.bioinfo.pojo.trem.AnalysisSoftware;
import com.wangyang.bioinfo.pojo.trem.Cancer;
import com.wangyang.bioinfo.service.IAnalysisSoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@RestController
@RequestMapping("/api/AnalysisSoftware")
public class AnalysisSoftwareController {
    @Autowired
    IAnalysisSoftwareService analysisSoftwareService;

    @PostMapping
    public AnalysisSoftware add(@RequestBody BaseTermParam baseTermParam){
        return  analysisSoftwareService.save(baseTermParam);
    }
}
