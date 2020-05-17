package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaCore.ui.CoupleMenu;

import org.bukkit.entity.Player;

public class CoupleCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(rp.getCouple() != null){
			Core.uiManager.requestUI(new CoupleMenu(player, rp.getCouple()));
		}else rp.sendMessage("§cVous n'êtes pas marié !");
	}

}
