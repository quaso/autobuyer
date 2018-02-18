package mq.coin.autobuyer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, AuthRequestInterceptor authRequestInterceptor){
        return builder.interceptors(authRequestInterceptor).build();
    }

}
