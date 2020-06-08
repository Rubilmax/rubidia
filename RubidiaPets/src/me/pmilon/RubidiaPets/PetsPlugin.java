package me.pmilon.RubidiaPets;

import java.io.File;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaPets.commands.PetCommandExecutor;
import me.pmilon.RubidiaPets.commands.PetsCommandExecutor;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaPets.utils.Configs;
import me.pmilon.RubidiaPets.utils.Settings;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class PetsPlugin extends JavaPlugin{
	
	public static PetsPlugin instance;
	public static File petsConfigFile;
	public static FileConfiguration petsConfig;
	public static ConsoleCommandSender console;
	
	public void onEnable(){
		instance = this;
		console = Bukkit.getConsoleSender();
		
		new BukkitTask(this) {

			@Override
			public void run() {
				for (Pet pet : Pets.pets) {
					if (pet.isActive()) {
						pet.heal(1);
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimerAsynchronously(0, 40);
		
		Configs.saveDefaultPetsConfig();
		Pets.onEnable(true);
	    
	    this.getCommand("pet").setExecutor(new PetCommandExecutor());
	    this.getCommand("pets").setExecutor(new PetsCommandExecutor());
	    
	    this.getServer().getPluginManager().registerEvents(new EventsHandler(), this);
	    
	    for(EntityType type : EntityType.values()){
			if (type.getEntityClass() != null && Creature.class.isAssignableFrom(type.getEntityClass())) {
				Settings.ENTITY_TYPES.add(type);
			}
		}
	}
  
	public void onEnd(){
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(entity.hasMetadata("pet")) {
					entity.remove();
				}
			}
		}
	  
		Pets.save(true);
		
		this.saveConfig();
	}
  
}