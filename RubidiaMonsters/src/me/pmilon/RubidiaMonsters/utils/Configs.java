package me.pmilon.RubidiaMonsters.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configs {
	
	public static void reloadMonstersConfig() {
	    if (RubidiaMonstersPlugin.monstersConfigFile == null) {
	    RubidiaMonstersPlugin.monstersConfigFile = new File(RubidiaMonstersPlugin.instance.getDataFolder(), "monsters.yml");
	    }
	    RubidiaMonstersPlugin.monstersConfig = YamlConfiguration.loadConfiguration(RubidiaMonstersPlugin.monstersConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(RubidiaMonstersPlugin.instance.getResource("monsters.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        RubidiaMonstersPlugin.monstersConfig.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getMonstersConfig(){
	    if (RubidiaMonstersPlugin.monstersConfig == null) {
	        reloadMonstersConfig();
	    }
	    return RubidiaMonstersPlugin.monstersConfig;
	}
	
	public static void saveMonstersConfig() {
	    if (RubidiaMonstersPlugin.monstersConfig == null || RubidiaMonstersPlugin.monstersConfigFile == null) {
	        return;
	    }
	    try {
	    	getMonstersConfig().save(RubidiaMonstersPlugin.monstersConfigFile);
	    } catch (IOException ex) {
	    	RubidiaMonstersPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + RubidiaMonstersPlugin.monstersConfigFile, ex);
	    }
	}
	
	public static void saveDefaultMonstersConfig() {
	    if (RubidiaMonstersPlugin.monstersConfigFile == null) {
	        RubidiaMonstersPlugin.monstersConfigFile = new File(RubidiaMonstersPlugin.instance.getDataFolder(), "monsters.yml");
	    }
	    if (!RubidiaMonstersPlugin.monstersConfigFile.exists()) {            
	    	RubidiaMonstersPlugin.instance.saveResource("monsters.yml", false);
	    }
	}

	public static void reloadDungeonsConfig() {
	    if (RubidiaMonstersPlugin.dungeonsConfigFile == null) {
	    RubidiaMonstersPlugin.dungeonsConfigFile = new File(RubidiaMonstersPlugin.instance.getDataFolder(), "dungeons.yml");
	    }
	    RubidiaMonstersPlugin.dungeonsConfig = YamlConfiguration.loadConfiguration(RubidiaMonstersPlugin.dungeonsConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(RubidiaMonstersPlugin.instance.getResource("dungeons.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        RubidiaMonstersPlugin.dungeonsConfig.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getDungeonsConfig(){
	    if (RubidiaMonstersPlugin.dungeonsConfig == null) {
	        reloadDungeonsConfig();
	    }
	    return RubidiaMonstersPlugin.dungeonsConfig;
	}
	
	public static void saveDungeonsConfig() {
	    if (RubidiaMonstersPlugin.dungeonsConfig == null || RubidiaMonstersPlugin.dungeonsConfigFile == null) {
	        return;
	    }
	    try {
	    	getDungeonsConfig().save(RubidiaMonstersPlugin.dungeonsConfigFile);
	    } catch (IOException ex) {
	    	RubidiaMonstersPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + RubidiaMonstersPlugin.dungeonsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultDungeonsConfig() {
	    if (RubidiaMonstersPlugin.dungeonsConfigFile == null) {
	        RubidiaMonstersPlugin.dungeonsConfigFile = new File(RubidiaMonstersPlugin.instance.getDataFolder(), "dungeons.yml");
	    }
	    if (!RubidiaMonstersPlugin.dungeonsConfigFile.exists()) {            
	    	RubidiaMonstersPlugin.instance.saveResource("dungeons.yml", false);
	    }
	}
	

	
	public static void saveConfigs(){
        try {
            if(Core.instance.getConfig().getBoolean("saveconfigs")){
            	RubidiaMonstersPlugin.instance.getConfig().save(new File(Core.getSavesFolder(), "mconfig.yml"));
	        	getMonstersConfig().save(new File(Core.getSavesFolder(), "monsters.yml"));
	        	getDungeonsConfig().save(new File(Core.getSavesFolder(), "dungeons.yml"));
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
