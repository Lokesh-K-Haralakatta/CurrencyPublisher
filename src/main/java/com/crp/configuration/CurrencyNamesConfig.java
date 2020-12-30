package com.crp.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyNamesConfig {
	// Enum to represent different currencies
	public static enum Currency {
		USD,EUR,INR
	}
		
	private static Currency[] getCurrencyTypes() {
		return Currency.values();
	}
	
	public static Integer getCurrencyCount() {
		return Currency.values().length;
	}
	
	public static List<String> getCurrencyNames(){
		List<String> cList = new ArrayList<>();
		for(Currency cur : getCurrencyTypes())
			cList.add(cur.name());
		return cList;
	}
}
