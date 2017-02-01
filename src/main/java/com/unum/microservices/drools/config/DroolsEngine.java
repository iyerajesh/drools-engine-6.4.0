package com.unum.microservices.drools.config;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*
 * @author Rajesh Iyer
 */

@Configuration
@ComponentScan({ "com.unum.microservices" })
@EnableAutoConfiguration

public class DroolsEngine {

	private static final Logger logger = LoggerFactory.getLogger(DroolsEngine.class);

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DroolsEngine.class, args);
	}

}
