package com.epic.followup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

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

    public static void main(String[] args){
        // 初始化Spring Application
        SpringApplication.run(FollowUpApplication.class, args);
    }

}
