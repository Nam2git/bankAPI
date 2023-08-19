package com.ws.tla;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ws.tla")
public class BankingApiApplication {

    @Generated
    public static void main(String[] args) {
        SpringApplication.run(BankingApiApplication.class, args);
    }

}
