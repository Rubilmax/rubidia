package me.pmilon.RubidiaQuests.ui;

import java.util.List;

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
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

public class PreQuestEditionUI extends UIHandler {

	private QuestPNJ pnj;
	
	private ItemStack ITEM_CHOOSE = new ItemStack(Material.BOOK, 1);
	private ItemStack ITEM_CREATE = new ItemStack(Material.WRITABLE_BOOK, 1);

	private int SLOT_BACK = 8;
	private int SLOT_CHOOSE = 3;
	private int SLOT_CREATE = 5;
	
	public PreQuestEditionUI(Player p, QuestPNJ pnj) {
		super(p);
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(pnj.getName() + " | Choose/Create",32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "PRE_QUEST_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new PNJQuests(this.getHolder(), this.getPnj()));
		else if(slot == this.SLOT_CHOOSE)Core.uiManager.requestUI(new QuestListChooseUI(this.getHolder(), this.getPnj(), false));
		else if(slot == this.SLOT_CREATE){
			List<Quest> quests = this.getPnj().getQuests();
			Quest quest = Quest.newDefault();
			quests.add(quest);
			Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), quest, this.getPnj()));
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_CHOOSE, this.getChoose());
		this.menu.setItem(this.SLOT_CREATE, this.getCreate());
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}
	
	
	private ItemStack getChoose(){
		ItemMeta meta = ITEM_CHOOSE.getItemMeta();
		meta.setDisplayName("Choose existant");
		ITEM_CHOOSE.setItemMeta(meta);
		return ITEM_CHOOSE;
	}
	private ItemStack getCreate(){
		ItemMeta meta = ITEM_CREATE.getItemMeta();
		meta.setDisplayName("Create one");
		ITEM_CREATE.setItemMeta(meta);
		return ITEM_CREATE;
	}
	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}

}
