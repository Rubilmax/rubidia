package me.pmilon.RubidiaGuilds.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaGuilds.claims.Claims;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.raids.Raid;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RaidCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		GMember member = GMember.get(player);
		
		if(member.hasGuild()){
			Guild guild = member.getGuild();
			if(guild.isRaiding()){
				Raid raid = guild.getCurrentRaid();
				Vector vector = new Vector(RandomUtils.random.nextDouble(), 0, RandomUtils.random.nextDouble()).normalize().multiply(RandomUtils.random.nextDouble()*5);
				Location center = LocationUtils.getSafeLocation(raid.getCenter().toVector().add(vector).toLocation(raid.getCenter().getWorld()));
				TeleportHandler.startTeleportation(rp, center, new RTeleportCause(RTeleportType.RAID_CENTER,null,null,null));
			}else{
				if(member.getPermission(Permission.CLAIM)){
					if(args.length > 0){
						String name = null;
						if(args.length > 0)name = ChatColor.translateAlternateColorCodes('&', args[0]);
						Claims.manageClaim(name, guild, player.getLocation(), member);
					}else rp.sendMessage("§cUtilisez /raid [NomDuTerritoire]");
				}else rp.sendMessage("§cVous n'avez pas la permission de gérer le territoire de votre guilde !");
			}
		}else rp.sendMessage("§cVous n'appartenez à aucune guilde !");
	}

}
