package com.example.demo;

import com.example.demo.integTest.FileUploadService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import javax.annotation.Resource;

@SpringBootApplication
public class DataProcessingApplication {
	public static void main(String[] args) {
		SpringApplication.run(DataProcessingApplication.class, args);
	}
}
