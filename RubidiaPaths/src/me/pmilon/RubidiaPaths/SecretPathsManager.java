package me.pmilon.RubidiaPaths;

import java.util.HashSet;
import java.util.Set;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerMoveEvent;
import me.pmilon.RubidiaPaths.commands.PortalCommandExecutor;
import me.pmilon.RubidiaPaths.paths.SecretPath;
import me.pmilon.RubidiaPaths.paths.SecretPathColl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class SecretPathsManager extends JavaPlugin implements Listener{
	
	public static WorldEditPlugin we;
	public static SecretPathsManager instance;
	public SecretPathColl coll;
	
	public static final Set<RPlayer> teleported = new HashSet<RPlayer>();
	
	public class ZoneVector {
	}
	
	public void onEnable(){
		instance = this;
		coll = new SecretPathColl(this);
		we = (WorldEditPlugin) getServer().getPluginManager() .getPlugin("WorldEdit");
		Bukkit.getPluginManager().registerEvents(this, this);
	    this.getCommand("portal").setExecutor(new PortalCommandExecutor());
	}
	
	public void onDisable(){
		this.coll.save();
	}
	
	@EventHandler
	public void onMove(RPlayerMoveEvent event){
		RPlayer rp = event.getRPlayer();
		Location location = event.getEvent().getTo();
		
		boolean insidePath = false;
		boolean beenTeleported = teleported.contains(rp);
		for(SecretPath path : SecretPathColl.paths){
			if(path.check(location)){
				if(beenTeleported){
					insidePath = true;
					break;
				} else {
					path.use(rp);
					return;
				}
			}
		}
		if(!insidePath)teleported.remove(rp);
	}
	
}