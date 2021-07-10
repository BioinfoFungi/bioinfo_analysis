package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.RNA.CircRNA;
import com.wangyang.bioinfo.pojo.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseRNAQuery;
import com.wangyang.bioinfo.pojo.vo.RNAVO;
import com.wangyang.bioinfo.service.ICircRNAService;
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
 * @date 2021/7/10
 */
@RestController
@RequestMapping("/api/circRNA")
public class CircRNAController {
    @Autowired
    ICircRNAService circRNAService;
    @Autowired
    IOrganizeFileService organizeFileService;

    @PostMapping
    public CircRNA add(@RequestBody CircRNA circRNAInput){

        CircRNA circRNA = circRNAService.add(circRNAInput);
        return circRNA;
    }

    @GetMapping
    public Page<RNAVO> page(BaseRNAQuery baseRNAQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<CircRNA> page = circRNAService.pageBy(baseRNAQuery, pageable);
        return circRNAService.convert(page);
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnNameAndCheck(name);
        circRNAService.initData(organizeFile.getLocalPath());
        return BaseResponse.ok("circRNA初始化完成!");
    }
}
