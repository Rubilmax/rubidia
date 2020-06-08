package me.pmilon.RubidiaCore.ritems.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaMonsters.events.MonsterKillEvent;
import me.pmilon.RubidiaMonsters.regions.Monster;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ItemListener implements Listener {

	private Plugin plugin;
	public ItemListener(Plugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onKill(MonsterKillEvent event){
		Player player = event.getKiller();
		RPlayer rp = RPlayer.get(player);
		Monster monster = event.getMonster();
		LivingEntity entity = monster.getEntity();
		
		int lootFactor = 1;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
				lootFactor += meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS) * Settings.ENCHANTMENT_LOOT_BONUS_FACTOR;
			}
		}
		
		for(RItemStack rItem : RItemStacks.ITEMS){
			if(Math.random() < 1 / rItem.getDropRarity()){
				ItemStack dropItem = rItem.getItemStack();
				dropItem.setAmount(dropItem.getAmount() * lootFactor);
				entity.getWorld().dropItemNaturally(entity.getLocation(), dropItem);
			}
		}
		
		Rarity rarity = null;
		double rarityFactor = (1 + rp.getLootBonusChanceFactor());
		for(int i = Rarity.values().length-1;i >= 0;i--){
			rarity = Rarity.values()[i];
			if(Math.random() < rarity.getFactor() * rarityFactor){
				break;
			}
		}
		
		HashSet<Weapon> weapons = Weapons.getByLevel(monster.getLevel(), (int) (6+(monster.getLevel()/30.0)));
		weapons.addAll(Weapons.getByRarity(rarity));
		List<Weapon> available = Arrays.asList(weapons.toArray(new Weapon[weapons.size()]));
		Collections.shuffle(available);
		for(Weapon weapon : available){
			if(Math.random() < weapon.getDropChance() / Settings.GLOBAL_WEAPON_DROP_REDUCTION){
				double suppProbability = Math.random();
				
				int suppLevel = 0;
				if(suppProbability < .51) suppLevel++;
				if(suppProbability < .26) suppLevel++;
				if(suppProbability < .12) suppLevel++;
				
				Weapon toDrop = weapon.getNewInstance();
				toDrop.setSuppLevel(suppLevel);
				entity.getWorld().dropItemNaturally(entity.getLocation(), toDrop.getNewItemStack(rp));
				break;
			}
		}
	}
	
	@EventHandler
	public void onPickUp(EntityPickupItemEvent event){
		Item item = event.getItem();
		if(item != null) {
			if(item.hasMetadata("unpickable")){
				event.setCancelled(true);
			}
			
			if(item.hasMetadata("autoremove")){
				event.setCancelled(true);
				item.remove();
			}
		}
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		final Projectile projectile = event.getEntity();
		if(projectile.hasMetadata("autoremove")) {
			projectile.remove();
		}
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
}
