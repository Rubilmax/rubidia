package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaQuests.dialogs.MessageManager;
import me.pmilon.RubidiaQuests.pnjs.DialogerPNJ;
import me.pmilon.RubidiaQuests.pnjs.HostPNJ;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler.PNJType;
import me.pmilon.RubidiaQuests.pnjs.PasserPNJ;
import me.pmilon.RubidiaQuests.pnjs.PastorPNJ;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.pnjs.ShopPNJ;
import me.pmilon.RubidiaQuests.ui.houses.HouseManagerUI;
import me.pmilon.RubidiaQuests.ui.shops.ShopEditionUI;
import me.pmilon.RubidiaQuests.utils.Utils;

public class PNJSettings extends UIHandler {

	private PNJHandler pnj;

	private ItemStack ITEM_TITLE = new ItemStack(Material.MAP, 1);
	private ItemStack ITEM_NAME = new ItemStack(Material.PAPER, 1);
	private ItemStack ITEM_DIAL_QUST = new ItemStack(Material.BOOK, 1);
	private ItemStack ITEM_NO_QUEST_DIAL = new ItemStack(Material.WRITABLE_BOOK, 1);
	private ItemStack ITEM_AGE = new ItemStack(Material.RED_BED, 1);
	private ItemStack ITEM_MOVE = new ItemStack(Material.COMPASS, 1);
	private ItemStack ITEM_DELETE = new ItemStack(Material.BARRIER, 1);
	
	private int SLOT_TITLE = 0;
	private int SLOT_NAME = 1;
	private int SLOT_FIX = 2;
	private int SLOT_DIAL_QUST = 4;
	private int SLOT_NO_QUEST = 5;
	private int SLOT_AGE = 6;
	private int SLOT_MOVE = 7;
	private int SLOT_DELETE = 8;
	
	private int LISTENING_ID_TITLE = 0;
	private int LISTENING_ID_NAME = 1;
	private int LISTENING_ID_NOQUEST = 2;
	private int LISTENING_ID_TARGET = 6;
	private int LISTENING_ID_MOVE = 3;
	private int LISTENING_ID_LOC1 = 4;
	private int LISTENING_ID_LOC2 = 5;
	
