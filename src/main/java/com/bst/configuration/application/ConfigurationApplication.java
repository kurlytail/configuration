package com.bst.configuration.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigurationApplication extends SpringBootServletInitializer {
	
	public static void main(final String[] args) {
		SpringApplication.run(ConfigurationApplication.class, args);
	}

}
