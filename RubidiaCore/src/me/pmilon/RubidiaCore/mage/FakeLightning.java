package me.pmilon.RubidiaCore.mage;

import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerSpawnEntityWeather;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaCore.utils.RandomUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FakeLightning {

	private boolean realFire;
	public FakeLightning(boolean realFire){
		this.realFire = realFire;
	}
	
	public boolean isRealFire() {
		return realFire;
	}
	
	public void setRealFire(boolean realFire) {
		this.realFire = realFire;
	}
	
	public void strike(final Location location, List<Entity> entities){
		WrapperPlayServerSpawnEntityWeather packet = new WrapperPlayServerSpawnEntityWeather();
		packet.setEntityID(RandomUtils.random.nextInt(200));
		packet.setType(1);
		packet.setX(location.getBlockX());
		packet.setY(location.getBlockY());
		packet.setZ(location.getBlockZ());
		packet.sendPacket(entities);
		
		location.getWorld().playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
		if(this.isRealFire())location.getBlock().setType(Material.FIRE);
		else {
			for(final Player pp : Core.toPlayerList(LocationUtils.getNearbyEntities(location, 64))) {
				final BlockData initialData = location.getBlock().getBlockData();
				Fire fire = (Fire) Bukkit.getServer().createBlockData(Material.FIRE);
				pp.sendBlockChange(location, fire);
				new BukkitTask(Core.instance) {

					@Override
					public void run() {
						pp.sendBlockChange(location, initialData);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(35);
			}
		}
	}
	
	public void strike(Entity entity){
		strike(entity.getLocation(), entity.getNearbyEntities(64, 64, 64));
	}
	
	public void strike(Location location){
		strike(location, LocationUtils.getNearbyEntities(location, 64));
	}
}
