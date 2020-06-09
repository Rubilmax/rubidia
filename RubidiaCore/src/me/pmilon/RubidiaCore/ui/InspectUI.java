package me.pmilon.RubidiaCore.ui;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InspectUI extends UIHandler {

	public final Player inspected;
	
	private int SLOT_HELMET = 4,SLOT_ARMOR = 13,SLOT_GAUNTLETS = 22,SLOT_BOOTS = 31,SLOT_MHAND = 12,SLOT_OHAND = 14;
	
	public InspectUI(Player p, Player inspected) {
		super(p);
		this.inspected = inspected;
		this.menu = Bukkit.createInventory(this.getHolder(), 36, RPlayer.get(inspected).getName());
	}

	@Override
	public String getType() {
		return "INSPECTION_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_HELMET, this.getHelmet());
		this.menu.setItem(this.SLOT_ARMOR, this.getArmor());
		this.menu.setItem(this.SLOT_GAUNTLETS, this.getGauntlet());
		this.menu.setItem(this.SLOT_BOOTS, this.getBoots());
		this.menu.setItem(this.SLOT_MHAND, this.getMHand());
		this.menu.setItem(this.SLOT_OHAND, this.getOHand());
		
		ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		ItemMeta meta = empty.getItemMeta();
		meta.setDisplayName(" ");
		empty.setItemMeta(meta);
		for(int i = 0;i < 36;i++){
			if(i != SLOT_HELMET && i != SLOT_ARMOR && i != SLOT_GAUNTLETS && i != SLOT_BOOTS && i != SLOT_MHAND && i != SLOT_OHAND){
				this.getMenu().setItem(i, empty);
			}
		}
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
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

	public ItemStack getHelmet(){
		ItemStack item = this.getInspected().getEquipment().getHelmet();
		if(item == null || item.getType().equals(Material.AIR)){
			item = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f§l" + ("Casque"));
			item.setItemMeta(meta);
		}else item = item.clone();
		return item;
	}
	public ItemStack getArmor(){
		ItemStack item = this.getInspected().getEquipment().getChestplate();
		if(item == null || item.getType().equals(Material.AIR)){
			item = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f§l" + ("Plastron"));
			item.setItemMeta(meta);
		}else item = item.clone();
		return item;
	}
	public ItemStack getGauntlet(){
		ItemStack item = this.getInspected().getEquipment().getLeggings();
		if(item == null || item.getType().equals(Material.AIR)){
			item = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f§l" + ("Gants"));
			item.setItemMeta(meta);
		}else item = item.clone();
		return item;
	}
	public ItemStack getBoots(){
		ItemStack item = this.getInspected().getEquipment().getBoots();
		if(item == null || item.getType().equals(Material.AIR)){
			item = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f§l" + ("Bottes"));
			item.setItemMeta(meta);
		}else item = item.clone();
		return item;
	}
	public ItemStack getMHand(){
		ItemStack item = this.getInspected().getEquipment().getItemInMainHand();
		if(item == null || item.getType().equals(Material.AIR)){
			item = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f§l" + ("Main droite"));
			item.setItemMeta(meta);
		}else item = item.clone();
		return item;
	}
	public ItemStack getOHand(){
		ItemStack item = this.getInspected().getEquipment().getItemInOffHand();
		if(item == null || item.getType().equals(Material.AIR)){
			item = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f§l" + ("Main gauche"));
			item.setItemMeta(meta);
		}else item = item.clone();
		return item;
	}

	private Player getInspected() {
		return this.inspected;
	}
	
}
