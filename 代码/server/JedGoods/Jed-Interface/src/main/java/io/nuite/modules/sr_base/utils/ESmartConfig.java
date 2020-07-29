package io.nuite.modules.sr_base.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: yangchuang
 * @Date: 2018/8/10 9:07
 * @Version: 1.0
 * @Description:
 */

@ConfigurationProperties(prefix = "esmart")
@Component
public class ESmartConfig {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
