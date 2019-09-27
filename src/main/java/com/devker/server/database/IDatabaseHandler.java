package com.devker.server.database;

import com.devker.server.data.Event;

public interface IDatabaseHandler {
	
	public boolean open();
	public boolean close();
	public boolean addEvent(Event event);

}
