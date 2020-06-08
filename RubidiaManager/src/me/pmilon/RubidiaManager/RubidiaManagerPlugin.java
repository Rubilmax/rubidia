package me.pmilon.RubidiaManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaManager.chunks.Chunk;
import me.pmilon.RubidiaManager.chunks.ChunkColl;
import me.pmilon.RubidiaManager.chunks.ChunkListener;
import me.pmilon.RubidiaManager.chunks.ChunkManager;
import me.pmilon.RubidiaManager.chunks.RChunk;
import me.pmilon.RubidiaManager.commands.ChunkCommandExecutor;
import me.pmilon.RubidiaManager.commands.ChunksCommandExecutor;
import me.pmilon.RubidiaManager.tasks.EndRegenTask;
import me.pmilon.RubidiaManager.tasks.NetherRegenTask;
import me.pmilon.RubidiaManager.tasks.ServerRestartTask;
import me.pmilon.RubidiaManager.tasks.WorldsRegenTask;
import me.pmilon.RubidiaManager.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;

public class RubidiaManagerPlugin extends JavaPlugin {

	private static WorldRegenHandler regenerator;
	private static ChunkColl chunkColl;
	
	public static MultiverseCore multiverseCore;
	public static RubidiaManagerPlugin instance;
	
	public static File chunksConfigFile;
	public static FileConfiguration chunksConfig;
	
	public static final int TICKS_PER_CHUNK_REGEN = 4;
	
	public void onEnable(){
		instance = this;
		multiverseCore = (MultiverseCore) this.getServer().getPluginManager().getPlugin("Multiverse-Core");
		
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		Configs.saveDefaultChunksConfig();
		
		regenerator = new WorldRegenHandler(this);
		chunkColl = new ChunkColl();
		
		Bukkit.getPluginManager().registerEvents(new ChunkListener(), this);
		
		this.getCommand("chunk").setExecutor(new ChunkCommandExecutor());
		this.getCommand("chunks").setExecutor(new ChunksCommandExecutor());
		
		if(this.getConfig().contains("autoRestart")) {
			int days = this.getConfig().getInt("autoRestart");
			if(days > 0) {
				this.scheduleRestart(days);
			}
		}
		this.scheduleNetherRegen();
		this.scheduleEndRegen();
		this.scheduleWorldsRegen();
	}
	
	public void onDisable(){
		RubidiaManagerPlugin.getChunkColl().save();
		this.saveConfig();
	}
	
	public static List<RChunk> getToRegen(World world){
		List<RChunk> toRegen = new ArrayList<RChunk>();
		for(Chunk chunk : ChunkColl.chunks){
			if(chunk.getWorld().equals(world) && chunk.isRegenable().equals("true")){
				toRegen.add((RChunk)chunk);
			}
		}
		return toRegen;
	}
	
	public static int regen(List<RChunk> toRegen){
		int id = 0;
		for(final RChunk rchunk : toRegen){
			final ChunkManager manager = ChunkManager.getManager(rchunk);
			if(manager.isSaved()){
				final int index = id;
				Bukkit.getScheduler().runTaskLater(RubidiaManagerPlugin.instance, new Runnable(){
					public void run(){
						manager.pasteFromFile();
						Bukkit.getConsoleSender().sendMessage("§6Regenerated §echunk #" + index);
					}
				}, id*RubidiaManagerPlugin.TICKS_PER_CHUNK_REGEN);
				id++;
			}
		}
		return id*RubidiaManagerPlugin.TICKS_PER_CHUNK_REGEN;
	}
	
	public static WorldRegenHandler getRegenerator(){
		return regenerator;
	}

	public static ChunkColl getChunkColl() {
		return chunkColl;
	}
	
	public static File getSavesFolder(World world){
		File dir = new File(RubidiaManagerPlugin.instance.getDataFolder().getAbsolutePath() + "/saves/" + world.getName());
		if(!dir.exists())dir.mkdirs();
		return dir;
	}
	
	public void scheduleRestart(int days){
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 2);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		if(date.before(Calendar.getInstance())) {
			date.add(Calendar.DAY_OF_YEAR, days);
		} else {
			date.add(Calendar.DAY_OF_YEAR, days - 1);
		}
		new Timer("RubidiaRestart").schedule(new ServerRestartTask(this), date.getTime());
		long time = date.getTime().getTime() - System.currentTimeMillis();
		long daysToDate = TimeUnit.MILLISECONDS.toDays(time);
		time -= TimeUnit.DAYS.toMillis(daysToDate);
		long hours = TimeUnit.MILLISECONDS.toHours(time);
		time -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		time -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		time -= TimeUnit.SECONDS.toMillis(seconds);
		String.format("%02d:%02d:%02d", hours, minutes, seconds);
		Bukkit.getConsoleSender().sendMessage("§6Next §erestart §6in §e" + String.format("%02d days, %02d:%02d:%02d", daysToDate, hours, minutes, seconds));
	}
	
	private void scheduleEndRegen(){
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 3);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		if(date.before(Calendar.getInstance())) {
			date.add(Calendar.DAY_OF_YEAR, 1);
		}
		new Timer("RubidiaEndRegen").scheduleAtFixedRate(new EndRegenTask(this), date.getTime(), 24*60*60*1000L);
		long time = date.getTime().getTime() - System.currentTimeMillis();
		long days = TimeUnit.MILLISECONDS.toDays(time);
		time -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(time);
		time -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		time -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		time -= TimeUnit.SECONDS.toMillis(seconds);
		String.format("%02d:%02d:%02d", hours, minutes, seconds);
		Bukkit.getConsoleSender().sendMessage("§6Next §eEnd regeneration §6in §e" + String.format("%02d days, %02d:%02d:%02d", days, hours, minutes, seconds));
	}
	
	private void scheduleNetherRegen(){
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		date.set(Calendar.HOUR_OF_DAY, 5);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		if(date.before(Calendar.getInstance())) {
			date.add(Calendar.WEEK_OF_YEAR, 1);
		}
		new Timer("RubidiaNetherRegen").scheduleAtFixedRate(new NetherRegenTask(this), date.getTime(), 7*24*60*60*1000L);
		long time = date.getTime().getTime() - System.currentTimeMillis();
		long days = TimeUnit.MILLISECONDS.toDays(time);
		time -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(time);
		time -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		time -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		time -= TimeUnit.SECONDS.toMillis(seconds);
		String.format("%02d:%02d:%02d", hours, minutes, seconds);
		Bukkit.getConsoleSender().sendMessage("§6Next §eNether regeneration §6in §e" + String.format("%02d days, %02d:%02d:%02d", days, hours, minutes, seconds));
	}
	
	private void scheduleWorldsRegen(){
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_MONTH, 1);
		date.set(Calendar.HOUR_OF_DAY, 4);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		if(date.before(Calendar.getInstance())) {
			date.add(Calendar.MONTH, 1);
		}
		new Timer("RubidiaWorldsRegen").scheduleAtFixedRate(new WorldsRegenTask(this), date.getTime(), 31*24*60*60*1000L);
		long time = date.getTime().getTime() - System.currentTimeMillis();
		long days = TimeUnit.MILLISECONDS.toDays(time);
		time -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(time);
		time -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		time -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		time -= TimeUnit.SECONDS.toMillis(seconds);
		String.format("%02d:%02d:%02d", hours, minutes, seconds);
		Bukkit.getConsoleSender().sendMessage("§6Next §eRubidia regeneration §6in §e" + String.format("%02d days, %02d:%02d:%02d", days, hours, minutes, seconds));
	}
}
