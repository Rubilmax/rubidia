package me.pmilon.RubidiaCore.events;

import org.bukkit.event.Cancellable;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;

public class RPlayerRequestDuelEvent extends RPlayerEvent implements Cancellable{

	protected RPlayer opponent;
	private boolean cancelled;
	public RPlayerRequestDuelEvent(RPlayer attacker, RPlayer opponent) {
		super(attacker);
		this.opponent = opponent;
	}
	
	public RPlayer getOpponent(){
		return this.opponent;
	}
	
	public void setOpponent(RPlayer rp){
		this.opponent = rp;
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
