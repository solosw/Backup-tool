package solosw.ServerHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import solosw.ServerHelper.Controller.ZipHelper;
import solosw.ServerHelper.Controller.ZipScheduler;

import java.nio.file.Paths;

@SpringBootApplication
@EnableConfigurationProperties
public class ServerHelperApplication {

	static String username;
	public static void main(String[] args)  throws Exception{
		ConfigurableApplicationContext applicationContext=SpringApplication.run(ServerHelperApplication.class, args);
		Environment environment = applicationContext.getBean(Environment.class);
		ZipHelper.BUFFER = environment.getProperty("maxSize", Integer.class, 0);
		ZipHelper.hallFilePath = environment.getProperty("fromFilePath");
		ZipHelper.outPutPath = environment.getProperty("toFilePath");
		ZipHelper.emailTo=environment.getProperty("mail.username");
		ZipHelper.password=environment.getProperty("mail.password");
		ZipHelper.port=Integer.parseInt( environment.getProperty("mail.port"));
		//System.out.println(username);
		ZipHelper.host=environment.getProperty("mail.host");
		ZipScheduler.getInstance(). scheduleCompressAndSendEmail();
	}

}
