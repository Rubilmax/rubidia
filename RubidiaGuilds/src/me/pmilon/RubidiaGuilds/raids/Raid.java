package me.pmilon.RubidiaGuilds.raids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class Raid {

	private static final int MAX_DISTANCE = 20;
	private static final Long RAID_TIME = 3*60*1000L;
	
	private String uuid;
	private Claim claim;
	private Location center;
	private Guild offensive;
	private Guild defensive;
	private double points = 0;
	private int maxPoints;
	
	private Long elapsedTime = 0L;
	private final List<RPlayer> rplayers = new ArrayList<RPlayer>();
	private final List<GMember> gmembers = new ArrayList<GMember>();
	private Long endDate = System.currentTimeMillis();
	private boolean finished = false;
	private boolean stopped = false;
	private boolean started = false;
	
	public HashMap<RPlayer, RaidTitle> titles = new HashMap<RPlayer, RaidTitle>();
	private final List<GMember> offendersInside = new ArrayList<GMember>();
	private final List<GMember> defendersInside = new ArrayList<GMember>();
	
	public static class RaidTitle{
		
		private String title;
		private int delay = 1;
		private int maxDelay;
		public RaidTitle(String title, int maxDelay){
			this.title = title;
			this.maxDelay = maxDelay;
		}
		
		public String getTitle(){
			return this.title;
		}
		
		public void setTitle(String title){
			this.title = title;
		}
		
		public void addDelay(){
			this.delay++;
		}
		
		public int getMaxDelay(){
			return this.maxDelay;
		}

		public int getDelay() {
			return delay;
		}
	}
	
	public Raid(String uuid, Claim claim, Location center, Guild offensive, Guild defensive){
		this.uuid = uuid;
		this.claim = claim;
		this.center = center;
		this.offensive = offensive;
		this.defensive = defensive;
		this.maxPoints = offensive != null ? (int) Math.round(offensive.getMembers().size()*.64*((double) RAID_TIME / 1000L)) : 0;
	}
	
	public static List<Raid> getOffensiveRaids(Guild offensive){
		return GuildsPlugin.raidcoll.getOffensiveRaids(offensive);
	}
	
	public static List<Raid> getDefensiveRaids(Guild defensive){
		return GuildsPlugin.raidcoll.getDefensiveRaids(defensive);
	}

	public Guild getOffensive() {
		return offensive;
	}

	public void setOffensive(Guild offensive) {
		this.offensive = offensive;
	}

	public Guild getDefensive() {
		return defensive;
	}

	public void setDefensive(Guild defensive) {
		this.defensive = defensive;
	}

	public Location getCenter() {
		return center;
	}

	public void setCenter(Location center) {
		this.center = center;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
		if(this.points < 0)this.points = 0;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}
	
	public void prestart(){
		getOffensive().setCurrentRaid(this);
		getDefensive().setCurrentRaid(this);
		for(GMember member : this.getGMembers()){
			if(member.isOnline()){
				final RPlayer rp = RPlayer.get(member);
				if(member.getGuild().equals(this.getOffensive())){
					if(member.getPlayer().getWorld().equals(this.getCenter().getWorld())){
						if(member.getPlayer().getLocation().distanceSquared(this.getCenter()) <= Math.pow(MAX_DISTANCE, 2)){
							rp.sendTitle("§6Offensive lancée", "§ePhase de préparation - 20 secondes", 0, 80, 40);
						}else rp.sendTitle("§6Offensive lancée", "§eVous serez téléporté dans 20 secondes", 0, 80, 40);
					}else rp.sendTitle("§6Offensive lancée", "§eVous serez téléporté dans 20 secondes", 0, 80, 40);
				}else{
					rp.sendTitle(("§4§lOffensive ennemie"), "§c[§l" + this.getClaim().getName() + "§c] " + this.getCenter().getBlockX() + "/" + this.getCenter().getBlockZ(), 0, 80, 40);
					new BukkitTask(GuildsPlugin.instance){

						@Override
						public void run() {
							if(rp.isOnline())rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, .5F);
						}

						@Override
						public void onCancel() {
						}
								
					}.runTaskTimerCancelling(0, 5, 15);
				}
			}
		}
		
		new BukkitTask(GuildsPlugin.instance){

			@Override
			public void run() {
				if(!stopped){
					updatePositions();
					for(GMember member : getDefensive().getMembers()){
						if(member.isOnline()){
							if(!getDefendersInside().contains(member)){
								return;
							}
						}
					}
					
					this.cancel();
				}else this.cancel();
			}

			@Override
			public void onCancel() {
				if(!stopped){
					for(RPlayer rp : getRPlayers()){
						rp.sendTitle("§eTout le monde est prêt !", "§6§l" + getOffensive().getName() + "  §e!§6§l  " + getDefensive().getName(), 0, 30, 10);
					}
					new BukkitTask(GuildsPlugin.instance){
						int index = 3;

						@Override
						public void run() {
							for(RPlayer rp : getRPlayers()){
								rp.sendTitle("§a" + index + "...", "§6§l" + getOffensive().getName() + "  §e!§6§l  " + getDefensive().getName(), 0, 10, 10);
								if(rp.isOnline())rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
							}
							index--;
						}

						@Override
						public void onCancel() {
							start();
						}
						
					}.runTaskTimerCancelling(40, 20, 60);
				}else{
					finished = true;
					getOffensive().setCurrentRaid(null);
					getDefensive().setCurrentRaid(null);
					endDate = System.currentTimeMillis();
					for(RPlayer rp : getRPlayers()){
						rp.sendTitle("", "", 0, 0, 0);
					}
				}
			}
			
		}.runTaskTimerCancelling(3*20, 5, 20*20);
	}
	
	public void start(){
		this.setStarted(true);
		for(RPlayer rp : this.getRPlayers()){
			if(rp.isOnline()){
				rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2F);
			}
		}
		for(GMember member : this.getGMembers()){
			if(member.isOnline()){
				if(member.getGuild().equals(this.getOffensive())){
					if(member.getPlayer().getWorld().equals(this.getCenter().getWorld())){
						if(member.getPlayer().getLocation().distanceSquared(this.getCenter()) > Math.pow(MAX_DISTANCE, 2)){
							TeleportHandler.teleport(member.getPlayer(), this.getCenter());
						}
					}
				}
			}
		}
		new BukkitTask(GuildsPlugin.instance){

			@Override
			public void run() {
				if(!stopped){
					updatePositions();
					setPoints(getPoints()+getOffendersInside().size());
					setPoints(getPoints()-getDefendersInside().size());
					
					String title = "";
					if(elapsedTime%(60.0*1000L) == 0)title = "§e>  §6" + ((RAID_TIME - elapsedTime)/(60*1000L)) + "§e min  <";
					for(RPlayer rp : getRPlayers()){
						if(elapsedTime >= RAID_TIME)title = "§4" + ("Temps écoulé !");
						if(titles.get(rp) != null){
							RaidTitle tle = titles.get(rp);
							title = tle.getTitle();
							if(tle.getDelay() > tle.getMaxDelay()){
								titles.put(rp, null);
							}
							tle.addDelay();
						}else titles.put(rp, new RaidTitle(title,2));
						rp.sendTitle(title, getSubtitle(), 0, 20, 0);
					}
					if(getPoints()/getMaxPoints() >= 1 || elapsedTime >= RAID_TIME)this.cancel();
					elapsedTime+=1000;
				}else this.cancel();
			}

			@Override
			public void onCancel() {
				finished = true;
				getOffensive().setCurrentRaid(null);
				getDefensive().setCurrentRaid(null);
				endDate = System.currentTimeMillis();
				if(!stopped){
					double ratio = ((double)getPoints())/getMaxPoints();
					for(RPlayer rp : getRPlayers()){
						GMember member = GMember.get(rp);
						rp.sendMessage("                  §b§m-----§8§m[  §r    §7RAID TERMINE    §8§m  ]§b§m-----");
						if(rp.isOnline())rp.getPlayer().sendMessage("");
						if(member.getGuild().equals(getOffensive())){
							if(ratio >= 1){
								rp.sendTitle("§6Offensive gagnée !", "§eCe territoire appartient désormais à votre guilde !", 0, 80, 40);
								rp.sendMessage("§eCe territoire ne peut être réclamé par une guilde pendant 2 heures.");
							}else{
								rp.sendTitle("§4Offensive perdue !", "§cVous n'avez pas généré assez de puissance !", 0, 80, 40);
							}
							rp.sendMessage("§eVotre guilde ne pourra attaquer le territoire de la guilde §6§l" + getDefensive().getName() + "§e pendant 6 heures.");
						}else{
							if(ratio >= 1){
								rp.sendTitle("§4Territoire perdu !", "§cCe territoire appartient désormais à la guilde " + getOffensive().getName() + " !", 0, 80, 40);
								rp.sendMessage("§eVotre guilde ne pourra réclamer ce territoire pendant 2 heures.");
							}else{
								rp.sendTitle("§6Offensive défendue !", "§eTerritoire conservé !", 0, 80, 40);
							}
							rp.sendMessage("§eVotre guilde ne pourra plus être attaquée par la guilde §6§l" + getOffensive().getName() + "§e pendant 6 heures.");
						}
						if(rp.isOnline()){
							rp.getPlayer().sendMessage("");
							rp.getPlayer().sendMessage("                  §b§m-----§8§m[                            ]§b§m-----");
						}
					}
					if(ratio >= 1){
						getDefensive().getClaims().remove(getClaim());
						getClaim().setGuild(getOffensive());
						getOffensive().getClaims().add(getClaim());
					}
				}else{
					for(RPlayer rp : rplayers){
						rp.sendTitle("", "", 0, 0, 0);
					}
				}
			}
			
		}.runTaskTimer(0, 20);
	}
	
	public void stop(){
		this.stopped = true;
	}
	
	public String getSubtitle(){
		String subtitle = "§8[§7" + Math.round(getPoints()) + "§8/" + Math.round(getMaxPoints()) + "]    ";
		double ratio = getPoints()/getMaxPoints();
		subtitle += (ratio > 0 ? "§4" : "") + "[";
		for(int i = 1;i < 41;i++){
			if(i/40.0 > ratio)subtitle += "§7|";
			else subtitle += "§c|";
		}
		subtitle += (ratio >= 1 ? "§4" : "§8") + "]    §8(§7" + Math.round(ratio*100) + "§8%) - ";
		long tTime = RAID_TIME-elapsedTime;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tTime);
		tTime -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(tTime);
		tTime -= TimeUnit.SECONDS.toMillis(seconds);
		String sTime = String.format("[§7%02d§8:§7%02d§8]", minutes, seconds);
		subtitle += sTime;
		return subtitle;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}
	
	public void updatePositions(){
		this.getOffendersInside().clear();
		this.getDefendersInside().clear();
		for(GMember member : this.getGMembers()){
			if(member.isOnline()){
				Chunk chunk = member.getPlayer().getLocation().getChunk();
				if(!member.getPlayer().isDead() && chunk.getWorld().equals(getClaim().getWorld()) && chunk.getX() == getClaim().getChunk().getX() && chunk.getZ() == getClaim().getChunk().getZ()){
					if(member.getGuild().equals(getOffensive()))this.getOffendersInside().add(member);
					else this.getDefendersInside().add(member);
				}
			}
		}
	}

	public List<GMember> getOffendersInside() {
		return offendersInside;
	}

	public List<GMember> getDefendersInside() {
		return defendersInside;
	}

	public List<GMember> getGMembers() {
		gmembers.clear();
		gmembers.addAll(getOffensive().getMembers());
		gmembers.addAll(getDefensive().getMembers());
		return gmembers;
	}

	public List<RPlayer> getRPlayers() {
		rplayers.clear();
		for(GMember member : gmembers)rplayers.add(RPlayer.get(member));
		return rplayers;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
}
