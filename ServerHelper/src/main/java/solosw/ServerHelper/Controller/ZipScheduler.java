package solosw.ServerHelper.Controller;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ZipScheduler {

    EmailHelper emailHelper=new EmailHelper();
   private static ZipScheduler instance;
    public static ZipScheduler getInstance(){
        if(instance==null) instance=new ZipScheduler();
        return instance;
    }

    public void scheduleCompressAndSendEmail() throws Exception{
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            try{
                System.out.println("开始任务");
                ZipHelper. compressDirectory(new File(ZipHelper.hallFilePath), new File(ZipHelper. outPutPath));
                System.out.println("压缩成功");
                emailHelper.sendEmailWithAttachment(ZipHelper.emailTo, "Weekly Zip File", "Please find the attached ZIP file.", ZipHelper.outPutPath);
            }catch (Exception e)
            {
                System.out.println(e);
            }

        };

        executor.scheduleAtFixedRate(task, 0, 7, TimeUnit.DAYS); // 每隔一周执行一次任务

        // 关闭线程池
        //executor.shutdown();
    }
}
