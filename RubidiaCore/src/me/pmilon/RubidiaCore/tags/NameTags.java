package me.pmilon.RubidiaCore.tags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Team;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RPlayer;

public class NameTags {

	@SuppressWarnings("deprecation")
	public static void update(){
		for(Player player : Bukkit.getOnlinePlayers()){
			for(Team team : player.getScoreboard().getTeams()){
				team.unregister();
			}
			
			player.setPlayerListName(ChatColor.stripColor(player.getName()));
			
			Objective renom = player.getScoreboard().getObjective("renom");
			if(renom != null)renom.unregister();
			renom = player.getScoreboard().registerNewObjective("renom", "dummy", "", RenderType.INTEGER);
			renom.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			
			for(RPlayer rp : RPlayer.getOnlines()){
				Team team = player.getScoreboard().registerNewTeam(rp.getName());//limited to 16 characters...
				team.addEntry(rp.getName());
		        
				String color = rp.isAtLeast(Mastery.MASTER) ? rp.getRClass().getDarkColor() : rp.getRClass().getColor();
				team.setPrefix("§8[§7" + rp.getRLevel() + "§8] ");
				team.setColor(ChatColor.getByChar((rp.isOp() ? "§4" : color).replace("§", "")));
				team.setSuffix(" " + color + (rp.isAtLeast(Mastery.HERO) ? "§l" : "") + "[" + rp.getRClass().toString().split("")[0] + "]");
				
				renom.getScore(rp.getPlayer()).setScore(rp.getRenom());
			}
		}
	}
	
}
