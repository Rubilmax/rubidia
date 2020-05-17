package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMemberDisbandGuildEvent extends GMemberEvent {

	public GMemberDisbandGuildEvent(final Guild guild, GMember member) {
		super(guild, member);
	}

}
