package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerCancellableEvent;

import org.bukkit.event.HandlerList;

public class RPlayerXPEvent extends RPlayerCancellableEvent {

	private double xp;
	private RXPSource source;
	
	public RPlayerXPEvent(RPlayer rplayer, double xp, RXPSource source){
		super(rplayer);
		this.xp = xp;
		this.source = source;
	}
	
	public double getXP(){
		return this.xp;
	}
	
	public void setXP(double xp){
		this.xp = xp;
	}
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	public RXPSource getSource() {
		return source;
	}

	public void setSource(RXPSource source) {
		this.source = source;
	}

}
