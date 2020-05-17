package me.pmilon.RubidiaCore.handlers;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerRequestedPlayerTradeEvent;
import me.pmilon.RubidiaCore.ui.TradingUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class TradingHandler implements Listener {

	public static HashMap<Player, Player> traderequest = new HashMap<Player, Player>();
	public static HashMap<Player, Integer> canceltraderequest = new HashMap<Player, Integer>();
	
	public static int REQUEST_TIME = 30;
	
	public static void requestTrade(final Player p1, final Player p2){
		final RPlayer rp1 = RPlayer.get(p1);
		if(!traderequest.containsKey(p1))handleTradeRequest(p1, p2);
		else{
			if(traderequest.get(p1).equals(p2))rp1.sendMessage("§cVous avez déjà demandé un échange avec §4" + p2.getName() + " §c!");
			else handleTradeRequest(p1, p2);
		}
	}
	
	private static void handleTradeRequest(final Player p1, final Player p2){
		final RPlayer rp1 = RPlayer.get(p1);
		final RPlayer rp2 = RPlayer.get(p2);
		traderequest.put(p1, p2);
		if(canceltraderequest.containsKey(p1)){
			Bukkit.getScheduler().cancelTask(canceltraderequest.get(p1));
			canceltraderequest.remove(p1);
		}
		canceltraderequest.put(p1, Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
			public void run(){
				traderequest.remove(p1);
				rp1.sendMessage("§4" + p2.getName() + "§c n'a pas répondu à votre demande d'échange à temps.");
				rp2.sendMessage("§cVous n'avez pas répondu à la demande d'échange à temps.");
			}
		}, REQUEST_TIME*20).getTaskId());
		rp1.sendMessage("§eVous avez demandé un échange à §6" + rp2.getName() + "§e.");
		rp2.sendMessage(("§6" + rp1.getName() + " §evous a demandé un échange !"));
		if(traderequest.containsKey(p2)){
			if(traderequest.get(p2).equals(p1)){
				rp1.sendMessage("§eL'échange avec §6" + rp2.getName() + " §eva débuter !");
				rp2.sendMessage(("§eL'échange avec §6" + rp1.getName() + " §eva débuter !"));
				Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
					public void run(){
						Bukkit.getScheduler().cancelTask(canceltraderequest.get(p1));
						Bukkit.getScheduler().cancelTask(canceltraderequest.get(p2));
						canceltraderequest.remove(p1);
						canceltraderequest.remove(p2);
						traderequest.remove(p1);
						traderequest.remove(p2);
						Core.uiManager.requestUI(new TradingUI(p1, p2, true));
					}
				}, 20);
			}
		}
		RPlayerRequestedPlayerTradeEvent event = new RPlayerRequestedPlayerTradeEvent(rp1, p2);
		Bukkit.getPluginManager().callEvent(event);
	}
	
}
