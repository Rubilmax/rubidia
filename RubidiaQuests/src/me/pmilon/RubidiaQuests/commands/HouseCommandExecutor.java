package me.pmilon.RubidiaQuests.commands;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.houses.House;
import me.pmilon.RubidiaQuests.pnjs.HostPNJ;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;

public class HouseCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 1) {
			if(DialogManager.isInDialog(player)){
			   	rp.getChat().forceCloseFixDisplay();
			   	rp.getChat().clearPNJMessages();
				DialogManager.setNoDialog(player);
			}
			
			if(args[0].equals("buy")){
				PNJHandler handler = PNJManager.getPNJByUniqueId(args[1]);
				if(handler != null) {
					if(handler instanceof HostPNJ) {
						HostPNJ host = (HostPNJ) handler;
						House house = host.getHouse();
						if(house != null) {
							if(EconomyHandler.hasEnough(rp, house.getPrice())) {
								EconomyHandler.withdraw(rp, house.getPrice());
								house.setOwnerUniqueId(rp.getUniqueId());
								rp.sendMessage("§aVous avez acheté la maison !");
							}
						}
					}
				}
			}else if(args[0].equals("no")){
				rp.sendMessage("§cVous avez refusé la proposition.");
			}
		} else rp.sendMessage("§cUtilisez /house [buy/no] [uuid]");
	}

}
