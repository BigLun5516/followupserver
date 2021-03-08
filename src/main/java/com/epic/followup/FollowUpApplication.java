package com.epic.followup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author : zx
 * @version V1.0
 */

@EnableJpaAuditing
@SpringBootApplication
public class FollowUpApplication {

    /**
     * 初始化restTemplate
     * @return RestTemplate
     */
    @Bean(name = "restTemplate")
    public RestTemplate initRestTempleta(){
        return new RestTemplate();
    }


    @PostConstruct
    void started() {
        //时区设置：中国上海
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static void main(String[] args){
        // 初始化Spring Application
        SpringApplication.run(FollowUpApplication.class, args);
    }

}
