package me.pmilon.RubidiaMonsters.commands;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;

import org.bukkit.command.CommandSender;

public class KillallCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		List<Monster> monsters = new ArrayList<Monster>();
		for(Monster monster : Monsters.entities.values()){
			if(monster.getTamer() == null)monsters.add(monster);
		}
		for(Monster monster : monsters){
			monster.kill(true);
		}
		sender.sendMessage("§eAll monsters have been removed!");
	}

}
