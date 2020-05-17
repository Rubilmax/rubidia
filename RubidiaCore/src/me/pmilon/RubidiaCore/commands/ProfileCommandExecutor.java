package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaCore.ui.ProfileUI;

import org.bukkit.entity.Player;

public class ProfileCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		Core.uiManager.requestUI(new ProfileUI(player));
	}

}
