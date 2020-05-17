package me.pmilon.RubidiaCore.ui.managers;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class UIManager {

	public final HashMap<String, UISession> playerSessions = new HashMap<String, UISession>();
	public final HashMap<Player, UIHandler> tempSessions = new HashMap<Player, UIHandler>();
	
	public UIManager(Core core){
		Bukkit.getPluginManager().registerEvents(new UIListener(this), core);
	}
	
	public UISession getSession(Player p){
		return this.playerSessions.get(p.getName());
	}
	
	public boolean isInTempSession(Player p){
		return this.tempSessions.containsKey(p);
	}
	
	public boolean requestUI(UIHandler uiHandler){
		if(this.hasActiveSession(uiHandler.getHolder()))this.getSession(uiHandler.getHolder()).getUIHandler().close(false);
		UISession uiSession = new UISession(uiHandler);
		this.playerSessions.put(uiHandler.getUniqueId(), uiSession);
		return uiHandler.openWindow("");
	}
	
	public void registerUI(UIHandler uiHandler){
		if(this.hasActiveSession(uiHandler.getHolder()))this.getSession(uiHandler.getHolder()).getUIHandler().close(false);
		UISession uiSession = new UISession(uiHandler);
		this.playerSessions.put(uiHandler.getUniqueId(), uiSession);
	}
	
	public boolean hasActiveSession(Player p){
		if(playerSessions.containsKey(p.getName()))return true;
		return false;
	}
	
	public boolean isUIHandler(Inventory inv){
		return this.getUIHandler(inv) != null;
	}

	public UIHandler getUIHandler(Inventory inv){
		for(UISession uiSession : this.playerSessions.values()){
			if(uiSession.getUIHandler().getMenu().equals(inv)) {
				return uiSession.getUIHandler();
			}
		}
		return null;
	}
}
