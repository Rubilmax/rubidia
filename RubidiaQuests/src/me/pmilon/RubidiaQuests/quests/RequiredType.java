package me.pmilon.RubidiaQuests.quests;

import org.bukkit.Material;

public enum RequiredType {

	LEVEL(Material.ENDER_PEARL),
	CLASS(Material.IRON_SHOVEL),
	JOB(Material.IRON_PICKAXE),
	NON_JOB(Material.IRON_PICKAXE),
	QUEST(Material.BOOK),
	MASTERY(Material.ENCHANTING_TABLE),
	TIME(Material.CLOCK),
	ITEM(Material.NETHER_STAR),
	NON_ACTIVE_QUEST(Material.WRITABLE_BOOK),
	NON_DONE_QUEST(Material.WRITTEN_BOOK);

	private final Material display;
	private RequiredType(Material display){
		this.display = display;
	}
	
	public Material getDisplay() {
		return this.display;
	}
	
}
