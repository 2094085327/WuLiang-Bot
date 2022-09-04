package simbot.example;

import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 启动类。
 * 其中，{@link SpringBootApplication} 为springboot的启动注解，
 * {@link EnableSimbot} 为simbot在springboot-starter下的启动注解。
 *
 * @author ForteScarlet
 *
 * MapperScan: 指定要扫描的mapper的位置
 */
@EnableSimbot
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class SimbotExampleApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SimbotExampleApplication.class);
        builder.headless(false).run(args);
    }

}
