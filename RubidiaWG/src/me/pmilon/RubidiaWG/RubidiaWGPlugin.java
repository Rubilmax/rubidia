package me.pmilon.RubidiaWG;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class RubidiaWGPlugin extends JavaPlugin {
    
	@Override
	public void onLoad(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        for(Field field : Flags.class.getDeclaredFields()) {
        	try {
				registry.register((Flag<?>) field.get(null));
			} catch (FlagConflictException e) {
				Bukkit.getConsoleSender().sendMessage("§4Unable to register flag §c" + field.getName() + " §4because of incompatibility.");
				e.printStackTrace();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
        }
	}
	
    @Override
    public void onEnable() {
    	WorldGuardPlugin plugin = WorldGuardPlugin.inst();
        getServer().getPluginManager().registerEvents(new WGRegionEventsListener(this), plugin);
        getServer().getPluginManager().registerEvents(new WGFlagsListener(), plugin);
    }
    
}