package me.pmilon.RubidiaCore.commands;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;

public class SpawnCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("set") && rp.isOp()) {
				Core.setSpawn(player.getLocation());
				player.sendMessage("§aVotre position a été enregistrée comme le nouveau spawn du serveur.");
				return;
			}
		}
		
		TeleportHandler.startTeleportation(rp, Core.getSpawn(), new RTeleportCause(RTeleportType.DELAYED_TELEPORTATION, null, null,null));
	}

}
