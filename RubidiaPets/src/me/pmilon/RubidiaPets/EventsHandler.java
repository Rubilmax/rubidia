package me.pmilon.RubidiaPets;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerXPEvent;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.events.MonsterKillEvent;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaPets.pets.RItemPearls;
import me.pmilon.RubidiaPets.ui.PetUI;
import me.pmilon.RubidiaPets.utils.Settings;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class EventsHandler implements Listener {

	@EventHandler
	public void onPetTargetPet(EntityTargetLivingEntityEvent event){//natural targetting cancelled
		Entity targetter = event.getEntity();
		Entity target = event.getTarget();
		if(targetter != null && Pet.isPet(targetter) || target != null && Pet.isPet(target)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public static void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if (player != null) {
			RPlayer rp = RPlayer.get(player);
			for(Pet pet : rp.getPets()){
				if(pet.isActive()){
					pet.spawn(rp.getPlayer());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player p = event.getPlayer();
		RPlayer rp = RPlayer.get(p);
		for(Pet pet : rp.getPets()){
			if(pet.isActive()){
				pet.despawn();
			}
		}
	}

	@EventHandler
	public void onPlayerClickPet(PlayerInteractEntityEvent e){
		if(e.getHand() != null){
			if(e.getHand().equals(EquipmentSlot.HAND)){
				Entity entity = e.getRightClicked();
				if(entity instanceof LivingEntity){
					final Pet pet = Pets.get(entity);
					if(pet != null){
						Player player = e.getPlayer();
						RPlayer rp = RPlayer.get(player);
						if(rp.getPets().contains(pet) || rp.isOp()){
							if(player.isSneaking()){
								e.setCancelled(true);
								Core.uiManager.requestUI(new PetUI(e.getPlayer(), pet));
							}else{
								ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
								if(item != null){
									if(item.isSimilar(RItemPearls.PET_FOOD.getItemStack())){
										if(pet.canBeFood()){
											pet.setCanBeFood(false);
											new BukkitTask(PetsPlugin.instance){

												@Override
												public void run() {
													pet.setCanBeFood(true);
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskLater(4);
											pet.addExp(1.06,true);
											pet.heal(Settings.HEALTH_PER_FOOD);
											item.setAmount(item.getAmount()-1);
											if(item.getAmount() < 1)e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
										}
									}else if(item.getType().equals(Material.SHEARS)){
										if(entity instanceof Snowman){
											e.setCancelled(true);
											rp.sendMessage("§cEnlevez-lui sa citrouille en changeant son apparence !");
										}
									}else if(item.getType().equals(Material.SADDLE)){
										if(entity instanceof Pig){
											e.setCancelled(true);
											rp.sendMessage("§cEquipez-le d'une selle en changeant son apparence !");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onDamage(RubidiaEntityDamageEvent event){
		LivingEntity damager = event.getDamager();
		LivingEntity entity = (LivingEntity) event.getEntity();
		if(damager instanceof Player){
			Player player = (Player)damager;
			RPlayer rp = RPlayer.get(player);
			for(Pet pet : rp.getPets()){
				if(pet.isActive()){
					if(pet.getEntity() != null){
						if(pet.getEntity().getHealth() > 1.0){
							pet.setTarget(entity);
						}
					}
				}
			}
		}else{
			if(entity instanceof Player){
				Player player = (Player)entity;
				RPlayer rp = RPlayer.get(player);
				for(Pet pet : rp.getPets()){
					if(pet.isActive()){
						if(pet.getEntity() != null){
							if(pet.getEntity().getHealth() > 1.0){
								pet.setTarget(damager);
							}
						}
					}
				}
			}
		}
		
		if(Pet.isPet(entity)){
			final Pet pet = Pets.get(entity);
			if(pet != null){
				if(entity.getHealth()-event.getDamages() <= 1.0){
					event.setDamages(0.0);
					entity.setHealth(1.0);
					
					Monster monster = Monsters.get(damager);
					if (monster != null) {
						monster.setTarget(pet.getOwner());
					}
				}
				
				new BukkitTask(PetsPlugin.instance){
					
					@Override
					public void run() {
						pet.updateHealth();
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}
		}
	}

	@EventHandler
	public void onEntityCombust(EntityCombustEvent event){
		Entity ent = event.getEntity();
		if(Pet.isPet(ent)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onXP(RPlayerXPEvent event){
		RPlayer rp = event.getRPlayer();
		for(Pet pet : rp.getPets()){
			if(pet.isActive()){
				pet.addExp(event.getXP()*.1,false);
			}
		}
	}
	
	@EventHandler
	public void onFall(EntityDamageEvent event){
		if(Pet.isPet(event.getEntity()) && !event.getCause().equals(DamageCause.ENTITY_ATTACK) && !event.getCause().equals(DamageCause.PROJECTILE) && !event.getCause().equals(DamageCause.MAGIC)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMonsterKill(MonsterKillEvent event) {
		Player player = event.getKiller();
		RPlayer rp = RPlayer.get(player);
		if (!rp.getPets().isEmpty()) {
			for (Pet pet : rp.getPets()) {
				if (pet.isActive()) {
					event.getKillers().put(pet.getName(), event.getMonster().getXP(player.getLevel()) * Settings.PET_EXPERIENCE_FACTOR);
				}
			}
		}
	}
}