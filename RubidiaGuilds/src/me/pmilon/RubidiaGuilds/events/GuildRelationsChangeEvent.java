package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GuildRelationsChangeEvent extends GuildEvent {

	private Guild with;
	public GuildRelationsChangeEvent(Guild guild, Guild with) {
		super(guild);
		this.with = with;
	}
	public Guild getWith() {
		return with;
	}
	public void setWith(Guild with) {
		this.with = with;
	}

}
