package com.example.bibliotheque_quali_dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BibliothequeQualiDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliothequeQualiDevApplication.class, args);
    }

}
