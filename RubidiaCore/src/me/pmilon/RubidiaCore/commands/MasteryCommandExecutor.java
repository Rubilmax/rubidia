package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

public class MasteryCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length >= 1){
			for (Mastery mastery : Mastery.values()) {
				if (mastery.toString().toLowerCase().contains(args[0].toLowerCase())) {
					if (args.length >= 2) {
						RPlayer targetRp = RPlayer.getFromName(args[1]);
						if (targetRp != null) rp = targetRp;
					}
					
					rp.setMastery(mastery);
					rp.sendMessage("Vous êtes désormais un " + mastery.getName() + " §f!");
					return;
				}
			}
			
			List<String> masteries = Arrays.asList(Mastery.values()).stream().map((Mastery mastery) -> mastery.toString().toLowerCase()).collect(Collectors.toList());
			rp.sendMessage("Les maîtrises disponibles sont : " + String.join(", ", masteries));
		}else rp.sendMessage("§cUtilisez /mastery [mastery] (joueur)");
	}

}