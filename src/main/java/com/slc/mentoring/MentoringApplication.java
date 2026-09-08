package com.slc.mentoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.slc.mentoring")
public class MentoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentoringApplication.class, args);
	}

}
