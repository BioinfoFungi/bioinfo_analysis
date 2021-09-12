package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.AnalysisSoftwareParam;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.entity.AnalysisSoftware;
import com.wangyang.bioinfo.service.IAnalysisSoftwareService;
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
@RequestMapping("/api/analysis_software")
public class AnalysisSoftwareController {
    @Autowired
    IAnalysisSoftwareService analysisSoftwareService;

    @Autowired
    IOrganizeFileService organizeFileService;


    @GetMapping
    @Anonymous
    public Page<AnalysisSoftware> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<AnalysisSoftware> analysisSoftware = analysisSoftwareService.pageBy(baseTermParam,pageable);
        return analysisSoftware;
    }

    @PostMapping
    public AnalysisSoftware add(@RequestBody AnalysisSoftwareParam analysisSoftwareParam,HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return   analysisSoftwareService.add(analysisSoftwareParam,user);
    }
    @PostMapping("/update/{id}")
    public AnalysisSoftware update(@PathVariable("id") Integer id, @RequestBody AnalysisSoftwareParam analysisSoftwareParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  analysisSoftwareService.update(id,analysisSoftwareParam,user);
    }
    @GetMapping("/del/{id}")
    public AnalysisSoftware delById(@PathVariable("id")Integer id){
        return analysisSoftwareService.delBy(id);
    }
    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        analysisSoftwareService.initData(organizeFile.getAbsolutePath(),true);
        return BaseResponse.ok("初始化完成!");
    }

//    @GetMapping("/init")
//    public BaseResponse initDataBy(@RequestParam("path") String path){
//        analysisSoftwareService.initData(path,true);
//        return BaseResponse.ok("初始化完成!");
//    }

    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam(value = "path",defaultValue = "") String path,
                                   @RequestParam(value = "isEmpty", defaultValue = "false") Boolean isEmpty){
        if(path!=null && path.equals("")){
            path = CacheStore.getValue("workDir")+"/TCGADOWNLOAD/data/AnalysisSoftware.tsv";
        }
        List<AnalysisSoftware> cancerStudyList = analysisSoftwareService.initData(path, isEmpty);
        return BaseResponse.ok("导入["+cancerStudyList.size()+"]个对象！");
    }
    @PostMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        analysisSoftwareService.createTSVFile(response);
    }


}
