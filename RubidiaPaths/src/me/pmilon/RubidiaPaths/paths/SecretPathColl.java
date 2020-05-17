package me.pmilon.RubidiaPaths.paths;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaPaths.SecretPathsManager;

public class SecretPathColl {
	
	public static final List<SecretPath> paths = new ArrayList<SecretPath>();

	private final SecretPathsManager plugin;
	public SecretPathColl(SecretPathsManager plugin){
		this.plugin = plugin;
		if(Configs.getPathConfig().contains("portals")){
			for(String name : Configs.getPathConfig().getConfigurationSection("portals").getKeys(false)){
				String path = "portals." + name;
				String title = Configs.getPathConfig().getString(path + ".title");
				String subtitle = Configs.getPathConfig().getString(path + ".subtitle");
				Location bottom = (Location) Configs.getPathConfig().get(path + ".bottom");
				Location top = (Location) Configs.getPathConfig().get(path + ".top");
				Location center = (Location) Configs.getPathConfig().get(path + ".center");
				String targetName = Configs.getPathConfig().getString(path + ".targetName");
				paths.add(new SecretPath(name, title, subtitle, targetName, center, bottom, top));
			}
		}
	}
	
	public SecretPathsManager getPlugin() {
		return plugin;
	}
	
	public static SecretPath get(String name){
		for(SecretPath path : paths){
			if(path.getName().equals(name)){
				return path;
			}
		}
		return null;
	}
	
	public void save(){
		for(SecretPath secretPath : paths){
			String path = "portals." + secretPath.getName();
			Configs.getPathConfig().set(path + ".title", secretPath.getTitle());
			Configs.getPathConfig().set(path + ".subtitle", secretPath.getSubtitle());
			Configs.getPathConfig().set(path + ".targetName", secretPath.getTargetName());
			Configs.getPathConfig().set(path + ".bottom", secretPath.getBottom());
			Configs.getPathConfig().set(path + ".top", secretPath.getTop());
			Configs.getPathConfig().set(path + ".center", secretPath.getCenter());
		}
		Configs.savePathConfig();
	}
	
}
