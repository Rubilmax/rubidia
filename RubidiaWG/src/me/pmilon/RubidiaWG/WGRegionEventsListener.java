package me.pmilon.RubidiaWG;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.pmilon.RubidiaWG.events.RegionEnterEvent;
import me.pmilon.RubidiaWG.events.RegionLeaveEvent;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.*;

public class WGRegionEventsListener implements Listener {
	
    private RubidiaWGPlugin plugin;
    
    private Map<Player, Set<ProtectedRegion>> playerRegions;
    
    public WGRegionEventsListener(RubidiaWGPlugin plugin) {
        this.plugin = plugin;
        playerRegions = new HashMap<Player, Set<ProtectedRegion>>();
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Set<ProtectedRegion> regions = playerRegions.remove(e.getPlayer());
        if (regions != null) {
            for(ProtectedRegion region : regions) {
                RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e.getPlayer().getLocation(), e.getPlayer().getLocation(), e);

                plugin.getServer().getPluginManager().callEvent(leaveEvent);
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Set<ProtectedRegion> regions = playerRegions.remove(e.getPlayer());
        if (regions != null) {
            for(ProtectedRegion region : regions) {
                RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e.getPlayer().getLocation(), e.getPlayer().getLocation(), e);

                plugin.getServer().getPluginManager().callEvent(leaveEvent);
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        e.setCancelled(updateRegions(e.getPlayer(), MovementWay.MOVE, e.getFrom(), e.getTo(), e));
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        e.setCancelled(updateRegions(e.getPlayer(), MovementWay.TELEPORT, e.getFrom(), e.getTo(), e));
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getPlayer().getLocation(), e.getPlayer().getLocation(), e);
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getPlayer().getLocation(), e.getRespawnLocation(), e);
    }
    
    private synchronized boolean updateRegions(final Player player, final MovementWay movement, Location from, Location to, final PlayerEvent event) {
        Set<ProtectedRegion> regions;
        Set<ProtectedRegion> oldRegions;
        
        if (playerRegions.get(player) == null) {
            regions = new HashSet<ProtectedRegion>();
        } else {
            regions = new HashSet<ProtectedRegion>(playerRegions.get(player));
        }
        
        oldRegions = new HashSet<ProtectedRegion>(regions);

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager manager = container.get(BukkitAdapter.adapt(to.getWorld()));
		ApplicableRegionSet set = manager.getApplicableRegions(BukkitAdapter.asBlockVector(to));
        
        for (final ProtectedRegion region : set) {
            if (!regions.contains(region)) {
                RegionEnterEvent e = new RegionEnterEvent(region, player, movement, from, to, event);
                
                plugin.getServer().getPluginManager().callEvent(e);
                
                if (e.isCancelled()) {
                    regions.clear();
                    regions.addAll(oldRegions);
                    
                    return true;
                } else {
                    regions.add(region);
                }
            }
        }
        
        Collection<ProtectedRegion> app = set.getRegions();
        Iterator<ProtectedRegion> itr = regions.iterator();
        while(itr.hasNext()) {
            final ProtectedRegion region = itr.next();
            if (!app.contains(region)) {
                if (manager.getRegion(region.getId()) != region) {
                    itr.remove();
                    continue;
                }
                RegionLeaveEvent e = new RegionLeaveEvent(region, player, movement, from, to, event);

                plugin.getServer().getPluginManager().callEvent(e);

                if (e.isCancelled())
                {
                    regions.clear();
                    regions.addAll(oldRegions);
                    return true;
                }
                else
                {
                    itr.remove();
                }
            }
        }
        playerRegions.put(player, regions);
        return false;
    }
}