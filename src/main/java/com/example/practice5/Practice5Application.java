package com.example.practice5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Practice5Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Practice5Application.class, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
