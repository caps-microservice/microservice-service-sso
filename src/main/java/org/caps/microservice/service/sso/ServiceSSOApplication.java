package org.caps.microservice.service.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;
@EnableEurekaClient
/*Eureka客户端*/

@SpringBootApplication
@EnableDiscoveryClient
/*注册到服务中心*/

@EnableFeignClients
/*开启 Feign 功能*/

@MapperScan(basePackages = "org.caps.microservice.service.sso.mapper")
public class ServiceSSOApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSSOApplication.class,args);
    }
}
