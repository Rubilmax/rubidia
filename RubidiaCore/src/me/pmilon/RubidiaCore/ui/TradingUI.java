package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradingUI extends UIHandler {

	protected final TradingUI instance;
	
	protected final Player trader;
	final ArrayList<Integer> noslots = new ArrayList<>(Arrays.asList(4,5,6,7,8,13,14,15,16,17,22,23,24,25,26,31,32,33,34,35,40,41,42,43,44));
	final ArrayList<Integer> slots1 = new ArrayList<>(Arrays.asList(0,1,2,3,9,10,11,12,18,19,20,21,27,28,29,30,36,37,38));
	final ArrayList<Integer> slots2 = new ArrayList<>(Arrays.asList(5,6,7,8,14,15,16,17,23,24,25,26,32,33,34,35,41,42,43));
	ItemStack ITEM_NONE = new ItemStack(Material.GRAY_DYE, 1);
	ItemStack ITEM_AGREE = new ItemStack(Material.LIME_DYE, 1);
	boolean triggerCloseEvent = true;
	
	public TradingUI(Player p, Player trader, boolean newtrade) {
		super(p);
		this.instance = this;
		this.trader = trader;
		this.menu = Bukkit.createInventory(this.getHolder(), 45, this.getHolder().getName() + " : " + this.getTrader().getName());
		if(newtrade)this.getUIManager().requestUI(new TradingUI(trader, p, false));
	}

	@Override
	public String getType() {
		return "TRADING_MENU";
	}

	@Override
	protected boolean openWindow() {
		ItemStack is = this.getSeparator();
		
		getMenu().setItem(4, is);
		getMenu().setItem(13, is);
		getMenu().setItem(22, is);
		getMenu().setItem(31, is);
		getMenu().setItem(40, is);
		getMenu().setItem(39, this.getNotReady());
		getMenu().setItem(44, this.getNotReadyTrader());
		return this.getHolder().openInventory(getMenu()) != null;
	}

	private Player getTrader() {
		return this.trader;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		ItemStack item = e.getCurrentItem();
		if(item != null){
			Collections.sort(slots1);
			Collections.sort(slots2);
			if(noslots.contains(e.getRawSlot())){
				e.setCancelled(true);
			}else if(slots1.contains(e.getRawSlot())){
				//CANCEL TRADE ACCEPT IF TRUE
				if(this.getMenu().getItem(39).getType().equals(Material.LIME_DYE)){
					this.setItem(39, this.getNotReady());
					this.getTraderUI().setItem(44, this.getTraderUI().getNotReadyTrader());
					Utils.updateInventory(this.getHolder());
					Utils.updateInventory(this.getTraderUI().getHolder());
				}
				if(this.getMenu().getItem(44).getType().equals(Material.LIME_DYE)){
					this.setItem(44, this.getNotReadyTrader());
					this.getTraderUI().setItem(39, this.getTraderUI().getNotReady());
					Utils.updateInventory(this.getHolder());
					Utils.updateInventory(this.getTraderUI().getHolder());
				}
				new BukkitTask(Core.instance){
					public void run(){
						for(int i : slots1){
							int index = slots1.indexOf(i);
							if(instance.getItem(slots1.get(index)) != null)instance.getTraderUI().setItem(slots2.get(index), instance.getItem(slots1.get(index)));
							else instance.getTraderUI().setItem(slots2.get(index), new  ItemStack(Material.AIR, 1));
						}
						Utils.updateInventory(instance.getTrader());
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(1);
			}else if(e.getRawSlot() == 39){
				if(item.getType().equals(Material.GRAY_DYE)) {
					//ACCEPT TRADE
					e.setCurrentItem(this.getReady());
					e.setCancelled(true);
					this.getTraderUI().setItem(44, this.getTraderUI().getReadyTrader());
					for(int i : slots1){
						int index = slots1.indexOf(i);
						if(this.getItem(slots1.get(index)) != null)this.getTraderUI().setItem(slots2.get(index), this.getItem(slots1.get(index)));
						else instance.getTraderUI().setItem(slots2.get(index), new  ItemStack(Material.AIR, 1));
					}
					Utils.updateInventory(this.getTrader());
					
					//TRADE END
					if(e.getClickedInventory().getItem(44).getType().equals(Material.LIME_DYE)){
						this.getTraderUI().close(false, true);
						this.close(false, true);
					}
				} else if(item.getType().equals(Material.LIME_DYE)) {
					//DENY TRADE
					e.setCurrentItem(this.getNotReady());
					e.setCancelled(true);
					this.getTraderUI().setItem(44, this.getTraderUI().getNotReadyTrader());
					
					for(Object i : slots1){
						int index = slots1.indexOf(i);
						if(this.getItem(slots1.get(index)) != null)this.getTraderUI().setItem(slots2.get(index), this.getItem(slots1.get(index)));
						else instance.getTraderUI().setItem(slots2.get(index), new ItemStack(Material.AIR, 1));
					}
					Utils.updateInventory(this.getTrader());
				}
			}else e.setCancelled(true);
		}
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(triggerCloseEvent){
			this.getTraderUI().close(false, false);
			this.close(true, false);
		}
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	private void close(boolean selfClose, boolean tradeOk){
		this.triggerCloseEvent = false;
		this.close(false);
		if(tradeOk){
			rp.sendMessage("§aL'échange s'est bien déroulé !");
			for(int slot : slots2)if(this.getItem(slot) != null)this.getHolder().getInventory().addItem(this.getItem(slot));
			Utils.updateInventory(this.getHolder());
		}else{
			if(selfClose)rp.sendMessage("§cVous avez annulé l'échange !");
			else rp.sendMessage("§4" + this.getTrader().getName() + " §ca annulé l'échange !");
			
			for(int slot : slots1)if(this.getItem(slot) != null)this.getHolder().getInventory().addItem(this.getItem(slot));
		}
	}
	
	private TradingUI getTraderUI() {
		if(this.getUIManager().hasActiveSession(this.getTrader()))return (TradingUI) this.getUIManager().getSession(this.getTrader()).getUIHandler();;
		return null;
	}
	
	private ItemStack getItem(int slot){
		return this.getMenu().getItem(slot);
	}
	private void setItem(int slot, ItemStack is){
		this.getMenu().setItem(slot, is);
	}
	
	private ItemStack getNotReadyTrader(){
		ItemMeta imn2 = ITEM_NONE.getItemMeta();
		imn2.setDisplayName("§4§l" + ("Pas prêt"));
		imn2.setLore(Arrays.asList(("§c" + this.getTrader().getName() + " §cn'est pas prêt à échanger")));
		ITEM_NONE.setItemMeta(imn2);
		ITEM_NONE.setAmount(1);
		return ITEM_NONE;
	}
	private ItemStack getNotReady(){
		ItemMeta imn = ITEM_NONE.getItemMeta();
		imn.setDisplayName("§4§l" + ("Pas prêt"));
		imn.setLore(Arrays.asList(("§cCliquez ici pour confirmer l'échange"), ("§cVous n'êtes pas prêt à échanger")));
		ITEM_NONE.setItemMeta(imn);
		ITEM_NONE.setAmount(1);
		return ITEM_NONE;
	}
	private ItemStack getReadyTrader(){
		ItemMeta im2 = ITEM_AGREE.getItemMeta();
		im2.setDisplayName("§2§l" + ("Prêt"));
		im2.setLore(Arrays.asList(("§a" + this.getTrader().getName() + " §aest prêt")));
		ITEM_AGREE.setItemMeta(im2);
		ITEM_AGREE.setAmount(1);
		return ITEM_AGREE;
	}
	private ItemStack getReady(){
		ItemMeta im = ITEM_AGREE.getItemMeta();
		im.setDisplayName("§2§l" + ("Prêt"));
		im.setLore(Arrays.asList(("§aCliquez ici pour annuler l'échange"), ("§aVous êtes prêt à échanger")));
		ITEM_AGREE.setItemMeta(im);
		ITEM_AGREE.setAmount(1);
		return ITEM_AGREE;
	}
	private ItemStack getSeparator(){
		ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§8" + ("Séparateur"));
		im.setLore(Arrays.asList(("§7Vous ne pouvez accéder à la partie droite de cet inventaire")));
		is.setItemMeta(im);
		return is;
	}

}
