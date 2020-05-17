package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.events.RXPSource;
import me.pmilon.RubidiaCore.events.RXPSourceType;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.command.CommandSender;

public class LevelCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		if(args.length >= 2){
			RPlayer rp = RPlayer.getFromName(args[0]);
			if(rp == null){
				sender.sendMessage("§cCouldn't find any RPlayer with name §4" + args[0]);
				return;
			}
			if(Utils.isInteger(args[1])){
				int level = Integer.valueOf(args[1]);
				if(level > 200)level = 200;
				if(rp.isOnline()){
					if(rp.getRLevel() != level){
						rp.setRLevel(level, new RXPSource(RXPSourceType.COMMAND, null, null));
						sender.sendMessage("§6" + rp.getName() + " §eis now level §6" + args[1]);
					}else sender.sendMessage("§6" + rp.getName() + " §eis already level §6" + args[1]);
				}else{
					rp.setRLevel(level, new RXPSource(RXPSourceType.COMMAND, null, null));
					sender.sendMessage("§6" + rp.getName() + " §eis now level §6" + args[1]);
				}
			}else sender.sendMessage("§cPlease use /level " + args[0] + " [level]");
		}else sender.sendMessage("§cPlease use /level [player] [level]");
	}

}
