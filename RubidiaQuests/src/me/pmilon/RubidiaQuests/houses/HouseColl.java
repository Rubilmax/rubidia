package me.pmilon.RubidiaQuests.houses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.utils.Configs;

public class HouseColl extends Database<String, House> {
	
	public static HashMap<Location, House> houses = new HashMap<Location, House>();

	public HouseColl() {
		if(Configs.getHousesConfig().contains("houses")){
			for(String uniqueId : Configs.getHousesConfig().getConfigurationSection("houses").getKeys(false)) {
				String path = "houses." + uniqueId;
				List<Block> blocks = new ArrayList<Block>();
				if(Configs.getHousesConfig().contains(path + ".locations")) {
					for(String id : Configs.getHousesConfig().getConfigurationSection(path + ".locations").getKeys(false)) {
						Location location = (Location) Configs.getHousesConfig().get(path + ".locations." + id);
						blocks.add(location.getBlock());
					}
				} else QuestsPlugin.console.sendMessage("§cNo location for house §4" + uniqueId);
				
				House house = new House(uniqueId,
						Configs.getHousesConfig().getString(path + ".ownerUniqueId"),
						blocks, Configs.getHousesConfig().getDouble(path + ".blockPrice"));
				for(Block block : house.getBlocks()) {
					houses.put(block.getLocation(), house);
				}
				this.load(uniqueId, house);
			}
		}
		QuestsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §eHOUSES");
	}
	
	public House get(RPlayer owner) {
		for(House house : this.data()) {
			if(house.isInhabited()) {
				if(house.getOwner().equals(owner)) {
					return house;
				}
			}
		}
		return null;
	}
	
	public House get(Location location) {
		return houses.get(location.getBlock().getLocation());
	}

	@Override
	protected void onSaveEnd(boolean arg0) {
		Configs.saveHousesConfig();
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) QuestsPlugin.console.sendMessage("§a   Saving Houses...");
	}

	@Override
	protected void save(boolean debug, House house) {
		String path = "houses." + house.getUniqueId();
		Configs.getHousesConfig().set(path + ".ownerUniqueId", house.getOwnerUniqueId());
		Configs.getHousesConfig().set(path + ".blockPrice", house.getBlockPrice());
		for(int i = 0;i < house.getBlocks().size();i++) {
			Configs.getHousesConfig().set(path + ".locations." + i, house.getBlocks().get(i).getLocation());
		}
	}

	@Override
	protected House getDefault(String uuid) {
		return new House(uuid, "", new ArrayList<Block>(), 5);
	}

}
