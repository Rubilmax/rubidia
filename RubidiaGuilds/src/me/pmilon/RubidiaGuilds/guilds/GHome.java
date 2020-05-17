package me.pmilon.RubidiaGuilds.guilds;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class GHome {

	private int index;
	private String name;
	private Location location;
	private ItemStack display;
	public GHome(int index, String name, Location location, ItemStack display){
		this.index = index;
		this.name = name;
		this.location = location;
		this.display = display;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ItemStack getDisplay() {
		return display.clone();
	}

	public void setDisplay(ItemStack display) {
		this.display = display;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
