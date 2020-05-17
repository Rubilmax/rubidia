package me.pmilon.RubidiaCore.RManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum RClass {
	
	VAGRANT(Material.WOODEN_SHOVEL, "Vagabond", "§7", "§7"),
	PALADIN(Material.DIAMOND_CHESTPLATE, "Paladin", "§2", "§a"),
	RANGER(Material.ARROW, "Ranger", "§9", "§b"),
	MAGE(Material.STICK, "Mage", "§6", "§e"),
	ASSASSIN(Material.FEATHER, "Assassin", "§4", "§c"),
	VAMPIRE(Material.REDSTONE, "Vampire", "§5", "§d");
	
	private final Material display;
	private final String name;
	private final String darkColor;
	private final String color;
	private RClass(Material display, String name, String darkColor, String color){
		this.display = display;
		this.name = name;
		this.darkColor = darkColor;
		this.color = color;
	}
	
	public Material getDisplay(){
		return this.display;
	}
	
	public String getName(){
		return this.name;
	}

	public String getDarkColor() {
		return darkColor;
	}

	public String getColor() {
		return color;
	}

	public ChatColor getChatColor() {
		return ChatColor.getByChar(this.getColor().replace("§", ""));
	}

	public ChatColor getDarkChatColor() {
		return ChatColor.getByChar(this.getDarkColor().replace("§", ""));
	}
	
	public static int indexOf(RClass rClass) {
		for(int i = 0;i < RClass.values().length;i++) {
			if(rClass.equals(RClass.values()[i])) {
				return i;
			}
		}
		return -1;
	}
}
