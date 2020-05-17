package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMemberChangeGuildNameEvent extends GMemberEvent {

	private String name;
	public GMemberChangeGuildNameEvent(Guild guild, GMember member, String name) {
		super(guild, member);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
