package me.pmilon.RubidiaManager.tasks;

import org.bukkit.Bukkit;

import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

public class ServerRestartTask extends AbstractTask {

	public ServerRestartTask(RubidiaManagerPlugin plugin) {
		super(plugin);
	}

	@Override
	public void runTaskSynchronously() {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
	}

}
