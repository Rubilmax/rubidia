package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMemberPvpGMemberCancelledEvent extends GMemberEvent {

	private GMember defender;
	public GMemberPvpGMemberCancelledEvent(Guild guild, GMember attacker, GMember defender) {
		super(guild, attacker);
		this.setDefender(defender);
	}
	
	public GMember getDefender() {
		return defender;
	}
	
	public void setDefender(GMember defender) {
		this.defender = defender;
	}

}
