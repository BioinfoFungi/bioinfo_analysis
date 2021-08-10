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
    //http://localhost:8080/api/code?codeId=1&cancerStudyId=66&authorize=
    //ws://localhost:8080/websocket/socketServer.do
    //http://coolaf.com/tool/chattest
    @Autowired
    ICodeService codeService;
    @GetMapping
    public BaseResponse runRById(@RequestParam("codeId") Integer codeId,@RequestParam("cancerStudyId")Integer cancerStudyId){
        codeService.runRCode(codeId,cancerStudyId);
        return BaseResponse.ok("任务提交成功");
    }
}
