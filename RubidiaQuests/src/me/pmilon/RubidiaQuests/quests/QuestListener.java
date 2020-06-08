package me.pmilon.RubidiaQuests.quests;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerDeathEvent;
import me.pmilon.RubidiaCore.events.RPlayerMoveEvent;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.events.MonsterKillEvent;
import me.pmilon.RubidiaMonsters.events.MonsterTameChangeEvent;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.events.RPlayerAcceptQuestEvent;
import me.pmilon.RubidiaQuests.events.RPlayerFinishQuestEvent;
import me.pmilon.RubidiaQuests.events.RPlayerGiveUpQuestEvent;
import me.pmilon.RubidiaQuests.events.RPlayerObjectiveEvent;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;

public class QuestListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW) //to trigger AFTER rubidiaCore.JoinEvent
	public void onJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		new BukkitTask(QuestsPlugin.instance){

			@Override
			public void run() {
				Utils.updateFollowedQuest(player, true);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(40);
	}
	
	@EventHandler
	public void onMine(BlockBreakEvent e){
		Player breaker = e.getPlayer();
		if(!e.isCancelled()){
			if(breaker != null){
				RPlayer rp = RPlayer.get(breaker);
				Block block = e.getBlock();
				if(block != null){
					if(!rp.getQuestsOfType(ObjectiveType.MINE).isEmpty()){
						for(Quest quest : rp.getQuestsOfType(ObjectiveType.MINE)){
							quest.check(rp, block);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(RPlayerMoveEvent e){
		RPlayer rp = e.getRPlayer();
		List<Quest> quests = rp.getQuestsOfType(ObjectiveType.DISCOVER, ObjectiveType.FOLLOW);
		if(!quests.isEmpty()){
			for(Quest quest : quests){
				quest.check(rp, e.getEvent().getTo());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCraft(final CraftItemEvent e){
		HumanEntity h = e.getWhoClicked();
		if(h instanceof Player){
			Player p = (Player)h;
			final RPlayer rp = RPlayer.get(p);
			if(!e.isCancelled()){
				final List<Quest> quests = rp.getQuestsOfType(ObjectiveType.CRAFT);
				if(!quests.isEmpty()){
					for(Quest quest : quests){
						quest.check(rp, e.getCurrentItem());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onKill(MonsterKillEvent e){
		Player player = e.getKiller();
		RPlayer rp = RPlayer.get(player);
		if(!rp.getQuestsOfType(ObjectiveType.KILL).isEmpty()){
			for(Quest quest : rp.getQuestsOfType(ObjectiveType.KILL)){
				quest.check(rp, e.getMonster());
			}
		}
		
		Monster monster = e.getMonster();
		LivingEntity entity = monster.getEntity();
		if(entity.hasMetadata("questUUID")){
			Quest quest = QuestsPlugin.questColl.get(entity.getMetadata("questUUID").get(0).asString());
			if(quest != null){
				if(entity.hasMetadata("qEventIndex")){
					int index = entity.getMetadata("qEventIndex").get(0).asInt();
					if(index < quest.getQEvents().size()){
						QEvent qEvent = quest.getQEvents().get(index);
						for(RPlayer rpp : qEvent.monsters.keySet()){
							if(qEvent.monsters.containsKey(rpp))qEvent.monsters.get(rpp).remove(entity);
							if(rpp.getFollowedQuest() != null){
								if(rpp.getFollowedQuest().equals(quest)){
									Utils.updateFollowedQuest(rpp.getPlayer(), false);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent e){
		Player player = e.getPlayer();
		if(e.getState().toString().contains("CAUGHT")){
			Entity entity = e.getCaught();
			if(entity instanceof Item){
				Item item = (Item)entity;
				ItemStack stack = item.getItemStack();
				RPlayer rp = RPlayer.get(player);
				if(!rp.getQuestsOfType(ObjectiveType.FISH).isEmpty()){
					for(Quest quest : rp.getQuestsOfType(ObjectiveType.FISH)){
						quest.check(rp, stack);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPickUp(EntityPickupItemEvent event){
		if(event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if(player != null){
				RPlayer rp = RPlayer.get(player);
				Quest quest = rp.getFollowedQuest();
				if(quest != null){
					if(!quest.getObjectivesByType(ObjectiveType.GET).isEmpty()){
						Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){

							@Override
							public void run() {
								Utils.updateFollowedQuest(player, false);
							}
							
						}, 1);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onTameChange(MonsterTameChangeEvent event){
		Player player = event.getTamer();
		if(player == null)player = event.getMonster().getTamer();
		if(player != null){
			RPlayer rp = RPlayer.get(player);
			Quest quest = rp.getFollowedQuest();
			if(quest != null){
				if(!quest.getObjectivesByType(ObjectiveType.LEASH).isEmpty()){
					final Player p = player;
					Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){

						@Override
						public void run() {
							Utils.updateFollowedQuest(p, false);
						}
						
					}, 1);
				}
			}
		}
	}
	
	@EventHandler
	public void onPNJTransform(LightningStrikeEvent event){
		for(Entity entity : event.getLightning().getNearbyEntities(2, 2, 2)){
			if(entity instanceof Villager){
				if(PNJManager.isPNJ((Villager)entity)){
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(RubidiaEntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(DialogManager.isInDialog(player)){
				event.setCancelled(true);
			}
		}
	}
	
	
	
	//QUEST ITEMS
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if((Utils.isQuestItem(event.getCurrentItem()) || Utils.isQuestItem(event.getCursor())) && !RPlayer.get((Player)event.getWhoClicked()).isOp()){
			event.setCancelled(true);
			RPlayer rp = RPlayer.get((Player) event.getWhoClicked());
			rp.sendMessage("§cVous ne pouvez toucher un objet de quête !");
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		if(event.getItemDrop() != null){
			if(Utils.isQuestItem(event.getItemDrop().getItemStack())){
				event.setCancelled(true);
				RPlayer rp = RPlayer.get(event.getPlayer());
				rp.sendMessage("§cVous ne pouvez jeter un objet de quête !");
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if(item != null){
			if(Utils.isQuestItem(item)){
				event.setCancelled(true);
				RPlayer rp = RPlayer.get(player);
				rp.sendMessage("§cVous ne pouvez intéragir avec un objet de quête !");
			}
		}
	}
	
	@EventHandler
	public void onRPlayerDeath(RPlayerDeathEvent event){
		for(int i = 0;i < event.getInventoryDrops().length;i++){
			ItemStack item = event.getInventoryDrops()[i];
			if(item != null){
				if(Utils.isQuestItem(item)){
					event.getInventoryDrops()[i] = null;
				}
			}
		}
		for(int i = 0;i < event.getArmorDrops().length;i++){
			ItemStack item = event.getArmorDrops()[i];
			if(item != null){
				if(Utils.isQuestItem(item)){
					event.getArmorDrops()[i] = null;
				}
			}
		}
	}
	
	
	@EventHandler
	public void onAccept(RPlayerAcceptQuestEvent event){
		RPlayer rp = event.getRPlayer();
		Quest quest = event.getQuest();
		if(rp.isOnline()){
			Player player = rp.getPlayer();
			boolean ok = true;
			List<Integer> slots = new ArrayList<Integer>();
			for(QEvent qEvent : quest.getQEvents()){
				if(qEvent.getType().equals(QEventType.ITEM)){
					int amount = 0;
					for(int i = 0;i < 36;i++){//because we only wanna check inventory contents
						if(amount < qEvent.getItemStack().getAmount()){
							if(!slots.contains(i) && i != 8){
								slots.add(i);
								ItemStack stack = player.getInventory().getItem(i);
								if(stack == null || stack.getType().equals(Material.AIR))amount += 64;
								else if(stack.isSimilar(qEvent.getItemStack()))amount += 64-stack.getAmount();
							}
							if(i == 35 && amount < qEvent.getItemStack().getAmount()){
								ok = false;
								break;
							}
						}else break;
					}
				}
			}
			
			if(!ok){
				event.setCancelled(true);
				PNJHandler pnj = PNJManager.getPNJByUniqueId(event.getHandlerUUID());
				if(pnj != null)rp.sendMessage("§cIl semble que vous ne pouvez porter ce que §4" + pnj.getName() + " §cveut vous donner. Faîtes de la place dans votre inventaire !");
				else rp.sendMessage("§cIl semble que vous ne pouvez porter ce que l'on veut vous donner. Faîtes de la place dans votre inventaire !");
			}
		}
	}
	
	@EventHandler
	public void onGiveUp(RPlayerGiveUpQuestEvent event){
		final RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			Quest quest = event.getQuest();
			if(rp.getFollowedQuest() != null){
				if(rp.getFollowedQuest().equals(quest)){
					Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){

						@Override
						public void run() {
							Utils.updateFollowedQuest(rp.getPlayer(), true);
						}
						
					}, 1);
				}
			}
		}
	}
	
	@EventHandler
	public void onFinish(RPlayerFinishQuestEvent event){
		RPlayer rp = event.getRPlayer();
		Quest quest = event.getQuest();
		if(rp.isOnline()){
			Player player = rp.getPlayer();
			boolean ok = true;
			List<Integer> slots = new ArrayList<Integer>();
			for(Reward reward : quest.getRewards()){
				if(reward.getType().equals(RewardType.ITEM)){
					int amount = 0;
					for(int i = 0;i < 36;i++){//because we only wanna check the inventory contents
						if(amount < reward.getItemStack().getAmount()){
							if(!slots.contains(i) && i != 8){
								slots.add(i);
								ItemStack stack = player.getInventory().getItem(i);
								if(stack == null || stack.getType().equals(Material.AIR))amount += 64;
								else if(stack.isSimilar(reward.getItemStack()))amount += 64-stack.getAmount();
							}
							if(i == 35 && amount < reward.getItemStack().getAmount()){
								ok = false;
								break;
							}
						}else break;
					}
				}
			}
			
			if(!ok){
				event.setCancelled(true);
				PNJHandler pnj = quest.getHolder(rp);
				if(pnj != null)rp.sendMessage("§cIl semble que vous ne pouvez porter ce que §4" + pnj.getName() + " §cveut vous donner. Faîtes de la place dans votre inventaire !");
				else rp.sendMessage("§cIl semble que vous ne pouvez porter ce que l'on veut vous donner. Faîtes de la place dans votre inventaire !");
			}
		}
	}

	@EventHandler
	public void onUpdate(RPlayerObjectiveEvent event){
		final RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			if(rp.getFollowedQuest() != null){
				Quest quest = event.getQuest();
				if(rp.getFollowedQuest().equals(quest)){
					Utils.updateFollowedQuest(rp.getPlayer(), false);
				}
			}
		}
	}

}
