package com.crp.service;

import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.crp.pojos.CurrencyModel;

@Service
public class CurrencyRatesRetrieveService {
	private static Logger Log = Logger.getLogger(CurrencyRatesRetrieveService.class.getName());
	private static final String FE_API_URL = "https://api.exchangeratesapi.io/";
	private static RestTemplate restTemplate = new RestTemplate();
	
	//Retrieves and returns the currency rates for latest date with EURO as base 
	public synchronized CurrencyModel getLatestCurrencyRates() {
		String latestCurURL = FE_API_URL + "latest";
		
		Log.info("Getting latest currency rates from " + latestCurURL);
		
		CurrencyModel response = null;
		try {
			response = restTemplate.getForObject(latestCurURL, CurrencyModel.class);
			Log.info("Latest currency rates retrieval successful");
		}
		catch(Exception e) {
			Log.severe("Exception caught while retrieving latest currency rates");
			Log.severe(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	//Retrieves and returns the currency rates for given date with EURO as base
	//Given date string should be in the format YYYY-MM-DD
	public synchronized CurrencyModel getCurrencyRatesForDate(final String date) {
		String curURLWithDate = FE_API_URL + date;
		
		Log.info("Getting currency rates from " + curURLWithDate);
		
		CurrencyModel response = null;
		try {
			response = restTemplate.getForObject(curURLWithDate, CurrencyModel.class);
			Log.info("Currency rates retrieval for " + date + " successful");
		}
		catch(Exception e) {
			Log.severe("Exception caught while retrieving currency rates for " + date);
			Log.severe(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	//Retrieves and returns the currency rates for given base
	//Given date string should be in the format YYYY-MM-DD
	//Base string should be one from list supported by api.exchangeratesapi.io
	public synchronized CurrencyModel getCurrencyRatesForBase(final String date, final String base) {
		String curURL = FE_API_URL + date + "?base=" + base;
		
		Log.info("Getting currency rates from " + curURL);
		
		CurrencyModel response = null;
		try {
			response = restTemplate.getForObject(curURL, CurrencyModel.class);
			Log.info("Currency rates retrieval for base " + response.getBase() + " with date " + 
						response.getDate() + " successful");
		}
		catch(Exception e) {
			Log.severe("Exception caught while retrieving currency rates for base " + base);
			Log.severe(ExceptionUtils.getStackTrace(e));
		}
		
		return response;
	}

}
