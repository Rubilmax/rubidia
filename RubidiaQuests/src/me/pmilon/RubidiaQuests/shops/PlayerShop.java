package me.pmilon.RubidiaQuests.shops;

import java.util.List;

import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaQuests.ui.shops.ShopEditionUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerShop extends Shop {

	private Player holder;
	private String title;
	private boolean start = false;
	private ShopEditionUI editionUI;
	public PlayerShop(List<ItemStack> itemStacks, List<Integer[]> prices, String inventoryTitle, Player holder, String title) {
		super(itemStacks, prices, inventoryTitle);
		this.holder = holder;
		this.title = title;
	}
	
	public Player getHolder() {
		return holder;
	}
	
	public void setHolder(Player holder) {
		this.holder = holder;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
		if(start){
			this.getHolder().teleport(this.getHolder().getLocation().add(0,.3,0));
			this.getHolder().setFlying(true);
			this.getHolder().setFlySpeed(0);
		}else{
			this.getHolder().setFlying(false);
			this.getHolder().setFlySpeed(Settings.DEFAULT_FLY_SPEED);
		}
	}

	public ShopEditionUI getEditionUI() {
		return editionUI;
	}

	public void setEditionUI(ShopEditionUI editionUI) {
		this.editionUI = editionUI;
	}

}
