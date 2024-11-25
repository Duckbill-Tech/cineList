package com.duckbill.cine_list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication()
public class CineListApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CineListApplication.class, args);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/static/**")) {
			registry.addResourceHandler("/static/**")
					.addResourceLocations("classpath:/static/");
		}
	}
}