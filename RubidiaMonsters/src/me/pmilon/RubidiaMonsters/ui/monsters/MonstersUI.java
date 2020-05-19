package me.pmilon.RubidiaMonsters.ui.monsters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.attacks.AbstractAttack;
import me.pmilon.RubidiaMonsters.regions.Drop;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaMonsters.ui.regions.RegionMonstersEditMenu;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.quests.QEvent;
import me.pmilon.RubidiaQuests.quests.Quest;

public class MonstersUI extends ListMenuUIHandler<Monster> {

	private Region region;
	public MonstersUI(Player p, Region region) {
		super(p, "Monster Selection", "Sélection de monstre", 5);
		this.region = region;
		for(Monster monster : Monsters.monsters){
			boolean isTook = false;
			for(Region reg : Regions.regions){
				if(reg.getMonsters().contains(monster)){
					isTook = true;
					break;
				}
			}
			if(!isTook)this.list.add(monster);
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Choisissez un monstre à ajouter"), ("§7ou cliquez ici pour en créer un nouveau"), ("§7Clic droit pour éditer un monstre")));
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
		boolean isQuest = false;
		for(Quest quest : QuestsPlugin.questColl.data()){
			for(QEvent event : quest.getQEvents()){
				if(event.getMonster().equals(monster)){
					isQuest = true;
					break;
				}
			}
			if(isQuest)break;
		}
		return isQuest ? Utils.setGlowingWithoutAttributes(item) : item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player arg1, ItemStack is) {
		if(is != null){
			int slot = e.getRawSlot();
			Monster monster = this.get(slot);
			if(e.isRightClick()) {
				Core.uiManager.requestUI(new MonsterEditionMenu(this.getHolder(), this.getRegion(), monster));
			} else if (monster != null) {
				this.getRegion().getMonsters().add(monster);
				Core.uiManager.requestUI(new RegionMonstersEditMenu(this.getHolder(), this.getRegion()));
			}
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Monster monster = new Monster(UUID.randomUUID().toString(), "No name", EntityType.ZOMBIE, 0.0, 1.0, 0.5, new ArrayList<Drop>(), true, new ArrayList<AbstractAttack>());
		Monsters.monsters.add(monster);
		Core.uiManager.requestUI(new MonsterEditionMenu(this.getHolder(), this.getRegion(), monster));
	}

	@Override
	protected void onOpen() {
	}

	@Override
	public String getType() {
		return "MONSTERS_LIST_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}
}
