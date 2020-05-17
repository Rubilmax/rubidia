package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMemberLeaveGuildEvent extends GMemberEvent {

	public GMemberLeaveGuildEvent(Guild guild, GMember member) {
		super(guild, member);
	}

}
