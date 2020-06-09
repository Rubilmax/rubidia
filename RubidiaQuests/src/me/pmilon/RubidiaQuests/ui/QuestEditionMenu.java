package me.pmilon.RubidiaQuests.ui;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.QuestType;

@SuppressWarnings("deprecation")
public class QuestEditionMenu extends UIHandler {

	private Quest quest;
	private QuestPNJ pnj;
	
	private ItemStack ITEM_TITLE = new ItemStack(Material.MAP, 1);
	private ItemStack ITEM_SUBTITLE = new ItemStack(Material.PAPER, 1);
	private ItemStack ITEM_EVT = new ItemStack(Material.TNT, 1);
	private ItemStack ITEM_OBJ = new ItemStack(Material.OAK_LEAVES, 1);
	private ItemStack ITEM_REW = new ItemStack(Material.EMERALD, 1);
	private ItemStack ITEM_REQ = new ItemStack(Material.REDSTONE_BLOCK, 1);
	private ItemStack ITEM_NOND = new ItemStack(Material.BOOK, 1);
	private ItemStack ITEM_PRED = new ItemStack(Material.BOOK, 1);
	private ItemStack ITEM_POSTD = new ItemStack(Material.BOOK, 1);
	private ItemStack ITEM_DELETE = new ItemStack(Material.BARRIER, 1);

	private int SLOT_BACK = 33;
	private int SLOT_TITLE = 0;
	private int SLOT_SUBTITLE = 1;
	private int SLOT_EVT = 2;
	private int SLOT_OBJ = 3;
	private int SLOT_REW = 4;
	private int SLOT_REQ = 5;
	private int SLOT_NOND = 6;
	private int SLOT_PRED = 7;
	private int SLOT_POSTD = 8;
	private int SLOT_DELETE = 29;
	private int SLOT_AUTOFINISH = 10;
	private int SLOT_REDONE = 11;
	private int SLOT_GIVEUP = 12;
	private int SLOT_ORDERED = 14;
	private int SLOT_UNIQUE = 16;
	private int SLOT_START = 22;
	
	private int LISTENING_ID_TITLE = 0;
	private int LISTENING_ID_SUBTITLE = 1;
	
	public QuestEditionMenu(Player p, Quest quest, QuestPNJ pnj) {
		super(p);
		this.quest = quest;
		this.pnj = pnj;
	}
	
