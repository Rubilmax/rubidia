package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.scrolls.ScrollType;
import me.pmilon.RubidiaCore.scrolls.Scrolls;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScrollCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		if(args.length > 2){
			RPlayer rp = RPlayer.getFromName(args[0]);
			if(rp == null){
				sender.sendMessage("§cCouldn't find any RPlayer with name §4" + args[0]);
				return;
			}
			if(rp.isOnline()){
				try{
					Player target = rp.getPlayer();
					Scrolls.newScroll(target, ScrollType.valueOf(args[1]), args[2]);
					rp.sendMessage("§eVous avez reçu un parchemin !");
				}catch(IllegalArgumentException ex){
					sender.sendMessage("§4ERROR: §cPossible scrolltypes are " + Utils.valuesOf(ScrollType.class));
				}
			}else sender.sendMessage("§4" + args[0] + " §cmust be online!");
		}else sender.sendMessage("§cUse /scroll [player] [scrolltype] [args:(§4CITYTP:NameOfCity §c| §4SKPADD:SkpAmount§c)]");
	}

}
