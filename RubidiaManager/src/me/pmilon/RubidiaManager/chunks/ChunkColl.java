package me.pmilon.RubidiaManager.chunks;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaManager.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class ChunkColl {

	public static final List<Chunk> chunks = new ArrayList<Chunk>();
	
	public ChunkColl(){
		if(Configs.getChunksConfig().contains("worlds")){
			List<String> toDelete = new ArrayList<String>();
			
			for(String world : Configs.getChunksConfig().getConfigurationSection("worlds").getKeys(false)){
				if(Configs.getChunksConfig().contains("worlds." + world + ".chunks")){
					for(String chunk : Configs.getChunksConfig().getConfigurationSection("worlds." + world + ".chunks").getKeys(false)){
						if(chunk.split(",").length > 2){
							World bworld = Bukkit.getWorld(world);
							if(bworld != null){
								int x = Integer.parseInt(chunk.split(",")[1]);
								int z = Integer.parseInt(chunk.split(",")[2]);
								chunks.add(new RChunk(bworld, x, z, Configs.getChunksConfig().getBoolean("worlds." + world + ".chunks." + chunk + ".regenerated")));
							}else{
								if(!toDelete.contains(world))toDelete.add(world);
							}
						}else Configs.getChunksConfig().set("worlds." + world + ".chunks." + chunk, null);
					}
				}
				
				if(Configs.getChunksConfig().getConfigurationSection("worlds." + world + ".nochunks") != null){
					for(String chunk : Configs.getChunksConfig().getConfigurationSection("worlds." + world + ".nochunks").getKeys(false)){
						if(chunk.split(",").length > 2){
							World bworld = Bukkit.getWorld(world);
							if(bworld != null){
								int x = Integer.parseInt(chunk.split(",")[1]);
								int z = Integer.parseInt(chunk.split(",")[2]);
								chunks.add(new NChunk(bworld, x, z));
							}else{
								if(!toDelete.contains(world))toDelete.add(world);
							}
						}else Configs.getChunksConfig().set("worlds." + world + ".nochunks." + chunk, null);
					}
				}
			}
			
			for(String world : toDelete){
				Configs.getChunksConfig().set("worlds." + world, null);
			}
			Configs.saveChunksConfig();
		}
	}
	
	public Chunk get(org.bukkit.Chunk chk){
		return this.get(chk.getWorld(), chk.getX(), chk.getZ());
	}
	
	public Chunk get(World world, int x, int z){
		for(Chunk chunk : chunks){
			if(chunk.getWorld().equals(world)
					&& chunk.getX() == x
					&& chunk.getZ() == z)return chunk;
		}
		return null;
	}
	
	public void delete(Chunk chunk){
		chunks.remove(chunk);
		if(chunk instanceof RChunk){
			Configs.getChunksConfig().set("worlds." + chunk.getWorld().getName() + ".chunks." + chunk.getSaveName(), null);
		}else if(chunk instanceof NChunk){
			Configs.getChunksConfig().set("worlds." + chunk.getWorld().getName() + ".nochunks." + chunk.getSaveName(), null);
		}
	}
	
	public void save(){
		for(Chunk chunk : chunks){
			if(chunk instanceof RChunk){
				RChunk rchunk = (RChunk)chunk;
				Configs.getChunksConfig().set("worlds." + chunk.getWorld().getName() + ".chunks." + chunk.getSaveName() + ".regenerated", rchunk.isRegenerated());
			}else if(chunk instanceof NChunk){
				Configs.getChunksConfig().set("worlds." + chunk.getWorld().getName() + ".nochunks." + chunk.getSaveName(), true);
			}
		}
		Configs.saveChunksConfig();
	}
}
