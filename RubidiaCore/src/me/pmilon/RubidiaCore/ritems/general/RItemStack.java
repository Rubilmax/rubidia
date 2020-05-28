package me.pmilon.RubidiaCore.ritems.general;

import org.bukkit.inventory.ItemStack;

public class RItemStack {

	final private ItemStack stack;
	final private int dropRarity;//average number of mobs to be killed before dropping
	
	public RItemStack(ItemStack stack, int dropRarity){
		this.stack = stack;
		this.dropRarity = dropRarity;
	}
	
	public ItemStack getItemStack() {
		return stack.clone();
	}

	public int getDropRarity() {
		return dropRarity;
	}
	
	public double getDropProbability() {
		return 1. / this.getDropRarity();
	}
	
	
}
