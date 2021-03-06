package me.pmilon.RubidiaGuilds.events;

import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMemberClaimEvent extends GMemberEvent {

	private Claim claim;
	public GMemberClaimEvent(Guild guild, GMember member, Claim claim) {
		super(guild, member);
		this.claim = claim;
	}
	
	public Claim getClaim() {
		return claim;
	}
	
	public void setClaim(Claim claim) {
		this.claim = claim;
	}

}
