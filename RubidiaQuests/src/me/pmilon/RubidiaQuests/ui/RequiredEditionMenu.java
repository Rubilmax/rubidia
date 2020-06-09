package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.Arrays;
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
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaQuests.dialogs.MessageManager;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.quests.RequiredHolder;
import me.pmilon.RubidiaQuests.quests.RequiredType;

public class RequiredEditionMenu extends UIHandler {

	private RequiredHolder requiredHolder;
	private PNJHandler pnj;
	private Required required;
	
	private ItemStack ITEM_QUEST = new ItemStack(Material.BOOK, 1);

	private int SLOT_BACK = 15;
	private int SLOT_TYPE = 0;
	private int SLOT_CLASS = 1;
	private int SLOT_JOB = 2;
	private int SLOT_MASTERY = 3;
	private int SLOT_QUEST = 4;
	private int SLOT_ITEM = 5;
	private int SLOT_TIME_START = 6;
	private int SLOT_TIME_END = 7;
	private int SLOT_LEVEL = 8;
	private int SLOT_DIALOGING = 11;
	private int SLOT_DIALOG = 12;
	
	private final int LIST_ID_TIME_START = 1;
	private final int LIST_ID_TIME_END = 2;
	private final int LIST_ID_NODIALOG = 3;
	private final int LIST_ID_LEVEL = 4;
	
