package com.crp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.crp.threads.CurrencyRatesPublisherThreadsPool;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyRatesPublisherApplication implements CommandLineRunner {
	private static Logger Log = Logger.getLogger(CurrencyRatesPublisherApplication.class.getName());
	
	private static ApplicationContext appCxt = null;
	
	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		Log.info("Starting CurrencyPublisherApplication");
		SpringApplication app = new SpringApplication(CurrencyRatesPublisherApplication.class);
		app.setBannerMode(Mode.OFF);
		Log.info("Switched off application banner");
		appCxt = app.run(args);
		Log.info("Return from CurrencyPublisherApplication");
	}

	@Override
	public void run(String... args) throws Exception {
		Log.info("Running CommandLineRunner run method");
		CurrencyRatesPublisherThreadsPool publisherThreads 
		      = new CurrencyRatesPublisherThreadsPool(env.getProperty("currency.sdate"));
		
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
