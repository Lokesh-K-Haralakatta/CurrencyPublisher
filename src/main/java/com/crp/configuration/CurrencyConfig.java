package com.crp.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyConfig {
	// Enum to represent different currencies
	public static enum Currency {
		USD,EUR,INR
	}
		
	public static Currency[] getCurrencyTypes() {
		return Currency.values();
	}
	
	public static Integer getCurrencyCount() {
		return Currency.values().length;
	}
}
