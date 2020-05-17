package me.pmilon.RubidiaCore.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import me.pmilon.RubidiaCore.Core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configs {
	
	public static void reloadPlayerConfig() {
	    if (Core.playerConfigFile == null) {
	    Core.playerConfigFile = new File(Core.instance.getDataFolder(), "players.yml");
	    }
	    Core.playerConfig = YamlConfiguration.loadConfiguration(Core.playerConfigFile);
	}
	
	public static FileConfiguration getPlayerConfig(){
	    if (Core.playerConfig == null) {
	        reloadPlayerConfig();
	    }
	    return Core.playerConfig;
	}
	
	public static void savePlayerConfig() {
	    if (Core.playerConfig == null || Core.playerConfigFile == null) {
	        return;
	    }
	    try {
	        getPlayerConfig().save(Core.playerConfigFile);
	    } catch (IOException ex) {
	    	Core.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Core.playerConfigFile, ex);
	    }
	}
	
	public static void saveDefaultPlayerConfig() {
	    if (Core.playerConfigFile == null) {
	        Core.playerConfigFile = new File(Core.instance.getDataFolder(), "players.yml");
	    }
	    if (!Core.playerConfigFile.exists()) {            
	    	Core.instance.saveResource("players.yml", false);
	    }
	}

	
	public static void reloadCitiesConfig() {
	    if (Core.cityConfigFile == null) {
	    	Core.cityConfigFile = new File(Core.instance.getDataFolder(), "cities.yml");
	    }
	    Core.cityConfig = YamlConfiguration.loadConfiguration(Core.cityConfigFile);
	}
	
	public static FileConfiguration getCitiesConfig(){
	    if (Core.cityConfig == null) {
	        reloadCitiesConfig();
	    }
	    return Core.cityConfig;
	}
	
	public static void saveCitiesConfig() {
	    if (Core.cityConfig == null || Core.cityConfigFile == null) {
	        return;
	    }
	    try {
	        getCitiesConfig().save(Core.cityConfigFile);
	    } catch (IOException ex) {
	    	Core.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Core.cityConfigFile, ex);
	    }
	}
	
	public static void saveDefaultCitiesConfig() {
	    if (Core.cityConfigFile == null) {
	    	Core.cityConfigFile = new File(Core.instance.getDataFolder(), "cities.yml");
	    }
	    if (!Core.cityConfigFile.exists()) {            
	    	Core.instance.saveResource("cities.yml", false);
	    }
	}
	

	public static void reloadDatabase() {
	    if (Core.databaseFile == null) {
	    	Core.databaseFile = new File(Core.instance.getDataFolder(), "database.yml");
	    }
	    Core.database = YamlConfiguration.loadConfiguration(Core.databaseFile);
	}
	
	public static FileConfiguration getDatabase(){
	    if (Core.database == null) {
	        reloadDatabase();
	    }
	    return Core.database;
	}
	
	public static void saveDatabase() {
	    if (Core.database == null || Core.databaseFile == null) {
	        return;
	    }
	    try {
	    	getDatabase().save(Core.databaseFile);
	    } catch (IOException ex) {
	    	Core.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Core.databaseFile, ex);
	    }
	}
	
	public static void saveDefaultDatabase() {
	    if (Core.databaseFile == null) {
	    	Core.databaseFile = new File(Core.instance.getDataFolder(), "database.yml");
	    }
	    if (!Core.databaseFile.exists()) {            
	    	Core.instance.saveResource("database.yml", false);
	    }
	}
	

	public static void reloadPathConfig() {
	    if (Core.pathConfigFile == null) {
	    	Core.pathConfigFile = new File(Core.instance.getDataFolder(), "paths.yml");
	    }
	    Core.pathConfig = YamlConfiguration.loadConfiguration(Core.pathConfigFile);
	}
	
	public static FileConfiguration getPathConfig(){
	    if (Core.pathConfig == null) {
	        reloadPathConfig();
	    }
	    return Core.pathConfig;
	}
	
	public static void savePathConfig() {
	    if (Core.pathConfig == null || Core.pathConfigFile == null) {
	        return;
	    }
	    try {
	        getPathConfig().save(Core.pathConfigFile);
	    } catch (IOException ex) {
	    	Core.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Core.pathConfigFile, ex);
	    }
	}
	
	public static void saveDefaultPathConfig() {
	    if (Core.pathConfigFile == null) {
	    	Core.pathConfigFile = new File(Core.instance.getDataFolder(), "paths.yml");
	    }
	    if (!Core.pathConfigFile.exists()) {            
	    	Core.instance.saveResource("paths.yml", false);
	    }
	}


	public static void reloadWeaponsConfig() {
	    if (Core.weaponConfigFile == null) {
	    	Core.weaponConfigFile = new File(Core.instance.getDataFolder(), "weapons.yml");
	    }
	    Core.weaponConfig = YamlConfiguration.loadConfiguration(Core.weaponConfigFile);
	}
	
	public static FileConfiguration getWeaponsConfig(){
	    if (Core.weaponConfig == null) {
	        reloadWeaponsConfig();
	    }
	    return Core.weaponConfig;
	}
	
	public static void saveWeaponsConfig() {
	    if (Core.weaponConfig == null || Core.weaponConfigFile == null) {
	        return;
	    }
	    try {
	        getWeaponsConfig().save(Core.weaponConfigFile);
	    } catch (IOException ex) {
	    	Core.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Core.weaponConfigFile, ex);
	    }
	}
	
	public static void saveDefaultWeaponsConfig() {
	    if (Core.weaponConfigFile == null) {
	    	Core.weaponConfigFile = new File(Core.instance.getDataFolder(), "weapons.yml");
	    }
	    if (!Core.weaponConfigFile.exists()) {            
	    	Core.instance.saveResource("weapons.yml", false);
	    }
	}
	

	public static void reloadCouplesConfig() {
	    if (Core.coupleConfigFile == null) {
	    	Core.coupleConfigFile = new File(Core.instance.getDataFolder(), "couples.yml");
	    }
	    Core.coupleConfig = YamlConfiguration.loadConfiguration(Core.coupleConfigFile);
	}
	
	public static FileConfiguration getCouplesConfig(){
	    if (Core.coupleConfig == null) {
	    	reloadCouplesConfig();
	    }
	    return Core.coupleConfig;
	}
	
	public static void saveCouplesConfig() {
	    if (Core.coupleConfig == null || Core.coupleConfigFile == null) {
	        return;
	    }
	    try {
	        getCouplesConfig().save(Core.coupleConfigFile);
	    } catch (IOException ex) {
	    	Core.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Core.coupleConfigFile, ex);
	    }
	}
	
	public static void saveDefaultCouplesConfig() {
	    if (Core.coupleConfigFile == null) {
	    	Core.coupleConfigFile = new File(Core.instance.getDataFolder(), "couples.yml");
	    }
	    if (!Core.coupleConfigFile.exists()) {            
	    	Core.instance.saveResource("couples.yml", false);
	    }
	}
	
	
	public static void saveConfigs(){
        try {
            if(Core.instance.getConfig().getBoolean("saveconfigs")){
            	Core.instance.getConfig().save(new File(Core.getSavesFolder(), "config.yml"));
	        	getPlayerConfig().save(new File(Core.getSavesFolder(), "players.yml"));
    			getCitiesConfig().save(new File(Core.getSavesFolder(), "cities.yml"));
    			getDatabase().save(new File(Core.getSavesFolder(), "database.yml"));
    			getPathConfig().save(new File(Core.getSavesFolder(), "paths.yml"));
    			getWeaponsConfig().save(new File(Core.getSavesFolder(), "weapons.yml"));
    			getCouplesConfig().save(new File(Core.getSavesFolder(), "couples.yml"));
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
