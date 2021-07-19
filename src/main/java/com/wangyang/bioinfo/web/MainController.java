package com.wangyang.bioinfo.web;

import com.github.rcaller.graphics.SkyTheme;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.util.StringCacheStore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @author wangyang
 * @date 2021/4/23
 */
//@RestController
@Controller
@Slf4j
public class MainController {

    @Autowired
    TestAsync testAsync;


//    @RequestMapping("/")
//    public void index(HttpServletResponse response) throws IOException {
//        response.sendRedirect("index.html");
//    }
    @GetMapping("/api/global")
    @ResponseBody
    public Map<String,String> globalConfig(){
        Map<String,String> map = new HashMap<>();
        map.put("attachment", StringCacheStore.getValue("workDir")+"/upload");
        map.put("cancerStudy", StringCacheStore.getValue("workDir")+"/data");
        map.put("Attachment_base_url", StringCacheStore.getValue("Attachment_base_url"));
        return map;
    }


    @GetMapping("/test")
    @ResponseBody
    public String test(){
        System.out.println("-----test before");
        testAsync.testAsync();
        System.out.println("-----test after");
        return "你好";
    }

    @RequestMapping("/img")
    @ResponseBody
    public void testImg(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse) throws IOException {
        File file = new File("/home/wy/Pictures/R385376855fc011b6fce9e0c34ace0517.jpeg");

        byte[] img = Files.readAllBytes(file.toPath());
        String base64 = Base64.getEncoder().encodeToString(img);
        httpServletResponse.setContentType("image/png");
        OutputStream os = httpServletResponse.getOutputStream();
        os.write(img);
//        os.write(base64.);
        os.flush();
        os.close();
    }


    @RequestMapping("/testJavaCallR")
    @ResponseBody
    public void testJavaCallR(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse) throws IOException {

        RCaller caller = RCaller.create();
        RCode code = RCode.create();

        code.addRCode("x <- rnorm(30)");
        code.addRCode("y <- rnorm(30)");
        code.addRCode("ols <- lm(y~x)");

        caller.setGraphicsTheme(new SkyTheme());
        File plt = code.startPlot();
        code.addRCode("barplot(x,y)");
        code.addRCode("abline(ols$coefficients[1], ols$coefficients[2])");
        code.addRCode("abline(mean(y), 0)");
        code.addRCode("abline(v = mean(x))");
        code.endPlot();
        caller.setRCode(code);
        caller.runAndReturnResult("ols");


        byte[] img = Files.readAllBytes(plt.toPath());
        String base64 = Base64.getEncoder().encodeToString(img);
        httpServletResponse.setContentType("image/png");
        OutputStream os = httpServletResponse.getOutputStream();
        os.write(img);
//        os.write(base64.);
        os.flush();
        os.close();
    }
    @RequestMapping("/testJavaCallData")
    @ResponseBody
    public void testJavaCallData(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse) throws IOException {

        RCaller caller = RCaller.create();
        RCode code = RCode.create();

//        code.addRCode("df <- read.csv(\"~/Downloads/TCGA_COAD_Counts.tsv\")");
//        code.addRCode("df <- head(df)");
        caller.redirectROutputToStream(httpServletResponse.getOutputStream());
        code.addRCode("a<-1");
        code.addRCode("version");
        caller.runAndReturnResultOnline("a");
    }
}
