package me.pmilon.RubidiaCore.events;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class RXPSource {

	private RXPSourceType type;
	private Block block;
	private LivingEntity monster;
	public RXPSource(RXPSourceType type, Block block, LivingEntity monster){
		this.type = type;
		this.block = block;
		this.monster = monster;
	}
	
	public RXPSourceType getType() {
		return type;
	}
	
	public void setType(RXPSourceType type) {
		this.type = type;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}

	public LivingEntity getMonster() {
		return monster;
	}

	public void setMonster(LivingEntity monster) {
		this.monster = monster;
	}
	
}
