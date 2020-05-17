package me.pmilon.RubidiaMonsters.dungeons;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaMonsters.utils.Configs;

import org.bukkit.Location;

public class Dungeons {
	
	public static List<Dungeon> dungeons = new ArrayList<Dungeon>();
	
	public static void onEnable(boolean debug){
		if(Configs.getDungeonsConfig().contains("dungeons")){
			for(String uuid : Configs.getDungeonsConfig().getConfigurationSection("dungeons").getKeys(false)){
				String path = "dungeons." + uuid;
				List<Region> rooms = new ArrayList<Region>();
				if(Configs.getDungeonsConfig().contains(path + ".regionUUIDs")){
					for(String ruuid : Configs.getDungeonsConfig().getStringList(path + ".regionUUIDs")){
						rooms.add(Regions.get(ruuid));
					}
				}
				Dungeon dungeon = new Dungeon(uuid,
						rooms,
						(Location) Configs.getDungeonsConfig().get(path + ".entry"),
						Configs.getDungeonsConfig().getInt(path + ".minLevel"));
				dungeons.add(dungeon);
				if(debug)RubidiaMonstersPlugin.console.sendMessage("§6Loaded dungeon : §e" + dungeon.getUUID());
			}
		}
	}
	
	public static void onDisable(boolean debug){
		for(Dungeon dungeon : dungeons){
			String path = "dungeons." + dungeon.getUUID();
			Configs.getDungeonsConfig().set(path + ".entry", dungeon.getEntry());
			Configs.getDungeonsConfig().set(path + ".minLevel", dungeon.getMinLevel());
			List<String> rUUIDs = new ArrayList<String>();
			for(Region region : dungeon.getRooms()){
				rUUIDs.add(region.getUUID());
			}
			Configs.getDungeonsConfig().set(path + ".regionUUIDs", rUUIDs);
			if(debug)RubidiaMonstersPlugin.console.sendMessage("§6Saved §e" + dungeon.getUUID());
		}
		RubidiaMonstersPlugin.getInstance().saveConfig();
	}
	
	public static Dungeon get(String uuid){
		for(Dungeon dungeon : dungeons){
			if(dungeon.getUUID().equals(uuid)){
				return dungeon;
			}
		}
		return null;
	}

}
