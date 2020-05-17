package me.pmilon.RubidiaGuilds.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaGuilds.ui.GuildsUI;

import org.bukkit.entity.Player;

public class GuildsCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer arg1, String[] arg2) {
		Core.uiManager.requestUI(new GuildsUI(player));
	}

}