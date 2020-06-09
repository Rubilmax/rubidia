package me.pmilon.RubidiaCore.ui.weapons;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Sets;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Configs;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;

public class WeaponSetEditionMenu extends UIHandler {

	private Weapon weapon;
	private Set set;
	
	private int SLOT_NAME = 0, SLOT_BUFFS = 1, SLOT_WEAPONS = 2, SLOT_BACK = 7, SLOT_DELETE = 8;
	private int LIST_ID_NAME = 1;
	
	public WeaponSetEditionMenu(Player p, Weapon weapon, Set set) {
		super(p);
		this.weapon = weapon;
		this.set = set;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate("Set Edition :: " + set.getName(),32));
	}

	@Override
	public String getType() {
		return "WEAPONS_SET_EDITION_MENU";
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_NAME){
					this.getSet().setName(this.getMessage());
				}
			}
		}
		this.menu.setItem(this.SLOT_NAME, this.getName());
		this.menu.setItem(this.SLOT_BUFFS, this.getBuffs());
		this.menu.setItem(this.SLOT_WEAPONS, this.getWeapons());
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_DELETE, this.getDelete());
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_NAME)this.close(true, this.LIST_ID_NAME);
		else if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new WeaponSetSelectionMenu(this.getHolder(), this.getWeapon()));
		else if(slot == this.SLOT_DELETE){
			Sets.sets.remove(this.getSet());
			Configs.getWeaponsConfig().set("sets." + this.getSet().getUUID(), null);
			Configs.saveWeaponsConfig();
			for(Weapon weapon : this.getSet().getWeapons()){
				weapon.setSetUUID("0000");
			}
			Core.uiManager.requestUI(new WeaponSetSelectionMenu(this.getHolder(), this.getWeapon()));
		}else if(slot == this.SLOT_BUFFS)Core.uiManager.requestUI(new WeaponSetBuffsMenu(this.getHolder(), this.getWeapon(), this.getSet()));
		else if(slot == this.SLOT_WEAPONS){
			for(Weapon weapon : this.getSet().getWeapons()){
				weapon.setSetUUID("0000");
			}
			this.getSet().getWeapons().clear();
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
	
	private ItemStack getName(){
		ItemStack item = new ItemStack(Material.MAP, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getSet().getName());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getBuffs(){
		ItemStack item = new ItemStack(Material.POTION, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Buffs");
		meta.setLore(this.getSet().getBuffState(this.getHolder()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getWeapons(){
		ItemStack item = new ItemStack(Material.POTION, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Weapons");
		meta.setLore(this.getSet().getWeaponState(null));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	private ItemStack getDelete(){
		ItemStack back = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("§4§lDELETE");
		back.setItemMeta(meta);
		return back;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}

}
