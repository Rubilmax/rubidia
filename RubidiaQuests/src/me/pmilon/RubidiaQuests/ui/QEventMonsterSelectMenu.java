package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.QEvent;
import me.pmilon.RubidiaQuests.quests.Quest;

public class QEventMonsterSelectMenu extends ListMenuUIHandler<Monster>{

	private Quest quest;
	private QuestPNJ pnj;
	private QEvent qEvent;
	public QEventMonsterSelectMenu(Player p, QuestPNJ pnj, Quest quest, QEvent qEvent) {
		super(p, "Monster selection menu", "Menu de sélection du monstre", 5);
		this.quest = quest;
		this.pnj = pnj;
		this.qEvent = qEvent;
		for(Monster monster : Monsters.monsters){
			boolean ok = true;
			for(Region region : Regions.regions){
				if(region.getMonsters().contains(monster)){
					ok = false;
					break;
				}
			}
			if(ok)this.list.add(monster);
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Choisissez un monstre à ajouter"), ("§7ou cliquez ici pour retourner au menu précédent")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected ItemStack getItem(Monster monster) {
		ItemStack item = new ItemStack(Material.valueOf(monster.getType().toString() + "_SPAWN_EGG"), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(monster.getName());
		meta.setLore(Arrays.asList(monster.getType().toString()));
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player arg1, ItemStack arg2) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		Monster monster = this.get(slot);
		if(monster != null){
			this.getQEvent().setMonsterUUID(monster.getUUID());
			Core.uiManager.requestUI(new QEventEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), this.getQEvent()));
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Core.uiManager.requestUI(new QEventEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), this.getQEvent()));
	}

	@Override
	protected void onOpen() {
	}

	@Override
	public String getType() {
		return "QEVENT_MONSTER_SELECTION_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
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

	public QEvent getQEvent() {
		return qEvent;
	}

	public void setQEvent(QEvent qEvent) {
		this.qEvent = qEvent;
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}

}
