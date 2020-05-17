package me.pmilon.RubidiaMonsters.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityUtils {

	public static Entity byUUID(World world, UUID uuid){
		if(world == null){
			for(World world1 : Bukkit.getWorlds()){
				for(Entity entity : world1.getEntities()){
					if(entity.getUniqueId().equals(uuid))
						return entity;
				}
			}
		}else{
			for(Entity entity : world.getEntities()){
				if(entity.getUniqueId().equals(uuid))
					return entity;
			}
		}
					
		return null;
	}

}