package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PastorPNJ;

import org.bukkit.entity.Player;

public class MarryCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 1){
			PNJHandler handler = PNJManager.getPNJByUniqueId(args[0]);
			if(handler != null){
				if(handler instanceof PastorPNJ){
					PastorPNJ pastor = (PastorPNJ)handler;
					rp.getChat().forceCloseFixDisplay();
				   	rp.getChat().clearPNJMessages();
					if(rp.marry()){
						pastor.setTaken(false);
					}
				}
			}
		}
	}

}
