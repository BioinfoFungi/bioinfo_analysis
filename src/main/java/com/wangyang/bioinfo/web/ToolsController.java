package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.snakemake.Snakemake;
import com.wangyang.bioinfo.pojo.entity.tools.Tools;
import com.wangyang.bioinfo.pojo.param.SnakemakeParam;
import com.wangyang.bioinfo.pojo.param.ToolsParam;
import com.wangyang.bioinfo.service.ISnakemakeService;
import com.wangyang.bioinfo.service.IToolsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/tools")
public class ToolsController {

    @Autowired
    private IToolsService toolsService;

    @GetMapping
    public Page<Tools> page(@PageableDefault(sort = {"id"},direction =  Sort.Direction.DESC) Pageable pageable){
        Page<Tools> tools = toolsService.pageBy(pageable);
        return tools;
    }

    @PostMapping
    public Tools add(@Valid @RequestBody ToolsParam toolsParam, HttpServletRequest request){
        Tools tools = new Tools();
        BeanUtils.copyProperties(toolsParam,tools);
        User user = (User) request.getAttribute("user");
        tools.setUserId(user.getId());
        return toolsService.add(tools, user);
    }

    @GetMapping("/del/{id}")
    public Tools delProject(@PathVariable("id")int id,HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return toolsService.delBy(id);
    }

    @GetMapping("/find/{id}")
    public Tools findProjectById(@PathVariable("id")int id){
        Tools tools = toolsService.findById(id);
        return tools;
    }

    @PostMapping("/update/{id}")
    public Tools update(@PathVariable("id")int id, @Valid @RequestBody ToolsParam toolsParam, HttpServletRequest request){
        Tools tools = new Tools();
        BeanUtils.copyProperties(toolsParam,tools);
        User user = (User) request.getAttribute("user");
        return toolsService.update(id, tools, user);
    }
}
