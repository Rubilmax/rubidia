package me.pmilon.RubidiaManager.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;
import me.pmilon.RubidiaManager.chunks.Chunk;
import me.pmilon.RubidiaManager.chunks.ChunkColl;
import me.pmilon.RubidiaManager.chunks.ChunkManager;
import me.pmilon.RubidiaManager.chunks.NChunk;
import me.pmilon.RubidiaManager.chunks.RChunk;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Region;

public class ChunkCommandExecutor extends HybridAdminCommandExecutor {
	
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label,
			final String[] args) {
		if(sender.isOp()){
			return true;
		}else sender.sendMessage("§cYou really thought you could do this without being operator!");
		return false;
	}
	
	public static void toggleChunk(CommandSender sender, Chunk chunk){
		if(chunk instanceof RChunk){
			Claim claim = Claim.get(chunk.getBukkitChunk());
			if(claim == null || !claim.getGuild().isActive()){
				RubidiaManagerPlugin.getChunkColl().delete(chunk);
				NChunk nchunk = new NChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
				ChunkColl.chunks.add(nchunk);
				
				ChunkManager manager = ChunkManager.getManager(nchunk);
				manager.saveToFile();
				
				sender.sendMessage("§2Chunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §4NoChunk §e| §2Saved §e| §4No Regeneration");
			}else sender.sendMessage("§cYou may not modify a chunk an active guild is in...");
		}else if(chunk instanceof NChunk){
			RubidiaManagerPlugin.getChunkColl().delete(chunk);
			RChunk rchunk = new RChunk(chunk.getWorld(), chunk.getX(), chunk.getZ(), true);
			ChunkColl.chunks.add(rchunk);
			
			ChunkManager manager = ChunkManager.getManager(rchunk);
			manager.saveToFile();
			
			sender.sendMessage("§4NoChunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §2Chunk §e| §2Saved §e| §2Regeneration");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAdminCommand(final CommandSender sender, final String[] args) {
		if(sender instanceof Player){
			sender.sendMessage("§4§lWARNING: §cThis command is broken. Please use /chs");
		}
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("regen")){
				if(args.length > 1){
					World world = Bukkit.getWorld(args[1]);
					if(world != null){
						if(args.length > 2){
							if(args.length > 3){
								int x = Integer.parseInt(args[2]);
								int z = Integer.parseInt(args[3]);
								List<RChunk> toRegen = new ArrayList<RChunk>();
								
								Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(world, x,z);
								if(chunk != null){
									String regen = chunk.isRegenable();
									if(regen.equals("true")){
										sender.sendMessage("§eRegenerating chunk §6" + x + "§e,§6" + z + "§e in world §6" + args[1] + "§e...");
										toRegen.add((RChunk)chunk);
										RubidiaManagerPlugin.regen(toRegen);
										sender.sendMessage("§eChunk §6" + x + "§e,§6" + z + "§e in world §6" + args[1] + " §eregenerated!");
									}else sender.sendMessage("§cCannot regenerate this chunk: " + regen);
								}else sender.sendMessage("§cNo chunk to regenerate.");
							}else sender.sendMessage("§cPlease use /c regen (world) ([x] [z])");
						}else{
							sender.sendMessage("§eRegenerating §6" + args[1] + "§e...");
							List<RChunk> toRegen = RubidiaManagerPlugin.getToRegen(world);
							
							Bukkit.getScheduler().runTaskLater(RubidiaManagerPlugin.instance, new Runnable(){
								public void run(){
									sender.sendMessage("§6" + args[1] + " §eregenerated!");
								}
							}, toRegen.size());
							
							RubidiaManagerPlugin.regen(toRegen);
						}
					}else sender.sendMessage("§cCannot find world with name §4" + args[1]);
				}else{
					if(sender instanceof Player){
						List<RChunk> toRegen = new ArrayList<RChunk>();
						
						Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(((Player)sender).getLocation().getChunk());
						if(chunk != null){
							String regen = chunk.isRegenable();
							if(regen.equals("true")){
								sender.sendMessage("§eRegenerating chunk...");
								toRegen.add((RChunk)chunk);
								RubidiaManagerPlugin.regen(toRegen);
								sender.sendMessage("§eChunk regenerated!");
							}else sender.sendMessage("§cCannot regenerate this chunk: " + regen);
						}else sender.sendMessage("§cNo chunk to regenerate.");
					}else sender.sendMessage("§cYou must be a player to regen the chunk you are in! Use /c regen (world) ([x] [z]) instead.");
				}
			}else if(args[0].equalsIgnoreCase("fregen")){
				if(args.length > 1){
					World world = Bukkit.getWorld(args[1]);
					if(world != null){
						if(args.length > 2){
							if(args.length > 3){
								int x = Integer.parseInt(args[2]);
								int z = Integer.parseInt(args[3]);
								List<RChunk> toRegen = new ArrayList<RChunk>();
								
								Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(world, x,z);
								if(chunk != null){
									sender.sendMessage("§eRegenerating chunk §6" + x + "§e,§6" + z + "§e in world §6" + args[1] + "§e...");
									toRegen.add((RChunk)chunk);
									RubidiaManagerPlugin.regen(toRegen);
									sender.sendMessage("§eChunk §6" + x + "§e,§6" + z + "§e in world §6" + args[1] + " §eregenerated!");
								}else sender.sendMessage("§cNo chunk to regenerate.");
							}else sender.sendMessage("§cPlease use /c fregen (world) ([x] [z])");
						}else{
							sender.sendMessage("§eRegenerating §6" + args[1] + "§e...");
							List<RChunk> toRegen = RubidiaManagerPlugin.getToRegen(world);
							
							Bukkit.getScheduler().runTaskLater(RubidiaManagerPlugin.instance, new Runnable(){
								public void run(){
									sender.sendMessage("§6" + args[1] + " §eregenerated!");
								}
							}, toRegen.size());
							
							RubidiaManagerPlugin.regen(toRegen);
						}
					}else sender.sendMessage("§cCannot find world with name §4" + args[1]);
				}else{
					if(sender instanceof Player){
						List<RChunk> toRegen = new ArrayList<RChunk>();
						
						Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(((Player)sender).getLocation().getChunk());
						if(chunk != null){
							sender.sendMessage("§eRegenerating chunk...");
							toRegen.add((RChunk)chunk);
							RubidiaManagerPlugin.regen(toRegen);
							sender.sendMessage("§eChunk regenerated!");
						}else sender.sendMessage("§cNo chunk to regenerate.");
					}else sender.sendMessage("§cYou must be a player to regen the chunk you are in! Use /c fregen (world) ([x] [z]) instead.");
				}
			}else if(args[0].equalsIgnoreCase("howmany") || args[0].equalsIgnoreCase("h")){
				List<RChunk> rchunks = new ArrayList<RChunk>();
				List<NChunk> nchunks = new ArrayList<NChunk>();
				List<RChunk> nrchunks = new ArrayList<RChunk>();
				for(Chunk chunk : ChunkColl.chunks){
					if(chunk instanceof RChunk){
						RChunk rchunk = (RChunk) chunk;
						rchunks.add(rchunk);
						if(!rchunk.isRegenerated())nrchunks.add(rchunk);
					}else if(chunk instanceof NChunk){
						nchunks.add((NChunk) chunk);
					}
				}
				
				sender.sendMessage("§6" + rchunks.size() + " §eregenerating chunks & §6" + nchunks.size() + " §eNoChunks & §6" + nrchunks.size() + " §enot regenerated chunks");
			}else if(args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("tk")){
				if(sender instanceof Player){
					Player p = (Player)sender;
					World world = Bukkit.getWorld("RubidiaSave");
					if(world != null){
						int x = p.getLocation().getChunk().getX();
						int z = p.getLocation().getChunk().getZ();
						Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(world, x,z);
						if(chunk == null){
							chunk = new RChunk(world, x, z, true);
							ChunkColl.chunks.add(chunk);
						}
						
						ChunkManager manager = ChunkManager.getManager(chunk);
						manager.saveToFile();
						BukkitWorld localWorld = manager.getLocalWorld();
						EditSession session = manager.getEditSession();
						manager.localWorld = new BukkitWorld(p.getWorld());
						manager.editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((com.sk89q.worldedit.world.World) new BukkitWorld(p.getWorld()), -1);
						Chunk to = RubidiaManagerPlugin.getChunkColl().get(p.getWorld(), x,z);
						if(to == null)to = new RChunk(p.getWorld(), x, z, false);
						manager.pasteFromFile();
						manager.localWorld = localWorld;
						manager.editSession = session;
						
						ChunkManager newManager = ChunkManager.getManager(to);
						newManager.saveToFile();
						to.getBukkitChunk().getWorld().setBiome(to.getX(), to.getZ(), chunk.getBukkitChunk().getWorld().getBiome(chunk.getX(), chunk.getZ()));
						
						sender.sendMessage("§eChunk taken!");
					}else sender.sendMessage("§cCan't find world with name §4RubidiaSave");
				}else sender.sendMessage("§cYou must be a player to take the chunk you are in! Use /c regen (world) ([x] [z]) instead.");
			}else if(args[0].equalsIgnoreCase("togglechunk") || args[0].equalsIgnoreCase("t")){
				if(sender instanceof Player){
					Location location = ((Player)sender).getLocation();
					Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(location.getChunk());
					ChunkCommandExecutor.toggleChunk(sender, chunk);
				}else{
					if(args.length > 3){
						World world = Bukkit.getWorld(args[1]);
						if(world != null){
							int x = Integer.parseInt(args[2]);
							int z = Integer.parseInt(args[3]);
							
							Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(world, x,z);
							ChunkCommandExecutor.toggleChunk(sender, chunk);
						}else sender.sendMessage("§cCannot find world with name §4" + args[1]);
					}else sender.sendMessage("§cPlease use /c t [world] [x] [z]");
				}
			}else if(args[0].equalsIgnoreCase("togglechunkregion") || args[0].equalsIgnoreCase("tr")){
				if(sender instanceof Player){
					Player player = (Player)sender;
					LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
					Region region;
					try {
						region = session.getSelection(session.getSelectionWorld());
						Set<BlockVector2> chunks = region.getChunks();
						for(BlockVector2 position : chunks){
							Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(player.getWorld().getChunkAt(position.getBlockX(), position.getBlockZ()));
							ChunkCommandExecutor.toggleChunk(sender, chunk);
						}
					} catch (IncompleteRegionException e) {
						player.sendMessage("§cYou must make a selection first!");
					}
				}else sender.sendMessage("§cOnly players can do dis");
			}else if(args[0].equalsIgnoreCase("nochunkregion") || args[0].equalsIgnoreCase("nr")){
				if(sender instanceof Player){
					Player player = (Player)sender;
					LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
					Region region;
					try {
						region = session.getSelection(session.getSelectionWorld());
						Set<BlockVector2> chunks = region.getChunks();
						for(BlockVector2 position : chunks){
							Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(player.getWorld().getChunkAt(position.getBlockX(), position.getBlockZ()));
							if(chunk instanceof RChunk)ChunkCommandExecutor.toggleChunk(sender, chunk);
						}
					} catch (IncompleteRegionException e) {
						player.sendMessage("§cYou must make a selection first!");
					}
				}else sender.sendMessage("§cOnly players can do dis");
			}else if(args[0].equalsIgnoreCase("rchunkregion") || args[0].equalsIgnoreCase("rr")){
				if(sender instanceof Player){
					Player player = (Player)sender;
					LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
					Region region;
					try {
						region = session.getSelection(session.getSelectionWorld());
						Set<BlockVector2> chunks = region.getChunks();
						for(BlockVector2 position : chunks){
							Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(player.getWorld().getChunkAt(position.getBlockX(), position.getBlockZ()));
							if(chunk instanceof NChunk)ChunkCommandExecutor.toggleChunk(sender, chunk);
						}
					} catch (IncompleteRegionException e) {
						player.sendMessage("§cYou must make a selection first!");
					}
				}else sender.sendMessage("§cOnly players can do dis");
			}else if(args[0].equalsIgnoreCase("info")){
				if(sender instanceof Player){
					Player player = (Player)sender;
					Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(player.getLocation().getChunk());
					if(chunk instanceof RChunk){
						sender.sendMessage("§2Chunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e| §2Saved §e| " + (((RChunk) chunk).isRegenerated() ? "§aRegenerated" : "§cDestroyed") + " §e| §2Regeneration");
					}else if(chunk instanceof NChunk){
						sender.sendMessage("§4NoChunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e| §2Saved §e| §4No Regeneration");
					}
				}else sender.sendMessage("§cOnly players can do dis!");
			}
		}else sender.sendMessage("§cPlease use /c [regen (world) ([x] [z])/t ([world] [x] [z])/h/tr/nr/rr/info]");
	}
	
}
