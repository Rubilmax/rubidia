package me.pmilon.RubidiaMonsters.regions;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.regions.Region.RegionType;

import org.bukkit.Location;

public class Regions {
	
	public static List<Region> regions = new ArrayList<Region>();
	
	public static void onEnable(boolean debug){
		if (debug) RubidiaMonstersPlugin.console.sendMessage("§a    Loading Regions...");
		if(RubidiaMonstersPlugin.getInstance().getConfig().contains("regions")){
			for(String rId : RubidiaMonstersPlugin.getInstance().getConfig().getConfigurationSection("regions").getKeys(false)){
				String path = "regions." + rId;
				List<Monster> monsters = new ArrayList<Monster>();
				if(RubidiaMonstersPlugin.getInstance().getConfig().contains(path + ".monsterUUIDs")){
					for(String uuid : RubidiaMonstersPlugin.getInstance().getConfig().getStringList(path + ".monsterUUIDs")){
						monsters.add(Monsters.get(uuid));
					}
				}
				Region region = new Region(rId,
						(Location)RubidiaMonstersPlugin.getInstance().getConfig().get(path + ".center"),
						RubidiaMonstersPlugin.getInstance().getConfig().getDouble(path + ".xRange"),
						RubidiaMonstersPlugin.getInstance().getConfig().getDouble(path + ".yRange"),
						RubidiaMonstersPlugin.getInstance().getConfig().getDouble(path + ".zRange"),
						monsters,
						RubidiaMonstersPlugin.getInstance().getConfig().getBoolean(path + ".square"),
						RubidiaMonstersPlugin.getInstance().getConfig().getInt(path + ".minLevel"),
						RubidiaMonstersPlugin.getInstance().getConfig().getInt(path + ".maxLevel"),
						RubidiaMonstersPlugin.getInstance().getConfig().getDouble(path + ".yShift"),
						RubidiaMonstersPlugin.getInstance().getConfig().getBoolean(path + ".fadingLevel"),
						RubidiaMonstersPlugin.getInstance().getConfig().getInt(path + ".maxMonstersAmount"),
						RubidiaMonstersPlugin.getInstance().getConfig().getDouble(path + ".rageProbability"),
						RubidiaMonstersPlugin.getInstance().getConfig().getString(path + ".dungeonUUID"),
						RubidiaMonstersPlugin.getInstance().getConfig().contains(path + ".regionType") ? RegionType.valueOf(RubidiaMonstersPlugin.getInstance().getConfig().getString(path + ".regionType")) : RegionType.DEFAULT);
				regions.add(region);
				if(Regions.regions.size() % 25 == 0){
					if(debug)RubidiaMonstersPlugin.console.sendMessage("§6LOADED §e" + Regions.regions.size() + " §6REGIONS");
				}
			}
		}
		if(debug)RubidiaMonstersPlugin.console.sendMessage("§6LOADED §e" + Regions.regions.size() + " §6REGIONS");
	}
	
	public static void save(boolean debug){
		if (debug) RubidiaMonstersPlugin.console.sendMessage("§a    Saving Regions...");
		for(Region region : regions){
			String path = "regions." + region.getUUID();
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".center", region.getCenter());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".xRange", region.getXRange());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".yRange", region.getYRange());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".zRange", region.getZRange());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".square", region.isSquare());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".minLevel", region.getMinLevel());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".maxLevel", region.getMaxLevel());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".yShift", region.getYShift());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".fadingLevel", region.isFadingLevel());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".maxMonstersAmount", region.getMaxMonstersAmount());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".rageProbability", region.getRageProbability());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".dungeonUUID", region.getDungeonUUID());
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".regionType", region.getType().toString());
			List<String> mUUIDs = new ArrayList<String>();
			for(Monster monster : region.getMonsters()){
				mUUIDs.add(monster.getUUID());
			}
			RubidiaMonstersPlugin.getInstance().getConfig().set(path + ".monsterUUIDs", mUUIDs);
			if(debug)RubidiaMonstersPlugin.console.sendMessage("§6Saved §e" + region.getUUID());
		}
		RubidiaMonstersPlugin.getInstance().saveConfig();
	}
	
	public static Region get(String uuid){
		for(Region region : regions){
			if(region.getUUID().equals(uuid)){
				return region;
			}
		}
		return null;
	}
	
	public static Region get(Location location){
		for(Region region : regions){
			if(region.isIn(location)){
				return region;
			}
		}
		return null;
	}
	
	public static Region getByCenter(Location center){
		for(Region region : regions){
			if(region.getCenter().getWorld().equals(center.getWorld())){
				if(region.getCenter().distanceSquared(center) < 1){
					return region;
				}
			}
		}
		return null;
	}

}
