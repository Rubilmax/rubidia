package me.pmilon.RubidiaCore.ui.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.WeaponUse;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponsUI extends ListMenuUIHandler<Weapon> {
	
	public int LIST_ID_SEARCH = 1;
	
	public WeaponsUI(Player p) {
		super(p, "Weapons Menu", "Menu des Armes", 5);
		LinkedHashMap<String, List<Weapon>> lists = new LinkedHashMap<String, List<Weapon>>();
		for(RClass rClass : RClass.values()){
			lists.put(rClass.toString(), new ArrayList<Weapon>());
		}
		lists.put("SHIELDS", new ArrayList<Weapon>());
		lists.put("ARMORS", new ArrayList<Weapon>());
		for(Weapon weapon : Weapons.weapons){
			if(weapon.isAttack()){
				lists.get(weapon.getRClass().toString()).add(weapon);
			}else if(weapon.getType().equals(Material.SHIELD)){
				lists.get("SHIELDS").add(weapon);
			}else{
				lists.get("ARMORS").add(weapon);
			}
		}
		for(List<Weapon> weapons : lists.values()){
			Collections.sort(weapons, new Comparator<Weapon>() {
		        public int compare(Weapon w1, Weapon w2) {
		            return w1.getLevel()-w2.getLevel();
		        }
		    });
			list.addAll(weapons);
		}
		this.page_index = rp.weaponsIndex;
	}

	@Override
	protected void onOpen() {
		if(this.getListeningId() == this.LIST_ID_SEARCH){
			if(this.getMessage() != null){
				if(!this.getMessage().isEmpty()){
					this.list.clear();
					LinkedHashMap<String, List<Weapon>> lists = new LinkedHashMap<String, List<Weapon>>();
					for(RClass rClass : RClass.values()){
						lists.put(rClass.toString(), new ArrayList<Weapon>());
					}
					lists.put("SHIELDS", new ArrayList<Weapon>());
					lists.put("ARMORS", new ArrayList<Weapon>());
					for(Weapon weapon : Weapons.weapons){
						if(weapon.getName().toLowerCase().contains(this.getMessage().toLowerCase())){
							if(weapon.isAttack()){
								lists.get(weapon.getRClass().toString()).add(weapon);
							}else if(weapon.getType().equals(Material.SHIELD)){
								lists.get("SHIELDS").add(weapon);
							}else{
								lists.get("ARMORS").add(weapon);
							}
						}
					}
					for(List<Weapon> weapons : lists.values()){
						Collections.sort(weapons, new Comparator<Weapon>() {
					        public int compare(Weapon w1, Weapon w2) {
					            return w1.getLevel()-w2.getLevel();
					        }
					    });
						this.list.addAll(weapons);
					}
					this.page_index = 0;
				}
			}
		}
	}

	@Override
	protected ItemStack getItem(Weapon weapon) {
		ItemStack stack = weapon.getNewItemStack(rp);
		stack.setAmount(weapon.getLevel() > 0 ? (weapon.getLevel() > 64 ? 64 : weapon.getLevel()) : 1);
		return weapon.getSet() != null ? Utils.setGlowingWithoutAttributes(stack) : stack;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack is) {
		if(is != null){
			if(rp.isOp()){
				int slot = e.getRawSlot();
				Weapon weapon = this.get(slot);
				if(e.isLeftClick())Core.uiManager.requestUI(new WeaponEditionMenu(this.getHolder(), this.get(slot)));
				else this.getHolder().getInventory().addItem(weapon.getNewItemStack(rp));
			}
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Choisissez une arme à éditer"), ("§7ou cliquez ici pour en créer une nouvelle"), ("§7Clic droit pour rechercher une arme")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		if(rp.isOp()){
			if(e.isRightClick()){
				this.close(true, this.LIST_ID_SEARCH);
			}else{
				Weapon weapon = new Weapon(UUID.randomUUID().toString(), "Aucun nom", Rarity.COMMON, Material.WOODEN_SWORD, RClass.VAGRANT, 0.0, 0, 2, 0, "0000", WeaponUse.MELEE, 1.2, 0);
				Weapons.weapons.add(weapon);
				Core.uiManager.requestUI(new WeaponEditionMenu(this.getHolder(), weapon));
			}
		}
	}

	@Override
	public String getType() {
		return "WEAPONS_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void onPageTurn() {
		rp.weaponsIndex = this.page_index;
	}

}
