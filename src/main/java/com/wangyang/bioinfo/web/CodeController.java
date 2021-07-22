package com.wangyang.bioinfo.web;

import com.github.rcaller.rstuff.RCaller;
import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.service.ICodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@RestController
@RequestMapping("/api/R")
public class CodeController {

    @Autowired
    ICodeService codeService;

    @GetMapping("/RProcess/{cancerStudyId}")
    public void process(@PathVariable("cancerStudyId") Integer cancerStudyId, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();


//        outputStream.write("abcd".getBytes());
//        outputStream.print("ddd");
//        outputStream.flush();
//        outputStream.write("abcd".getBytes());
//        outputStream.flush();
//        outputStream.close();
            codeService.processByCancerStudyId(cancerStudyId, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
