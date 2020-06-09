package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.Quest;

public class ObjectiveEditionMenu extends UIHandler {

	private Quest quest;
	private QuestPNJ pnj;
	private Objective objective;
	
	private ItemStack ITEM_LOC = new ItemStack(Material.COMPASS, 1);
	private ItemStack ITEM_NAME = new ItemStack(Material.MAP, 1);
	private ItemStack ITEM_DIALOGS = new ItemStack(Material.BOOK, 1);
	private ItemStack ITEM_SIDEQUEST = new ItemStack(Material.BOOK, 1);

	private int SLOT_BACK = 13;
	private int SLOT_ITEM =1;
	private int SLOT_LOC = 4;
	private int SLOT_MAT = 2;
	private int SLOT_AMT = 3;
	private int SLOT_TYPE = 0;
	private int SLOT_ENT = 5;
	private int SLOT_NAME = 6;
	private int SLOT_TAKE = 7;
	private int SLOT_DIALOGS = 8;
	private int SLOT_SIDEQUEST = 10;
	
	private int LIST_ID_LOC = 0;
	private int LIST_ID_NAME = 1;
	private int LIST_ID_MAT = 2;
	private int LIST_ID_ITEM = 3;
	public ObjectiveEditionMenu(Player p, Quest quest, QuestPNJ pnj, Objective objective) {
		super(p);
		this.quest = quest;
		this.pnj = pnj;
		this.objective = objective;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.quest.getTitle() + " | Objective " + this.objective.getIndex(), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "OBJECTIVE_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new ObjectivesEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
		else if(slot == this.SLOT_AMT){
			int amount = 1;
			if(e.isShiftClick())amount = 10;
			if(e.isRightClick()){
				this.getObjective().setAmount(this.getObjective().getAmount()-amount);
			}else{
				this.getObjective().setAmount(this.getObjective().getAmount()+amount);
			}
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
		}else if(slot == this.SLOT_ENT)Core.uiManager.requestUI(new ObjectiveMonsterSelectMenu(this.getHolder(), this.getPnj(), this.getQuest(), this.getObjective()));
		else if(slot == this.SLOT_MAT)this.close(true, this.LIST_ID_MAT);
		else if(slot == this.SLOT_ITEM)this.close(true, this.LIST_ID_ITEM);
		else if(slot == this.SLOT_LOC)this.close(true, this.LIST_ID_LOC);
		else if(slot == this.SLOT_NAME)this.close(true, this.LIST_ID_NAME);
		else if(slot == this.SLOT_TYPE){
			this.menu.clear();
			if(this.getObjective().getType().equals(ObjectiveType.CRAFT)){
				this.getObjective().setType(ObjectiveType.DISCOVER);
				this.menu.setItem(this.SLOT_LOC, this.getLoc());
			}else if(this.getObjective().getType().equals(ObjectiveType.DISCOVER)){
				this.getObjective().setType(ObjectiveType.GET);
				this.menu.setItem(this.SLOT_ITEM, this.getItem());
				this.menu.setItem(this.SLOT_TAKE, this.getTakeItem());
				this.menu.setItem(this.SLOT_NAME, this.getName());
				this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
			}else if(this.getObjective().getType().equals(ObjectiveType.GET)){
				this.getObjective().setType(ObjectiveType.KILL);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
				this.menu.setItem(this.SLOT_ENT, this.getEnt());
			}else if(this.getObjective().getType().equals(ObjectiveType.KILL)){
				this.getObjective().setType(ObjectiveType.MINE);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
				this.menu.setItem(this.SLOT_MAT, this.getMat());
			}else if(this.getObjective().getType().equals(ObjectiveType.MINE)){
				this.getObjective().setType(ObjectiveType.TALK);
				this.menu.setItem(this.SLOT_NAME, this.getName());
				this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
			}else if(this.getObjective().getType().equals(ObjectiveType.TALK)){
				this.getObjective().setType(ObjectiveType.FISH);
				this.menu.setItem(this.SLOT_ITEM, this.getItem());
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
			}else if(this.getObjective().getType().equals(ObjectiveType.FISH)){
				this.getObjective().setType(ObjectiveType.LEASH);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
				this.menu.setItem(this.SLOT_ENT, this.getEnt());
				this.menu.setItem(this.SLOT_NAME, this.getName());
				this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
			}else if(this.getObjective().getType().equals(ObjectiveType.LEASH)){
				this.getObjective().setType(ObjectiveType.FOLLOW);
				this.menu.setItem(this.SLOT_LOC, this.getLoc());
				this.menu.setItem(this.SLOT_NAME, this.getName());
				this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
			}else if(this.getObjective().getType().equals(ObjectiveType.FOLLOW)){
				this.getObjective().setType(ObjectiveType.TIME);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
			}else if(this.getObjective().getType().equals(ObjectiveType.TIME)){
				this.getObjective().setType(ObjectiveType.SIDE_QUEST);
				this.menu.setItem(this.SLOT_NAME, this.getName());
				this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
				this.menu.setItem(this.SLOT_SIDEQUEST, this.getSideQuest());
			}else if(this.getObjective().getType().equals(ObjectiveType.SIDE_QUEST)){
				this.getObjective().setType(ObjectiveType.CRAFT);
				this.menu.setItem(this.SLOT_ITEM, this.getItem());
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
			}
			this.menu.setItem(slot, this.getOType());
			this.menu.setItem(this.SLOT_BACK, this.getBack());
		}else if(slot == this.SLOT_TAKE){
			this.getObjective().setTakeItem(!this.getObjective().isTakeItem());
			this.menu.setItem(slot, this.getTakeItem());
		}else if(slot == this.SLOT_DIALOGS)Core.uiManager.requestUI(new QuestObjectiveDialogsMenu(this.getHolder(), this.getPnj(), this.getQuest(), this.getObjective()));
		else if(slot == this.SLOT_SIDEQUEST)Core.uiManager.requestUI(new QuestListObjectiveChooseUI(this.getHolder(), this.getQuest(), this.getPnj(), this.getObjective()));
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_NAME){
					if(QuestsPlugin.pnjManager.getPNJUUID(this.getMessage()) != null){
						this.getObjective().setName(this.getMessage());
						this.getObjective().setTargetUUID(QuestsPlugin.pnjManager.getPNJUUID(this.getMessage()));
					}
				}else if(this.getListeningId() == this.LIST_ID_LOC){
					this.getObjective().setLocation(this.getHolder().getLocation());
					this.getObjective().setInfoName(this.getMessage());
				}else if(this.getListeningId() == this.LIST_ID_MAT){
					if(this.getHolder().getEquipment().getItemInMainHand() != null){
						if(!this.getHolder().getEquipment().getItemInMainHand().getType().equals(Material.AIR)){
							this.getObjective().setMaterial(this.getHolder().getEquipment().getItemInMainHand().getType());
							this.getObjective().setInfoName(this.getMessage());
							this.menu.setItem(this.SLOT_MAT, this.getMat());
						}
					}
				}else if(this.getListeningId() == this.LIST_ID_ITEM){
					if(this.getHolder().getItemInHand() != null){
						if(!this.getHolder().getItemInHand().getType().equals(Material.AIR)){
							this.getObjective().setItemStack(this.getHolder().getItemInHand().clone());
							this.getObjective().setInfoName(this.getMessage());
						}
					}
				}
			}
		}
		
		this.menu.clear();
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_TYPE, this.getOType());
		if(this.getObjective().getType().equals(ObjectiveType.DISCOVER)){
			this.menu.setItem(this.SLOT_LOC, this.getLoc());
		}else if(this.getObjective().getType().equals(ObjectiveType.GET)){
			this.menu.setItem(this.SLOT_ITEM, this.getItem());
			this.menu.setItem(this.SLOT_TAKE, this.getTakeItem());
			this.menu.setItem(this.SLOT_NAME, this.getName());
			this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
		}else if(this.getObjective().getType().equals(ObjectiveType.KILL)){
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
			this.menu.setItem(this.SLOT_ENT, this.getEnt());
		}else if(this.getObjective().getType().equals(ObjectiveType.MINE)){
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
			this.menu.setItem(this.SLOT_MAT, this.getMat());
		}else if(this.getObjective().getType().equals(ObjectiveType.TALK)){
			this.menu.setItem(this.SLOT_NAME, this.getName());
			this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
		}else if(this.getObjective().getType().equals(ObjectiveType.CRAFT) || this.getObjective().getType().equals(ObjectiveType.FISH)){
			this.menu.setItem(this.SLOT_ITEM, this.getItem());
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
		}else if(this.getObjective().getType().equals(ObjectiveType.LEASH)){
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
			this.menu.setItem(this.SLOT_ENT, this.getEnt());
			this.menu.setItem(this.SLOT_NAME, this.getName());
			this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
		}else if(this.getObjective().getType().equals(ObjectiveType.FOLLOW)){
			this.menu.setItem(this.SLOT_LOC, this.getLoc());
			this.menu.setItem(this.SLOT_NAME, this.getName());
			this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
		}else if(this.getObjective().getType().equals(ObjectiveType.TIME)){
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
		}else if(this.getObjective().getType().equals(ObjectiveType.SIDE_QUEST)){
			this.menu.setItem(this.SLOT_NAME, this.getName());
			this.menu.setItem(this.SLOT_DIALOGS, this.getDialogs());
			this.menu.setItem(this.SLOT_SIDEQUEST, this.getSideQuest());
		}
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	
	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	protected ItemStack getItem(){
		return this.getObjective().getItemStack();
	}
	protected ItemStack getLoc(){
		Location location = this.getObjective().getLocation();
		ItemMeta meta = ITEM_LOC.getItemMeta();
		meta.setDisplayName(location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
		ITEM_LOC.setItemMeta(meta);
		return ITEM_LOC;
	}
	protected ItemStack getMat(){
		ItemStack ITEM_MAT = new ItemStack(this.getObjective().getMaterial(), 1);
		ItemMeta meta = ITEM_MAT.getItemMeta();
		meta.setDisplayName(this.getObjective().getMaterial().toString());
		ITEM_MAT.setItemMeta(meta);
		return ITEM_MAT;
	}
	protected ItemStack getAmt(){
		ItemStack ITEM_AMT = new ItemStack(Material.LIME_STAINED_GLASS_PANE, this.getObjective().getAmount() > 0 ? this.getObjective().getAmount() : 1);
		ItemMeta meta = ITEM_AMT.getItemMeta();
		meta.setDisplayName(String.valueOf(this.getObjective().getAmount()));
		ITEM_AMT.setItemMeta(meta);
		return ITEM_AMT;
	}
	protected ItemStack getOType(){
		ItemStack ITEM_TYPE = new ItemStack(this.getObjective().getType().getDisplay(), 1);
		ItemMeta meta = ITEM_TYPE.getItemMeta();
		meta.setDisplayName(this.getObjective().getType().toString());
		ITEM_TYPE.setItemMeta(meta);
		return ITEM_TYPE;
	}
	protected ItemStack getEnt(){
		Monster monster = this.getObjective().getMonster();
		ItemStack ITEM_ENT = new ItemStack(Material.valueOf((monster != null ? monster.getType() : EntityType.PIG).toString() + "_SPAWN_EGG"), 1);
		ItemMeta meta = ITEM_ENT.getItemMeta();
		meta.setDisplayName(monster != null ? monster.getName() : "");
		ITEM_ENT.setItemMeta(meta);
		return ITEM_ENT;
	}
	protected ItemStack getName(){
		ItemMeta meta = ITEM_NAME.getItemMeta();
		meta.setDisplayName(this.getObjective().getName());
		ITEM_NAME.setItemMeta(meta);
		return ITEM_NAME;
	}
	protected ItemStack getTakeItem(){
		ItemStack item = new ItemStack(this.getObjective().isTakeItem() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Take Item : " + String.valueOf(this.getObjective().isTakeItem()));
		item.setItemMeta(meta);
		return item;
	}
	protected ItemStack getDialogs(){
		ItemMeta meta = ITEM_DIALOGS.getItemMeta();
		meta.setDisplayName("Talk Dialogs");
		meta.setLore(this.getObjective().getDialogs());
		ITEM_DIALOGS.setItemMeta(meta);
		return ITEM_DIALOGS;
	}
	protected ItemStack getSideQuest(){
		ItemMeta meta = ITEM_SIDEQUEST.getItemMeta();
		if(this.getObjective().getSideQuest() != null){
			meta.setDisplayName(this.getObjective().getSideQuest().getColoredTitle());
			meta.setLore(Arrays.asList(this.getObjective().getSideQuest().getColoredSubtitle()));
		}
		ITEM_SIDEQUEST.setItemMeta(meta);
		return ITEM_SIDEQUEST;
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

}
