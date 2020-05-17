package me.pmilon.RubidiaCore.tags;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TagStandManager {

	public static HashMap<Entity, TagStand> tagStands = new HashMap<Entity,TagStand>();
	
	public static void registerTagStand(TagStand tag){
		TagStandManager.tagStands.put(tag.getHolder(), tag);
	}
	
	public static void unregisterTagStand(TagStand tag){
		if(TagStandManager.tagStands.containsKey(tag.getHolder())) TagStandManager.tagStands.remove(tag.getHolder());
	}
	
	public static TagStand getTagStand(Entity holder){
		if(TagStandManager.tagStands.containsKey(holder)) return TagStandManager.tagStands.get(holder);
		return null;
	}
	
	public static boolean hasTagStand(Entity holder){
		return TagStandManager.tagStands.containsKey(holder);
	}
	
	public static void update(Player player) {
		for (TagStand tag : TagStandManager.tagStands.values()) {
			tag.show(player);
		}
	}

}
