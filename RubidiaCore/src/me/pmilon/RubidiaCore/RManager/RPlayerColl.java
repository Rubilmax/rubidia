package me.pmilon.RubidiaCore.RManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.Smiley;
import me.pmilon.RubidiaCore.abilities.RAbility;
import me.pmilon.RubidiaCore.chat.RChatUtils;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RPlayerColl extends Database<String,RPlayer> {
	
	public static int saves = 0;
	
	public RPlayerColl(){
		if(Configs.getPlayerConfig().contains("players")){
			for(String k : Configs.getPlayerConfig().getConfigurationSection("players").getKeys(false)){
				String path = "players." + k;
				SPlayer[] saves = new SPlayer[4];
				if(Configs.getPlayerConfig().contains(path + ".saves")){
					for(int i = 0;i < 4;i++){
						String path2 = path + ".saves." + i;
						if(Configs.getPlayerConfig().contains(path2)){
							HashMap<Integer, ItemStack> creative = new HashMap<Integer, ItemStack>();
							if(Configs.getPlayerConfig().contains(path2 + ".gamemode.creative")){
								for(String s : Configs.getPlayerConfig().getConfigurationSection(path2 + ".gamemode.creative").getKeys(false)){
									creative.put(Integer.valueOf(s), Configs.getPlayerConfig().getItemStack(path2 + ".gamemode.creative." + s));
								}
							}
							HashMap<Integer, ItemStack> survival = new HashMap<Integer, ItemStack>();
							if(Configs.getPlayerConfig().contains(path2 + ".gamemode.survival")){
								for(String s : Configs.getPlayerConfig().getConfigurationSection(path2 + ".gamemode.survival").getKeys(false)){
									survival.put(Integer.valueOf(s), Configs.getPlayerConfig().getItemStack(path2 + ".gamemode.survival." + s));
								}
							}
							HashMap<Integer, ItemStack> lastInventory = new HashMap<Integer, ItemStack>();
							if(Configs.getPlayerConfig().contains(path2 + ".lastInventory")){
								for(String s : Configs.getPlayerConfig().getConfigurationSection(path2 + ".lastInventory").getKeys(false)){
									lastInventory.put(Integer.valueOf(s), Configs.getPlayerConfig().getItemStack(path2 + ".lastInventory." + s));
								}
							}
							HashMap<Integer, ItemStack> enderchest = new HashMap<Integer, ItemStack>();
							if(Configs.getPlayerConfig().contains(path2 + ".enderChest")){
								for(String s : Configs.getPlayerConfig().getConfigurationSection(path2 + ".enderChest").getKeys(false)){
									enderchest.put(Integer.valueOf(s), Configs.getPlayerConfig().getItemStack(path2 + ".enderChest." + s));
								}
							}
							HashMap<RAbility, Integer> abilityLevels = new HashMap<RAbility, Integer>();
							if(Configs.getPlayerConfig().contains(path2 + ".ability")){
								for(String s : Configs.getPlayerConfig().getConfigurationSection(path2 + ".ability").getKeys(false)){
									try {
										abilityLevels.put(RAbility.valueOf(s), Configs.getPlayerConfig().getInt(path2 + ".ability." + s));
									} catch(Exception ex) {
										Configs.getPlayerConfig().set(path2 + ".ability." + s, null);
									}
								}
							}
							List<Pet> pets = new ArrayList<Pet>();
							for(String uuid : Configs.getPlayerConfig().getStringList(path2 + ".pets")){
								Pet pet = Pets.get(uuid);
								if(pet != null){
									pets.add(pet);
								}
							}
							List<Quest> activeQuests = new ArrayList<Quest>();
							for(String uuid : Configs.getPlayerConfig().getStringList(path2 + ".activeQuests")){
								Quest quest = Quest.get(uuid);
								if(quest != null){
									activeQuests.add(quest);
								}
							}
							List<Quest> doneQuests = new ArrayList<Quest>();
							for(String uuid : Configs.getPlayerConfig().getStringList(path2 + ".doneQuests")){
								Quest quest = Quest.get(uuid);
								if(quest != null){
									doneQuests.add(quest);
								}
							}
							try {
								RJob.valueOf(Configs.getPlayerConfig().getString(path2 + ".rJob"));
							} catch(Exception ex) {
								Configs.getPlayerConfig().set(path2 + ".rJob", "JOBLESS");
							}
							Quest followedQuest = Quest.get(Configs.getPlayerConfig().getString(path2 + ".followedQuest"));
							Configs.getPlayerConfig().set(path2 + ".currentnrj", null);
							SPlayer sp = new SPlayer(i, Configs.getPlayerConfig().getInt(path2 + ".rLevel"),
									Configs.getPlayerConfig().getDouble(path2 + ".rExp"),
									RClass.valueOf(Configs.getPlayerConfig().getString(path2 + ".rClass")),
									RJob.valueOf(Configs.getPlayerConfig().getString(path2 + ".rJob")),
									Mastery.valueOf(Configs.getPlayerConfig().getString(path2 + ".mastery")),
									Configs.getPlayerConfig().getInt(path2 + ".skillpoints"),
									Configs.getPlayerConfig().getInt(path2 + ".distinctionpoints"),
									abilityLevels,
									Configs.getPlayerConfig().getInt(path2 + ".strength"),
									Configs.getPlayerConfig().getInt(path2 + ".endurance"),
									Configs.getPlayerConfig().getInt(path2 + ".agility"),
									Configs.getPlayerConfig().getInt(path2 + ".intelligence"),
									Configs.getPlayerConfig().getInt(path2 + ".perception"),
									Configs.getPlayerConfig().getInt(path2 + ".currentnrj"),
									Configs.getPlayerConfig().getInt(path2 + ".kills"),
									Configs.getPlayerConfig().getInt(path2 + ".renom"),
									pets, creative, survival, Configs.getPlayerConfig().getInt(path2 + ".bank"),
									activeQuests, doneQuests, followedQuest,
									(Location)Configs.getPlayerConfig().get(path2 + ".lastLocation"),
									lastInventory, enderchest,
									Configs.getPlayerConfig().getDouble(path2 + ".lastHealth"),
									Configs.getPlayerConfig().getInt(path2 + ".lastFoodLevel"));
							saves[i] = sp;
							RPlayerColl.saves++;
						}
					}
				}
				
				Gender gender = Gender.UNKNOWN;
				if(Configs.getPlayerConfig().contains(path + ".gender")){
					gender = Gender.valueOf(Configs.getPlayerConfig().getString(path + ".gender"));
				}
				
				RPlayer rp = new RPlayer(k,
						Configs.getPlayerConfig().getString(path + ".name"),
						gender,
						Configs.getPlayerConfig().getLong(path + ".birthDate"),
						Configs.getPlayerConfig().getBoolean(path + ".profileUpdated"),
						Configs.getPlayerConfig().getBoolean(path + ".notifonfriendjoin"),
						Configs.getPlayerConfig().getBoolean(path + ".notifonchatrequest"),
						Configs.getPlayerConfig().getBoolean(path + ".invocation"),
						Configs.getPlayerConfig().getBoolean(path + ".teleportation"),
						Configs.getPlayerConfig().getInt(path + ".combatLevel"),
						Configs.getPlayerConfig().getBoolean(path + ".clickSound"),
						Configs.getPlayerConfig().getBoolean(path + ".music"),
						Configs.getPlayerConfig().getBoolean(path + ".usingCycle"),
						Configs.getPlayerConfig().getBoolean(path + ".publicData"),
						Configs.getPlayerConfig().getLong(path + ".vip"),
						false, saves, Configs.getPlayerConfig().getInt(path + ".lastLoadedSPlayerId"),
						Configs.getPlayerConfig().getLong(path + ".lastConnectionDate"),
						Configs.getPlayerConfig().getLong(path + ".gamingTime"),
						Configs.getPlayerConfig().getInt(path + ".pendingRubis"),
						Configs.getPlayerConfig().getString(path + ".coupleUUID"),
						Configs.getPlayerConfig().getLong(path + ".lastDivorce"),
						Configs.getPlayerConfig().getInt(path + ".chatHeight"),
						Configs.getPlayerConfig().getInt(path + ".chatboxWidth"),
						Configs.getPlayerConfig().getInt(path + ".chatboxHeight"),
						Configs.getPlayerConfig().contains(path + ".usingChat") ? Configs.getPlayerConfig().getBoolean(path + ".usingChat") : true);
				this.load(k,rp);
				SPlayer sp = rp.getSaves()[rp.getLastLoadedSPlayerId()];
				if(sp == null){
					for(SPlayer sp1 : rp.getSaves()){
						if(sp1 != null){
							sp = sp1;
							break;
						}
					}
					if(sp == null){
						sp = this.newDefaultSPlayer(0);
						rp.getSaves()[0] = sp;
					}
					rp.setLastLoadedSPlayerId(sp.getId());
				}
				rp.setLoadedSPlayer(sp);
				
				/*if(!Configs.getPlayerConfig().contains(path + ".saves")){
					Configs.getPlayerConfig().set(path, null);
					rp.setModified(true);
					int level = rp.getRLevel();
					rp.setRLevel(0, new RXPSource(RXPSourceType.ADJUST, null,null));
					rp.resetAbilities();
					rp.resetStats();
					int skp = 0;
					int skd = 0;
					for(int i = 1;i <= level;i++){
						if(i < Settings.LEVEL_MASTER){
							skp++;
							skd++;
						}
						skp++;
						skd++;
					}
					rp.setSkillDistinctionPoints(skd);
					rp.setSkillPoints(skp);
					rp.setRLevel(level, new RXPSource(RXPSourceType.ADJUST, null,null));
				}*/
				if(this.size() % 500 == 0){
					Core.console.sendMessage("§6LOADED §e" + this.size() + " §6PLAYERS & §e" + RPlayerColl.saves + " §6SAVES");
				}
			}
			//this.save(false);
		}
		Core.console.sendMessage("§6LOADED §e" + this.size() + " §6PLAYERS & §e" + RPlayerColl.saves + " §6SAVES");
	}

	public RPlayer get(Player p){
		return this.get(p.getUniqueId().toString());
	}

	public RPlayer get(GMember member){
		return this.get(member.getUniqueId());
	}
	
	public RPlayer getFromName(String name){
		for(RPlayer rp : this.data()){
			if(rp.getName().equals(name)){
				return rp;
			}
		}
		return null;
	}
	
	public SPlayer newDefaultSPlayer(int id){
		return new SPlayer(id, 0, 0.0, RClass.VAGRANT, RJob.JOBLESS, Mastery.VAGRANT, 0, 0, new HashMap<RAbility, Integer>(), 0, 0, 0, 0, 0, 100, 0, 1000, new ArrayList<Pet>(), new HashMap<Integer, ItemStack>(), new HashMap<Integer, ItemStack>(), 0, new ArrayList<Quest>(), new ArrayList<Quest>(), null, null, new HashMap<Integer, ItemStack>(), new HashMap<Integer, ItemStack>(), 20, 20);
	}
	
	public boolean contains(Player p){
		return this.containsKey(p.getUniqueId().toString());
	}

	@Override
	protected void save(boolean debug, RPlayer rp) {
		if(rp.isModified()){
			rp.setModified(false);
			String path = "players." + rp.getUniqueId();
			Configs.getPlayerConfig().set(path + ".name", rp.getName());
			Configs.getPlayerConfig().set(path + ".gender", rp.getSex().toString());
			Configs.getPlayerConfig().set(path + ".birthDate", rp.getBirthDate());
			Configs.getPlayerConfig().set(path + ".profileUpdated", rp.isProfileUpdated());
			Configs.getPlayerConfig().set(path + ".notifonfriendjoin", rp.getNotifOnFriendJoin());
			Configs.getPlayerConfig().set(path + ".notifonchatrequest", rp.getNotifOnChatRequest());
			Configs.getPlayerConfig().set(path + ".invocation", rp.getWouldLikeInvocation());
			Configs.getPlayerConfig().set(path + ".teleportation", rp.getWouldLikeTeleportation());
			Configs.getPlayerConfig().set(path + ".combatLevel", rp.getCombatLevel());
			Configs.getPlayerConfig().set(path + ".clickSound", rp.getClickSound());
			Configs.getPlayerConfig().set(path + ".music", rp.getMusic());
			Configs.getPlayerConfig().set(path + ".usingCycle", rp.isUsingCycle());
			Configs.getPlayerConfig().set(path + ".publicData", rp.isPublicData());
			Configs.getPlayerConfig().set(path + ".vip", rp.getVip());
			Configs.getPlayerConfig().set(path + ".friends", null);
			Configs.getPlayerConfig().set(path + ".lastLoadedSPlayerId", rp.getLastLoadedSPlayerId());
			Configs.getPlayerConfig().set(path + ".lastConnectionDate", rp.getLastConnectionDate());
			Configs.getPlayerConfig().set(path + ".gamingTime", rp.getGamingTime());
			Configs.getPlayerConfig().set(path + ".pendingRubis", rp.getPendingRubis());
			Configs.getPlayerConfig().set(path + ".coupleUUID", rp.getCoupleUUID());
			Configs.getPlayerConfig().set(path + ".lastDivorce", rp.getLastDivorce());
			Configs.getPlayerConfig().set(path + ".chatHeight", rp.getChatHeight());
			Configs.getPlayerConfig().set(path + ".chatboxWidth", rp.getChatboxWidth());
			Configs.getPlayerConfig().set(path + ".chatboxHeight", rp.getChatboxHeight());
			Configs.getPlayerConfig().set(path + ".usingChat", rp.isUsingChat());
			for(int i = 0;i < rp.getSaves().length;i++){
				SPlayer sp = rp.getSaves()[i];
				if(sp != null){
					if(sp.isModified()){
						sp.save(rp);
					}
				} else Configs.getPlayerConfig().set(path + ".saves." + i, null);
			}
			if(debug){//if true == server stopping
				if(rp.isOnline()){
					if(Smiley.isSmileying(rp.getPlayer())){
						rp.smileyTask.run();
					}
				}
				Core.console.sendMessage("§6Saved §e" + rp.getName());
			}
		}
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) Core.console.sendMessage("§eSaving RPlayerColl...");
	}

	@Override
	protected void onSaveEnd(boolean debug) {
		Configs.savePlayerConfig();
	}

	@Override
	protected RPlayer getDefault(String uuid) {
		SPlayer[] saves = new SPlayer[4];
		saves[0] = this.newDefaultSPlayer(0);
		RPlayer rp = new RPlayer(uuid, Bukkit.getPlayer(UUID.fromString(uuid)).getName(), Gender.UNKNOWN, 0L, false, true, true, true, true, 3, true, true, true, false, 0L, true, saves, 0, System.currentTimeMillis(), 0L, 0, null, 0L, 11, RChatUtils.MAX_CHAT_WIDTH, RChatUtils.MAX_CHAT_HEIGHT, true);
		rp.setLoadedSPlayer(rp.getSaves()[rp.getLastLoadedSPlayerId()]);
		return rp;
	}
}
