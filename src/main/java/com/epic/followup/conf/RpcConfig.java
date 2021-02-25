package com.epic.followup.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author : zx
 * @version V1.0
 */

@Component
@PropertySource( value = "classpath:rpc.properties")
@ConfigurationProperties(prefix="rpc")
public class RpcConfig {

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
