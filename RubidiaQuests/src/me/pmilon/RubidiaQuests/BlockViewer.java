package me.pmilon.RubidiaQuests;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Particle;
import org.bukkit.block.Block;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

public class BlockViewer {

	public static HashMap<String, Integer> blockViewers = new HashMap<String, Integer>();
	
	public static void show(RPlayer rp, final List<Block> blocks) {
		BlockViewer.cancel(rp);
		BlockViewer.blockViewers.put(rp.getUniqueId(),
				new BukkitTask(QuestsPlugin.instance) {

					@Override
					public void run() {
						if(rp.isOnline()) {
							for(Block block : blocks) {
								if(block.getLocation().distanceSquared(rp.getPlayer().getLocation()) < 32*32) {
									rp.getPlayer().spawnParticle(Particle.CLOUD, block.getLocation().add(.5, .5, .5), 1, 0, 0, 0, 0);
								}
							}
						}
					}

					@Override
					public void onCancel() {
					}
			
		}.runTaskTimer(0, 20).getTaskId());
	}
	
	public static void cancel(RPlayer rp) {
		if(BlockViewer.blockViewers.containsKey(rp.getUniqueId())) {
			BukkitTask task = BukkitTask.tasks.get(BlockViewer.blockViewers.get(rp.getUniqueId()));
			if(task != null) {
				task.cancel();
			}
			BlockViewer.blockViewers.remove(rp.getUniqueId());
		}
	}
	
	public static boolean isViewing(RPlayer rp) {
		return BlockViewer.blockViewers.containsKey(rp.getUniqueId());
	}
	
}
