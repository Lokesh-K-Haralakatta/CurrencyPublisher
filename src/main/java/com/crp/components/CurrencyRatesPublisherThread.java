package com.crp.components;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.crp.pojos.Currency;
import com.crp.service.CurrencyRatesRetrieveService;

public class CurrencyRatesPublisherThread implements Runnable {
	private static Logger Log = Logger.getLogger(CurrencyRatesPublisherThread.class.getName());
	
	private LocalDate latestDate = LocalDate.now();
	private LocalDate nextDate;
	private String currency;
	private String startDate;
	
	private CurrencyRatesRetrieveService retrieveService;
		
	public CurrencyRatesPublisherThread(String cur, String sDate) {
		this.currency = cur;
		try {
			startDate = sDate;
			Log.info("Injected start date: " + startDate);
			this.nextDate = LocalDate.parse(startDate);
		}
		catch(Exception e) {
			Log.warning("Exception caught while parsing startDate : " + startDate);
			this.nextDate = latestDate;
		}
		
		Log.info("Next Date: " + nextDate.toString());
		this.latestDate = latestDate.plusDays(1);
		
		retrieveService = new CurrencyRatesRetrieveService();
		Log.info("Instantiated CurrencyRetrieveService for CurrencyPublisherThread with base: " + getCurrencyThreadName());
	}
	
	@Override
	public void run() {
		Log.info("Started Currency Publisher Thread for " + currency);
		while(nextDate.isBefore(latestDate)) {
			Log.info("Getting " +currency +" rates for date: " + nextDate.toString());
			//Add here logic to get currency dates 
			//from GET https://api.exchangeratesapi.io/2010-01-12 HTTP/1.1
			try {
				Currency cRates = retrieveService.getCurrencyRatesForBase(nextDate.toString(), currency);
				Log.info("Retrieved Base Currency: " + cRates.getBase());
				Log.info("Currency rates retrieved for date: " + cRates.getDate());
				//Add logic to publish retrieved currency rates to respective Kafka topic
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.severe("Exception caught while retrieving currency rates for base: " +
							currency + " and date: " + startDate);
				Log.severe(ExceptionUtils.getStackTrace(e));
			}
			
			nextDate = nextDate.plusDays(1);
		}
		
		Log.info("Currency Publisher Thread for " + currency + " completed...");
		Thread.yield();
	}

	public String getCurrencyThreadName() {
		return currency;
	}
}
