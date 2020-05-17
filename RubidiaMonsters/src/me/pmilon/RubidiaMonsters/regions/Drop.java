package me.pmilon.RubidiaMonsters.regions;

import org.bukkit.inventory.ItemStack;

public class Drop {

	private int index;
	private ItemStack item;
	private double probability;
	public Drop(int index, ItemStack item, double probability){
		this.index = index;
		this.item = item.clone();
		this.probability = probability;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
}
