package com.appsdeveloperblog.photoapp.api.users;

import feign.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class PhotoAppApiUsersApplication {
    public static final String MYAPPLICATION_ENVIRONMENT = "myapplication.environment";
    org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private Environment environment;
    private final String MY_APPLICATION_ENVIRONMENT = "Development bean created. myapplication.environment = " + environment.getProperty(MYAPPLICATION_ENVIRONMENT);
    public final String NOT_PRODUCTION_BEAN_CREATED_MYAPPLICATION_ENVIRONMENT = "Not Production bean created. myapplication.environment = " + environment.getProperty(MYAPPLICATION_ENVIRONMENT);
    public final String PRODUCTION_BEAN_CREATED_MYAPPLICATION_ENVIRONMENT = "Production bean created. myapplication.environment = " + environment.getProperty(MYAPPLICATION_ENVIRONMENT);

    @Autowired
    public PhotoAppApiUsersApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(PhotoAppApiUsersApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile("production")
    Logger.Level feignLoggerLevel() {
        return Logger.Level.NONE;
    }

    @Bean
    @Profile("!production")
    Logger.Level feignDefaultLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @Profile("production")
    public String createProductionBean() {
        logger.info(PRODUCTION_BEAN_CREATED_MYAPPLICATION_ENVIRONMENT);
        return "Production bean";
    }

    @Bean
    @Profile("!production")
    public String createNotProductionBean() {
        logger.info(NOT_PRODUCTION_BEAN_CREATED_MYAPPLICATION_ENVIRONMENT);
        return "Not production bean";
    }

    @Bean
    @Profile("default")
    public String createDevelopmentBean() {
        logger.info(MY_APPLICATION_ENVIRONMENT);
        return "Development bean";
    }
}
