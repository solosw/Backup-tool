package solosw.ServerHelper.Controller;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Properties;

public class EmailHelper {


    private JavaMailSender mailSender;
    public void init() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(ZipHelper.host);
        mailSender.setPort(ZipHelper.port);
        mailSender.setUsername(ZipHelper.emailTo);
        mailSender.setPassword(ZipHelper.password);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.qq.com");

        // 配置邮件发送器
        // ...

        this.mailSender = mailSender;
    }
    public EmailHelper() {
       //直接初始化
        init();
    }

    public  void sendEmailWithAttachment(String to, String subject, String text, String attachmentFilePath) throws Exception {
        System.out.println("开始发送");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setFrom(ZipHelper.emailTo);
        FileSystemResource file = new FileSystemResource(new File(attachmentFilePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);

        mailSender.send(message);

    }
}

