package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.ui.SPlayerSelectionMenu;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.command.CommandSender;

public class VIPCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		if(args.length > 1){
			RPlayer rp = RPlayer.getFromName(args[0]);
			if(Utils.isDouble(args[1])){
				if(rp != null){
					double time = Double.valueOf(args[1]);
					if(time == 0){
						rp.setVip((long) time);
						if(rp.getLastLoadedSPlayerId() == 3){
							rp.sendMessage("§c§cVous devez sélectionner un autre personnage ou devenir VIP de nouveau pour continuer à jouer !");
							Core.uiManager.requestUI(new SPlayerSelectionMenu(rp.getPlayer()));
						}
					}else{
						if(rp.isVip()){
							rp.setVip((long) (rp.getVip() + time*60*60*1000));
						}else rp.setVip((long) (System.currentTimeMillis() + time*60*60*1000));
					}
					rp.sendMessage(rp.isVip() ? "§2Vous §aêtes désormais §8§l[§6§lVIP§8§l]" : "§4Vous §cn'êtes désormais plus §8§l[§6§lVIP§8§l]");
					sender.sendMessage(rp.isVip() ? "§2" + args[0] + " §ais now §8§l[§6§lVIP§8§l]" : "§4" + args[0] + " §cis no longer §8§l[§6§lVIP§8§l]");
				}else sender.sendMessage("§4" + args[0] + " §cdoes not exist...");
			}else sender.sendMessage("§4" + args[1] + " §cis not a duration...");
		}else sender.sendMessage("§cUse /vip [player] [duration]");
	}

}
