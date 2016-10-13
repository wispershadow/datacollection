package org.wispersd.core.datacollect.esbase;

import java.net.InetAddress;
import java.util.Properties;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchClientFactory {
	private static final String ATTRIB_HOSTS = "hosts";
	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchClientFactory.class);
	
	public static Client createSearchClient(Properties prop) {
		Settings.Builder builder = Settings.settingsBuilder();
		Settings settings = builder.put(prop).build();
		TransportClient client = TransportClient.builder().settings(settings).build();
		String hostList = prop.getProperty(ATTRIB_HOSTS);
		String[] arr = hostList.split(",");
		String[] pair = null;
		for(String nextHost: arr) {
			pair = nextHost.split(":");
			try {
				client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(pair[0]), Integer.parseInt(pair[1])));
			} catch (Exception e) {
				logger.error("Error adding transport address", e);
			}
		}
		return client;
	}
}
