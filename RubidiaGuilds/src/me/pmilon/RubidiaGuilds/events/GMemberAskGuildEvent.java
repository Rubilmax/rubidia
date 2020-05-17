package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMemberAskGuildEvent extends GMemberEvent{

	public GMemberAskGuildEvent(Guild guild, GMember member) {
		super(guild, member);
	}

}