	public Quest getQuest(){
		return this.quest;
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "QUEST_EDITION_MENU";
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
		else if(slot == this.SLOT_DELETE){
			Core.uiManager.requestUI(new QuestDeletionConfirmationUI(rp, this.getQuest(), this.getPnj()));
		}else if(slot == this.SLOT_AUTOFINISH){
			this.getQuest().setAutoFinish(!this.getQuest().isAutoFinish());
			this.menu.setItem(this.SLOT_AUTOFINISH, this.getAutoFinish());
		}else if(slot == this.SLOT_REDONE){
			this.getQuest().setRedonable(!this.getQuest().isRedonable());
			this.menu.setItem(this.SLOT_REDONE, this.getRedonable());
		}else if(slot == this.SLOT_GIVEUP){
			this.getQuest().setGiveupable(!this.getQuest().isGiveupable());
			this.menu.setItem(this.SLOT_GIVEUP, this.getGiveupable());
		}else if(slot == this.SLOT_ORDERED){
			this.getQuest().setOrderedQuest(!this.getQuest().isOrderedQuest());
			this.menu.setItem(this.SLOT_ORDERED, this.getOrderedQuest());
		}else if(slot == this.SLOT_UNIQUE){
			this.getQuest().setUnique(!this.getQuest().isUnique());
			this.menu.setItem(this.SLOT_UNIQUE, this.getUnique());
		}else if(slot == this.SLOT_TITLE)this.close(true, this.LISTENING_ID_TITLE);
		else if(slot == this.SLOT_SUBTITLE)this.close(true, this.LISTENING_ID_SUBTITLE);
		else if(slot == this.SLOT_START){
			if(e.isRightClick()){
				rp.getActiveQuests().remove(this.getQuest());
				this.getQuest().accept(rp, this.getPnj().getUniqueId());
				this.close(false);
			}else{
				if(this.getQuest().getType().equals(QuestType.NORMAL))this.getQuest().setType(QuestType.EVENT);
				else if(this.getQuest().getType().equals(QuestType.EVENT))this.getQuest().setType(QuestType.MISC);
				else if(this.getQuest().getType().equals(QuestType.MISC))this.getQuest().setType(QuestType.OFFICE);
				else if(this.getQuest().getType().equals(QuestType.OFFICE))this.getQuest().setType(QuestType.STORY);
				else if(this.getQuest().getType().equals(QuestType.STORY))this.getQuest().setType(QuestType.NORMAL);
				this.menu.setItem(this.SLOT_START, this.getStart());
			}
		}else if(slot == this.SLOT_NOND)Core.uiManager.requestUI(new QuestNonDialogsMenu(this.getHolder(), this.getPnj(), this.getQuest()));
		else if(slot == this.SLOT_PRED)Core.uiManager.requestUI(new QuestPreDialogsMenu(this.getHolder(), this.getQuest(), this.getPnj()));
		else if(slot == this.SLOT_POSTD)Core.uiManager.requestUI(new QuestPostDialogsMenu(this.getHolder(), this.getQuest(), this.getPnj()));
		else if(slot == this.SLOT_OBJ)Core.uiManager.requestUI(new ObjectivesEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
		else if(slot == this.SLOT_REW)Core.uiManager.requestUI(new RewardsEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
		else if(slot == this.SLOT_REQ)Core.uiManager.requestUI(new RequiredsEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
		else if(slot == this.SLOT_EVT)Core.uiManager.requestUI(new QEventsEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LISTENING_ID_SUBTITLE)this.getQuest().setSubtitle(this.getMessage());
				else if(this.getListeningId() == this.LISTENING_ID_TITLE)this.getQuest().setTitle(this.getMessage());
			}
		}
		
		this.menu = Bukkit.createInventory(this.getHolder(), 36, StringUtils.abbreviate(this.getQuest().getTitle() + " | Edition", 32));
		
		this.menu.setItem(this.SLOT_DELETE, this.getDelete());
		this.menu.setItem(this.SLOT_EVT, this.getEvt());
		this.menu.setItem(this.SLOT_OBJ, this.getObj());
		this.menu.setItem(this.SLOT_REW, this.getRew());
		this.menu.setItem(this.SLOT_REQ, this.getReq());
		this.menu.setItem(this.SLOT_NOND, this.getNond());
		this.menu.setItem(this.SLOT_PRED, this.getPred());
		this.menu.setItem(this.SLOT_POSTD, this.getPostd());
		this.menu.setItem(this.SLOT_TITLE, this.getTitle());
		this.menu.setItem(this.SLOT_SUBTITLE, this.getSubtitle());
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_AUTOFINISH, this.getAutoFinish());
		this.menu.setItem(this.SLOT_REDONE, this.getRedonable());
		this.menu.setItem(this.SLOT_GIVEUP, this.getGiveupable());
		this.menu.setItem(this.SLOT_ORDERED, this.getOrderedQuest());
		this.menu.setItem(this.SLOT_UNIQUE, this.getUnique());
		this.menu.setItem(this.SLOT_START, this.getStart());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private ItemStack getTitle(){
		ItemMeta meta = ITEM_TITLE.getItemMeta();
		meta.setDisplayName(this.getQuest().getTitle());
		ITEM_TITLE.setItemMeta(meta);
		return ITEM_TITLE;
	}
	private ItemStack getSubtitle(){
		ItemMeta meta = ITEM_SUBTITLE.getItemMeta();
		meta.setDisplayName(this.getQuest().getSubtitle());
		ITEM_SUBTITLE.setItemMeta(meta);
		return ITEM_SUBTITLE;
	}
	private ItemStack getEvt(){
		ItemMeta meta = ITEM_EVT.getItemMeta();
		meta.setDisplayName("Events");
		ITEM_EVT.setItemMeta(meta);
		return ITEM_EVT;
	}
	private ItemStack getObj(){
		ItemMeta meta = ITEM_OBJ.getItemMeta();
		meta.setDisplayName("Objectives");
		ITEM_OBJ.setItemMeta(meta);
		return ITEM_OBJ;
	}
	private ItemStack getRew(){
		ItemMeta meta = ITEM_REW.getItemMeta();
		meta.setDisplayName("Rewards");
		ITEM_REW.setItemMeta(meta);
		return ITEM_REW;
	}
	private ItemStack getReq(){
		ItemMeta meta = ITEM_REQ.getItemMeta();
		meta.setDisplayName("Requireds");
		ITEM_REQ.setItemMeta(meta);
		return ITEM_REQ;
	}
	private ItemStack getNond(){
		ItemMeta meta = ITEM_NOND.getItemMeta();
		meta.setDisplayName("Not finished Dialogs");
		meta.setLore(this.getQuest().getNonDialogs());
		ITEM_NOND.setItemMeta(meta);
		return ITEM_NOND;
	}
	private ItemStack getPred(){
		ItemMeta meta = ITEM_PRED.getItemMeta();
		meta.setDisplayName("Pre dialogs");
		meta.setLore(this.getQuest().getPreDialogs());
		ITEM_PRED.setItemMeta(meta);
		return ITEM_PRED;
	}
	private ItemStack getPostd(){
		ItemMeta meta = ITEM_POSTD.getItemMeta();
		meta.setDisplayName("Post Dialogs");
		meta.setLore(this.getQuest().getPostDialogs());
		ITEM_POSTD.setItemMeta(meta);
		return ITEM_POSTD;
	}
	private ItemStack getDelete(){
		ItemMeta meta = ITEM_DELETE.getItemMeta();
		meta.setDisplayName("Delete");
		ITEM_DELETE.setItemMeta(meta);
		return ITEM_DELETE;
	}
	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	protected ItemStack getStart(){
		Wool wool = new Wool(this.getQuest().getType().getColor());
		ItemStack ITEM_START = wool.toItemStack(1);
		ItemMeta meta = ITEM_START.getItemMeta();
		meta.setDisplayName("Select quest type or start quest (right click)");
		ITEM_START.setItemMeta(meta);
		return ITEM_START;
	}
	protected ItemStack getAutoFinish(){
		ItemStack item = new ItemStack(this.getQuest().isAutoFinish() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("AutoFinish : " + String.valueOf(this.getQuest().isAutoFinish()));
		item.setItemMeta(meta);
		return item;
	}
	protected ItemStack getRedonable(){
		ItemStack item = new ItemStack(this.getQuest().isRedonable() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Redonable : " + String.valueOf(this.getQuest().isRedonable()));
		item.setItemMeta(meta);
		return item;
	}
	protected ItemStack getGiveupable(){
		ItemStack item = new ItemStack(this.getQuest().isGiveupable() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Giveupable : " + String.valueOf(this.getQuest().isGiveupable()));
		item.setItemMeta(meta);
		return item;
	}
	protected ItemStack getOrderedQuest(){
		ItemStack item = new ItemStack(this.getQuest().isOrderedQuest() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Ordered quest : " + String.valueOf(this.getQuest().isOrderedQuest()));
		item.setItemMeta(meta);
		return item;
	}
	protected ItemStack getUnique(){
		ItemStack item = new ItemStack(this.getQuest().isUnique() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Unique quest : " + String.valueOf(this.getQuest().isUnique()));
		item.setItemMeta(meta);
		return item;
	}
	
	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}

}
