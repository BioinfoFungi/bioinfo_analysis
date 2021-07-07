package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.Cancer;
import com.wangyang.bioinfo.pojo.Project;
import com.wangyang.bioinfo.pojo.Study;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.CancerParam;
import com.wangyang.bioinfo.pojo.vo.ProjectListVo;
import com.wangyang.bioinfo.service.ICancerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@RestController
@RequestMapping("/api/cancer")
public class CancerController {
    @Autowired
    ICancerService cancerService;

    @GetMapping
    public Page<Cancer> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<Cancer> cancers = cancerService.pageBy(baseTermParam,pageable);
        return cancers;
    }

    @PostMapping
    public Cancer add(@RequestBody  CancerParam cancerParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  cancerService.addCancer(cancerParam,user);
    }

    @GetMapping("/listAll")
    public List<Cancer> listAll(){
        return cancerService.listAll();
    }
}
