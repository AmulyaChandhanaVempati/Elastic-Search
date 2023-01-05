package com.example.projectpractice;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class ProjectpracticeApplication {
	
	public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
		System.out.println("Application Starts....");
		
		SpringApplication.run(ProjectpracticeApplication.class, args);
	}


}
