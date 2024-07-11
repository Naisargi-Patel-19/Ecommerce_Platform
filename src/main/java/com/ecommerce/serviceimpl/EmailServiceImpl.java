package com.ecommerce.serviceimpl;

import com.ecommerce.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.mail.javamail.MimeMessageHelper.*;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender;
    private Configuration configuration;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender,Configuration configuration){
        this.javaMailSender = javaMailSender;
        this.configuration = configuration;
    }

    @Override
    @Async("asyncTaskExecutor")
    public void sendEmail(String to, String subject, Map<String, Object> model) {
        log.info("Thread being used for Email sending is : " + Thread.currentThread().getName());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.addAttachment("Async_Config.png", new ClassPathResource("Async_Config.png"));
            helper.addAttachment("Best_Practice.png", new ClassPathResource("Best_Practice.png"));
            helper.addAttachment("CustomExceptionClass.png", new ClassPathResource("CustomExceptionClass.png"));
            helper.addAttachment("Email_Service.png", new ClassPathResource("Email_Service.png"));
            helper.addAttachment("Profile_Setting.png", new ClassPathResource("Profile_Setting.png"));
            helper.addAttachment("Upload_Multipart_File.png", new ClassPathResource("Upload_Multipart_File.png"));

            Template template = configuration.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setTo(to);
            helper.setText(html, true);
            helper.setSubject(subject);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error sending email: ", e);
            e.printStackTrace();
        }
    }
}
