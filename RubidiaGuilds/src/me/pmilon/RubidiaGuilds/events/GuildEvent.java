package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.Guild;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class GuildEvent extends Event implements Cancellable {

	protected boolean cancelled;
	protected Guild guild;
	public GuildEvent(Guild guild){
		this.guild = guild;
	}
	
	public Guild getGuild(){
		return this.guild;
	}
	
	public void setGuild(Guild guild){
		this.guild = guild;
	}
	
	@Override
	public boolean isCancelled(){
		return this.cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled){
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
	
}
