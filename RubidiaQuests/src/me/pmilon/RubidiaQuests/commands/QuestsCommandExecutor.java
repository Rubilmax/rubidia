package me.pmilon.RubidiaQuests.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaQuests.ui.PlayerQuestList;

import org.bukkit.entity.Player;

public class QuestsCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		Core.uiManager.requestUI(new PlayerQuestList(player));
	}

}
