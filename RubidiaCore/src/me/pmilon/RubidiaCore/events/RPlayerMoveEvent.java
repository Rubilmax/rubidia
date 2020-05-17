package me.pmilon.RubidiaCore.events;

import org.bukkit.event.player.PlayerMoveEvent;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerCancellableEvent;

public class RPlayerMoveEvent extends RPlayerCancellableEvent {

	private final PlayerMoveEvent event;
	public RPlayerMoveEvent(RPlayer rplayer, PlayerMoveEvent event) {
		super(rplayer);
		this.event = event;
	}
	
	public PlayerMoveEvent getEvent() {
		return event;
	}

}
