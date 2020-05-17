package me.pmilon.RubidiaCore.chat;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerDeathEvent;
import me.pmilon.RubidiaCore.events.RPlayerPreChatMessageEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Settings;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// Spigot Chat is made to be ran asynchronously but we need to access the Spigot API, which is mainly synchronous
// So we instead listen to the synchronous event PlayerChatEvent, which delays chat messages but we don't care!
@SuppressWarnings("deprecation")
public class RChatListener implements Listener {
	
	public static void onPlayerJoin(PlayerJoinEvent event, RPlayer rp){
		if(!event.getJoinMessage().equals("") && rp != null){
			event.setJoinMessage("");
			rp.getChat().addInfo("§a§lBienvenue sur §2§lRubidia §a§l!");
			if(!rp.isVanished()){
				for(RPlayer rpp : RPlayer.getOnlines()){
					if(!rpp.equals(rp)){
						rpp.getChat().addInfo("§6[+] §e" + rp.getName() + (" vient de se connecter"));
						rpp.getChat().update();
					}
				}
			}
		}
	}
	
	public static void onPlayerQuit(PlayerQuitEvent event, RPlayer rp){
		if(!event.getQuitMessage().equals("") && rp != null){
			event.setQuitMessage("");
			if(!rp.isVanished()){
				for(RPlayer rpp : RPlayer.getOnlines()){
					if(!rpp.equals(rp)){
						rpp.getChat().addInfo("§6[-] §e" + rp.getName() + (" vient de se déconnecter"));
						rpp.getChat().update();
					}
				}
			}
			if(rp.getChat() != null){
				rp.getChat().kill();
				rp.setChat(null);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(PlayerChatEvent e){
		RPlayer rp = RPlayer.get(e.getPlayer());
		String message = e.getMessage();
		e.setCancelled(true);
		
		rp.prechat(message, rp.getChat().getDefaultType());
	}

	@EventHandler
	public void onRPlayerChat(RPlayerPreChatMessageEvent event){
		final RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			if(rp.isMuted()){
				rp.sendMessage("§cVotre langue a été coupée !");
				event.setCancelled(true);
			}
			
			if(event.getMessageType().equals(ChatType.SHOUT)){
				if(rp.shoutIndex >= Settings.SHOUT_LIMIT){
					event.setCancelled(true);
					rp.sendMessage("§cVous ne pouvez crier que 5 fois par minute.");
				}else{
					rp.shoutIndex++;
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							rp.shoutIndex--;
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(60*20);
				}
			}
			
			if(!event.isCancelled()){
				String message = event.getMessage();
				boolean badword = false;
				for(String string : Core.instance.getConfig().getStringList("badwords")){
					if(message.toLowerCase().contains(string)){
						badword = true;
						break;
					}
				}
				if(badword){
					rp.badword++;
					if(rp.badword > 3){
						rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 1, .1F);
						rp.sendMessage("§6§lSTOP ! §eCette fois, c'est terminé. Calmez-vous et respectez les autres joueurs.");
						rp.mute(240);
						rp.badword = 0;
						event.setCancelled(true);
						return;
					}
				}
				
				boolean help = false;
				for(String string : new String[]{"bug","glitch","coinc","help","aide"}){
					if(message.toLowerCase().contains(string)){
						help = true;
						break;
					}
				}
				if(help){
					rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, .5F);
					rp.sendMessage("§6§lCoincé ? §eTapez /glitch pour vous sortir de là !");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRPlayerDeath(RPlayerDeathEvent event){
		final RPlayer rp = event.getRPlayer();
		if(!event.isCancelled()){
			if(rp.isOnline()){
				for(RPlayer rpp : RPlayer.getOnlines()){
					if(rpp.knows(rp)){
						rpp.getChat().addInfo(("§eVotre équipier §6" + rp.getName() + "§e vient de mourir !"));
						rpp.getChat().update();
					}
				}
			}
		}
	}
}
