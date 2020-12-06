package com.lokesh.publisher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyPublisherApplication implements CommandLineRunner {
	private static Logger Log = Logger.getLogger(CurrencyPublisherApplication.class.getName());
	private static ApplicationContext appCxt = null;
	
	@Autowired
	private CurrencyPublisherThreadsPool publisherThreads;
	
	public static void main(String[] args) {
		Log.info("Starting CurrencyPublisherApplication");
		SpringApplication app = new SpringApplication(CurrencyPublisherApplication.class);
		app.setBannerMode(Mode.OFF);
		Log.info("Switched off application banner");
		appCxt = app.run(args);
		Log.info("Return from CurrencyPublisherApplication");
	}

	@Override
	public void run(String... args) throws Exception {
		Log.info("Running CommandLineRunner run method");
		publisherThreads.executeCurrencyPulisherThreads();
		Log.info("Return from CommandLineRunner run method");
	}

	public static ApplicationContext getApplicationContext() {
		return appCxt;
	}
	
	@Bean
    public ExitCodeGenerator exitCodeGenerator() {
		return () -> 42;
    }
}
