package me.pmilon.RubidiaCore.events.abstracts;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.event.Cancellable;

public class RPlayerCancellableEvent extends RPlayerEvent implements Cancellable {

	private boolean cancelled = false;
	
	protected RPlayerCancellableEvent(RPlayer rplayer){
		super(rplayer);
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled  = cancelled;
	}
}
