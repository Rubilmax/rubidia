package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.chairs.Chair;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DelayedPlayerUnsitEvent extends Event{

	private Player player;
	private Chair chair;
	public DelayedPlayerUnsitEvent(Player player, Chair chair){
		this.player = player;
		this.chair = chair;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Chair getChair() {
		return chair;
	}

	public void setChair(Chair chair) {
		this.chair = chair;
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
