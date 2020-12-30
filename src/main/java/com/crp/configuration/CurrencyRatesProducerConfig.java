package com.crp.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.crp.pojos.CurrencyModel;

@Configuration
public class CurrencyRatesProducerConfig {
	private static Logger Log = Logger.getLogger(CurrencyRatesProducerConfig.class.getName());
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootStrapServers;
	
	@Bean(name = "producerFactory")
	public ProducerFactory<String, CurrencyModel> producerFactory() {
	    return new DefaultKafkaProducerFactory<>(producerConfigs());
	}
	
	@Bean(name = "producerConfig")
	public Map<String, Object> producerConfigs() {
	    Map<String, Object> props = new HashMap<>();
	    props.put("bootstrap.servers", bootStrapServers);
	    props.put("key.serializer", StringSerializer.class);
	    props.put("value.serializer", JsonSerializer.class);
	    Log.info("ProducerConfig: " + props.toString());
	    return props;
	}
	
	@Bean(name = "producerTemplate")
	public KafkaTemplate<String, CurrencyModel> kafkaTemplate() {
	    KafkaTemplate<String, CurrencyModel> kTemplate = new KafkaTemplate<String, CurrencyModel>(producerFactory());
	    Log.info("Kafka Template instantiated with properties: " +
	               kTemplate.getProducerFactory().getConfigurationProperties());
	    
	    return kTemplate;
	}
}
