package me.pmilon.RubidiaPets.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.utils.Settings;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PetDistinctionsMenu extends UIHandler {

	private Pet pet;
	private int SLOT_SKD = 0, SLOT_ARD = 1, SLOT_PAT = 2, SLOT_ACT = 3, SLOT_BACK = 4;
	private boolean canClick = true;
	public PetDistinctionsMenu(Player p, Pet pet) {
		super(p);
		this.pet = pet;
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, StringUtils.abbreviate(pet.getName() + " §r: Distinctions", 32));
	}

	@Override
	public String getType() {
		return "PET_DISTINCTIONS_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_SKD, this.getDistinctionPoints());
		this.getMenu().setItem(this.SLOT_ARD, this.getArdor());
		this.getMenu().setItem(this.SLOT_PAT, this.getPatience());
		this.getMenu().setItem(this.SLOT_ACT, this.getAcuity());
		this.getMenu().setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot > 0 && slot < 4){
			if(canClick){
				canClick = false;
				new BukkitTask(Core.instance){
					public void run(){
						canClick = true;
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(6);
				int amount = 1;
				if(e.isShiftClick())amount = 5;
				if(this.getPet().getDistinctionPoints() >= amount){
					this.getPet().setDistinctionPoints(this.getPet().getDistinctionPoints()-amount);
					if(slot == this.SLOT_ARD){
						this.getPet().setArdor(this.getPet().getArdor()+amount);
						this.menu.setItem(slot, this.getArdor());
					}else if(slot == this.SLOT_PAT){
						this.getPet().setPatience(this.getPet().getPatience()+amount);
						this.menu.setItem(slot, this.getPatience());
						this.getPet().getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getPet().getMaxHealth());
						this.getPet().getEntity().setHealth(this.getPet().getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
						this.getPet().updateHealth();
					}else if(slot == this.SLOT_ACT){
						this.getPet().setAcuity(this.getPet().getAcuity()+amount);
						this.menu.setItem(slot, this.getAcuity());
					}
					this.menu.setItem(this.SLOT_SKD, this.getDistinctionPoints());
				}else{
					this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					rp.sendMessage("§cVotre compagnon n'a pas assez de points de distinction !");
				}
			}
		}else if(slot == this.SLOT_BACK){
			Core.uiManager.requestUI(new PetUI(this.getHolder(), this.getPet()));
		}
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
	
	private ItemStack getDistinctionPoints(){
		ItemStack item = new ItemStack(Material.BOOK, this.getPet().getDistinctionPoints() > 0 ? (this.getPet().getDistinctionPoints() > 64 ? 64 : this.getPet().getDistinctionPoints()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = this.getPet().getDistinctionPoints() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + this.getPet().getDistinctionPoints() + color + " " + ("point") + (this.getPet().getDistinctionPoints() > 1 ? "s" : "") + (" de distinction"));
		meta.setLore(Arrays.asList("§7" + ("Les points de distinction sont gagnés à chaque niveau que votre compagnon gagne."), "§7" + ("Ils vous permettent d'augmenter le niveau des distinctions suivantes."), "§7" + ("Les détails sur les effets de chaque distinction sont disponibles au survol.")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getArdor(){
		ItemStack item = new ItemStack(Material.LIGHT_GRAY_DYE, this.getPet().getArdor() > 0 ? (this.getPet().getArdor() > 64 ? 64 : this.getPet().getArdor()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = this.getPet().getArdor() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + this.getPet().getArdor() + color + " " + ("point") + (this.getPet().getArdor() > 1 ? "s" : "") + (" d'ardeur"));
		meta.setLore(Arrays.asList("§7" + ("L'ardeur améliore tous les dégâts"), ("§7que votre compagnon inflige aux ennemis."), "§4" + Utils.round(this.getPet().getDamages(), 2) + " " + (this.getPet().getDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ARDOR_DAMAGES_FACTOR, 2) + ")" : "") + "§l §8" + ("dégâts")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getPatience(){
		ItemStack item = new ItemStack(Material.CACTUS_GREEN, this.getPet().getPatience() > 0 ? (this.getPet().getPatience() > 64 ? 64 : this.getPet().getPatience()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = this.getPet().getPatience() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + this.getPet().getPatience() + color + " " + ("point") + (this.getPet().getPatience() > 1 ? "s" : "") + (" de patience"));
		meta.setLore(Arrays.asList("§7" + ("La patience augmente les points de vie de votre compagnon."), "§4" + Utils.round(this.getPet().getMaxHealth(), 2) + " " + (this.getPet().getDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.PATIENCE_HEALTH_FACTOR, 2) + ")" : "") + "§l §8" + ("points de vie")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getAcuity(){
		ItemStack item = new ItemStack(Material.PURPLE_DYE, this.getPet().getAcuity() > 0 ? (this.getPet().getAcuity() > 64 ? 64 : this.getPet().getAcuity()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = this.getPet().getAcuity() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + this.getPet().getAcuity() + color + " " + ("point") + (this.getPet().getAcuity() > 1 ? "s" : "") + (" d'acuité"));
		meta.setLore(Arrays.asList("§7" + ("L'acuité améliore la vitesse d'attaque de votre compagnon."), "§4" + Utils.round(this.getPet().getAttackSpeed(), 2) + " " + (this.getPet().getDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ACUITY_ATTACKSPEED_FACTOR, 2) + ")" : "") + "§l §8" + ("attaque/seconde")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("§7" + ("Retour"));
		back.setItemMeta(meta);
		return back;
	}
	
	public Pet getPet() {
		return pet;
	}
	
	public void setPet(Pet pet) {
		this.pet = pet;
	}

}
