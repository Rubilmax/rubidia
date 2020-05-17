package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GuildLevelChangeEvent extends GuildEvent {

	protected int lastLevel;
	protected int newLevel;
	public GuildLevelChangeEvent(Guild guild, int lastLevel, int newLevel) {
		super(guild);
		this.lastLevel = lastLevel;
		this.newLevel = newLevel;
	}

	public int getLastLevel(){
		return this.lastLevel;
	}
	
	public void setLastLevel(int lastLevel){
		this.lastLevel = lastLevel;
	}
	
	public int getNewLevel(){
		return this.newLevel;
	}

	public void setNewLevel(int newLevel){
		this.newLevel = newLevel;
	}
}
