package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.Project;
import com.wangyang.bioinfo.pojo.entity.snakemake.Snakemake;
import com.wangyang.bioinfo.pojo.param.ProjectParam;
import com.wangyang.bioinfo.pojo.param.ProjectQuery;
import com.wangyang.bioinfo.pojo.param.SnakemakeParam;
import com.wangyang.bioinfo.pojo.vo.ProjectListVo;
import com.wangyang.bioinfo.pojo.vo.ProjectVo;
import com.wangyang.bioinfo.service.ISnakemakeService;
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
@RequestMapping("/api/snakemake")
public class SnakemakeController {

    @Autowired
    private ISnakemakeService snakemakeService;

    @GetMapping
    public Page<Snakemake> page(@PageableDefault(sort = {"id"},direction =  Sort.Direction.DESC) Pageable pageable){
        Page<Snakemake> snakemakes = snakemakeService.pageBy(pageable);
        return snakemakes;
    }

    @PostMapping
    public Snakemake add(@Valid @RequestBody SnakemakeParam snakemakeParam, HttpServletRequest request){
        Snakemake snakemake = new Snakemake();
        BeanUtils.copyProperties(snakemakeParam,snakemake);
        User user = (User) request.getAttribute("user");
        snakemake.setUserId(user.getId());
        snakemake = snakemakeService.add(snakemake, user);
        return snakemake;
    }

    @GetMapping("/del/{id}")
    public Snakemake delProject(@PathVariable("id")int id,HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return snakemakeService.delBy(id);
    }

    @GetMapping("/find/{id}")
    public Snakemake findProjectById(@PathVariable("id")int id){
        Snakemake snakemake = snakemakeService.findById(id);
        return snakemake;
    }

    @PostMapping("/update/{id}")
    public Snakemake update(@PathVariable("id")int id, @Valid @RequestBody SnakemakeParam snakemakeParam, HttpServletRequest request){
        Snakemake snakemake = new Snakemake();
        BeanUtils.copyProperties(snakemakeParam,snakemake);
        User user = (User) request.getAttribute("user");
        return snakemakeService.update(id, snakemake, user);
    }
}
