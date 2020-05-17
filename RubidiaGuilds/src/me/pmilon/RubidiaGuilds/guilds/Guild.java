package me.pmilon.RubidiaGuilds.guilds;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.events.GMemberAskGuildEvent;
import me.pmilon.RubidiaGuilds.events.GuildExpChangeEvent;
import me.pmilon.RubidiaGuilds.events.GuildInviteGMemberEvent;
import me.pmilon.RubidiaGuilds.events.GuildLevelChangeEvent;
import me.pmilon.RubidiaGuilds.events.GuildRelationsChangeEvent;
import me.pmilon.RubidiaGuilds.raids.Raid;
import me.pmilon.RubidiaGuilds.utils.Configs;
import me.pmilon.RubidiaGuilds.utils.LevelUtils;
import me.pmilon.RubidiaGuilds.utils.Settings;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class Guild {

	public static int NAME_LENGTH = 10;
	public static int DESC_LENGTH = 22;
	
	private String uuid;
	private String name;
	private String description;
	private int level;
	private double experience;
	private int defaultRankId;
	private Rank[] ranks;
	private GHome[] homes;
	private List<String> membersUUIDs;
	private List<String> alliesUUIDs;
	private List<String> enemiesUUIDs;
	private List<Claim> claims;
	private boolean claimBuildable;
	private boolean claimDoorsUsable;
	private boolean claimChestsUsable;
	private boolean claimMobsDamageable;
	private boolean peaceful;
	private ItemStack cape;
	private boolean glowing;
	private int bank;
	private Long lastConnection;
	
	public List<GMember> invited;
	public List<GMember> askers;
	private Raid currentRaid;
	private final List<GMember> members = new ArrayList<GMember>();
	private final List<Guild> enemies = new ArrayList<Guild>();
	private final List<Guild> allies = new ArrayList<Guild>();
	
	public Guild(String uuid, String name, String description, int level, double experience, int defaultRankId, Rank[] ranks, GHome[] homes,
			List<String> membersUUIDs, List<String> alliesUUIDs, List<String> enemiesUUIDs, List<Claim> claims, boolean claimBuildable,
			boolean claimDoorsUsable, boolean claimChestsUsable, boolean claimMobsDamageable, ItemStack cape, boolean glowing,
			int bank, boolean peaceful, String lastRaidUUID, Long lastConnection){
		this.uuid = uuid;
		this.name = name;
		this.description = description;
		this.level = level;
		this.experience = experience;
		this.defaultRankId = defaultRankId;
		this.ranks = ranks;
		this.homes = homes;
		this.membersUUIDs = membersUUIDs;
		this.alliesUUIDs = alliesUUIDs;
		this.enemiesUUIDs = enemiesUUIDs;
		this.claims = claims;
		this.claimBuildable = claimBuildable;
		this.claimDoorsUsable = claimDoorsUsable;
		this.claimChestsUsable = claimChestsUsable;
		this.claimMobsDamageable = claimMobsDamageable;
		this.cape = cape;
		this.glowing = glowing;
		this.bank = bank;
		this.peaceful = peaceful;
		this.lastConnection = lastConnection;
		
		this.invited = new ArrayList<GMember>();
		this.askers = new ArrayList<GMember>();
	}
	
	public static Guild getByUUID(String uuid){
		return GuildsPlugin.gcoll.get(uuid);
	}
	
	public static Guild getByName(String name){
		return GuildsPlugin.gcoll.getByName(name);
	}
	
	public static Guild getNone(){
		return GuildsPlugin.gcoll.getNone();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		GuildLevelChangeEvent event = new GuildLevelChangeEvent(this, this.level, level);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			this.level = event.getNewLevel();
		}
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		GuildExpChangeEvent event = new GuildExpChangeEvent(this, this.experience, experience);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			this.experience = event.getNewExp();
			while(this.experience >= LevelUtils.getLevelExperienceAmount(this.level)){
				this.experience -= LevelUtils.getLevelExperienceAmount(this.level);
				this.level += 1;
			}
			while(this.experience < 0){
				this.level -= 1;
				this.experience += LevelUtils.getLevelExperienceAmount(this.level);
			}
		}
	}

	public List<GMember> getMembers() {
		if(this.members.isEmpty() && !this.getMembersUUIDs().isEmpty()){
			for(String uuid : this.getMembersUUIDs()){
				GMember member = GMember.get(uuid);
				if(member != null && member.getGuildId().equals(this.getUUID())){
					this.members.add(member);
				}
			}
		}
		return members;
	}
	
	public void addMember(GMember member){
		if(!this.getMembers().contains(member)){
			this.getMembers().add(member);
			member.setGuild(this);
			member.setRankId(this.getDefaultRankId());
		}
	}
	
	public void removeMember(GMember member){
		if(this.getMembers().contains(member)){
			Guild none = Guild.getNone();
			this.getMembers().remove(member);
			this.getMembersUUIDs().remove(member.getUniqueId());
			member.setGuild(none);
			member.setRank(none.getDefaultRank());
			
			RPlayer rp = RPlayer.get(member);
			if(rp.isOnline()){
				rp.getPlayer().getWorld().spawnParticle(Particle.REDSTONE, rp.getPlayer().getEyeLocation(), 25, .25, .25, .25);
				rp.getPlayer().getWorld().spawnParticle(Particle.SLIME, rp.getPlayer().getEyeLocation(), 25, .25, .25, .25);
				rp.getPlayer().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, rp.getPlayer().getEyeLocation(), 25, .25, .25, .25);
				rp.getPlayer().getWorld().spawnParticle(Particle.TOWN_AURA, rp.getPlayer().getEyeLocation(), 25, .25, .25, .25);
			}
		}
	}

	public List<Guild> getAllies() {
		if(this.allies.isEmpty() && !this.getAlliesUUIDs().isEmpty()){
			for(String ally : this.getAlliesUUIDs()){
				Guild guild = Guild.getByUUID(ally);
				if(guild != null){
					this.allies.add(guild);
				}
			}
		}
		return allies;
	}

	public void addAlly(Guild guild){
		if(!this.getAllies().contains(guild)){
			GuildRelationsChangeEvent event = new GuildRelationsChangeEvent(this, guild);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				this.getAllies().add(guild);
				this.getAlliesUUIDs().add(guild.getUUID());
			}
		}
	}
	
	public void removeAlly(Guild guild){
		if(this.getAllies().contains(guild)){
			GuildRelationsChangeEvent event = new GuildRelationsChangeEvent(this, guild);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				this.getAllies().remove(guild);
				this.getAlliesUUIDs().remove(guild.getUUID());
			}
		}
	}
	
	public List<Guild> getEnemies() {
		if(this.enemies.isEmpty() && !this.getEnemiesUUIDs().isEmpty()){
			for(String enemy : this.getEnemiesUUIDs()){
				Guild guild = Guild.getByUUID(enemy);
				if(guild != null){
					this.enemies.add(guild);
				}
			}
		}
		return enemies;
	}

	public void addEnemy(Guild guild){
		if(guild != null && !this.getEnemies().contains(guild)){
			GuildRelationsChangeEvent event = new GuildRelationsChangeEvent(this, guild);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				this.getEnemies().add(guild);
				this.getEnemiesUUIDs().add(guild.getUUID());
			}
		}
	}
	
	public void removeEnemy(Guild guild){
		if(this.getEnemies().contains(guild)){
			GuildRelationsChangeEvent event = new GuildRelationsChangeEvent(this, guild);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				this.getEnemies().remove(guild);
				this.getEnemiesUUIDs().remove(guild.getUUID());
			}
		}
	}
	
	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	
	public Relation getRelationTo(Guild guild){
		if(guild != null){
			if(this.equals(guild))return Relation.MEMBER;
			else if(this.isPeaceful() || guild.isPeaceful())return Relation.PEACEFUL;
			else if(this.getAllies().contains(guild) && guild.getAllies().contains(this))return Relation.ALLY;
			else if(this.getEnemies().contains(guild) || guild.getEnemies().contains(this))return Relation.ENEMY;
		}
		return Relation.NEUTRAL;
	}

	public void broadcastAllMessage(String message){
		this.broadcastMessage(Relation.MEMBER, message);
		this.broadcastAllyMessage(message);
	}
	
	public void broadcastAllyMessage(String message){
		for(Guild allied : this.getAllies()){
			allied.broadcastMessage(Relation.ALLY, message);
		}
	}
	
	public void broadcastMessage(Relation type, String message){
		for(GMember member : this.getMembers()){
			if(RPlayer.get(member) != null){
				RPlayer.get(member).sendMessage(message.replaceAll("§&d", type.getDColorCode()).replaceAll("§&c", type.getCColorCode()));
			}
		}
	}

	public boolean isClaimBuildable() {
		return claimBuildable;
	}

	public void setClaimBuildable(boolean claimBuildable) {
		this.claimBuildable = claimBuildable;
	}

	public boolean isPeaceful() {
		return peaceful;
	}

	public void setPeaceful(boolean peaceful) {
		this.peaceful = peaceful;
	}
	
	public GMember getLeader(){
		for(GMember member : this.getMembers()){
			if(member.isLeader()){
				return member;
			}
		}
		return null;
	}
	
	public void disband(){
		if(this.getCurrentRaid() != null){
			if(this.equals(this.getCurrentRaid().getDefensive())){
				this.getClaims().remove(this.getCurrentRaid().getClaim());
				this.getCurrentRaid().getClaim().setGuild(this.getCurrentRaid().getOffensive());
				this.getCurrentRaid().getOffensive().getClaims().add(this.getCurrentRaid().getClaim());
				for(GMember member : this.getCurrentRaid().getOffensive().getMembers()){
					RPlayer rp = RPlayer.get(member);
					rp.sendMessage("                  §b§m-----§8§m[  §r    §7RAID TERMINE    §8§m  ]§b§m-----");
					if(rp.isOnline())rp.getPlayer().sendMessage("");
					rp.sendMessage("                §eLes ennemis ont abandonné leur guilde.");
					if(rp.isOnline()){
						rp.getPlayer().sendMessage("");
						rp.getPlayer().sendMessage("                  §b§m-----§8§m[                            ]§b§m-----");
					}
				}
			}else{
				for(GMember member : this.getCurrentRaid().getDefensive().getMembers()){
					RPlayer rp = RPlayer.get(member);
					rp.sendMessage("                  §b§m-----§8§m[  §r    §7RAID TERMINE    §8§m  ]§b§m-----");
					if(rp.isOnline())rp.getPlayer().sendMessage("");
					rp.sendMessage("                §eLes ennemis ont abandonné leur guilde.");
					if(rp.isOnline()){
						rp.getPlayer().sendMessage("");
						rp.getPlayer().sendMessage("                  §b§m-----§8§m[                            ]§b§m-----");
					}
				}
			}
			this.getCurrentRaid().stop();
		}
		
		for(GMember member : this.getMembers()){
			RPlayer rp = RPlayer.get(member);
			if(rp.isOnline()){
				rp.getPlayer().getWorld().spawnParticle(Particle.SLIME, rp.getPlayer().getEyeLocation(), 25, .25, .25, .25);
				rp.getPlayer().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, rp.getPlayer().getEyeLocation(), 25, .25, .25, .25);
				rp.getPlayer().getWorld().spawnParticle(Particle.TOWN_AURA, rp.getPlayer().getEyeLocation(), 25, .25, .25, .25);
			}
			member.setGuild(Guild.getNone());
			member.setRank(Guild.getNone().getDefaultRank());
		}
		
		for(Guild guild : this.getAllies()){
			guild.removeAlly(this);
		}
		
		for(Guild guild : this.getEnemies()){
			guild.removeEnemy(this);
		}
		
		for(Claim claim : this.getClaims()){
			claim.delete();
		}
		
		GuildsPlugin.gcoll.remove(this.getUUID());
		Configs.getGuildConfig().set("guilds." + this.getUUID(), null);
		
		RPlayer.broadcastMessage("§cLa guilde §4§l" + this.getName() + " §ca été dissoute.");
	}

	public void invite(GMember member){
		GuildInviteGMemberEvent event = new GuildInviteGMemberEvent(this, member);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			if(this.askers.contains(member)){
				this.askers.remove(member);
				this.addMember(member);
				RPlayer.get(member).sendMessage("§6§l" + this.getName() + " §ea accepté votre demande d'adhésion !");
				this.broadcastAllMessage("§&d" + member.getName() + " §&ca rejoint la guilde §&d§l" + this.getName() + "§&c !");
			}else if(this.invited.contains(member)){
				this.invited.remove(member);
				RPlayer.get(member).sendMessage("§eLa guilde §6§l" + this.getName() + " §ea annulé son invitation.");
			}else{
				this.invited.add(member);
				RPlayer.get(member).sendMessage("§eLa guilde §6§l" + this.getName() + " §evous a invité à rejoindre ses membres !");
			}
		}
	}
	
	public void ask(GMember member){
		GMemberAskGuildEvent event = new GMemberAskGuildEvent(this, member);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			if(this.invited.contains(member)){
				this.invited.remove(member);
				this.addMember(member);
				RPlayer.get(member).sendMessage("§eVous avez rejoint la guilde §6§l" + this.getName() + " §e!");
				this.broadcastAllMessage("§&d" + member.getName() + " §&ca rejoint la guilde §&d§l" + this.getName() + "§&c !");
			}else if(this.askers.contains(member)){
				this.askers.remove(member);
				RPlayer.get(member).sendMessage("§eVous avez annulé votre requête d'adhésion à la guilde §6§l" + this.getName() + "§e.");
				this.broadcastMessage(Relation.MEMBER, "§&d" + member.getName() + " §&ca annulé sa demande d'adhésion.");
			}else{
				this.askers.add(member);
				RPlayer.get(member).sendMessage("§eVous avez demandé à rejoindre la guilde §6§l" + this.getName() + "§e.");
				this.broadcastMessage(Relation.MEMBER, "§&d" + member.getName() + " §&ca demandé à rejoindre votre guilde !");
			}
		}
	}

	public boolean isGlowing() {
		return glowing;
	}

	public void setGlowing(boolean glowing) {
		this.glowing = glowing;
	}

	public boolean isClaimDoorsUsable() {
		return claimDoorsUsable;
	}

	public void setClaimDoorsUsable(boolean claimDoorsUsable) {
		this.claimDoorsUsable = claimDoorsUsable;
	}

	public boolean isClaimChestsUsable() {
		return claimChestsUsable;
	}

	public void setClaimChestsUsable(boolean claimChestsUsable) {
		this.claimChestsUsable = claimChestsUsable;
	}

	public int getBank() {
		return bank;
	}

	public void setBank(int bank) {
		this.bank = bank;
	}

	public List<String> getAlliesUUIDs() {
		return alliesUUIDs;
	}

	public void setAlliesUUIDs(List<String> alliesUUIDs) {
		this.alliesUUIDs = alliesUUIDs;
	}

	public List<String> getEnemiesUUIDs() {
		return enemiesUUIDs;
	}

	public void setEnemiesUUIDs(List<String> enemiesUUIDs) {
		this.enemiesUUIDs = enemiesUUIDs;
	}

	public List<String> getMembersUUIDs() {
		return membersUUIDs;
	}

	public void setMembersUUIDs(List<String> membersUUIDs) {
		this.membersUUIDs = membersUUIDs;
	}

	public boolean isConnected(){
		for(GMember gm : this.getMembers()){
			if(gm.isOnline()){
				if(gm.getPlayer().isOnline()){
					return true;
				}
			}
		}
		return false;
	}

	public List<Claim> getClaims() {
		return claims;
	}

	public void setClaims(List<Claim> claims) {
		this.claims = claims;
	}

	
	public boolean isRaiding() {
		return this.getCurrentRaid() != null;
	}

	public Raid getCurrentRaid() {
		return currentRaid;
	}

	public void setCurrentRaid(Raid currentRaid) {
		this.currentRaid = currentRaid;
	}

	public Long getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(Long lastConnection) {
		this.lastConnection = lastConnection;
	}
	
	public boolean isActive(){
		return this.getLastConnection() >= System.currentTimeMillis()-Settings.TIME_BEFORE_INACTIVE;
	}

	public boolean isClaimMobsDamageable() {
		return claimMobsDamageable;
	}

	public void setClaimMobsDamageable(boolean claimMobsDamageable) {
		this.claimMobsDamageable = claimMobsDamageable;
	}

	public ItemStack getCape() {
		return this.isGlowing() ? Utils.setGlowingWithoutAttributes(cape.clone()) : cape.clone();
	}

	public void setCape(ItemStack cape) {
		this.cape = cape;
	}

	public void withdraw(int cost){
		this.setBank(this.getBank()-cost);
		this.broadcastMessage(Relation.MEMBER, "§4" + cost + "⟡  §cont été retirées de votre banque de guilde.");
		this.broadcastMessage(Relation.MEMBER, "§7Votre nouveau solde est de §f" + this.getBank() + "⟡");
	}
	
	public void deposit(int amount) {
		this.setBank(this.getBank()+amount);
		this.broadcastMessage(Relation.MEMBER, "§4" + amount + "⟡  §cont été ajoutées à votre banque de guilde.");
		this.broadcastMessage(Relation.MEMBER, "§7Votre nouveau solde est de §f" + this.getBank() + "⟡");
	}

	public GHome[] getHomes() {
		return homes;
	}

	public void setHomes(GHome[] homes) {
		this.homes = homes;
	}

	public int getDefaultRankId() {
		return defaultRankId;
	}

	public void setDefaultRankId(int defaultRankId) {
		this.defaultRankId = defaultRankId;
	}

	public Rank[] getRanks() {
		return ranks;
	}

	public void setRanks(Rank[] ranks) {
		this.ranks = ranks;
	}
	
	public Rank getDefaultRank(){
		return this.getRanks()[this.getDefaultRankId()];
	}

	public int getMaxBankAmount() {
		return Settings.CAPE_COST*this.getLevel();
	}
}
