package com.wangyang.bioinfo.util;

import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.dto.FileDTO;
import com.wangyang.bioinfo.pojo.dto.term.BaseFileDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static  <FILE extends BaseFile> FILE checkPath(BaseFileDTO baseFileDTO, FILE file){

//        if(baseFileParam.getFileType()==null){
//            String extension = FilenameUtils.getExtension(baseFileParam.getAbsolutePath());
//            if(extension.equals("")){
//                throw new BioinfoException("路径必须添加后缀名！");
//            }
//            file.setFileType(extension);
//        }

//        if(baseFileParam.getFileName()==null){
//            String basename = FilenameUtils.getBasename(baseFileParam.getAbsolutePath());
//            file.setFileName(basename);
//        }
        if(baseFileDTO.getRelativePath()==null){
            String relativePath = FilenameUtils.relativePath(baseFileDTO.getAbsolutePath());
            file.setRelativePath(relativePath);
        }
        return file;
    }

    public static  List<FileDTO> listPath(String strPath){
        String bashPtah = CacheStore.getValue("workDir");
        Path path = Paths.get(bashPtah, strPath);
        List<FileDTO> list = new ArrayList<>();
        findFileList(strPath,path.toFile(),list);
        return list;
    }

    public static void findFileList(String strPath,File dir, List<FileDTO> fileNames) {
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            return;
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                FileDTO fileDTO = new FileDTO();
                fileDTO.setAbsolutePath(dir + File.separator + file.getName());
                fileDTO.setFileName(file.getName());
                fileDTO.setRelativePath(strPath+File.separator+file.getName());
                fileNames.add(fileDTO);// 添加文件全路径名
            } else {// 如果是目录
                findFileList(strPath,file, fileNames);// 回调自身继续查询
            }
        }
    }
    public static Path getJarResources(String resourceName) {
        try {
            Path source ;
            URI templateUri = ResourceUtils.getURL("classpath:"+resourceName).toURI();

            if ("jar".equalsIgnoreCase(templateUri.getScheme())) {
                // Create new file system for jar
                FileSystem fileSystem = getFileSystem(templateUri);
                source = fileSystem.getPath("/BOOT-INF/classes/" + resourceName);
            } else {
                source = Paths.get(templateUri);
            }
            return source;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean writeFile(String content,String strPath){
        FileWriter fileWriter=null;
        try {
            Path path = Paths.get(strPath);
            Files.createDirectories(path.getParent());
            File file = path.toFile();
            if(file.exists()){
                file.delete();
            }
            fileWriter = new FileWriter(file,false);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(fileWriter!=null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
    private static FileSystem getFileSystem(@NonNull URI uri) throws IOException {
        Assert.notNull(uri, "Uri must not be null");

        FileSystem fileSystem;

        try {
            fileSystem = FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        }

        return fileSystem;
    }
}
