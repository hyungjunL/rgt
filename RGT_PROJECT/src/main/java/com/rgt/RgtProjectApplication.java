package com.rgt;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RgtProjectApplication {

	public static void main(String[] args) {
		
		ConfigurableApplicationContext context = new SpringApplicationBuilder(RgtProjectApplication.class).run(args);
	}

}
