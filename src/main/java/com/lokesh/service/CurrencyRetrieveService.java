package com.lokesh.service;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.lokesh.pojos.Currency;

@Component
public class CurrencyRetrieveService {
	private static Logger Log = Logger.getLogger(CurrencyRetrieveService.class.getName());
	private static final String FE_API_URL = "https://api.exchangeratesapi.io/";
	
	public Currency getLatestCurrencyRates() {
		String latestCurURL = FE_API_URL + "latest";
		
		Log.info("Getting latest currency rates from " + latestCurURL);
		RestTemplate restTemplate = new RestTemplate();
		
		Currency response = restTemplate.getForObject(latestCurURL, Currency.class);
		
		return response;
	}
}
