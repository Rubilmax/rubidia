package me.pmilon.RubidiaQuests.quests;

import org.bukkit.Material;

public enum RewardType {

	CLASS(Material.GOLDEN_HOE),
	JOB(Material.DIAMOND_AXE),
	ITEM(Material.NETHER_STAR),
	XP(Material.EXPERIENCE_BOTTLE),
	MONEY(Material.EMERALD),
	MASTERY(Material.DIAMOND_HOE),
	SKP(Material.BOOKSHELF),
	SKD(Material.WRITABLE_BOOK),
	QUEST(Material.BOOK),
	COMMAND(Material.MAP);
	
	private final Material display;
	private RewardType(Material display){
		this.display = display;
	}
	public Material getDisplay() {
		return display;
	}
	
}
