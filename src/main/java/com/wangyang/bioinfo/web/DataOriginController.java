package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.trem.DataOrigin;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.pojo.param.DataOriginParam;
import com.wangyang.bioinfo.service.IDataOriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@RestController
@RequestMapping("/api/data_origin")
public class DataOriginController {

    @Autowired
    IDataOriginService dataOriginService;



    @GetMapping
    public Page<DataOrigin> page(BaseTermParam baseTermParam, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
        Page<DataOrigin> dataOrigins = dataOriginService.pageBy(baseTermParam,pageable);
        return dataOrigins;
    }
    @PostMapping
    public DataOrigin add(@RequestBody  DataOriginParam dataOriginParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return  dataOriginService.addDataOrigin(dataOriginParam,user);
    }
    @GetMapping("/createTSVFile")
    public void createTSVFile(HttpServletResponse response){
        dataOriginService.createTSVFile(response);
    }
    @GetMapping("/listAll")
    public List<DataOrigin> listAll(){
        return dataOriginService.listAll();
    }
}
