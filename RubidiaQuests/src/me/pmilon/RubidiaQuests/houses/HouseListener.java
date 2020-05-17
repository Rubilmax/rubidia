package me.pmilon.RubidiaQuests.houses;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;

public class HouseListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Block block = e.getBlock();
			if(block != null){
				House house = House.get(block);
				if(house != null){
					if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
						RPlayer owner = house.getOwner();
						e.setCancelled(true);
						rp.sendActionBar("§4§lHey ! §cVous ne pouvez pas construire chez " + owner.getName() + " !");
					} else e.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Block block = e.getBlock();
			if(block != null){
				House house = House.get(block);
				if(house != null){
					if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
						RPlayer owner = house.getOwner();
						e.setCancelled(true);
						rp.sendActionBar("§4§lHey ! §cVous ne pouvez pas construire chez " + owner.getName() + " !");
					} else e.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockChange(EntityChangeBlockEvent e){
		Block block = e.getBlock();
		Entity changer = e.getEntity();
		if(block != null){
			if(changer instanceof Player){
				Player player = (Player)changer;
				RPlayer rp = RPlayer.get(player);
				House house = House.get(block);
				if(house != null && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
					e.setCancelled(true);
				} else e.setCancelled(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityInteract(EntityInteractEvent e){
		Player player = null;
		
		Entity entity = e.getEntity();
		if(entity instanceof Creature){
			Pet pet = Pets.get(entity);
			if(pet != null){
				player = pet.getOwner();
			}
		}
		
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Block block = e.getBlock();
			if(block != null){
				House house = House.get(block);
				if(house != null){
					if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
						RPlayer owner = house.getOwner();
						e.setCancelled(true);
						rp.sendActionBar("§4§lHey ! §cVous ne pouvez pas utiliser le matériel de " + owner.getName() + " !");
					} else e.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Block block = e.getClickedBlock();
			if(block != null){
				House house = House.get(block);
				if(house != null){
					if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
						RPlayer owner = house.getOwner();
						e.setCancelled(true);
						rp.sendActionBar("§4§lHey ! §cVous ne pouvez pas utiliser le matériel de " + owner.getName() + " !");
					} else e.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Entity entity = e.getRightClicked();
			if(entity != null){
				if(entity instanceof ArmorStand || entity instanceof Minecart || entity instanceof Hanging){
					House house = House.get(entity.getLocation().getBlock());
					if(house != null){
						if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
							RPlayer owner = house.getOwner();
							e.setCancelled(true);
							rp.sendActionBar("§4§lHey ! §cVous ne pouvez pas utiliser le matériel de " + owner.getName() + " !");
						} else e.setCancelled(false);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(RubidiaEntityDamageEvent event){
		if(!event.isCancelled()){
			LivingEntity damaged = (LivingEntity) event.getEntity();
			House house = House.get(damaged.getLocation().getBlock());
			if(house != null){
				LivingEntity damager = event.getDamager();
				if(damager instanceof Player){
					Player p = (Player)damager;
					RPlayer rp = RPlayer.get(p);
					if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
						Monster monster = Monsters.get(damaged);
						if(monster != null){
							if(monster.getTamer() != null){
								if(!monster.getTamer().equals(p)){
									RPlayer owner = house.getOwner();
									event.setCancelled(true);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez infliger de dommages à une créature apprivoisée par §e" + owner.getName() + "§c !");
								} else event.setCancelled(false);
							}
						}
					}
				}
			}
		}
	}
	
	/*@EventHandler(priority = EventPriority.MONITOR)
	public void onBucketEmpty(PlayerBucketEmptyEvent event){
		Player player = event.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Block block = event.getBlockClicked().getRelative(event.getBlockFace());
			if(block != null){
				House house = House.get(block);
				if(house != null){
					if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
						RPlayer owner = house.getOwner();
						event.setCancelled(true);
						Utils.updateInventory(player);
						rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire chez §e" + owner.getName() + " §c!");
					} else event.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBucketFill(PlayerBucketFillEvent event){
		Player player = event.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Block block = event.getBlockClicked().getRelative(event.getBlockFace());
			if(block != null){
				House house = House.get(block);
				if(house != null){
					if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
						RPlayer owner = house.getOwner();
						event.setCancelled(true);
						Utils.updateInventory(player);
						rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire chez §e" + owner.getName() + " §c!");
					} else event.setCancelled(false);
				}
			}
		}
	}*/

	@EventHandler(priority = EventPriority.MONITOR)
	public void onHangingBreak(HangingBreakByEntityEvent event){
		Entity entity = event.getEntity();
		House house = House.get(entity.getLocation().getBlock());
		if(house != null){
			Player player = null;
			Entity remover = event.getRemover();
			if(remover instanceof Player){
				player = (Player)remover;
			}else if(remover instanceof Projectile){
				ProjectileSource source = ((Projectile)remover).getShooter();
				if(source instanceof Player){
					player = (Player)source;
				}
			}
			
			if(player != null){
				RPlayer rp = RPlayer.get(player);
				if(!rp.isOp() && !rp.getUniqueId().equals(house.getOwnerUniqueId())){
					RPlayer owner = house.getOwner();
					event.setCancelled(true);
					Utils.updateInventory(player);
					rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire chez §e" + owner.getName() + " §c!");
				} else event.setCancelled(false);
			}
		}
	}
	
}
