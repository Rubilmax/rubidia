package me.pmilon.RubidiaMonsters.events;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaMonsters.regions.Monster;

public class MonsterKillEvent extends MonsterEvent {

	private Player killer;
	private final HashMap<String, Double> killers;
	public MonsterKillEvent(Monster monster, Player killer, HashMap<String, Double> killers) {
		super(monster);
		this.killer = killer;
		this.killers = killers;
	}
	
	public HashMap<String, Double> getKillers() {
		return killers;
	}

	public Player getKiller() {
		return killer;
	}

	public void setKiller(Player killer) {
		this.killer = killer;
	}

}
