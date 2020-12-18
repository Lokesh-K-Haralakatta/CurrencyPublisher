package com.crp.test.service;

import org.junit.jupiter.api.Test;

import com.crp.pojos.Currency;
import com.crp.service.CurrencyRatesRetrieveService;

class CurrencyRatesRetrieveServiceTests {

	private CurrencyRatesRetrieveService currencyService = new CurrencyRatesRetrieveService();
	
	@Test
	void testGetLatestCurrencyRates() {
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

	@Test
	void testGetCurrencyRatesForDate() {
		String date = "2019-10-30";
		Currency currencyRates = currencyService.getCurrencyRatesForDate(date);
		
		assert(currencyRates.getBase().equals("EUR"));
		assert(currencyRates.getDate().equals(date));
		assert(currencyRates.getRates().containsKey("USD"));
		assert(currencyRates.getRates().containsKey("GBP"));
		assert(currencyRates.getRates().containsKey("JPY"));
		assert(currencyRates.getRates().containsKey("SGD"));
		assert(currencyRates.getRates().containsKey("INR"));
	}
	
	@Test
	void testGetCurrencyRatesForBase() {
		String date = "2019-10-30";
		String base = "INR";
		
		Currency currencyRates = currencyService.getCurrencyRatesForBase(date, base);
		
		assert(currencyRates.getBase().equals(base));
		assert(currencyRates.getDate().equals(date));
		
		assert(currencyRates.getRates().containsKey("USD"));
		assert(currencyRates.getRates().containsKey("GBP"));
		assert(currencyRates.getRates().containsKey("JPY"));
		assert(currencyRates.getRates().containsKey("SGD"));
		assert(currencyRates.getRates().containsKey("INR"));
	}
	
}

