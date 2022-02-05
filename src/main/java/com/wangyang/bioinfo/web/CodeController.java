package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.pojo.param.CodeParam;
import com.wangyang.bioinfo.pojo.param.CodeQuery;
import com.wangyang.bioinfo.pojo.support.FileContent;
import com.wangyang.bioinfo.pojo.support.FileTree;
import com.wangyang.bioinfo.service.task.ICodeService;
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
import javax.validation.Valid;

import java.io.File;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired
    ICodeService codeService;

    @Autowired
    IOrganizeFileService organizeFileService;

    @GetMapping
    public Page<Code> page(CodeQuery codeQuery,
                                            @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
                                            @RequestParam(value = "more", defaultValue = "false") Boolean more){
        Page<Code> codes = codeService.pageBy(codeQuery,pageable);
        return codes;
    }



    @PostMapping
    public Code add(@RequestBody  CodeParam codeParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Code code = codeService.saveBy(codeParam,user);
        return code;
    }

    @PostMapping("/update/{id}")
    public Code update(@PathVariable("id")Integer id, @RequestBody @Valid CodeParam codeParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Code Code = codeService.updateBy(id,codeParam,user);
        return Code;
    }
    @GetMapping("/del/{id}")
    public Code del(@PathVariable("id") Integer id){
        return  codeService.delBy(id);
    }

    @GetMapping("/findByCan/{id}")
    public List<Code> findExecute(@PathVariable("id") Integer id){
        return codeService.findExecute(id);
    }



    @GetMapping("/checkFile/{id}")
    public Code checkFileExist(@PathVariable("id") Integer id){
        Code code = codeService.checkFileExist(id);
        return code;
    }
    @GetMapping("/findById/{id}")
    public Code findById(@PathVariable("id") Integer id){
        Code code = codeService.findById(id);
        return code;
    }

    @GetMapping("/init/{name}")
    public BaseResponse initData(@PathVariable("name") String name){
        OrganizeFile organizeFile = organizeFileService.findByEnName(name);
        codeService.initData(organizeFile.getAbsolutePath(),true);
        return BaseResponse.ok("CancerStudy初始化完成!");
    }
    @GetMapping("/init")
    public BaseResponse initDataBy(@RequestParam(value = "path",defaultValue = "") String path,
                                   @RequestParam(value = "isEmpty", defaultValue = "false") Boolean isEmpty){
        if(path!=null && path.equals("")){
            path = CacheStore.getValue("workDir")+"/TCGADOWNLOAD/data/Code.tsv";
        }
        List<Code> cancerStudyList = codeService.initData(path, isEmpty);
        return BaseResponse.ok("导入["+cancerStudyList.size()+"]个对象！");
    }
    @GetMapping("/file")
    public List<FileTree> listFiles(@RequestParam(value = "path", defaultValue = "")  String path){
        if(path!=null && path.equals("")){
            path = CacheStore.getValue("workDir")+"/TCGADOWNLOAD/R";
        }
        return codeService.listFiles(path);
    }

    @GetMapping("/listAll")
    public List<Code> listAll(){
        return codeService.listAll();
    }

    @GetMapping("/{crudType}/listAll")
    public List<Code> listByCrudType(@PathVariable("crudType") CrudType crudType){
        return codeService.listByCrudType(crudType);
    }


    @PostMapping("/saveFileContent")
    public BaseResponse saveFileContent(Integer id,@RequestBody Code inputCode,HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Code code = codeService.update(id, inputCode, user);
        String path = CacheStore.getValue("workDir");
        path = path+ File.separator +code.getRelativePath();
        codeService.saveContent(path,code.getContent());
        return BaseResponse.ok("保存文件成功！");
    }
    @GetMapping("/getFileContent")
    public Code getFileContent(Integer id){
        Code code = codeService.findById(id);
        String path = CacheStore.getValue("workDir");
        path = path+ File.separator +code.getRelativePath();
        code.setContent(codeService.getFileContent(path));
        return code;
    }
    @PostMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        codeService.createTSVFile(response);
    }
}
