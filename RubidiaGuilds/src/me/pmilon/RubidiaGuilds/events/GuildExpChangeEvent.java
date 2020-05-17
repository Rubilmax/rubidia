package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GuildExpChangeEvent extends GuildEvent {

	protected double lastExp;
	protected double newExp;
	public GuildExpChangeEvent(Guild guild, double lastExp, double newExp) {
		super(guild);
		this.lastExp = lastExp;
		this.newExp = newExp;
	}

	public double getLastExp(){
		return this.lastExp;
	}
	
	public void setLastExp(double lastExp){
		this.lastExp = lastExp;
	}
	
	public double getNewExp(){
		return this.newExp;
	}

	public void setNewExp(double newExp){
		this.newExp = newExp;
	}
}
