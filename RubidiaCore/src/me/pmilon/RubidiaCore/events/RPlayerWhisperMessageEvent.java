package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class RPlayerWhisperMessageEvent extends RPlayerEvent implements Cancellable {

	private Player to;
	private String message;
	private boolean cancelled;
	
	public RPlayerWhisperMessageEvent(RPlayer rplayer, Player to, String message) {
		super(rplayer);
		this.setTo(to);
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}

	public Player getTo() {
		return to;
	}

	public void setTo(Player to) {
		this.to = to;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
