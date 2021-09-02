package com.wangyang.bioinfo.web;

import com.github.rcaller.graphics.SkyTheme;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.websocket.WebSocketServer;
import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.util.CacheStore;
import lombok.extern.slf4j.Slf4j;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


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
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Autowired
    WebSocketServer webSocketHandler;
    @Value("${bioinfo.download_url}")
    private  String download_url;

    @GetMapping("/testExecutor")
    @ResponseBody
    public String testExecutor(){
        BlockingQueue<Runnable> queue = executor.getThreadPoolExecutor().getQueue();
        System.out.println(queue.size());
        return "queue size:"+ queue.size();
    }

//    @RequestMapping("/")
//    public void index(HttpServletResponse response) throws IOException {
//        response.sendRedirect("index.html");
//    }
    @GetMapping("/api/global")
    @ResponseBody
    @Anonymous
    public Map<String,String> globalConfig(){
        Map<String,String> map = new HashMap<>();
        map.put("attachment", CacheStore.getValue("workDir")+"/upload");
        map.put("cancerStudy", CacheStore.getValue("workDir")+"/data");
        map.put("Attachment_base_url", CacheStore.getValue("Attachment_base_url"));
        map.put("download_url", download_url);
        return map;
    }


    @GetMapping("/test")
    @ResponseBody
    @Anonymous
    public String test(String fromUser,String toUser,String msg){
        System.out.println("-----test before");
        testAsync.testAsync();
        webSocketHandler.sendMessageToUser(fromUser,toUser,msg);
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
    //http://coolaf.com/tool/chattest
    //ws://localhost:8080/websocket/socketServer.do
    @RequestMapping("/Rimg")
    @Async("taskExecutor")
    @ResponseBody
    public void testRImg(@RequestParam("file")String file) throws IOException, REngineException, REXPMismatchException {
        System.out.printf(file);
        RConnection c = new RConnection();

//        c.eval("library(maftools)");
//        c.eval("laml.maf = system.file('extdata', 'tcga_laml.maf.gz', package = 'maftools')");
//        c.eval("laml.clin = system.file('extdata', 'tcga_laml_annot.tsv', package = 'maftools') ");
//        c.eval("laml = read.maf(maf = laml.maf, clinicalData = laml.clin)");
//        String string = c.eval("hgd_inline({plot.new();oncoplot(maf = laml)})").asString();
        c.eval("library(httpgd)");
        c.eval("library(ggplot2)");
        c.eval("deg <- readr::read_tsv(\"/home/wangyang/workspace/www/data/TCGADOWNLOAD/data/TCGA_"+file+"_DESeq2.tsv\")");
        c.eval("deg <- dplyr::mutate(deg,direction = factor(ifelse(padj < 0.01 & abs(log2FoldChange)>2,\n" +
                "    ifelse(log2FoldChange>0,\"Up\",\"Down\"),\"NS\"),levels = c(\"Up\",\"Down\",\"NS\")))");

        c.eval("p <- ggplot(deg,aes(x=log2FoldChange,y=-log10(padj),colour=direction))+geom_point(alpha=0.6)");
        String string = c.eval("hgd_inline({plot.new();print(p)})").asString();
        c.close();
//        webSocketHandler.sendMessageToUsers(new TextMessage(string));

//        httpServletResponse.setContentType("image/svg+xml;charset=utf-8");
//        OutputStream os = httpServletResponse.getOutputStream();
//        os.write(string.getBytes());
////        os.write(base64.);
//        os.flush();
//        os.close();
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
        caller.runAndReturnResult("a");
    }

    @RequestMapping("/testJavaCallOnline")
    @ResponseBody
    public void testJavaCallOnline(HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse) throws IOException {

        RCaller rcaller = RCaller.create();
        rcaller.redirectROutputToStream(httpServletResponse.getOutputStream());

        RCode code = RCode.create();
        code.addRCode("b<-1:10");
        code.addRCode("m<-mean(b)");

        rcaller.runAndReturnResultOnline("m");
        code.addRCode("b<-1:10");
        code.addRCode("m<-mean(b)");

        rcaller.runAndReturnResultOnline("m");
        rcaller.stopRCallerOnline();

    }

    @RequestMapping("/testCmd")
    @ResponseBody
    public void testCmd(){
        try {
            ProcessBuilder pb = new ProcessBuilder("");
            Process process =pb.start();
            OutputStream outputStream = process.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @GetMapping("/testSleep")
    @ResponseBody
    public String testSleep() throws InterruptedException {
        Thread.sleep(3000);
        return "success!";
    }
}
