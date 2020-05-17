package me.pmilon.RubidiaManager.chunks;

import org.bukkit.World;

public class RChunk extends Chunk {

	private boolean regenerated;
	public RChunk(World world, int x, int z, boolean regenerated) {
		super(world, x, z);
		this.regenerated = regenerated;
	}
	
	public boolean isRegenerated() {
		return regenerated;
	}
	
	public void setRegenerated(boolean regenerated) {
		this.regenerated = regenerated;
	}

}
