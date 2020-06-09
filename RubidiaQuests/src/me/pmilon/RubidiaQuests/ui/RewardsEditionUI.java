package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Reward;
import me.pmilon.RubidiaQuests.quests.RewardType;

public class RewardsEditionUI extends UIHandler {

	private Quest quest;
	private QuestPNJ pnj;
	private int SLOT_BACK = 17;
	public RewardsEditionUI(Player p, Quest quest, QuestPNJ pnj) {
		super(p);
		this.quest = quest;
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getQuest().getTitle() + " | Rewards",32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "REWARDS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getQuest(), this.getPnj()));
		else{
			Reward reward = null;
			for(Reward rw : this.getQuest().getRewards()){
				if(slot == rw.getIndex()){
					reward = rw;
					break;
				}
			}
			if(reward != null){
				if(e.isRightClick()){
					this.getQuest().delete(reward);
					Core.uiManager.requestUI(new RewardsEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
				}else Core.uiManager.requestUI(new RewardEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), reward));
			}else{
				reward = new Reward(this.getQuest().getUUID(), slot, RewardType.CLASS, RClass.VAGRANT, RJob.JOBLESS, new ItemStack(Material.STONE, 1), Mastery.ADVENTURER, 0, 0.0, this.getQuest().getUUID(), "", "", true);
				this.getQuest().getRewards().add(reward);
				Core.uiManager.requestUI(new RewardEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), reward));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		for(Reward reward : this.getQuest().getRewards()){
			this.menu.setItem(reward.getIndex(), this.getRew(reward));
		}
		for(int i = 0;i < this.SLOT_BACK;i++){
			if(this.getMenu().getItem(i) == null || this.getMenu().getItem(i).getType().equals(Material.AIR))this.menu.setItem(i, this.getRew(null));
		}
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	protected ItemStack getRew(Reward reward){
		ItemStack item;
		if(reward != null){
			item = new ItemStack(reward.getType().getDisplay(), 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(reward.getType().toString());
			meta.setLore(Arrays.asList(reward.getInformation()));
			item.setItemMeta(meta);
		}else{
			item = new ItemStack(Material.SLIME_BALL, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("NOTHING");
			item.setItemMeta(meta);
		}
		return item;
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}

}
