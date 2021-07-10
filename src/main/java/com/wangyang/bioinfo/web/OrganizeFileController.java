package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseFileQuery;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@RestController
@RequestMapping("/api/organize_file")
public class OrganizeFileController {

    @Autowired
    IOrganizeFileService organizeService;
    @PostMapping
    public OrganizeFile add(@RequestBody OrganizeFile organizeFileParam){
        OrganizeFile organizeFile = organizeService.add(organizeFileParam);
        return organizeFile;
    }

    @GetMapping
    public Page<OrganizeFile> page(BaseFileQuery baseFileQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<OrganizeFile> organizeFiles = organizeService.pageBy(baseFileQuery, pageable);
        return organizeFiles;
    }

    @GetMapping("/findByEnName/{name}")
    public OrganizeFile findByEnName(@PathVariable("name") String name){
        return organizeService.findByEnNameAndCheck(name);
    }
}
