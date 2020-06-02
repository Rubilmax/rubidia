package me.pmilon.RubidiaMonsters;

import java.io.File;
import java.util.Random;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.commands.KillallCommandExecutor;
import me.pmilon.RubidiaMonsters.commands.RegionsCommandExecutor;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaMonsters.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RubidiaMonstersPlugin extends JavaPlugin {

	public static Random random = new Random();

	public static RubidiaMonstersPlugin instance;
	public static ConsoleCommandSender console;
	
	public static FileConfiguration monstersConfig = null;
	public static File monstersConfigFile = null;
	public static FileConfiguration dungeonsConfig = null;
	public static File dungeonsConfigFile = null;
	
	public void onEnable(){
		instance = this;
		console = Bukkit.getConsoleSender();
		this.reloadConfig();
		Configs.reloadMonstersConfig();
		Bukkit.getPluginManager().registerEvents(new MonsterListener(this), this);
		this.getCommand("killall").setExecutor(new KillallCommandExecutor());
		this.getCommand("regions").setExecutor(new RegionsCommandExecutor());
		
		new BukkitTask(RubidiaMonstersPlugin.instance) {
			
			@Override
			public void run(){
				if(Regions.regions.size() > 0){
					Region region = Regions.regions.get(RubidiaMonstersPlugin.random.nextInt(Regions.regions.size()));
					if(region.getMonsters().size() > 0){
						if(region.entities.size() < region.getMaxMonstersAmount()){
							Monster monster = region.getMonsters().get(RubidiaMonstersPlugin.random.nextInt(region.getMonsters().size()));
							Location location = region.getRandomSpawnLocation(monster);
							
							if(location != null) {
								new BukkitTask(RubidiaMonstersPlugin.instance) {

									@Override
									public void run() {
										monster.spawnInRegion(location);
									}

									@Override
									public void onCancel() {
									}
									
								}.runTaskLater(0);
							}
						}
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimerAsynchronously(0, 1);
	}
	
	public static void onStart(){
		Monsters.onEnable(true);
		Regions.onEnable(true);
	}
	
	public static void onEnd(){
		Monsters.save(true);
		Regions.save(true);
	}
	
	public static RubidiaMonstersPlugin getInstance(){
		return instance;
	}
	
}
