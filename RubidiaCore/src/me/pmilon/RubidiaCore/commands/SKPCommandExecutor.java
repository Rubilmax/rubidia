package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.command.CommandSender;

public class SKPCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		if(args.length > 1){
			RPlayer rp = RPlayer.getFromName(args[1]);
			if(rp != null){
				if(args.length > 3){
					if(Utils.isInteger(args[2])){
						int ability = Integer.valueOf(args[2]);
						if(Utils.isInteger(args[3])){
							int amount = Integer.valueOf(args[3]);
							if(ability == 0){
								if(args[0].equals("add")){
									rp.setSkillPoints(rp.getSkillPoints()+amount);
								}else if(args[0].equals("set")){
									rp.setSkillPoints(amount);
								}
								sender.sendMessage("§6" + rp.getName() + " §ehas now §6" + rp.getSkillPoints() + " §eskillpoints.");
							}else{
								if(args[0].equals("add")){
									rp.setAbLevel(ability, rp.getAbLevel(ability)+amount);
								}else if(args[0].equals("set")){
									rp.setAbLevel(ability, amount);
								}
								sender.sendMessage("§6" + rp.getName() + " §ehas now §6" + rp.getAbLevel(ability) + " §eskillpoints in ability §6#" + ability);
							}
						}else sender.sendMessage("§cUse /skp [add/set] " + args[1] + " " + args[2] + " [amount]");
					}else sender.sendMessage("§cUse /skp [add/set] " + args[1] + " [ability] [amount]");
				}else sender.sendMessage("§cUse /skp [add/set] " + args[1] + " [ability] [amount]");
			}else sender.sendMessage("§cCan't find RPlayer with name §4" + args[1] + "§c.");
		}else sender.sendMessage("§cUse /skp [add/set] [player] [ability] [amount]");
	}

}
