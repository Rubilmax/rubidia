package me.pmilon.RubidiaCore.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Gender;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.chat.RChatUtils;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaCore.couples.Couple;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaMonsters.utils.Configs;

public class ResetCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0) {
			if(args[0].equals("RubidiaPartPourUnNouveauDepart!") && player.getName().equals("Rubilmax")) {
				if(rp.getName().equals("Rubilmax")) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.kickPlayer("§4RUBIDIA SE REINITIALISE");
					}
					for(RPlayer rpp : Core.rcoll.data()) {
						SPlayer[] defaultSaves = new SPlayer[4];
						defaultSaves[0] = Core.rcoll.newDefaultSPlayer(0);
						defaultSaves[0].setModified(true);
						rpp.setSaves(defaultSaves);
						rpp.setLastLoadedSPlayerId(0);
						rpp.setLoadedSPlayer(defaultSaves[0]);
						rpp.setChatboxHeight(RChatUtils.MAX_CHAT_HEIGHT);
						rpp.setChatboxWidth(RChatUtils.MAX_CHAT_WIDTH);
						rpp.setChatHeight(11);
						rpp.setSex(Gender.UNKNOWN);
						rpp.setBirthDate(0L);
						rpp.setProfileUpdated(false);
						rpp.setNotifOnFriendJoin(true);
						rpp.setNotifOnChatRequest(true);
						rpp.setWouldLikeInvocation(true);
						rpp.setWouldLikeTeleportation(true);
						rpp.setCombatLevel(3);
						rpp.setClickSound(true);
						rpp.setMusic(true);
						rpp.setUsingCycle(true);
						rpp.setLastConnectionDate(0);
						rpp.setGamingTime(0L);
						rpp.setPendingRubis(0);
						rpp.setCoupleUUID(null);
						rpp.setLastDivorce(0L);
					}
					for(Couple couple : new ArrayList<Couple>(Core.coupleColl.data())) {
						Core.coupleColl.remove(couple.getUUID());
						me.pmilon.RubidiaCore.utils.Configs.getCouplesConfig().set("couples." + couple.getUUID(), null);
					}
					for(Guild guild : new ArrayList<Guild>(GuildsPlugin.gcoll.data())) {
						if(!guild.getUUID().equals(Guild.getNone().getUUID())) {
							guild.disband();
						}
					}
					for(Region region : Regions.regions) {
						region.clear();
					}
					Monsters.entities.clear();
					Monsters.monsters.clear();
					Regions.regions.clear();
					Configs.getMonstersConfig().set("monsters", null);
					RubidiaMonstersPlugin.getInstance().getConfig().set("regions", null);
					Core.database.set("lastReset", System.currentTimeMillis());
				}
			}
		}
	}

}
