package com.wangyang.bioinfo.service;

import javax.mail.MessagingException;

/**
 * @author Likaifeng
 * @date 2021/6/22
 * 简单文本邮件
 */
public interface MailService {

    /**
     * 简单文本邮件
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param contnet 邮件内容
     */
    void sendSimpleMail(String to, String subject, String contnet);

    /**
     * HTML 文本邮件
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     */
    void sendHtmlMail(String to, String subject, String contnet) throws MessagingException;

    /**
     * 附件邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     * @param filePath 附件路径
     */
    void sendAttachmentsMail(String to, String subject, String contnet, String filePath) throws MessagingException;

    /**
     * 图片邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     * @param rscPath 图片路径
     * @param rscId 图片ID
     */
    void sendInlinkResourceMail(String to, String subject, String contnet, String rscPath, String rscId) throws MessagingException;
}
