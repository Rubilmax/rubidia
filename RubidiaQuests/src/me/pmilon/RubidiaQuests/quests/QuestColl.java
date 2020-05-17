package me.pmilon.RubidiaQuests.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.utils.Configs;

public class QuestColl extends Database<String,Quest> {
	
	public final List<Objective> timedObjectives = new ArrayList<Objective>();
	
	public QuestColl(){
		if(Configs.getQuestsConfig().contains("quests")){
			for(String uuid : Configs.getQuestsConfig().getConfigurationSection("quests").getKeys(false)){
				List<Objective> objectives = new ArrayList<Objective>();
				if(Configs.getQuestsConfig().contains("quests." + uuid + ".objectives")){
					for(String objective : Configs.getQuestsConfig().getConfigurationSection("quests." + uuid + ".objectives").getKeys(false)){
						String path = "quests." + uuid + ".objectives." + objective;
						
						HashMap<String,Long[]> scores = new HashMap<String,Long[]>();
						if(Configs.getQuestsConfig().contains(path + ".scores")){
							for(String rpUUID : Configs.getQuestsConfig().getConfigurationSection(path + ".scores").getKeys(false)){
								Long[] scoress = new Long[4];
								for(int i = 0;i < 4;i++){
									scoress[i] = Configs.getQuestsConfig().getLong(path + ".scores." + rpUUID + "." + i);
								}
								Configs.getQuestsConfig().set(path + "." + rpUUID, null);
								scores.put(rpUUID, scoress);
							}
						}
						
						Material material;
						try {
							material = Material.valueOf(Configs.getQuestsConfig().getString(path + ".material"));
						} catch (Exception ex) {
							material = Material.STONE;
						}
						
						Objective obj = new Objective(this, uuid, Integer.valueOf(objective),
								ObjectiveType.valueOf(Configs.getQuestsConfig().getString(path + ".type")),
								Configs.getQuestsConfig().getItemStack(path + ".itemStack"),
								material,
								Configs.getQuestsConfig().getString(path + ".monsterUUID"),
								((Location) Configs.getQuestsConfig().get(path + ".location")),
								Configs.getQuestsConfig().getString(path + ".infoName"),
								Configs.getQuestsConfig().getString(path + ".targetUUID"),
								Configs.getQuestsConfig().getString(path + ".sidequestUUID"),
								Configs.getQuestsConfig().getString(path + ".name"),
								Configs.getQuestsConfig().getInt(path + ".amount"),
								Configs.getQuestsConfig().getBoolean(path + ".takeItem"),
								Configs.getQuestsConfig().getStringList(path + ".dialogs"),
								scores, false);
						objectives.add(obj);
					}
				}
				List<Reward> rewards = new ArrayList<Reward>();
				if(Configs.getQuestsConfig().contains("quests." + uuid + ".rewards")){
					for(String reward : Configs.getQuestsConfig().getConfigurationSection("quests." + uuid + ".rewards").getKeys(false)){
						String path = "quests." + uuid + ".rewards." + reward;
						try {
							RJob rJob = RJob.valueOf(Configs.getQuestsConfig().getString(path + ".rjob"));
							rewards.add(new Reward(uuid,
									Integer.valueOf(reward),
									RewardType.valueOf(Configs.getQuestsConfig().getString(path + ".type")),
									RClass.valueOf(Configs.getQuestsConfig().getString(path + ".rclass")),
									rJob,
									Configs.getQuestsConfig().getItemStack(path + ".itemStack"),
									Mastery.valueOf(Configs.getQuestsConfig().getString(path + ".mastery")),
									Configs.getQuestsConfig().getInt(path + ".amount"),
									Configs.getQuestsConfig().getDouble(path + ".rexp"),
									Configs.getQuestsConfig().getString(path + ".questUUID"),
									Configs.getQuestsConfig().getString(path + ".command"),
									Configs.getQuestsConfig().getString(path + ".infoName"),
									false));
						} catch(Exception e) {
							Configs.getQuestsConfig().set(path, null);
						}
					}
				}
				List<Required> requireds = new ArrayList<Required>();
				if(Configs.getQuestsConfig().contains("quests." + uuid + ".requireds")){
					for(String required : Configs.getQuestsConfig().getConfigurationSection("quests." + uuid + ".requireds").getKeys(false)){
						String path = "quests." + uuid + ".requireds." + required;
						try {
							RJob rJob = RJob.valueOf(Configs.getQuestsConfig().getString(path + ".rjob"));
							requireds.add(new Required(uuid,
									Integer.valueOf(required),
									RequiredType.valueOf(Configs.getQuestsConfig().getString(path + ".type")),
									Configs.getQuestsConfig().getItemStack(path + ".itemStack"),
									RClass.valueOf(Configs.getQuestsConfig().getString(path + ".rclass")),
									rJob,
									Configs.getQuestsConfig().getString(path + ".questUUID"),
									Configs.getQuestsConfig().getInt(path + ".level"),
									Mastery.valueOf(Configs.getQuestsConfig().getString(path + ".mastery")),
									Configs.getQuestsConfig().getLong(path + ".timeStart"),
									Configs.getQuestsConfig().getLong(path + ".timeEnd"),
									Configs.getQuestsConfig().getBoolean(path + ".dialog"),
									Configs.getQuestsConfig().getString(path + ".noDialog"),
									false));
						} catch(Exception e) {
							Configs.getQuestsConfig().set(path, null);
						}
					}
				}
				List<QEvent> qEvents = new ArrayList<QEvent>();
				if(Configs.getQuestsConfig().contains("quests." + uuid + ".qEvents")){
					for(String qEventUUID : Configs.getQuestsConfig().getConfigurationSection("quests." + uuid + ".qEvents").getKeys(false)){
						String path = "quests." + uuid + ".qEvents." + qEventUUID;
						List<Block> blocks = new ArrayList<Block>();
						for(String block : Configs.getQuestsConfig().getStringList(path + ".blocks")){
							String[] part = block.split(",");
							if(part.length > 3){
								World world = Bukkit.getWorld(part[0]);
								blocks.add(new Location(world, Double.valueOf(part[1]), Double.valueOf(part[2]), Double.valueOf(part[3])).getBlock());
							}
						}
						ItemStack itemStack;
						if(Configs.getQuestsConfig().contains(path + ".itemStack")){
							itemStack = Configs.getQuestsConfig().getItemStack(path + ".itemStack");
						}else itemStack = new ItemStack(Material.STONE,1);
						QEvent qEvent = new QEvent(uuid,
								Integer.valueOf(qEventUUID),
								QEventType.valueOf(Configs.getQuestsConfig().getString(path + ".type")),
								Configs.getQuestsConfig().getInt(path + ".amount"),
								Configs.getQuestsConfig().getDouble(path + ".range"),
								(Location)Configs.getQuestsConfig().get(path + ".location"),
								Configs.getQuestsConfig().getString(path + ".monsterUUID"),
								Configs.getQuestsConfig().getInt(path + ".monsterLevel"),
								(PotionEffect)Configs.getQuestsConfig().get(path + ".potionEffect"),
								blocks, itemStack, false);
						qEvent.setItemStack(itemStack);
						qEvents.add(qEvent);
					}
				}
				HashMap<String, String[]> holders = new HashMap<String, String[]>();
				if(Configs.getQuestsConfig().contains("quests." + uuid + ".holders")){
					for(String rpUUID : Configs.getQuestsConfig().getConfigurationSection("quests." + uuid + ".holders").getKeys(false)){
						String[] holderss = new String[4];
						for(int i = 0;i < 4;i++){
							holderss[i] = Configs.getQuestsConfig().getString("quests." + uuid + ".holders." + rpUUID + "." + i);
						}
						holders.put(rpUUID, holderss);
					}
				}
				Quest quest = new Quest(Configs.getQuestsConfig().contains("quests." + uuid + ".type") ? QuestType.valueOf(Configs.getQuestsConfig().getString("quests." + uuid + ".type")) : QuestType.NORMAL,
						uuid,
						Configs.getQuestsConfig().getString("quests." + uuid + ".title").trim(),
						Configs.getQuestsConfig().getString("quests." + uuid + ".subtitle").trim(),
						objectives, rewards, requireds, qEvents,
						Configs.getQuestsConfig().getStringList("quests." + uuid + ".preDialogs"),
						Configs.getQuestsConfig().getStringList("quests." + uuid + ".postDialogs"),
						Configs.getQuestsConfig().getStringList("quests." + uuid + ".nonDialogs"),
						Configs.getQuestsConfig().getBoolean("quests." + uuid + ".autoFinish"),
						Configs.getQuestsConfig().getBoolean("quests." + uuid + ".redonable"),
						Configs.getQuestsConfig().getBoolean("quests." + uuid + ".giveupable"),
						Configs.getQuestsConfig().getBoolean("quests." + uuid + ".orderedQuest"),
						Configs.getQuestsConfig().getBoolean("quests." + uuid + ".unique"), holders);
				this.load(uuid,quest);
				if(this.size()%50 == 0){
					QuestsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §eQUESTS");
				}
			}
		}
		QuestsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §eQUESTS");
	}
	
