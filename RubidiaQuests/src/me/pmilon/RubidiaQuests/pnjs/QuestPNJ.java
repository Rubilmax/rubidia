package me.pmilon.RubidiaQuests.pnjs;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.utils.Configs;
import me.pmilon.RubidiaQuests.utils.Utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class QuestPNJ extends PNJHandler {

	private List<Quest> quests;
	private String noQuestDialog;
	public QuestPNJ(String uuid, String title, String name, Location loc, int age, List<Quest> quests, String noQuestDialog, boolean fix) {
		super(uuid, title, "§6§l", name, "§e", PNJType.QUEST, loc, age, fix);
		this.quests = quests;
		this.noQuestDialog = noQuestDialog;
	}

	public boolean hasQuest(){
		if(this.getQuests() != null){
			return !this.getQuests().isEmpty();
		}
		return false;
	}
	
	public List<Quest> getQuests() {
		return quests;
	}
	
	public Quest getAvailableQuest(RPlayer rplayer){
		for(Quest quest : this.getQuests()){
			if((!rplayer.getDoneQuests().contains(quest) || quest.isRedonable()) && quest.isAvailable(rplayer)){
				boolean thisQuest = true;
				for(Quest activeQuest : rplayer.getActiveQuests()){
					if(activeQuest.equals(quest) || activeQuest.hasObjectiveTalkTo(this.getUniqueId()))thisQuest = false;
				}
				if(thisQuest)return quest;
			}
		}
		return null;
	}
	
	public Quest getActiveQuest(RPlayer rplayer){
		for(Quest quest : rplayer.getActiveQuests()){
			if(this.getQuests().contains(quest))return quest;
		}
		return null;
	}

	public void setQuests(List<Quest> quests) {
		this.quests = quests;
	}

	@Override
	protected void onSave() {
		List<String> quests = new ArrayList<String>();
		for(Quest quest : this.getQuests()){
			if(quest != null)quests.add(quest.getUUID());
		}
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".quests", quests);
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".noQuestDialog", this.getNoQuestDialog());
	}

	@Override
	protected void onRightClick(PlayerInteractEntityEvent e, final Player p, Villager villager) {
		final RPlayer rp = RPlayer.get(p);
		final Quest quest = this.getAvailableQuest(rp);
		if(quest != null){
			final QuestPNJ instance = this;
			PNJDialog dialog = new PNJDialog(p, instance, villager, quest.getPreDialogs(), DialogType.AMBIENT, new Runnable(){
				public void run(){
					if(!rp.getActiveQuests().contains(quest) && (!rp.getDoneQuests().contains(quest) || quest.isRedonable()) && quest.isAvailable(rp)){
						p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
						Utils.sendQuestion(rp, "Souhaitez-vous accepter cette quête ?",
								"/quest yes " + quest.getUUID() + " " + instance.getUniqueId(),
								"/quest no " + quest.getUUID() + " " + instance.getUniqueId());
					}else DialogManager.setNoDialog(p);
				}
			}, true, true);
			dialog.start();
		}else{
			List<Quest> quests = new ArrayList<Quest>(this.getQuests());
			for(Quest doneQuest : rp.getDoneQuests()){
				if(quests.contains(doneQuest)){
					if(!doneQuest.isRedonable())quests.remove(doneQuest);
				}
			}
			
			if(!quests.isEmpty()){
				boolean done = false;
				for(int i = 0;i < quests.size();i++){
					for(Required required : quests.get(i).getRequireds()){
						if(required.isDialog()){
							if(!required.check(rp)){
								List<String> dialogs = new ArrayList<String>();
								dialogs.add(required.getNoDialog());
								PNJDialog dialog = new PNJDialog(p, this, villager, dialogs, DialogType.NO, null, false, true);
								dialog.start();
								done = true;
								break;
							}
						}
					}
					if(done)break;
				}
				if(!done){
					List<String> dialogs = new ArrayList<String>();
					dialogs.add(this.getNoQuestDialog());
					PNJDialog dialog = new PNJDialog(p, this, villager, dialogs, DialogType.AMBIENT, null, false, true);
					dialog.start();
				}
			}else{
				List<String> dialogs = new ArrayList<String>();
				dialogs.add(this.getNoQuestDialog());
				PNJDialog dialog = new PNJDialog(p, this, villager, dialogs, DialogType.AMBIENT, null, false, true);
				dialog.start();
			}
		}
	}

	public String getNoQuestDialog() {
		return noQuestDialog;
	}

	public void setNoQuestDialog(String noQuestDialog) {
		this.noQuestDialog = noQuestDialog;
	}

	@Override
	protected void onDelete() {
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

}
