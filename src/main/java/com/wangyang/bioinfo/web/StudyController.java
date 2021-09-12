package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.Cancer;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.StudyParam;
import com.wangyang.bioinfo.pojo.entity.Study;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.service.IStudyService;
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
@RequestMapping("/api/study")
public class StudyController {
    @Autowired
    IStudyService studyService;

    @Autowired
    IOrganizeFileService organizeFileService;

    @GetMapping
    @Anonymous
    public Page<Study> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<Study> studies = studyService.pageBy(baseTermParam,pageable);
        return studies;
    }
    @PostMapping
    public Study add(@RequestBody  StudyParam studyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  studyService.add(studyParam,user);
    }
    @PostMapping("/update/{id}")
    public Study update(@PathVariable("id") Integer id, @RequestBody StudyParam studyParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  studyService.update(id,studyParam,user);
    }
    @GetMapping("/del/{id}")
    public Study delById(@PathVariable("id")Integer id){
        return studyService.delBy(id);
    }

    @PostMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        studyService.createTSVFile(response);
    }
    @GetMapping("/listAll")
    public List<Study> listAll(){
        return studyService.listAll();
    }


    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        studyService.initData(organizeFile.getAbsolutePath(),true);
        return BaseResponse.ok("初始化完成!");
    }

    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam(value = "path",defaultValue = "") String path,
                                   @RequestParam(value = "isEmpty", defaultValue = "false") Boolean isEmpty){
        if(path!=null && path.equals("")){
            path = CacheStore.getValue("workDir")+"/TCGADOWNLOAD/data/Study.tsv";
        }
        List<Study> cancerStudyList = studyService.initData(path, isEmpty);
        return BaseResponse.ok("导入["+cancerStudyList.size()+"]个对象！");
    }
}
