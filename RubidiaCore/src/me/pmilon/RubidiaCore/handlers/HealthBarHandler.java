package me.pmilon.RubidiaCore.handlers;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.tasks.BossBarTimer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.regions.Monsters;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class HealthBarHandler implements Listener {
	
	public HealthBarHandler(Core core){
		Bukkit.getPluginManager().registerEvents(this, core);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	private void onHit(RubidiaEntityDamageEvent e){
		if(!e.isCancelled()){
			Entity damager = e.getDamager();
			if(damager instanceof Player){
				final Player player = (Player)damager;
				final RPlayer rp = RPlayer.get(player);
				final LivingEntity damaged = (LivingEntity)e.getEntity();
				if(!(damaged instanceof ArmorStand) && !(damaged instanceof EnderDragon)){
					if(rp.getCombatLevel() > 0){
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								if(rp.barTimers.containsKey(damaged)){
									BossBarTimer timer = rp.barTimers.get(damaged);
									timer.cancel();
									timer.run();
								}
								rp.barTimers.put(damaged, (BossBarTimer) new BossBarTimer(Core.instance, player, HealthBarHandler.newHealthBar(rp, damaged), damaged).runTaskLater(130));
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(0);
					}
				}
			}
		}
	}

	@EventHandler
	private void onDamage(final EntityDamageEvent e){
		if(e.getEntity() instanceof LivingEntity){
			LivingEntity entity = (LivingEntity)e.getEntity();
			if(!(entity instanceof ArmorStand) && !(entity instanceof EnderDragon)){
				for(Player player : Core.toPlayerList(entity.getNearbyEntities(16, 16, 16))){
					RPlayer rp = RPlayer.get(player);
					if(rp.barTimers.containsKey(entity)){
						final BossBarTimer timer = rp.barTimers.get(entity);
						timer.cancel();
						timer.run();
						rp.barTimers.put(entity, (BossBarTimer) new BossBarTimer(Core.instance, player, HealthBarHandler.newHealthBar(rp, entity), entity).runTaskLater(130));
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDeath(final EntityDeathEvent e){
		if(e.getEntity() instanceof LivingEntity){
			final LivingEntity entity = (LivingEntity)e.getEntity();
			for(final Player player : Core.toPlayerList(entity.getNearbyEntities(16, 16, 16))){
				final RPlayer rp = RPlayer.get(player);
				if(rp.barTimers.containsKey(entity)){
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							final BossBarTimer timer = rp.barTimers.get(entity);
							timer.cancel();
							timer.run();
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(20);
				}
			}
		}
	}
	
	public static String getTheoricName(RPlayer rp, LivingEntity entity){
		if(entity != null){
			String name = entity.getType().toString();
			if(entity instanceof Bat)name = ("CHAUVE-SOURIS");
			else if(entity instanceof CaveSpider)name = ("ARAIGNEE BLEUE");
			else if(entity instanceof Chicken)name = ("POULE");
			else if(entity instanceof Cow)name = ("VACHE");
			else if(entity instanceof Giant)name = ("GEANT");
			else if(entity instanceof Guardian)name = ("GARDIEN");
			else if(entity instanceof Horse)name = ("CHEVAL");
			else if(entity instanceof IronGolem)name = ("GOLEM DE FER");
			else if(entity instanceof MagmaCube)name = ("CUBE DE MAGMA");
			else if(entity instanceof MushroomCow)name = ("CHAMPIMEUH");
			else if(entity instanceof Pig)name = ("COCHON");
			else if(entity instanceof PigZombie)name = ("COCHON ZOMBIE");
			else if(entity instanceof Rabbit)name = ("LAPIN");
			else if(entity instanceof Sheep)name = ("MOUTON");
			else if(entity instanceof Skeleton)name = ("SQUELETTE");
			else if(entity instanceof Snowman)name = ("GOLEM DE NEIGE");
			else if(entity instanceof Spider)name = ("ARAIGNEE");
			else if(entity instanceof Squid)name = ("PIEUVRE");
			else if(entity instanceof Villager)name = ("VILLAGEOIS");
			else if(entity instanceof Witch)name = ("SORCIERE");
			else if(entity instanceof Wolf)name = ("LOUP");
			else if(entity instanceof Player)name = entity.getName();
			if(entity.getCustomName() != null)name = entity.getCustomName();
			
			me.pmilon.RubidiaMonsters.regions.Monster monster = Monsters.get(entity);
			if(monster != null){
				name = (monster.isEnraged() ? "§c" : "§e") + monster.getName();
			}
			double ratio = entity.getHealth()/entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			name = "§8[§7" + Math.round(entity.getHealth()) + "§8/" + Math.round(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) + "] §r" + name + "§l    §8(§7" + Math.round(ratio*100) + "§8%)";
			return name;
		}
		return "";
	}
	
	public static BarColor getBossBarColor(LivingEntity entity){
		double ratio = entity.getHealth()/entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return ratio >= .6 ? BarColor.GREEN : ratio >= .25 ? BarColor.YELLOW : BarColor.RED;
	}
	
	public static BarStyle getBossBarStyle(LivingEntity entity){
		double health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return health >= 90 ? BarStyle.SEGMENTED_20 : health >= 60 ? BarStyle.SEGMENTED_12 : health >= 30 ? BarStyle.SEGMENTED_10 : BarStyle.SEGMENTED_6;
	}
	
	public static BossBar newHealthBar(RPlayer rp, LivingEntity entity){
		BossBar bar = Bukkit.createBossBar(HealthBarHandler.getTheoricName(rp, entity),
				HealthBarHandler.getBossBarColor(entity),
				HealthBarHandler.getBossBarStyle(entity),
				BarFlag.DARKEN_SKY);
		bar.setProgress(Math.max(Math.min(entity.getHealth()/entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(),1.0),0.0));
		bar.setVisible(true);
		bar.addPlayer(rp.getPlayer());
		return bar;
	}
	
}
