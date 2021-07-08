package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.MRNA;
import com.wangyang.bioinfo.pojo.param.BaseRNAQuery;
import com.wangyang.bioinfo.pojo.vo.RNAVO;
import com.wangyang.bioinfo.service.IMRNAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
}
