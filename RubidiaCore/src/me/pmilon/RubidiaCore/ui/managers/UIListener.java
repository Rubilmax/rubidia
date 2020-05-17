package me.pmilon.RubidiaCore.ui.managers;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerPreChatMessageEvent;
import me.pmilon.RubidiaCore.events.RPlayerRequestDuelEvent;
import me.pmilon.RubidiaCore.events.RPlayerRequestedPlayerTradeEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.PlayerMenu;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.events.GuildInviteGMemberEvent;
import me.pmilon.RubidiaGuilds.events.GuildRelationsChangeEvent;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.ui.GRelationsUI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UIListener implements Listener {
	
	private final UIManager uiManager;
	
	public UIListener(UIManager uiManager) {
		this.uiManager = uiManager;
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent e){
		if(this.uiManager.hasActiveSession(e.getPlayer()))this.uiManager.getSession(e.getPlayer()).getUIHandler().close(false);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		if(this.uiManager.hasActiveSession(e.getPlayer()))this.uiManager.getSession(e.getPlayer()).getUIHandler().close(false);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(this.uiManager.hasActiveSession(e.getEntity()))this.uiManager.getSession(e.getEntity()).getUIHandler().close(false);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			if(e.getClickedInventory() != null){
				Player p = (Player) e.getWhoClicked();
				UISession uiSession = uiManager.getSession(p);
				if(uiSession != null){
					if(uiSession.getUIHandler().getMenu().equals(p.getOpenInventory().getTopInventory())) {
						if(e.getClickedInventory().equals(p.getOpenInventory().getTopInventory())){
							uiSession.getUIHandler().onInventoryClick(e, p);
						} else if(e.getClickedInventory().equals(p.getOpenInventory().getBottomInventory())) {
							uiSession.getUIHandler().onGeneralClick(e, p);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent e){
		if(e.getPlayer() instanceof Player){
			if(e.getInventory() != null){
				Player p = (Player) e.getPlayer();
				UISession session = uiManager.getSession(p);
				if(session != null){
					if(session.getUIHandler().getMenu().equals(e.getInventory())){
						session.getUIHandler().onInventoryClose(e, p);
						this.uiManager.playerSessions.remove(session.getIdentifier());
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerChat(RPlayerPreChatMessageEvent e){
		RPlayer rplayer = e.getRPlayer();
		if(this.uiManager.isInTempSession(rplayer.getPlayer())){
			UIHandler ui = this.uiManager.tempSessions.get(rplayer.getPlayer());
			String message = ChatColor.translateAlternateColorCodes('&', e.getMessage());
			if(message.equalsIgnoreCase("-") && !ui.isKeepingWindowAfterEditMode()){
				this.uiManager.tempSessions.remove(rplayer.getPlayer());
				rplayer.sendMessage("§aVous avez quitté le mode d'édition.");
			}else{
				this.uiManager.registerUI(ui);
				ui.openWindow(message);
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onRequestDuel(RPlayerRequestDuelEvent e){
		if(this.uiManager.hasActiveSession(e.getOpponent().getPlayer()))if(this.uiManager.getSession(e.getOpponent().getPlayer()).getUIHandler().getType().equals("PLAYER_MENU"))((PlayerMenu)this.uiManager.getSession(e.getOpponent().getPlayer()).getUIHandler()).updateDuel();
	}
	@EventHandler
	public void onRequestTrade(RPlayerRequestedPlayerTradeEvent e){
		if(this.uiManager.hasActiveSession(e.getTrading()))if(this.uiManager.getSession(e.getTrading()).getUIHandler().getType().equals("PLAYER_MENU"))((PlayerMenu)this.uiManager.getSession(e.getTrading()).getUIHandler()).updateTrade();
	}
	@EventHandler
	public void onGuildInvite(GuildInviteGMemberEvent e){
		if(this.uiManager.hasActiveSession(e.getGMember().getPlayer()))if(this.uiManager.getSession(e.getGMember().getPlayer()).getUIHandler().getType().equals("PLAYER_MENU"))((PlayerMenu)this.uiManager.getSession(e.getGMember().getPlayer()).getUIHandler()).updateInvite();
	}
	@EventHandler
	public void onGuildRelationsChange(GuildRelationsChangeEvent e){
		for(GMember member : e.getWith().getMembers()){
			RPlayer rp = RPlayer.get(member);
			if(rp.isOnline()){
				if(this.uiManager.hasActiveSession(rp.getPlayer())){
					if(this.uiManager.getSession(rp.getPlayer()).getUIHandler().getType().equals("GUILD_RELATIONS_MENU")){
						final GRelationsUI ui = ((GRelationsUI)this.uiManager.getSession(rp.getPlayer()).getUIHandler());
						new BukkitTask(Core.instance){
							public void run(){
								ui.updateAlly();
								ui.updateEnemy();
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(1);
					}
				}
			}
		}
		for(GMember member : e.getGuild().getMembers()){
			RPlayer rp = RPlayer.get(member);
			if(rp.isOnline()){
				if(this.uiManager.hasActiveSession(rp.getPlayer())){
					if(this.uiManager.getSession(rp.getPlayer()).getUIHandler().getType().equals("GUILD_RELATIONS_MENU")){
						final GRelationsUI ui = ((GRelationsUI)this.uiManager.getSession(rp.getPlayer()).getUIHandler());
						new BukkitTask(Core.instance){
							public void run(){
								ui.updateAlly();
								ui.updateEnemy();
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(1);
					}
				}
			}
		}
	}
}
