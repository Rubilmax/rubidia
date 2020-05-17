package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

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
import me.pmilon.RubidiaCore.levels.RLevels;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.quests.RequiredType;
import me.pmilon.RubidiaQuests.quests.Reward;
import me.pmilon.RubidiaQuests.quests.RewardType;

public class RewardEditionMenu extends UIHandler {

	private Quest quest;
	private QuestPNJ pnj;
	private Reward reward;

	private ItemStack ITEM_XP = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
	private ItemStack ITEM_QUEST = new ItemStack(Material.BOOK, 1);
	private ItemStack ITEM_CMD = new ItemStack(Material.MAP, 1);
	
	private int SLOT_TYPE = 0;
	private int SLOT_BACK = 13;
	private int SLOT_CLASS = 1;
	private int SLOT_JOB = 2;
	private int SLOT_ITEM = 3;
	private int SLOT_XP = 4;
	private int SLOT_AMT = 5;
	private int SLOT_MASTERY = 6;
	private int SLOT_QUEST = 7;
	private int SLOT_CMD = 8;
	
	private int LIST_ID_XP = 1;
	private int LIST_ID_CMD = 2;
	private int LIST_ID_ITEM = 3;
	
	public RewardEditionMenu(Player p, Quest quest, QuestPNJ pnj, Reward reward) {
		super(p);
		this.quest = quest;
		this.pnj = pnj;
		this.reward = reward;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.quest.getTitle() + " | Reward " + this.reward.getIndex(),32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "REWARD_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new RewardsEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
		else if(slot == this.SLOT_CLASS){
			if(this.getReward().getRClass().equals(RClass.VAGRANT))this.getReward().setRClass(RClass.ASSASSIN);
			else if(this.getReward().getRClass().equals(RClass.ASSASSIN))this.getReward().setRClass(RClass.MAGE);
			else if(this.getReward().getRClass().equals(RClass.MAGE))this.getReward().setRClass(RClass.PALADIN);
			else if(this.getReward().getRClass().equals(RClass.PALADIN))this.getReward().setRClass(RClass.RANGER);
			else if(this.getReward().getRClass().equals(RClass.RANGER))this.getReward().setRClass(RClass.VAGRANT);
			this.menu.setItem(this.SLOT_CLASS, this.getRClass());
		}else if(slot == this.SLOT_JOB){
			//TODO cycle jobs
			this.menu.setItem(this.SLOT_JOB, this.getRJob());
		}else if(slot == this.SLOT_AMT){
			int amount = 1;
			if(e.isShiftClick())amount = 10;
			if(e.isRightClick()){
				this.getReward().setAmount(this.getReward().getAmount()-amount);
			}else{
				this.getReward().setAmount(this.getReward().getAmount()+amount);
			}
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
		}else if(slot == this.SLOT_XP)this.close(true, this.LIST_ID_XP);
		else if(slot == this.SLOT_CMD)this.close(true, this.LIST_ID_CMD);
		else if(slot == this.SLOT_ITEM)this.close(true, this.LIST_ID_ITEM);
		else if(slot == this.SLOT_MASTERY){
			if(this.getReward().getMastery().equals(Mastery.ADVENTURER))this.getReward().setMastery(Mastery.MASTER);
			else if(this.getReward().getMastery().equals(Mastery.MASTER))this.getReward().setMastery(Mastery.HERO);
			else if(this.getReward().getMastery().equals(Mastery.HERO))this.getReward().setMastery(Mastery.ADVENTURER);
			this.menu.setItem(this.SLOT_MASTERY, this.getMastery());
		}else if(slot == this.SLOT_QUEST){
			Core.uiManager.requestUI(new QuestListRewardChoose(this.getHolder(), this.getQuest(), this.getPnj(), this.getReward()));
		}else if(slot == this.SLOT_TYPE){
			this.menu.clear();
			if(this.getReward().getType().equals(RewardType.CLASS)){
				this.getReward().setType(RewardType.ITEM);
				this.menu.setItem(this.SLOT_ITEM, this.getItem());
			}else if(this.getReward().getType().equals(RewardType.ITEM)){
				this.getReward().setType(RewardType.JOB);
				this.menu.setItem(this.SLOT_JOB, this.getRJob());
			}else if(this.getReward().getType().equals(RewardType.JOB)){
				this.getReward().setType(RewardType.MASTERY);
				this.menu.setItem(this.SLOT_MASTERY, this.getMastery());
			}else if(this.getReward().getType().equals(RewardType.MASTERY)){
				this.getReward().setType(RewardType.MONEY);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
			}else if(this.getReward().getType().equals(RewardType.MONEY)){
				this.getReward().setType(RewardType.QUEST);
				this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
			}else if(this.getReward().getType().equals(RewardType.QUEST)){
				this.getReward().setType(RewardType.SKP);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
			}else if(this.getReward().getType().equals(RewardType.SKP)){
				this.getReward().setType(RewardType.SKD);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
			}else if(this.getReward().getType().equals(RewardType.SKD)){
				this.getReward().setType(RewardType.XP);
				this.menu.setItem(this.SLOT_XP, this.getXP());
			}else if(this.getReward().getType().equals(RewardType.XP)){
				this.getReward().setType(RewardType.COMMAND);
				this.menu.setItem(this.SLOT_CMD, this.getCmd());
			}else if(this.getReward().getType().equals(RewardType.COMMAND)){
				this.getReward().setType(RewardType.CLASS);
				this.menu.setItem(this.SLOT_CLASS, this.getRClass());
			}
			this.menu.setItem(this.SLOT_TYPE, this.getRType());
			this.menu.setItem(this.SLOT_BACK, this.getBack());
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_XP){
					if(this.getMessage().contains("%")) {
						String[] parts = this.getMessage().split("%");
						int level = 0;
						if(parts.length > 1) {
							if(Utils.isInteger(parts[1])){
								level = Integer.valueOf(parts[1]);
							} else rp.sendMessage("§cLe niveau spécifié n'est pas un entier : x%Niveau");
						} else {
							for(Required rq : this.getQuest().getRequireds()){
								if(rq.getType().equals(RequiredType.LEVEL)){
									level = rq.getLevel();
									break;
								}
							}
						}
						
						if(Utils.isDouble(parts[0])){
							double ratio = Integer.valueOf(parts[0])/100.;
							this.getReward().setRExp(ratio*RLevels.getRLevelTotalExp(level));
						} else rp.sendMessage("§cLe pourcentage spécifié n'est pas reconnu : x%Niveau");
					} else {
						if(Utils.isDouble(this.getMessage())){
							this.getReward().setRExp(Double.valueOf(this.getMessage()));
						} else rp.sendMessage("§cLe nombre spécifié n'est pas reconnu : 35,2 = 35.2 (pas de virgule)");
					}
				}
				else if(this.getListeningId() == this.LIST_ID_CMD)this.getReward().setCommand(this.getMessage());
				else if(this.getListeningId() == this.LIST_ID_ITEM){
					ItemStack inhand = this.getHolder().getEquipment().getItemInMainHand();
					if(inhand != null){
						if(!inhand.getType().equals(Material.AIR)){
							this.getReward().setItemStack(inhand.clone());
							this.getReward().setInfoName(this.getMessage());
						}
					}
				}
			}
		}

		this.menu.clear();
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_TYPE, this.getRType());
		if(this.getReward().getType().equals(RewardType.ITEM)){
			this.menu.setItem(this.SLOT_ITEM, this.getItem());
		}else if(this.getReward().getType().equals(RewardType.JOB)){
			this.menu.setItem(this.SLOT_JOB, this.getRJob());
		}else if(this.getReward().getType().equals(RewardType.MASTERY)){
			this.menu.setItem(this.SLOT_MASTERY, this.getMastery());
		}else if(this.getReward().getType().equals(RewardType.QUEST)){
			this.menu.setItem(this.SLOT_QUEST, this.getQuestTarget());
		}else if(this.getReward().getType().equals(RewardType.SKP) || this.getReward().getType().equals(RewardType.SKD) || this.getReward().getType().equals(RewardType.MONEY)){
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
		}else if(this.getReward().getType().equals(RewardType.XP)){
			this.menu.setItem(this.SLOT_XP, this.getXP());
		}else if(this.getReward().getType().equals(RewardType.COMMAND)){
			this.menu.setItem(this.SLOT_CMD, this.getCmd());
		}else if(this.getReward().getType().equals(RewardType.CLASS)){
			this.menu.setItem(this.SLOT_CLASS, this.getRClass());
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
	protected ItemStack getRType(){
		ItemStack ITEM_TYPE = new ItemStack(this.getReward().getType().getDisplay(), 1);
		ItemMeta meta = ITEM_TYPE.getItemMeta();
		meta.setDisplayName(this.getReward().getType().toString());
		ITEM_TYPE.setItemMeta(meta);
		return ITEM_TYPE;
	}
	protected ItemStack getRClass(){
		ItemStack ITEM_CLASS = new ItemStack(this.getReward().getRClass().getDisplay(), 1);
		ItemMeta meta = ITEM_CLASS.getItemMeta();
		meta.setDisplayName(this.getReward().getRClass().toString());
		ITEM_CLASS.setItemMeta(meta);
		return ITEM_CLASS;
	}
	protected ItemStack getRJob(){
		ItemStack ITEM_JOB = new ItemStack(this.getReward().getRJob().getDisplay(), 1);
		ItemMeta meta = ITEM_JOB.getItemMeta();
		meta.setDisplayName(this.getReward().getRJob().toString());
		ITEM_JOB.setItemMeta(meta);
		return ITEM_JOB;
	}
	protected ItemStack getItem(){
		return this.getReward().getItemStack();
	}
	protected ItemStack getXP(){
		ItemMeta meta = ITEM_XP.getItemMeta();
		meta.setDisplayName(String.valueOf(this.getReward().getRExp()));
		ITEM_XP.setItemMeta(meta);
		return ITEM_XP;
	}
	protected ItemStack getCmd(){
		ItemMeta meta = ITEM_CMD.getItemMeta();
		meta.setDisplayName(this.getReward().getCommand());
		ITEM_CMD.setItemMeta(meta);
		return ITEM_CMD;
	}
	protected ItemStack getAmt(){
		ItemStack ITEM_MONEY = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, this.getReward().getAmount() > 0 ? (this.getReward().getAmount() > 64 ? 64 : this.getReward().getAmount()) : 1);
		ItemMeta meta = ITEM_MONEY.getItemMeta();
		meta.setDisplayName(String.valueOf(this.getReward().getAmount()));
		ITEM_MONEY.setItemMeta(meta);
		return ITEM_MONEY;
	}
	protected ItemStack getMastery(){
		ItemStack ITEM_MASTERY = new ItemStack(Material.ENCHANTING_TABLE, this.getReward().getMastery().getId()+1);
		ItemMeta meta = ITEM_MASTERY.getItemMeta();
		meta.setDisplayName(this.getReward().getMastery().toString());
		ITEM_MASTERY.setItemMeta(meta);
		return ITEM_MASTERY;
	}
	protected ItemStack getQuestTarget(){
		ItemMeta meta = ITEM_QUEST.getItemMeta();
		meta.setDisplayName(this.getReward().getQuestTarget().getColoredTitle());
		meta.setLore(Arrays.asList(this.getReward().getQuestTarget().getColoredSubtitle()));
		ITEM_QUEST.setItemMeta(meta);
		return ITEM_QUEST;
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

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

}
