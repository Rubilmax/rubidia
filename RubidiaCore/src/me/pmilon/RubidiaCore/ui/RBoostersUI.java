package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.boosters.RBooster;
import me.pmilon.RubidiaCore.boosters.RBooster.RBoosterType;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RBoostersUI extends UIHandler {

	private RBooster[] boosters = new RBooster[5];
	
	public RBoostersUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, "Boosters");
	}

	@Override
	public String getType() {
		return "BOOSTERS_MENU";
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < 5;i++){
			this.getMenu().setItem(i, this.getBooster(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		RBooster booster = boosters[slot];
		RBooster active = rp.getActiveBooster(booster.getBoosterType());
		if(active != null){
			active.stop();
		}else{
			if(rp.getRenom() >= booster.getBoosterType().getCost() || rp.isOp()){
				booster.start();
			}else rp.sendMessage("§cVous n'avez pas assez de renom pour activer ce booster.");
		}
		this.getMenu().setItem(slot, this.getBooster(slot));
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	public ItemStack getBooster(int i){
		RBooster booster = boosters[i];
		if(booster == null)booster = new RBooster(rp, RBoosterType.values()[i]);
		boolean active = rp.hasActiveBooster(booster.getBoosterType());
		ItemStack item = new ItemStack(active ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((active ? "§a" : "§7") + "Booster " + booster.getBoosterType().toString());
		meta.setLore(Arrays.asList("§8" + (booster.getBoosterType().getLevel() >= 0 ? "+" : "") + Utils.round(booster.getFactor()*100, 1) + "% " + (booster.getBoosterType().getType().getDisplayFr()), "§e" + booster.getBoosterType().getCost() + (" points de renom par minute")));
		item.setItemMeta(meta);
		boosters[i] = booster;
		return item;
	}

}
