package me.pmilon.RubidiaGuilds.guilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.utils.Configs;

public class GColl extends Database<String,Guild>{
	
	public GColl(){
		if(Configs.getGuildConfig().contains("guilds")){
			for(String uuid : Configs.getGuildConfig().getConfigurationSection("guilds").getKeys(false)){
				String path = "guilds." + uuid;
				List<Claim> claims = new ArrayList<Claim>();
				if(Configs.getGuildConfig().contains(path + ".claimUUIDs")){
					for(String claimUUID : Configs.getGuildConfig().getStringList(path + ".claimUUIDs")){
						Claim claim = Claim.get(claimUUID);
						if(claim != null && claim.getGuildUUID().equals(uuid))claims.add(claim);
					}
				}
				Rank[] ranks = new Rank[7];
				if(Configs.getGuildConfig().contains(path + ".ranks")){
					for(int i = 0;i < 7;i++){
						String path2 = path + ".ranks." + i;
						if(Configs.getGuildConfig().contains(path2)){
							boolean[] canSetHome = new boolean[8];
							boolean[] canHome = new boolean[8];
							for(int j = 0;j < 8;j++){
								canSetHome[j] = Configs.getGuildConfig().getBoolean(path2 + ".canSetHome." + j);
								canHome[j] = Configs.getGuildConfig().getBoolean(path2 + ".canHome." + j);
							}
							HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
							for(Permission permission : Permission.values()){
								permissions.put(permission.getPermission(), Configs.getGuildConfig().getBoolean(path2 + "." + permission.getPermission()));
							}
							Rank rank = new Rank(Configs.getGuildConfig().getItemStack(path2 + ".item"),
									Configs.getGuildConfig().getString(path2 + ".name"),
									Configs.getGuildConfig().getInt(path2 + ".id"),
									permissions, canHome, canSetHome);
							ranks[i] = rank;
						}
					}
				}
				GHome[] homes = new GHome[8];
				if(Configs.getGuildConfig().contains(path + ".homes")){
					for(int i = 0;i < 8;i++){
						String path2 = path + ".homes." + i;
						if(Configs.getGuildConfig().contains(path2)){
							GHome home = new GHome(i,
									Configs.getGuildConfig().getString(path2 + ".name"),
									(Location)Configs.getGuildConfig().get(path2 + ".location"),
									Configs.getGuildConfig().getItemStack(path2 + ".display"));
							homes[i] = home;
						}
					}
				}
				Guild newGuild = new Guild(uuid,
						Configs.getGuildConfig().getString(path + ".name"),
						Configs.getGuildConfig().getString(path + ".description"),
						Configs.getGuildConfig().getInt(path + ".level"),
						Configs.getGuildConfig().getDouble(path + ".experience"),
						Configs.getGuildConfig().getInt(path + ".defaultRankId"),
						ranks, homes,
						Configs.getGuildConfig().getStringList(path + ".members"),
						Configs.getGuildConfig().getStringList(path + ".alliesUUIDs"),
						Configs.getGuildConfig().getStringList(path + ".enemiesUUIDs"),
						claims,
						Configs.getGuildConfig().getBoolean(path + ".claimBuildable"),
						Configs.getGuildConfig().getBoolean(path + ".claimDoorsUsable"),
						Configs.getGuildConfig().getBoolean(path + ".claimChestsUsable"),
						Configs.getGuildConfig().getBoolean(path + ".claimMobsDamageable"),
						Configs.getGuildConfig().getItemStack(path + ".displayItem"),
						Configs.getGuildConfig().getBoolean(path + ".glowing"),
						Configs.getGuildConfig().getInt(path + ".bank"),
						Configs.getGuildConfig().getBoolean(path + ".peaceful"),
						Configs.getGuildConfig().getString(path + ".lastRaidUUID"),
						Configs.getGuildConfig().contains(path + ".lastConnection") ? Configs.getGuildConfig().getLong(path + ".lastConnection") : System.currentTimeMillis());
				this.load(uuid,newGuild);
				if(this.size() % 100 == 0){
					GuildsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §6GUILDS");
				}
			}
		}
		GuildsPlugin.console.sendMessage("§6LOADED §e" + this.size() + " §6GUILDS");
	}
	
	public Guild getByName(String name){
		for(Guild guild : this.data()){
			if(guild.getName().equals(name))return guild;
		}
		return null;
	}
	
	public Guild getNone(){
		return this.get("00000000-0000-0000-0000-000000000000");
	}
	
