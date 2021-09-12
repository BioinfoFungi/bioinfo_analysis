package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.entity.DEG;
import com.wangyang.bioinfo.service.IDEGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/4/24
 */
@RestController
@RequestMapping("/api/deg")
public class DEGController {

    @Autowired
    IDEGService degService;



    @GetMapping
    public Page<DEG> page(@PageableDefault(sort = {"id"},direction = DESC)Pageable pageable){
        Page<DEG> page = degService.page(pageable);

        return page;
    }
}
