package me.pmilon.RubidiaCore.ui.weapons;

import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponSetBuffsMenu extends UIHandler {

	private Weapon weapon;
	private Set set;
	private int SLOT_BACK = 8;
	public WeaponSetBuffsMenu(Player p, Weapon weapon, Set set) {
		super(p);
		this.weapon = weapon;
		this.set = set;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate("Set Buffs",32));
	}

	@Override
	public String getType() {
		return "WEAPONS_SET_BUFFS_MENU";
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < 8;i++){
			ItemStack item;
			if(this.getSet().getBuffs().size() > i)item = this.getBuff(this.getSet().getBuffs().get(i));
			else item = this.getBuff(null);
			this.menu.setItem(i, item);
		}
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		List<Buff> buffs = this.getSet().getBuffs();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new WeaponSetEditionMenu(this.getHolder(), this.getWeapon(), this.getSet()));
		else if(buffs.size() > slot){
			if(e.isRightClick()){
				this.getSet().delete(buffs.get(slot));
				Core.uiManager.requestUI(this);
			}else Core.uiManager.requestUI(new WeaponSetBuffEditionMenu(this.getHolder(), this.getWeapon(), this.getSet(), buffs.get(slot)));
		}else{
			Buff buff = new Buff(buffs.size(), BuffType.MELEE_DAMAGE, 0);
			this.getSet().getBuffs().add(buff);
			Core.uiManager.requestUI(new WeaponSetBuffEditionMenu(this.getHolder(), this.getWeapon(), this.getSet(), buff));
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
	
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	private ItemStack getBuff(Buff buff){
		ItemStack item;
		if(buff != null){
			item = new ItemStack(buff.getType().getMaterial(), buff.getLevel() > 0 ? (buff.getLevel() > 64 ? 64 : buff.getLevel()) : 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(buff.getType().toString());
			item.setItemMeta(meta);
		}else{
			item = new ItemStack(Material.SLIME_BALL, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("NOTHING");
			item.setItemMeta(meta);
		}
		return item;
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
