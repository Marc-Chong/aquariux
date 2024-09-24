package com.Marc.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
@Service
public class TestApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
		System.out.println("Hello world!");
	}
}
