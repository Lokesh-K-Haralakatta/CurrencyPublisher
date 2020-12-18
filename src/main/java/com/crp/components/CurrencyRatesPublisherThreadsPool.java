package com.crp.components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class CurrencyRatesPublisherThreadsPool {
	private static Logger Log = Logger.getLogger(CurrencyRatesPublisherThreadsPool.class.getName());
	
	// Enum to represent different currencies
	private enum Currency {
		USD,EUR,INR
	}
	
	// Maximum number of threads in thread pool 
    private static final int MAX_T = Currency.values().length;
    
    // Threads Pool Reference
    private ExecutorService threadsPool = Executors.newFixedThreadPool(MAX_T);
    
    // Currency publisher threads list
    private List<CurrencyRatesPublisherThread> threadsList = new ArrayList<>();
    
	public CurrencyRatesPublisherThreadsPool(String sDate) {
		Log.info("Instantiating Currency Publisher Threads...");
		//Instantiate separate publisher threads
		for(Currency cur : Currency.values()) {
			threadsList.add(new CurrencyRatesPublisherThread(cur.name(), sDate));
			Log.info("Created publisher thread for currency " + cur.name());
		}
		Log.info("Currency Publisher Threads instantiation done...");
	}
	
	public void executeCurrencyPulisherThreads() {
		Log.info("Adding CurrencyPublisher Threads to threads pool for execution");
		for(CurrencyRatesPublisherThread task: threadsList) {
			threadsPool.execute(task);
			Log.info("Currency Publisher Thread - " + task.getCurrencyThreadName() 
			      + " added to threads pool for execution...");
		}
		
		Log.info("Added all currency threads to pool and waiting for completion...");
		threadsPool.shutdown();
	}
}
