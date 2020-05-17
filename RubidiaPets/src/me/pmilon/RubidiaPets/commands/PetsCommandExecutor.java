package me.pmilon.RubidiaPets.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaPets.ui.PetsUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetsCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender instanceof Player){
			Core.uiManager.requestUI(new PetsUI((Player)sender));
		}
		return false;
	}

}
