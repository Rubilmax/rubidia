package me.pmilon.RubidiaCore.tags;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class TagStandListener implements Listener {

	private Core plugin;
	public TagStandListener(Core plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		new BukkitTask(Core.instance) {

			@Override
			public void run() {
				TagStandManager.update(event.getPlayer());
			}

			@Override
			public void onCancel() {
			}
		
		// PlayerRespawnEvent is triggered just before player's respawn
		// so we run a task on the next server tick (to make sure the player's respawned)
		}.runTaskLater(5);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		new BukkitTask(Core.instance) {

			@Override
			public void run() {
				TagStandManager.update(event.getPlayer());
			}

			@Override
			public void onCancel() {
			}
		
		// PlayerRespawnEvent is triggered just before player's respawn
		// so we run a task on the next server tick (to make sure the player's respawned)
		}.runTaskLater(5);
	}
	
	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		new BukkitTask(Core.instance) {

			@Override
			public void run() {
				TagStandManager.update(event.getPlayer());
			}

			@Override
			public void onCancel() {
			}
		
		// PlayerRespawnEvent is triggered just before player's respawn
		// so we run a task on the next server tick (to make sure the player's respawned)
		}.runTaskLater(5);
	}
	
	@EventHandler
	public void onEntityMount(EntityMountEvent event) {
		Entity mounted = event.getMount();
		if (mounted != null) {
			if (TagStandManager.hasTagStand(mounted)) {
				TagStand stand = TagStandManager.getTagStand(mounted);
				stand.remove();
				TagStandManager.registerTagStand(stand);
			}
		}
	}
	
	@EventHandler
	public void onEntityDismount(EntityDismountEvent event) {
		Entity dismounted = event.getDismounted();
		if (dismounted != null) {
			if (TagStandManager.hasTagStand(dismounted)) {
				TagStand stand = TagStandManager.getTagStand(dismounted);
				stand.display();
			}
		}
	}
	
	public Core getPlugin() {
		return plugin;
	}

	public void setPlugin(Core plugin) {
		this.plugin = plugin;
	}
}
