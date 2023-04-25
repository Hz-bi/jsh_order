package com.lala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
@EnableCaching
@EnableSwagger2
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class LaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LaApplication.class, args);
    }
}
