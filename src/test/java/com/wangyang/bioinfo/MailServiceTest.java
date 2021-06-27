package com.wangyang.bioinfo;

import com.wangyang.bioinfo.service.MailService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * 作者 likaifeng
 * 邮件测试
 */

//@RunWith(SpringRunner.class)
@Ignore
//@SpringBootTest(classes = BioinfoApplication.class)
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    @Resource
    private TemplateEngine templateEngine;

//    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("1749748955@qq.com","测试spring boot imail-主题","测试spring boot imail - 内容");
    }

//    @Test
    public void sendHtmlMail() throws MessagingException {

        String content = "<html>\n" +
                "<body>\n" +
                "<h3>hello world</h3>\n" +
                "<h1>html</h1>\n" +
                "<body>\n" +
                "</html>\n";

        mailService.sendHtmlMail("1749748955@qq.com","这是一封HTML邮件",content);
    }

//    @Test
    public void sendAttachmentsMail() throws MessagingException {
        String filePath = "C:/Users/Administrator/Desktop/aaa.png";
        String content = "<html>\n" +
                "<body>\n" +
                "<h3>hello world</h3>\n" +
                "<h1>html</h1>\n" +
                "<h1>附件传输</h1>\n" +
                "<body>\n" +
                "</html>\n";
        mailService.sendAttachmentsMail("3300297450@qq.com","这是一封HTML邮件",content, filePath);
    }

//    @Test
    public void sendInlinkResourceMail() throws MessagingException {
        //TODO 改为本地图片目录
        String imgPath = "C:/Users/Administrator/Desktop/aaa.png";  //?raw=true
        String rscId = "doc0001";
        String content = "<html>" +
                "<body>" +
                "<h3>图片</h3>" +
                "<h1>html</h1>" +
                "<h1>图片邮件</h1>" +
                "<img src='cid:"+rscId+"'></img>" +
                "<body>" +
                "</html>";

        mailService.sendInlinkResourceMail("3300297450@qq.com","这是一封图片邮件",content, imgPath, rscId);
    }

//    @Test
    public void testTemplateMailTest() throws MessagingException {
        Context context = new Context();
        context.setVariable("id","wangyang");

        String emailContent = templateEngine.process("emailTeplate", context);
        mailService.sendHtmlMail("1749748955@qq.com","这是一封HTML模板邮件",emailContent);

    }
}
