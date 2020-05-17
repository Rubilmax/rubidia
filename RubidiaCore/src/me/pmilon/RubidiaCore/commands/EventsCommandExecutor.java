package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaCore.ui.EventListUI;

import org.bukkit.entity.Player;

public class EventsCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		Core.uiManager.requestUI(new EventListUI(rp.getPlayer()));
	}

}
