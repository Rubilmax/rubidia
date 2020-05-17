package me.pmilon.RubidiaCore.ui.abstracts;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.managers.UIManager;
import me.pmilon.RubidiaGuilds.guilds.GMember;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;


public abstract class UIHandler {

	protected final Player player;
	protected final RPlayer rp;
	protected final GMember gm;
	protected final String id;
	protected boolean keepWindowAfterEditMode = false;
	
	protected String message;
	protected int listeningId = -1;
	protected Inventory menu;
	
	public UIHandler(Player p){
		this.id = p.getName();
		this.player = p;
		this.rp = RPlayer.get(p);
		this.gm = GMember.get(p);
	}
	
	public Player getHolder(){
		return this.player;
	}
	
	public String getUniqueId(){
		return this.id;
	}
	
	public UIManager getUIManager(){
		return Core.uiManager;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public int getListeningId(){
		return this.listeningId;
	}
	
	public boolean isKeepingWindowAfterEditMode(){
		return keepWindowAfterEditMode;
	}
	
	public void setKeepingWindowAfterEditMode(boolean keepWindowAfterEditMode){
		this.keepWindowAfterEditMode = keepWindowAfterEditMode;
	}
	
	public abstract String getType();
	
	public boolean openWindow(String message){
		this.message = message;
		if(this.getUIManager().tempSessions.containsKey(this.getHolder()))this.getUIManager().tempSessions.remove(this.getHolder());
		return this.openWindow();
	}
	
	protected abstract boolean openWindow();
	
	public abstract void onInventoryClick(InventoryClickEvent e, Player p);
	
	public abstract void onGeneralClick(InventoryClickEvent e, Player p);
	
	public abstract void onInventoryClose(InventoryCloseEvent e, Player p);
	
	public void close(boolean temporary, int... listeningId){
		if(temporary){
			this.getUIManager().tempSessions.put(this.getHolder(), this);
			this.listeningId = listeningId[listeningId.length-1];//for multiple message listening
			rp.sendMessage("§aQuittez le mode d'édition en tapant \"-\".");
		}else if(this.getUIManager().playerSessions.containsKey(this.getHolder().getName()))this.getUIManager().playerSessions.remove(this.getHolder().getName());
		this.closeUI();
	}
	
	protected abstract void closeUI();

	public Inventory getMenu() {
		return menu;
	}

	public void setMenu(Inventory menu) {
		this.menu = menu;
	}
}
