package me.pmilon.RubidiaCore.utils;

public class BukkitConverter {

	public static String convert(String material) {
		if(material.contains("WOOD_")) {
			material = material.replaceAll("WOOD_", "WOODEN_");
		}
		if(material.contains("GOLD_")) {
			material = material.replaceAll("GOLD_", "GOLDEN_");
		}
		if(material.contains("_SPADE")) {
			material = material.replaceAll("_SPADE", "_SHOVEL");
		}
		return material;
	}
	
}
