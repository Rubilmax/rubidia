package me.pmilon.RubidiaMonsters.events;

import org.bukkit.Location;

import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Region;

public class MonsterSpawnEvent extends MonsterEvent {

	protected Region region;
	protected Location location;
	protected boolean enraged;
	public MonsterSpawnEvent(Monster monster, Region region, Location location, boolean enraged) {
		super(monster);
		this.region = region;
		this.location = location;
		this.enraged = enraged;
	}
	
	public Region getRegion() {
		return this.region;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public boolean isEnraged() {
		return this.enraged;
	}
	
	public void setEnraged(boolean enraged) {
		this.enraged = enraged;
	}

}
