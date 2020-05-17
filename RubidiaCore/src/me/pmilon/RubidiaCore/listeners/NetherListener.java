package me.pmilon.RubidiaCore.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

public class NetherListener implements Listener {

	public static HashMap<Player, BukkitTask> gravity = new HashMap<Player, BukkitTask>();
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();
		if(from.getWorld().equals(to.getWorld()) && to.getWorld().getEnvironment().equals(Environment.NETHER)) {
			if(to.getY() - from.getY() > .1) {
				if(from.clone().subtract(player.getVelocity().multiply(.2)).getBlock().getType().isSolid()
						&& !to.clone().subtract(0,.01,0).getBlock().getType().isSolid()) {
					player.removePotionEffect(PotionEffectType.LEVITATION);
					player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 999999999, 3, true, false, false));
					gravity.put(player, new BukkitTask(Core.instance) {

						@Override
						public void run() {
							player.removePotionEffect(PotionEffectType.LEVITATION);
							player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 999999999, 251, true, false, false));
						}

						@Override
						public void onCancel() {
							player.removePotionEffect(PotionEffectType.LEVITATION);
							player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 999999999, 251, true, false, false));
						}
						
					}.runTaskLater(25));
				}
			} else {
				if(gravity.containsKey(player)) {
					BukkitTask task = gravity.get(player);
					task.cancel();
					gravity.remove(player);
				}
			}
		}
	}

	@EventHandler
	public void onTeleport(final PlayerTeleportEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		Location to = e.getTo();
		if(to.getWorld().getEnvironment().equals(Environment.NETHER)){
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, true, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 999999999, 251, true, false, false));
			rp.sendTitle("§7Les enfers", "", 20, 40, 20);
		} else {
			p.removePotionEffect(PotionEffectType.LEVITATION);
		}
	}
	
	public static void updateEntities() {
		for(World world : Bukkit.getWorlds()) {
			if(world.getEnvironment().equals(Environment.NETHER)) {
				for(Entity entity : world.getEntities()) {
					if(entity instanceof LivingEntity && !(entity instanceof Player)) {
						((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 999999999, 251, true, false, false));
					}
				}
			}
		}
	}
	
}
