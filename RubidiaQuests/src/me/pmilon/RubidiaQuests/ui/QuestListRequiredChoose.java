package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.QuestType;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.quests.RequiredHolder;

@SuppressWarnings("deprecation")
public class QuestListRequiredChoose extends ListMenuUIHandler<Quest> {

	private RequiredHolder requiredHolder;
	private PNJHandler pnj;
	private Required required;
	public QuestListRequiredChoose(Player p, RequiredHolder requiredHolder, PNJHandler pnjHandler, Required required) {
		super(p, StringUtils.abbreviate((requiredHolder instanceof Quest ? ((Quest) requiredHolder).getTitle() : " | Requireds"),32), StringUtils.abbreviate((requiredHolder instanceof Quest ? ((Quest) requiredHolder).getTitle() : " | Requireds"),32), 5);
		this.requiredHolder = requiredHolder;
		this.pnj = pnjHandler;
		this.required = required;
		HashMap<String, List<Quest>> quests = new HashMap<String, List<Quest>>();
		for(QuestType type : QuestType.values()){
			quests.put(type.toString(), new ArrayList<Quest>());
		}
		for(Quest quest : QuestsPlugin.questColl.data()){
			quests.get(quest.getType().toString()).add(quest);
		}
		for(QuestType type : QuestType.values()){
			List<Quest> questss = quests.get(type.toString());
			Collections.sort(questss, new Comparator<Quest>(){

				@Override
				public int compare(Quest q1, Quest q2) {
					return q1.getTitle().compareTo(q2.getTitle());
				}
				
			});
			this.list.addAll(questss);
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Click one to choose or click here to get back");
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected ItemStack getItem(Quest quest) {
		Wool wool = new Wool(quest.getType().getColor());
		ItemStack item = wool.toItemStack(1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(quest.getColoredTitle());
		List<String> lore = new ArrayList<String>();
		lore.add(quest.getColoredSubtitle());
		for(Objective objective : quest.getObjectives()){
			lore.add("§8" + objective.getType().getToDo() + objective.getInformation());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player arg1, ItemStack arg2) {
		Quest quest = this.get(e.getRawSlot());
		if(quest != null) {
			this.getRequired().setQuestTargetUUID(quest.getUUID());
			Core.uiManager.requestUI(new RequiredEditionMenu(this.getHolder(), this.getRequiredHolder(), this.getPnj(), this.getRequired()));
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Core.uiManager.requestUI(new RequiredEditionMenu(this.getHolder(), this.getRequiredHolder(), this.getPnj(), this.getRequired()));
	}

	@Override
	public String getType() {
		return "REQUIRED_QUEST_SELECTION_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	public PNJHandler getPnj() {
		return pnj;
	}

	public void setPnj(PNJHandler pnj) {
		this.pnj = pnj;
	}

	public Required getRequired() {
		return required;
	}

	public void setRequired(Required required) {
		this.required = required;
	}

	public RequiredHolder getRequiredHolder() {
		return requiredHolder;
	}

	public void setRequiredHolder(RequiredHolder requiredHolder) {
		this.requiredHolder = requiredHolder;
	}

	@Override
	protected void onOpen() {
		// nothing
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}
}
