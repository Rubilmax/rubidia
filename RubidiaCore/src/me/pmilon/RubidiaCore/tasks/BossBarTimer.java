package me.pmilon.RubidiaCore.tasks;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BossBarTimer extends BukkitTask{

	private Player player;
	private BossBar bar;
	private LivingEntity entity;
	public BossBarTimer(Plugin plugin, Player player, BossBar bar, LivingEntity entity) {
		super(plugin);
		this.player = player;
		this.bar = bar;
		this.entity = entity;
	}
	
	@Override
	public void run() {
		this.getBar().removePlayer(this.getPlayer());
		RPlayer.get(this.getPlayer()).barTimers.remove(this.getEntity());
	}
	
	@Override
	public void onCancel() {
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public BossBar getBar() {
		return bar;
	}

	public void setBar(BossBar bar) {
		this.bar = bar;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

}
