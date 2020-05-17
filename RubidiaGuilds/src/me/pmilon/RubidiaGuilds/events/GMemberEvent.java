package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public abstract class GMemberEvent extends GuildEvent {

	private GMember member;
	public GMemberEvent(Guild guild, GMember member) {
		super(guild);
		this.member = member;
	}
	
	public GMember getGMember() {
		return member;
	}
	
	public void setGMember(GMember member) {
		this.member = member;
	}

}
