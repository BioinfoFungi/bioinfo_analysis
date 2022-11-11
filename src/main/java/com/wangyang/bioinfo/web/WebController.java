package com.wangyang.bioinfo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/admin")
    public String  index(){
        return "redirect:/admin/index.html";
    }
}
