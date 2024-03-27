package com.clipg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 77507
 */
@SpringBootApplication
@EnableAsync
public class Work4Application {

    public static void main(String[] args) {
        SpringApplication.run(Work4Application.class, args);
    }

}
