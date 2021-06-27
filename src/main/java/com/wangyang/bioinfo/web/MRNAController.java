package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.MRNA;
import com.wangyang.bioinfo.pojo.MiRNA;
import com.wangyang.bioinfo.pojo.param.BaseRNAParam;
import com.wangyang.bioinfo.service.IMRNAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public MRNA add(@RequestBody MRNA lncRNAInput){

        MRNA miRNA = mrnaService.add(lncRNAInput);
        return miRNA;
    }

    @GetMapping
    public Page<MRNA> page(BaseRNAParam baseRNAParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<MRNA> mrnaPage = mrnaService.pageBy(baseRNAParam, pageable);
        return mrnaPage;
    }
}
