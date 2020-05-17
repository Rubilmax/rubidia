package me.pmilon.RubidiaCore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerToggleHidingEvent extends Event implements Cancellable {

	private boolean cancelled;
	private Player player;
	private int entityId;
	private boolean visible;

	public PlayerToggleHidingEvent(Player p, int entityId, boolean visible) {
		this.player = p;
		this.entityId = entityId;
		this.visible = visible;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public int getEntityId(){
		return this.entityId;
	}
	
	public boolean getState(){
		return this.visible;
	}
	
	public void setPlayer(Player p){
		this.player = p;
	}
	
	public void setEntityId(int i){
		this.entityId = i;
	}
	
	public void setState(boolean visible){
		this.visible = visible;
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
