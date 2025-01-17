package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.entity.LncRNA;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseRNAQuery;
import com.wangyang.bioinfo.pojo.vo.RNAVO;
import com.wangyang.bioinfo.service.ILncRNAService;
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
 * @date 2021/6/27
 */
@RestController
@RequestMapping("/api/lncRNA")
public class LncRNAController {
    @Autowired
    ILncRNAService lncRNAService;
    @Autowired
    IOrganizeFileService organizeFileService;

    @PostMapping
    public LncRNA add(@RequestBody  LncRNA lncRNAInput){

        LncRNA lncRNA = lncRNAService.add(lncRNAInput);
        return lncRNA;
    }

    @GetMapping
    public Page<RNAVO> page(BaseRNAQuery baseRNAQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<LncRNA> lncRNAPage = lncRNAService.pageBy(baseRNAQuery, pageable);
        return lncRNAService.convert(lncRNAPage);
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        lncRNAService.initData(organizeFile.getAbsolutePath(),true);
        return BaseResponse.ok("lncRNA初始化完成!");
    }
}
