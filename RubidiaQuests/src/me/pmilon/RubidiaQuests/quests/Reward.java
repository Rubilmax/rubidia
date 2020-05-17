package me.pmilon.RubidiaQuests.quests;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RXPSource;
import me.pmilon.RubidiaCore.events.RXPSourceType;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaCore.levels.RLevels;

import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.utils.Configs;
import me.pmilon.RubidiaQuests.utils.Utils;

public class Reward {

	private int index;
	private String questUUID;
	private RewardType type;
	private RClass rclass;
	private RJob rjob;
	private Mastery mastery;
	private ItemStack itemStack;
	private int amount;
	private double rexp;
	private String questTargetUUID;
	private String command;
	private String infoName;
	
	private boolean modified;
	
	public Reward(String questUUID, int index, RewardType type, RClass rclass, RJob rjob, ItemStack itemStack, Mastery mastery, int amount, double rexp, String questTargetUUID, String command, String infoName, boolean modified){
		this.questUUID = questUUID;
		this.index = index;
		this.type = type;
		this.rclass = rclass;
		this.rjob = rjob;
		this.itemStack = itemStack;
		this.amount = amount;
		this.rexp = rexp;
		this.questTargetUUID = questTargetUUID;
		this.mastery = mastery;
		this.command = command;
		this.infoName = infoName;
		this.modified = modified;
	}
	
	public void save(boolean debug){
		if(this.isModified()){
			this.setModified(false);
			String path = this.getQuestPath() + "." + this.getIndex();
			Configs.getQuestsConfig().set(path + ".type", this.getType().toString());
			Configs.getQuestsConfig().set(path + ".rclass", this.getRClass().toString());
			Configs.getQuestsConfig().set(path + ".rjob", this.getRJob().toString());
			Configs.getQuestsConfig().set(path + ".itemStack", this.getItemStack());
			Configs.getQuestsConfig().set(path + ".rexp", this.getRExp());
			Configs.getQuestsConfig().set(path + ".amount", this.getAmount());
			Configs.getQuestsConfig().set(path + ".mastery", this.getMastery().toString());
			Configs.getQuestsConfig().set(path + ".questUUID", this.getQuestTargetUUID());
			Configs.getQuestsConfig().set(path + ".command", this.getCommand());
			Configs.getQuestsConfig().set(path + ".infoName", this.getInfoName());
			if(debug)QuestsPlugin.console.sendMessage("    §6Reward §e" + this.getIndex() + " :: " + this.getType().toString());
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		this.setModified(true);
	}

	public RewardType getType() {
		return type;
	}

	public void setType(RewardType type) {
		this.type = type;
		this.setModified(true);
	}

	public RClass getRClass() {
		return rclass;
	}

	public void setRClass(RClass rclass) {
		this.rclass = rclass;
		this.setModified(true);
	}

	public RJob getRJob() {
		return rjob;
	}

	public void setRJob(RJob rjob) {
		this.rjob = rjob;
		this.setModified(true);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.setModified(true);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
		this.setModified(true);
	}
	
	public Quest getQuestTarget(){
		return Quest.get(this.getQuestTargetUUID());
	}
	
	public String getQuestTargetUUID() {
		return questTargetUUID;
	}
	
	public void setQuestTargetUUID(String questTargetUUID) {
		this.questTargetUUID = questTargetUUID;
		this.setModified(true);
	}
	
	public double getRExp(){
		return this.rexp;
	}
	
	public void setRExp(double  rexp){
		this.rexp = rexp;
		this.setModified(true);
	}
	
	public void make(final RPlayer rp){
		if(this.getType().equals(RewardType.CLASS)){
			rp.setRClass(this.getRClass());
			rp.sendMessage("§2[+] §aVous êtes désormais " + rp.getClassName());
		}else if(this.getType().equals(RewardType.JOB)){
			rp.setRJob(this.getRJob());
			rp.sendMessage("§2[+] §aVous êtes désormais " + rp.getRJob().getName());
		}else if(this.getType().equals(RewardType.ITEM)){
			if(rp.isOnline()){
				rp.getPlayer().getInventory().addItem(this.getItemStack());
				rp.getPlayer().sendMessage("§2[+] §a" + this.getItemStack().getAmount() + " " + this.getInfoName());
			}
		}else if(this.getType().equals(RewardType.XP)){
			rp.addRExp(this.getRExp(), new RXPSource(RXPSourceType.QUEST, null, null));
			if(rp.isOnline())rp.getPlayer().sendMessage("§2[+] §a" + this.getRExp() + " XP");
		}else if(this.getType().equals(RewardType.MONEY)){
			rp.sendMessage("§2[+] §a" + this.getAmount() + " émeraudes");
			if(rp.isOnline())EconomyHandler.deposit(rp.getPlayer(), this.getAmount());
		}else if(this.getType().equals(RewardType.SKP)){
			rp.setSkillPoints(rp.getSkillPoints()+this.getAmount());
			rp.sendMessage("§2[+] §a" + this.getAmount() + " point" + (this.getAmount() > 1 ? "s" : "") + " de compétence");
		}else if(this.getType().equals(RewardType.SKD)){
			rp.setSkillDistinctionPoints(rp.getSkillDistinctionPoints()+this.getAmount());
			rp.sendMessage("§2[+] §a" + this.getAmount() + " point" + (this.getAmount() > 1 ? "s" : "") + " de distinction");
		}else if(this.getType().equals(RewardType.MASTERY)){
			rp.setMastery(this.getMastery());
			rp.sendMessage("§2[+] §aVous êtes désormais " + rp.getClassName());
		}else if(this.getType().equals(RewardType.QUEST)){
			if(rp.getFollowedQuest() != null){
				if(rp.getFollowedQuest().equals(this.getQuest())){
					rp.setFollowedQuest(this.getQuestTarget());
				}
			}
			final PNJHandler pnj = DialogManager.getDialog(rp.getPlayer()).getPnj();
			PNJDialog dialog = new PNJDialog(rp.getPlayer(), pnj, DialogManager.getDialog(rp.getPlayer()).getTalker(), getQuestTarget().getPreDialogs(), DialogType.AMBIENT, new Runnable(){
				public void run(){
					getQuestTarget().accept(rp, pnj.getUniqueId());
					if(rp.isOnline())Utils.updateFollowedQuest(rp.getPlayer(), true);
				}
			}, false, true);
			dialog.start();
		}else if(this.getType().equals(RewardType.COMMAND)){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.getCommand().replaceAll("%player", rp.getName()));
		}
	}
	public String getQuestUUID() {
		return questUUID;
	}
	public void setQuestUUID(String questUUID) {
		this.questUUID = questUUID;
		this.setModified(true);
	}

