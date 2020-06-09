package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.QEvent;
import me.pmilon.RubidiaQuests.quests.QEventType;
import me.pmilon.RubidiaQuests.quests.Quest;

public class QEventsEditionUI extends UIHandler {


	private Quest quest;
	private QuestPNJ pnj;
	private int SLOT_BACK = 8;
	public QEventsEditionUI(Player p, Quest quest, QuestPNJ pnj) {
		super(p);
		this.quest = quest;
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getQuest().getTitle() + " | QEvents",32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "QEVENTS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getQuest(), this.getPnj()));
		else{
			QEvent qEvent = null;
			for(QEvent qe : this.getQuest().getQEvents()){
				if(slot == qe.getIndex()){
					qEvent = qe;
					break;
				}
			}
			if(qEvent != null){
				if(e.isRightClick()){
					this.getQuest().delete(qEvent);
					Core.uiManager.requestUI(new QEventsEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
				}else Core.uiManager.requestUI(new QEventEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), qEvent));
			}else{
				qEvent = new QEvent(this.getQuest().getUUID(), slot, QEventType.TELEPORTATION, 0, 3.0, this.getHolder().getLocation(), Monsters.monsters.get(0).getUUID(), 1, new PotionEffect(PotionEffectType.BLINDNESS, 30, 0, true, false), new ArrayList<Block>(), new ItemStack(Material.STONE, 1), true);
				this.getQuest().getQEvents().add(qEvent);
				Core.uiManager.requestUI(new QEventEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), qEvent));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		for(QEvent qEvent : this.getQuest().getQEvents()){
			this.menu.setItem(qEvent.getIndex(), this.getEvt(qEvent));
		}
		for(int i = 0;i < this.SLOT_BACK;i++){
			if(this.getMenu().getItem(i) == null || this.getMenu().getItem(i).getType().equals(Material.AIR))this.menu.setItem(i, this.getEvt(null));
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
	protected ItemStack getEvt(QEvent qEvent){
		ItemStack item;
		if(qEvent != null){
			item = new ItemStack(qEvent.getType().getDisplay(), 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(qEvent.getType().toString());
			meta.setLore(Arrays.asList(qEvent.getInformation()));
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
