package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class ASyncGMemberCreateGuildEvent extends GMemberEvent {

	public ASyncGMemberCreateGuildEvent(Guild guild, GMember member) {
		super(guild, member);
	}

}
