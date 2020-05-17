package me.pmilon.RubidiaManager.tasks;

import java.util.TimerTask;

import org.bukkit.Bukkit;

import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

public abstract class AbstractTask extends TimerTask{

	private final RubidiaManagerPlugin plugin;
	public AbstractTask(RubidiaManagerPlugin plugin){
		this.plugin = plugin;
	}
	
	public RubidiaManagerPlugin getPlugin() {
		return plugin;
	}
	
	@Override
	public void run(){
		Bukkit.getScheduler().runTaskLater(this.getPlugin(), new Runnable(){
			public void run(){
				runTaskSynchronously();
			}
		},0);
	}
	
	public abstract void runTaskSynchronously();

}
