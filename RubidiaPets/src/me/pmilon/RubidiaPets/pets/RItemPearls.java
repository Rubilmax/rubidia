package me.pmilon.RubidiaPets.pets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.ritems.general.RItemStack;
import me.pmilon.RubidiaCore.ritems.general.RItemStacks;
import me.pmilon.RubidiaPets.pets.Pearl.PearlType;

public class RItemPearls {

	public static final List<RItemStack> PEARLS = new ArrayList<RItemStack>();
	
	public static RItemStack PET_FOOD;
	
	public static void enable(){
		ItemStack food = new ItemStack(Material.BEETROOT, 1);
		ItemMeta mt = food.getItemMeta();
		mt.setDisplayName("§fNutriture");
		food.setItemMeta(mt);
		PET_FOOD = new RItemStack(food, 64);
		
		for(PearlType type : PearlType.values()){
			for(int h : new int[]{1,2,6,12,24}){
				for(int level : new int[]{1,2,3}){
					String lvl = "";
					for(int i = 0;i < level;i++){
						lvl += "I";
					}
					ItemStack stack = new ItemStack(Material.ENDER_PEARL, 1);
					ItemMeta meta = stack.getItemMeta();
					meta.setDisplayName("§aPerle de " + type.getDisplay()[1] + " " + lvl + " §2(" + h + "h)");
					meta.setLore(Arrays.asList("§7Cette perle vous octroiera un bonus éphémère", "§7une fois équipée sur votre compagnon."));
					stack.setItemMeta(meta);
					PEARLS.add(new RItemStack(stack, (int) (8463+1004*Math.pow(h, 2)*Math.pow(level, 3))));
				}
			}
		}
		RItemStacks.ITEMS.addAll(PEARLS);
		
		for(Field field : RItemPearls.class.getFields()){
			if(field.getType().equals(RItemStack.class)){
				try {
					RItemStacks.ITEMS.add((RItemStack) field.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
