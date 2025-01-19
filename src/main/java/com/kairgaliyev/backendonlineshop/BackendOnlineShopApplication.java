package com.kairgaliyev.backendonlineshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.kairgaliyev.backendonlineshop.model")
@EnableJpaRepositories(basePackages = "com.kairgaliyev.backendonlineshop.repository")
public class BackendOnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendOnlineShopApplication.class, args);
    }

}
