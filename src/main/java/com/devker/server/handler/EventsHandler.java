package com.devker.server.handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.devker.server.data.Event;
import com.devker.server.database.IDatabaseHandler;

public class EventsHandler {
	
	private static final Logger logger = LogManager.getLogger(EventsHandler.class);
	
	private Thread thread;
	private BlockingQueue<Event> queue;
	private IDatabaseHandler databaseHandler;

	public EventsHandler(IDatabaseHandler databaseHandler) {
		this.databaseHandler=databaseHandler;
		this.queue = new LinkedBlockingDeque<Event>();
	}

	private void startThread() {
		logger.info("Started events queue thread.");
		this.thread = new Thread(() -> startHandling());
		this.thread.start();
	}

	public void addEvent(Event event) {
		logger.info("Added event to the queue.");
		this.queue.add(event);
		if (this.thread == null) {
			this.startThread();
		} else {
			if (!this.thread.isAlive()) {
				this.startThread();
			}
		}
	}

	private void startHandling() {
		while (!this.queue.isEmpty()) {
			Event event = this.queue.poll();
			if (event != null) {
				this.databaseHandler.addEvent(event);
			}
		}
	}

}
