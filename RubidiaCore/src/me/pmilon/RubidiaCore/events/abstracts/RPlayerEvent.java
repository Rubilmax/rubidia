package me.pmilon.RubidiaCore.events.abstracts;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RPlayerEvent extends Event {

	protected RPlayer rplayer;
	
	protected RPlayerEvent(RPlayer rplayer){
		this.rplayer = rplayer;
	}
	
	public RPlayer getRPlayer(){
		return this.rplayer;
	}
	
	public void setRPlayer(RPlayer rplayer){
		this.rplayer = rplayer;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
