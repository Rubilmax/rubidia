package me.pmilon.RubidiaQuests.quests;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.utils.Configs;

public class Required {

	private String holderUUID;
	private int index;
	private RequiredType type;
	private ItemStack itemStack;
	private RClass rclass;
	private RJob rjob;
	private Mastery mastery;
	private String questTargetUUID;
	private int level;
	private Long timeStart;
	private Long timeEnd;
	private boolean dialog;
	private String noDialog;
	
	private boolean modified;
	
	public Required(String holderUUID, int index, RequiredType type, ItemStack itemStack, RClass rclass, RJob rjob, String questTargetUUID, int level, Mastery mastery, Long timeStart, Long timeEnd, boolean dialog, String noDialog, boolean modified){
		this.holderUUID = holderUUID;
		this.index = index;
		this.type= type;
		this.itemStack = itemStack;
		this.rclass = rclass;
		this.rjob = rjob;
		this.questTargetUUID = questTargetUUID;
		this.mastery = mastery;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.dialog = dialog;
		this.noDialog = noDialog;
		this.level = level;
		this.modified = modified;
	}

	public boolean check(RPlayer rplayer){
		if(this.getType().equals(RequiredType.LEVEL))return rplayer.getRLevel() >= this.getLevel();
		else if(this.getType().equals(RequiredType.CLASS))return this.getRclass().equals(rplayer.getRClass());
		else if(this.getType().equals(RequiredType.JOB))return this.getRjob().equals(rplayer.getRJob());
		else if(this.getType().equals(RequiredType.NON_JOB))return !this.getRjob().equals(rplayer.getRJob());
		else if(this.getType().equals(RequiredType.QUEST))return rplayer.getDoneQuests().contains(this.getQuestTarget());
		else if(this.getType().equals(RequiredType.MASTERY))return rplayer.isAtLeast(this.getMastery());
		else if(this.getType().equals(RequiredType.TIME)){
			if(rplayer.isOnline()){
				Long time = rplayer.getPlayer().getWorld().getTime();
				return time > this.getTimeStart() && time < this.getTimeEnd();
			}
		}else if(this.getType().equals(RequiredType.ITEM)){
			if(rplayer.isOnline()){
				Player player = rplayer.getPlayer();
				int amount = 0;
				for(int slot = 0;slot < player.getInventory().getSize();slot++){
					ItemStack item = player.getInventory().getItem(slot);
					if(item != null){
						if(item.isSimilar(this.getItemStack())){
							amount += item.getAmount();
						}
						if(amount > this.getItemStack().getAmount())return true;
					}
				}
				return false;
			}
		}
		else if(this.getType().equals(RequiredType.NON_ACTIVE_QUEST))return !rplayer.getActiveQuests().contains(this.getQuestTarget());
		else if(this.getType().equals(RequiredType.NON_DONE_QUEST))return !rplayer.getDoneQuests().contains(this.getQuestTarget());
		return true;
	}
	
	public void save(boolean debug){
		if(this.isModified()){
			this.setModified(false);
			String path = this.getQuestPath() + "." + this.getIndex();
			Configs.getQuestsConfig().set(path + ".type", this.getType().toString());
			Configs.getQuestsConfig().set(path + ".itemStack", this.getItemStack());
			Configs.getQuestsConfig().set(path + ".rclass", this.getRclass().toString());
			Configs.getQuestsConfig().set(path + ".rjob", this.getRjob().toString());
			Configs.getQuestsConfig().set(path + ".questUUID", this.getQuestTargetUUID());
			Configs.getQuestsConfig().set(path + ".level", this.getLevel());
			Configs.getQuestsConfig().set(path + ".mastery", this.getMastery().toString());
			Configs.getQuestsConfig().set(path + ".timeStart", this.getTimeStart());
			Configs.getQuestsConfig().set(path + ".timeEnd", this.getTimeEnd());
			Configs.getQuestsConfig().set(path + ".dialog", this.isDialog());
			Configs.getQuestsConfig().set(path + ".noDialog", this.getNoDialog());
			if(debug)QuestsPlugin.console.sendMessage("    §6Required §e" + this.getIndex() + " :: " + this.getType().toString());
		}
	}

	public String getHolderUUID() {
		return holderUUID;
	}

	public void setHolderUUID(String holderUUID) {
		this.holderUUID = holderUUID;
		this.setModified(true);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		this.setModified(true);
	}

	public RequiredType getType() {
		return type;
	}

	public void setType(RequiredType type) {
		this.type = type;
		this.setModified(true);
	}

	public RClass getRclass() {
		return rclass;
	}

	public void setRclass(RClass rclass) {
		this.rclass = rclass;
		this.setModified(true);
	}

	public RJob getRjob() {
		return rjob;
	}

	public void setRjob(RJob rjob) {
		this.rjob = rjob;
		this.setModified(true);
	}
	
	public String getInformation(){
		if(this.getType().equals(RequiredType.LEVEL))return "Niveau : " + this.getLevel();
		else if(this.getType().equals(RequiredType.CLASS))return "Classe : " + this.getRclass().toString();
		else if(this.getType().equals(RequiredType.JOB))return "Métier : " + this.getRjob().toString();
		else if(this.getType().equals(RequiredType.NON_JOB))return "Sauf métier : " + this.getRjob().toString();
		else if(this.getType().equals(RequiredType.QUEST)){
			if(this.getQuestTarget() == null)this.setQuestTargetUUID(this.getHolderUUID());
			return "Quête " + this.getQuestTarget().getColoredTitle();
		}
		else if(this.getType().equals(RequiredType.TIME))return "Heure : entre " + this.getTimeStart() + " et " + this.getTimeEnd();
		else if(this.getType().equals(RequiredType.ITEM))return "Item : " + this.getItemStack().getAmount() + " " + (this.getItemStack().getItemMeta().hasDisplayName() ? this.getItemStack().getItemMeta().getDisplayName() : this.getItemStack().getType().toString());
		else if(this.getType().equals(RequiredType.NON_ACTIVE_QUEST))return "Quête non active : " + this.getQuestTarget().getColoredTitle();
		else if(this.getType().equals(RequiredType.NON_DONE_QUEST))return "Quête non complétée : " + this.getQuestTarget().getColoredTitle();
		else if(this.getType().equals(RequiredType.MASTERY))return "Maîtrise : " + this.getMastery().toString();
		return "";
	}

	public Mastery getMastery() {
		return mastery;
	}

	public void setMastery(Mastery mastery) {
		this.mastery = mastery;
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

	public Long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
		this.setModified(true);
	}

	public Long getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Long timeEnd) {
		this.timeEnd = timeEnd;
		this.setModified(true);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.setModified(true);
	}

	public String getNoDialog() {
		return noDialog;
	}

	public void setNoDialog(String noDialog) {
		this.noDialog = noDialog;
		this.setModified(true);
	}

	public boolean isDialog() {
		return dialog;
	}

	public void setDialog(boolean dialog) {
		this.dialog = dialog;
		this.setModified(true);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
		this.getQuest().setRequiredsModified(true);
	}
	
	public Quest getQuest(){
		return Quest.get(this.getHolderUUID());
	}
	
	public String getQuestPath(){
		return "quests." + this.getQuest().getUUID() + ".requireds";
	}
	
}
