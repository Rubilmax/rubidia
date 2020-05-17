package me.pmilon.RubidiaGuilds.claims;

import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.utils.Configs;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class Claim {

	private String UUID;
	private String name;
	private final World world;
	private final int X;
	private final int Z;
	private String guildUUID;
	
	private final Chunk chunk;
	private Guild guild;
	private boolean modified = false;
	public Claim(String uuid, String name, World world, int x, int z, String guildUUID){
		this.UUID = uuid;
		this.name = name;
		this.world = world;
		this.X = x;
		this.Z = z;
		this.guildUUID = guildUUID;
		this.chunk = world.getChunkAt(x, z);
	}
	
	public static Claim get(Location location){
		return GuildsPlugin.claimcoll.get(location);
	}
	
	public static Claim get(Chunk chunk){
		return GuildsPlugin.claimcoll.get(chunk);
	}
	
	public static Claim get(String uuid){
		return GuildsPlugin.claimcoll.get(uuid);
	}
	
	public String getUUID() {
		return UUID;
	}
	
	public void setUUID(String uUID) {
		UUID = uUID;
		this.setModified(true);
	}

	public World getWorld() {
		return world;
	}

	public int getX() {
		return X;
	}

	public int getZ() {
		return Z;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public Guild getGuild() {
		if(this.guild == null || !GuildsPlugin.gcoll.containsKey(this.guild.getUUID())){
			this.guild = Guild.getByUUID(this.getGuildUUID());
		}
		return this.guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
		this.guildUUID = guild.getUUID();
		this.setModified(true);
	}

	public String getGuildUUID() {
		return guildUUID;
	}

	public void setGuildUUID(String guildUUID) {
		this.guildUUID = guildUUID;
		this.setModified(true);
	}
	
	public void delete(){
		GuildsPlugin.claimcoll.remove(this.getUUID());
		Configs.getClaimsConfig().set("claims." + this.getUUID(), null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setModified(true);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
