package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.RNA.MRNA;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseRNAQuery;
import com.wangyang.bioinfo.pojo.vo.RNAVO;
import com.wangyang.bioinfo.service.IMRNAService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@RestController
@RequestMapping("/api/mRNA")
public class MRNAController {
    @Autowired
    IMRNAService mrnaService;

    @Autowired
    IOrganizeFileService organizeFileService;

    @PostMapping
    public MRNA add(@RequestBody MRNA mrnaInput){

        MRNA mrna = mrnaService.add(mrnaInput);
        return mrna;
    }

    @GetMapping
    public Page<RNAVO> page(BaseRNAQuery baseRNAQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<MRNA> mrnaPage = mrnaService.pageBy(baseRNAQuery, pageable);
        return mrnaService.convert(mrnaPage);
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnNameAndCheck(name);
        List<MRNA> mrnas = mrnaService.initData(organizeFile.getAbsolutePath());
        return BaseResponse.ok("mRNA初始化完成!");
    }
}
