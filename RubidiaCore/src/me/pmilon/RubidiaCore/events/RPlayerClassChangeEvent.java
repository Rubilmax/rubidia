package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;

import org.bukkit.event.Cancellable;

public class RPlayerClassChangeEvent extends RPlayerEvent implements Cancellable{

	private RClass previous;
	private RClass next;
	private boolean cancelled = false;
	
	public RPlayerClassChangeEvent(RPlayer rplayer, RClass previous, RClass next) {
		super(rplayer);
		this.previous = previous;
		this.next = next;
	}

	public RClass getPreviousRClass() {
		return previous;
	}

	public void setPreviousRClass(RClass previous) {
		this.previous = previous;
	}

	public RClass getNewRClass() {
		return next;
	}

	public void setNewRClass(RClass next) {
		this.next = next;
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
