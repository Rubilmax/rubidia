package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridCommandExecutor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ConsoleCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		player.sendMessage(this.INVALID_SENDER);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		this.onCommand(sender, args);
	}
	
	public abstract void onCommand(CommandSender sender, String[] args);
	
}
