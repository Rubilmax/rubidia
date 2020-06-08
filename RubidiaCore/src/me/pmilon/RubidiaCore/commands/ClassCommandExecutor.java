package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

public class ClassCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length >= 1){
			for (RClass rClass : RClass.values()) {
				if (rClass.toString().toLowerCase().contains(args[0].toLowerCase())) {
					if (args.length >= 2) {
						RPlayer targetRp = RPlayer.getFromName(args[1]);
						if (targetRp != null) rp = targetRp;
					}
					
					rp.setRClass(rClass);
					rp.sendMessage("Vous êtes désormais un " + rClass.getColor() + rClass.getName() + " §f!");
					return;
				}
			}
			
			List<String> rClasses = Arrays.asList(RClass.values()).stream().map((RClass rClass) -> rClass.toString().toLowerCase()).collect(Collectors.toList());
			rp.sendMessage("Les classes disponibles sont : " + String.join(", ", rClasses));
		}else rp.sendMessage("§cUtilisez /class [classe] (joueur)");
	}

}
