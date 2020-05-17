package me.pmilon.RubidiaQuests.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;

import org.bukkit.entity.Player;

public class PNJTPCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			String name = args[0];
			PNJHandler handler = PNJManager.getPNJ(name);
			if(handler != null){
				if(handler.isSpawned()){
					player.teleport(handler.getEntity());
				}
			}
		}
	}

}
