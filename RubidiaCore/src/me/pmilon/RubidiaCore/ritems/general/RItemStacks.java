package me.pmilon.RubidiaCore.ritems.general;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaPets.pets.RItemPearls;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RItemStacks {

	public final static List<RItemStack> ITEMS = new ArrayList<RItemStack>();
	public final static List<RItemStack> POWDERS = new ArrayList<RItemStack>();
	
	public static RItemStack STAR_STONE;
	public static RItemStack ORICHALCUM;
	public static RItemStack POWDER_STRENGTH;
	public static RItemStack POWDER_ENDURANCE;
	public static RItemStack POWDER_AGILITY;
	public static RItemStack POWDER_INTELLIGENCE;
	public static RItemStack POWDER_PERCEPTION;
	public static RItemStack POWDER_ATTACK;
	public static RItemStack POWDER_DEFENSE;
	public static RItemStack ELYTRA;
	public static RItemStack EMERALD;
	
	public static void enable(){
		ItemStack star = new ItemStack(Material.NETHER_STAR,1);
		ItemMeta META = star.getItemMeta();
		META.setDisplayName("§fPierre étoile");
		star.setItemMeta(META);
		STAR_STONE = new RItemStack(star, 879);
		
		ItemStack orichalcum = new ItemStack(Material.SUNFLOWER, 1);
		META.setDisplayName("§fOrichalque");
		orichalcum.setItemMeta(META);
		ORICHALCUM = new RItemStack(orichalcum, 1022);
		
		ItemStack str = new ItemStack(Material.ROSE_RED, 1);
		META.setDisplayName("§fJoyau de §cforce");
		str.setItemMeta(META);
		POWDER_STRENGTH = new RItemStack(str, 4234);
		
		ItemStack end = new ItemStack(Material.LIME_DYE, 1);
		META.setDisplayName("§fJoyau d'§aendurance");
		end.setItemMeta(META);
		POWDER_ENDURANCE = new RItemStack(end, 4234);
		
		ItemStack agi = new ItemStack(Material.DANDELION_YELLOW, 1);
		META.setDisplayName("§fJoyau d'§eagilité");
		agi.setItemMeta(META);
		POWDER_AGILITY = new RItemStack(agi, 4234);
		
		ItemStack intel = new ItemStack(Material.LIGHT_BLUE_DYE, 1);
		META.setDisplayName("§fJoyau d'§bintelligence");
		intel.setItemMeta(META);
		POWDER_INTELLIGENCE = new RItemStack(intel, 4234);
		
		ItemStack per = new ItemStack(Material.MAGENTA_DYE, 1);
		META.setDisplayName("§fJoyau de §dperception");
		per.setItemMeta(META);
		POWDER_PERCEPTION = new RItemStack(per, 4234);
		
		ItemStack atq = new ItemStack(Material.ORANGE_DYE, 1);
		META.setDisplayName("§fJoyau d'§6attaque");
		atq.setItemMeta(META);
		POWDER_ATTACK = new RItemStack(atq, 5367);
		
		ItemStack def = new ItemStack(Material.CYAN_DYE, 1);
		META.setDisplayName("§fJoyau de §3défense");
		def.setItemMeta(META);
		POWDER_DEFENSE = new RItemStack(def, 5367);
		
		ItemStack ely = new ItemStack(Material.ELYTRA, 1);
		META.setDisplayName("§fAiles de dragon");
		ely.setItemMeta(META);
		ELYTRA = new RItemStack(ely, 1456);
		
		EMERALD = new RItemStack(new ItemStack(Material.EMERALD, 1), 99);
		
		for(Field field : RItemStacks.class.getFields()){
			if(field.getType().equals(RItemStack.class)){
				try {
					ITEMS.add((RItemStack) field.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				if(field.getName().contains("POWDER")){
					try {
						POWDERS.add((RItemStack) field.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		RItemPearls.enable();
	}
	
	public static RItemStack valueOf(String name){
		for(Field field : RItemStacks.class.getFields()){
			if(field.getType().equals(RItemStack.class)){
				if(field.getName().equals(name)){
					try {
						return (RItemStack) field.get(null);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
}
