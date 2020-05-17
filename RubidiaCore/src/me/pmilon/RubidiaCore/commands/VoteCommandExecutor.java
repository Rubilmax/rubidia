package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridCommandExecutor;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaCore.utils.VectorUtils;
import me.pmilon.RubidiaCore.votes.Vote;
import me.pmilon.RubidiaCore.votes.Votes;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			if(args.length > 5){
				if(Votes.hasVoted(rp)){
					rp.sendMessage("§cVous avez déjà voté !");
				}else{
					if(args[5].equalsIgnoreCase("true")){
						Votes.vote(rp, true);
						rp.sendMessage("§aVous avez voté §2POUR§a !");
						rp.getChat().forceCloseFixDisplay();
					}else if(args[5].equalsIgnoreCase("false")){
						Votes.vote(rp, false);
						rp.sendMessage("§aVous avez voté §2CONTRE§a !");
						rp.getChat().forceCloseFixDisplay();
					}
				}
			}else{
				if(Votes.currentVote != null)rp.sendMessage("§cUn vote est déjà soumis !");
				else{
					Vote vote = null;
					if(args[0].equalsIgnoreCase("restart")){
						vote = new Vote(10*60*1000L, "Souhaitez-vous redémarrer le serveur ?", "restart"){

							@Override
							public void run() {
								Core.restart();
							}
							
						};
					}else if(args[0].equalsIgnoreCase("weather")){
						vote = new Vote(3*60*1000L, "Souhaitez-vous inverser la météo ?", "weather"){

							@Override
							public void run() {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "toggledownfall");
							}
							
						};
					}else if(args[0].equalsIgnoreCase("time")){
						if(player.getWorld().getTime() > 13000){
							vote = new Vote(3*60*1000L, "Souhaitez-vous mettre le jour ?", "time"){

								@Override
								public void run() {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set day");
								}
								
							};
						}else{
							vote = new Vote(3*60*1000L, "Souhaitez-vous mettre la nuit ?", "time"){

								@Override
								public void run() {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set night");
								}
								
							};
						}
					}else if(args[0].equalsIgnoreCase("slam")){
						if(args.length > 1){
							final Player target = Bukkit.getPlayer(args[1]);
							if(target != null){
								vote = new Vote(1*60*1000L, "Souhaitez-vous donner une claque à " + args[1] + " ?", "slam"){

									@Override
									public void run() {
										if(target.isOnline()){
											if(target.getHealth() > 1)target.damage(1);
											target.setVelocity(VectorUtils.rotateVector(target.getEyeLocation().getDirection().multiply(.75).setY(.03), RandomUtils.random.nextInt(45)*(RandomUtils.random.nextBoolean() ? -1 : 1), 0));
											for(Player po : Bukkit.getOnlinePlayers()){
												po.playSound(po.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
												RPlayer.get(po).sendMessage("§6" + target.getName() + " §ea reçu une claque !");
											}
										}else{
											for(Player po : Bukkit.getOnlinePlayers()){
												RPlayer.get(po).sendMessage("§6" + target.getName() + " §ea échappé à la claque comme un trouillard...");
											}
										}
									}
									
								};
							}else rp.sendMessage("§4" + args[0] + "§ n'est pas en ligne !");
						}else rp.sendMessage("§cUtilisez /vote slam [joueur]");
					}else rp.sendMessage("§cUtilisez /vote [restart/weather/time/slam (joueur)]");
					if(vote != null){
						if(Votes.startVote(vote)){
							RPlayer.broadcastMessage("§2" + rp.getName() + " §aa soumis un nouveau vote !");
							Votes.vote(rp, true);
							rp.getChat().forceCloseFixDisplay();
						}else{
							int min = (int) Math.round(((double)(Votes.lastVotes.get(vote.getType())+vote.getTime()-System.currentTimeMillis()))/(60*1000L));
							rp.sendMessage("§cVous ne pouvez soumettre de vote de ce type pendant §4" + min + " §cminutes.");
						}
					}
				}
			}
		}else rp.sendMessage("§cUtilisez /vote [restart/weather/time/slam (joueur)]");
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		if(args.length > 0){
			if(args.length <= 5) {
				if(Votes.currentVote != null)sender.sendMessage("§cUn vote est déjà soumis !");
				else {
					Vote vote = null;
					if(args[0].equalsIgnoreCase("restart")){
						vote = new Vote(10*60*1000L, "Souhaitez-vous redémarrer le serveur ?", "restart"){

							@Override
							public void run() {
								Core.restart();
							}
							
						};
					} else if(args[0].equalsIgnoreCase("weather")){
						vote = new Vote(3*60*1000L, "Souhaitez-vous ensoleiller la météo ?", "weather"){

							@Override
							public void run() {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
							}
							
						};
					} else if(args[0].equalsIgnoreCase("slam")){
						if(args.length > 1){
							final Player target = Bukkit.getPlayer(args[1]);
							if(target != null){
								vote = new Vote(1*60*1000L, "Souhaitez-vous donner une claque à " + args[1] + " ?", "slam"){

									@Override
									public void run() {
										if(target.isOnline()){
											if(target.getHealth() > 1)target.damage(1);
											target.setVelocity(VectorUtils.rotateVector(target.getEyeLocation().getDirection().multiply(.75).setY(.03), RandomUtils.random.nextInt(45)*(RandomUtils.random.nextBoolean() ? -1 : 1), 0));
											for(Player po : Bukkit.getOnlinePlayers()){
												po.playSound(po.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
												RPlayer.get(po).sendMessage("§6" + target.getName() + " §ea reçu une claque !");
											}
										}else{
											for(Player po : Bukkit.getOnlinePlayers()){
												RPlayer.get(po).sendMessage("§6" + target.getName() + " §ea échappé à la claque comme un trouillard...");
											}
										}
									}
									
								};
							}else sender.sendMessage("§4" + args[0] + "§ n'est pas en ligne !");
						}else sender.sendMessage("§cUtilisez /vote slam [joueur]");
					}else sender.sendMessage("§cUtilisez /vote [restart/weather/time/slam (joueur)]");
					
					if(vote != null){
						if(Votes.startVote(vote)){
							RPlayer.broadcastMessage("§2" + sender.getName() + " §aa soumis un nouveau vote !");
						}else{
							int min = (int) Math.round(((double)(Votes.lastVotes.get(vote.getType())+vote.getTime()-System.currentTimeMillis()))/(60*1000L));
							sender.sendMessage("§cVous ne pouvez soumettre de vote de ce type pendant §4" + min + " §cminutes.");
						}
					} else sender.sendMessage("§cUtilisez /vote [restart/weather/time/slam (joueur)]");
				}
			} else sender.sendMessage("§cUtilisez /vote [restart/weather/time/slam (joueur)]");
		} else sender.sendMessage("§cUtilisez /vote [restart/weather/time/slam (joueur)]");
	}

}
