package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.entity.AnalysisSoftware;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.entity.DataOrigin;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.DataOriginParam;
import com.wangyang.bioinfo.service.IDataOriginService;
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
 * @date 2021/6/26
 */
@RestController
@RequestMapping("/api/data_origin")
public class DataOriginController {

    @Autowired
    IDataOriginService dataOriginService;

    @Autowired
    IOrganizeFileService organizeFileService;


    @GetMapping
    @Anonymous
    public Page<DataOrigin> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<DataOrigin> dataOrigins = dataOriginService.pageBy(baseTermParam,pageable);
        return dataOrigins;
    }
    @PostMapping
    public DataOrigin add(@RequestBody  DataOriginParam dataOriginParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  dataOriginService.add(dataOriginParam,user);
    }
    @PostMapping("/update/{id}")
    public DataOrigin update(@PathVariable("id") Integer id, @RequestBody DataOriginParam dataOriginParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  dataOriginService.update(id,dataOriginParam,user);
    }
    @GetMapping("/del/{id}")
    public DataOrigin delById(@PathVariable("id")Integer id){
        return dataOriginService.delBy(id);
    }

    @PostMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        dataOriginService.createTSVFile(response);
    }
    @GetMapping("/listAll")
    public List<DataOrigin> listAll(){
        return dataOriginService.listAll();
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        dataOriginService.initData(organizeFile.getAbsolutePath(),true);
        return BaseResponse.ok("初始化完成!");
    }

    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam(value = "path",defaultValue = "") String path,
                                   @RequestParam(value = "isEmpty", defaultValue = "false") Boolean isEmpty){
        if(path!=null && path.equals("")){
            path = CacheStore.getValue("workDir")+"/TCGADOWNLOAD/data/DataOrigin.tsv";
        }
        List<DataOrigin> cancerStudyList = dataOriginService.initData(path, isEmpty);
        return BaseResponse.ok("导入["+cancerStudyList.size()+"]个对象！");
    }
}
