package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		if(args.length > 0){
			Player player = Bukkit.getPlayer(args[0]);
			if(player != null){
				RPlayer rp = RPlayer.get(player);
				if(rp.isMuted()){
					rp.unmute(true);
					sender.sendMessage("§ePlayer §6" + args[0] + " §ehas been unmuted.");
				}else{
					long duration = 0;
					if(args.length > 1){
						if(Utils.isInteger(args[1])){
							duration = Integer.valueOf(args[1]);
							sender.sendMessage("§ePlayer §6" + args[0] + " §ehas been muted for §6" + duration + "§e seconds.");
						}else{
							sender.sendMessage("§ePlayer §6" + args[0] + " §ehas been muted.");
						}
					}else{
						sender.sendMessage("§ePlayer §6" + args[0] + " §ehas been muted.");
					}
					rp.mute(duration);
				}
			}else sender.sendMessage("§cPlayer" + args[0] + " could not be found.");
		}else sender.sendMessage("§cPlease use /mute [player]");
	}

}
