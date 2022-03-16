package com.work.bookseller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
//@PropertySource("classpath:application-${spring.profiles.active:default}.properties")
//@PropertySource("classpath:application-${spring.profiles.active:default}.properties")
@Profile("default")
@EnableJpaAuditing
public class BookSellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSellerApplication.class, args);
    }

}
