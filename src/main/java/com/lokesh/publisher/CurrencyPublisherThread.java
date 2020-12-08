package com.lokesh.publisher;

import java.time.LocalDate;
import java.util.logging.Logger;

public class CurrencyPublisherThread implements Runnable {
	private static Logger Log = Logger.getLogger(CurrencyPublisherThread.class.getName());
		
	private LocalDate latestDate = LocalDate.now();
	private LocalDate nextDate;
	private String currency;
	
	public CurrencyPublisherThread(String cur, String startDate) {
		this.currency = cur;
		try {
			Log.info("Injected start date: " + startDate);
			this.nextDate = LocalDate.parse(startDate);
		}
		catch(Exception e) {
			Log.warning("Exception caught while parsing startDate : " + startDate);
			this.nextDate = latestDate;
		}
		
		Log.info("Next Date: " + nextDate.toString());
		this.latestDate = latestDate.plusDays(1);
	}
	
	@Override
	public void run() {
		Log.info("Started Currency Publisher Thread for " + currency);
		while(nextDate.isBefore(latestDate)) {
			Log.info("Getting " +currency +" rates for date: " + nextDate.toString());
			//Add here logic to get currency dates 
			//from GET https://api.exchangeratesapi.io/2010-01-12 HTTP/1.1
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
