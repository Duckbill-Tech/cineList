package com.duckbill.cine_list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.duckbill.cine_list"})
public class CineListApplication {

	public static void main(String[] args) {
		SpringApplication.run(CineListApplication.class, args);
	}
}