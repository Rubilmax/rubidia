package me.pmilon.RubidiaQuests.quests;

import org.bukkit.DyeColor;

public enum QuestType {

	NORMAL(DyeColor.GREEN, "§2","§a"),
	MISC(DyeColor.GRAY, "§7","§f"),
	STORY(DyeColor.ORANGE, "§6","§e"),
	EVENT(DyeColor.PURPLE, "§5","§d"),
	OFFICE(DyeColor.LIGHT_BLUE,"§9","§b");
	
	private final DyeColor color;
	private final String darkColor;
	private final String lightColor;
	private QuestType(DyeColor color, String darkColor, String lightColor){
		this.color = color;
		this.darkColor = darkColor;
		this.lightColor = lightColor;
	}
	
	public DyeColor getColor() {
		return color;
	}

	public String getDarkColor() {
		return darkColor;
	}

	public String getLightColor() {
		return lightColor;
	}
	
}
