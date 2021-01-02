package com.crp.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import com.crp.configuration.CurrencyNamesConfig.Currency;

@Configuration
public class CurrencyTopicsConfig {
	private static Logger Log = Logger.getLogger(CurrencyTopicsConfig.class.getName());
	
	@Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean(name = "kafkaAdmin")
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        KafkaAdmin kAdmin = new KafkaAdmin(configs);
        Log.info("KafkaAdmin bean instantiated with bootstarp server address(s): " + 
        		kAdmin.getConfigurationProperties().get(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG));
        
        return kAdmin;
    }
    
	@Bean(name = "INR")
	public NewTopic topicINR() {
	    NewTopic inrTopic = TopicBuilder.name(Currency.INR.name())
	            						.build();
	    Log.info("INR Topic built with default config");
	    
	    return inrTopic;
	}
	
	@Bean(name = "EUR")
	public NewTopic topicEUR() {
	    NewTopic eurTopic = TopicBuilder.name(Currency.EUR.name())
	            						.build();
	    Log.info("EUR Topic built with default config");
	    
	    return eurTopic;
	}
	
	@Bean(name = "USD")
	public NewTopic topicUSD() {
	    NewTopic usdTopic = TopicBuilder.name(Currency.USD.name())
	            						.build();
	    Log.info("USD Topic built with default config");
	    
	    return usdTopic;
	}
}
