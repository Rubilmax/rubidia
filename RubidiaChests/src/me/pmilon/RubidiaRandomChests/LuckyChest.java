package me.pmilon.RubidiaRandomChests;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaCore.utils.Sounds;
import me.pmilon.RubidiaCore.utils.VectorUtils;
import net.minecraft.server.v1_13_R2.*;

public class LuckyChest {

	private Location location;
	private BlockFace orientation;
	private boolean permanent;
	
	private BukkitTask spawnTask;
	private BukkitTask particleTask;
	private boolean active = false;
	public LuckyChest(Location location, BlockFace orientation, boolean permanent) {
		this.location = location;
		this.orientation = orientation;
		this.permanent = permanent;
		this.handleSpawn();
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public BlockFace getOrientation() {
		return orientation;
	}
	
	public void setOrientation(BlockFace orientation) {
		this.orientation = orientation;
	}
	
	public void handleSpawn() {
		if(this.isPermanent()) {
			this.getLocation().getBlock().setType(Material.AIR);
			this.spawnTask = new BukkitTask(RandomChestsPlugin.getInstance()) {

				@Override
				public void run() {
					spawn();
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(this.getNextDelay());
		}
	}
	
	public void spawn() {
		this.getLocation().getBlock().setType(Material.TRAPPED_CHEST);
		final Chest chest = (Chest) this.getLocation().getBlock().getState();
		Directional directional = (Directional) chest.getBlockData();
		directional.setFacing(this.getOrientation());
		chest.setBlockData(directional);
		chest.update();
	    World nmsWorld = ((CraftWorld) chest.getWorld()).getHandle();
	    TileEntityChest teC = (TileEntityChest) nmsWorld.getTileEntity(new BlockPosition(chest.getX(), chest.getY(), chest.getZ()));
	    teC.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"Coffre chance\"}"));

		for(int i = 0;i < chest.getBlockInventory().getSize();i++){
			if(RandomUtils.random.nextInt(100) < 45){//45% to have an item INSIDE WHICH:
				if(RandomUtils.random.nextInt(100) < 9){//9% to have loot -- 91% to have else
					int r = RandomUtils.random.nextInt(4);
					chest.getBlockInventory().setItem(i, new ItemStack(r==0 ? Material.ROTTEN_FLESH : r == 1 ?Material.STRING : r == 2 ? Material.BONE : Material.GUNPOWDER, RandomUtils.random.nextInt(4)+1));
				}else if(RandomUtils.random.nextInt(100) < 7){
					chest.getBlockInventory().setItem(i, new ItemStack(Material.EMERALD, RandomUtils.random.nextInt(6)+1));
				}else if(RandomUtils.random.nextInt(100) < 5){
					int r = RandomUtils.random.nextInt(4);
					chest.getBlockInventory().setItem(i, new ItemStack(r==0 ? Material.APPLE : r == 1 ? Material.MUSHROOM_STEW : r == 2 ? Material.COOKIE : Material.PUMPKIN_PIE, RandomUtils.random.nextInt(4)+1));
				}else if(RandomUtils.random.nextInt(100) < 5){
					int r = RandomUtils.random.nextInt(7);
					chest.getBlockInventory().setItem(i, new ItemStack(r==0 ? Material.COOKED_CHICKEN : r == 1 ? Material.COOKED_BEEF : r == 2 ? Material.COOKED_SALMON : r == 3 ? Material.COOKED_MUTTON : r == 4 ? Material.BEEF : r == 5 ? Material.CHICKEN : r == 6 ? Material.SALMON : Material.COOKED_MUTTON, RandomUtils.random.nextInt(4)+1));
				}else if(RandomUtils.random.nextInt(1000) < 3){
					chest.getBlockInventory().setItem(i, new ItemStack(Material.CARROT_ON_A_STICK, 1));
				}else if(RandomUtils.random.nextInt(1000) < 2){
					chest.getBlockInventory().setItem(i, new ItemStack(Material.GOLDEN_APPLE, 1));
				}
			}
		}

		this.setActive(true);
		this.particleTask = new BukkitTask(RandomChestsPlugin.getInstance()){
			
			@Override
			public void run(){
				if(isActive()){
					chest.getWorld().spawnParticle(Particle.CRIT, LocationUtils.getCenter(chest.getLocation()).add(0,.5,0), 25, .15, .15, .15, .5);
				}else this.cancel();
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0,20);
	}
	
	public void despawn(boolean handleRespawn) {
		if(this.isActive()) {
			this.setActive(false);
			Block block = this.getLocation().getBlock();
			if(block.getType().equals(Material.TRAPPED_CHEST)){
				((Chest)block.getState()).getBlockInventory().clear();
				block.setType(Material.AIR);
			}
			this.particleTask.cancel();
			this.getLocation().getWorld().playSound(this.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
			if(handleRespawn && this.spawnTask != null) {
				this.spawnTask.runTaskLater(this.getNextDelay());
			}
		}
	}
	
	public int getNextDelay() {
		return RandomUtils.random.nextInt(5*60*20)+5*60*20;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public BukkitTask getSpawnTask() {
		return spawnTask;
	}
	
	public void open(Player player) {
		Sounds.playFoundTreasure(player);
		final Location origin = LocationUtils.getCenter(this.getLocation());
		final Vector v = new Vector(.6,0,0);
		final Vector v2 = new Vector(-.6,0,0);
		for(int i = 0;i < 60;i++){
			Bukkit.getScheduler().runTaskLater(RandomChestsPlugin.getInstance(), new Runnable(){
				public void run(){
					VectorUtils.rotateAroundAxisY(v, .2);
					VectorUtils.rotateAroundAxisY(v2, .2);
					v.add(new Vector(0,.1,0));
					v2.add(new Vector(0,.1,0));
					origin.getWorld().spawnParticle(Particle.NOTE, origin.clone().add(v), 0, 0, 0, 0);
					origin.getWorld().spawnParticle(Particle.NOTE, origin.clone().add(v2), 0, 0, 0, 0);
				}
			}, i/2);
		}
		Bukkit.getScheduler().runTaskLater(RandomChestsPlugin.getInstance(), new Runnable(){
			public void run(){
				Firework f = (Firework) origin.getWorld().spawn(origin.clone(), Firework.class);
				FireworkMeta fm = f.getFireworkMeta();
				fm.addEffect(FireworkEffect.builder()
						.flicker(false)
						.trail(true)
						.withColor(Color.AQUA)
						.withColor(Color.YELLOW)
						.withFade(Color.AQUA)
						.withFade(Color.YELLOW)
						.withFade(Color.WHITE)
						.build());
				fm.setPower(0);
				f.setFireworkMeta(fm);
			}
		}, 12);
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}
	
}
