package com.gabrielferreira02.springmail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringmailApplication.class, args);
		log.info("SpringMail application is running");
	}

}
