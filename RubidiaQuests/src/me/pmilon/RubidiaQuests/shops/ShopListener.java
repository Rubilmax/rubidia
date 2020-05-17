package me.pmilon.RubidiaQuests.shops;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.projectiles.ProjectileSource;

public class ShopListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e){
		if(e.getHand() != null){
			if(e.getHand().equals(EquipmentSlot.HAND)){
				Entity entity = e.getRightClicked();
				if(entity instanceof Player){
					Player keeper = (Player)entity;
					RPlayer rpk = RPlayer.get(keeper);
					PlayerShop shop = rpk.getShop();
					if(shop != null){
						if(shop.isStart()){
							Player player = e.getPlayer();
							shop.open(player);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onAttack(EntityDamageByEntityEvent e){
		Entity damaged = e.getEntity();
		if(damaged instanceof Player){
			Player player = (Player)damaged;
			RPlayer rp = RPlayer.get(player);
			PlayerShop shop = rp.getShop();
			if(shop != null){
				if(shop.isStart()){
					e.setCancelled(true);
					Entity damager = e.getDamager();
					Player player2 = null;
					if(damager instanceof Player){
						player2 = (Player)damager;
					}else if(damager instanceof Projectile){
						ProjectileSource source = ((Projectile)damager).getShooter();
						if(source instanceof Player){
							player2 = (Player)source;
						}
					}
					if(player2 != null){
						RPlayer rp2 = RPlayer.get(player2);
						rp2.sendActionBar("§4§lHey! §cYou cannot damage a shopkeeper!", "§4§lHey ! §cVous ne pouvez infliger de dégâts à un marchand !");
					}
				}
			}
		}
	}
	
}
