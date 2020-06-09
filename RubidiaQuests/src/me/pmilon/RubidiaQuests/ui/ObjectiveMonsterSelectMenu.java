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
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;

public class ObjectiveMonsterSelectMenu extends ListMenuUIHandler<Monster>{

	private Quest quest;
	private QuestPNJ pnj;
	private Objective objective;
	public ObjectiveMonsterSelectMenu(Player p, QuestPNJ pnj, Quest quest, Objective objective) {
		super(p, "Monster selection menu", "Menu de sélection du monstre", 5);
		this.quest = quest;
		this.pnj = pnj;
		this.objective = objective;
		for(Monster monster : Monsters.monsters){
			this.list.add(monster);
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
			this.getObjective().setMonsterUUID(monster.getUUID());
			Core.uiManager.requestUI(new ObjectiveEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), this.getObjective()));
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Core.uiManager.requestUI(new ObjectiveEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), this.getObjective()));
	}

	@Override
	protected void onOpen() {
	}

	@Override
	public String getType() {
		return "OBJECTIVE_MONSTER_SELECTION_MENU";
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

	public Objective getObjective() {
		return objective;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}

}
