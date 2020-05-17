package me.pmilon.RubidiaCore.duels;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.duels.RDuel.RDuelOutcome;
import me.pmilon.RubidiaCore.events.RPlayerDeathEvent;
import me.pmilon.RubidiaCore.events.RPlayerMoveEvent;
import me.pmilon.RubidiaGuilds.events.GMemberPvpGMemberCancelledEvent;
import me.pmilon.RubidiaGuilds.guilds.GMember;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class RDuelListener implements Listener {

	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		if(rp.isInDuel()){
			if(rp.equals(rp.getCurrentDuel().getChallenger())){
				rp.getCurrentDuel().reward(RDuelOutcome.CHALLENGER_FORFAIT);
			}else{
				rp.getCurrentDuel().reward(RDuelOutcome.CHALLENGED_FORFAIT);
			}
		}
	}
	
	@EventHandler
	public void onRPlayerDeath(RPlayerDeathEvent e){
		RPlayer rp = e.getRPlayer();
		if(rp.isOnline()){
			if(rp.isInDuel()){
				e.setCancelled(true);
				if(rp.equals(rp.getCurrentDuel().getChallenger())){
					rp.getCurrentDuel().reward(RDuelOutcome.CHALLENGER_LOSER);
				}else{
					rp.getCurrentDuel().reward(RDuelOutcome.CHALLENGER_WINNER);
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getHand() != null){
			if(e.getHand().equals(EquipmentSlot.HAND)){
				Player p = e.getPlayer();
				RPlayer rp = RPlayer.get(p);
				if(rp.isInDuel() && e.getAction().toString().contains("BLOCK")){
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		Entity ec = e.getDamager();
		Entity ed = e.getEntity();

		if(ec instanceof Player){
			Player pc = (Player)ec;
			RPlayer rpc = RPlayer.get(pc);
			if(ed instanceof Player){
				Player pd = (Player)ed;
				RPlayer rpd = RPlayer.get(pd);
				if(rpc.isInDuel()){
					if(!rpc.getDuelOpponent().equals(rpd)){
						e.setCancelled(true);
						rpc.sendActionBar("§4§lHey! §r§cYou are in duel against §4" + rpc.getDuelOpponent().getName() + "§c!", "§4§lHey! §r§cVous êtes en duel contre §4" + rpc.getDuelOpponent().getName() + " §c!");
						return;
					}
				}else{
					if(rpd.isInDuel()){
						e.setCancelled(true);
						rpc.sendActionBar("§4§lHey! §r§4" + ed.getName() + " §cis in duel against §4" + rpd.getDuelOpponent().getName() + "§c!", "§4§lHey! §r§4" + ed.getName() + " §cest en duel contre §4" + rpd.getDuelOpponent().getName() + " §c!");
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onFactionPvp(GMemberPvpGMemberCancelledEvent e){
		GMember member = e.getGMember();
		GMember mtarget = e.getDefender();
		RPlayer rp = RPlayer.get(member);
		RPlayer rpt = RPlayer.get(mtarget);
		if(rp.isInDuel()){
			if(rp.getDuelOpponent().equals(rpt)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onMove(RPlayerMoveEvent e){
		PlayerMoveEvent event = e.getEvent();
		Location to = event.getTo();
		RPlayer rp = e.getRPlayer();
		if(rp.isInDuel()){
			if(rp.getCurrentDuel().isStarted()){
				Vector link = rp.getCurrentDuel().getCenter().toVector().subtract(to.toVector());
				if(Math.pow(link.getX(),2)+Math.pow(link.getZ(),2) > Math.pow(rp.getCurrentDuel().getRadius(),2) || Math.abs(link.getY()) > rp.getCurrentDuel().getHeight()){
					rp.getPlayer().setVelocity(link.setY(.6).normalize().multiply(.5));
					rp.sendActionBar("§4§lHey! §cStay in the duel area, you coward!","§4§lHey ! §cReste dans la zone de duel, lâche !");
				}
			}
		}
	}
}
