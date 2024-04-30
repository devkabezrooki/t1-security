package com.example.t1security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class T1SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(T1SecurityApplication.class, args);
	}

}
