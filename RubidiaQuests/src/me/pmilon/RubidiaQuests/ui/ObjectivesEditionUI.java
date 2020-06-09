package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.Quest;

public class ObjectivesEditionUI extends UIHandler {

	private Quest quest;
	private QuestPNJ pnj;
	private int SLOT_BACK = 17;
	public ObjectivesEditionUI(Player p, Quest quest, QuestPNJ pnj) {
		super(p);
		this.quest = quest;
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getQuest().getTitle() + " | Objectives",32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "QUEST_OBJECTIVES_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getQuest(), this.getPnj()));
		else{
			Objective objective = null;
			for(Objective ob : this.getQuest().getObjectives()){
				if(slot == ob.getIndex()){
					objective = ob;
					break;
				}
			}
			if(objective != null){
				if(e.isRightClick()){
					this.getQuest().delete(objective);
					Core.uiManager.requestUI(new ObjectivesEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
				}else Core.uiManager.requestUI(new ObjectiveEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), objective));
			}else{
				objective = new Objective(QuestsPlugin.questColl, this.getQuest().getUUID(), slot, ObjectiveType.CRAFT, new ItemStack(Material.STONE, 1), Material.STONE, Monsters.monsters.get(0).getUUID(), this.getHolder().getLocation(), "infoName", this.getPnj().getUniqueId(), this.getQuest().getUUID(), this.getPnj().getName(), 1, true, new ArrayList<String>(), new HashMap<String,Long[]>(), true);
				this.getQuest().getObjectives().add(objective);
				Core.uiManager.requestUI(new ObjectiveEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), objective));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		for(Objective objective : this.getQuest().getObjectives()){
			this.menu.setItem(objective.getIndex(), this.getObj(objective));
		}
		for(int i = 0;i < this.SLOT_BACK;i++){
			if(this.getMenu().getItem(i) == null || this.getMenu().getItem(i).getType().equals(Material.AIR))this.menu.setItem(i, this.getObj(null));
		}
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}
	
	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	protected ItemStack getObj(Objective objective){
		ItemStack item;
		if(objective != null){
			item = new ItemStack(objective.getType().getDisplay(), 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(objective.getType().toString());
			meta.setLore(Arrays.asList(objective.getInformation()));
			item.setItemMeta(meta);
		}else{
			item = new ItemStack(Material.SLIME_BALL, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("NOTHING");
			item.setItemMeta(meta);
		}
		return item;
	}

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}

}
