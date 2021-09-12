package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.AnalysisSoftware;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.DataCategoryParam;
import com.wangyang.bioinfo.pojo.entity.DataCategory;
import com.wangyang.bioinfo.service.IDataCategoryService;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.util.BaseResponse;
import com.wangyang.bioinfo.util.CacheStore;
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
 * @date 2021/7/25
 */
@RestController
@RequestMapping("/api/data_category")
public class DataCategoryController {

    @Autowired
    IDataCategoryService dataCategoryService;

    @Autowired
    IOrganizeFileService organizeFileService;

    @GetMapping
    @Anonymous
    public Page<DataCategory> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<DataCategory> dataCategories = dataCategoryService.pageBy(baseTermParam,pageable);
        return dataCategories;
    }

    @PostMapping
    public DataCategory add(@RequestBody DataCategoryParam dataCategoryParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  dataCategoryService.add(dataCategoryParam,user);
    }
    @PostMapping("/update/{id}")
    public DataCategory update(@PathVariable("id") Integer id, @RequestBody DataCategoryParam dataCategoryParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  dataCategoryService.update(id,dataCategoryParam,user);
    }
    @GetMapping("/del/{id}")
    public DataCategory delById(@PathVariable("id")Integer id){
        return dataCategoryService.delBy(id);
    }
    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        dataCategoryService.initData(organizeFile.getAbsolutePath(),true);
        return BaseResponse.ok("初始化完成!");
    }
    @PostMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        dataCategoryService.createTSVFile(response);
    }
    @GetMapping("/listAll")
    public List<DataCategory> listAll(){
        return dataCategoryService.listAll();
    }
    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam(value = "path",defaultValue = "") String path,
                                   @RequestParam(value = "isEmpty", defaultValue = "false") Boolean isEmpty){
        if(path!=null && path.equals("")){
            path = CacheStore.getValue("workDir")+"/TCGADOWNLOAD/data/DataCategory.tsv";
        }
        List<DataCategory> cancerStudyList = dataCategoryService.initData(path, isEmpty);
        return BaseResponse.ok("导入["+cancerStudyList.size()+"]个对象！");
    }

}
