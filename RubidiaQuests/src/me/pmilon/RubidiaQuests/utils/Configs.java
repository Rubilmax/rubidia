package me.pmilon.RubidiaQuests.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.QuestsPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configs {
	
	public static File questsConfigFile;
	public static FileConfiguration questsConfig;
	public static File pnjConfigFile;
	public static FileConfiguration pnjConfig;
	public static File shopsConfigFile;
	public static FileConfiguration shopsConfig;
	public static File housesConfigFile;
	public static FileConfiguration housesConfig;

	public static void reloadQuestsConfig() {
	    if (Configs.questsConfigFile == null) {
			Configs.questsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "quests.yml");
	    }
	    Configs.questsConfig = YamlConfiguration.loadConfiguration(Configs.questsConfigFile);
	}
	
	public static FileConfiguration getQuestsConfig(){
	    if (Configs.questsConfig == null) {
	        reloadQuestsConfig();
	    }
	    return Configs.questsConfig;
	}
	
	public static void saveQuestsConfig() {
	    if (Configs.questsConfig == null || Configs.questsConfigFile == null) {
	        return;
	    }
	    try {
	        getQuestsConfig().save(Configs.questsConfigFile);
	    } catch (IOException ex) {
	    	QuestsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Configs.questsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultQuestsConfig() {
	    if (Configs.questsConfigFile == null) {
	    	Configs.questsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "quests.yml");
	    }
	    if (!Configs.questsConfigFile.exists()) {            
	    	QuestsPlugin.instance.saveResource("quests.yml", false);
	    }
	}
	

	public static void reloadPNJConfig() {
	    if (Configs.pnjConfigFile == null) {
	    	Configs.pnjConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "pnjs.yml");
	    }
	    Configs.pnjConfig = YamlConfiguration.loadConfiguration(Configs.pnjConfigFile);
	}
	
	public static FileConfiguration getPNJConfig(){
	    if (Configs.pnjConfig == null) {
	        reloadPNJConfig();
	    }
	    return Configs.pnjConfig;
	}
	
	public static void savePNJConfig() {
	    if (Configs.pnjConfig == null || Configs.pnjConfigFile == null) {
	        return;
	    }
	    try {
	        getPNJConfig().save(Configs.pnjConfigFile);
	    } catch (IOException ex) {
	    	QuestsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Configs.pnjConfigFile, ex);
	    }
	}
	
	public static void saveDefaultPNJConfig() {
	    if (Configs.pnjConfigFile == null) {
	    	Configs.pnjConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "pnjs.yml");
	    }
	    if (!Configs.pnjConfigFile.exists()) {            
	    	QuestsPlugin.instance.saveResource("pnjs.yml", false);
	    }
	}
	

	public static void reloadShopsConfig() {
	    if (Configs.shopsConfigFile == null) {
	    	Configs.shopsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "shops.yml");
	    }
	    Configs.shopsConfig = YamlConfiguration.loadConfiguration(Configs.shopsConfigFile);
	}
	
	public static FileConfiguration getShopsConfig(){
	    if (Configs.shopsConfig == null) {
	        reloadShopsConfig();
	    }
	    return Configs.shopsConfig;
	}
	
	public static void saveShopsConfig() {
	    if (Configs.shopsConfig == null || Configs.shopsConfigFile == null) {
	        return;
	    }
	    try {
	        getShopsConfig().save(Configs.shopsConfigFile);
	    } catch (IOException ex) {
	    	QuestsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Configs.shopsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultShopsConfig() {
	    if (Configs.shopsConfigFile == null) {
	    	Configs.shopsConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "shops.yml");
	    }
	    if (!Configs.shopsConfigFile.exists()) {            
	    	QuestsPlugin.instance.saveResource("shops.yml", false);
	    }
	}


	public static void reloadHousesConfig() {
	    if (Configs.housesConfigFile == null) {
	    	Configs.housesConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "houses.yml");
	    }
	    Configs.housesConfig = YamlConfiguration.loadConfiguration(Configs.housesConfigFile);
	}
	
	public static FileConfiguration getHousesConfig(){
	    if (Configs.housesConfig == null) {
	        reloadHousesConfig();
	    }
	    return Configs.housesConfig;
	}
	
	public static void saveHousesConfig() {
	    if (Configs.housesConfig == null || Configs.housesConfigFile == null) {
	        return;
	    }
	    try {
	        getHousesConfig().save(Configs.housesConfigFile);
	    } catch (IOException ex) {
	    	QuestsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + Configs.housesConfigFile, ex);
	    }
	}
	
	public static void saveDefaultHousesConfig() {
	    if (Configs.housesConfigFile == null) {
	    	Configs.housesConfigFile = new File(QuestsPlugin.instance.getDataFolder(), "houses.yml");
	    }
	    if (!Configs.housesConfigFile.exists()) {            
	    	QuestsPlugin.instance.saveResource("houses.yml", false);
	    }
	}
	
	public static void saveConfigs(){
        try {
        	QuestsPlugin.instance.getConfig().save(new File(Core.getSavesFolder(), "qconfig.yml"));
        	Configs.getQuestsConfig().save(new File(Core.getSavesFolder(), "quests.yml"));
        	Configs.getPNJConfig().save(new File(Core.getSavesFolder(), "pnjs.yml"));
        	Configs.getShopsConfig().save(new File(Core.getSavesFolder(), "shops.yml"));
        	Configs.getHousesConfig().save(new File(Core.getSavesFolder(), "houses.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
