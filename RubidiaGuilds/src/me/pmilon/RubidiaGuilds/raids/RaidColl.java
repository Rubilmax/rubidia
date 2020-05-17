package me.pmilon.RubidiaGuilds.raids;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.utils.Configs;

public class RaidColl extends Database<String,Raid> {
	
	public RaidColl(){
		if(Configs.getRaidsConfig().contains("raids")){
			for(String raidUUID : Configs.getRaidsConfig().getConfigurationSection("raids").getKeys(false)){
				String path = "raids." + raidUUID;
				
				Raid raid = new Raid(raidUUID,
						Claim.get(Configs.getRaidsConfig().getString(path + ".claimUUID")),
						(Location)Configs.getRaidsConfig().get(path + ".center"),
						Guild.getByUUID(Configs.getRaidsConfig().getString(path + ".offensiveUUID")),
						Guild.getByUUID(Configs.getRaidsConfig().getString(path + ".defensiveUUID")));
				raid.setPoints(Configs.getRaidsConfig().getInt(path + ".points"));
				raid.setMaxPoints(Configs.getRaidsConfig().getInt(path + ".maxPoints"));
				raid.setEndDate(Configs.getRaidsConfig().getLong(path + ".endDate"));
				raid.setFinished(Configs.getRaidsConfig().getBoolean(path + ".finished"));
				this.load(raidUUID,raid);
				if(this.size() % 100 == 0){
					GuildsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §6RAIDS");
				}
			}
		}
		GuildsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §6RAIDS");
	}
	
	public void startNew(Guild offensive, Location location){
		Raid raid = new Raid(UUID.randomUUID().toString(), Claim.get(location), location, offensive, Claim.get(location).getGuild());
		this.load(raid.getUUID(),raid);
		raid.prestart();
	}
	
	public List<Raid> getOffensiveRaids(Guild offensive){
		List<Raid> raids = new ArrayList<Raid>();
		for(Raid raid : this.data()){
			if(offensive.equals(raid.getOffensive())){
				raids.add(raid);
			}
		}
		return raids;
	}

	public List<Raid> getDefensiveRaids(Guild defensive){
		List<Raid> raids = new ArrayList<Raid>();
		for(Raid raid : this.data()){
			if(defensive.equals(raid.getDefensive())){
				raids.add(raid);
			}
		}
		return raids;
	}

	@Override
	protected Raid getDefault(String arg0) {
		return null;
	}

	@Override
	protected void onSaveEnd(boolean arg0) {
		Configs.saveRaidsConfig();
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) GuildsPlugin.console.sendMessage("§a   Saving Raids...");
	}

	@Override
	protected void save(boolean debug, Raid raid) {
		String path = "raids." + raid.getUUID();
		if(raid.getClaim() != null)Configs.getRaidsConfig().set(path + ".claimUUID", raid.getClaim().getUUID());
		Configs.getRaidsConfig().set(path + ".center", raid.getCenter());
		if(raid.getOffensive() != null)Configs.getRaidsConfig().set(path + ".offensiveUUID", raid.getOffensive().getUUID());
		if(raid.getDefensive() != null)Configs.getRaidsConfig().set(path + ".defensiveUUID", raid.getDefensive().getUUID());
		Configs.getRaidsConfig().set(path + ".points", raid.getPoints());
		Configs.getRaidsConfig().set(path + ".maxPoints", raid.getMaxPoints());
		Configs.getRaidsConfig().set(path + ".endDate", raid.getEndDate());
		Configs.getRaidsConfig().set(path + ".finished", raid.isFinished());
		if(debug)GuildsPlugin.console.sendMessage("§6Saved §e" + raid.getUUID());
	}
}