	public Mastery getMastery() {
		return mastery;
	}

	public void setMastery(Mastery mastery) {
		this.mastery = mastery;
		this.setModified(true);
	}

	public String getInformation() {
		if(this.getType().equals(RewardType.CLASS))return this.getRClass().toString();
		else if(this.getType().equals(RewardType.ITEM))return this.getItemStack().getAmount() + " " + this.getInfoName();
		else if(this.getType().equals(RewardType.JOB))return this.getRJob().toString();
		else if(this.getType().equals(RewardType.MASTERY))return this.getMastery().toString();
		else if(this.getType().equals(RewardType.MONEY) || this.getType().equals(RewardType.SKP) || this.getType().equals(RewardType.SKD))return String.valueOf(this.getAmount());
		else if(this.getType().equals(RewardType.QUEST)){
			if(this.getQuestTarget() == null)this.setQuestTargetUUID(this.getQuestUUID());
			return this.getQuestTarget().getColoredTitle();
		}
		else if(this.getType().equals(RewardType.XP)) {
			int level = 0;
			for(Required rq : this.getQuest().getRequireds()){
				if(rq.getType().equals(RequiredType.LEVEL)){
					level = rq.getLevel();
					break;
				}
			}
			return String.valueOf(this.getRExp()) + " XP (" + me.pmilon.RubidiaCore.utils.Utils.round(this.getRExp()*100/RLevels.getRLevelTotalExp(level), 2) + "% du niveau " + level + ")";
		}
		else if(this.getType().equals(RewardType.COMMAND))return this.getCommand();
		return "";
	}
	
	public String getInfos(){
		String reward= "§6[+] §e";
		if(this.getType().equals(RewardType.CLASS))reward += "Classe : " + this.getInformation();
		else if(this.getType().equals(RewardType.ITEM))reward += "Item : " + this.getInformation();
		else if(this.getType().equals(RewardType.JOB))reward += "Métier : " + this.getInformation();
		else if(this.getType().equals(RewardType.MASTERY))reward += "Spécialisation : " + this.getInformation();
		else if(this.getType().equals(RewardType.MONEY))reward += this.getInformation() + " émeraudes";
		else if(this.getType().equals(RewardType.QUEST))reward += "Quête : " + this.getInformation();
		else if(this.getType().equals(RewardType.SKP))reward += this.getInformation() + " points de compétence";
		else if(this.getType().equals(RewardType.SKD))reward += this.getInformation() + " points de distinction";
		else if(this.getType().equals(RewardType.XP))reward += this.getInformation() + " XP";
		return reward;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
		this.setModified(true);
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
		this.setModified(true);
	}

	public void delete() {
		String path = this.getQuestPath() + "." + this.getIndex();
		Configs.getQuestsConfig().set(path, null);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		this.getQuest().setRewardsModified(true);
	}
	
	public Quest getQuest(){
		return Quest.get(this.getQuestUUID());
	}
	
	public String getQuestPath(){
		return "quests." + this.getQuestUUID() + ".rewards";
	}
	
}
