package top.flya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
/**
 * 启动程序
 *
 * @author ruoyi
 */

@SpringBootApplication
public class RuoYiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "true");
        SpringApplication application = new SpringApplication(RuoYiApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("Hello,World!");
    }

}
