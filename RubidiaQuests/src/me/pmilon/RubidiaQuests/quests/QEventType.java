package me.pmilon.RubidiaQuests.quests;

import org.bukkit.Material;

public enum QEventType {

	TELEPORTATION(Material.COMPASS),
	EFFECT(Material.POTION),
	BLOCKS(Material.BRICK),
	SPAWN(Material.IRON_SWORD),
	ITEM(Material.NETHER_STAR);
	
	private final Material display;
	private QEventType(Material display){
		this.display = display;
	}
	public Material getDisplay() {
		return display;
	}
	
}
