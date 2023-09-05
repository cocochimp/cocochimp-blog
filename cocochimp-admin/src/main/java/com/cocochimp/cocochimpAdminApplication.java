package com.cocochimp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cocochimp.mapper")
public class cocochimpAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(cocochimpAdminApplication.class,args);
    }
}
