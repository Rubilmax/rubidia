package me.pmilon.RubidiaQuests.commands;

import java.util.ArrayList;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaQuests.shops.PlayerShop;
import me.pmilon.RubidiaQuests.ui.shops.ShopEditionUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(!rp.isInCombat()){
			if(rp.getShop() == null)rp.setShop(new PlayerShop(new ArrayList<ItemStack>(), new ArrayList<Integer[]>(), ("Boutique de " + rp.getName()), player, ("Boutique de " + rp.getName())));
			Core.uiManager.requestUI(new ShopEditionUI(player, rp.getShop(),0));
		}else rp.sendMessage("§cVous ne pouvez ouvrir de boutique en combat !");
	}

}
