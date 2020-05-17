package me.pmilon.RubidiaMonsters.events;

import org.bukkit.Location;

import me.pmilon.RubidiaMonsters.regions.Monster;

public class MonsterSpawnEvent extends MonsterEvent {

	protected Location location;
	public MonsterSpawnEvent(Monster monster, Location location) {
		super(monster);
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

}
