package me.pmilon.RubidiaQuests.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.levels.RLevels;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.events.RPlayerAcceptQuestEvent;
import me.pmilon.RubidiaQuests.events.RPlayerFinishQuestEvent;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PNJSession;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.utils.Configs;
import me.pmilon.RubidiaQuests.utils.Utils;

public class Quest extends RequiredHolder {

	private QuestType type;
	private String uuid;
	private String title;
	private String subtitle;
	private List<Objective> objectives;
	private List<Reward> rewards;
	private List<QEvent> qEvents;
	private List<String> preDialogs;
	private List<String> postDialogs;
	private List<String> nonDialogs;
	private HashMap<String, String[]> holders;
	private boolean autoFinish;
	private boolean redonable;
	private boolean giveupable;
	private boolean orderedQuest;
	private boolean unique;

	private boolean objectivesModified = false;
	private boolean rewardsModified = false;
	
	public Quest(QuestType type,String uuid, String title, String subtitle, List<Objective> objectives,
	List<Reward> rewards, List<Required> requireds, List<QEvent> qEvents, List<String> preDialogs, List<String> postDialogs, List<String> nonDialogs, boolean autoFinish, boolean redonable, boolean giveupable, boolean ordered, boolean unique, HashMap<String, String[]> holders)
	{
		super(uuid, requireds);
		this.type = type;
		this.uuid = uuid;
		this.title = title;
		this.subtitle = subtitle;
		this.objectives = objectives;
		this.rewards = rewards;
		this.setRequireds(requireds);
		this.qEvents = qEvents;
		this.preDialogs = preDialogs;
		this.postDialogs = postDialogs;
		this.nonDialogs = nonDialogs;
		this.autoFinish = autoFinish;
		this.redonable = redonable;
		this.giveupable = giveupable;
		this.orderedQuest = ordered;
		this.unique = unique;
		this.holders = holders;
	}

	public static Quest get(String uuid){
		return QuestsPlugin.questColl.get(uuid);
	}
	
