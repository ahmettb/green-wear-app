package com.finalYearProject.product.utilities.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {

    private static final Charset DEFAULT_CHARSET;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol = "smtp";
    private Charset defaultEncoding = DEFAULT_CHARSET;
    private Map<String, String> properties = new HashMap<>();

    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
    }

}