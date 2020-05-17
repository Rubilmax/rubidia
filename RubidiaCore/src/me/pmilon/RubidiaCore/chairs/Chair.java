package me.pmilon.RubidiaCore.chairs;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.events.DelayedPlayerUnsitEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class Chair {

	private Block block;
	private List<Player> players = new ArrayList<Player>();
	private List<ArmorStand> chairStands = new ArrayList<ArmorStand>();
	private List<Location> sitLocations = new ArrayList<Location>();
	private Core plugin;
	private List<Integer> taskIds = new ArrayList<Integer>();;
	public Chair(Core plugin, Block block){
		this.plugin = plugin;
		this.block = block;
	}
	
	public void sitPlayer(final Player player){
		if(this.isSittable()){
			Stairs stairs = (Stairs) this.getBlock().getBlockData();
			final Location sitLocation = this.getBlock().getLocation().clone().add(.5,.3,.5);
			final ArmorStand stand = this.getBlock().getWorld().spawn(sitLocation, ArmorStand.class);
			stand.setMarker(true);
			stand.setVisible(false);
			stand.setGravity(false);
			if(stairs.getFacing().equals(BlockFace.WEST)){
				sitLocation.setYaw(90);
				sitLocation.add(-.09,0,0);
				if(this.getPlayers().size() > 0){
					this.getSitLocations().get(0).add(0,0,-.35);
					sitLocation.add(0,0,.35);
				}
			}else if(stairs.getFacing().equals(BlockFace.NORTH)){
				sitLocation.setYaw(180);
				sitLocation.add(0,0,-.09);
				if(this.getPlayers().size() > 0){
					this.getSitLocations().get(0).add(-.35,0,0);
					sitLocation.add(.35,0,0);
				}
			}else if(stairs.getFacing().equals(BlockFace.EAST)){
				sitLocation.setYaw(-90);
				sitLocation.add(.09,0,0);
				if(this.getPlayers().size() > 0){
					this.getSitLocations().get(0).add(0,0,.35);
					sitLocation.add(0,0,-.35);
				}
			}else if(stairs.getFacing().equals(BlockFace.SOUTH)){
				sitLocation.add(0,0,.09);
				if(this.getPlayers().size() > 0){
					this.getSitLocations().get(0).add(.35,0,0);
					sitLocation.add(-.35,0,0);
				}
			}
			if(this.getPlayers().size() > 0){
				this.stopTask(0);
				final ArmorStand stand1 = this.getChairStands().get(0);
				final Player player1 = this.getPlayers().get(0);
				player1.leaveVehicle();
				stand1.teleport(getSitLocations().get(0));
				stand1.addPassenger(player1);
				this.startTask(player1, stand1);
			}
			new BukkitTask(this.getPlugin()){
				public void run(){
					stand.teleport(sitLocation);
					stand.addPassenger(player);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(2);
			this.getPlayers().add(player);
			this.getChairStands().add(stand);
			this.getSitLocations().add(sitLocation);
			this.startTask(player, stand);
		}
	}

	public void startTask(final Player player, final ArmorStand stand){
		final Chair instance = this;
		this.taskIds.add(Bukkit.getScheduler().runTaskTimer(this.getPlugin(), new Runnable(){
			public void run(){
				if(!getBlock().getType().toString().contains("STAIRS")){
					for(Player player : players){
						unsitPlayer(player);
					}
				}else{
					if(stand.getPassengers().isEmpty()){
						DelayedPlayerUnsitEvent event = new DelayedPlayerUnsitEvent(player, instance);
						Bukkit.getPluginManager().callEvent(event);
					}else{
						double x = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.001 - player.getHealth();
						double y = .25;
						if(y > x)y = x;
						player.setHealth(player.getHealth() + y);
					}
				}
			}
		},20,20).getTaskId());
	}
	
	public void unsitPlayer(Player player){
		int index = this.getPlayers().indexOf(player);
		player.eject();
		this.getPlayers().remove(player);
		this.getChairStands().get(index).remove();
		this.getChairStands().remove(index);
		this.getSitLocations().remove(index);
		this.stopTask(index);
	}
	
	public boolean isSittable(){
		return this.getPlayers().size() < 2 && ChairAPI.isSittable(this.getBlock());
	}
	
	public void stopTask(int index){
		Bukkit.getScheduler().cancelTask(this.taskIds.get(index));
		this.taskIds.remove(index);
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Core getPlugin() {
		return plugin;
	}

	public void setPlugin(Core plugin) {
		this.plugin = plugin;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<ArmorStand> getChairStands() {
		return chairStands;
	}

	public void setChairStands(List<ArmorStand> chairStands) {
		this.chairStands = chairStands;
	}

	public List<Location> getSitLocations() {
		return sitLocations;
	}

	public void setSitLocations(List<Location> sitLocations) {
		this.sitLocations = sitLocations;
	}
}
