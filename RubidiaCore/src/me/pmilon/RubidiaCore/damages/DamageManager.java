package me.pmilon.RubidiaCore.damages;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.abilities.RAbility;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.WeaponUse;
import me.pmilon.RubidiaCore.ritems.weapons.Piercing.PiercingType;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.raids.Raid;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class DamageManager {

	public static double getDamageResistance(RDamageCause cause, LivingEntity entity, boolean average){
		Player player = entity instanceof Player ? (Player)entity : null;
		EntityEquipment equipment = entity.getEquipment();
		double defensePoints = 10;
		double factor = 1.0;
		
	    for(ItemStack item : equipment.getArmorContents()){
	    	if(item != null){
	    		defensePoints += DamageManager.getDefensePoint(item, cause, average);
	    	}
	    }
	    
	    if(equipment.getItemInOffHand() != null){
	    	if(equipment.getItemInOffHand().getType().equals(Material.SHIELD)){
	    		double points = DamageManager.getDefensePoint(equipment.getItemInOffHand(), cause, average);
	    		if(player != null)points *= player.isBlocking() ? 1 : .5;
	    		defensePoints += points;
	    	}
	    }
	    
	    if(equipment.getItemInMainHand() != null){
	    	if(equipment.getItemInMainHand().getType().equals(Material.SHIELD)){
	    		double points = DamageManager.getDefensePoint(equipment.getItemInMainHand(), cause, average);
	    		if(player != null)points *= player.isBlocking() ? 1 : .5;
	    		defensePoints += points;
	    	}
	    }
	    
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			factor += rp.getDefenseFactor();
			defensePoints += rp.getBonus(PiercingType.DEFENSE);
			
			if(rp.getRClass().equals(RClass.MAGE)) {
				if(cause.equals(RDamageCause.MAGIC)
						|| cause.equals(RDamageCause.ABILITY)) {
					factor += RAbility.MAGE_4.getDamages(rp)*.01;
				}
			}
		}
		
		if(entity.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
			for(PotionEffect effect : entity.getActivePotionEffects()){
				if(effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)){
					factor += (effect.getAmplifier()+1)*Settings.POTIONEFFECT_DAMAGES_FACTOR;
					break;
				}
			}
		}
		
	    return defensePoints*factor;
	}
	
	public static double getDefensePoint(ItemStack item, RDamageCause cause, boolean average){
		double factor = 1.0;
    	RItem rItem = new RItem(item);
    	if(rItem.isWeapon()){
	    	Weapon weapon = rItem.getWeapon();
	    	if(weapon != null){
	    		double points = RandomUtils.random.nextInt(weapon.getMaxDamages()-weapon.getMinDamages())+weapon.getMinDamages()+RandomUtils.random.nextDouble();
	    		if(average)points = (weapon.getMaxDamages()+weapon.getMinDamages())*.5;
	    		
				Map<Enchantment, Integer> enchants = item.getEnchantments();
			    for(Enchantment enchant : enchants.keySet()){
			    	if(enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL)){
			    		factor += enchants.get(enchant)*Settings.ENCHANTMENT_PROTECTION_FACTOR;
			    	}else{
				    	switch(cause){
							case EXPLOSION:
								if(enchant.equals(Enchantment.PROTECTION_EXPLOSIONS)){
						    		factor += enchants.get(enchant)*Settings.ENCHANTMENT_PROTECTION_FACTOR;
							    }
								break;
							case FALL:
								if(enchant.equals(Enchantment.PROTECTION_FALL)){
						    		factor += enchants.get(enchant)*Settings.ENCHANTMENT_PROTECTION_FACTOR;
						    	}
								break;
							case FIRE:
								if(enchant.equals(Enchantment.PROTECTION_FIRE)){
						    		factor += enchants.get(enchant)*Settings.ENCHANTMENT_PROTECTION_FACTOR;
						    	}
								break;
							case RANGE:
								if(enchant.equals(Enchantment.PROTECTION_PROJECTILE)){
						    		factor += enchants.get(enchant)*Settings.ENCHANTMENT_PROTECTION_FACTOR;
								}
								break;
							default:
								break;
				    	}
			    	}
			    }
			    
		    	return points*factor;
	    	}
    	}
    	return 0;
	}
	
	public static boolean damage(LivingEntity damaged, LivingEntity damager, double damages, RDamageCause cause){
		if(!DamageManager.canDamage(damager, damaged, cause)){
			return false;
		}

		RPlayer rp = null;
		if(damager instanceof Player){
			Player player = (Player)damager;
			rp = RPlayer.get(player);
			if(cause.equals(RDamageCause.ABILITY)){
				damages *= rp.getAbilityDamagesFactor();
			}
			rp.setLastCombat(System.currentTimeMillis());
		}
		if(damaged instanceof Player){
			Player pplayer = (Player)damaged;
			RPlayer rpp = RPlayer.get(pplayer);
			if(cause.equals(RDamageCause.ABILITY)){
				damages *= 1-rpp.getAbilityDefenseFactor();
			}
			rpp.setLastCombat(System.currentTimeMillis());
		}
		
		if(!cause.equals(RDamageCause.ABILITY)){
			damages *= 10.0/getDamageResistance(cause, damaged, false);
		}
		
		if(damages < .5)damages = .5;

		RubidiaEntityDamageEvent e = new RubidiaEntityDamageEvent(damaged, damager, null, damages, cause);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled()){
			damaged.damage(0.0, e.getDamager());
			damaged.damage(e.getDamages());
			return true;
		}
		
		return false;
	}

	public static void damageEvent(EntityDamageByEntityEvent event, double damages, RDamageCause cause, boolean critical){
		LivingEntity damaged = (LivingEntity) event.getEntity();
		Projectile projectile = event.getDamager() instanceof Projectile ? (Projectile)event.getDamager() : null;
		LivingEntity damager = (LivingEntity) (projectile != null ? projectile.getShooter() : event.getDamager());
		if(!DamageManager.canDamage(damager, damaged, cause)){
			if(projectile != null)projectile.remove();
			return;
		}
		
		RPlayer rp = null;
		if(damager instanceof Player){
			Player player = (Player)damager;
			rp = RPlayer.get(player);
			if(cause.equals(RDamageCause.ABILITY)){
				damages *= rp.getAbilityDamagesFactor();
			}
			rp.setLastCombat(System.currentTimeMillis());
		}
		if(damaged instanceof Player){
			Player pplayer = (Player)damaged;
			RPlayer rpp = RPlayer.get(pplayer);
			if(cause.equals(RDamageCause.ABILITY)){
				damages *= 1-rpp.getAbilityDefenseFactor();
			}
			rpp.setLastCombat(System.currentTimeMillis());
		}
		
		Monster monster = Monsters.entities.get(damager);
		if(critical || monster != null){
			if(RandomUtils.random.nextInt(100) < 20+(rp != null ? rp.getCriticalStrikeChanceFactor()*100 : -10)){
				damages *= rp != null ? rp.getCriticalStrikeDamagesFactor() : 2.0;
				damaged.getWorld().spawnParticle(Particle.CRIT, damaged.getLocation().add(0,damaged.getHeight(),0), 44, damaged.getWidth(), .4, damaged.getWidth(), 0);
			}
		}
		
		if(!cause.equals(RDamageCause.ABILITY)){
			damages *= 10.0/getDamageResistance(cause, damaged, false);
		}
		
		if(damages < .5)damages = .5;
		
		RubidiaEntityDamageEvent e = new RubidiaEntityDamageEvent(damaged, damager, projectile, damages, cause);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled()){
			event.setCancelled(false);
			event.setDamage(0.0);
			event.setDamage(DamageModifier.BASE, e.getDamages());
		}
	}
	
	public static boolean canDamage(Entity damager, LivingEntity damaged, RDamageCause cause){
		RPlayer rpDamaged = damaged instanceof Player ? RPlayer.get((Player)damaged) : null;
		RPlayer rpDamager = damager instanceof Player ? RPlayer.get((Player)damager) : null;
		
		if(damager.equals(damaged))return false;
		
		if(damaged instanceof Player){
			Player defender = (Player)damaged;
			GMember mtarget = GMember.get(defender);
			
			if(damager instanceof Player){
				Player attacker = (Player)damager;
				if(!rpDamager.isInDuel() || !rpDamager.getDuelOpponent().equals(rpDamaged)){
					GMember member = GMember.get(attacker);
					
					if(!member.hasGuild() || !mtarget.hasGuild()){
						attacker.spawnParticle(Particle.BARRIER, defender.getLocation().add(0,1,0), 1, .25, .75, .25);
						rpDamager.sendActionBar("§4§lHey! §cYou can only attack players during a raid or a duel!", "§4§lHey ! §cVous ne pouvez attaquer des joueurs que durant une offensive ou un duel !");
						return false;
					} else if(member.getGuild().equals(mtarget.getGuild())) {
						rpDamager.sendActionBar("§4§lHey ! §cVous ne pouvez attaquer des joueurs de votre propre guilde !");
						return false;
					} else if(mtarget.getGuild().isRaiding()){
						Raid raid = mtarget.getGuild().getCurrentRaid();
						Relation offRelation = member.getGuild().getRelationTo(raid.getOffensive());
						Relation defRelation = member.getGuild().getRelationTo(raid.getDefensive());
						if(!raid.isStarted()){
							attacker.spawnParticle(Particle.BARRIER, defender.getLocation().add(0,1,0), 1, .25, .75, .25);
							if(mtarget.getGuild().equals(raid.getDefensive()) && (offRelation.equals(Relation.ALLY) || offRelation.equals(Relation.MEMBER)) || mtarget.getGuild().equals(raid.getOffensive()) && (defRelation.equals(Relation.ALLY) || defRelation.equals(Relation.MEMBER))){
								rpDamager.sendActionBar("§4§lHey! §cRaid against guild §4§l" + mtarget.getGuild().getName() + " §chas not yet started!", "§4§lHey ! §cL'offensive contre la guilde §4§l" + mtarget.getGuild().getName() + " §cn'a pas encore débuté !");
								return false;
							}else{
								rpDamager.sendActionBar("§4§lHey! §cYou can only attack players during a raid!", "§4§lHey ! §cVous ne pouvez attaquer des joueurs que durant une offensive !");
								return false;
							}
						}else if(mtarget.getGuild().equals(raid.getDefensive()) && !offRelation.equals(Relation.ALLY) && !offRelation.equals(Relation.MEMBER) || mtarget.getGuild().equals(raid.getOffensive()) && !defRelation.equals(Relation.ALLY) && !defRelation.equals(Relation.MEMBER)){
							rpDamager.sendActionBar("§4§lHey! §cYou can only help if you're a member or an ally!", "§4§lHey ! §cVous ne pouvez aider que si vous êtes membre ou allié !");
							return false;
						}
					} else {
						attacker.spawnParticle(Particle.BARRIER, defender.getLocation().add(0,1,0), 1, .25, .75, .25);
						rpDamager.sendActionBar("§4§lHey! §cYou can only attack players during a raid!", "§4§lHey ! §cVous ne pouvez attaquer des joueurs que durant une offensive !");
						return false;
					}
				}
			}
		}else if(Pet.isPet(damaged)){
			Pet pet = Pets.get(damaged);
			if(pet != null)return canDamage(damager, pet.getOwner(), RDamageCause.ABILITY/*to avoid below block chance*/);
		}
		
		if(!cause.equals(RDamageCause.ABILITY)){
			if(RandomUtils.random.nextInt(1000) < (rpDamaged != null ? rpDamaged.getBlockChanceFactor()*1000 : 25)){
				if(rpDamager != null){
					if(rpDamaged == null)rpDamager.sendActionBar("§cTarget has blocked your attack!", "§cLa cible a bloqué votre attaque !");
					else rpDamager.sendActionBar("§c" + rpDamaged.getName() + " blocked your attack!", "§c" + rpDamaged.getName() + " a bloqué votre attaque !");
				}
				if(rpDamaged != null){
					if(rpDamager == null)rpDamaged.sendActionBar("§aYou blocked the attack!", "§aVous avez bloqué l'attaque !");
					else rpDamaged.sendActionBar("§aYou blocked " + rpDamager.getName() + "'s attack!", "§aVous avez bloqué l'attaque de " + rpDamager.getName() + " !");
				}
				damaged.setNoDamageTicks(12);
				damaged.getWorld().spawnParticle(Particle.SLIME, damaged.getLocation().add(0,damaged.getHeight()/2.,0), 50, damaged.getWidth(), .3, damaged.getWidth(), 0);
				damaged.getWorld().playSound(damaged.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 2);
				return false;
			}
		}
		return true;
	}
	
	public static double getDamages(LivingEntity damager, LivingEntity damaged, ItemStack item, RDamageCause cause, boolean critical, boolean average){
		double damages = 0.0;
		double factor = 1.0;
		Player player = damager instanceof Player ? (Player)damager : null;
		RPlayer rp = player != null ? RPlayer.get(player) : null;
		Monster monster = Monsters.entities.get(damager);
		boolean handAttack = false;
		if(monster == null || monster.getDamagesFactor() == 0){
			if(!Pet.isPet(damager)){
				if(item != null){
			    	RItem rItem = new RItem(item);
			    	if(rItem.isWeapon()){
						Weapon weapon = rItem.getWeapon();
						if(weapon != null){
							if(weapon.isAttack()){
								damages = (RandomUtils.random.nextInt(weapon.getMaxDamages()-weapon.getMinDamages())+weapon.getMinDamages()+RandomUtils.random.nextDouble());
								if(average)damages = (weapon.getMaxDamages()+weapon.getMinDamages())*.5;
								if(!weapon.getWeaponUse().toString().contains(cause.toString())){
									damages = 0;
								}
								
								Map<Enchantment, Integer> enchants = item.getEnchantments();
								if(cause.equals(RDamageCause.MELEE)) {
									if(weapon.getWeaponUse().equals(WeaponUse.MELEE_RANGE)){
										if(rp != null)damages *= rp.getStrength()*Settings.STRENGTH_FACTOR_MELEE_RANGE_DAMAGES_ON_MELEE;
									}else if(weapon.getWeaponUse().equals(WeaponUse.MELEE)){
										if(rp != null)factor += rp.getStrength()*Settings.STRENGTH_FACTOR_MELEE_DAMAGES_ON_MELEE;
										if(damaged != null){
											for(Enchantment enchant : enchants.keySet()){
												if(enchant.equals(Enchantment.DAMAGE_ARTHROPODS) && (damaged.getType().equals(EntityType.SPIDER) || damaged.getType().equals(EntityType.CAVE_SPIDER) || damaged.getType().equals(EntityType.SILVERFISH))){
													factor += enchants.get(enchant)*Settings.ENCHANTMENT_SPECIFIC_DAMAGES_FACTOR;
												}else if(enchant.equals(Enchantment.DAMAGE_UNDEAD) && (damaged.getType().equals(EntityType.ZOMBIE) || damaged.getType().equals(EntityType.PIG_ZOMBIE) || damaged.getType().equals(EntityType.SKELETON) || damaged.getType().equals(EntityType.WITHER))){
													factor += enchants.get(enchant)*Settings.ENCHANTMENT_SPECIFIC_DAMAGES_FACTOR;
												}
											}
										}
									}else if(rp != null)damages = rp.getStrength()*Settings.STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE;
									
									for(Enchantment enchant : enchants.keySet()){
										if(enchant.equals(Enchantment.DAMAGE_ALL)){
											factor += enchants.get(enchant)*Settings.ENCHANTMENT_DAMAGE_ALL_FACTOR;
										}
									}
								} else if(cause.equals(RDamageCause.MAGIC)) {
									if(rp != null)factor += rp.getIntelligence()*Settings.INTELLIGENCE_FACTOR_MAGIC_DAMAGES_ON_MAGIC;

									for(Enchantment enchant : enchants.keySet()){
										if(enchant.equals(Enchantment.DAMAGE_ALL)){
											factor += enchants.get(enchant)*Settings.ENCHANTMENT_DAMAGE_ALL_FACTOR;
										}else if(enchant.equals(Enchantment.ARROW_DAMAGE)){
											factor += enchants.get(enchant)*Settings.ENCHANTMENT_POWER_FACTOR;
										}else if(damaged != null){
											if(enchant.equals(Enchantment.DAMAGE_ARTHROPODS) && (damaged.getType().equals(EntityType.SPIDER) || damaged.getType().equals(EntityType.CAVE_SPIDER) || damaged.getType().equals(EntityType.SILVERFISH))){
												factor += enchants.get(enchant)*Settings.ENCHANTMENT_SPECIFIC_DAMAGES_FACTOR;
											}else if(enchant.equals(Enchantment.DAMAGE_UNDEAD) && (damaged.getType().equals(EntityType.ZOMBIE) || damaged.getType().equals(EntityType.PIG_ZOMBIE) || damaged.getType().equals(EntityType.SKELETON) || damaged.getType().equals(EntityType.WITHER))){
												factor += enchants.get(enchant)*Settings.ENCHANTMENT_SPECIFIC_DAMAGES_FACTOR;
											}
										}
									}
								} else if(cause.equals(RDamageCause.RANGE)) {
									if(rp != null) {
										factor += rp.getAgility()*Settings.AGILITY_FACTOR_RANGE_DAMAGES_ON_RANGE;
									}
									
									for(Enchantment enchant : enchants.keySet()){
										if(enchant.equals(Enchantment.ARROW_DAMAGE)){
											factor += enchants.get(enchant)*Settings.ENCHANTMENT_POWER_FACTOR;
										}
									}
								}
							}else handAttack = true;
						}else handAttack = true;
			    	}else handAttack = true;
				}else handAttack = true;
			}else{
				Pet pet = Pets.get(damager);
				if(pet != null)damages = pet.getDamages();
			}
		}else damages = monster.getDamages();
		
		if(rp != null){
			if(handAttack && cause.equals(RDamageCause.MELEE)){
				damages = rp.getStrength()*Settings.STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE;
			}
			damages += rp.getBonus(PiercingType.ATTACK);
			damages *= rp.getNextAttackFactor();//needs to be after all sums
		}
		
		if(damaged instanceof Player) {
			RPlayer rpp = RPlayer.get((Player) damaged);
			if(rpp.getRClass().equals(RClass.PALADIN)) {
				if(rpp.isActiveAbility(RAbility.PALADIN_5)) {
					factor += RAbility.PALADIN_5.getDamages(rpp)*.01 - 1;
				}
			}
		}
		
		if(damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
			for(PotionEffect effect : damager.getActivePotionEffects()){
				if(effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)){
					factor += (effect.getAmplifier()+1)*Settings.POTIONEFFECT_DAMAGES_FACTOR;
					break;
				}
			}
		}
		
		if(cause.equals(RDamageCause.MELEE))factor += rp != null ? rp.getAdditionalFactor(BuffType.MELEE_DAMAGE) : Set.getAdditionalFactor(damager, BuffType.MELEE_DAMAGE);
		if(cause.equals(RDamageCause.RANGE))factor += rp != null ? rp.getAdditionalFactor(BuffType.RANGED_DAMAGE) : Set.getAdditionalFactor(damager, BuffType.RANGED_DAMAGE);
		if(cause.equals(RDamageCause.MAGIC))factor += rp != null ? rp.getAdditionalFactor(BuffType.MAGIC_DAMAGE) : Set.getAdditionalFactor(damager, BuffType.MAGIC_DAMAGE);
		
		return damages*factor;
	}
	
	public static List<LivingEntity> toDamageableEntityList(List<? extends Entity> entities) {
		Predicate<Entity> predicate = (Entity entity) ->
			entity instanceof LivingEntity
			&& !(entity instanceof ArmorStand)
			&& (!(entity instanceof Villager) || !PNJManager.isPNJ((Villager) entity));
		
		return entities.stream().filter(predicate).map((Entity entity) -> (LivingEntity) entity).collect(Collectors.toList());
	}
}
