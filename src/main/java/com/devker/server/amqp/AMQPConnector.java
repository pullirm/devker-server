package com.devker.server.amqp;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.devker.server.handler.EventsHandler;
import com.rabbitmq.client.ConnectionFactory;

public class AMQPConnector {
	
	private static final Logger logger = LogManager.getLogger(AMQPConnector.class);
	
	private String deviceId;
	
	private ConnectionFactory factory;
	private String host;
	private String username;
	private String password;
	
	private EventsConsumer eventsConsumer;
	
	public AMQPConnector(String deviceId, String host, String username, String password) {
		this.deviceId = deviceId;
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	private void initConnectionFactory() {
		this.factory = new ConnectionFactory();
		this.factory.setHost(this.host);
		this.factory.setUsername(this.username);
		this.factory.setPassword(this.password);
	}
	
	public void startListening() {
		this.initConnectionFactory();
		this.eventsConsumer = new EventsConsumer(this.deviceId, this.factory);
		this.eventsConsumer.startListening();
	}

}
