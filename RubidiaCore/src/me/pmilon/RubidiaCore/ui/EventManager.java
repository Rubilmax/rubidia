package me.pmilon.RubidiaCore.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.REvents.Event;
import me.pmilon.RubidiaCore.REvents.Events;
import me.pmilon.RubidiaCore.REvents.Event.EventType;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventManager extends UIHandler {

	private Event event;
	
	private int SLOT_TYPE = 0;
	private int SLOT_SUBTITLE = 1;
	private int SLOT_STARTDATE = 2;
	private int SLOT_DURATION = 3;
	private int SLOT_FACTOR = 4;
	private int SLOT_START = 6;
	private int SLOT_BACK = 7;
	private int SLOT_DELETE = 8;
	
	private int LIST_ID_SUBTITLE = 1, LIST_ID_DURATION = 2, LIST_ID_STARTDATE = 3, LIST_ID_FACTOR = 4;
	public EventManager(Player p, Event event) {
		super(p);
		this.event = event;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, "Event Manager");
	}
	
	@Override
	public String getType() {
		return "EVENT_MANAGER_MENU";
	}
	
	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_SUBTITLE){
					this.getEvent().setSubtitle(this.getMessage());
				}else if(this.getListeningId() == this.LIST_ID_FACTOR){
					if(Utils.isDouble(this.getMessage())){
						this.getEvent().setFactor(Double.valueOf(this.getMessage()));
					}
				}else if(this.getListeningId() == this.LIST_ID_DURATION){
					Date date = null;
					try {
						date = new SimpleDateFormat("HH:mm").parse(this.getMessage());
					} catch (ParseException e) {
						this.getHolder().sendMessage("§cFormat: HH:mm");
					}
					if(date != null){
						this.getEvent().setDuration(date.getTime()+1*60*60*1000L);
					}
				}else if(this.getListeningId() == this.LIST_ID_STARTDATE){
					Date date = null;
					try {
						date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(this.getMessage());
					} catch (ParseException e) {
						this.getHolder().sendMessage("§cFormat: dd/MM/yyyy HH:mm");
					}
					if(date != null){
						this.getEvent().setStartDate(date.getTime());
					}
				}
			}
		}
		
		this.getMenu().setItem(this.SLOT_TYPE, this.getEType());
		this.getMenu().setItem(this.SLOT_SUBTITLE, this.getSubtitle());
		this.getMenu().setItem(this.SLOT_STARTDATE, this.getStartDate());
		this.getMenu().setItem(this.SLOT_DURATION, this.getDuration());
		this.getMenu().setItem(this.SLOT_FACTOR, this.getFactor());
		this.getMenu().setItem(this.SLOT_START, this.getStart());
		this.getMenu().setItem(this.SLOT_BACK, this.getBack());
		this.getMenu().setItem(this.SLOT_DELETE, this.getDelete());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_TYPE){
			if(this.getEvent().getType().equals(EventType.XP))this.getEvent().setType(EventType.RUBIS);
			else if(this.getEvent().getType().equals(EventType.RUBIS))this.getEvent().setType(EventType.XP);
			this.getMenu().setItem(this.SLOT_TYPE, this.getEType());
		}else if(slot == this.SLOT_DELETE){
			if(this.getEvent().isActive())this.getEvent().finish();
			Events.currentEvents.remove(this.getEvent());
			Core.uiManager.requestUI(new EventListUI(this.getHolder()));
		}else if(slot == this.SLOT_START){
			this.getEvent().start();
		}else if(slot == this.SLOT_FACTOR)this.close(true, this.LIST_ID_FACTOR);
		else if(slot == this.SLOT_SUBTITLE)this.close(true, this.LIST_ID_SUBTITLE);
		else if(slot == this.SLOT_STARTDATE){
			this.close(true, this.LIST_ID_STARTDATE);
			this.getHolder().sendMessage("§aFormat: dd/MM/yyyy HH:mm");
		}else if(slot == this.SLOT_DURATION){
			this.close(true, this.LIST_ID_DURATION);
			this.getHolder().sendMessage("§aFormat: HH:mm");
		}else if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new EventListUI(this.getHolder()));
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}
	
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public ItemStack getEType(){
		ItemStack item = new ItemStack(this.getEvent().getType().getMaterial(),1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.getEvent().getType().toString());
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getSubtitle(){
		ItemStack item = new ItemStack(Material.MAP,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§d" + this.getEvent().getSubtitle());
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getStartDate(){
		ItemStack item = new ItemStack(Material.CLOCK,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Start: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(this.getEvent().getStartDate())));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getDuration(){
		ItemStack item = new ItemStack(Material.CLOCK,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Duration: " + new SimpleDateFormat("HH:mm").format(new Date(this.getEvent().getDuration()-1*60*60*1000L)));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getFactor(){
		ItemStack item = new ItemStack(Material.COMPASS,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Factor: " + String.valueOf(this.getEvent().getFactor()));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getDelete(){
		ItemStack ITEM_DELETE = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = ITEM_DELETE.getItemMeta();
		meta.setDisplayName("Delete");
		ITEM_DELETE.setItemMeta(meta);
		return ITEM_DELETE;
	}
	public ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	public ItemStack getStart(){
		ItemStack ITEM_START = new ItemStack(Material.GREEN_WOOL, 1);
		ItemMeta meta = ITEM_START.getItemMeta();
		meta.setDisplayName("Start event");
		ITEM_START.setItemMeta(meta);
		return ITEM_START;
	}

}
