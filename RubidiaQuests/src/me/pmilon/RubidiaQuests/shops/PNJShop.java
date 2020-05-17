package me.pmilon.RubidiaQuests.shops;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class PNJShop extends Shop {

	private String uuid;
	public PNJShop(String uuid, List<ItemStack> itemStacks, List<Integer[]> prices, String inventoryTitle) {
		super(itemStacks, prices, inventoryTitle);
		this.uuid = uuid;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uUID) {
		this.uuid = uUID;
	}
	
}