	public static Quest newDefault(){
		return QuestsPlugin.questColl.addDefault(UUID.randomUUID().toString());
	}
	
	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
		this.setModified(true);
	}

	public String getTitle() {
		return title;
	}

	public String getColoredTitle() {
		return this.getType().getDarkColor() + title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.setModified(true);
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getColoredSubtitle() {
		return this.getType().getLightColor() + subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		this.setModified(true);
	}

	public List<Objective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Objective> objectives) {
		this.objectives = objectives;
		this.setObjectivesModified(true);
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
		this.setRewardsModified(true);
	}

	public List<String> getPreDialogs() {
		return preDialogs;
	}

	public void setPreDialogs(List<String> preDialogs) {
		this.preDialogs = preDialogs;
		this.setModified(true);
	}

	public List<String> getPostDialogs() {
		return postDialogs;
	}

	public void setPostDialogs(List<String> postDialogs) {
		this.postDialogs = postDialogs;
		this.setModified(true);
	}

	public List<String> getNonDialogs() {
		return nonDialogs;
	}

	public void setNonDialogs(List<String> nonDialogs) {
		this.nonDialogs = nonDialogs;
		this.setModified(true);
	}

	public List<Objective> getObjectivesByType(ObjectiveType... types){
		List<Objective> objectives = new ArrayList<Objective>();
		for(int i = 0;i < types.length;i++){
			for(Objective objective : this.getObjectives()){
				if(objective.getType().equals(types[i])){
					objectives.add(objective);
				}
			}
		}
		return objectives;
	}
	
	public List<QEvent> getQEventsByType(QEventType... types){
		List<QEvent> events = new ArrayList<QEvent>();
		for(int i = 0;i < types.length;i++){
			for(QEvent event : this.getQEvents()){
				if(event.getType().equals(types[i])){
					events.add(event);
				}
			}
		}
		return events;
	}

	public boolean check(RPlayer rplayer, Block block){
		boolean returnement = false;
		for(Objective objective : this.getObjectivesByType(ObjectiveType.MINE)){
			if(!objective.isFilled(rplayer)){
				int score = (int) objective.getScore(rplayer);
				if(objective.check(rplayer, block) && !returnement)returnement = true;;
				if(score != objective.getScore(rplayer))this.sendUpdate(objective, rplayer);
			}
		}
		if(this.isAutoFinish())this.checkIsAutoFinish(rplayer);
		return returnement;
	}
	public void check(RPlayer rplayer, ItemStack stack){
		for(Objective objective : this.getObjectivesByType(ObjectiveType.CRAFT, ObjectiveType.FISH)){
			if(!objective.isFilled(rplayer)){
				int score = (int) objective.getScore(rplayer);
				objective.check(rplayer, stack);
				if(score != objective.getScore(rplayer))this.sendUpdate(objective, rplayer);
			}
		}
		if(this.isAutoFinish())this.checkIsAutoFinish(rplayer);
	}
	public boolean check(RPlayer rplayer, Monster monster){
		boolean returnement = false;
		for(Objective objective : this.getObjectivesByType(ObjectiveType.KILL)){
			if(!objective.isFilled(rplayer)){
				int score = (int) objective.getScore(rplayer);
				if(objective.check(rplayer, monster) && !returnement)returnement = true;
				if(score != objective.getScore(rplayer))this.sendUpdate(objective, rplayer);
			}
		}
		if(this.isAutoFinish())this.checkIsAutoFinish(rplayer);
		return returnement;
	}
	public boolean check(RPlayer rplayer, Location location){
		boolean returnement = false;
		for(Objective objective : this.getObjectivesByType(ObjectiveType.DISCOVER, ObjectiveType.FOLLOW)){
			if(!objective.isFilled(rplayer)){
				int score = (int) objective.getScore(rplayer);
				if(objective.check(rplayer, location) && !returnement)returnement = true;
				if(score != objective.getScore(rplayer))this.sendUpdate(objective, rplayer);
			}
		}
		if(this.isAutoFinish())this.checkIsAutoFinish(rplayer);
		return returnement;
	}
	
	public void checkIsAutoFinish(final RPlayer rp){
		if(this.hasFinished(rp)){
			finish(rp);
		}
	}

	public boolean hasObjectiveTalkTo(String targetUUID){
		for(Objective objective : this.getObjectives()){
			if(objective.getType().equals(ObjectiveType.TALK) || objective.getType().equals(ObjectiveType.GET)){
				if(objective.getTargetUUID() != null){
					if(objective.getTargetUUID().equals(targetUUID)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isAvailable(RPlayer rplayer){
		if(this.isUnique()){
			for(SPlayer sp : rplayer.getSaves()){
				if(sp != null){
					if(sp.getDoneQuests().contains(this)){
						return false;
					}
				}
			}
		}
		for(Required required : this.getRequireds()){
			if(!required.check(rplayer))return false;
		}
		return true;
	}
	
	public boolean accept(RPlayer rp, String handlerUUID){
		RPlayerAcceptQuestEvent event = new RPlayerAcceptQuestEvent(rp, this, handlerUUID);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			rp.getActiveQuests().add(this);
			for(QEvent qEvent : this.getQEvents()){
				qEvent.doEvent(rp);
			}
			for(Objective objective : this.getObjectives()){
				objective.setScore(rp, objective.getDefaultScore());
				if(objective.getType().equals(ObjectiveType.FOLLOW) && rp.isOnline()){
					PNJHandler handler = PNJManager.getPNJByUniqueId(objective.getTargetUUID());
					if(handler != null){
						handler.createFake(rp.getPlayer());
					}
				}
			}
			if(rp.isOnline()){
				rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2F);
				rp.sendTitle(this.getColoredTitle(), this.getColoredSubtitle(), 5, 100, 10);
			}
			this.setHolder(rp, handlerUUID);
		}
		return !event.isCancelled();
	}
	
	public void finish(RPlayer rp){
		RPlayerFinishQuestEvent event = new RPlayerFinishQuestEvent(rp, this);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			RPlayer rp1 = event.getRPlayer();
			Quest quest = event.getQuest();
			rp1.getActiveQuests().remove(quest);
			rp1.getDoneQuests().add(quest);
			for(Quest qst : rp1.getQuestsOfType(ObjectiveType.SIDE_QUEST)){
				for(Objective obj : qst.getObjectivesByType(ObjectiveType.SIDE_QUEST)){
					if(obj.getSideQuestUUID().equals(quest.getUUID())){
						obj.check(rp1, this.getHolderUUID(rp1));
					}
				}
			}
			
			if(rp1.isOnline()){
				rp1.getPlayer().playSound(rp1.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2F);
				rp1.sendTitle(quest.getColoredTitle(), ("§eQuête terminée !"), 5, 100, 10);
			}
			for(Objective objective : quest.getObjectives()){
				objective.removeScore(rp1);
			}
			for(Reward reward : quest.getRewards()){
				reward.make(rp1);
			}
			if(rp1.isOnline() && rp1.getFollowedQuest() != null){
				if(rp1.getFollowedQuest().equals(this)){
					Utils.updateFollowedQuest(rp1.getPlayer(), true);
				}
			}
			quest.setHolder(rp1, null);
		}
	}
	
	public boolean hasFinished(RPlayer rp){
		for(Objective objective : this.getObjectives()){
			if(!objective.isFilled(rp)){
				/*String uuid = this.getHolderUUID(rp);
				if(uuid != null){
					if(uuid.equals(objective.getTargetUUID())){
						if(objective.getType().equals(ObjectiveType.LEASH)){
							int amount = 0;
							for(LivingEntity entity : Core.toLivingEntityList(rp.getPlayer().getNearbyEntities(8, 8, 8))){
								if(entity.isLeashed()){
									if(entity.getLeashHolder().equals(rp.getPlayer())){
										Monster monster = Monsters.get(entity);
										if(monster != null){
											if(monster.getUUID().equals(monster.getUUID()) && monster.getTamer() != null){
												if(monster.getTamer().equals(rp.getPlayer())){
													amount++;
												}
											}
										}
									}
								}
							}
							if(amount >= objective.getAmount())continue;
						}
					}
				}*/
				return false;
			}
		}
		for(QEvent event : this.getQEvents()){
			if(event.getType().equals(QEventType.SPAWN)){
				if(event.monsters.containsKey(rp)){
					if(event.monsters.get(rp).size() > 0){
						return false;
					}
				}
			}
		}
		return true;
	}

	public void sendUpdate(Objective objective, RPlayer rp){
		if(rp.isOnline()){
			rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2F);
			rp.sendActionBar(this.getColoredTitle() + " §7| " + objective.getInfos(rp));
		}
	}

	public boolean isAutoFinish() {
		return autoFinish;
	}

	public void setAutoFinish(boolean autoFinish) {
		this.autoFinish = autoFinish;
		this.setModified(true);
	}

	public void delete(Objective objective) {
		objective.delete();
		this.getObjectives().remove(objective);
		this.setObjectivesModified(true);
	}

	public void delete(Reward reward) {
		reward.delete();
		this.getRewards().remove(reward);
		this.setRewardsModified(true);
	}
	
	public void delete(QEvent qEvent) {
		qEvent.delete();
		this.getQEvents().remove(qEvent);
	}

	public boolean isGiveupable(){
		return this.giveupable;
	}

	public boolean isRedonable() {
		return redonable;
	}

	public void setRedonable(boolean redonable) {
		this.redonable = redonable;
		this.setModified(true);
	}

	public void setGiveupable(boolean giveupable) {
		this.giveupable = giveupable;
		this.setModified(true);
	}

	public void delete() {
		QuestsPlugin.questColl.remove(this.getUUID());
		for(RPlayer rp : Core.rcoll.data()){
			List<Quest> actives = rp.getActiveQuests();
			List<Quest> done = rp.getDoneQuests();
			if(actives.contains(this)){
				actives.remove(this);
				rp.setActiveQuests(actives);
			}
			if(done.contains(this)){
				done.remove(this);
				rp.setDoneQuests(done);
			}
		}
		for(PNJSession session : PNJManager.pnjs.values()){
			if(session.getPNJHandler() instanceof QuestPNJ){
				QuestPNJ pnj = (QuestPNJ)session.getPNJHandler();
				List<Quest> quests2 = pnj.getQuests();
				if(quests2.contains(this)){
					quests2.remove(this);
					pnj.setQuests(quests2);
				}
			}
		}
		for(Quest quest : QuestsPlugin.questColl.data()){
			for(Reward reward : quest.getRewards()){
				if(reward.getQuestTargetUUID().equals(this.getUUID())){
					reward.delete();
					reward.getQuest().getRewards().remove(reward);
				}
			}
			for(Required required : quest.getRequireds()){
				if(required.getQuestTargetUUID().equals(this.getUUID())){
					required.delete();
					required.getQuest().getRequireds().remove(required);
				}
			}
		}
		Configs.getQuestsConfig().set("quests." + this.getUUID(), null);
	}

	public boolean isObjectivesModified() {
		return objectivesModified;
	}

	public void setObjectivesModified(boolean objectivesModified) {
		this.objectivesModified = objectivesModified;
	}

	public boolean isRewardsModified() {
		return rewardsModified;
	}

	public void setRewardsModified(boolean rewardsModified) {
		this.rewardsModified = rewardsModified;
	}

	public PNJHandler getHolder(RPlayer rp){
		if(this.getHolders().containsKey(rp.getUniqueId())){
			return PNJManager.getPNJByUniqueId(this.getHolders().get(rp.getUniqueId())[rp.getLastLoadedSPlayerId()]);
		}
		return null;
	}
	
	public String getHolderUUID(RPlayer rp){
		if(this.getHolders().containsKey(rp.getUniqueId())){
			return this.getHolders().get(rp.getUniqueId())[rp.getLastLoadedSPlayerId()];
		}
		return null;
	}
	
	public void setHolder(RPlayer rp, String pnjUUID){
		String[] holders = new String[4];
		if(this.getHolders().containsKey(rp.getUniqueId()))holders = this.getHolders().get(rp.getUniqueId());
		holders[rp.getLastLoadedSPlayerId()] = pnjUUID;
		this.getHolders().put(rp.getUniqueId(), holders);
		this.setModified(true);
	}

	public HashMap<String, String[]> getHolders() {
		return holders;
	}

	public void setHolders(HashMap<String, String[]> holders) {
		this.holders = holders;
		this.setModified(true);
	}

	public List<QEvent> getQEvents() {
		return qEvents;
	}

	public void setQEvents(List<QEvent> qEvents) {
		this.qEvents = qEvents;
	}

	public boolean isMain() {
		return this.getType().equals(QuestType.STORY);
	}

	public boolean isOrderedQuest() {
		return orderedQuest;
	}

	public void setOrderedQuest(boolean orderedQuest) {
		this.orderedQuest = orderedQuest;
		this.setModified(true);
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public List<Quest> giveUp(RPlayer rp){
		List<Quest> quests = new ArrayList<Quest>();
		rp.getActiveQuests().remove(this);
		quests.add(this);
		for(Objective objective : this.getObjectivesByType(ObjectiveType.SIDE_QUEST)){
			Quest squest = objective.getSideQuest();
			if(squest != null){
				quests.addAll(squest.giveUp(rp));
			}
		}
		return quests;
	}

	public String getProblem(){
		for(Reward reward : this.getRewards()){
			if(reward.getType().equals(RewardType.ITEM)){
				ItemStack stack = reward.getItemStack();
				if(stack == null)return "Reward : no itemstack";
				else{
					Material type = stack.getType();
					if(type.equals(Material.SHIELD)
							|| type.toString().contains("_SWORD")
							|| type.toString().contains("_AXE")
							|| type.toString().contains("_HOE")
							|| type.equals(Material.BOW)
							|| type.toString().contains("_HELMET")
							|| type.toString().contains("_CHESTPLATE")
							|| type.toString().contains("_LEGGINGS")
							|| type.toString().contains("_BOOTS")){
						RItem rItem = new RItem(stack);
						if(!rItem.isWeapon()){
							return "Reward: old weapon";
						}
					}
				}
			}else if(reward.getType().equals(RewardType.XP)){
				boolean requiredXP = false;
				for(Required required : this.getRequireds()){
					if(required.getType().equals(RequiredType.LEVEL)){
						requiredXP = true;
						int level = required.getLevel();
						double ratio = reward.getRExp()/RLevels.getRLevelTotalExp(level);
						if(ratio < .03){
							return "Reward: XP < 3% of level " + level;
						} else if(ratio > 1) {
							return "Reward: XP > 100% of level " + level;
						}
					}
				}
				if(!requiredXP)return "Required: type level needed";
			}
		}
		for(QEvent event : this.getQEvents()){
			if(event.getType().equals(QEventType.SPAWN)){
				Monster monster = event.getMonster();
				if(monster.isAverage())return "QEvent: average monster spawned";
				for(Region region : Regions.regions){
					if(region.getMonsters().contains(monster)){
						return "QEvent: unspecific monster spawned";
					}
				}
			}
		}
		return "";
	}

	public QuestType getType() {
		return type;
	}

	public void setType(QuestType type) {
		this.type = type;
		this.setModified(true);
	}
}
