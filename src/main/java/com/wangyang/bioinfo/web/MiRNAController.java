package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.entity.MiRNA;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseRNAQuery;
import com.wangyang.bioinfo.pojo.vo.RNAVO;
import com.wangyang.bioinfo.service.IMiRNAService;
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
@RequestMapping("/api/miRNA")
public class MiRNAController {
    @Autowired
    IMiRNAService miRNAService;
    @Autowired
    IOrganizeFileService organizeFileService;

    @PostMapping
    public MiRNA add(@RequestBody MiRNA miRNAInput){

        MiRNA miRNA = miRNAService.add(miRNAInput);
        return miRNA;
    }

    @GetMapping
    public Page<RNAVO> page(BaseRNAQuery baseRNAQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<MiRNA> miRNAPage = miRNAService.pageBy(baseRNAQuery, pageable);
        return miRNAService.convert(miRNAPage);
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        miRNAService.initData(organizeFile.getAbsolutePath(),true);
        return BaseResponse.ok("miRNA初始化完成!");
    }
}
