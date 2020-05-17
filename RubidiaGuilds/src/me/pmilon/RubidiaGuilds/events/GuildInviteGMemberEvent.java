package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GuildInviteGMemberEvent extends GMemberEvent {

	public GuildInviteGMemberEvent(Guild guild, GMember member) {
		super(guild, member);
	}

}
