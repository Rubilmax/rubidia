package me.pmilon.RubidiaCore.commands.abstracts;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class HybridCommandExecutor implements CommandExecutor {

	protected String INVALID_SENDER = "§fUnknown command. Type \"/help\" for help.";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			RPlayer rp = RPlayer.get(player);
			this.onPlayerCommand(player, rp, args);
		}else if(sender.isOp()){
			this.onConsoleCommand(sender, args);
		}else sender.sendMessage(this.INVALID_SENDER);
		return true;
	}
	
	public abstract void onPlayerCommand(Player player, RPlayer rp, String[] args);
	
	public abstract void onConsoleCommand(CommandSender sender, String[] args);

}
