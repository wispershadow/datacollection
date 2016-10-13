package org.wispersd.core.datacollect.searchintegration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KafkaToElasticSearchBoot {
	
	public static void main(String[] args) {
		ApplicationContext applContext = new ClassPathXmlApplicationContext(new String[]{"applicationContext-kafkasink.xml", "applicationContext-esbase.xml"});
		
		/*
		KafkaToElasticSearchSink sink = (KafkaToElasticSearchSink)applContext.getBean("kafkaToElasticSearchSink");
		String key = "order-process|fraudCheck-8796322464695";
		String param = "{\"id\":8796322464695,\"processName\":\"order-process\",\"action\":\"fraudCheck\",\"success\":true,\"startTime\":\"2016/07/15 07:30:34\",\"endTime\":\"2016/07/15 07:36:34\",\"executionTime\":249}";
		try {
			sink.execute("omsWorfklowEvent", key, param);
			key = "order-process|sendOrderPlacedNotification-8796322497463";
			param = "{\"id\":8796322497463,\"processName\":\"order-process\",\"action\":\"sendOrderPlacedNotification\",\"success\":true,\"startTime\":\"2016/07/15 14:26:34\",\"endTime\":\"2016/07/15 14:30:34\",\"executionTime\":21}";
			sink.execute("omsWorfklowEvent", key, param);
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

}
