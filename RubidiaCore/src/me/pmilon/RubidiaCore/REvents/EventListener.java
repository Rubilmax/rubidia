package me.pmilon.RubidiaCore.REvents;

import me.pmilon.RubidiaCore.REvents.Event.EventType;
import me.pmilon.RubidiaCore.events.RPlayerXPEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventListener implements Listener {
	
	@EventHandler
	public void onXP(RPlayerXPEvent event){
		for(Event ev : Events.currentEvents){
			if(ev.isActive()){
				if(ev.getType().equals(EventType.XP)){
					event.setXP(event.getXP()*ev.getFactor());
				}
			}
		}
	}
	
}
