package me.pmilon.RubidiaQuests.shops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.utils.Configs;

public class PNJShopColl extends Database<String,PNJShop>{

	public PNJShopColl(){
		if(Configs.getShopsConfig().contains("shops")){
			for(String shopId : Configs.getShopsConfig().getConfigurationSection("shops").getKeys(false)){
				String path = "shops." + shopId;
				List<ItemStack> items = new ArrayList<ItemStack>();
				List<Integer[]> prices = new ArrayList<Integer[]>();
				if(Configs.getShopsConfig().contains(path + ".trades")){
					for(String index : Configs.getShopsConfig().getConfigurationSection(path + ".trades").getKeys(false)){
						ItemStack stack = Configs.getShopsConfig().getItemStack(path + ".trades." + index + ".item");
						Integer[] price = new Integer[2];
						price[0] = Configs.getShopsConfig().getInt(path + ".trades." + index + ".blocks");
						price[1] = Configs.getShopsConfig().getInt(path + ".trades." + index + ".emeralds");
						items.add(stack);
						prices.add(price);
					}
				}
				PNJShop shop = new PNJShop(shopId, items, prices,
						Configs.getShopsConfig().getString(path + ".inventoryTitle"));
				this.load(shopId,shop);
			}
		}
	}

	@Override
	protected PNJShop getDefault(String uuid) {
		return new PNJShop(uuid, new ArrayList<ItemStack>(), new ArrayList<Integer[]>(), "Nouvelle boutique");
	}

	@Override
	protected void onSaveEnd(boolean arg0) {
		Configs.saveShopsConfig();
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) QuestsPlugin.console.sendMessage("§a   Saving Shops...");
	}

	@Override
	protected void save(boolean debug, PNJShop shop) {
		String path = "shops." + shop.getUUID();
		Configs.getShopsConfig().set(path + ".inventoryTitle", shop.getInventoryTitle());
		Configs.getShopsConfig().set(path + ".trades", null);
		for(int i = 0;i < shop.getItemStacks().size();i++){
			ItemStack item = shop.getItemStacks().get(i);
			Integer[] prices = shop.getPrices().get(i);
			Configs.getShopsConfig().set(path + ".trades." + i + ".item", item);
			Configs.getShopsConfig().set(path + ".trades." + i + ".blocks", prices[0]);
			Configs.getShopsConfig().set(path + ".trades." + i + ".emeralds", prices[1]);
		}
		if(debug) QuestsPlugin.console.sendMessage("§6Saved §eShop " + shop.getUUID());
	}
	
}
