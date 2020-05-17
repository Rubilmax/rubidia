package me.pmilon.RubidiaCore.commands.abstracts;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		if(!rp.isLoading()) {
			this.onCommand(player, rp, args);
		} else rp.sendMessage("§cVous avez un personnage en chargement.");
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		sender.sendMessage("§cYou can only use this command as a player.");
	}
	
	public abstract void onCommand(Player player, RPlayer rp, String[] args);
	
}
