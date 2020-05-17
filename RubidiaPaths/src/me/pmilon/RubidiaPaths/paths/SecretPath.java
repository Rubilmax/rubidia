package me.pmilon.RubidiaPaths.paths;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaPaths.SecretPathsManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SecretPath {

	private String name;
	private String title;
	private String subtitle;
	private String targetName;
	
	private Location center, bottom, top;
	public SecretPath(String name, String title, String subtitle, String targetName, Location center, Location bottom, Location top){
		this.name = name;
		this.title = title;
		this.subtitle = subtitle;
		this.targetName = targetName;
		this.center = center;
		this.bottom = bottom;
		this.top = top;
	}
	
	public static SecretPath get(String name){
		return SecretPathColl.get(name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
	public void use(final RPlayer rp){
		if(this.getTargetName() != null){
			if(rp.isOnline()) {
				SecretPath target = SecretPath.get(this.getTargetName());
				if(target != null){
					final Player player = rp.getPlayer();
					if(player.isGliding())player.setGliding(false);
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 255, true, false));
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 255, true, false));
					TeleportHandler.teleport(player, target.getCenter());
					player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 1, .001F);
					final String title = target.getTitle();
					final String subtitle = target.getSubtitle();
					Bukkit.getScheduler().runTaskLater(SecretPathsManager.instance, new Runnable(){
						public void run(){
							if(title != null && subtitle != null){
								if(!title.equals("null") && !subtitle.equals("null")){
									rp.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), 5, 30, 20);
								}
							}
						}
					}, 1);
					
					SecretPathsManager.teleported.add(rp);
				}
			}
		}
	}
	
	public boolean check(Location location){
		return LocationUtils.isInBox(location, this.getBottom(), this.getTop());
	}

	public Location getCenter() {
		return center;
	}

	public void setCenter(Location center) {
		this.center = center;
	}

	public Location getBottom() {
		return bottom;
	}

	public void setBottom(Location bottom) {
		this.bottom = bottom;
	}

	public Location getTop() {
		return top;
	}

	public void setTop(Location top) {
		this.top = top;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
}
