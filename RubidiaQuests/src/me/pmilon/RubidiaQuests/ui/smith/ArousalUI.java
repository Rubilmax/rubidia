package me.pmilon.RubidiaQuests.ui.smith;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.crafts.Crafts;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

public class ArousalUI extends UIHandler {
	
	private final int SLOT_ROUSE = 4;
	public ArousalUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, "Éveil");
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "AROUSAL_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event, Player arg1) {
		ItemStack stack = event.getCurrentItem();
		if(stack != null) {
			if(!stack.getType().equals(Material.AIR)) {
				int slot = event.getRawSlot();
				if(slot == this.SLOT_ROUSE) {
					event.setCancelled(true);
					for(int i = 0;i < this.SLOT_ROUSE;i++) {
						ItemStack item = this.menu.getItem(i);
						if(item != null) {
							if(Weapons.COMMON_WEAPON_TYPES.contains(item.getType())) {
								if(item.hasItemMeta()) {
									if(item.getItemMeta().hasLore()) {
										if(item.getItemMeta().getLore().contains(Crafts.ROUSABLE_LORE)) {
											RItem rItem = new RItem(item);
											if(!rItem.isWeapon()) {
												Weapon weapon = Weapons.random(rp, 0, rp.getRLevel(), item.getType());
												if(weapon == null) {
													weapon = Weapons.random(0, rp.getRLevel(), item.getType());
													if(weapon == null) {
														weapon = Weapons.random(item.getType(), Rarity.COMMON);
													}
												}
												
												this.getHolder().getInventory().addItem(weapon.getNewItemStack(rp));
												rp.sendMessage("§aLe forgeron a éveillé " + weapon.getDisplayName() + " §a!");
												this.getHolder().getWorld().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
												this.getHolder().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, this.getHolder().getLocation().add(0,.8,0), 40, .5, .8, .5, 0);
												continue;
											}
										}
									}
								}
							}
							
							this.getHolder().getInventory().addItem(item);
							rp.sendMessage("§aLe forgeron ne peut pas éveiller " + (item.hasItemMeta() ? item.getItemMeta().getDisplayName() : "cet item") + "§a.");
						}
					}
					
					this.close(false);
				}
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		for(int i = 0;i < this.SLOT_ROUSE;i++) {
			ItemStack stack = this.menu.getItem(i);
			if(stack != null) {
				this.getHolder().getInventory().addItem(stack);
			}
		}
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_ROUSE, this.getRouse());
		return this.getHolder().openInventory(this.menu) != null;
	}
	

	private ItemStack getRouse(){
		ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f§lÉveil");
		meta.setLore(Arrays.asList("§7Les arme/armures déposées ici seront éveillées", "§7et pourront par la suite être utilisées", "§eOpération aléatoire"));
		item.setItemMeta(meta);
		return item;
	}

}
