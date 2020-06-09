package me.pmilon.RubidiaQuests.ui.smith;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

public class SmithMenu extends UIHandler {

	private int SLOT_AROUSAL = 0, SLOT_ENHANCEMENT = 2, SLOT_PIERCING = 4;
	
	public SmithMenu(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, ("Forgeron"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "FORGE_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_AROUSAL)Core.uiManager.requestUI(new ArousalUI(this.getHolder()));
		else if(slot == this.SLOT_ENHANCEMENT)Core.uiManager.requestUI(new EnhancementUI(this.getHolder()));
		else if(slot == this.SLOT_PIERCING)Core.uiManager.requestUI(new PiercingUI(this.getHolder()));
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_AROUSAL, this.getArousal());
		this.getMenu().setItem(this.SLOT_ENHANCEMENT, this.getEnhancement());
		this.getMenu().setItem(this.SLOT_PIERCING, this.getPiercing());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private ItemStack getArousal(){
		ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Eveil");
		meta.setLore(Arrays.asList("§7Eveillez votre arme/armure pour", "§7révéler les secrets qu'ell renferme", "§eItem éveillable requis"));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getEnhancement(){
		ItemStack item = new ItemStack(Material.ANVIL,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6" + ("Renforcement"));
		meta.setLore(Arrays.asList("§7Améliorez la puissance de votre arme/armure", "§7en y appliquant un renforcement", "§ePierre étoile requise"));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getPiercing(){
		ItemStack item = new ItemStack(Material.SUNFLOWER,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Piercing");
		meta.setLore(Arrays.asList("§7Percez votre arme/armure afin d'y placer", "§7de joyaux pour augmenter vos caractéristiques", "§eOrichalque requis"));
		item.setItemMeta(meta);
		return item;
	}

}
