package me.pmilon.RubidiaCore.tags;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TagStandManager {

	public static HashMap<String, TagStand> tagStands = new HashMap<String,TagStand>();
	
	public static void registerTagStand(TagStand tag){
		TagStandManager.tagStands.put(tag.getHolder().getUniqueId().toString(), tag);
	}
	
	public static void unregisterTagStand(TagStand tag){
		String uuid = tag.getHolder().getUniqueId().toString();
		if(TagStandManager.tagStands.containsKey(uuid)) {
			TagStandManager.tagStands.remove(uuid);
		}
	}
	
	public static TagStand getTagStand(Entity holder){
		String uuid = holder.getUniqueId().toString();
		if(TagStandManager.tagStands.containsKey(uuid)) {
			return TagStandManager.tagStands.get(uuid);
		}
		return null;
	}
	
	public static boolean hasTagStand(Entity holder){
		return TagStandManager.tagStands.containsKey(holder.getUniqueId().toString());
	}
	
	public static void update(Player player) {
		for (TagStand tag : TagStandManager.tagStands.values()) {
			tag.show(player);
		}
	}

}
