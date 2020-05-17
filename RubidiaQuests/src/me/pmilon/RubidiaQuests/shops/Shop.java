package me.pmilon.RubidiaQuests.shops;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tags.TagStandManager;
import me.pmilon.RubidiaQuests.ui.shops.ShopUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {

	private String inventoryTitle;
	private List<ItemStack> itemStacks;
	private List<Integer[]> prices;
	private List<Player> buyers = new ArrayList<Player>();
	public Shop(List<ItemStack> itemStacks, List<Integer[]> prices, String inventoryTitle){
		this.itemStacks = itemStacks;
		this.prices = prices;
		this.inventoryTitle = inventoryTitle;
	}
	
	public List<ItemStack> getItemStacks() {
		return itemStacks;
	}
	
	public void setItemStacks(List<ItemStack> itemStacks) {
		this.itemStacks = itemStacks;
	}
	
	public void open(Player player){
		Core.uiManager.requestUI(new ShopUI(player, this));
		this.getBuyers().add(player);
	}
	
	public void close(){
		for(Player player : this.buyers){
			if(Core.uiManager.hasActiveSession(player)){
				Core.uiManager.getSession(player).getUIHandler().close(false);
			}
		}
		this.buyers.clear();
		if(this instanceof PlayerShop){
			PlayerShop shop = (PlayerShop)this;
			shop.setStart(false);
			if(TagStandManager.hasTagStand(shop.getHolder())){
				TagStandManager.getTagStand(shop.getHolder()).remove();
			}
		}
	}

	public String getInventoryTitle() {
		return inventoryTitle;
	}

	public void setInventoryTitle(String inventoryTitle) {
		this.inventoryTitle = inventoryTitle;
	}

	public List<Player> getBuyers() {
		return buyers;
	}

	public void setBuyers(List<Player> buyers) {
		this.buyers = buyers;
	}
	
	public void removeItemStack(ItemStack[] item){
		for(int i = 0;i < this.getItemStacks().size();i++){
			ItemStack stack = this.getItemStacks().get(i);
			Integer[] prices = this.getPrices().get(i);
			if(stack.isSimilar(item[0])){
				if(stack.getAmount() == item[0].getAmount()){
					if(prices[0] == item[1].getAmount()){
						if(prices[1] == item[2].getAmount()){
							this.getItemStacks().remove(stack);
							this.getPrices().remove(prices);
						}
					}
				}
			}
		}
	}

	public List<Integer[]> getPrices() {
		return prices;
	}

	public void setPrices(List<Integer[]> prices) {
		this.prices = prices;
	}
	
}
