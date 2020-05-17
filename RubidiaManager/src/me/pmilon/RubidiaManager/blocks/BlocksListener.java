package me.pmilon.RubidiaManager.blocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.pmilon.RubidiaManager.RubidiaManagerPlugin;
import me.pmilon.RubidiaManager.chunks.Chunk;
import me.pmilon.RubidiaManager.chunks.ChunkColl;
import me.pmilon.RubidiaManager.chunks.ChunkManager;
import me.pmilon.RubidiaManager.chunks.RChunk;
import me.pmilon.RubidiaManager.events.PlayerChunkEnterEvent;

public class BlocksListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Block block = e.getBlock();
		if(block != null){
			Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(block.getChunk());
			if(chunk == null){
				chunk = new RChunk(block.getWorld(), block.getChunk().getX(), block.getChunk().getZ(), true);
				ChunkColl.chunks.add(chunk);
			}

			if(chunk instanceof RChunk){
				RChunk rchunk = (RChunk)chunk;
				
				if(rchunk.isRegenerated()){
					rchunk.setRegenerated(false);
					
					ChunkManager manager = ChunkManager.getManager(chunk);
					if(!manager.isSaved()){
						manager.saveToFile();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		Block block = e.getBlock();
		if(block != null){
			Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(block.getChunk());
			if(chunk == null){
				chunk = new RChunk(block.getWorld(), block.getChunk().getX(), block.getChunk().getZ(), true);
				ChunkColl.chunks.add(chunk);
			}

			if(chunk instanceof RChunk){
				RChunk rchunk = (RChunk)chunk;
				
				if(rchunk.isRegenerated()){
					rchunk.setRegenerated(false);
					
					ChunkManager manager = ChunkManager.getManager(chunk);
					if(!manager.isSaved()){
						manager.saveToFile();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onExplosion(ExplosionPrimeEvent e){
		Entity entity = e.getEntity();
		if(entity != null){
			int range = (int) Math.ceil((double)e.getRadius());
			List<Location> maxRange = new ArrayList<Location>();
			maxRange.add(entity.getLocation());
			maxRange.add(entity.getLocation().add(range,0,0));
			maxRange.add(entity.getLocation().add(0,0,range));
			maxRange.add(entity.getLocation().add(-range,0,0));
			maxRange.add(entity.getLocation().add(0,0,-range));
			maxRange.add(entity.getLocation().add(range,0,range));
			maxRange.add(entity.getLocation().add(range,0,-range));
			maxRange.add(entity.getLocation().add(-range,0,range));
			maxRange.add(entity.getLocation().add(-range,0,-range));
			
			List<Chunk> toSave = new ArrayList<Chunk>();
			
			for(Location location : maxRange){
				Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(location.getChunk());
				if(chunk == null){
					chunk = new RChunk(location.getWorld(), location.getChunk().getX(), location.getChunk().getZ(), true);
					ChunkColl.chunks.add(chunk);
				}
				toSave.add(chunk);
			}
			
			for(Chunk chunk : toSave){
				if(chunk instanceof RChunk){
					RChunk rchunk = (RChunk)chunk;
					
					if(rchunk.isRegenerated()){
						rchunk.setRegenerated(false);
						
						ChunkManager manager = ChunkManager.getManager(chunk);
						if(!manager.isSaved()){
							manager.saveToFile();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockChange(EntityChangeBlockEvent e){
		Block block = e.getBlock();
		if(block != null){
			Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(block.getChunk());
			if(chunk == null){
				chunk = new RChunk(block.getWorld(), block.getChunk().getX(), block.getChunk().getZ(), true);
				ChunkColl.chunks.add(chunk);
			}

			if(chunk instanceof RChunk){
				RChunk rchunk = (RChunk)chunk;
				
				if(rchunk.isRegenerated()){
					rchunk.setRegenerated(false);
					
					ChunkManager manager = ChunkManager.getManager(chunk);
					if(!manager.isSaved()){
						manager.saveToFile();
					}
				}
			}
		}
	}

	@EventHandler
	private void onMove(PlayerMoveEvent e){
		Player player = e.getPlayer();
		if(player != null){
			if(!e.getFrom().getWorld().equals(e.getTo().getWorld()) || !e.getFrom().getChunk().equals(e.getTo().getChunk())){
				PlayerChunkEnterEvent event = new PlayerChunkEnterEvent(player, e.getFrom().getChunk(), e.getTo().getChunk());
				Bukkit.getPluginManager().callEvent(event);
				if(event.isCancelled())e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	private void onChunkEnter(PlayerChunkEnterEvent e){
		List<Chunk> toSave = new ArrayList<Chunk>();
		
		Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(e.getTo());
		if(chunk == null){
			chunk = new RChunk(e.getTo().getWorld(), e.getTo().getX(), e.getTo().getZ(), true);
			ChunkColl.chunks.add(chunk);
		}
		toSave.add(chunk);
		
		for(org.bukkit.Chunk rawChunk : chunk.getRawAdjacents()){
			Chunk rchunk = RubidiaManagerPlugin.getChunkColl().get(rawChunk);
			if(rchunk == null){
				rchunk = new RChunk(rawChunk.getWorld(), rawChunk.getX(), rawChunk.getZ(), true);
				ChunkColl.chunks.add(rchunk);
			}
			toSave.add(chunk);
		}
		
		for(Chunk chunkToSave : toSave){
			if(chunkToSave instanceof RChunk){
				RChunk rchunk = (RChunk)chunkToSave;
				
				if(rchunk.isRegenerated()){
					ChunkManager manager = ChunkManager.getManager(chunkToSave);
					if(!manager.isSaved()){
						manager.saveToFile();
					}
				}
			}
		}
	}

}
