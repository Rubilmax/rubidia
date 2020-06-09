package me.pmilon.RubidiaMonsters.ui.regions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Region.RegionType;
import me.pmilon.RubidiaMonsters.regions.Regions;

@SuppressWarnings("deprecation")
public class RegionManager extends UIHandler {

	private Region region;
	
	private int SLOT_CENTER = 0, SLOT_X = 1, SLOT_Y = 2, SLOT_Z = 3, SLOT_MONSTERS = 4, SLOT_MINLVL = 5, SLOT_MAXLVL = 6, SLOT_YSHIFT = 7, SLOT_MAXMOBS = 8, SLOT_SQUARE = 10, SLOT_FADINGLVL = 11, SLOT_RAGE = 12, SLOT_DELETE = 15, SLOT_EMPTY = 17, SLOT_TYPE = 13;
	private int LIST_ID_CENTER = 1, LIST_ID_X = 2, LIST_ID_Y = 3, LIST_ID_Z = 4, LIST_ID_MINLVL = 5, LIST_ID_MAXLVL = 6, LIST_ID_YSHIFT = 7, LIST_ID_MAXMOBS = 8, LIST_ID_RAGE = 9;
	public RegionManager(Player p, Region region) {
		super(p);
		this.region = region;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, "Region Manager");
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "REGION_MANAGER_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_CENTER)this.close(true, this.LIST_ID_CENTER);
		else if(slot == this.SLOT_X)this.close(true, this.LIST_ID_X);
		else if(slot == this.SLOT_Y)this.close(true, this.LIST_ID_Y);
		else if(slot == this.SLOT_Z)this.close(true, this.LIST_ID_Z);
		else if(slot == this.SLOT_MONSTERS)Core.uiManager.requestUI(new RegionMonstersEditMenu(this.getHolder(), this.getRegion()));
		else if(slot == this.SLOT_MINLVL)this.close(true, this.LIST_ID_MINLVL);
		else if(slot == this.SLOT_MAXLVL)this.close(true, this.LIST_ID_MAXLVL);
		else if(slot == this.SLOT_YSHIFT)this.close(true, this.LIST_ID_YSHIFT);
		else if(slot == this.SLOT_MAXMOBS)this.close(true, this.LIST_ID_MAXMOBS);
		else if(slot == this.SLOT_RAGE)this.close(true, this.LIST_ID_RAGE);
		else if(slot == this.SLOT_SQUARE){
			this.getRegion().setSquare(!this.getRegion().isSquare());
			this.getRegion().setMaxMonstersAmount((int) Math.round(this.getRegion().getSize()/312));
			this.menu.setItem(this.SLOT_SQUARE, this.getSquare());
		}else if(slot == this.SLOT_FADINGLVL){
			this.getRegion().setFadingLevel(!this.getRegion().isFadingLevel());
			this.menu.setItem(this.SLOT_FADINGLVL, this.getFading());
		}else if(slot == this.SLOT_DELETE){
			Regions.regions.remove(this.getRegion());
			RubidiaMonstersPlugin.getInstance().getConfig().set("regions." + this.getRegion().getUUID(), null);
			RubidiaMonstersPlugin.getInstance().saveConfig();
			this.closeUI();
		}else if(slot == this.SLOT_EMPTY){
			this.getRegion().clear();
		}else if(slot == this.SLOT_TYPE){
			this.getRegion().setType(RegionType.values()[(Arrays.asList(RegionType.values()).indexOf(this.getRegion().getType())+1)%RegionType.values().length]);
			this.getMenu().setItem(this.SLOT_TYPE, this.getRType());
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_CENTER)this.getRegion().setCenter(LocationUtils.getCenter(this.getHolder().getLocation()));
				else if(this.getListeningId() == this.LIST_ID_X){
					if(Utils.isDouble(this.getMessage())){
						double x = Double.valueOf(this.getMessage());
						if(x > 0){
							this.getRegion().setXRange(x);
							this.getRegion().setMaxMonstersAmount((int) Math.round(this.getRegion().getSize()/312));
						}else this.getHolder().sendMessage("§cRange must be > 0!");
					}
				}else if(this.getListeningId() == this.LIST_ID_Y){
					if(Utils.isDouble(this.getMessage())){
						double y = Double.valueOf(this.getMessage());
						this.getRegion().setYRange(y);
						this.getRegion().setMaxMonstersAmount((int) Math.round(this.getRegion().getSize()/312));
					}
				}else if(this.getListeningId() == this.LIST_ID_Z){
					if(Utils.isDouble(this.getMessage())){
						double z = Double.valueOf(this.getMessage());
						if(z > 0){
							this.getRegion().setZRange(z);
							this.getRegion().setMaxMonstersAmount((int) Math.round(this.getRegion().getSize()/312));
						}else this.getHolder().sendMessage("§cRange must be > 0!");
					}
				}else if(this.getListeningId() == this.LIST_ID_RAGE){
					if(Utils.isDouble(this.getMessage())){
						double rage = Double.valueOf(this.getMessage());
						if(rage >= 0){
							this.getRegion().setRageProbability(rage);
						}else this.getHolder().sendMessage("§cRage probability must be >= 0!");
					}
				}else if(this.getListeningId() == this.LIST_ID_MINLVL){
					if(Utils.isInteger(this.getMessage())){
						int min = Integer.valueOf(this.getMessage());
						this.getRegion().setMinLevel(min);
					}
				}else if(this.getListeningId() == this.LIST_ID_MAXLVL){
					if(Utils.isInteger(this.getMessage())){
						int max = Integer.valueOf(this.getMessage());
						this.getRegion().setMaxLevel(max);
					}
				}else if(this.getListeningId() == this.LIST_ID_YSHIFT){
					if(Utils.isDouble(this.getMessage())){
						double shift = Double.valueOf(this.getMessage());
						this.getRegion().setYShift(shift);
					}
				}else if(this.getListeningId() == this.LIST_ID_MAXMOBS){
					if(Utils.isInteger(this.getMessage())){
						int max = Integer.valueOf(this.getMessage());
						this.getRegion().setMaxMonstersAmount(max);
					}
				}
			}
		}
		
		this.menu.setItem(this.SLOT_CENTER, this.getCenter());
		this.menu.setItem(this.SLOT_X, this.getX());
		this.menu.setItem(this.SLOT_Y, this.getY());
		this.menu.setItem(this.SLOT_Z, this.getZ());
		this.menu.setItem(this.SLOT_MONSTERS, this.getMobs());
		this.menu.setItem(this.SLOT_MINLVL, this.getMinLvl());
		this.menu.setItem(this.SLOT_MAXLVL, this.getMaxLvl());
		this.menu.setItem(this.SLOT_YSHIFT, this.getYShift());
		this.menu.setItem(this.SLOT_MAXMOBS, this.getMaxMobs());
		this.menu.setItem(this.SLOT_SQUARE, this.getSquare());
		this.menu.setItem(this.SLOT_FADINGLVL, this.getFading());
		this.menu.setItem(this.SLOT_RAGE, this.getRage());
		this.menu.setItem(this.SLOT_TYPE, this.getRType());
		this.menu.setItem(this.SLOT_DELETE, this.getDelete());
		this.menu.setItem(this.SLOT_EMPTY, this.getEmpty());
		return this.getHolder().openInventory(this.menu) != null;
	}

	private ItemStack getCenter() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getRegion().getCenter().toString());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getX() {
		ItemStack item = new ItemStack(Material.ARROW, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("xRange: " + this.getRegion().getXRange());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getY() {
		ItemStack item = new ItemStack(Material.ARROW, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("yRange: " + this.getRegion().getYRange());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getZ() {
		ItemStack item = new ItemStack(Material.ARROW, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("zRange: " + this.getRegion().getZRange());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getRage() {
		ItemStack item = new ItemStack(Material.REDSTONE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("rageProbability: " + this.getRegion().getRageProbability());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getMobs() {
		ItemStack item = new ItemStack(Material.EGG, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Monsters");
		List<String> lore = new ArrayList<String>();
		for(Monster monster : region.getMonsters()){
			lore.add(monster.getName());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getMinLvl() {
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Level Min: " + this.getRegion().getMinLevel());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getMaxLvl() {
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Level Max: " + this.getRegion().getMaxLevel());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getYShift() {
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("yShift: " + this.getRegion().getYShift());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getMaxMobs() {
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("maxMobs: " + this.getRegion().getMaxMonstersAmount());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getRType() {
		Wool wool = new Wool(this.getRegion().getType().getDyeColor());
		ItemStack item = wool.toItemStack(1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Type: " + this.getRegion().getType().toString());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getSquare() {
		ItemStack item = new ItemStack(this.getRegion().isSquare() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Square: " + this.getRegion().isSquare());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getFading() {
		ItemStack item = new ItemStack(this.getRegion().isFadingLevel() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Fading level: " + this.getRegion().isFadingLevel());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getDelete(){
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Delete");
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getEmpty(){
		ItemStack item = new ItemStack(Material.APPLE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Empty region");
		item.setItemMeta(meta);
		return item;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

}
