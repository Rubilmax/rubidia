package me.pmilon.RubidiaGuilds.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaGuilds.GuildsPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configs {

	public static void reloadGuildConfig() {
	    if (GuildsPlugin.guildConfigFile == null) {
	    	GuildsPlugin.guildConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "guilds.yml");
	    }
	    GuildsPlugin.guildConfig = YamlConfiguration.loadConfiguration(GuildsPlugin.guildConfigFile);
	}
	
	public static FileConfiguration getGuildConfig(){
	    if (GuildsPlugin.guildConfig == null) {
	        reloadGuildConfig();
	    }
	    return GuildsPlugin.guildConfig;
	}
	
	public static void saveGuildConfig() {
	    if (GuildsPlugin.guildConfig == null || GuildsPlugin.guildConfigFile == null) {
	        return;
	    }
	    try {
	        getGuildConfig().save(GuildsPlugin.guildConfigFile);
	    } catch (IOException ex) {
	    	GuildsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + GuildsPlugin.guildConfigFile, ex);
	    }
	}
	
	public static void saveDefaultGuildConfig() {
	    if (GuildsPlugin.guildConfigFile == null) {
	    	GuildsPlugin.guildConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "guilds.yml");
	    }
	    if (!GuildsPlugin.guildConfigFile.exists()) {            
	    	GuildsPlugin.instance.saveResource("guilds.yml", false);
	    }
	}
	
	

	public static void reloadMembersConfig() {
	    if (GuildsPlugin.membersConfigFile == null) {
	    	GuildsPlugin.membersConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "gmembers.yml");
	    }
	    GuildsPlugin.membersConfig = YamlConfiguration.loadConfiguration(GuildsPlugin.membersConfigFile);
	}
	
	public static FileConfiguration getMembersConfig(){
	    if (GuildsPlugin.membersConfig == null) {
	        reloadMembersConfig();
	    }
	    return GuildsPlugin.membersConfig;
	}
	
	public static void saveMembersConfig() {
	    if (GuildsPlugin.membersConfig == null || GuildsPlugin.membersConfigFile == null) {
	        return;
	    }
	    try {
	        getMembersConfig().save(GuildsPlugin.membersConfigFile);
	    } catch (IOException ex) {
	    	GuildsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + GuildsPlugin.membersConfigFile, ex);
	    }
	}
	
	public static void saveDefaultMembersConfig() {
	    if (GuildsPlugin.membersConfigFile == null) {
	    	GuildsPlugin.membersConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "gmembers.yml");
	    }
	    if (!GuildsPlugin.membersConfigFile.exists()) {            
	    	GuildsPlugin.instance.saveResource("gmembers.yml", false);
	    }
	}
	

	
	public static void reloadRaidsConfig() {
	    if (GuildsPlugin.raidsConfigFile == null) {
	    	GuildsPlugin.raidsConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "raids.yml");
	    }
	    GuildsPlugin.raidsConfig = YamlConfiguration.loadConfiguration(GuildsPlugin.raidsConfigFile);
	}
	
	public static FileConfiguration getRaidsConfig(){
	    if (GuildsPlugin.raidsConfig == null) {
	        reloadRaidsConfig();
	    }
	    return GuildsPlugin.raidsConfig;
	}
	
	public static void saveRaidsConfig() {
	    if (GuildsPlugin.raidsConfig == null || GuildsPlugin.raidsConfigFile == null) {
	        return;
	    }
	    try {
	        getRaidsConfig().save(GuildsPlugin.raidsConfigFile);
	    } catch (IOException ex) {
	    	GuildsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + GuildsPlugin.raidsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultRaidsConfig() {
	    if (GuildsPlugin.raidsConfigFile == null) {
	    	GuildsPlugin.raidsConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "raids.yml");
	    }
	    if (!GuildsPlugin.raidsConfigFile.exists()) {            
	    	GuildsPlugin.instance.saveResource("raids.yml", false);
	    }
	}
	
	

	public static void reloadClaimsConfig() {
	    if (GuildsPlugin.claimsConfigFile == null) {
	    	GuildsPlugin.claimsConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "claims.yml");
	    }
	    GuildsPlugin.claimsConfig = YamlConfiguration.loadConfiguration(GuildsPlugin.claimsConfigFile);
	}
	
	public static FileConfiguration getClaimsConfig(){
	    if (GuildsPlugin.claimsConfig == null) {
	        reloadClaimsConfig();
	    }
	    return GuildsPlugin.claimsConfig;
	}
	
	public static void saveClaimsConfig() {
	    if (GuildsPlugin.claimsConfig == null || GuildsPlugin.claimsConfigFile == null) {
	        return;
	    }
	    try {
	        getClaimsConfig().save(GuildsPlugin.claimsConfigFile);
	    } catch (IOException ex) {
	    	GuildsPlugin.instance.getLogger().log(Level.SEVERE, "Could not save config to " + GuildsPlugin.claimsConfigFile, ex);
	    }
	}
	
	public static void saveDefaultClaimsConfig() {
	    if (GuildsPlugin.claimsConfigFile == null) {
	    	GuildsPlugin.claimsConfigFile = new File(GuildsPlugin.instance.getDataFolder(), "claims.yml");
	    }
	    if (!GuildsPlugin.claimsConfigFile.exists()) {            
	    	GuildsPlugin.instance.saveResource("claims.yml", false);
	    }
	}
	
	
	
	public static void saveConfigs(){
        try {
        	GuildsPlugin.instance.getConfig().save(new File(Core.getSavesFolder(), "gconfig.yml"));
        	Configs.getGuildConfig().save(new File(Core.getSavesFolder(), "guilds.yml"));
        	Configs.getMembersConfig().save(new File(Core.getSavesFolder(), "gmembers.yml"));
        	Configs.getRaidsConfig().save(new File(Core.getSavesFolder(), "raids.yml"));
        	Configs.getClaimsConfig().save(new File(Core.getSavesFolder(), "claims.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
