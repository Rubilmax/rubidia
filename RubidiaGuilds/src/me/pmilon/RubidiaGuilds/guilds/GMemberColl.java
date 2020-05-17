package me.pmilon.RubidiaGuilds.guilds;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.utils.Configs;

public class GMemberColl extends Database<String,GMember> {
	
	public GMemberColl(){
		if(Configs.getMembersConfig().contains("gmembers")){
			for(String uuid : Configs.getMembersConfig().getConfigurationSection("gmembers").getKeys(false)){
				String path = "gmembers." + uuid;
				boolean[] canSetHome = new boolean[8];
				boolean[] canHome = new boolean[8];
				for(int i = 0;i < 8;i++){
					canSetHome[i] = Configs.getMembersConfig().getBoolean(path + ".canSetHome." + i);
					canHome[i] = Configs.getMembersConfig().getBoolean(path + ".canHome." + i);
				}
				HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
				for(Permission permission : Permission.values()){
					permissions.put(permission.getPermission(), Configs.getMembersConfig().getBoolean(path + "." + permission.getPermission()));
				}
				GMember member = new GMember(uuid,
						Configs.getMembersConfig().getString(path + ".name"),
						Configs.getMembersConfig().getString(path + ".guild"),
						Configs.getMembersConfig().getInt(path + ".rankId"),
						permissions, canHome, canSetHome);
				this.load(uuid,member);
			}
		}
	}
	
	public GMember get(Player player){
		return this.get(player.getUniqueId().toString());
	}

	public GMember get(RPlayer rp) {
		return this.get(rp.getUniqueId());
	}
	
	public boolean contains(Player player){
		return this.get(player) != null;
	}

	@Override
	protected GMember getDefault(String uuid) {
		return new GMember(uuid, Bukkit.getPlayer(UUID.fromString(uuid)).getName(), Guild.getNone().getUUID(), 6, Permission.getDefault(false), new boolean[8], new boolean[8]);
	}

	@Override
	protected void onSaveEnd(boolean arg0) {
		Configs.saveMembersConfig();
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) GuildsPlugin.console.sendMessage("§a   Saving GMembers...");
	}

	@Override
	protected void save(boolean debug, GMember member) {
		if(member.isModified()){
			member.setModified(false);
			String path = "gmembers." + member.getUniqueId();
			Configs.getMembersConfig().set(path + ".name", member.getName());
			Configs.getMembersConfig().set(path + ".guild", member.getGuildId());
			Configs.getMembersConfig().set(path + ".rankId", member.getRankId());
			for(int i = 0;i < 8;i++){
				Configs.getMembersConfig().set(path + ".canSetHome." + i, member.canSetHome(i));
				Configs.getMembersConfig().set(path + ".canHome." + i, member.canHome(i));
			}
			for(Permission permission : Permission.values()){
				Configs.getMembersConfig().set(path + "." + permission.getPermission(), member.getPermission(permission));
			}
			if(debug)GuildsPlugin.console.sendMessage("§6Saved §e" + member.getName());
		}
	}
	
}
