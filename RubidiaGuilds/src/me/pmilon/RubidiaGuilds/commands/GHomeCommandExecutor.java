package me.pmilon.RubidiaGuilds.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.guilds.GHome;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.ui.GHomeListUI;

import org.bukkit.entity.Player;

public class GHomeCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		GMember member = GMember.get(player);
		
		if(member.hasGuild()){
			Guild guild = member.getGuild();
			if(args.length > 0){
				GHome home = null;
				String name = args[0];
				boolean id = Utils.isInteger(name);
				if(id){
					int index = Integer.valueOf(name);
					home = guild.getHomes()[index];
				}else{
					for(GHome h : guild.getHomes()){
						if(h != null){
							if(h.getName().equalsIgnoreCase(name)){
								home = h;
								break;
							}
						}
					}
				}
				if(home != null){
					if(member.canHome(home.getIndex())){
						TeleportHandler.startTeleportation(rp, home.getLocation(), new RTeleportCause(RTeleportType.GUILD_HOME,null,null,null));
					}else rp.sendMessage("§cVous n'avez pas la permission d'utiliser le point de rassemblement de votre guilde !");
				}else{
					if(id)rp.sendMessage("§cImpossible de trouver un PR avec cet index.");
					else rp.sendMessage("§cImpossible de trouver un PR avec ce nom.");
				}
			}else Core.uiManager.requestUI(new GHomeListUI(player, guild));
		}else rp.sendMessage("§cVous n'appartenez à aucune guilde !");
	}

}
