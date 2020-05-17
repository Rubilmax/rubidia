package me.pmilon.RubidiaGuilds.events;

import org.bukkit.Location;

import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMemberClaimedChunkEnterEvent extends GMemberEvent{

	private Claim claim;
	private Location location;
	public GMemberClaimedChunkEnterEvent(Guild entered, GMember member, Claim claim, Location location){
		super(entered, member);
		this.claim = claim;
		this.location = location;
	}
	
	public Location getClaimed() {
		return location;
	}
	
	public void setClaimed(Location location) {
		this.location = location;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

}
