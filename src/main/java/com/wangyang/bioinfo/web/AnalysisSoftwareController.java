package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.trem.AnalysisSoftware;
import com.wangyang.bioinfo.service.IAnalysisSoftwareService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@RestController
@RequestMapping("/api/analysis_software")
public class AnalysisSoftwareController {
    @Autowired
    IAnalysisSoftwareService analysisSoftwareService;

    @Autowired
    IOrganizeFileService organizeFileService;


    @GetMapping
    @Anonymous
    public Page<AnalysisSoftware> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<AnalysisSoftware> analysisSoftwares = analysisSoftwareService.pageBy(baseTermParam,pageable);
        return analysisSoftwares;
    }

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

    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam("path") String path){
        analysisSoftwareService.initData(path);
        return BaseResponse.ok("初始化完成!");
    }
}
