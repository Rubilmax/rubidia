package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.QEvent;
import me.pmilon.RubidiaQuests.quests.QEventType;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.utils.WE;

public class QEventEditionMenu extends UIHandler {


	private Quest quest;
	private QuestPNJ pnj;
	private QEvent qEvent;

	private ItemStack ITEM_RNG = new ItemStack(Material.MAP, 1);
	private ItemStack ITEM_LOC = new ItemStack(Material.COMPASS, 1);
	private ItemStack ITEM_EFF = new ItemStack(Material.POTION, 1);
	private ItemStack ITEM_BLK = new ItemStack(Material.BRICK, 1);
	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	
	private int SLOT_TYPE = 0;
	private int SLOT_AMT = 1;
	private int SLOT_RNG = 2;
	private int SLOT_LOC = 3;
	private int SLOT_MST = 4;
	private int SLOT_LVL = 5;
	private int SLOT_EFF = 6;
	private int SLOT_BLK = 7;
	private int SLOT_ITEM = 8;
	private int SLOT_BACK = 17;
	
	private int LIST_ID_RNG = 1;
	private int LIST_ID_LOC = 2;
	private int LIST_ID_EFF = 3;
	private int LIST_ID_BLK = 4;
	private int LIST_ID_ITEM = 5;
	public QEventEditionMenu(Player p, Quest quest, QuestPNJ pnj, QEvent qEvent) {
		super(p);
		this.quest = quest;
		this.pnj = pnj;
		this.qEvent = qEvent;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.quest.getTitle() + " | QEvent " + this.getQEvent().getIndex(),32));
	}
	
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "QEVENT_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK){
			Core.uiManager.requestUI(new QEventsEditionUI(this.getHolder(), this.getQuest(), this.getPnj()));
		}else if(slot == this.SLOT_BLK)this.close(true, this.LIST_ID_BLK);
		else if(slot == this.SLOT_EFF)this.close(true, this.LIST_ID_EFF);
		else if(slot == this.SLOT_MST)Core.uiManager.requestUI(new QEventMonsterSelectMenu(this.getHolder(), this.getPnj(), this.getQuest(), this.getQEvent()));
		else if(slot == this.SLOT_LOC)this.close(true, this.LIST_ID_LOC);
		else if(slot == this.SLOT_RNG)this.close(true, this.LIST_ID_RNG);
		else if(slot == this.SLOT_ITEM)this.close(true, this.LIST_ID_ITEM);
		else if(slot == this.SLOT_AMT){
			int amount = 1;
			if(e.isShiftClick())amount = 10;
			if(e.isRightClick()){
				this.getQEvent().setAmount(this.getQEvent().getAmount()-amount);
			}else{
				this.getQEvent().setAmount(this.getQEvent().getAmount()+amount);
			}
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
		}else if(slot == this.SLOT_LVL){
			int amount = 1;
			if(e.isShiftClick())amount = 10;
			if(e.isRightClick()){
				this.getQEvent().setMonsterLevel(this.getQEvent().getMonsterLevel()-amount);
			}else{
				this.getQEvent().setMonsterLevel(this.getQEvent().getMonsterLevel()+amount);
			}
			this.menu.setItem(this.SLOT_LVL, this.getLvl());
		}else if(slot == this.SLOT_TYPE){
			this.menu.clear();
			if(this.getQEvent().getType().equals(QEventType.BLOCKS)){
				this.getQEvent().setType(QEventType.EFFECT);
				this.menu.setItem(this.SLOT_EFF, this.getEff());
			}else if(this.getQEvent().getType().equals(QEventType.EFFECT)){
				this.getQEvent().setType(QEventType.SPAWN);
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
				this.menu.setItem(this.SLOT_MST, this.getMst());
				this.menu.setItem(this.SLOT_LVL, this.getLvl());
				this.menu.setItem(this.SLOT_LOC, this.getLoc());
				this.menu.setItem(this.SLOT_RNG, this.getRng());
			}else if(this.getQEvent().getType().equals(QEventType.SPAWN)){
				this.getQEvent().setType(QEventType.TELEPORTATION);
				this.menu.setItem(this.SLOT_LOC, this.getLoc());
			}else if(this.getQEvent().getType().equals(QEventType.TELEPORTATION)){
				this.getQEvent().setType(QEventType.ITEM);
				this.menu.setItem(this.SLOT_ITEM, this.getQEvent().getItemStack().clone());
			}else if(this.getQEvent().getType().equals(QEventType.ITEM)){
				this.getQEvent().setType(QEventType.BLOCKS);
				this.menu.setItem(this.SLOT_BLK, this.getBlk());
				this.menu.setItem(this.SLOT_AMT, this.getAmt());
			}
			this.menu.setItem(this.SLOT_TYPE, this.getQType());
			this.menu.setItem(this.SLOT_BACK, this.getBack());
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_LOC){
					this.getQEvent().setLocation(this.getHolder().getLocation());
				}else if(this.getListeningId() == this.LIST_ID_BLK){
					LocalSession session = QuestsPlugin.worldEdit.getSession(this.getHolder());
					if(session != null) {
						if(session.getSelectionWorld() != null){
							if(session.isSelectionDefined(session.getSelectionWorld())){
								try {
									this.getQEvent().setBlocks(WE.getBlocks(this.getHolder().getWorld(), session.getSelection(session.getSelectionWorld())));
								} catch (IncompleteRegionException e) {
									rp.sendMessage("§cSélectionnez une région complète");
								}
							}else rp.sendMessage("§cSélectionnez une région complète");
						}else rp.sendMessage("§cSélectionnez une région complète");
					}else rp.sendMessage("§cSélectionnez une région complète");
				}else if(this.getListeningId() == this.LIST_ID_RNG){
					this.getQEvent().setRange(Double.valueOf(this.getMessage()));
				}else if(this.getListeningId() == this.LIST_ID_EFF){
					String[] part = this.getMessage().split(" ");
					if(part.length > 3){
						PotionEffectType type = PotionEffectType.getByName(part[0]);
						int amplifier = Integer.valueOf(part[1]);
						int duration = (int) (Double.valueOf(part[2])*20);
						boolean particles = Boolean.valueOf(part[3]);
						this.getQEvent().setPotionEffect(new PotionEffect(type, duration, amplifier, true, particles));
					}
				}else if(this.getListeningId() == this.LIST_ID_ITEM){
					if(this.getHolder().getEquipment().getItemInMainHand() != null){
						if(!this.getHolder().getEquipment().getItemInMainHand().getType().equals(Material.AIR)){
							this.getQEvent().setItemStack(this.getHolder().getEquipment().getItemInMainHand().clone());
						}
					}
				}
			}
		}

		this.menu.clear();
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_TYPE, this.getQType());
		if(this.getQEvent().getType().equals(QEventType.BLOCKS)){
			this.menu.setItem(this.SLOT_BLK, this.getBlk());
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
		}else if(this.getQEvent().getType().equals(QEventType.EFFECT)){
			this.menu.setItem(this.SLOT_EFF, this.getEff());
		}else if(this.getQEvent().getType().equals(QEventType.SPAWN)){
			this.menu.setItem(this.SLOT_AMT, this.getAmt());
			this.menu.setItem(this.SLOT_MST, this.getMst());
			this.menu.setItem(this.SLOT_LVL, this.getLvl());
			this.menu.setItem(this.SLOT_LOC, this.getLoc());
			this.menu.setItem(this.SLOT_RNG, this.getRng());
		}else if(this.getQEvent().getType().equals(QEventType.TELEPORTATION)){
			this.menu.setItem(this.SLOT_LOC, this.getLoc());
		}else if(this.getQEvent().getType().equals(QEventType.ITEM)){
			this.menu.setItem(this.SLOT_ITEM, this.getQEvent().getItemStack().clone());
		}
		return this.getHolder().openInventory(this.menu) != null;
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

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}
	
	
	protected ItemStack getBack(){
		ItemMeta meta = ITEM_BACK.getItemMeta();
		meta.setDisplayName("Get Back");
		ITEM_BACK.setItemMeta(meta);
		return ITEM_BACK;
	}
	protected ItemStack getQType(){
		ItemStack ITEM_TYPE = new ItemStack(this.getQEvent().getType().getDisplay(), 1);
		ItemMeta meta = ITEM_TYPE.getItemMeta();
		meta.setDisplayName(this.getQEvent().getType().toString());
		ITEM_TYPE.setItemMeta(meta);
		return ITEM_TYPE;
	}
	protected ItemStack getAmt(){
		ItemStack ITEM_MONEY = new ItemStack(Material.LIME_STAINED_GLASS_PANE, this.getQEvent().getAmount() > 0 ? (this.getQEvent().getAmount() > 64 ? 64 : this.getQEvent().getAmount()) : 1);
		ItemMeta meta = ITEM_MONEY.getItemMeta();
		meta.setDisplayName(String.valueOf(this.getQEvent().getAmount()));
		ITEM_MONEY.setItemMeta(meta);
		return ITEM_MONEY;
	}
	protected ItemStack getRng(){
		ItemMeta meta = ITEM_RNG.getItemMeta();
		meta.setDisplayName(String.valueOf(this.getQEvent().getRange()));
		ITEM_RNG.setItemMeta(meta);
		return ITEM_RNG;
	}
	protected ItemStack getLoc(){
		Location location = this.getQEvent().getLocation();
		ItemMeta meta = ITEM_LOC.getItemMeta();
		meta.setDisplayName(location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
		ITEM_LOC.setItemMeta(meta);
		return ITEM_LOC;
	}
	protected ItemStack getMst(){
		Monster monster = this.getQEvent().getMonster();
		ItemStack ITEM_ENT = new ItemStack(Material.valueOf((monster != null ? monster.getType() : EntityType.PIG).toString() + "_SPAWN_EGG"), 1);
		ItemMeta meta = ITEM_ENT.getItemMeta();
		meta.setDisplayName(monster != null ? monster.getName() : "");
		ITEM_ENT.setItemMeta(meta);
		return ITEM_ENT;
	}
	protected ItemStack getEff(){
		PotionEffect effect = this.getQEvent().getPotionEffect();
		ItemMeta meta = ITEM_EFF.getItemMeta();
		meta.setDisplayName(effect.getType().toString() + " " + effect.getAmplifier() + " " + (effect.getDuration()/20) + "sec " + (effect.hasParticles() ? "particles" : "no particles"));
		ITEM_EFF.setItemMeta(meta);
		return ITEM_EFF;
	}
	protected ItemStack getLvl(){
		ItemStack ITEM_MONEY = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, this.getQEvent().getMonsterLevel() > 0 ? (this.getQEvent().getMonsterLevel() > 64 ? 64 : this.getQEvent().getMonsterLevel()) : 1);
		ItemMeta meta = ITEM_MONEY.getItemMeta();
		meta.setDisplayName(String.valueOf(this.getQEvent().getMonsterLevel()));
		ITEM_MONEY.setItemMeta(meta);
		return ITEM_MONEY;
	}
	protected ItemStack getBlk(){
		List<Block> blocks = this.getQEvent().getBlocks();
		ItemMeta meta = ITEM_BLK.getItemMeta();
		meta.setDisplayName("Blocks");
		List<String> lore = new ArrayList<String>();
		for(Block block : blocks){
			lore.add(block.getX() + " " + block.getY() + " " + block.getZ());
		}
		meta.setLore(lore);
		ITEM_BLK.setItemMeta(meta);
		return ITEM_BLK;
	}

}
