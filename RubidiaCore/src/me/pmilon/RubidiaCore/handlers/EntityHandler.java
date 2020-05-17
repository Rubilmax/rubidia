package me.pmilon.RubidiaCore.handlers;
 
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
 
public class EntityHandler {
    public static Table<Integer, Integer, Boolean> observerEntityMap = HashBasedTable.create();
 
    private static final PacketType[] ENTITY_PACKETS = { 
        PacketType.Play.Server.ENTITY_EQUIPMENT, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.COLLECT,
        PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.SPAWN_ENTITY_PAINTING,
        PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB, PacketType.Play.Server.ENTITY_VELOCITY, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.ENTITY_LOOK,
        PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_TELEPORT,
        PacketType.Play.Server.ENTITY_HEAD_ROTATION, PacketType.Play.Server.ENTITY_STATUS, PacketType.Play.Server.ATTACH_ENTITY,
        PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.ENTITY_EFFECT, PacketType.Play.Server.REMOVE_ENTITY_EFFECT,
        PacketType.Play.Server.BLOCK_BREAK_ANIMATION
    };
 
    public static void onEnable(Plugin plugin){
    	plugin.getServer().getPluginManager().registerEvents(new EntityListener(), plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ENTITY_PACKETS){
        	
	        @Override
	        public void onPacketSending(PacketEvent event) {
                int entityID = event.getPacket().getIntegers().read(0);
 
                if (!EntityHandler.isVisible(event.getPlayer(), entityID)) {
                    event.setCancelled(true);
                }
	        }
	        
	    });
    }
 
    private static boolean setVisibility(Player observer, int entityID, boolean visible) {
        return !setMembership(observer, entityID, !visible);
    }
 
    private static boolean setMembership(Player observer, int entityID, boolean member) {
        if (member) {
            return observerEntityMap.put(observer.getEntityId(), entityID, true) != null;
        } else {
            return observerEntityMap.remove(observer.getEntityId(), entityID) != null;
        }
    }
 
    private static boolean getMembership(Player observer, int entityID) {
        return observerEntityMap.contains(observer.getEntityId(), entityID);
    }
 
    private static boolean isVisible(Player observer, int entityID) { 
        return !getMembership(observer, entityID); 
    }
 
    public static void removeEntity(Entity entity, boolean destroyed) {
        int entityID = entity.getEntityId();
 
        for (Map<Integer, Boolean> maps : observerEntityMap.rowMap().values()) {
            maps.remove(entityID);
        }
    }
 
    public static void removePlayer(Player player) {
        observerEntityMap.rowMap().remove(player.getEntityId());
    }
 
    public static final boolean toggleEntity(Player observer, Entity entity) {
        if (isVisible(observer, entity.getEntityId())) {
            return EntityHandler.hideEntity(observer, entity);
        } else {
            return !EntityHandler.showEntity(observer, entity);
        }
    }
 
    public static final boolean showEntity(Player observer, Entity entity) {
        validate(observer, entity);
        boolean hiddenBefore = !setVisibility(observer, entity.getEntityId(), true);
        
        if (hiddenBefore) {
            ProtocolLibrary.getProtocolManager().updateEntity(entity, Arrays.asList(observer));
        }
        return hiddenBefore;
    }
 
    public static final boolean hideEntity(Player observer, Entity entity) {
        validate(observer, entity);
        boolean visibleBefore = setVisibility(observer, entity.getEntityId(), false);
 
        if (visibleBefore) {
            PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            destroyEntity.getIntegerArrays().write(0, new int[] { entity.getEntityId() });
 
            // Make the entity disappear
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(observer, destroyEntity);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot send server packet.", e);
            }
        }
        return visibleBefore;
    }
 
    public static final boolean canSee(Player observer, Entity entity) {
        validate(observer, entity);
 
        return isVisible(observer, entity.getEntityId());
    }
 
    private static void validate(Player observer, Entity entity) {
        Preconditions.checkNotNull(observer, "observer cannot be NULL.");
        Preconditions.checkNotNull(entity, "entity cannot be NULL.");
    }
    
    public static class EntityListener implements Listener {
    	
        @EventHandler
        public void onEntityDeath(EntityDeathEvent e) {
            EntityHandler.removeEntity(e.getEntity(), true);
        }

        
        @EventHandler
        public void onChunkUnload(ChunkUnloadEvent e) {
            for (Entity entity : e.getChunk().getEntities()) {
            	EntityHandler.removeEntity(entity, false);
            }
        }

        
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent e) {
        	EntityHandler.removePlayer(e.getPlayer());
        }
    }
}