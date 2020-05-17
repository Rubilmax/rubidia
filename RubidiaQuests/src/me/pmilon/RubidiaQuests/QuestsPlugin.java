package me.pmilon.RubidiaQuests;

import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Sounds;
import me.pmilon.RubidiaQuests.commands.HouseCommandExecutor;
import me.pmilon.RubidiaQuests.commands.PNJCommandExecutor;
import me.pmilon.RubidiaQuests.commands.PNJTPCommandExecutor;
import me.pmilon.RubidiaQuests.commands.PassCommandExecutor;
import me.pmilon.RubidiaQuests.commands.QuestCommandExecutor;
import me.pmilon.RubidiaQuests.commands.QuestItemCommandExecutor;
import me.pmilon.RubidiaQuests.commands.QuestProblemsCommandExecutor;
import me.pmilon.RubidiaQuests.commands.QuestsCommandExecutor;
import me.pmilon.RubidiaQuests.commands.ShopCommandExecutor;
import me.pmilon.RubidiaQuests.dialogs.MessageManager;
import me.pmilon.RubidiaQuests.houses.HouseColl;
import me.pmilon.RubidiaQuests.houses.HouseListener;
import me.pmilon.RubidiaQuests.pnjs.DialogerPNJ;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PNJSession;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.QuestColl;
import me.pmilon.RubidiaQuests.quests.QuestListener;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.shops.PNJShopColl;
import me.pmilon.RubidiaQuests.shops.ShopListener;
import me.pmilon.RubidiaQuests.utils.Configs;
import me.pmilon.RubidiaQuests.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class QuestsPlugin extends JavaPlugin {

	public static QuestsPlugin instance;
	public static PNJManager pnjManager;
	public static ConsoleCommandSender console;
	
	public static QuestColl questColl;
	public static PNJShopColl shopColl;
	public static HouseColl houseColl;
	
	public static WorldEditPlugin worldEdit;

	public void onEnable(){
		instance = this;
		console = Bukkit.getConsoleSender();
		
		console.sendMessage("§a   Loading Quests...");
		questColl = new QuestColl();
		console.sendMessage("§a   Loading Shops...");
		shopColl = new PNJShopColl();
		console.sendMessage("§a   Loading Houses...");
		houseColl = new HouseColl();
		
		if(Bukkit.getPluginManager().getPlugin("WorldEdit") != null){
			worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		}
		
		Bukkit.getPluginManager().registerEvents(new QuestListener(), this);
		Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
		Bukkit.getPluginManager().registerEvents(new HouseListener(), this);
		
		this.getCommand("pnj").setExecutor(new PNJCommandExecutor());
		this.getCommand("pnjtp").setExecutor(new PNJTPCommandExecutor());
		this.getCommand("quest").setExecutor(new QuestCommandExecutor());
		this.getCommand("quests").setExecutor(new QuestsCommandExecutor());
		this.getCommand("questitem").setExecutor(new QuestItemCommandExecutor());
		this.getCommand("shop").setExecutor(new ShopCommandExecutor());
		this.getCommand("pass").setExecutor(new PassCommandExecutor());
		this.getCommand("house").setExecutor(new HouseCommandExecutor());
		this.getCommand("qproblems").setExecutor(new QuestProblemsCommandExecutor());
		
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		Configs.saveDefaultQuestsConfig();
		Configs.saveDefaultPNJConfig();
		Configs.saveDefaultShopsConfig();
		Configs.saveDefaultHousesConfig();
	}
	
	public static void onStart(){//called later
		console.sendMessage("§a   Loading PNJs...");
		pnjManager = new PNJManager(QuestsPlugin.instance);
		QuestsPlugin.updateDialogs();
		
		new BukkitTask(QuestsPlugin.instance){

			@Override
			public void run() {
				QuestsPlugin.onTaskTimer();
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0,10);
	}
	
	public static void onEnd(){
		for(List<Villager> villagers : PNJManager.pnjTemps.values()){
			for(Villager villager : villagers){
				villager.remove();
			}
		}
		for(List<Villager> villagers : PNJManager.pnjTokillTemps.values()){
			for(Villager villager : villagers){
				villager.remove();
			}
		}

		questColl.saveAll(true);
		PNJManager.save(true);
		shopColl.saveAll(true);
		houseColl.saveAll(true);
	}

	public static void onTaskTimer(){
		for(Objective timed : QuestsPlugin.questColl.getTimedObjectives()){
			for(String rpUUID : timed.getScores().keySet()){
				RPlayer rp = Core.rcoll.get(rpUUID);
				long start = timed.getScore(rp);
				if(rp != null){
					if(start + timed.getAmount()*1000L < System.currentTimeMillis()){
						rp.getActiveQuests().remove(timed.getQuest());
						timed.removeScore(rp);
						rp.sendTitle("§4" + timed.getQuest().getColoredTitle(), ("§cNon terminée à temps !"), 5, 80, 10);
						if(rp.isOnline()){
							if(rp.getFollowedQuest() != null){
								if(rp.getFollowedQuest().equals(timed.getQuest())){
									Utils.updateFollowedQuest(rp.getPlayer(), true);
								}
							}
							Sounds.playQuestGiveUp(rp.getPlayer());
						}
					}
				}
			}
		}
		for(PNJSession session : PNJManager.pnjs.values()){
			PNJHandler pnj = session.getPNJHandler();
			if(pnj.getEntity() != null){
				pnj.updateColor();
			}
		}
	}
	
	public static void updateDialogs(){
		for(Quest quest : QuestsPlugin.questColl.data()){
			for(int i = 0;i < quest.getPreDialogs().size();i++){
				quest.getPreDialogs().set(i, MessageManager.filterDialog(quest.getPreDialogs().get(i)));
			}
			for(int i = 0;i < quest.getPostDialogs().size();i++){
				quest.getPostDialogs().set(i, MessageManager.filterDialog(quest.getPostDialogs().get(i)));
			}
			for(int i = 0;i < quest.getNonDialogs().size();i++){
				quest.getNonDialogs().set(i, MessageManager.filterDialog(quest.getNonDialogs().get(i)));
			}
			for(Objective objective : quest.getObjectives()){
				for(int i = 0;i < objective.getDialogs().size();i++){
					objective.getDialogs().set(i, MessageManager.filterDialog(objective.getDialogs().get(i)));
				}
			}
			for(Required required : quest.getRequireds()){
				required.setNoDialog(MessageManager.filterDialog(required.getNoDialog()));
			}
		}
		
		for(PNJSession session : PNJManager.pnjs.values()){
			PNJHandler pnj = session.getPNJHandler();
			if(pnj instanceof DialogerPNJ){
				DialogerPNJ dialoger = (DialogerPNJ)pnj;
				for(int i = 0;i < dialoger.getDialogs().size();i++){
					dialoger.getDialogs().set(i, MessageManager.filterDialog(dialoger.getDialogs().get(i)));
				}
			}else if(pnj instanceof QuestPNJ){
				QuestPNJ quester = (QuestPNJ)pnj;
				quester.setNoQuestDialog(MessageManager.filterDialog(quester.getNoQuestDialog()));
			}
		}
	}
}
