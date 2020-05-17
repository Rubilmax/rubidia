package me.pmilon.RubidiaCore.abilities;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.projectiles.ProjectileSource;

public class AbilitiesListener implements Listener{

	public AbilitiesListener(){
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		RPlayer rp = RPlayer.get(player);
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				if(!rp.getRClass().equals(RClass.VAGRANT) && event.getItem() != null){
					RAbility ability = rp.registerAbilityClick(event.getItem(), event.getAction().toString());
					if(ability != null) {
						ability.execute(rp, event);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		RPlayer rp = RPlayer.get(player);
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				Entity entity = event.getRightClicked();
				if(entity instanceof LivingEntity) {
					LivingEntity target = (LivingEntity) entity;
					ItemStack item = player.getEquipment().getItemInMainHand();
					if(!rp.getRClass().equals(RClass.VAGRANT) && item != null){
						RAbility ability = rp.registerAbilityClick(item, "MELEE_RIGHT_CLICK");
						if(ability != null) {
							ability.damage(rp, Arrays.asList(target));
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		if(rp.getLoadedSPlayer() != null){
			if(rp.getRClass().equals(RClass.ASSASSIN)){
				if(rp.isActiveAbility(RAbility.ASSASSIN_7)){
					if(p.getGameMode().equals(GameMode.SPECTATOR)){
						p.setGameMode(GameMode.valueOf(p.getMetadata("assassinGamemode").get(0).asString()));
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Inventory inv = e.getClickedInventory();
		HumanEntity human = e.getWhoClicked();
		
		if(inv != null){
			if(human instanceof Player){
				Player player = (Player)human;
				RPlayer rp = RPlayer.get(player);
				if(rp.getRClass().equals(RClass.ASSASSIN) && rp.isActiveAbility(RAbility.ASSASSIN_2)){
					if(e.getSlotType().equals(SlotType.ARMOR)){
						if(e.getCurrentItem().getType().equals(Material.PUMPKIN)){
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAttack(RubidiaEntityDamageEvent event){
		if(!event.isCancelled()){
			LivingEntity damager = event.getDamager();
			LivingEntity target = (LivingEntity) event.getEntity();
			if(event.getProjectile() != null){
				if(damager instanceof Player){
					Player player = (Player)damager;
					RPlayer rp = RPlayer.get(player);
					Projectile projectile = event.getProjectile();
					if(projectile instanceof Arrow){
						Arrow arrow = (Arrow) event.getProjectile();
						if(rp.getRClass().equals(RClass.RANGER)){
							if(arrow.hasMetadata("snipershot")){
								arrow.remove();
								event.setDamages(RAbility.RANGER_5.getAbility().getDamages(rp));
							} else if(arrow.hasMetadata("RANGER_12")){
								List<MetadataValue> metadata = arrow.getMetadata("RANGER_12");
								if(metadata.size() > 0) {
									BukkitTask task = BukkitTask.tasks.get(metadata.get(0).asInt());
									if(task != null) {
										task.cancel();
									}
								}
								RAbility.RANGER_12.getAbility().animate(rp, target);
							} else if(arrow.hasMetadata("RANGER_9")){
								List<MetadataValue> metadata = arrow.getMetadata("RANGER_9");
								if(metadata.size() > 0) {
									BukkitTask task = BukkitTask.tasks.get(metadata.get(0).asInt());
									if(task != null) {
										task.cancel();
									}
									event.setDamages(RAbility.RANGER_9.getDamages(rp));
									event.setDamageCause(RDamageCause.ABILITY);
								}
							}
							RAbility.RANGER_1.getAbility().animate(rp, target);
							if(rp.getAbilityLevel(RAbility.RANGER_4) > 0) {
								RAbility.RANGER_4.getAbility().animate(rp, target);
							}
						}
					}else if(projectile instanceof Snowball){
						if(rp.getRClass().equals(RClass.MAGE)){
							if(rp.isActiveAbility(RAbility.MAGE_5)){
								event.setCancelled(true);
								RAbility.MAGE_5.getAbility().damage(rp, Arrays.asList(target));
							}
						}
					}
				}
			} else {
				final LivingEntity damaged = (LivingEntity) event.getEntity();
				if(damager instanceof Player){
					Player player = (Player) damager;
					if(event.getDamageCause().equals(RDamageCause.MELEE)) {
						final RPlayer rp = RPlayer.get(player);
						if(rp.getRClass().equals(RClass.PALADIN)) {
							RAbility.PALADIN_1.getAbility().animate(rp, damaged);
						}
						
						ItemStack item = player.getEquipment().getItemInMainHand();
						if(!rp.getRClass().equals(RClass.VAGRANT) && item != null){
							final RAbility ability = rp.registerAbilityClick(item, "MELEE_LEFT_CLICK");
							if(ability != null) {
								new BukkitTask(Core.instance) {

									@Override
									public void run() {
										ability.damage(rp, Arrays.asList(damaged));
									}

									@Override
									public void onCancel() {
									}
									
								}.runTaskLater(0);
							}
						}
					}
					
					if(damaged instanceof Player){
						if(!((Player) damaged).canSee(player)){
							event.setDamages(event.getDamages()*.25);
						}
					}
				}
			}
			
			if(target instanceof Player) {
				Player player = (Player) target;
				RPlayer rp = RPlayer.get(player);
				if(rp.getRClass().equals(RClass.MAGE)) {
					RAbility.MAGE_1.getAbility().animate(rp, damager);
					RAbility.MAGE_2.getAbility().animate(rp, damager);
				}
			}
		}
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if(projectile instanceof Arrow) {
			Arrow arrow = (Arrow) projectile;
			ProjectileSource source = projectile.getShooter();
			if(source instanceof Player) {
				Player player = (Player) source;
				RPlayer rp = RPlayer.get(player);
				if(rp.getRClass().equals(RClass.RANGER)) {
					for(int i : new int[] {6,7,8,10}) {
						if(arrow.hasMetadata("RANGER_" + i)){
							List<MetadataValue> metadata = arrow.getMetadata("RANGER_" + i);
							if(metadata.size() > 0) {
								BukkitTask task = BukkitTask.tasks.get(metadata.get(0).asInt());
								if(task != null) {
									task.cancel();
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBowShoot(EntityShootBowEvent event) {
		if(!event.isCancelled()) {
			LivingEntity entity = event.getEntity();
			if(entity instanceof Player) {
				Player player = (Player) entity;
				RPlayer rp = RPlayer.get(player);
				if(rp.getRClass().equals(RClass.RANGER)) {
					RAbility.RANGER_2.getAbility().execute(rp, event);
					if(rp.isActiveAbility(RAbility.RANGER_6)) {
						RAbility.RANGER_6.getAbility().run(rp, event);
					}
					if(rp.isActiveAbility(RAbility.RANGER_7)) {
						RAbility.RANGER_7.getAbility().run(rp, event);
					}
					if(rp.isActiveAbility(RAbility.RANGER_8)) {
						RAbility.RANGER_8.getAbility().run(rp, event);
					}
					if(rp.isActiveAbility(RAbility.RANGER_10)) {
						RAbility.RANGER_10.getAbility().run(rp, event);
					}
					if(rp.isActiveAbility(RAbility.RANGER_12)) {
						RAbility.RANGER_12.getAbility().run(rp, event);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onExplode(ExplosionPrimeEvent event){
		Entity entity = event.getEntity();
		if(entity.hasMetadata("mageMeteor")){
			Player player = Bukkit.getPlayer(UUID.fromString(entity.getMetadata("mageMeteor").get(0).asString()));
			RPlayer rp = RPlayer.get(player);
			RAbility.MAGE_1.getAbility().damage(rp, DamageManager.toDamageableEntityList(entity.getNearbyEntities(2.5, 2.5, 2.5)));
		}
	}
}
