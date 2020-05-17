package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.command.CommandSender;

public class SKDCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		if(args.length > 1){
			RPlayer rp = RPlayer.getFromName(args[1]);
			if(rp != null){
				if(args.length > 3){
					if(Utils.isInteger(args[3])){
						int amount = Integer.valueOf(args[3]);
						switch (args[2].toLowerCase()){
						case "strength": case "str":
							if(args[0].equals("add")){
								rp.setStrength(rp.getStrength()+amount);
							}else if(args[0].equals("set")){
								rp.setStrength(amount);
							}
							sender.sendMessage("§6" + rp.getName() + " §ehas now §6" + rp.getLoadedSPlayer().getStrength() + " §estrength points.");
							break;
						case "endurance": case "end":
							if(args[0].equals("add")){
								rp.setEndurance(rp.getEndurance()+amount);
							}else if(args[0].equals("set")){
								rp.setEndurance(amount);
							}
							sender.sendMessage("§6" + rp.getName() + " §ehas now §6" + rp.getLoadedSPlayer().getEndurance() + " §eendurance points.");
							break;
						case "agility": case "agi":
							if(args[0].equals("add")){
								rp.setAgility(rp.getAgility()+amount);
							}else if(args[0].equals("set")){
								rp.setAgility(amount);
							}
							sender.sendMessage("§6" + rp.getName() + " §ehas now §6" + rp.getLoadedSPlayer().getAgility() + " §eagility points.");
							break;
						case "intelligence": case "int":
							if(args[0].equals("add")){
								rp.setIntelligence(rp.getIntelligence()+amount);
							}else if(args[0].equals("set")){
								rp.setIntelligence(amount);
							}
							sender.sendMessage("§6" + rp.getName() + " §ehas now §6" + rp.getLoadedSPlayer().getIntelligence() + " §eintelligence points.");
							break;
						case "perception": case "per":
							if(args[0].equals("add")){
								rp.setPerception(rp.getPerception()+amount);
							}else if(args[0].equals("set")){
								rp.setPerception(amount);
							}
							sender.sendMessage("§6" + rp.getName() + " §ehas now §6" + rp.getLoadedSPlayer().getPerception() + " §eperception points.");
							break;
						case "none": case "n":
							if(args[0].equals("add")){
								rp.setSkillDistinctionPoints(rp.getSkillDistinctionPoints()+amount);
							}else if(args[0].equals("set")){
								rp.setSkillDistinctionPoints(amount);
							}
							sender.sendMessage("§6" + rp.getName() + " §ehas §6" + rp.getSkillDistinctionPoints() + " §edistinction points.");
							break;
						default:
							sender.sendMessage("§cPossible distinctions are : strength, endurance, agility, intelligence, perception, none");
							break;
						}
					}else sender.sendMessage("§cUse /skd [add/get/set] " + args[1] + " [distinction] [amount]");
				}else sender.sendMessage("§cUse /skd [add/get/set] " + args[1] + " [distinction] [amount]");
			}else sender.sendMessage("§cCan't find RPlayer with name §4" + args[1] + "§c.");
		}else sender.sendMessage("§cUse /skd [add/get/set] [player] [distinction] [amount]");
	}

}
