package mq.coin.autobuyer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("app.coin")
@Data
public class AppProperties {
    private String apiKey;
    private String apiSecret;
}
