package me.pmilon.RubidiaMonsters.ui.monsters;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.attacks.AbstractAttack;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Region;

public class MonsterAttackEditionMenu extends UIHandler {

	private Region region;
	private Monster monster;
	private AbstractAttack attack;
	
	private int SLOT_TYPE = 0, SLOT_COOLDOWN = 1, SLOT_LEARNING_FACTOR = 2, SLOT_BACK = 8;
	private int LIST_ID_CLD = 1,LIST_ID_LRNG = 2;
	public MonsterAttackEditionMenu(Player p, Region region, Monster monster, AbstractAttack attack) {
		super(p);
		this.region = region;
		this.monster = monster;
		this.attack = attack;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, "Attack Edition Menu");
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "ATTACK_EDTION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_TYPE){
			
		}else if(slot == this.SLOT_COOLDOWN) {
			this.close(true, this.LIST_ID_CLD);
		} else if(slot == this.SLOT_LEARNING_FACTOR) {
			this.close(true, this.LIST_ID_LRNG);
		} else if(slot == this.SLOT_BACK) {
			Core.uiManager.requestUI(new MonsterEditionMenu(this.getHolder(), this.getRegion(), this.getMonster()));
		}
	}

	public Region getRegion() {
		return this.region;
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(this.getListeningId() == this.LIST_ID_CLD){
				if(Utils.isInteger(this.getMessage())){
					this.getAttack().setCooldown(Integer.valueOf(this.getMessage()));
				}else this.getHolder().sendMessage("§cPlease specify a cooldown in ticks.");
			}else if(this.getListeningId() == this.LIST_ID_LRNG){
				if(Utils.isDouble(this.getMessage())){
					this.getAttack().setLearningFactor(Double.valueOf(this.getMessage()));
				}else this.getHolder().sendMessage("§cPlease specify a valid learning factor.");
			}
		}
		
		this.menu.setItem(this.SLOT_TYPE, this.getAType());
		this.menu.setItem(this.SLOT_COOLDOWN, this.getCld());
		this.menu.setItem(this.SLOT_LEARNING_FACTOR, this.getLrngFactor());
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	public ItemStack getAType(){
		ItemStack item = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Type: " + this.getAttack().getType().toString());
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getCld(){
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Cooldown: " + String.valueOf(this.getAttack().getCooldown()));
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getLrngFactor(){
		ItemStack item = new ItemStack(Material.APPLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Learning factor: " + String.valueOf(this.getAttack().getLearningFactor()));
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public AbstractAttack getAttack() {
		return attack;
	}

	public void setAttack(AbstractAttack attack) {
		this.attack = attack;
	}

}