	public Guild addDefault(String name, String description, GMember leader, boolean peaceful){
		List<String> members = new ArrayList<String>();
		members.add(leader.getUniqueId());
		ItemStack cape = new ItemStack(Material.ORANGE_BANNER, 1);
		ItemMeta meta = cape.getItemMeta();
		meta.setDisplayName("§fCape de " + name);
		cape.setItemMeta(meta);
		Rank[] ranks = new Rank[7];
		ranks[0] = new Rank(new ItemStack(Material.FLINT_AND_STEEL, 1), "Leader", 0, Permission.getDefault(true), new boolean[]{true,true,true,true,true,true,true,true}, new boolean[]{true,true,true,true,true,true,true,true});
		ranks[6] = new Rank(new ItemStack(Material.APPLE, 1), "Recrue", 6, Permission.getDefault(false), new boolean[8], new boolean[8]);
		Guild guild = new Guild(UUID.randomUUID().toString(), name, description, 1, 0.0, 6, ranks, new GHome[8], members, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Claim>(), false, true, false, false, cape, false, 0, peaceful, "", System.currentTimeMillis());
		this.load(guild.getUUID(),guild);
		leader.setGuild(guild);
		leader.setRank(ranks[0]);
		return guild;
	}
	
	@Override
	protected Guild getDefault(String arg0) {
		return null;
	}

	@Override
	protected void onSaveEnd(boolean debug) {
		Configs.saveGuildConfig();
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) GuildsPlugin.console.sendMessage("§a   Saving Guilds...");
	}

	@Override
	protected void save(boolean debug, Guild guild) {
		String path = "guilds." + guild.getUUID();
		Configs.getGuildConfig().set(path + ".name", guild.getName());
		Configs.getGuildConfig().set(path + ".description", guild.getDescription());
		Configs.getGuildConfig().set(path + ".level", guild.getLevel());
		Configs.getGuildConfig().set(path + ".experience", guild.getExperience());
		Configs.getGuildConfig().set(path + ".defaultRankId", guild.getDefaultRankId());
		for(int i = 0;i < guild.getRanks().length;i++){
			String path2 = path + ".ranks." + i;
			Rank rank = guild.getRanks()[i];
			if(rank != null){
				Configs.getGuildConfig().set(path2 + ".item", rank.getItemStack());
				Configs.getGuildConfig().set(path2 + ".name", rank.getName());
				Configs.getGuildConfig().set(path2 + ".id", rank.getId());
				for(int j = 0;j < 8;j++){
					Configs.getGuildConfig().set(path2 + ".canSetHome." + j, rank.canSetHome(j));
					Configs.getGuildConfig().set(path2 + ".canHome." + j, rank.canHome(j));
				}
				for(Permission permission : Permission.values()){
					Configs.getGuildConfig().set(path2 + "." + permission.getPermission(), rank.getPermission(permission));
				}
			}else Configs.getGuildConfig().set(path2, null);//delete save
		}
		for(int i = 0;i < guild.getHomes().length;i++){
			String path2 = path + ".homes." + i;
			GHome home = guild.getHomes()[i];
			if(home != null){
				Configs.getGuildConfig().set(path2 + ".name", home.getName());
				Configs.getGuildConfig().set(path2 + ".location", home.getLocation());
				Configs.getGuildConfig().set(path2 + ".display", home.getDisplay());
			}else Configs.getGuildConfig().set(path2, null);//delete save
		}
		Configs.getGuildConfig().set(path + ".members", guild.getMembersUUIDs());
		Configs.getGuildConfig().set(path + ".alliesUUIDs", guild.getAlliesUUIDs());
		Configs.getGuildConfig().set(path + ".enemiesUUIDs", guild.getEnemiesUUIDs());
		List<String> claimUUIDs = new ArrayList<String>();
		for(Claim claim : guild.getClaims()){
			if(claim != null)claimUUIDs.add(claim.getUUID());
		}
		Configs.getGuildConfig().set(path + ".claimUUIDs", claimUUIDs);
		Configs.getGuildConfig().set(path + ".claimBuildable", guild.isClaimBuildable());
		Configs.getGuildConfig().set(path + ".claimDoorsUsable", guild.isClaimDoorsUsable());
		Configs.getGuildConfig().set(path + ".claimChestsUsable", guild.isClaimChestsUsable());
		Configs.getGuildConfig().set(path + ".claimMobsDamageable", guild.isClaimMobsDamageable());
		Configs.getGuildConfig().set(path + ".alive", true);
		Configs.getGuildConfig().set(path + ".bank", guild.getBank());
		Configs.getGuildConfig().set(path + ".displayItem", guild.getCape());
		Configs.getGuildConfig().set(path + ".glowing", guild.isGlowing());
		Configs.getGuildConfig().set(path + ".peaceful", guild.isPeaceful());
		Configs.getGuildConfig().set(path + ".lastConnection", guild.getLastConnection());
		if(debug)GuildsPlugin.console.sendMessage("§6Saved §e" + guild.getUUID()+ " §6aka §e" + guild.getName());
	}
	
}
