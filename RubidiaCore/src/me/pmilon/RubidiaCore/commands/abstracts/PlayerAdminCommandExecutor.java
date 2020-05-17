package me.pmilon.RubidiaCore.commands.abstracts;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.entity.Player;

public abstract class PlayerAdminCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(rp.isOp()){
			this.onAdminCommand(player, rp, args);
		}else rp.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être opérateur ?");
	}

	public abstract void onAdminCommand(Player player, RPlayer rp, String[] args);
	
}
