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
import me.pmilon.RubidiaMonsters.regions.Drop;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.utils.Configs;

public class MonsterDropsEditionUI extends UIHandler {

	private Region region;
	private Monster monster;
	
	private int SLOT_BACK = 8;
	public MonsterDropsEditionUI(Player p, Region region, Monster monster) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, "Monster Drops Edition");
		this.region = region;
		this.monster = monster;
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "MONSTER_DROPS_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK) {
			Core.uiManager.requestUI(new MonsterEditionMenu(this.getHolder(), this.getRegion(), this.getMonster()));
		} else {
			if(e.isRightClick()){
				this.getMonster().getDrops().remove(slot);
				Configs.getMonstersConfig().set("monsters." + this.getMonster().getUUID() + ".drops." + slot, null);
				Configs.saveMonstersConfig();
				Core.uiManager.requestUI(this);
			}else this.close(true, slot+1);
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				ItemStack is = this.getHolder().getEquipment().getItemInMainHand();
				if(Utils.isDouble(this.getMessage()) && is != null){
					if(!is.getType().equals(Material.AIR)){
						double probability = Double.valueOf(this.getMessage());
						if(this.getMonster().getDrops().size() > this.getListeningId()-1)this.getMonster().getDrops().set(this.getListeningId()-1, new Drop(this.getListeningId()-1, is, probability));
						else this.getMonster().getDrops().add(new Drop(this.getListeningId()-1, is, probability));
					}
				}
			}
		}
		
		for(int i = 0;i < 8;i++){
			ItemStack item;
			if(this.getMonster().getDrops().size() > i)item = this.getDrop(this.getMonster().getDrops().get(i));
			else item = this.getDrop(null);
			this.menu.setItem(i, item);
		}
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}


	protected ItemStack getDrop(Drop drop){
		ItemStack item;
		if(drop != null){
			item = drop.getItem().clone();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(String.valueOf(drop.getProbability()));
			item.setItemMeta(meta);
		}else{
			item = new ItemStack(Material.SLIME_BALL, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("NOTHING");
			item.setItemMeta(meta);
		}
		return item;
	}
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

}
