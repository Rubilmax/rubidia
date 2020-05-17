package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridCommandExecutor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HMPCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		rp.sendMessage("§6§l" + Core.rcoll.size() + " §ejoueurs ont rejoint Rubidia depuis son début (9 février 2015)");
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		sender.sendMessage("§6COUNT: §e" + Core.rcoll.size());
	}

}
