package me.pmilon.RubidiaGuilds.claims;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.utils.Configs;

public class ClaimColl extends Database<String,Claim> {
	
	public ClaimColl(){
		if(Configs.getClaimsConfig().contains("claims")){
			for(String claimUUID : Configs.getClaimsConfig().getConfigurationSection("claims").getKeys(false)){
				String path = "claims." + claimUUID;
				String uuid = Configs.getClaimsConfig().getString(path + ".guildUUID");
				if(uuid != null){
					if(!uuid.isEmpty()){
						if(Configs.getGuildConfig().contains("guilds." + uuid)){
							Claim claim = new Claim(claimUUID,
									Configs.getClaimsConfig().getString(path + ".name"),
									Bukkit.getWorld(Configs.getClaimsConfig().getString(path + ".world")),
									Configs.getClaimsConfig().getInt(path + ".x"),
									Configs.getClaimsConfig().getInt(path + ".z"),
									Configs.getClaimsConfig().getString(path + ".guildUUID"));
							this.load(claimUUID,claim);
							if(this.size() % 100 == 0){
								GuildsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §6CLAIMS");
							}
						}else Configs.getClaimsConfig().set(path, null);
					}
				}
			}
		}
		GuildsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §6CLAIMS");
	}
	
	public Claim get(Location location){
		return this.get(location.getChunk());
	}
	
	public Claim get(Chunk chunk){
		for(Claim claim : this.data()){
			if(chunk.getX() == claim.getX() && chunk.getZ() == claim.getZ() && chunk.getWorld().equals(claim.getWorld())){
				return claim;
			}
		}
		return null;
	}

	@Override
	protected Claim getDefault(String arg0) {
		return null;
	}

	@Override
	protected void onSaveEnd(boolean arg0) {
		Configs.saveClaimsConfig();
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) GuildsPlugin.console.sendMessage("§a   Saving Claims...");
	}

	@Override
	protected void save(boolean debug, Claim claim) {
		if(claim.isModified()){
			claim.setModified(false);
			String path = "claims." + claim.getUUID();
			Configs.getClaimsConfig().set(path + ".name", claim.getName());
			Configs.getClaimsConfig().set(path + ".world", claim.getWorld().getName());
			Configs.getClaimsConfig().set(path + ".x", claim.getX());
			Configs.getClaimsConfig().set(path + ".z", claim.getZ());
			if(claim.getGuild() != null)Configs.getClaimsConfig().set(path + ".guildUUID", claim.getGuildUUID().toString());
			if(debug)GuildsPlugin.console.sendMessage("§6Saved §e" + claim.getUUID());
		}
	}
	
}
