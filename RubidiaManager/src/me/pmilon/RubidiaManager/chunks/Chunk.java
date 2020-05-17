package me.pmilon.RubidiaManager.chunks;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaWG.Flags;
import me.pmilon.RubidiaWG.WGUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public abstract class Chunk {

	private World world;
	private int x;
	private int z;
	
	private Location origin;
	private Location destination;
	
	public Chunk(World world, int x, int z){
		this.world = world;
		this.x = x;
		this.z = z;
		
		this.origin = new Location(world, x*16, 0, z*16);
		this.destination = new Location(world, x*16+15, 255, z*16+15);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public String getSaveName(){
		return this.getWorld().getName() + "," + String.valueOf(this.getX()) + "," + String.valueOf(this.getZ());
	}
	
	public org.bukkit.Chunk getBukkitChunk(){
		return Bukkit.getWorld(this.getWorld().getName()).getChunkAt(this.getX(), this.getZ());
	}
	
	public List<org.bukkit.Chunk> getRawAdjacents(){
		List<org.bukkit.Chunk> chunks = new ArrayList<org.bukkit.Chunk>();
		for(int x = -1;x <= 1;x++){
			for(int z = -1;z <= 1;z++){
				if(x != 0 || z != 0)chunks.add(this.getWorld().getChunkAt(this.getX()+x, this.getZ()+z));
			}
		}
		return chunks;
	}

	public String isRegenable(){
		if(this instanceof RChunk){
			if(!((RChunk)this).isRegenerated()){
				List<org.bukkit.Chunk> chunks = this.getRawAdjacents();
				chunks.add(this.getBukkitChunk());
				
				for(org.bukkit.Chunk chk : chunks){
					Claim temp = Claim.get(chk);
					if(temp != null){
						if(temp.getGuild().isActive()){
							return "Relative chunk " + chk.getX() + " " + chk.getZ() + " is claimed!";
						}
					}
				}
				
				if(WGUtils.testState(null, this.getWorld(), new Vector(this.getOrigin().getX(),this.getOrigin().getY(),this.getOrigin().getZ()), new Vector(this.getDestination().getX(),this.getDestination().getY(),this.getDestination().getZ()), Flags.REGEN)){
					return "true";
				}else return "Unregenable region inside";
			}else return "Chunk already regenerated";
		}
		return "Chunk saved as NoChunk";
	}
}
