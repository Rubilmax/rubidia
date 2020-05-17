package me.pmilon.RubidiaQuests.pnjs;

import me.pmilon.RubidiaQuests.shops.PNJShop;
import me.pmilon.RubidiaQuests.utils.Configs;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class ShopPNJ extends DialogerPNJ {

	private PNJShop shop;
	public ShopPNJ(String uuid, String title, String name,
			Location loc, int age, boolean fix, PNJShop shop, List<String> dialogs) {
		super(uuid, title, "§2§l", name, "§a", PNJType.SHOP, loc, age, fix, dialogs, false);
		this.shop = shop;
	}

	@Override
	protected void onSubSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".shopUUID", this.getShop().getUUID());
	}

	@Override
	protected void onDelete() {
		Configs.getShopsConfig().set("shops." + this.getShop().getUUID(), null);
	}

	public PNJShop getShop() {
		return shop;
	}

	public void setShop(PNJShop shop) {
		this.shop = shop;
	}

	@Override
	protected void onTalk(Player player) {
		getShop().open(player);
	}

	@Override
	protected void onSpawn(Villager villager) {
		// TODO Auto-generated method stub
		
	}

}
