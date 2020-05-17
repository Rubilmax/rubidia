package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.chat.ChatType;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;

import org.bukkit.entity.Player;

public class BienvenueCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(rp.lastWelcome != null){
			rp.prechat("Bienvenue " + rp.lastWelcome.getName() + " !", ChatType.GLOBAL);
			rp.lastWelcome = null;
		}else rp.sendMessage("§cIl n'y a personne à accueillir !");
	}

}
