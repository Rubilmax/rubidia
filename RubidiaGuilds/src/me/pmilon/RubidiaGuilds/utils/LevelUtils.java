package me.pmilon.RubidiaGuilds.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public class LevelUtils {

	public static List<Material> loots = Arrays.asList(/*Material.NETHER_STAR, */Material.SPIDER_EYE, Material.ROTTEN_FLESH, Material.BONE, Material.STRING, Material.GUNPOWDER, Material.BLAZE_ROD, Material.ENDER_PEARL, Material.NETHER_WART, Material.WHEAT, Material.CARROT, Material.DRAGON_EGG, Material.MELON, Material.PUMPKIN, Material.POTATO, Material.MELON_SLICE, Material.SUGAR_CANE);
	
	public static double getLevelExperienceAmount(int level){
		return (level+1)*53.2+11.9;
	}
	
}
