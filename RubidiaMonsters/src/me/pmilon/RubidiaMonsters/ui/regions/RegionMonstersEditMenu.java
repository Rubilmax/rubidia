package me.pmilon.RubidiaMonsters.ui.regions;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.ui.monsters.MonsterEditionMenu;
import me.pmilon.RubidiaMonsters.ui.monsters.MonstersUI;

public class RegionMonstersEditMenu extends UIHandler {

	private Region region;
	
	private int SLOT_BACK = 8;
	public RegionMonstersEditMenu(Player p, Region region) {
		super(p);
		this.region = region;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, "Region Monsters");
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "REGION_MONSTERS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new RegionManager(this.getHolder(), this.getRegion()));
		else if(this.getRegion().getMonsters().size() > slot){
			if(e.isRightClick()){
				this.getRegion().getMonsters().remove(slot);
				Core.uiManager.requestUI(this);
			}else Core.uiManager.requestUI(new MonsterEditionMenu(this.getHolder(), this.getRegion(), this.getRegion().getMonsters().get(slot)));
		}else Core.uiManager.requestUI(new MonstersUI(this.getHolder(), this.getRegion()));
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < 8;i++){
			ItemStack item;
			if(this.getRegion().getMonsters().size() > i)item = this.getMob(this.getRegion().getMonsters().get(i));
			else item = this.getMob(null);
			this.menu.setItem(i, item);
		}
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}


	protected ItemStack getMob(Monster monster){
		ItemStack item;
		if(monster != null){
			item = new ItemStack(Material.valueOf(monster.getType().toString() + "_SPAWN_EGG"), 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(monster.getName());
			meta.setLore(Arrays.asList(monster.getType().toString()));
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
}
