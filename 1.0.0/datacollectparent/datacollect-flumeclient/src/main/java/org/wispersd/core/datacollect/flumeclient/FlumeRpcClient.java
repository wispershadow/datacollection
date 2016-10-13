package org.wispersd.core.datacollect.flumeclient;

import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

public class FlumeRpcClient {
	private static final String DEFAULT_CHARSET = "UTF-8";
	private volatile RpcClient rpcClient;
	private Properties flumeClientProps;
	
	public void init() {
		rpcClient = RpcClientFactory.getInstance(flumeClientProps);
	}
	
	public void destroy() {
		if (rpcClient != null) {
			rpcClient.close();
		}
	}
	
	public void sendDataToFlume(String data) {
		Event event = EventBuilder.withBody(data, Charset.forName(DEFAULT_CHARSET));
		try {
			rpcClient.append(event);
		} catch (EventDeliveryException e) {
			// clean up and recreate the client
			rpcClient.close();
			rpcClient = null;
			rpcClient = RpcClientFactory.getInstance(flumeClientProps);
		}
	}
	
	

}
