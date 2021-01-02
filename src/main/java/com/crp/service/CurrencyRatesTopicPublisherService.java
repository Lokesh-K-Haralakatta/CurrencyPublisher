package com.crp.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.crp.configuration.CurrencyNamesConfig;
import com.crp.pojos.CurrencyModel;

@Service
public class CurrencyRatesTopicPublisherService {
	private static Logger Log = Logger.getLogger(CurrencyRatesTopicPublisherService.class.getName());
	
	@Autowired
	private ApplicationContext ctx;
	
	private KafkaTemplate<String,CurrencyModel> kafkaTemplate;
	
	private ProducerFactory<String, CurrencyModel> producerFactory() {
	    return new DefaultKafkaProducerFactory<>(producerConfigs());
	}
	
	private Map<String, Object> producerConfigs() {
	    Map<String, Object> props = new HashMap<>();
	    props.put("bootstrap.servers", "localhost:9092");
	    props.put("key.serializer", StringSerializer.class);
	    props.put("value.serializer", JsonSerializer.class);
	    Log.info("ProducerConfig: " + props.toString());
	    return props;
	}
	
	private KafkaTemplate<String, CurrencyModel> kafkaTemplate() {
	    KafkaTemplate<String, CurrencyModel> kTemplate = new KafkaTemplate<String, CurrencyModel>(producerFactory());
	    Log.info("Kafka Template instantiated with properties: " +
	               kTemplate.getProducerFactory().getConfigurationProperties());
	    
	    return kTemplate;
	}
	
	public CurrencyRatesTopicPublisherService() {
		kafkaTemplate = this.kafkaTemplate();
	}
	
	public boolean checkKafkaTopicsStatus() {
		List<String> beansList  = Arrays.asList(ctx.getBeanDefinitionNames());
		Log.info("Defined Bean Names: " + beansList.toString());
		
		return beansList.containsAll(CurrencyNamesConfig.getCurrencyNames());
		
	}
	
	public void publishCurrencyRate(final CurrencyModel currency) {
		Log.info("Received request to publish currency rate for base currency: " + currency.getBase() + " of " + currency.getDate());
		try {
				ListenableFuture<SendResult<String, CurrencyModel>> future = kafkaTemplate.send(currency.getBase(), currency);
				Log.info("Initiated currency rate send using Kafka Template Instance, attaching callback...");		
				//Add callback to be called after send completes
				future.addCallback(new ListenableFutureCallback<SendResult<String, CurrencyModel>>() {
					@Override
					public void onSuccess(SendResult<String, CurrencyModel> result) {
						Log.info("Publish currency rate for topic [ " + result.getProducerRecord().topic() + 
								" ] with offset=[ " + result.getRecordMetadata().offset() + " ]");
					}
					@Override
					public void onFailure(Throwable ex) {
						Log.severe("Unable to publish currency rate due to : " + ex.getMessage());
					}
				});
		}catch(Exception e) {
			Log.severe("Exception caught while trying to publish currency rate...");
			Log.severe(ExceptionUtils.getStackTrace(e));
		}
	}
}
