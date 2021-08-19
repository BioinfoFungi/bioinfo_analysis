package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.trem.DataCategory;
import com.wangyang.bioinfo.pojo.trem.Study;
import com.wangyang.bioinfo.service.IDataCategoryService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@RestController
@RequestMapping("/api/dataCategory")
public class DataCategoryController {

    @Autowired
    IDataCategoryService dataCategoryService;

    @Autowired
    IOrganizeFileService organizeFileService;
    @PostMapping
    public DataCategory add(@RequestBody BaseTermParam baseTermParam){
        return  dataCategoryService.save(baseTermParam);
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        dataCategoryService.initData(organizeFile.getAbsolutePath());
        return BaseResponse.ok("初始化完成!");
    }
    @GetMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        dataCategoryService.createTSVFile(response);
    }
    @GetMapping("/listAll")
    public List<DataCategory> listAll(){
        return dataCategoryService.listAll();
    }


}
