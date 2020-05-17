package me.pmilon.RubidiaPets;

import java.io.File;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaPets.commands.PetCommandExecutor;
import me.pmilon.RubidiaPets.commands.PetsCommandExecutor;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaPets.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class PetsPlugin extends JavaPlugin{
	
	public static PetsPlugin instance;
	public static File petsConfigFile;
	public static FileConfiguration petsConfig;
	public static ConsoleCommandSender console;

	public static void onPlayerJoin(RPlayer rp){//started by Core
		for(Pet pet : rp.getPets()){
			if(pet.isActive()){
				pet.spawn(rp.getPlayer());
			}
		}
	}
	
  public void onEnable(){
	instance = this;
	console = Bukkit.getConsoleSender();
	
	Configs.saveDefaultPetsConfig();
	Pets.onEnable(true);
    
    this.getCommand("pet").setExecutor(new PetCommandExecutor());
    this.getCommand("pets").setExecutor(new PetsCommandExecutor());
    
    this.getServer().getPluginManager().registerEvents(new EventsHandler(), this);
  }
  
  public void onEnd(){
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(entity instanceof LivingEntity){
					if(entity.hasMetadata("pet")) {
						entity.remove();
					}
				}
			}
		}
	  
		Pets.save(true);
		
		this.saveConfig();
  }
  
}