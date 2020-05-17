package me.pmilon.RubidiaGuilds.claims;

import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.utils.Settings;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ClaimsListener implements Listener {

	private GuildsPlugin plugin;
	public ClaimsListener(GuildsPlugin guildsPlugin) {
		this.plugin = guildsPlugin;
		Bukkit.getPluginManager().registerEvents(this, guildsPlugin);
	}
	
	protected GuildsPlugin getPlugin(){
		return this.plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			GMember member = GMember.get(player);
			Block block = e.getBlock();
			if(block != null){
				Claim claim = Claim.get(block.getLocation());
				if(claim != null){
					Guild guild = claim.getGuild();
					if(!rp.isOp()){
						if(guild != null){
							if(guild.isActive()){
								if(member.hasGuild()){
									if(member.getGuild().equals(guild)){
										if(!member.getPermission(Permission.BUILD)){//Permissions ONLY applied in guild territory.
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de votre guilde !");
										}
										return;
									}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
										if(!guild.getDefaultRank().getPermission(Permission.BUILD)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de vos alliés !");
										}
										return;
									}
								}

								if(!guild.isClaimBuildable()){
									e.setCancelled(true);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire sur le territoire de §e" + guild.getName() + " §c!");
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			GMember member = GMember.get(player);
			Block block = e.getBlock();
			if(block != null){
				Claim claim = Claim.get(block.getLocation());
				if(claim != null){
					Guild guild = claim.getGuild();
					if(!rp.isOp()){
						if(guild != null){
							if(guild.isActive()){
								if(member.hasGuild()){
									if(member.getGuild().equals(guild)){
										if(!member.getPermission(Permission.BUILD)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de votre guilde !");
										}
										return;
									}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
										if(!guild.getDefaultRank().getPermission(Permission.BUILD)){//Permissions ONLY applied in guild territory.
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de vos alliés !");
										}
										return;
									}
								}
								
								if(!guild.isClaimBuildable()){
									e.setCancelled(true);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire sur le territoire de §e" + guild.getName() + " §c!");
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockChange(EntityChangeBlockEvent e){
		Block block = e.getBlock();
		Entity changer = e.getEntity();
		if(block != null){
			if(changer instanceof Player){
				Player player = (Player)changer;
				GMember member = GMember.get(player);
				Claim claim = Claim.get(block.getLocation());
				if(claim != null){
					Guild guild = claim.getGuild();
					if(guild != null){
						if(guild.isActive()){
							if(!member.hasGuild() || member.getGuild().equals(guild) || !member.getGuild().getRelationTo(guild).equals(Relation.ENEMY)){
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
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
			GMember member = GMember.get(player);
			Block block = e.getBlock();
			if(block != null){
				Claim claim = Claim.get(block.getLocation());
				if(claim != null){
					Guild guild = claim.getGuild();
					if(!rp.isOp()){
						if(guild != null){
							if(guild.isActive()){
								String type = block.getType().toString();
								if(member.hasGuild()){
									if(member.getGuild().equals(guild)){
										if((type.contains("DOOR") || type.contains("GATE") || type.contains("PLATE") || type.contains("BUTTON")) && !member.getPermission(Permission.USE_DOORS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'intéragir dans le territoire de votre guilde !");
										}else if(Settings.INTERACT_BLOCKS.contains(block.getType()) && !member.getPermission(Permission.USE_CHESTS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'utiliser le matériel de votre guilde !");
										}
										return;
									}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
										if((type.contains("DOOR") || type.contains("GATE") || type.contains("PLATE") || type.contains("BUTTON")) && !guild.getDefaultRank().getPermission(Permission.USE_DOORS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'intéragir dans le territoire de vos alliés !");
										}else if(Settings.INTERACT_BLOCKS.contains(block.getType()) && !guild.getDefaultRank().getPermission(Permission.USE_CHESTS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'utiliser le matériel de vos alliés !");
										}
										return;
									}
								}
								
								if((type.contains("DOOR") || type.contains("GATE") || type.contains("PLATE") || type.contains("BUTTON")) && !guild.isClaimDoorsUsable()){
									e.setCancelled(true);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez intéragir dans le territoire de la guilde §e" + guild.getName() + " §c!");
								}else if(Settings.INTERACT_BLOCKS.contains(block.getType()) && !guild.isClaimChestsUsable()){
									e.setCancelled(true);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez utiliser le matériel de la guilde §e" + guild.getName() + " §c!");
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			GMember member = GMember.get(player);
			Block block = e.getClickedBlock();
			if(block != null){
				Claim claim = Claim.get(block.getLocation());
				if(claim != null){
					Guild guild = claim.getGuild();
					if(!rp.isOp()){
						if(guild != null){
							if(guild.isActive()){
								String type = block.getType().toString();
								List<Material> restricted = Arrays.asList(Material.CHEST, Material.HOPPER, Material.FURNACE, Material.ANVIL, Material.ENCHANTING_TABLE, Material.CAULDRON, Material.CRAFTING_TABLE, Material.DROPPER, Material.END_PORTAL, Material.BEACON, Material.BREWING_STAND, Material.DISPENSER);
								if(member.hasGuild()){
									if(member.getGuild().equals(guild)){
										if((type.contains("DOOR") || type.contains("GATE") || type.contains("PLATE") || type.contains("BUTTON")) && !member.getPermission(Permission.USE_DOORS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'intéragir dans le territoire de votre guilde !");
										}else if(restricted.contains(block.getType()) && !member.getPermission(Permission.USE_CHESTS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'utiliser le matériel de votre guilde !");
										}else if(player.getInventory().getItemInMainHand().getType().equals(Material.PAINTING) || player.getInventory().getItemInMainHand().getType().equals(Material.ITEM_FRAME)){
											if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
												if(!member.getPermission(Permission.BUILD)){
													e.setCancelled(true);
													rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de votre guilde !");
												}
											}
										}
										return;
									}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
										if((type.contains("_DOOR") || type.contains("_GATE") || type.contains("_PLATE") || type.contains("_BUTTON") || type.contains("LEVER")) && !guild.getDefaultRank().getPermission(Permission.USE_DOORS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'intéragir dans le territoire de vos alliés !");
										}else if(restricted.contains(block.getType()) && !guild.getDefaultRank().getPermission(Permission.USE_CHESTS)){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'utiliser le matériel de vos alliés !");
										}else if(player.getInventory().getItemInMainHand().getType().equals(Material.PAINTING) || player.getInventory().getItemInMainHand().getType().equals(Material.ITEM_FRAME)){
											if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
												if(!guild.getDefaultRank().getPermission(Permission.BUILD)){//Permissions ONLY applied in guild territory.
													e.setCancelled(true);
													rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de vos alliés !");
												}
											}
										}
										return;
									}
								}
								
								if((type.contains("DOOR") || type.contains("GATE") || type.contains("PLATE") || type.contains("BUTTON")) && !guild.isClaimDoorsUsable()){
									e.setCancelled(true);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez intéragir dans le territoire de la guilde §e" + guild.getName() + " §c!");
								}else if(restricted.contains(block.getType()) && !guild.isClaimChestsUsable()){
									e.setCancelled(true);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez utiliser le matériel de la guilde §e" + guild.getName() + " §c!");
								}else if(player.getInventory().getItemInMainHand().getType().equals(Material.PAINTING) || player.getInventory().getItemInMainHand().getType().equals(Material.ITEM_FRAME)){
									if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
										if(!guild.isClaimBuildable()){
											e.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire sur le territoire de §e" + guild.getName() + " §c!");
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
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
		Player player = e.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			GMember member = GMember.get(player);
			Entity entity = e.getRightClicked();
			if(entity != null){
				if(entity instanceof ArmorStand || entity instanceof Minecart || entity instanceof Hanging){
					Claim claim = Claim.get(entity.getLocation());
					if(claim != null){
						Guild guild = claim.getGuild();
						if(!rp.isOp()){
							if(guild != null){
								if(guild.isActive()){
									if(member.hasGuild()){
										if(member.getGuild().equals(guild)){
											if(!member.getPermission(Permission.USE_CHESTS)){
												e.setCancelled(true);
												rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'utiliser le matériel de votre guilde !");
											}
											return;
										}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
											if(!guild.getDefaultRank().getPermission(Permission.USE_CHESTS)){
												e.setCancelled(true);
												rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission d'utiliser le matériel de vos alliés !");
											}
											return;
										}
									}
									
									if(!guild.isClaimChestsUsable()){
										e.setCancelled(true);
										rp.sendActionBar("§4§lHey ! §cVous ne pouvez utiliser le matériel de la guilde §e" + guild.getName() + " §c!");
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
	public void onEntityDamage(RubidiaEntityDamageEvent event){
		if(!event.isCancelled()){
			LivingEntity damaged = (LivingEntity) event.getEntity();
			Claim claim = Claim.get(damaged.getLocation());
			if(claim != null){
				Guild guild = claim.getGuild();
				if(guild != null){
					if(guild.isActive()){
						LivingEntity damager = event.getDamager();
						if(damager instanceof Player){
							Player p = (Player)damager;
							RPlayer rp = RPlayer.get(p);
							GMember member = GMember.get(p);
							if(!rp.isOp()){
								Monster monster = Monsters.get(damaged);
								if(monster != null){
									if(monster.getTamer() != null){
										if(!monster.getTamer().equals(p)){
											if(member.hasGuild()){
												if(member.getGuild().equals(guild)){
													if(!member.getPermission(Permission.DAMAGE_MOBS)){
														event.setCancelled(true);
														rp.sendActionBar("§4§lHey ! §cVous ne pouvez infliger de dommages à une créature apprivoisée à l'intérieur du territoire de la guilde §e" + guild.getName() + "§c !");
													}
													return;
												}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
													if(!guild.getDefaultRank().getPermission(Permission.DAMAGE_MOBS)){
														event.setCancelled(true);
														rp.sendActionBar("§4§lHey ! §cVous ne pouvez infliger de dommages à une créature apprivoisée à l'intérieur du territoire de la guilde §e" + guild.getName() + "§c !");
													}
													return;
												}
											}
											
											if(!guild.isClaimMobsDamageable()){
												event.setCancelled(true);
												rp.sendActionBar("§4§lHey ! §cVous ne pouvez infliger de dommages à une créature apprivoisée à l'intérieur du territoire de la guilde §e" + guild.getName() + "§c !");
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
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event){
		Player player = event.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			GMember member = GMember.get(player);
			Block block = event.getBlockClicked().getRelative(event.getBlockFace());
			if(block != null){
				Claim claim = Claim.get(block.getLocation());
				if(claim != null){
					Guild guild = claim.getGuild();
					if(!rp.isOp()){
						if(guild != null){
							if(guild.isActive()){
								if(member.hasGuild()){
									if(member.getGuild().equals(guild)){
										if(!member.getPermission(Permission.BUILD)){
											event.setCancelled(true);
											Utils.updateInventory(player);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de votre guilde !");
										}
										return;
									}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
										if(!guild.getDefaultRank().getPermission(Permission.BUILD)){//Permissions ONLY applied in guild territory.
											event.setCancelled(true);
											Utils.updateInventory(player);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de vos alliés !");
										}
										return;
									}
								}
								
								if(!guild.isClaimBuildable()){
									event.setCancelled(true);
									Utils.updateInventory(player);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire sur le territoire de §e" + guild.getName() + " §c!");
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event){
		Player player = event.getPlayer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			GMember member = GMember.get(player);
			Block block = event.getBlockClicked().getRelative(event.getBlockFace());
			if(block != null){
				Claim claim = Claim.get(block.getLocation());
				if(claim != null){
					Guild guild = claim.getGuild();
					if(!rp.isOp()){
						if(guild != null){
							if(guild.isActive()){
								if(member.hasGuild()){
									if(member.getGuild().equals(guild)){
										if(!member.getPermission(Permission.BUILD)){
											event.setCancelled(true);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de votre guilde !");
										}
										return;
									}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
										if(!guild.getDefaultRank().getPermission(Permission.BUILD)){//Permissions ONLY applied in guild territory.
											event.setCancelled(true);
											Utils.updateInventory(player);
											rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de vos alliés !");
										}
										return;
									}
								}
								
								if(!guild.isClaimBuildable()){
									event.setCancelled(true);
									Utils.updateInventory(player);
									rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire sur le territoire de §e" + guild.getName() + " §c!");
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onHangingBreak(HangingBreakByEntityEvent event){
		Entity entity = event.getEntity();
		Claim claim = Claim.get(entity.getLocation());
		if(claim != null){
			Guild guild = claim.getGuild();
			if(guild != null){
				if(guild.isActive()){
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
						GMember member = GMember.get(player);
						if(!rp.isOp()){
							if(!guild.isClaimBuildable()){
								if(member.hasGuild()){
									if(member.getGuild().equals(guild)){
										if(member.getPermission(Permission.BUILD)){
											return;
										}else rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de votre guilde !");
									}else if(member.getGuild().getRelationTo(guild).equals(Relation.ALLY)){
										if(!guild.getDefaultRank().getPermission(Permission.BUILD)){
											return;
										}else rp.sendActionBar("§4§lHey ! §cVous n'avez pas la permission de construire à l'intérieur du territoire de vos alliés !");
									}else rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire sur le territoire de §e" + guild.getName() + " §c!");
								}else rp.sendActionBar("§4§lHey ! §cVous ne pouvez construire sur le territoire de §e" + guild.getName() + " §c!");
							}else return;
						}else return;
					}
					
					if(!guild.isClaimBuildable())event.setCancelled(true);//we cancel unless...
				}
			}
		}
	}
}
