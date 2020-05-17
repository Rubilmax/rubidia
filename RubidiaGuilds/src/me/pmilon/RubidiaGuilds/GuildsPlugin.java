package me.pmilon.RubidiaGuilds;

import java.io.File;
import java.util.HashMap;

import me.pmilon.RubidiaGuilds.claims.ClaimColl;
import me.pmilon.RubidiaGuilds.claims.ClaimsListener;
import me.pmilon.RubidiaGuilds.commands.GHomeCommandExecutor;
import me.pmilon.RubidiaGuilds.commands.GuildCommandExecutor;
import me.pmilon.RubidiaGuilds.commands.GuildsCommandExecutor;
import me.pmilon.RubidiaGuilds.commands.MapCommandExecutor;
import me.pmilon.RubidiaGuilds.commands.RaidCommandExecutor;
import me.pmilon.RubidiaGuilds.guilds.GColl;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.GMemberColl;
import me.pmilon.RubidiaGuilds.raids.RaidColl;
import me.pmilon.RubidiaGuilds.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class GuildsPlugin extends JavaPlugin {

	public static GuildsPlugin instance;
	public static ConsoleCommandSender console;
	public static GMemberColl gmembercoll;
	public static GColl gcoll;
	public static RaidColl raidcoll;
	public static ClaimColl claimcoll;
	public static MainListener mainListener;
	public static ClaimsListener claimsListener;
	
	public static File guildConfigFile;
	public static FileConfiguration guildConfig;
	public static File membersConfigFile;
	public static FileConfiguration membersConfig;
	public static File raidsConfigFile;
	public static FileConfiguration raidsConfig;
	public static File claimsConfigFile;
	public static FileConfiguration claimsConfig;
	
	public static HashMap<GMember, String> guildCreationName = new HashMap<GMember, String>();
	public static HashMap<GMember, String> guildCreationDescription = new HashMap<GMember, String>();
	public static HashMap<GMember, Boolean> guildCreationPeaceful = new HashMap<GMember, Boolean>();
	
	public void onEnable(){
		instance = this;
		mainListener = new MainListener(this);
		claimsListener = new ClaimsListener(this);
		
		this.getCommand("guild").setExecutor(new GuildCommandExecutor());
		this.getCommand("guilds").setExecutor(new GuildsCommandExecutor());
		this.getCommand("ghome").setExecutor(new GHomeCommandExecutor());
		this.getCommand("raid").setExecutor(new RaidCommandExecutor());
		this.getCommand("map").setExecutor(new MapCommandExecutor());
		
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		Configs.saveDefaultGuildConfig();
		Configs.saveDefaultMembersConfig();
		Configs.saveDefaultRaidsConfig();
		Configs.saveDefaultClaimsConfig();
	}
	
	public static void onStart(){
		console = Bukkit.getConsoleSender();
		console.sendMessage("§a   Loading GMembers...");
		gmembercoll = new GMemberColl();
		console.sendMessage("§a   Loading Claims...");
		claimcoll = new ClaimColl();
		console.sendMessage("§a   Loading Guilds...");
		gcoll = new GColl();
		console.sendMessage("§a   Loading Raids...");
		raidcoll = new RaidColl();
	}
	
	public static void onEnd(){
		gmembercoll.saveAll(true);
		claimcoll.saveAll(true);
		gcoll.saveAll(true);
		raidcoll.saveAll(true);
	}
	
}
