package me.pmilon.RubidiaGuilds.guilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaGuilds.GuildsPlugin;

public class GMember extends PermissionsHolder {
	
	private final static List<GMember> onlines = new ArrayList<GMember>();

	private String uuid;
	private String name;
	private String guildId;
	private int rankId;
	
	private Player player = null;
	
	public GMember(String uuid, String name, String guildId, int rankId, HashMap<String, Boolean> permissions, boolean[] canHome, boolean[] canSetHome){
		super(permissions, canHome, canSetHome);
		this.uuid = uuid;
		this.name = name;
		this.guildId = guildId;
		this.rankId = rankId;
	}
	
	public static GMember get(Player player){
		return GuildsPlugin.gmembercoll.get(player);
	}
	
	public static GMember get(RPlayer rp){
		return GuildsPlugin.gmembercoll.get(rp);
	}
	
	public static GMember get(String uuid){
		return GuildsPlugin.gmembercoll.get(uuid);
	}

	public static List<GMember> getOnlines() {
		return onlines;
	}

	public String getUniqueId() {
		return uuid;
	}

	public void setUniqueId(String uuid) {
		this.uuid = uuid;
		this.setModified(true);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setModified(true);
	}

	public Guild getGuild() {
		return Guild.getByUUID(this.guildId);
	}
	
	public void setGuild(Guild guild){
		this.guildId = guild.getUUID();
		this.setModified(true);
	}
	
	public String getGuildId(){
		return this.guildId;
	}

	public void setGuildId(String guildId) {
		this.guildId = guildId;
		this.setModified(true);
	}

	public int getRankId() {
		return rankId;
	}

	public void setRankId(int rankId) {
		this.rankId = rankId;
		this.setModified(true);
	}
	
	public Player getPlayer(){
		return this.player;
	}

	public boolean isOnline(){
		return this.getPlayer() != null;
	}
	
	public boolean isLeader(){
		return this.getRankId() == 0;
	}

	public boolean hasGuild(){
		return this.getGuild() != null && !(this.getGuild().equals(Guild.getNone()));
	}

	public boolean isInvited(Guild guild){
		return guild.invited.contains(this);
	}
	
	public boolean hasAsked(Guild guild){
		return guild.askers.contains(this);
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.setModified(true);
	}
	
	public void setRank(Rank rank){
		this.setRankId(rank.getId());
		rank.resetPermissions(this);
		RPlayer rp = RPlayer.get(this);
		rp.sendMessage("§eVous avez été promu §6" + rank.getName() + " §e!");
	}
	
	public Rank getRank(){
		if(this.hasGuild()){
			return this.getGuild().getRanks()[this.getRankId()];
		}
		return Guild.getNone().getDefaultRank();
	}

	public boolean canSetHome(int index) {
		RPlayer rp = RPlayer.get(this);
		return rp.isOp() || super.canSetHome(index);
	}

	public boolean canHome(int index) {
		RPlayer rp = RPlayer.get(this);
		return rp.isOp() || super.canHome(index);
	}
	
	public boolean getPermission(Permission permission){
		RPlayer rp = RPlayer.get(this);
		return rp.isOp() || this.isLeader() || super.getPermission(permission);
	}
}
