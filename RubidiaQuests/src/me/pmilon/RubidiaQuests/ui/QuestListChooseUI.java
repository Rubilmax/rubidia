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
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PNJSession;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.QuestType;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.quests.Reward;

@SuppressWarnings("deprecation")
public class QuestListChooseUI extends ListMenuUIHandler<Quest> {

	private QuestPNJ pnj;
	private boolean showAll;
	public QuestListChooseUI(Player p, QuestPNJ pnj, boolean showAll) {
		super(p, StringUtils.abbreviate(pnj.getName() + " | Choose quest",32), StringUtils.abbreviate(pnj.getName() + " | Choose quest",32), 5);
		this.pnj = pnj;
		this.showAll = showAll;
		HashMap<String, List<Quest>> quests = new HashMap<String, List<Quest>>();
		for(QuestType type : QuestType.values()){
			quests.put(type.toString(), new ArrayList<Quest>());
		}
		for(Quest quest : QuestsPlugin.questColl.data()){
			if(!showAll){
				for(PNJSession session : PNJManager.pnjs.values()){
					PNJHandler handler = session.getPNJHandler();
					if(handler instanceof QuestPNJ){
						if(((QuestPNJ)handler).getQuests().contains(quest)){
							continue;
						}
					}
				}
			}
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

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
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
		lore.add("§7§lObjectifs");
		for(Objective objective : quest.getObjectives()){
			lore.add("§8" + objective.getType().getToDo() + objective.getInformation());
		}
		lore.add("§4§lPré-requis");
		for(Required required : quest.getRequireds()) {
			lore.add("§c" + required.getInformation());
		}
		lore.add("§2§lRécompenses");
		for(Reward reward : quest.getRewards()) {
			lore.add(reward.getInfos());
		}
		lore.add("§6§lPNJs");
		for(PNJSession session : PNJManager.pnjs.values()) {
			PNJHandler handler = session.getPNJHandler();
			if(handler instanceof QuestPNJ) {
				QuestPNJ quester = (QuestPNJ) handler;
				if(quester.getQuests().contains(quest)) {
					lore.add("§e" + quester.getName() + " (" + quester.getLocation().getWorld().getName() + ")");
				}
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player p, ItemStack is) {
		Quest quest = this.get(e.getRawSlot());
		if(quest != null) {
			this.getPnj().getQuests().add(quest);
			Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), quest, this.getPnj()));
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		if(e.isRightClick()){
			Core.uiManager.requestUI(new QuestListChooseUI(this.getHolder(), this.getPnj(), !this.showAll));
		}else{
			Core.uiManager.requestUI(new PreQuestEditionUI(this.getHolder(), this.getPnj()));
		}
	}

	@Override
	public String getType() {
		return "QUEST_LIST_SELECTION_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
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
