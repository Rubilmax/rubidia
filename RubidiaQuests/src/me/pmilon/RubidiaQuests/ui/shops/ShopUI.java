package me.pmilon.RubidiaQuests.ui.shops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.shops.PlayerShop;
import me.pmilon.RubidiaQuests.shops.Shop;

public class ShopUI extends ListMenuUIHandler<ItemStack>{

	private Shop shop;
	private boolean canClick = true;
	public ShopUI(Player p, Shop shop) {
		super(p, shop.getInventoryTitle(), shop.getInventoryTitle(), 3);
		this.shop = shop;
	}
	
	@Override
	protected ItemStack getInformations() {
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(("§8§lBoutique" + (this.getShop() instanceof PlayerShop ? " de " + ((PlayerShop)this.getShop()).getHolder().getName() : "")));
		meta.setLore(Arrays.asList(("§f§lCliquez§7 un item pour acheter §f§l1§7 exemplaire"), ("§f§lSneak + cliquez§7 un item pour acheter §f§l5§7 exemplaires")));
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	protected ItemStack getItem(ItemStack items) {
		ItemStack item = items.clone();
		RItem rItem = new RItem(item);
		if(rItem.isWeapon())rItem.getWeapon().updateState(rp, item);
		Integer[] prices = this.getShop().getPrices().get(this.getShop().getItemStacks().indexOf(items));
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? Utils.getModifiableCopy(meta.getLore()) : new ArrayList<String>();
		int cost = prices[0]*64+prices[1];
		String ccolorCode = rp.getBank() > cost ? "§a" : "§c";
		String dcolorCode = rp.getBank() > cost ? "§2" : "§4";
		int quantity = this.getShop() instanceof PlayerShop ? ((PlayerShop)this.getShop()).getEditionUI().getQuantity(items) : 1;
		lore.addAll(Arrays.asList("§8§m-------------------", "", ("§7Coût : " + ccolorCode + cost + " " + dcolorCode + "émeraudes"), ("§7Quantité restante : §8" + quantity)));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	protected void onClick(InventoryClickEvent e, Player p, ItemStack item) {
		e.setCancelled(true);
		if(!canClick)return;
		canClick = false;
		Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){
			public void run(){
				canClick = true;
			}
		}, 4);
		
		int slot = e.getRawSlot();
		int amount = 1;
		if(e.isShiftClick())amount*=5;
		ItemStack stack = this.get(slot);
		Integer[] prices = this.getShop().getPrices().get(this.getShop().getItemStacks().indexOf(stack));
		int cost = (prices[0]*64+prices[1])*amount;
		if(rp.getBank() > cost){
			int quantityAvailable = this.getShop() instanceof PlayerShop ? ((PlayerShop)this.getShop()).getEditionUI().getQuantity(stack) : 5;
			if(quantityAvailable >= amount){
				if(Utils.getAmountCanHold(p, stack) >= stack.getAmount()*amount){
					String id;
					ItemStack is = stack.clone();
					ItemMeta meta = is.getItemMeta();
					if(meta.hasDisplayName())id = meta.getDisplayName();
					else id = is.getType().toString();
					rp.sendMessage("§aVous avez acheté §e" + amount*stack.getAmount() + " §6" + id + " §apour §2" + cost + " §aémeraudes !");
					EconomyHandler.withdraw(p, cost);
					for(int i = 0;i < amount;i++){
						p.getInventory().addItem(is);
					}
					if(this.getShop() instanceof PlayerShop){
						PlayerShop shop = (PlayerShop)this.getShop();
						RPlayer.get(shop.getHolder()).sendMessage("§2" + rp.getName() + " §avous a acheté §e" + amount*stack.getAmount() + " §6" + id + " §apour §2" + cost + " §aémeraudes !");
						EconomyHandler.deposit(shop.getHolder(), cost);
						shop.getEditionUI().buyItem(stack, amount);
					}
					if(quantityAvailable-amount > 0)this.menu.setItem(slot, this.getItem(stack));
					else{
						this.closeUI();
						this.getShop().open(this.getHolder());
					}
				}else rp.sendMessage("§cVous n'avez pas assez de place pour garder vos items dans votre inventaire !");
			}else rp.sendMessage("§cIl n'y a plus assez d'items disponibles...");
		}else rp.sendMessage("§cVous n'avez pas assez d'émeraudes dans votre banque !");
	}
	
	@Override
	protected void onInfosClick(InventoryClickEvent e) {
	}
	
	@Override
	protected void onOpen() {
		this.list.clear();
		for(ItemStack item : this.getShop().getItemStacks()){
			this.list.add(item);
		}
	}
	
	@Override
	public String getType() {
		return "SHOP_TRADE_MENU";
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		this.getShop().getBuyers().remove(this.getHolder());
		if(this.getShop() instanceof PlayerShop){
			((PlayerShop)this.getShop()).getHolder().sendMessage("§6§l[-] §e" + this.getHolder().getName());
		}
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@Override
	protected void onPageTurn() {
	}
}