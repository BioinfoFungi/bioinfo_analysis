package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.dto.FileDTO;
import com.wangyang.bioinfo.util.BaseResponse;
import com.wangyang.bioinfo.util.CacheStore;
import com.wangyang.bioinfo.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/17
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @RequestMapping("/{filename}")
    public BaseResponse download(@PathVariable("filename") String filename,
                                 HttpServletRequest request, HttpServletResponse response){
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = FileUtils.readFileToByteArray(new File(CacheStore.getValue("workDir")+"/data/"+filename));
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("Content-Disposition","attachment;filename="+filename);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            return BaseResponse.ok("success!");
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResponse.error(e.getMessage());
        }

    }
    @GetMapping("/list")
    public List<FileDTO> listPath(@RequestParam("path") String path){
        return FileUtil.listPath(path);
    }
}
