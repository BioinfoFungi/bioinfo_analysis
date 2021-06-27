package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.util.StringCacheStore;
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


    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("index.html");
    }

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
}
