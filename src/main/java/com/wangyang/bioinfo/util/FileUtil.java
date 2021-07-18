package com.wangyang.bioinfo.util;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.param.BaseFileParam;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author wangyang
 * @date 2021/7/17
 */
public class FileUtil {

    //https://www.cnblogs.com/chengxuxiaoyuan/p/12329145.html
    //https://blog.csdn.net/xiongyouqiang/article/details/80488202
    public static ResponseEntity<byte[]> downloadByResponseEntity(String filename){
        try {
            byte[] body = null;
            if(false){
                File file = new File("E://123.jpg");
                InputStream is = new FileInputStream(file);
                body = new byte[is.available()];
                is.read(body);
            }else {
                body = FileUtils.readFileToByteArray(new File(filename));
            }


            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + filename);
            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
            return entity;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void download(String filename, String filePath,
                                HttpServletRequest request, HttpServletResponse response){
        //设置响应头和客户端保存文件名
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        long downloadedLength = 0l;
        try {
            //打开本地文件流
            InputStream inputStream = new FileInputStream(filePath);
            //激活下载操作
            OutputStream os = response.getOutputStream();

            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
                downloadedLength += b.length;
            }

            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  <FILE extends BaseFile> FILE checkPath(BaseFileParam baseFileParam,FILE file){

        if(baseFileParam.getFileType()==null){
            String extension = FilenameUtils.getExtension(baseFileParam.getAbsolutePath());
            if(extension.equals("")){
                throw new BioinfoException("路径必须添加后缀名！");
            }
            file.setFileType(extension);
        }

        if(baseFileParam.getFileName()==null){
            String basename = FilenameUtils.getBasename(baseFileParam.getAbsolutePath());
            file.setFileName(basename);
        }
        if(baseFileParam.getRelativePath()==null){
            String relativePath = FilenameUtils.relativePath(baseFileParam.getAbsolutePath());
            file.setRelativePath(relativePath);
        }
        return file;
    }


}