	public List<Objective> getTimedObjectives(){
		return timedObjectives;
	}

	@Override
	protected Quest getDefault(String uuid) {
		List<Objective> objectives = new ArrayList<Objective>();
		List<Reward> rewards = new ArrayList<Reward>();
		List<Required> requireds = new ArrayList<Required>();
		List<QEvent> qEvents = new ArrayList<QEvent>();
		List<String> empty1 = new ArrayList<String>();
		List<String> empty2 = new ArrayList<String>();
		List<String> empty3 = new ArrayList<String>();
		Quest quest = new Quest(QuestType.NORMAL,uuid, "Title", "Subtitle", objectives, rewards, requireds, qEvents, empty1, empty2, empty3, false, false, true, false, false, new HashMap<String, String[]>());
		requireds.add(new Required(quest.getUUID(), 0, RequiredType.LEVEL, new ItemStack(Material.STONE, 1), RClass.VAGRANT, RJob.JOBLESS, quest.getUUID(), 0, Mastery.ADVENTURER, 0L, 0L, false, "Required not filled", true));
		quest.setRequireds(requireds);
		quest.setModified(true);
		return quest;
	}

	@Override
	protected void onSaveEnd(boolean arg0) {
		Configs.saveQuestsConfig();
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) QuestsPlugin.console.sendMessage("§a   Saving Quests...");
	}

	@Override
	protected void save(boolean debug, Quest quest) {
		if(quest.isModified()){
			quest.setModified(false);
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".type", quest.getType().toString());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".title", quest.getTitle());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".subtitle", quest.getSubtitle());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".preDialogs", quest.getPreDialogs());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".postDialogs", quest.getPostDialogs());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".nonDialogs", quest.getNonDialogs());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".autoFinish", quest.isAutoFinish());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".redonable", quest.isRedonable());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".giveupable", quest.isGiveupable());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".main", quest.isMain());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".orderedQuest", quest.isOrderedQuest());
			Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".unique", quest.isUnique());
			for(String rpUUID : quest.getHolders().keySet()){
				for(int i = 0;i < 4;i++){
					Configs.getQuestsConfig().set("quests." + quest.getUUID() + ".holders." + rpUUID + "." + i, quest.getHolders().get(rpUUID)[i]);
				}
			}
			if(debug)QuestsPlugin.console.sendMessage(" §6Saved §e" + quest.getColoredTitle());
		}
		if(quest.isObjectivesModified()){
			quest.setObjectivesModified(false);
			for(Objective objective : quest.getObjectives()){
				objective.save(debug);
			}
		}
		if(quest.isRewardsModified()){
			quest.setRewardsModified(false);
			for(Reward reward : quest.getRewards()){
				reward.save(debug);
			}
		}
		if(quest.isRequiredsModified()){
			quest.setRequiredsModified(false);
			for(Required required : quest.getRequireds()){
				required.save(debug);
			}
		}
		for(QEvent qEvent : quest.getQEvents()){
			qEvent.save(debug);
		}
	}
	
}
