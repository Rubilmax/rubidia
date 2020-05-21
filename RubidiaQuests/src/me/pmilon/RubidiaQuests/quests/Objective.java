package me.pmilon.RubidiaQuests.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.events.RPlayerObjectiveEvent;
import me.pmilon.RubidiaQuests.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Objective {

	private String questUUID;
	private int index;
	private String name;
	private ObjectiveType type;
	private ItemStack itemStack;
	private Material material;
	private int amount;
	private Location location;
	private String infoName;
	private String monsterUUID;
	private String targetUUID;
	private String sidequestUUID;
	private boolean takeItem;
	private List<String> dialogs;
	private HashMap<String, Long[]> scores;
	private List<RPlayer> checker = new ArrayList<RPlayer>();
	
	private boolean modified;
	private RItem rItem = null;

	public Objective(QuestColl coll, String questUUID, int index, ObjectiveType type, ItemStack itemStack, Material material, String monsterUUID, Location location, String infoName, String targetUUID, String sidequestUUID, String name, int amount, boolean takeItem, List<String> dialogs, HashMap<String, Long[]> scores, boolean modified){
		this.questUUID = questUUID;
		this.index = index;
		this.type = type;
		this.itemStack = itemStack;
		this.material = material;
		this.amount = amount;
		this.location = location;
		this.infoName = infoName;
		this.monsterUUID = monsterUUID;
		this.targetUUID = targetUUID;
		this.sidequestUUID = sidequestUUID;
		this.name = name;
		this.scores = scores;
		this.takeItem = takeItem;
		this.dialogs = dialogs;
		this.modified = modified;
		if(this.getType().equals(ObjectiveType.TIME)){
			coll.timedObjectives.add(this);
		}
	}
	
	public boolean check(RPlayer rplayer, Block block){
		if(this.isAvailable(rplayer)){
			if(this.getType().equals(ObjectiveType.MINE)){
				if(this.getMaterial().equals(block.getType())){
					int score = (int)this.getScore(rplayer);
					if(score > 0){
						this.setScore(rplayer, score-1);
					}
					RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
					Bukkit.getPluginManager().callEvent(event);
					return true;
				}
			}
		}
		return false;
	}
	public void check(RPlayer rplayer, ItemStack stack){
		if(rItem == null)rItem = new RItem(this.getItemStack());
		if(this.isAvailable(rplayer)){
			if(this.getItemStack().isSimilar(stack) || (rItem.isWeapon() && stack.getType().equals(this.getItemStack().getType()))){
				int score = (int)this.getScore(rplayer);
				if(score > 0){
					this.setScore(rplayer, score-stack.getAmount());
				}
				RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
				Bukkit.getPluginManager().callEvent(event);
			}
		}
	}
	public boolean check(RPlayer rplayer, Monster monster){
		if(this.isAvailable(rplayer)){
			if(this.getType().equals(ObjectiveType.KILL)){
				if(this.getMonsterUUID().equals(monster.getUUID())){
					int score = (int) this.getScore(rplayer);
					if(score > 0){
						this.setScore(rplayer, score-1);
					}
					RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
					Bukkit.getPluginManager().callEvent(event);
					return true;
				}
			}
		}
		return false;
	}
	public boolean check(final RPlayer rplayer, Location location){
		if(this.isAvailable(rplayer)){
			if(this.getType().equals(ObjectiveType.DISCOVER) || this.getType().equals(ObjectiveType.FOLLOW)){
				if(!this.checker.contains(rplayer)){
					this.checker.add(rplayer);
					new BukkitTask(QuestsPlugin.instance){
						public void run(){
							checker.remove(rplayer);
						}

						@Override
						public void onCancel() {
						}
					}.runTaskLater(0);
					if(this.getLocation().getWorld().equals(location.getWorld())){
						if(this.getLocation().distanceSquared(location) < 44){
							int score = (int) this.getScore(rplayer);
							if(score > 0){
								this.setScore(rplayer, score-1);
							}
							RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
							Bukkit.getPluginManager().callEvent(event);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public boolean check(RPlayer rplayer, String targetUUID){
		if(this.isAvailable(rplayer)){
			if(this.getTargetUUID().equals(targetUUID)){
				if(this.getType().equals(ObjectiveType.TALK)){
					int score = (int) this.getScore(rplayer);
					if(score > 0){
						this.setScore(rplayer, score-1);
					}
					RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
					Bukkit.getPluginManager().callEvent(event);
					return true;
				}else if(this.getType().equals(ObjectiveType.SIDE_QUEST)){
					Quest quest = this.getSideQuest();
					if(quest.hasFinished(rplayer)){
						int score = (int) this.getScore(rplayer);
						if(score > 0){
							this.setScore(rplayer, score-1);
						}
					}
					RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
					Bukkit.getPluginManager().callEvent(event);
					return true;//to send update even when he talks for the first time and when he comes back
				}else if(this.getType().equals(ObjectiveType.LEASH)){
					if(rplayer.isOnline()){
						int score = (int) this.getScore(rplayer);
						if(score > 0){
							for(Entity entity : rplayer.getPlayer().getNearbyEntities(8, 8, 8)){
								if (entity instanceof LivingEntity) {
									LivingEntity living = (LivingEntity) entity;
									if(living.isLeashed()){
										if(living.getLeashHolder().equals(rplayer.getPlayer())){
											Monster monster = Monsters.get(living);
											if(monster != null){
												if(this.getMonsterUUID().equals(monster.getUUID()) && monster.getTamer() != null){
													if(monster.getTamer().equals(rplayer.getPlayer())){
														this.setScore(rplayer, this.getScore(rplayer)-1);
														monster.setTamer(null);
														monster.kill(true);
														if(this.getScore(rplayer) <= 0)break;
													}
												}
											}
										}
									}
								}
							}
							RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
							Bukkit.getPluginManager().callEvent(event);
						}
						if(score != this.getScore(rplayer))return true;
					}
				}else if(this.getType().equals(ObjectiveType.GET)){
					if(rplayer.isOnline()){
						Player player = rplayer.getPlayer();
						int score = (int) this.getScore(rplayer);
						if(score > 0){
							for(int slot = 0;slot < player.getInventory().getSize();slot++){
								ItemStack item = player.getInventory().getItem(slot);
								if(item != null){
									if(item.isSimilar(this.getItemStack())){
										int amount = (int) (item.getAmount()-this.getScore(rplayer));
										int newScore = (int) this.getScore(rplayer);
										if(amount > 0){
											newScore = 0;
											item.setAmount(amount);
										}else{
											newScore -= item.getAmount();
											item = new ItemStack(Material.AIR);
										}
										this.setScore(rplayer, newScore);
										
										if(this.isTakeItem()){
											player.getInventory().setItem(slot, item);
										}
									}
								}
							}
							RPlayerObjectiveEvent event = new RPlayerObjectiveEvent(rplayer, this.getQuest(), this);
							Bukkit.getPluginManager().callEvent(event);
							Utils.updateInventory(player);
						}
						if(score != this.getScore(rplayer) || score <= 0)return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isFilled(RPlayer rplayer){
		if(this.getType().equals(ObjectiveType.TIME))return true;
		return this.getScore(rplayer) < 1;
	}
	
	public String getInfos(RPlayer rplayer){
		boolean monsterColor = false;
		if(this.getType().equals(ObjectiveType.LEASH) && rplayer.isOnline()){
			for(Entity entity : rplayer.getPlayer().getNearbyEntities(8, 8, 8)){
				if (entity instanceof LivingEntity) {
					LivingEntity living = (LivingEntity) entity;
					if(living.isLeashed()){
						if(living.getLeashHolder().equals(rplayer.getPlayer())){
							Monster monster = Monsters.get(living);
							if(monster != null){
								if(monster.getUUID().equals(monster.getUUID()) && monster.getTamer() != null){
									if(monster.getTamer().equals(rplayer.getPlayer())){
										monsterColor = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		String prefix = "";
		if(this.getType().equals(ObjectiveType.TIME))prefix = "§6[⧗] §e";
		else if(this.isFilled(rplayer))prefix = "§2[✔] §a";
		else if(this.getType().equals(ObjectiveType.SIDE_QUEST) && rplayer.getActiveQuests().contains(this.getSideQuest()))prefix = "§5[▶] §d";
		else if(this.getType().equals(ObjectiveType.GET) && rplayer.getPlayer().getInventory().containsAtLeast(this.getItemStack(), 1) || monsterColor)prefix = "§5[$] §d";
		else prefix = "§4[✘] §c";
		String toDo = this.getType().equals(ObjectiveType.SIDE_QUEST) && rplayer.getActiveQuests().contains(this.getSideQuest()) ? "Aider " : this.getType().getToDo();
		String goal = this.getType().equals(ObjectiveType.TIME) ? "" : this.getInformation() + " ";
		String score = this.getType().equals(ObjectiveType.TIME) ? this.convertTimeToString((((this.getScore(rplayer)+this.getAmount()*1000L)-System.currentTimeMillis())/1000L)) : "(" + (this.getDefaultScore()-this.getScore(rplayer)) + "/" + this.getDefaultScore() + ")";
		return prefix + toDo + goal + score;
	}
	
	public void save(boolean debug){
		if(this.isModified()){
			this.setModified(false);
			String path = this.getQuestPath() + "." + this.getIndex();
			Configs.getQuestsConfig().set(path + ".type", this.getType().toString());
			Configs.getQuestsConfig().set(path + ".itemStack", this.getItemStack());
			Configs.getQuestsConfig().set(path + ".material", this.getMaterial().toString());
			Configs.getQuestsConfig().set(path + ".amount", this.getAmount());
			Configs.getQuestsConfig().set(path + ".location", this.getLocation());
			Configs.getQuestsConfig().set(path + ".infoName", this.getInfoName());
			Configs.getQuestsConfig().set(path + ".monsterUUID", this.getMonsterUUID());
			Configs.getQuestsConfig().set(path + ".amount", this.getAmount());
			Configs.getQuestsConfig().set(path + ".targetUUID", this.getTargetUUID());
			Configs.getQuestsConfig().set(path + ".sidequestUUID", this.getSideQuestUUID());
			Configs.getQuestsConfig().set(path + ".takeItem", this.isTakeItem());
			Configs.getQuestsConfig().set(path + ".name", this.getName());
			Configs.getQuestsConfig().set(path + ".dialogs", this.getDialogs());
			for(String uuid : this.getScores().keySet()){
				for(int i = 0;i < 4;i++){
					Configs.getQuestsConfig().set(path + ".scores." + uuid + "." + i, this.getScores().get(uuid)[i]);
				}
			}
			if(debug)QuestsPlugin.console.sendMessage("    §6Objective §e" + this.getIndex() + " :: " + this.getType().toString());
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		this.setModified(true);
	}

	public ObjectiveType getType() {
		return type;
	}

	public void setType(ObjectiveType type) {
		this.type = type;
		this.setModified(true);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.rItem = new RItem(itemStack);
		this.setModified(true);
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
		this.setModified(true);
	}
	
	public String getTargetUUID() {
		return targetUUID;
	}
	
	public void setTargetUUID(String targetUUID) {
		this.targetUUID = targetUUID;
		this.setModified(true);
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
		this.setModified(true);
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
		this.setModified(true);
	}
	
	public HashMap<String, Long[]> getScores() {
		return scores;
	}
	
	public void setScores(HashMap<String, Long[]> scores) {
		this.scores = scores;
		this.setModified(true);
	}
	
	public void setScore(RPlayer rplayer, long score){
		Long[] scores = new Long[4];
		if(this.getScores().containsKey(rplayer.getUniqueId()))scores = this.getScores().get(rplayer.getUniqueId());
		scores[rplayer.getLastLoadedSPlayerId()] = score;
		this.getScores().put(rplayer.getUniqueId(), scores);
		this.setModified(true);
	}
	
	public long getScore(RPlayer rplayer){
		if(!this.getScores().containsKey(rplayer.getUniqueId())){
			Long[] scores = new Long[4];
			scores[rplayer.getLastLoadedSPlayerId()] = this.getDefaultScore();
			this.getScores().put(rplayer.getUniqueId(), scores);
		}
		return this.getScores().get(rplayer.getUniqueId())[rplayer.getLastLoadedSPlayerId()];
	}

	public void removeScore(RPlayer rplayer){
		if(this.scores.containsKey(rplayer.getUniqueId())){
			this.scores.remove(rplayer.getUniqueId());
		}
		Configs.getQuestsConfig().set(this.getQuestPath() + "." + this.getIndex() + ".scores." + rplayer.getUniqueId(), null);
		this.setModified(true);
	}
	
	public long getDefaultScore(){
		if(this.getType().equals(ObjectiveType.GET)){
			return this.getItemStack().getAmount();
		}else if(this.getType().equals(ObjectiveType.MINE) || this.getType().equals(ObjectiveType.KILL) || this.getType().equals(ObjectiveType.CRAFT) || this.getType().equals(ObjectiveType.FISH) || this.getType().equals(ObjectiveType.LEASH)){
			return this.getAmount();
		}else if(this.getType().equals(ObjectiveType.DISCOVER) || this.getType().equals(ObjectiveType.TALK) || this.getType().equals(ObjectiveType.SIDE_QUEST) || this.getType().equals(ObjectiveType.FOLLOW)){
			return 1;
		}else if(this.getType().equals(ObjectiveType.TIME)){
			return System.currentTimeMillis();
		}
		return 0;
	}
	
	public String getInformation() {
		if(this.getType().equals(ObjectiveType.GET)){
			return this.getItemStack().getAmount() + " " + this.getInfoName() + " à " + this.getName();
		}else if(this.getType().equals(ObjectiveType.LEASH)){
			String name = this.getMonster() == null ? "???" : this.getMonster().getName().toLowerCase();
			return this.getAmount() + " " + name + (this.getAmount() > 1 && !name.endsWith("s") && !name.endsWith("x") ? "s" : "") + " à " + this.getName();
		}else if(this.getType().equals(ObjectiveType.FOLLOW)){
			return this.getName() + " " + this.getInfoName();
		}else if(this.getType().equals(ObjectiveType.MINE) || this.getType().equals(ObjectiveType.CRAFT) || this.getType().equals(ObjectiveType.FISH)){
			return this.getAmount() + " " + this.getInfoName();
		}else if(this.getType().equals(ObjectiveType.DISCOVER)){
			return this.getInfoName();
		}else if(this.getType().equals(ObjectiveType.KILL)){
			String name = this.getMonster() == null ? "???" : this.getMonster().getName().toLowerCase();
			return this.getAmount() + " " + name + (this.getAmount() > 1 && !name.endsWith("s") && !name.endsWith("x") ? "s" : "");
		}else if(this.getType().equals(ObjectiveType.TALK)){
			return this.getName();
		}else if(this.getType().equals(ObjectiveType.TIME)){
			long time = this.getAmount();
			return this.convertTimeToString(time);
		}else if(this.getType().equals(ObjectiveType.SIDE_QUEST)){
			return this.getName();
		}
		return "";
	}
	
	public String convertTimeToString(long time){
		String duration = "";
		if(time >= 3600000){
			long hours = TimeUnit.SECONDS.toHours(time);
			time -= TimeUnit.HOURS.toSeconds(hours);
			long minutes = TimeUnit.SECONDS.toMinutes(time);
			time -= TimeUnit.MINUTES.toSeconds(minutes);
			long seconds = TimeUnit.SECONDS.toSeconds(time);
			time -= TimeUnit.SECONDS.toSeconds(seconds);
			duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}else{
			long minutes = TimeUnit.SECONDS.toMinutes(time);
			time -= TimeUnit.MINUTES.toSeconds(minutes);
			long seconds = TimeUnit.SECONDS.toSeconds(time);
			time -= TimeUnit.SECONDS.toSeconds(seconds);
			duration = String.format("%02d:%02d", minutes, seconds);
		}
		return duration;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		this.setModified(true);
	}
	
	public boolean isTakeItem() {
		return takeItem;
	}

	public void setTakeItem(boolean takeItem) {
		this.takeItem = takeItem;
		this.setModified(true);
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
		this.setModified(true);
	}
	
	public void delete(){
		String path = this.getQuestPath() + "." + this.getIndex();
		Configs.getQuestsConfig().set(path, null);
	}

	public List<String> getDialogs() {
		return dialogs;
	}

	public void setDialogs(List<String> dialogs) {
		this.dialogs = dialogs;
		this.setModified(true);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		this.getQuest().setObjectivesModified(true);
	}

	public String getQuestUUID() {
		return questUUID;
	}

	public void setQuestUUID(String questUUID) {
		this.questUUID = questUUID;
		this.setModified(true);
	}
	
	public Quest getQuest(){
		return Quest.get(this.getQuestUUID());
	}
	
	public String getQuestPath(){
		return "quests." + this.getQuest().getUUID() + ".objectives";
	}

	public String getMonsterUUID() {
		return monsterUUID;
	}

	public void setMonsterUUID(String monsterUUID) {
		this.monsterUUID = monsterUUID;
		this.setModified(true);
	}

	public Monster getMonster(){
		return Monsters.get(this.getMonsterUUID());
	}

	public boolean isAvailable(RPlayer rp){
		if(this.getQuest().isOrderedQuest()){
			for(int i = 0;i < this.getQuest().getObjectives().indexOf(this);i++){
				Objective objective = this.getQuest().getObjectives().get(i);
				if(!objective.isFilled(rp))return false;
			}
		}
		return true;
	}

	public String getSideQuestUUID() {
		return sidequestUUID;
	}

	public void setSideQuestUUID(String sidequestUUID) {
		this.sidequestUUID = sidequestUUID;
		this.setModified(true);
	}
	
	public Quest getSideQuest(){
		return Quest.get(this.getSideQuestUUID());
	}
}
