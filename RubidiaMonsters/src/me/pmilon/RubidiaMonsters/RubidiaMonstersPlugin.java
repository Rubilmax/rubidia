package me.pmilon.RubidiaMonsters;

import java.io.File;
import java.util.Random;

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
import org.bukkit.entity.LivingEntity;
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
		
		/*Bukkit.getScheduler().runTaskTimer(this, new Runnable(){
			public void run(){
				for(World world : Bukkit.getWorlds()){
					for(LivingEntity entity : Core.toLivingEntityList(world.getEntities())){
						if(!(entity instanceof Player) && !(entity instanceof ArmorStand)){
							boolean ok = entity instanceof Villager ? !QuestsPlugin.pnjManager.isPNJ((Villager) entity) : true;
							if(!Pet.isPet(entity) && ok && !CustomEntities.isCustom(entity)){
								if(!Monsters.entities.containsKey(entity))entity.remove();
								else if(Monsters.get((LivingEntity)entity).getRegisteredRegion() == null)entity.remove();
							}
						}
					}
				}
			}
		}, 100, 600);*/
		Bukkit.getScheduler().runTaskTimer(this, new Runnable(){
			public void run(){
				if(Regions.regions.size() > 0){
					Region region = Regions.regions.get(RubidiaMonstersPlugin.random.nextInt(Regions.regions.size()));
					if(region.getMonsters().size() > 0){
						if(region.entities.size() < region.getMaxMonstersAmount()){
							if(region.hasSpawnLocation()){
								Monster monster = region.getMonsters().get(RubidiaMonstersPlugin.random.nextInt(region.getMonsters().size()));
								int i = 0;
								Location location = null;
								while(i < 100 && location == null) {
									location = region.getRandomSpawnLocation(monster);
									i++;
								}
								if(location != null) {
									monster.spawnInRegion(location);
								}
							}
						}
					}
				}
			}
		}, 0, 1);
	}
	
	public static void onStart(){
		Monsters.onEnable(true);
		Regions.onEnable(true);
	}
	
	public static void onEnd(){
		Monsters.save(true);
		Regions.save(true);
	}
	
	public void onDisable(){
		for(LivingEntity entity : Monsters.entities.keySet()){
			entity.remove();
		}
	}
	
	public static RubidiaMonstersPlugin getInstance(){
		return instance;
	}
	
}
