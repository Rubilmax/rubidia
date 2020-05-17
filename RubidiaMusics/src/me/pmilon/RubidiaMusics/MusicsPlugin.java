package me.pmilon.RubidiaMusics;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaWG.Flags;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class MusicsPlugin extends JavaPlugin implements Listener {

	public static MusicsPlugin instance;
	public WorldGuardPlugin wgPlugin;

	public void onEnable(){
		instance = this;
        wgPlugin = getWGPlugin();
        if (wgPlugin == null){
            getLogger().warning("Could not find World Guard, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		Bukkit.getPluginManager().registerEvents(this, this);
		
		this.reloadConfig();
	}
	
	public void onDisable(){
		this.saveConfig();
	}
    
    private WorldGuardPlugin getWGPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (plugin == null || !(plugin instanceof WorldGuardPlugin))
        {
            return null;
        }
        
        return (WorldGuardPlugin) plugin;
    }
	
	/*@EventHandler
	public void onBoatEnter(VehicleEnterEvent event){
		Entity entity = event.getEntered();
		if(entity instanceof Player){
			Player player= (Player)entity;
			Entity vehicle = event.getVehicle();
			if(vehicle instanceof Boat){
				SongManager.playSong(player, Song.BOAT_TRAVEL, player.getLocation());
			}
		}
	}*/
	
	/*@EventHandler
	public void onAeroplane(EntityToggleGlideEvent event){
		Entity entity = event.getEntity();
		if(entity instanceof Player){
			Player player = (Player)entity;
			if(event.isGliding())SongManager.playSong(player, Song.AERO_TRAVEL, player.getLocation());
			else{
				ProtectedRegion region = WGUtils.getHighestPriorityWithFlag(this.wgPlugin.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()), Flags.MUSIC);
				if(region != null){
					String music = region.getFlag(Flags.MUSIC);
					SongManager.playSong(player, Song.getByName(music), (Location) this.getConfig().get(region.getId()));
				}
			}
		}
	}*/
	
	/*@EventHandler
	public void onBoatQuit(VehicleExitEvent event){
		Entity entity = event.getExited();
		if(entity instanceof Player){
			Player player = (Player)entity;
			Entity vehicle = event.getVehicle();
			if(vehicle instanceof Boat){
				ProtectedRegion region = WGUtils.getHighestPriorityWithFlag(this.wgPlugin.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()), Flags.MUSIC);
				if(region != null){
					String music = region.getFlag(Flags.MUSIC);
					SongManager.playSong(player, Song.getByName(music), (Location) this.getConfig().get(region.getId()));
				}
			}
		}
	}*/

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		Location to = event.getTo();
		if(SongManager.attemptingErasing.containsKey(player)){
			List<Location> toRemove = new ArrayList<Location>();
			for(Location location : SongManager.attemptingErasing.get(player)){
				if(!location.getWorld().equals(to.getWorld()))return;
				if(to.distanceSquared(location) < 3844){
					toRemove.add(location);
				}
			}
			for(Location location : toRemove){
				SongManager.eraseAttemptingLocation(player, location);
			}
		}
		if(SongManager.attemptingPlaying.containsKey(player)){
			Location location = SongManager.attemptingPlayingLocations.get(player);
			if(!location.getWorld().equals(to.getWorld()))return;
			if(to.distanceSquared(location) < 3844){
				SongManager.playAttemptingPlaying(player, location);
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
					Player player = event.getPlayer();
					RPlayer rp = RPlayer.get(player);
					Block block = event.getClickedBlock();
					if(rp.isOp() && block.getType().equals(Material.JUKEBOX) && player.isSneaking()){
						ProtectedRegion region = WGUtils.getHighestPriorityWithFlag(this.wgPlugin.getRegionManager(player.getWorld()).getApplicableRegions(block.getLocation()), Flags.MUSIC);
						if(region != null){
							this.getConfig().set(region.getId(), block.getLocation());
							player.sendMessage("Jukebox added to region " + region.getId());
						}
					}
				}
			}
		}
	}
}
