package me.pmilon.RubidiaCore.commands.abstracts;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class HybridAdminCommandExecutor extends HybridCommandExecutor {

	@Override
	public void onPlayerCommand(Player player, RPlayer rp, String[] args) {
		if(rp.isOp()){
			this.onAdminCommand(player, args);
		}else rp.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être opérateur ?");
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) {
		this.onAdminCommand(sender, args);
	}
	
	public abstract void onAdminCommand(CommandSender sender, String[] args);

}
