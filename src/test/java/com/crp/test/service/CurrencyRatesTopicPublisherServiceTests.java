package com.crp.test.service;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.crp.configuration.CurrencyNamesConfig;
import com.crp.configuration.CurrencyRatesProducerConfig;
import com.crp.configuration.CurrencyTopicsConfig;
import com.crp.pojos.CurrencyModel;
import com.crp.service.CurrencyRatesRetrieveService;
import com.crp.service.CurrencyRatesTopicPublisherService;

@DirtiesContext
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "/test.properties")
@SpringBootTest(
	    // slice our unit test app context down to just these specific pieces
	    classes = {
	    		// Required configuration classes
	    		CurrencyNamesConfig.class,
		    	CurrencyTopicsConfig.class,
		    	CurrencyRatesProducerConfig.class,
		    	// Dependent class
		    	CurrencyRatesRetrieveService.class,
		    	// Service class to test
		    	CurrencyRatesTopicPublisherService.class
	    }
	)
public class CurrencyRatesTopicPublisherServiceTests {
	private static Logger Log = Logger.getLogger(CurrencyRatesTopicPublisherServiceTests.class.getName());
	private static final String CURRENCY_TOPIC = "EUR";
	
	@Autowired
	private CurrencyRatesTopicPublisherService publisherService;
	
	@Autowired
	private CurrencyRatesRetrieveService retrieveService;
	
	private KafkaMessageListenerContainer<String, CurrencyModel> container;

	private BlockingQueue<ConsumerRecord<String, CurrencyModel>> records;

	@ClassRule
	public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, CURRENCY_TOPIC);
	
	@Test
	public void testcheckKafkaClusterStatus() {
		assert(publisherService.checkKafkaTopicsStatus());
	}
	
	@Before
	public void setUp() throws Exception {
		Log.info("Setting up Message Listener Container for publisher testing...");
		
		//Build instance for JSONDeserializer
		JsonDeserializer<CurrencyModel> deserializer = new JsonDeserializer<>(CurrencyModel.class);
	    deserializer.setRemoveTypeHeaders(false);
	    deserializer.addTrustedPackages("*");
	    deserializer.setUseTypeMapperForKey(true);
	    
	    // set up the Kafka consumer properties
	    Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("currencyrates", "false",
	            									embeddedKafka.getEmbeddedKafka());
	    consumerProperties.put("key.deserializer", StringDeserializer.class);
	    consumerProperties.put("value.deserializer", deserializer);
	    
	    // create a Kafka consumer factory
	    //DefaultKafkaConsumerFactory<String, CurrencyModel> consumerFactory = new DefaultKafkaConsumerFactory<String, CurrencyModel>(
	      //      																consumerProperties);
	    DefaultKafkaConsumerFactory<String, CurrencyModel> consumerFactory = new DefaultKafkaConsumerFactory<String, CurrencyModel>(
	    		consumerProperties, new StringDeserializer(), deserializer);
	    // set the topic that needs to be consumed
	    ContainerProperties containerProperties = new ContainerProperties(CURRENCY_TOPIC);

	    // create a Kafka MessageListenerContainer
	    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

	    // create a thread safe queue to store the received message
	    records = new LinkedBlockingQueue<>();
	    Log.info("Message records in Queue: " + records.size());

	    // setup a Kafka message listener
	    container.setupMessageListener(new MessageListener<String, CurrencyModel>() {
	          @Override
	          public void onMessage(ConsumerRecord<String, CurrencyModel> record) {
	        	  Log.info("Message record received...");
	        	  records.add(record);
	          }
	     });

	    // start the container and underlying message listener
	    container.start();
	    Log.info("Message Listener Container started...");

	    // wait until the container has the required number of assigned partitions
	    ContainerTestUtils.waitForAssignment(container,
	        embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
	    Log.info("Partitions assigned to container...");
	}
	
	@Test
	public void testPublishCurrencyRate() throws InterruptedException {
		//Retrieve latest currency
		CurrencyModel latestCurrency = retrieveService.getLatestCurrencyRates();
		Log.info("Latest currency rates retrieved for base currency: " + latestCurrency.getBase() + " of " + latestCurrency.getDate());
		//Publish to currency topic
		publisherService.publishCurrencyRate(latestCurrency);
		Log.info("Currency publish initiated using publisherService Instance");
		
		//Validate the currency message is consumed
  	  	Log.info("Message records in Queue: " + records.size());
		Log.info("Waiting to poll message record from queue...");
		ConsumerRecord<String, CurrencyModel> received = records.poll(10, TimeUnit.SECONDS);
		Log.info("Message Record received from queue for base " + received.value().getBase());
	    if(received.value().getBase().equalsIgnoreCase(latestCurrency.getBase())) {
	    	Log.info("Received message record is same as published message record");
	    	assert(true);
	    }
	    else {
	    	Log.severe("Received message record not match with published message record");
	    	assert(false);
	    }
	    
	    Log.info("End of currency rate publish test unit");
	}
	
	@After
	  public void tearDown() {
	    // stop the container
	    container.stop();
	    Log.info("Message Listener Container stopped");
	  }
}
