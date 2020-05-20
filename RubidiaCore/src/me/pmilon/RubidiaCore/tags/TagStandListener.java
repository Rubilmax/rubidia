package me.pmilon.RubidiaCore.tags;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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
	
	public Core getPlugin() {
		return plugin;
	}

	public void setPlugin(Core plugin) {
		this.plugin = plugin;
	}
}
