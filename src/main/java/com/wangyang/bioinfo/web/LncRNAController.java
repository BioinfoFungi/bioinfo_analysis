package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.CancerStudy;
import com.wangyang.bioinfo.pojo.LncRNA;
import com.wangyang.bioinfo.pojo.param.BaseRNAParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
import com.wangyang.bioinfo.service.ILncRNAService;
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
@RequestMapping("/api/lncRNA")
public class LncRNAController {
    @Autowired
    ILncRNAService lncRNAService;

    @PostMapping
    public LncRNA add(@RequestBody  LncRNA lncRNAInput){

        LncRNA lncRNA = lncRNAService.add(lncRNAInput);
        return lncRNA;
    }

    @GetMapping
    public Page<LncRNA> page(BaseRNAParam baseRNAParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<LncRNA> lncRNAPage = lncRNAService.pageBy(baseRNAParam, pageable);
        return lncRNAPage;
    }

}
