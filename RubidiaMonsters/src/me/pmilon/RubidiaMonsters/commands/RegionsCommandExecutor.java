package me.pmilon.RubidiaMonsters.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaMonsters.ui.regions.RegionsUI;

import org.bukkit.entity.Player;

public class RegionsCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer arg1, String[] arg2) {
		Core.uiManager.requestUI(new RegionsUI(player));
	}

}
