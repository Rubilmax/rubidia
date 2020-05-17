package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;

import org.bukkit.entity.Player;

public class VanishCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		rp.setVanished(!rp.isVanished());
		if(rp.isVanished())rp.sendMessage("§6Vous avez disparu !");
		else rp.sendMessage("§cVous avez réapparu.");
	}

}
