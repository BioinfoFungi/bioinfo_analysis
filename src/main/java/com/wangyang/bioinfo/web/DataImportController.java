package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.BaseResponse;
import com.wangyang.bioinfo.util.StringCacheStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/8/20
 */
@RestController
@RequestMapping("/api/data_import")
public class DataImportController {
    @Autowired
    IAnalysisSoftwareService analysisSoftwareService;
    @Autowired
    ICancerService cancerService;
    @Autowired
    IStudyService studyService;
    @Autowired
    IDataOriginService dataOriginService;
    @Autowired
    IDataCategoryService dataCategoryService;
    @Autowired
    ICancerStudyService cancerStudyService;

    @Autowired
    ICodeService codeService;


    private static String[] filenames = new String[]{"Cancer","Study","DataOrigin","DataCategory","AnalysisSoftware","CancerStudy","Code"};

    @GetMapping("/init")
    public  Map<String,String> importALl(@RequestParam(value = "path",defaultValue = "") String pathDir){
        if(pathDir.equals("")){
            pathDir = StringCacheStore.getValue("workDir")+"/TCGADOWNLOAD/data";
        }
//        Arrays.asList(filenames
        Map<String,String> map = new HashMap<>();
        for(String filename : filenames){
            Path path = Paths.get(pathDir, filename + ".tsv");
            boolean exists = path.toFile().exists();
            String absolutePath=null;
            if(exists){
                absolutePath = path.toAbsolutePath().toString();
                if(filename.equals("Cancer")){
                    cancerService.initData(absolutePath);
                }
                if(filename.equals("Study")){
                    studyService.initData(absolutePath);
                }
                if(filename.equals("DataOrigin")){
                    dataOriginService.initData(absolutePath);
                }
                if(filename.equals("DataCategory")){
                    dataCategoryService.initData(absolutePath);
                }
                if(filename.equals("AnalysisSoftware")){
                    analysisSoftwareService.initData(absolutePath);
                }

                if(filename.equals("CancerStudy")){
                    cancerStudyService.initData(absolutePath);
                }
                if(filename.equals("Code")){
                    codeService.initData(absolutePath);
                }

            }
            map.put(filename,absolutePath);
        }
        return map;
    }

}
