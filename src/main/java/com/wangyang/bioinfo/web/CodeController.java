package com.wangyang.bioinfo.web;


import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired
    ICodeService codeService;
    @GetMapping
    public BaseResponse runRById(@RequestParam("codeId") Integer codeId,@RequestParam("cancerStudyId")Integer cancerStudyId){
        codeService.rCodePlot(codeId,cancerStudyId);
        return BaseResponse.ok("任务提交成功");
    }
}
