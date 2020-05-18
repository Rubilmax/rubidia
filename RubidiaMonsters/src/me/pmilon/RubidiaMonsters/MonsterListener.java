package me.pmilon.RubidiaMonsters;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class MonsterListener implements Listener {

	private final RubidiaMonstersPlugin plugin;
	public MonsterListener(RubidiaMonstersPlugin plugin){
		this.plugin = plugin;
	}
	
	public RubidiaMonstersPlugin getPlugin() {
		return plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDeath(EntityDeathEvent event){
		LivingEntity entity = event.getEntity();
		if(!(entity instanceof ArmorStand) && !(entity instanceof Player)){
			Monster monster = Monsters.get(entity);
			if(monster != null){
				event.setDroppedExp(0);
				monster.kill(false);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(RubidiaEntityDamageEvent event){
		LivingEntity entity = (LivingEntity) event.getEntity();
		if(entity instanceof Creature){
			Creature creature = (Creature)entity;
			LivingEntity damager = event.getDamager();
			/*if(creature.getTarget() == null){
				creature.setTarget(damager);
			}else if(creature.getTarget().isDead() || !creature.getTarget().isValid()){
				creature.setTarget(damager);
			}else if(creature.getTarget() instanceof Player){
				if(!((Player)creature.getTarget()).getGameMode().equals(GameMode.SURVIVAL)){
				}
			}else if(creature.getWorld().equals(creature.getTarget().getWorld())){
				if(creature.getTarget().getLocation().distanceSquared(creature.getLocation()) > 441){
					creature.setTarget(damager);
				}
			}else creature.setTarget(damager);*/
			creature.setTarget(damager);
		}
	}
	
	/*@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		final Chunk chunk = event.getChunk();
		for(Entity entity : chunk.getEntities()){
			if(entity instanceof LivingEntity){
				Monster monster = Monsters.get((LivingEntity)entity);
				if(monster != null){
					monster.kill(true);
				}
			}
		}
	}*/

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event){
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				Player player = event.getPlayer();
				Entity ent = event.getRightClicked();
				if(ent instanceof LivingEntity){
					LivingEntity entity = (LivingEntity)ent;
					Monster monster = Monsters.get(entity);
					if(monster != null){
						if(monster.getTamer() == null){
							ItemStack item = player.getEquipment().getItemInMainHand();
							if(item.getType().equals(Material.LEAD)){
								event.setCancelled(true);
								monster.setTamer(player);
								if(!RPlayer.get(player).isOp()){
									item.setAmount(item.getAmount()-1);
									if(item.getAmount() <= 0)player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR,1));
									else player.getEquipment().setItemInMainHand(item);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onLeashBreak(EntityUnleashEvent event){
		Entity ent = event.getEntity();
		if(ent instanceof LivingEntity){
			LivingEntity entity = (LivingEntity)ent;
			Monster monster = Monsters.get(entity);
			if(monster != null){
				monster.setTamer(null);
			}
		}
	}
}
