package me.pmilon.RubidiaCore.events;

import java.util.List;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;

public class RPlayerDeathEvent extends RPlayerEvent {

	private RPlayer killer;
	private final PlayerDeathEvent superEvent;
	private ItemStack[] inventoryDrops;//items in will be dropped
	private ItemStack[] armorDrops;    //items in will be dropped
	private List<ItemStack> drops;     //All items here will be dropped (both inventoryDrops & armorDrops are contained)
	private boolean keepInventory = false;
	private boolean cancelled = false;
	public RPlayerDeathEvent(PlayerDeathEvent superEvent, RPlayer rplayer, RPlayer killer, List<ItemStack> drops, ItemStack[] inventoryDrops, ItemStack[] armorDrops) {
		super(rplayer);
		this.killer = killer;
		this.superEvent = superEvent;
		this.inventoryDrops = inventoryDrops;
		this.armorDrops = armorDrops;
		this.drops = drops;
	}
	
	public ItemStack[] getInventoryDrops() {
		return inventoryDrops;
	}
	
	public void setInventoryDrops(ItemStack[] inventoryDrops) {
		this.inventoryDrops = inventoryDrops;
	}
	
	public ItemStack[] getArmorDrops() {
		return armorDrops;
	}
	
	public void setArmorDrops(ItemStack[] armorDrops) {
		this.armorDrops = armorDrops;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public List<ItemStack> getDrops() {
		return drops;
	}
	
	public void setDrops(List<ItemStack> drops) {
		this.drops = drops;
	}
	
	public PlayerDeathEvent getSuperEvent() {
		return superEvent;
	}
	
	public boolean isKeepInventory() {
		return keepInventory;
	}
	
	public void setKeepInventory(boolean keepInventory) {
		this.keepInventory = keepInventory;
	}
	
	public RPlayer getKiller() {
		return killer;
	}
	
	public void setKiller(RPlayer killer) {
		this.killer = killer;
	}

}
