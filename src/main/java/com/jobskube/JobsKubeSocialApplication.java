package com.jobskube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = {"http://localhost:3000", "http://jobskube.com"})
public class JobsKubeSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsKubeSocialApplication.class, args);
	}
}
