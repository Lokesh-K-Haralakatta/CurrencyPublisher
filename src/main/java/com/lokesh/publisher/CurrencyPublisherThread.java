package com.lokesh.publisher;

import java.util.Date;
import java.util.logging.Logger;

public class CurrencyPublisherThread implements Runnable {
	private static Logger Log = Logger.getLogger(CurrencyPublisherThread.class.getName());
	//private static Date START_DATE = new Date("1999-01-04");
	
	private String currency;
	
	
	public CurrencyPublisherThread(String cur) {
		this.currency = cur;
	}
	
	@Override
	public void run() {
		Log.info("Started Currency Publisher Thread for " + currency);
		int count = 1;
		while(count != 10) {
			Log.info("Getting " +currency +" rate for date: YYYY-MM-DD");
			//Add here logic to get currency dates 
			//from GET https://api.exchangeratesapi.io/2010-01-12 HTTP/1.1
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}
		Log.info("Currency Publisher Thread for " + currency + " completed...");
	}

	public String getCurrencyThreadName() {
		return currency;
	}
}
