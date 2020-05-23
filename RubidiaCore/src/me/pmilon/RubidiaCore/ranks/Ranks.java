package me.pmilon.RubidiaCore.ranks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class Ranks {

	public static final long TIMEOUT = 1*7*24*60*60*1000L;
	
	public static void update(){
		Core.console.sendMessage("§eUpdating ranks...");
		List<RPlayer> rps = new ArrayList<RPlayer>();
		for(RPlayer rp : Core.rcoll.data()){
			boolean isOp = Bukkit.getServer().getOperators().contains(Bukkit.getOfflinePlayer(UUID.fromString(rp.getUniqueId())));
			if(!isOp && System.currentTimeMillis() - rp.getLastConnectionDate() <= Ranks.TIMEOUT){
				rps.add(rp);
			}
		}
		Supplier<Stream<RPlayer>> tops = () -> rps.stream().sorted(Comparator.comparing(RPlayer::getMaxLevel).reversed()).limit(5);
		List<String> names = tops.get().map(RPlayer::getName).collect(Collectors.toList());
		List<String> scores = tops.get().map((RPlayer rp) -> "§3N. §9§l" + rp.getMaxLevel()).collect(Collectors.toList());
		List<String> uuids = tops.get().map((RPlayer rp) -> rp.getUniqueId()).collect(Collectors.toList());
		Ranks.updateRanks(names, scores, uuids, "level");
		
		tops = () -> rps.stream().sorted(Comparator.comparing(RPlayer::getMaxRenom).reversed()).limit(5);
		names = tops.get().map(RPlayer::getName).collect(Collectors.toList());
		scores = tops.get().map((RPlayer rp) -> "§8[§7" + rp.getMaxRenom() + "§8]").collect(Collectors.toList());
		uuids = tops.get().map((RPlayer rp) -> rp.getUniqueId()).collect(Collectors.toList());
		Ranks.updateRanks(names, scores, uuids, "renom");
		
		tops = () -> rps.stream().sorted(Comparator.comparing(RPlayer::getTotalMoney).reversed()).limit(5);
		names = tops.get().map(RPlayer::getName).collect(Collectors.toList());
		scores = tops.get().map((RPlayer rp) -> "§2§l" + rp.getTotalMoney() + " §aémd.").collect(Collectors.toList());
		uuids = tops.get().map((RPlayer rp) -> rp.getUniqueId()).collect(Collectors.toList());
		Ranks.updateRanks(names, scores, uuids, "money");
		
		tops = () -> rps.stream().sorted(Comparator.comparing(RPlayer::getTotalKills).reversed()).limit(5);
		names = tops.get().map(RPlayer::getName).collect(Collectors.toList());
		scores = tops.get().map((RPlayer rp) -> "§4§l" + rp.getTotalKills() + " §cmeurtre" + (rp.getKills() > 1 ? "s" : "")).collect(Collectors.toList());
		uuids = tops.get().map((RPlayer rp) -> rp.getUniqueId()).collect(Collectors.toList());
		Ranks.updateRanks(names, scores, uuids, "kills");
		
		tops = () -> rps.stream().sorted(Comparator.comparing(RPlayer::getGamingTime).reversed()).limit(5);
		names = tops.get().map(RPlayer::getName).collect(Collectors.toList());
		scores = tops.get().map((RPlayer rp) -> {
			long hours = TimeUnit.MILLISECONDS.toHours(rp.getGamingTime());
			return "§4§l" + hours + " §cheure" + (hours > 1 ? "s" : "");
		}).collect(Collectors.toList());
		uuids = tops.get().map((RPlayer rp) -> rp.getUniqueId()).collect(Collectors.toList());
		Ranks.updateRanks(names, scores, uuids, "gamingtime");
		
		List<Guild> guilds = new ArrayList<Guild>();
		for(Guild guild : GuildsPlugin.gcoll.data()){
			if(!guild.getName().equalsIgnoreCase("none") && guild.isActive())guilds.add(guild);
		}
		
		Supplier<Stream<Guild>> topGuilds = () -> guilds.stream().sorted(Comparator.comparing((Guild guild) -> guild.getClaims().size()).reversed()).limit(5);
		names = topGuilds.get().map(Guild::getName).collect(Collectors.toList());
		scores = topGuilds.get().map((Guild guild) -> {
			int claims = guild.getClaims().size();
			return "§6§l" + claims + "§e terre" + (claims > 1 ? "s" : "");
		}).collect(Collectors.toList());
		uuids = topGuilds.get().map((Guild guild) -> guild.getLeader().getUniqueId()).collect(Collectors.toList());
		Ranks.updateRanks(names, scores, uuids, "claims");
		
		Core.console.sendMessage("§eRanks updated!");
	}
	
	public static void updateRanks(List<String> names, List<String> scores, List<String> uuids, String rankType) {
		new BukkitTask(Core.instance) {

			@Override
			public void run() {
				for(int i = 0;i < names.size();i++){
					Location location = (Location) Configs.getDatabase().get("rankings." + rankType + "." + i + ".location");
					if(location != null){
						Block block = location.getBlock();
						for(BlockFace fc : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
							if(block.getRelative(fc).getType().equals(Material.WALL_SIGN)){
								Sign sign = (Sign)block.getRelative(fc).getState();
								sign.setLine(0, "§2§m---§2> §8#" + (i + 1) + "§2 <§m---");
								sign.setLine(1, "§7§l" + names.get(i));
								sign.setLine(2, "§3N. §9§l" + scores.get(i));
								sign.setLine(3, "§2§m----------");
								sign.update(true);
								Block blockUp = block.getRelative(BlockFace.UP);
								if(blockUp.getType().equals(Material.PLAYER_HEAD) || blockUp.getType().equals(Material.PLAYER_WALL_HEAD)){
									Skull skull = (Skull)blockUp.getState();
									skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuids.get(i))));
									skull.update(true);
								}
							}
						}
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(0); // to run task synchronously
	}
	
}
