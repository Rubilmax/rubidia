package me.pmilon.RubidiaManager.commands;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaManager.ui.ChunksUI;

public class ChunksCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		Core.uiManager.requestUI(new ChunksUI(player));
	}

}
