package me.pmilon.RubidiaManager.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;
import me.pmilon.RubidiaManager.chunks.RChunk;

public class WorldsRegenTask extends AbstractTask {

	public WorldsRegenTask(RubidiaManagerPlugin plugin) {
		super(plugin);
	}

	@Override
	public void runTaskSynchronously() {
		RPlayer.broadcastMessage("§4RÉGÉNÉRATION DES CARTES... §cDes lags sont à prévoir");
		RPlayer.broadcastTitle("§4RÉGÉNÉRATION DES CARTES...","§cDes lags sont à prévoir",0,100,40);
		List<String> names = this.getPlugin().getConfig().getStringList("regenWorlds");
		if(names != null) {
			int delay = 0;
			for(int i = 0;i < names.size();i++) {
				final World world = Bukkit.getWorld(names.get(i));
				if(world != null) {
					final List<RChunk> toRegen = RubidiaManagerPlugin.getToRegen(world);
					new BukkitTask(this.getPlugin()) {
						
						@Override
						public void run(){
							Core.console.sendMessage("§eRegenerating §6" + world.getName() + "§e...");
							new BukkitTask(this.getPlugin()) {
								public void run(){
									Core.console.sendMessage("§e" + world.getName() + " §6regenerated!");
								}

								@Override
								public void onCancel() {
								}
							}.runTaskLater(RubidiaManagerPlugin.regen(toRegen));
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(delay + 20);
					delay += toRegen.size()*RubidiaManagerPlugin.TICKS_PER_CHUNK_REGEN;
				} else Core.console.sendMessage("§cCouldn't regen world §4" + names.get(i) + "§c: unable to find it.");
			}
			
			new BukkitTask(this.getPlugin()) {
				
				@Override
				public void run(){
					RPlayer.broadcastMessage("§2RÉGÉNÉRATION TERMINÉE");
					RPlayer.broadcastTitle("§2RÉGÉNÉRATION TERMINÉE","",0,100,40);
					Core.console.sendMessage("§aFinished worlds regeneration.");
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(delay + 30);
		} else Core.console.sendMessage("§cNow is the time to regen worlds, but no worlds have been referenced in config.");
	}

}
