package org.wispersd.core.datacollect.searchintegration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.convert.converter.Converter;

public class ConsumerDataReverseConverter implements Converter<ConsumerRecord<String, String>, String>{

	public String convert(ConsumerRecord<String, String> consumerRecord) {
		return consumerRecord.value();
	}
}
