package me.pmilon.RubidiaMonsters.events;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaMonsters.regions.Monster;

public class MonsterKillEvent extends MonsterEvent {

	private Player player;
	public MonsterKillEvent(Monster monster, Player player) {
		super(monster);
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

}
