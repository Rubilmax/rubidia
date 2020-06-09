package me.pmilon.RubidiaCore.ui.weapons;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponSetBuffEditionMenu extends UIHandler {

	private Weapon weapon;
	private Buff buff;
	private Set set;
	
	private int SLOT_TYPE = 0, SLOT_LEVEL = 1, SLOT_BACK = 8;
	private int LIST_ID_LEVEL = 1;
	public WeaponSetBuffEditionMenu(Player p, Weapon weapon, Set set, Buff buff) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, "Buff Edition");
		this.weapon = weapon;
		this.set = set;
		this.buff = buff;
	}

	@Override
	public String getType() {
		return "WEAPONS_SET_BUFF_EDITION_MENU";
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(Utils.isInteger(this.getMessage())){
					int level = Integer.valueOf(this.getMessage());
					this.getBuff().setLevel(level);
				}
			}
		}
		this.menu.setItem(this.SLOT_TYPE, this.getBType());
		this.menu.setItem(this.SLOT_LEVEL, this.getLevel());
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new WeaponSetBuffsMenu(this.getHolder(), this.getWeapon(), this.getSet()));
		else if(slot == this.SLOT_TYPE){
			if(this.getBuff().getType().equals(BuffType.ABILITY_DAMAGE))this.getBuff().setType(BuffType.LIFT_COST);
			else if(this.getBuff().getType().equals(BuffType.LIFT_COST))this.getBuff().setType(BuffType.MELEE_DAMAGE);
			else if(this.getBuff().getType().equals(BuffType.MELEE_DAMAGE))this.getBuff().setType(BuffType.RANGED_DAMAGE);
			else if(this.getBuff().getType().equals(BuffType.RANGED_DAMAGE))this.getBuff().setType(BuffType.MAGIC_DAMAGE);
			else if(this.getBuff().getType().equals(BuffType.MAGIC_DAMAGE))this.getBuff().setType(BuffType.DEFENSE);
			else if(this.getBuff().getType().equals(BuffType.DEFENSE))this.getBuff().setType(BuffType.ATTACK_SPEED);
			else if(this.getBuff().getType().equals(BuffType.ATTACK_SPEED))this.getBuff().setType(BuffType.WALK_SPEED);
			else if(this.getBuff().getType().equals(BuffType.WALK_SPEED))this.getBuff().setType(BuffType.MAX_HEALTH);
			else if(this.getBuff().getType().equals(BuffType.MAX_HEALTH))this.getBuff().setType(BuffType.MAX_ENERGY);
			else if(this.getBuff().getType().equals(BuffType.MAX_ENERGY))this.getBuff().setType(BuffType.ENERGY_REGEN);
			else if(this.getBuff().getType().equals(BuffType.ENERGY_REGEN))this.getBuff().setType(BuffType.XP);
			else if(this.getBuff().getType().equals(BuffType.XP))this.getBuff().setType(BuffType.ABILITY_DAMAGE);
			this.menu.setItem(this.SLOT_TYPE, this.getBType());
		}else if(slot == this.SLOT_LEVEL)this.close(true, this.LIST_ID_LEVEL);
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
	
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	private ItemStack getBType(){
		ItemStack item = new ItemStack(this.getBuff().getType().getMaterial(), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getBuff().getType().toString());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getLevel(){
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, this.getBuff().getLevel() > 0 ? (this.getBuff().getLevel() > 64 ? 64 : this.getBuff().getLevel()) : 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(String.valueOf(this.getBuff().getLevel()));
		item.setItemMeta(meta);
		return item;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public Buff getBuff() {
		return buff;
	}

	public void setBuff(Buff buff) {
		this.buff = buff;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}

}
