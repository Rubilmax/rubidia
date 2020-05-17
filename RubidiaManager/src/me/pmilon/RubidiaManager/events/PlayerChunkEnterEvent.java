package me.pmilon.RubidiaManager.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChunkEnterEvent extends Event implements Cancellable {
	
	private final Player player;
	private final Chunk from;
	private final Chunk to;
	
	private boolean cancelled;
	public PlayerChunkEnterEvent(Player player, Chunk from, Chunk to){
		this.player = player;
		this.from = from;
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
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public Chunk getFrom() {
		return from;
	}

	public Chunk getTo() {
		return to;
	}

}
