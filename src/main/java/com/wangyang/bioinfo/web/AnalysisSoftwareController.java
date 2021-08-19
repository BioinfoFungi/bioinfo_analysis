package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.base.BaseTerm;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.CancerParam;
import com.wangyang.bioinfo.pojo.trem.AnalysisSoftware;
import com.wangyang.bioinfo.pojo.trem.Cancer;
import com.wangyang.bioinfo.service.IAnalysisSoftwareService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    IOrganizeFileService organizeFileService;
    @PostMapping
    public AnalysisSoftware add(@RequestBody BaseTermParam baseTermParam){
        return  analysisSoftwareService.save(baseTermParam);
    }
    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        analysisSoftwareService.initData(organizeFile.getAbsolutePath());
        return BaseResponse.ok("初始化完成!");
    }
}
