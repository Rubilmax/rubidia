package me.pmilon.RubidiaCore.aeroplane;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class AeroplaneListener implements Listener {
	
	private Plugin plugin;
	public AeroplaneListener(Plugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		event.getPlayer().setAllowFlight(true);
	}
	
	@EventHandler
	public void onFlyAttempt(PlayerToggleFlightEvent event){
		final Player player = event.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			if(!event.isCancelled()){
				if(player.getGameMode().equals(GameMode.ADVENTURE) || player.getGameMode().equals(GameMode.SURVIVAL))event.setCancelled(true);
				ItemStack item = player.getEquipment().getChestplate();
				if(item != null){
					if(item.getType().equals(Material.ELYTRA)){
						event.setCancelled(true);
						if(!player.isGliding()){
							if(rp.hasVigor(rp.getAeroplaneCost())){
								boolean ok = true;
								for(int i = 1;i < 21;i++){
									if(player.getLocation().add(0,i,0).getBlock().getType().isSolid()){
										ok = false;
										break;
									}
								}
								
								if(ok){
									rp.addVigor(-rp.getAeroplaneCost());
									player.setVelocity(new Vector(0,1.8,0));
									new BukkitTask(this.getPlugin()){
										Location lastLocation = player.getLocation().clone();
										
										@Override
										public void run() {
											if(!player.isDead()){
												if(lastLocation.getWorld().equals(player.getWorld())){
													if(lastLocation.distanceSquared(player.getLocation()) < .032){
														cancel();
													}
												}
											}else cancel();
											lastLocation = player.getLocation().clone();
										}

										@Override
										public void onCancel() {
											if(!player.isDead()){
												player.setGliding(true);
												player.setVelocity(player.getEyeLocation().getDirection().multiply(2.2).setY(.25));
											}
										}
										
									}.runTaskTimer(10, 0);
								}else RPlayer.get(player).sendMessage("§cVous ne pouvez vous élever ici !");
							}else rp.sendMessage("§cVous n'avez pas assez de vigueur pour vous élever !");
						}
					}
				}
			}
		}
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
}
