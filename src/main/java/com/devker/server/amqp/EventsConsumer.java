package com.devker.server.amqp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.devker.server.data.Event;
import com.devker.server.handler.EventsHandler;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

public class EventsConsumer {
	
	private static final Logger logger = LogManager.getLogger(EventsConsumer.class);
	
	private final String exchangePrefix="devker.server.device_events";
	
	private String deviceId;
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private int threads;
	private int threadCounter;
	private ArrayList<EventsHandler> eventsHandler;
	
	public EventsConsumer(String deviceId, ConnectionFactory factory, int threads) {
		this.deviceId=deviceId;
		this.factory=factory;
		this.threads=threads;
	}
	
	private void inithandlers() {
		this.eventsHandler=new ArrayList<>();
		for(int i=0; i<this.threads; i++) {
			EventsHandler eventsHandler = new EventsHandler();
			this.eventsHandler.add(eventsHandler);
		}
	}
	
	private String createExchangeName() {
		return exchangePrefix+"_"+this.deviceId;
	}
	
	private void deliveryCallback(String consumerTag, Delivery delivery) {
		try {
			Gson gson = new Gson();
			String message = new String(delivery.getBody(), "UTF-8");
			
			
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
	}
	
	public boolean startListening() {
		try {
			this.inithandlers();
			this.connection = this.factory.newConnection();
			this.channel = this.connection.createChannel();
			String exchangeName = this.createExchangeName();
			this.channel.exchangeDeclare(exchangeName, "topic");
		    String queueName = channel.queueDeclare().getQueue();
		    this.channel.queueBind(queueName, exchangeName, "*");
		    channel.basicConsume(queueName, true, (consumerTag, delivery) -> deliveryCallback(consumerTag, delivery), consumerTag -> { });
		    return true;
		} catch (IOException | TimeoutException e) {
			logger.error(e);
		}
		return false;
	}

}