	public PNJSettings(Player p, PNJHandler pnj) {
		super(p);
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(pnj.getName() + " | Settings",32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "PNJ_SETTINGS";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_TITLE)this.close(true, this.LISTENING_ID_TITLE);
		else if(slot == this.SLOT_NAME) {
			if(e.isRightClick()) {
				this.getPnj().setName(Utils.generateName());
			} else {
				this.close(true, this.LISTENING_ID_NAME);
			}
		}else if(slot == this.SLOT_NO_QUEST){
			if(this.getPnj().getType().equals(PNJType.PASSER))this.close(true, this.LISTENING_ID_TARGET);
			else if(this.getPnj().getType().equals(PNJType.QUEST))this.close(true, this.LISTENING_ID_NOQUEST);
			else if(this.getPnj().getType().equals(PNJType.PASTOR))this.close(true, this.LISTENING_ID_LOC2);
		}else if(slot == this.SLOT_DIAL_QUST){
			if(this.getPnj().getType().equals(PNJType.QUEST))Core.uiManager.requestUI(new PNJQuests(this.getHolder(), (QuestPNJ)this.getPnj()));
			else if(this.getPnj().getType().equals(PNJType.SHOP))Core.uiManager.requestUI(new ShopEditionUI(this.getHolder(), ((ShopPNJ) this.getPnj()).getShop(),0));
			else if(this.getPnj().getType().equals(PNJType.PASTOR))this.close(true, this.LISTENING_ID_LOC1);
			else if(this.getPnj().getType().equals(PNJType.HOST))Core.uiManager.requestUI(new HouseManagerUI(this.getHolder(), ((HostPNJ) this.getPnj()).getHouse()));
			else Core.uiManager.requestUI(new PNJDialogs(this.getHolder(), (DialogerPNJ)this.getPnj()));
		}else if(slot == this.SLOT_MOVE)this.close(true, this.LISTENING_ID_MOVE);
		else if(slot == this.SLOT_DELETE){
			this.getPnj().delete();
			this.close(false);
		}else if(slot == this.SLOT_AGE){
			this.getPnj().setBaby(!this.getPnj().isBaby());
		}else if(slot == this.SLOT_FIX){
			this.getPnj().setFix(!this.getPnj().isFix());
			getMenu().setItem(SLOT_FIX, getFix());
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LISTENING_ID_NAME){
					this.getPnj().setName(this.getMessage());
				}else if(this.getListeningId() == this.LISTENING_ID_TITLE)this.getPnj().setTitle(this.getMessage());
				else if(this.getListeningId() == this.LISTENING_ID_NOQUEST){
					if(this.getPnj().getType().equals(PNJType.QUEST))((QuestPNJ)this.getPnj()).setNoQuestDialog(MessageManager.filterDialog(this.getMessage()));
				}else if(this.getListeningId() == this.LISTENING_ID_MOVE)this.getPnj().setLocation(this.getHolder().getLocation());
				else if(this.getListeningId() == this.LISTENING_ID_LOC1)((PastorPNJ)this.getPnj()).setLocation1(this.getHolder().getLocation());
				else if(this.getListeningId() == this.LISTENING_ID_LOC2)((PastorPNJ)this.getPnj()).setLocation2(this.getHolder().getLocation());
				else if(this.getListeningId() == this.LISTENING_ID_TARGET){
					PasserPNJ passer = (PasserPNJ)this.getPnj();
					passer.setTargetName(this.getMessage());
					passer.setTargetLocation(this.getHolder().getLocation());
				}
			}
		}
		
		getMenu().setItem(SLOT_TITLE, getTitle());
		getMenu().setItem(SLOT_NAME, getName());
		getMenu().setItem(SLOT_FIX, getFix());
		if(getPnj() instanceof PastorPNJ){
			getMenu().setItem(SLOT_DIAL_QUST, getLoc1());
			getMenu().setItem(SLOT_NO_QUEST, getLoc2());
		}else if(getPnj() instanceof PasserPNJ){
			getMenu().setItem(SLOT_DIAL_QUST, getDialQust());
			getMenu().setItem(SLOT_NO_QUEST, getTargetLoc());
		}else{
			if(getPnj().getType().equals(PNJType.HOST))getMenu().setItem(SLOT_DIAL_QUST, getHouseManager());
			else getMenu().setItem(SLOT_DIAL_QUST, getDialQust());
			if(getPnj().getType().equals(PNJType.QUEST))getMenu().setItem(SLOT_NO_QUEST, getNoQuestDial());
		}
		getMenu().setItem(SLOT_AGE, getAge());
		getMenu().setItem(SLOT_MOVE, getMove());
		getMenu().setItem(SLOT_DELETE, getDelete());
		
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	public PNJHandler getPnj() {
		return pnj;
	}

	public void setPnj(PNJHandler pnj) {
		this.pnj = pnj;
	}
	
	
	private ItemStack getTitle(){
		ItemMeta meta = ITEM_TITLE.getItemMeta();
		meta.setDisplayName("Title");
		ITEM_TITLE.setItemMeta(meta);
		return ITEM_TITLE;
	}
	private ItemStack getName(){
		ItemMeta meta = ITEM_NAME.getItemMeta();
		meta.setDisplayName("Name");
		ITEM_NAME.setItemMeta(meta);
		return ITEM_NAME;
	}
	private ItemStack getFix(){
		ItemStack item = new ItemStack(this.getPnj().isFix() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Fix : " + String.valueOf(this.getPnj().isFix()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getAge(){
		ItemMeta meta = ITEM_AGE.getItemMeta();
		meta.setDisplayName("Age");
		meta.setLore(Arrays.asList(String.valueOf(this.getPnj().isBaby() ? "Baby" : "Adult")));
		ITEM_AGE.setItemMeta(meta);
		return ITEM_AGE;
	}
	private ItemStack getMove(){
		ItemMeta meta = ITEM_MOVE.getItemMeta();
		meta.setDisplayName("Move");
		ITEM_MOVE.setItemMeta(meta);
		return ITEM_MOVE;
	}
	private ItemStack getDialQust(){
		ItemMeta meta = ITEM_DIAL_QUST.getItemMeta();
		if(this.getPnj().getType().equals(PNJType.QUEST))meta.setDisplayName("Quests");
		else if(this.getPnj().getType().equals(PNJType.SHOP))meta.setDisplayName("Shop");
		else {
			meta.setDisplayName("Dialogs");
			meta.setLore(((DialogerPNJ) this.getPnj()).getDialogs());
		}
		ITEM_DIAL_QUST.setItemMeta(meta);
		return ITEM_DIAL_QUST;
	}
	private ItemStack getNoQuestDial(){
		ItemMeta meta = ITEM_NO_QUEST_DIAL.getItemMeta();
		if(this.getPnj().getType().equals(PNJType.QUEST)){
			meta.setDisplayName("No Quest Dialog");
			meta.setLore(Arrays.asList(((QuestPNJ)this.getPnj()).getNoQuestDialog()));
		}
		ITEM_NO_QUEST_DIAL.setItemMeta(meta);
		return ITEM_NO_QUEST_DIAL;
	}
	private ItemStack getHouseManager(){
		ItemStack item = new ItemStack(Material.BRICK,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("House Manager");
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getLoc1(){
		Location location = ((PastorPNJ)this.getPnj()).getLocation1();
		ItemStack item = new ItemStack(Material.COMPASS,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(location.getX() + " " + location.getY() + " " + location.getZ());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getLoc2(){
		Location location = ((PastorPNJ)this.getPnj()).getLocation2();
		ItemStack item = new ItemStack(Material.COMPASS,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(location.getX() + " " + location.getY() + " " + location.getZ());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getTargetLoc(){
		ItemStack item = new ItemStack(Material.COMPASS,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(((PasserPNJ)this.getPnj()).getTargetName());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getDelete(){
		ItemMeta meta = ITEM_DELETE.getItemMeta();
		meta.setDisplayName("Delete");
		ITEM_DELETE.setItemMeta(meta);
		return ITEM_DELETE;
	}

}
