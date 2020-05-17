package me.pmilon.RubidiaCore.commands;

import java.util.Arrays;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.chat.RChatFixDisplay;
import me.pmilon.RubidiaCore.commands.abstracts.HybridCommandExecutor;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		if(args.length == 0){
			rp.getChat().addFixDisplay(new RChatFixDisplay(rp,80,null).addLines(Arrays.asList("     §6" + ("Solde bancaire : §e") + rp.getBank() + (" §aémeraudes"))));
		}else if(args.length == 1){
			RPlayer rpo = RPlayer.getFromName(args[0]);
			if(rpo != null){
				rp.getChat().addFixDisplay(new RChatFixDisplay(rp, 80, null).addLines(Arrays.asList("     §6" + args[0].toUpperCase() + " : §e" + rpo.getBank() + (" §aémeraudes"))));
			}else rp.sendMessage("§4" + args[0] + " §cn'est jamais venu sur ce serveur ! Utilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("pay")){
				if(Bukkit.getPlayer(args[1]) != null){
					Player po = Bukkit.getPlayer(args[1]);
					if(Utils.isInteger(args[2])){
						int amount = Integer.valueOf(args[2]);
						if(amount > 0){
							RPlayer rpo = RPlayer.get(po);
							if(rp.getBank() >= amount){
								rp.sendMessage("§aVous avez payé §2" + args[1] + " §ade §e" + amount + " §aémeraudes !");
								rpo.sendMessage("§2" + rp.getName() + " §avous a payé de §e" + amount + " §aémeraudes !");
								EconomyHandler.withdraw(player, amount);
								EconomyHandler.deposit(po, amount);
							}
						}
					}
				}else rp.sendMessage("§4" + args[1] + " §cn'est jamais venu sur ce serveur ! Utilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
			}else if(args[0].equalsIgnoreCase("take")){
				if(rp.isOp()){
					if(Bukkit.getPlayer(args[1]) != null){
						Player po = Bukkit.getPlayer(args[1]);
						if(Utils.isInteger(args[2])){
							RPlayer rpo = RPlayer.get(po);
							int amount = Integer.valueOf(args[2]);
							if(rpo.getBank() < amount)rp.sendMessage("§4" + args[1] + " §cdid not have that much money! All of his emeralds have been taken.");
							rp.sendMessage("§aVous avez retiré §e" + amount + " §aémeraudes du compte de §2" + args[1] + "§a.");
							rpo.sendMessage("§4" + rp.getName() + " §cvous a pris §e" + amount + " §cémeraudes !");
							EconomyHandler.withdraw(po, Math.min(amount, rpo.getBank()));
						}
					}else rp.sendMessage("§4" + args[1] + " §cn'est jamais venu sur ce serveur ! Utilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
				}else rp.sendMessage("§cVous avez vraiment cru pouvoir faire ça sans être opérateur ?");
			}else rp.sendMessage("§cUtilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
		}else rp.sendMessage("§cUtilisez : " + (rp.isOp() ? "/money ([joueur]/pay/take [joueur] [montant])" : "/money ([joueur])"));
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage("§cOnly players can check their money.");
		}else if(args.length == 1){
			RPlayer rpo = RPlayer.getFromName(args[0]);
			if(rpo != null){
				sender.sendMessage("§e§m---------------------------------------------------");
				sender.sendMessage("§e|     §6" + args[0].toUpperCase() + " : §e" + rpo.getBank() + " §aemeralds");
				sender.sendMessage("§e§m---------------------------------------------------");
			}else sender.sendMessage("§4" + args[0] + " §chas never been on this server! Use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("pay")){
				if(Bukkit.getPlayer(args[1]) != null){
					Player po = Bukkit.getPlayer(args[1]);
					if(Utils.isInteger(args[2])){
						int amount = Integer.valueOf(args[2]);
						if(amount > 0){
							RPlayer rpo = RPlayer.get(po);
							sender.sendMessage("§aYou paid §2" + args[1] + " §aof §e" + amount + " §aemeralds.");
							rpo.sendMessage("§2§lRubidia§a vous a payé de §e" + amount + " §aémeraudes !");
							EconomyHandler.deposit(po, amount);
						}
					}
				}else sender.sendMessage("§cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
			}else if(args[0].equalsIgnoreCase("take")){
				if(Bukkit.getPlayer(args[1]) != null){
					Player po = Bukkit.getPlayer(args[1]);
					if(Utils.isInteger(args[2])){
						RPlayer rpo = RPlayer.get(po);
						int amount = Integer.valueOf(args[2]);
						if(rpo.getBank() < amount)sender.sendMessage("§4" + args[1] + " §cdoes not have that much money! All of his emeralds have been taken.");
						sender.sendMessage("§aYou withdrawed §e" + amount + " §aemeralds from §2" + args[2] + "§a's account.");
						rpo.sendMessage("§4§lRubidia§c vous a pris §e" + amount + " §cémeraudes !");
						EconomyHandler.withdraw(po, Math.min(amount, rpo.getBank()));
					}
				}else sender.sendMessage("§cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
			}else sender.sendMessage("§cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
		}else sender.sendMessage("§cPlease use: " + (sender.isOp() ? "/money ([player]/pay/take [player] [amount])" : "/money ([player])"));
	}
	
}
