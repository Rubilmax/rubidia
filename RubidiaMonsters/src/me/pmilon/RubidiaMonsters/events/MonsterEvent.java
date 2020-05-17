package me.pmilon.RubidiaMonsters.events;

import me.pmilon.RubidiaMonsters.regions.Monster;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MonsterEvent extends Event implements Cancellable {

	protected Monster monster;
	protected boolean cancelled = false;
	public MonsterEvent(Monster monster){
		this.monster = monster;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
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
