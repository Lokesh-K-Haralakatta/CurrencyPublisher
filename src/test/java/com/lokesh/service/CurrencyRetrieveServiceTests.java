package com.lokesh.service;

import org.junit.jupiter.api.Test;

import com.lokesh.pojos.Currency;

class CurrencyRetrieveServiceTests {

	private CurrencyRetrieveService currencyService;
	
	@Test
	void testGetLatestCurrencyRates() {
		currencyService = new CurrencyRetrieveService();
		Currency latestRates = currencyService.getLatestCurrencyRates();
		
		/*System.out.println("Base Currency: " + latestRates.getBase());
		System.out.println("Date: " + latestRates.getDate());
		System.out.println("Currency Rates: ");
		Map<String,Double> cRates = latestRates.getRates();
		for(String cur : cRates.keySet())
			System.out.println(cur + " ==> " + cRates.get(cur));
		*/
		
		assert(latestRates.getBase().equals("EUR"));
		
		assert(latestRates.getRates().containsKey("USD"));
		assert(latestRates.getRates().containsKey("GBP"));
		assert(latestRates.getRates().containsKey("JPY"));
		assert(latestRates.getRates().containsKey("SGD"));
		assert(latestRates.getRates().containsKey("INR"));
	}

}

