package com.crp.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.crp.configuration.CurrencyNamesConfig;

public class CurrencyRatesPublisherThreadsPool {
	private static Logger Log = Logger.getLogger(CurrencyRatesPublisherThreadsPool.class.getName());
	
	// Threads Pool Reference
    private ExecutorService threadsPool = Executors.newFixedThreadPool(CurrencyNamesConfig.getCurrencyCount());
    
    // Currency publisher threads list
    private List<CurrencyRatesPublisherThread> threadsList = new ArrayList<>();
    
	public CurrencyRatesPublisherThreadsPool(String sDate) {
		Log.info("Instantiating Currency Publisher Threads...");
		//Instantiate separate publisher threads
		for(String curName : CurrencyNamesConfig.getCurrencyNames()) {
			threadsList.add(new CurrencyRatesPublisherThread(curName, sDate));
			Log.info("Created publisher thread for currency " + curName);
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
