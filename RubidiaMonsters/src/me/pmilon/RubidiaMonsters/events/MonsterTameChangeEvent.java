package me.pmilon.RubidiaMonsters.events;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaMonsters.regions.Monster;

public class MonsterTameChangeEvent extends MonsterEvent{

	private Player tamer;
	public MonsterTameChangeEvent(Monster monster, Player tamer) {
		super(monster);
		this.tamer = tamer;
	}
	
	public Player getTamer() {
		return tamer;
	}
	
	public void setTamer(Player tamer) {
		this.tamer = tamer;
	}

}
