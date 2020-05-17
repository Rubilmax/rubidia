package me.pmilon.RubidiaQuests.pnjs;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EntityHandler;
import me.pmilon.RubidiaCore.tags.TagStand;
import me.pmilon.RubidiaCore.tags.TagStandManager;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.events.RPlayerFinishQuestEvent;
import me.pmilon.RubidiaQuests.events.RPlayerGiveUpQuestEvent;
import me.pmilon.RubidiaQuests.events.RPlayerObjectiveEvent;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Reward;
import me.pmilon.RubidiaQuests.quests.RewardType;
import me.pmilon.RubidiaQuests.utils.Utils;
import net.minecraft.server.v1_15_R1.EntityVillager;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PNJListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		RPlayer rp = RPlayer.get(player);
		List<Quest> quests = rp.getQuestsOfType(ObjectiveType.FOLLOW);
		if(!quests.isEmpty()){
			for(Quest quest : quests){
				for(Objective objective : quest.getObjectivesByType(ObjectiveType.FOLLOW)){
					if(!objective.isFilled(rp)){
						PNJHandler pnj = PNJManager.getPNJByUniqueId(objective.getTargetUUID());
						pnj.createFake(player);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(DialogManager.isInDialog(player))DialogManager.setNoDialog(player);
		if(PNJManager.pnjTemps.containsKey(player)){
			for(Villager villager : PNJManager.pnjTemps.get(player)){
				villager.remove();
			}
			PNJManager.pnjTemps.remove(player);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(player != null){
			if(PNJManager.pnjTemps.containsKey(player)){
				for(Villager villager : PNJManager.pnjTemps.get(player)){
					Location location = LocationUtils.getSafeLocation(event.getTo().clone().subtract(player.getEyeLocation().getDirection().normalize().multiply(1.35)));
					double distance = villager.getLocation().distance(location);
					if(distance > 1.75){
						if(distance < 36){
							EntityVillager v = ((CraftVillager)villager).getHandle();
							v.getNavigation().a(location.getX(), event.getTo().getY(), location.getZ(), .6+distance*.03);
						}else villager.teleport(location);
					}
				}
			}
			if(PNJManager.pnjTokillTemps.containsKey(player)){
				List<Villager> vs = PNJManager.pnjTokillTemps.get(player);
				List<Villager> villagers = new ArrayList<Villager>();
				for(Villager villager : vs){
					if(!villager.getWorld().equals(player.getWorld()))villagers.add(villager);
					else if(villager.getLocation().distanceSquared(player.getLocation()) > 42*42)villagers.add(villager);
				}
				vs.removeAll(villagers);
				PNJManager.pnjTokillTemps.put(player, vs);
				if(!villagers.isEmpty()){
					for(Villager villager : villagers){
						PNJHandler handler = PNJManager.getPNJByUniqueId(villager.getMetadata("FakeNPCVillager").get(0).asString());
						TagStand stand = TagStandManager.getTagStand(villager);
						if(stand != null) stand.remove();
						villager.remove();
						
						if(handler.getEntity() != null) EntityHandler.showEntity(player, handler.getEntity());
						handler.getTag().show(player);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();
		if(player != null){
			if(!DialogManager.isInDialog(player)){
				if(PNJManager.pnjTemps.containsKey(player)){
					for(Villager villager : PNJManager.pnjTemps.get(player)){
						Location location = event.getTo().clone().subtract(player.getEyeLocation().getDirection().normalize().multiply(1.35).setY(0));
						if(location.getBlock().getType().isTransparent())villager.teleport(location);
						else villager.teleport(event.getTo());
					}
				}
				if(PNJManager.pnjTokillTemps.containsKey(player)){
					for(Villager villager : PNJManager.pnjTokillTemps.get(player)){
						PNJHandler handler = PNJManager.getPNJByUniqueId(villager.getMetadata("FakeNPCVillager").get(0).asString());
						TagStand stand = TagStandManager.getTagStand(villager);
						if(stand != null) stand.remove();
						villager.remove();
						
						if(handler.getEntity() != null) EntityHandler.showEntity(player, handler.getEntity());
						handler.getTag().show(player);
					}
					PNJManager.pnjTokillTemps.remove(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onGiveUp(RPlayerGiveUpQuestEvent event){
		final RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			Quest quest = event.getQuest();
			List<Objective> objectives = quest.getObjectivesByType(ObjectiveType.FOLLOW);
			if(!objectives.isEmpty()){
				for(Objective objective : objectives){
					PNJHandler handler = PNJManager.getPNJByUniqueId(objective.getTargetUUID());
					handler.destroyFake(rp.getPlayer());
				}
			}
		}
	}
	
	@EventHandler
	public void onFinish(RPlayerFinishQuestEvent event){
		RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			Quest quest = event.getQuest();
			List<Objective> objectives = quest.getObjectivesByType(ObjectiveType.FOLLOW);
			if(!objectives.isEmpty()){
				for(Objective objective : objectives){
					PNJHandler handler = PNJManager.getPNJByUniqueId(objective.getTargetUUID());
					if(handler != null){
						handler.destroyFake(rp.getPlayer());
					}
				}
			}
		}
	}

	@EventHandler
	public void onUpdate(RPlayerObjectiveEvent event){
		final RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			Objective objective = event.getObjective();
			if(objective.getType().equals(ObjectiveType.FOLLOW)){
				final PNJHandler handler = PNJManager.getPNJByUniqueId(objective.getTargetUUID());
				if(handler != null){
					Villager villager = handler.getFakePNJ(rp.getPlayer());
					if(villager != null){
						boolean keep = false;
						final Quest quest = objective.getQuest();
						for(Reward reward : quest.getRewards()){
							if(reward.getType().equals(RewardType.QUEST)){
								keep = true;
							}
							if(keep)break;
						}
						new PNJDialog(rp.getPlayer(), handler, villager, objective.getDialogs(), DialogType.YES, new Runnable(){

							@Override
							public void run() {
								if(quest.isAutoFinish())quest.checkIsAutoFinish(rp);
								handler.destroyFake(rp.getPlayer());
							}
							
						}, keep && quest.hasFinished(rp) && quest.isAutoFinish(), true).start();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(final PlayerInteractEntityEvent e){
		if(e.getHand() != null){
			if(e.getHand().equals(EquipmentSlot.HAND)){
				if(e.getRightClicked() instanceof Villager){
					final Villager villager = (Villager)e.getRightClicked();
					boolean isPNJ = PNJManager.isPNJ(villager);
					if(isPNJ || villager.hasMetadata("FakeNPCVillager")){
						e.setCancelled(true);
						final PNJHandler handler = isPNJ ? PNJManager.getSession(villager).getPNJHandler() : PNJManager.getPNJByUniqueId(villager.getMetadata("FakeNPCVillager").get(0).asString());
						final Player p = e.getPlayer();
						final RPlayer rp = RPlayer.get(p);
						if(e.getPlayer().isSneaking() && rp.isOp()){
							handler.onSetting(e, e.getPlayer());
						}else{
							if(!DialogManager.isInDialog(p)){
								Quest active = null;
								for(Quest quest : rp.getActiveQuests()){
									if(handler.getUniqueId().equals(quest.getHolderUUID(rp))){
										active = quest;
										break;
									}
								}
								final Quest activeQuest = active;
								if(activeQuest != null){
									for(Objective objective : activeQuest.getObjectivesByType(ObjectiveType.GET, ObjectiveType.LEASH)){//so a quest cannot have an objective TALK with the quest holder
										objective.check(rp, handler.getUniqueId());
									}
									if(activeQuest.hasFinished(rp)){
										boolean keep = false;
										for(Reward reward : activeQuest.getRewards()){
											if(reward.getType().equals(RewardType.QUEST)){
												keep = true;
												break;
											}
										}
										PNJDialog dialog = new PNJDialog(p, handler, villager, activeQuest.getPostDialogs(), DialogType.YES, new Runnable(){
											public void run(){
												activeQuest.finish(rp);
											}
										}, keep, true);
										dialog.start();
									}else new PNJDialog(p, handler, villager, activeQuest.getNonDialogs(), DialogType.NO, null, false, true).start();
								}else{
									List<Quest> quests = rp.getQuestsOfType(ObjectiveType.TALK, ObjectiveType.GET, ObjectiveType.LEASH, ObjectiveType.SIDE_QUEST);
									if(!quests.isEmpty()){
										for(final Quest quest : quests){
											Objective objective = null;
											List<Objective> objectives = quest.getObjectivesByType(ObjectiveType.TALK, ObjectiveType.GET, ObjectiveType.LEASH, ObjectiveType.SIDE_QUEST);
											for(final Objective obj : objectives){
												final long score = obj.getScore(rp);
												if((!obj.isFilled(rp) || objectives.size() <= 1) && obj.check(rp, handler.getUniqueId())){
													objective = obj;
												}
												if(score != obj.getScore(rp))quest.sendUpdate(obj, rp);
											}
											if(objective != null){
												if(!objective.getDialogs().isEmpty()){
													boolean keep = false;
													for(Reward reward : quest.getRewards()){
														if(reward.getType().equals(RewardType.QUEST)){
															keep = true;
															break;
														}
													}
													final Objective obj = objective;
													new PNJDialog(p, handler, villager, objective.getDialogs(), DialogType.AMBIENT, new Runnable(){

														@Override
														public void run() {
															if(quest.isAutoFinish())quest.checkIsAutoFinish(rp);
															else if(obj.getType().equals(ObjectiveType.SIDE_QUEST)){
																Quest sideQuest = obj.getSideQuest();
																if(!rp.getActiveQuests().contains(sideQuest)){
																	sideQuest.accept(rp, handler.getUniqueId());
																	Utils.updateFollowedQuest(p, true);
																}
															}else if(obj.getType().equals(ObjectiveType.GET) || obj.getType().equals(ObjectiveType.LEASH)){
																for(Objective objective : quest.getObjectives()){
																	if(!objective.isFilled(rp)){
																		return;
																	}
																}
																quest.finish(rp);
															}
														}
														
													}, quest.hasFinished(rp) && keep && (quest.isAutoFinish() || handler.getUniqueId().equals(quest.getHolderUUID(rp))), true).start();
													return;
												}else break;
											}
										}
									}

									Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){
										public void run(){ //to delay quest end + starting quests (TALK enchainement)
											if(!DialogManager.isInDialog(p))
												handler.onRightClick(e, e.getPlayer(), villager);
										}
									},1);
								}
							}else DialogManager.getDialog(p).stepNext();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e){
		Entity entity = e.getEntity();
		if(entity instanceof Villager){
			Villager villager = (Villager)entity;
			if(PNJManager.isPNJ(villager)){
				PNJManager.getSession(villager).getPNJHandler().spawn(true);
			}else if(villager.hasMetadata("FakeNPCVillager")){
				List<Player> players = new ArrayList<Player>();
				PNJHandler pnj = PNJManager.getPNJByUniqueId(villager.getMetadata("FakeNPCVillager").get(0).asString());
				for(Player player : PNJManager.pnjTemps.keySet()){
					List<Villager> villagers = PNJManager.pnjTemps.get(player);
					if(villagers.contains(villager)){
						players.add(player);
					}
				}
				for(Player player : players){
					PNJManager.pnjTemps.get(player).remove(villager);
					pnj.createFake(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		Entity entity = e.getEntity();
		if(e.getEntity() instanceof Villager){
			Villager villager = (Villager)entity;
			if(PNJManager.isPNJ(villager) || villager.hasMetadata("FakeNPCVillager")){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onTarget(EntityTargetEvent e){
		Entity entity = e.getTarget();
		if(entity instanceof Villager){
			Villager villager = (Villager)entity;
			if(PNJManager.isPNJ(villager) || villager.hasMetadata("FakeNPCVillager"))e.setCancelled(true);
		}
	}

	@EventHandler
	private void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if (player != null) {
			RPlayer rp = RPlayer.get(player);
			if (rp.isOp()) return;
		}

		Block block = event.getBlock();
		for(Entity entity : LocationUtils.getNearbyEntities(block.getLocation(), 2.5)){
			if(entity instanceof Villager){
				if(PNJManager.isPNJ((Villager) entity)){
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	private void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if (player != null) {
			RPlayer rp = RPlayer.get(player);
			if (rp.isOp()) return;
		}
		
		Block block = event.getBlock();
		for(Entity entity : LocationUtils.getNearbyEntities(block.getLocation(), 3)){
			if(entity instanceof Villager){
				if(PNJManager.isPNJ((Villager) entity) || entity.hasMetadata("FakeNPCVillager")){
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		final Chunk chunk = event.getChunk();
		for(Entity entity : chunk.getEntities()){
			if(entity instanceof Villager){
				if(PNJManager.isPNJ((Villager) entity)){
					//event.setCancelled(true);
					Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){
						public void run(){
							chunk.load();
						}
					}, 1);
					return;
				}
			}
		}
	}
}
