package me.pmilon.RubidiaMonsters.dungeons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import me.pmilon.RubidiaMonsters.regions.Region;

public class Dungeon {

	private String uuid;
	private List<Region> rooms = new ArrayList<Region>();
	private Location entry;
	private int minLevel;
	
	//private Guild activeGuild;
	public Dungeon(String uuid, List<Region> rooms, Location entry, int minLevel){
		this.uuid = uuid;
		this.rooms = rooms;
		this.entry = entry;
		this.minLevel = minLevel;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public List<Region> getRooms() {
		return rooms;
	}

	public void setRooms(List<Region> rooms) {
		this.rooms = rooms;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public Location getEntry() {
		return entry;
	}

	public void setEntry(Location entry) {
		this.entry = entry;
	}
	
	
}