	public RequiredEditionMenu(Player p, RequiredHolder requiredHolder, PNJHandler pnjHandler, Required required) {
		super(p);
		this.requiredHolder = requiredHolder;
		this.pnj = pnjHandler;
		this.required = required;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate((this.getRequiredHolder() instanceof Quest ? ((Quest) this.getRequiredHolder()).getTitle() : " | Required " + this.getRequired().getIndex()), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "REQUIRED_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new RequiredsEditionUI(this.getHolder(), this.getRequiredHolder(), this.getPnj()));
		else if(slot == this.SLOT_CLASS){
			if(this.getRequired().getRclass().equals(RClass.VAGRANT))this.getRequired().setRclass(RClass.ASSASSIN);
			else if(this.getRequired().getRclass().equals(RClass.ASSASSIN))this.getRequired().setRclass(RClass.MAGE);
			else if(this.getRequired().getRclass().equals(RClass.MAGE))this.getRequired().setRclass(RClass.PALADIN);
			else if(this.getRequired().getRclass().equals(RClass.PALADIN))this.getRequired().setRclass(RClass.RANGER);
			else if(this.getRequired().getRclass().equals(RClass.RANGER))this.getRequired().setRclass(RClass.VAGRANT);
			this.menu.setItem(this.SLOT_CLASS, this.getRClass());
		}else if(slot == this.SLOT_JOB){
			//TODO cycle jobs
			this.menu.setItem(this.SLOT_JOB, this.getRJob());
		}else if(slot == this.SLOT_TYPE){
			this.menu.clear();
			if(this.getRequired().getType().equals(RequiredType.LEVEL)){
				this.getRequired().setType(RequiredType.CLASS);
				this.menu.setItem(this.SLOT_CLASS, this.getRClass());
			}else if(this.getRequired().getType().equals(RequiredType.CLASS)){
				this.getRequired().setType(RequiredType.JOB);
				this.menu.setItem(this.SLOT_JOB, this.getRJob());
			}else if(this.getRequired().getType().equals(RequiredType.JOB)){
				this.getRequired().setType(RequiredType.NON_JOB);
				this.menu.setItem(this.SLOT_JOB, this.getRJob());
			}else if(this.getRequired().getType().equals(RequiredType.NON_JOB)){
				this.getRequired().setType(RequiredType.QUEST);
				this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
			}else if(this.getRequired().getType().equals(RequiredType.QUEST)){
				this.getRequired().setType(RequiredType.NON_ACTIVE_QUEST);
				this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
			}else if(this.getRequired().getType().equals(RequiredType.NON_ACTIVE_QUEST)){
				this.getRequired().setType(RequiredType.NON_DONE_QUEST);
				this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
			}else if(this.getRequired().getType().equals(RequiredType.NON_DONE_QUEST)){
				this.getRequired().setType(RequiredType.TIME);
				this.menu.setItem(this.SLOT_TIME_START, this.getTimeStart());
				this.menu.setItem(this.SLOT_TIME_END, this.getTimeEnd());
			}else if(this.getRequired().getType().equals(RequiredType.TIME)){
				this.getRequired().setType(RequiredType.ITEM);
				this.menu.setItem(this.SLOT_ITEM, this.getItem());
			}else if(this.getRequired().getType().equals(RequiredType.ITEM)){
				this.getRequired().setType(RequiredType.MASTERY);
				this.menu.setItem(this.SLOT_MASTERY, this.getMastery());
			}else if(this.getRequired().getType().equals(RequiredType.MASTERY)){
				this.getRequired().setType(RequiredType.LEVEL);
				this.menu.setItem(this.SLOT_LEVEL, this.getLevel());
			}
			this.menu.setItem(this.SLOT_TYPE, this.getRType());
			this.menu.setItem(this.SLOT_BACK, this.getBack());
			this.menu.setItem(this.SLOT_DIALOGING, this.getIsDialog());
			this.menu.setItem(this.SLOT_DIALOG, this.getDialog());
		}else if(slot == this.SLOT_MASTERY){
			Mastery[] masteries = Mastery.values();
			this.getRequired().setMastery(masteries[(this.getRequired().getMastery().getId()+1) % masteries.length]);
			this.menu.setItem(this.SLOT_MASTERY, this.getMastery());
		}else if(slot == this.SLOT_ITEM){
			ItemStack inhand = this.getHolder().getEquipment().getItemInMainHand();
			if(inhand != null){
				if(!inhand.getType().equals(Material.AIR)){
					if(inhand.getAmount() == 1)inhand.setAmount(1);
					this.getRequired().setItemStack(inhand);
					this.menu.setItem(this.SLOT_ITEM, this.getItem());
				}
			}
		}else if(slot == this.SLOT_TIME_START)this.close(true, this.LIST_ID_TIME_START);
		else if(slot == this.SLOT_TIME_END)this.close(true, this.LIST_ID_TIME_END);
		else if(slot == this.SLOT_DIALOG)this.close(true, this.LIST_ID_NODIALOG);
		else if(slot == this.SLOT_LEVEL)this.close(true, this.LIST_ID_LEVEL);
		else if(slot == this.SLOT_DIALOGING){
			this.getRequired().setDialog(!this.getRequired().isDialog());
			this.menu.setItem(this.SLOT_DIALOGING, this.getIsDialog());
		}else if(slot == this.SLOT_QUEST)Core.uiManager.requestUI(new QuestListRequiredChoose(this.getHolder(), this.getRequiredHolder(), this.getPnj(), this.getRequired()));
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_TIME_START){
					if(Utils.isInteger(this.getMessage())){
						this.getRequired().setTimeStart(Long.valueOf(this.getMessage()));
					}else this.getHolder().sendMessage("§cPlease enter a valid integer");
				}else if(this.getListeningId() == this.LIST_ID_TIME_END){
					if(Utils.isInteger(this.getMessage())){
						this.getRequired().setTimeEnd(Long.valueOf(this.getMessage()));
					}else this.getHolder().sendMessage("§cPlease enter a valid integer");
				}else if(this.getListeningId() == this.LIST_ID_NODIALOG){
					this.getRequired().setNoDialog(MessageManager.filterDialog(this.getMessage()));
				}else if(this.getListeningId() == this.LIST_ID_LEVEL){
					if(Utils.isInteger(this.getMessage())){
						this.getRequired().setLevel(Integer.valueOf(this.getMessage()));
					}
				}
			}
		}

		this.menu.clear();
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_TYPE, this.getRType());
		this.menu.setItem(this.SLOT_DIALOGING, this.getIsDialog());
		this.menu.setItem(this.SLOT_DIALOG, this.getDialog());
		if(this.getRequired().getType().equals(RequiredType.CLASS)){
			this.menu.setItem(this.SLOT_CLASS, this.getRClass());
		}else if(this.getRequired().getType().equals(RequiredType.JOB)){
			this.menu.setItem(this.SLOT_JOB, this.getRJob());
		}else if(this.getRequired().getType().equals(RequiredType.NON_JOB)){
			this.menu.setItem(this.SLOT_JOB, this.getRJob());
		}else if(this.getRequired().getType().equals(RequiredType.QUEST)){
			this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
		}else if(this.getRequired().getType().equals(RequiredType.NON_ACTIVE_QUEST)){
			this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
		}else if(this.getRequired().getType().equals(RequiredType.NON_DONE_QUEST)){
			this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
		}else if(this.getRequired().getType().equals(RequiredType.TIME)){
			this.menu.setItem(this.SLOT_TIME_START, this.getTimeStart());
			this.menu.setItem(this.SLOT_TIME_END, this.getTimeEnd());
		}else if(this.getRequired().getType().equals(RequiredType.ITEM)){
			this.menu.setItem(this.SLOT_ITEM, this.getItem());
		}else if(this.getRequired().getType().equals(RequiredType.MASTERY)){
			this.menu.setItem(this.SLOT_MASTERY, this.getMastery());
		}else if(this.getRequired().getType().equals(RequiredType.LEVEL)){
			this.menu.setItem(this.SLOT_LEVEL, this.getLevel());
		}
		return this.getHolder().openInventory(this.menu) != null;
	}
	

	private ItemStack getIsDialog(){
		ItemStack item = new ItemStack(this.getRequired().isDialog() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Dialoging : " + String.valueOf(this.getRequired().isDialog()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getDialog(){
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getRequired().getNoDialog());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getRClass(){
		ItemStack item = new ItemStack(this.getRequired().getRclass().getDisplay(), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getRequired().getRclass().toString());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getRJob(){
		ItemStack item = new ItemStack(this.getRequired().getRjob().getDisplay(), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getRequired().getRjob().toString());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getQuestTarget(){
		Quest quest = Quest.get(this.getRequired().getQuestTargetUUID());
		ItemMeta meta = ITEM_QUEST.getItemMeta();
		meta.setDisplayName(quest.getColoredTitle());
		List<String> lore = new ArrayList<String>();
		lore.add(quest.getColoredSubtitle());
		for(Objective objective : quest.getObjectives()){
			lore.add("§8" + objective.getType().getToDo() + objective.getInformation());
		}
		meta.setLore(lore);
		ITEM_QUEST.setItemMeta(meta);
		return ITEM_QUEST;
	}
	private ItemStack getMastery(){
		ItemStack ITEM_MASTERY = new ItemStack(Material.ENCHANTING_TABLE, this.getRequired().getMastery().getId()+1);
		ItemMeta meta = ITEM_MASTERY.getItemMeta();
		meta.setDisplayName(this.getRequired().getMastery().toString());
		ITEM_MASTERY.setItemMeta(meta);
		return ITEM_MASTERY;
	}
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	private ItemStack getRType(){
		ItemStack item = new ItemStack(this.getRequired().getType().getDisplay(), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getRequired().getType().toString());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getItem(){
		return this.getRequired().getItemStack();
	}
	private ItemStack getTimeStart(){
		ItemStack item = new ItemStack(Material.CLOCK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Time Start");
		meta.setLore(Arrays.asList(String.valueOf(this.getRequired().getTimeStart())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getTimeEnd(){
		ItemStack item = new ItemStack(Material.CLOCK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Time End");
		meta.setLore(Arrays.asList(String.valueOf(this.getRequired().getTimeEnd())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getLevel(){
		ItemStack item = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Level");
		meta.setLore(Arrays.asList(String.valueOf(this.getRequired().getLevel())));
		item.setItemMeta(meta);
		return item;
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

}
