package me.pmilon.RubidiaCore.handlers;

import java.util.Random;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.packets.FakeArmorStand;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.RandomUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class GamePlayEffectsHandler implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(final RubidiaEntityDamageEvent e){
		if(!e.isCancelled()){
			final Entity damaged = e.getEntity();
			for(final Player player : Core.toPlayerList(damaged.getNearbyEntities(16, 16, 16))){
				RPlayer rp = RPlayer.get(player);
				if(rp.getCombatLevel() > 1){
					Location location = damaged.getLocation().add(new Vector(RandomUtils.random.nextDouble(),-.5,RandomUtils.random.nextDouble()));
					final FakeArmorStand stand = new FakeArmorStand(location.getWorld(), "§c-" + Math.round(e.getDamages()), true).spawn(location, player);
					
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							stand.move(new Vector(0, .015, 0), player);
						}

						@Override
						public void onCancel() {
							stand.destroy(player);
						}
						
					}.runTaskTimerCancelling(0, 1, 30);
					if(rp.getCombatLevel() > 2){
						new BukkitTask(Core.instance){
							public void run(){
								damaged.getWorld().spawnParticle(Particle.BLOCK_CRACK, damaged.getLocation().add(0,.6,0), 40, .3, .5, .3, 0, Material.REDSTONE_BLOCK.createBlockData());
								Random r = new Random();
								for(int i = 0;i < r.nextInt(4)+3;i++){
									ItemStack stack = new ItemStack(Material.REDSTONE);
									ItemMeta meta = stack.getItemMeta();
									meta.setDisplayName(String.valueOf(r.nextInt()));
									stack.setItemMeta(meta);
									final Item item = damaged.getWorld().dropItem(damaged.getLocation().add(0,.6,0), stack);
									item.setPickupDelay(Integer.MAX_VALUE);
									new BukkitTask(Core.instance){
										public void run(){
											item.remove();
										}

										@Override
										public void onCancel() {
										}
									}.runTaskLater(11);
								}
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(2);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onHopperPickUp(InventoryPickupItemEvent event){
		ItemStack item = event.getItem().getItemStack();
		if(item.hasItemMeta()){
			ItemMeta meta = item.getItemMeta();
			if(meta.hasDisplayName()){
				if(Utils.isInteger(meta.getDisplayName())){
					event.setCancelled(true);
				}
			}
		}
	}

}
