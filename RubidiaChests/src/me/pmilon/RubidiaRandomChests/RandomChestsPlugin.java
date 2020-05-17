package me.pmilon.RubidiaRandomChests;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomChestsPlugin extends JavaPlugin{

	private static RandomChestsPlugin instance;
	public static FileConfiguration chestsConfig;
	public static File chestsConfigFile;
	private static final HashMap<Location, LuckyChest> chests = new HashMap<Location, LuckyChest>();
	
	public void onEnable(){
		instance = this;
		Bukkit.getPluginManager().registerEvents(new ChestListener(this), this);
		this.getCommand("chest").setExecutor(new ChestCommandExecutor());
		
		if(this.getConfig().contains("chests")){
			for(String uuid : this.getConfig().getConfigurationSection("chests").getKeys(false)){
				Location location = (Location) this.getConfig().get("chests." + uuid + ".location");
				if(location != null) {
					BlockFace orientation = BlockFace.valueOf(this.getConfig().getString("chests." + uuid + ".orientation"));
					LuckyChest chest = new LuckyChest(location, orientation,
							this.getConfig().getBoolean("chests." + uuid + ".permanent"));
					chests.put(location, chest);
				}
			}
		}
	}
	
	public void onDisable(){
		this.getConfig().set("chests", null);
		for(LuckyChest chest : chests.values()){
			chest.despawn(false);
			String path = "chests." + UUID.randomUUID().toString();
			this.getConfig().set(path + ".location", chest.getLocation());
			this.getConfig().set(path + ".orientation", chest.getOrientation().toString());
			this.getConfig().set(path + ".permanent", chest.isPermanent());
		}
		this.saveConfig();
	}
	
	public static RandomChestsPlugin getInstance(){
		return instance;
	}

	public static HashMap<Location, LuckyChest> getChests() {
		return chests;
	}

}
