package me.pmilon.RubidiaQuests.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler.PNJType;
import me.pmilon.RubidiaQuests.pnjs.PasserPNJ;

public class PassCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
		   	rp.getChat().forceCloseFixDisplay();
		   	rp.getChat().clearPNJMessages();
			DialogManager.setNoDialog(player);
			if(args.length > 1){
				PNJHandler handler = PNJManager.getPNJByUniqueId(args[1]);
				if(handler.getType().equals(PNJType.PASSER)){
					PasserPNJ pnj = (PasserPNJ)handler;
					rp.sendMessage("§9§lEn route §bvers de nouveaux horizons...");
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 255, true, false));
					player.teleport(pnj.getTargetLocation());
				}
			}
		}
	}

}
