package me.pmilon.RubidiaCore.ui.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Sets;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.Configs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponSetSelectionMenu extends ListMenuUIHandler<Set> {

	private Weapon weapon;
	public WeaponSetSelectionMenu(Player p, Weapon weapon) {
		super(p, "Set Selection", "Set Selection", 3);
		this.weapon = weapon;
		for(Set set : Sets.sets){
			this.list.add(set);
		}
	}

	@Override
	protected void onOpen() {
	}

	@Override
	protected ItemStack getItem(Set set) {
		ItemStack item = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Set " + set.getName());
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack is) {
		if(is != null){
			int slot = e.getRawSlot();
			if(e.isRightClick()){
				Core.uiManager.requestUI(new WeaponSetEditionMenu(this.getHolder(), this.getWeapon(), this.get(slot)));
			}else{
				Set set = this.get(slot);
				this.getWeapon().setSetUUID(set.getUUID());
				this.getWeapon().setRarity(Rarity.SET);
				set.getWeapons().remove(this.getWeapon());
				set.getWeapons().add(this.getWeapon());
				if(Sets.sets.contains(set) && !Core.uiManager.isInTempSession(this.getHolder())){
					String path = "sets." + set.getUUID();
					Configs.getWeaponsConfig().set(path + ".name", set.getName());
					set.saveWeapons();
					set.saveBuffs();
					Configs.saveWeaponsConfig();
				}
				Core.uiManager.requestUI(new WeaponEditionMenu(this.getHolder(), this.getWeapon()));
			}
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Choisissez une arme à éditer"), ("§7ou cliquez ici pour un créer un nouveau")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Set set = new Set(UUID.randomUUID().toString(), "Aucun nom", new ArrayList<Buff>(), new ArrayList<Weapon>());
		Sets.sets.add(set);
		Core.uiManager.requestUI(new WeaponSetEditionMenu(this.getHolder(), this.getWeapon(), set));
	}

	@Override
	public String getType() {
		return "WEAPONS_SET_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}
	
	

}
