package com.wangyang.bioinfo.web;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.trem.Cancer;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.CancerParam;
import com.wangyang.bioinfo.service.ICancerService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@RestController
@RequestMapping("/api/cancer")
public class CancerController {
    @Autowired
    ICancerService cancerService;
    @Autowired
    IOrganizeFileService organizeFileService;

    @GetMapping
    public Page<Cancer> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<Cancer> cancers = cancerService.pageBy(baseTermParam,pageable);
        return cancers;
    }

    @PostMapping
    public Cancer add(@RequestBody  CancerParam cancerParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  cancerService.addCancer(cancerParam,user);
    }
    @GetMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        cancerService.createTSVFile(response);
    }

    @GetMapping("/listAll")
    public List<Cancer> listAll(){
        return cancerService.listAll();
    }



    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        cancerService.initData(organizeFile.getAbsolutePath());
        return BaseResponse.ok("初始化完成!");
    }

    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam("path") String path){
        cancerService.initData(path);
        return BaseResponse.ok("初始化完成!");
    }
}
