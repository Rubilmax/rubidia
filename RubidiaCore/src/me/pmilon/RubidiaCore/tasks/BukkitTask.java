package me.pmilon.RubidiaCore.tasks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class BukkitTask implements Runnable {
	
	public static HashMap<Integer, BukkitTask> tasks = new HashMap<Integer, BukkitTask>();
	
	private Plugin plugin;
	private int taskId = -1;
	private int autoCancelTask = -1;
	private boolean cancelled = false;
	
	private long lastDelay = 0L;
	private long lastTimer = 0L;
	public BukkitTask(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public BukkitTask runTaskTimer(long delay, long timer){
		this.lastDelay = delay;
		this.lastTimer = timer;
		this.taskId = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), this, delay, timer).getTaskId();
		tasks.put(this.getTaskId(), this);
		return this;
	}
	
	/*
	 * To allow asynchronous actions OUTSIDE Spigot API
	 * Such as file saving
	 */
	public BukkitTask runTaskTimerAsynchronously(long delay, long timer){
		this.lastDelay = delay;
		this.lastTimer = timer;
		this.taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlugin(), this, delay, timer).getTaskId();
		tasks.put(this.getTaskId(), this);
		return this;
	}

	public BukkitTask runTaskLater(long delay){
		this.lastDelay = delay;
		this.taskId = Bukkit.getScheduler().runTaskLater(this.getPlugin(), this, delay).getTaskId();
		tasks.put(this.getTaskId(), this);
		return this;
	}

	public BukkitTask runTaskTimerCancelling(long delay, long timer, long delayCancel){
		this.taskId = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), this, delay, timer).getTaskId();
		this.autoCancelTask = Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				cancel();
			}
			
		}, delay+delayCancel).getTaskId();
		tasks.put(this.getTaskId(), this);
		return this;
	}
	
	public void cancel(){
		if(!isCancelled()){
			setCancelled(true);
			Bukkit.getScheduler().cancelTask(this.taskId);
			if(this.autoCancelTask != -1)Bukkit.getScheduler().cancelTask(this.autoCancelTask);
			tasks.remove(this.taskId);
			this.onCancel();
		}
	}
	
	public boolean isScheduled(){
		return this.taskId != -1;
	}
	
	public int getTaskId(){
		return this.taskId;
	}
	
	public abstract void onCancel();

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public long getLastDelay() {
		return lastDelay;
	}

	public void setLastDelay(long lastDelay) {
		this.lastDelay = lastDelay;
	}

	public long getLastTimer() {
		return lastTimer;
	}

	public void setLastTimer(long lastTimer) {
		this.lastTimer = lastTimer;
	}
	
}
