package me.pmilon.RubidiaGuilds.claims;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.events.GMemberClaimEvent;
import me.pmilon.RubidiaGuilds.events.GMemberUnclaimEvent;
import me.pmilon.RubidiaGuilds.guilds.GHome;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.raids.Raid;

public class Claims{
	
	public static void manageClaim(String name, Guild claiming, Location location, GMember member){
		RPlayer rp = RPlayer.get(member);
		if(location.getWorld().getName().contains("nether") || location.getWorld().getName().contains("the_end")){
			rp.sendMessage("§cVous ne pouvez conquérir ce territoire !");
			return;
		}
		Claim claim = Claim.get(location);
		if(claim != null){
			if(claim.getGuild().equals(member.getGuild())){
				Claims.unclaim(claiming, claim, member, rp, location);
				return;
			}
		}
		Claims.claim(name, claiming, claim, location, member, rp);
	}

	private static void claim(String name, Guild guild, Claim claim, Location location, GMember member, RPlayer rp) {
		if(claim != null){
			Guild claimed = claim.getGuild();
			if(member.getPermission(Permission.CLAIM)){
				if(claimed != null){
					if(!claimed.equals(guild)){
						if(!claimed.isPeaceful()){
							if(!guild.isPeaceful()){
								if(claimed.isActive()){
									if(claimed.getRelationTo(guild).equals(Relation.ENEMY)){
										if(claimed.isConnected()){
											int points = guild.getLevel()-guild.getClaims().size();
											if(points > 0){
												for(Raid raid : Raid.getOffensiveRaids(guild)){
													if(claimed.equals(raid.getDefensive())){
														if(raid.getEndDate()+6*60*60*1000L > System.currentTimeMillis()){
															long tTime = (raid.getEndDate()+6*60*60*1000L)-System.currentTimeMillis();
															long hrs = TimeUnit.MILLISECONDS.toHours(tTime);
															tTime -= TimeUnit.HOURS.toMillis(hrs);
															long min = TimeUnit.MILLISECONDS.toMinutes(tTime);
															tTime -= TimeUnit.MINUTES.toMillis(min);
															String sFrTime = String.format("§4%02d §cheures & §4%02d §cminutes.", hrs, min);
															rp.sendMessage("§cVotre guilde ne peut attaquer le territoire de la guilde §6§l" + claimed.getName() + "§e pendant "+sFrTime);
															return;
														}
													}
												}
												for(Raid raid : Raid.getDefensiveRaids(guild)){
													if(claimed.equals(raid.getOffensive()) && claim.equals(raid.getClaim())){
														if(raid.getEndDate()+2*60*60*1000L > System.currentTimeMillis()){
															long tTime = (raid.getEndDate()+2*60*60*1000L)-System.currentTimeMillis();
															long hrs = TimeUnit.MILLISECONDS.toHours(tTime);
															tTime -= TimeUnit.HOURS.toMillis(hrs);
															long min = TimeUnit.MILLISECONDS.toMinutes(tTime);
															tTime -= TimeUnit.MINUTES.toMillis(min);
															String sFrTime = String.format("§4%02d §cheures & §4%02d §cminutes.", hrs, min);
															rp.sendMessage("§cCe territoire ne peut être réclamé par une guilde pendant "+sFrTime);
															return;
														}
													}
												}
												GuildsPlugin.raidcoll.startNew(guild, location);
											}else rp.sendMessage("§cVotre guilde possède trop de territoires pour son niveau !");
										}else rp.sendMessage("§cVous ne pouvez lancer de raid contre une guilde non connectée !");
									}else rp.sendMessage("§cVous ne pouvez lancer un raid que contre une guilde ennemie !");
								}else Claims.newClaim(name, guild, claim, rp, member, location);
							}else rp.sendMessage("§cVous ne pouvez conquérir de territoire appartenant à une autre guilde tant que votre guilde est en paix !");
						}else rp.sendMessage("§cCette guilde est en paix ! Vous ne pouvez conquérir son territoire.");
					}
				}else Claims.newClaim(name, guild, claim, rp, member, location);
			}else rp.sendMessage("§cVous n'êtes que " + member.getRank().getName().toLowerCase() + " ! Vous ne pouvez conquérir de territoire pour votre guilde.");
		}else Claims.newClaim(name, guild, claim, rp, member, location);
	}
	
	private static void newClaim(String name, Guild guild, Claim claim, RPlayer rp, GMember member, Location location){
		int points = guild.getLevel()-guild.getClaims().size();
		if(points > 0){
			if(claim == null){
				claim = new Claim(UUID.randomUUID().toString(), name, location.getWorld(), location.getChunk().getX(), location.getChunk().getZ(), null);
			}
			Claims.rawClaim(name, guild, member, claim, location);
		}else rp.sendMessage("§cVotre guilde possède trop de territoires pour son niveau !");
	}

	private static void unclaim(Guild guild, Claim claim, GMember member, RPlayer rp, Location location) {
		Guild claimed = claim.getGuild();
		if(member.getPermission(Permission.CLAIM)){
			if(claimed != null){
				if(claimed.equals(guild)){
					Claims.rawUnclaim(guild, member, claim, location);
				}else rp.sendMessage("§cCe territoire n'est pas la propriété de votre guilde.");
			}else rp.sendMessage("§cCe territoire n'est pas la propriété de votre guilde.");
		}else rp.sendMessage("§cVous n'êtes que " + member.getRank().getName().toLowerCase() + " ! Vous ne pouvez céder de territoire pour votre guilde.");
	}
	
	private static void rawClaim(String name, Guild guild, GMember member, Claim claim, final Location location){
		GMemberClaimEvent event = new GMemberClaimEvent(guild, member, claim);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			final Claim newClaim = event.getClaim();
			if(newClaim != null){
				GuildsPlugin.claimcoll.load(newClaim.getUUID(),newClaim);
				Guild last = claim.getGuild();
				if(last != null){
					last.getClaims().remove(newClaim);
					last.broadcastMessage(Relation.MEMBER, "§4§lATTENTION §cVotre guilde vient de perdre du territoire à x: " + newClaim.getX()*16 + " / z: " + newClaim.getZ() + " dans " + newClaim.getWorld().getName());
					last.broadcastAllyMessage("§5§l" + last.getName() + " §dvient de perdre du territoire à x: " + newClaim.getX()*16 + " / z: " + newClaim.getZ() + " dans " + newClaim.getWorld().getName());
				}
			}
			Guild newGuild = event.getGuild();
			newGuild.getClaims().add(newClaim);
			newClaim.setGuild(newGuild);
			newClaim.setName(name);
			newGuild.broadcastMessage(Relation.MEMBER, "§aVous venez de conquérir un nouveau territoire !");
			for(int i = 1;i <= 16;i++){
				final int r = i;
				new BukkitTask(GuildsPlugin.instance){

					@Override
					public void run() {
						List<Block> blocks = LocationUtils.getRoundBlocks(location, r, false);
						for(Block block : blocks){
							if(block.getChunk().equals(newClaim.getChunk())){
								Location location = LocationUtils.getSafeLocation(block.getLocation());
								location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, 15, .25, .5, .25, 0);
							}
						}
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater((long)i*3);
			}
		}
	}
	
	private static void rawUnclaim(Guild current, GMember member, Claim claim, final Location location){
		GMemberUnclaimEvent event = new GMemberUnclaimEvent(current, member, claim);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			final Claim unclaim = event.getClaim();
			Guild guild = event.getGuild();
			guild.getClaims().remove(unclaim);
			guild.broadcastMessage(Relation.MEMBER, "§eVotre guilde vient de céder du territoire à x: " + unclaim.getX()*16 + " / z: " + unclaim.getZ()*16);
			
			for(int x = 0;x < 16;x++){
				for(int z = 0;z < 16;z++){
					int xf = (unclaim.getX()*16)+x;
					int zf = (unclaim.getZ()*16)+z;
					for(int i = 0;i < guild.getHomes().length;i++){
						GHome home = guild.getHomes()[i];
						if(home != null){
							if(home.getLocation().getBlockX() == xf && home.getLocation().getBlockZ() == zf){
								guild.getHomes()[i] = null;
							}
						}
					}
				}
			}
			for(int i = 1;i <= 16;i++){
				final int r = i;
				new BukkitTask(GuildsPlugin.instance){

					@Override
					public void run() {
						List<Block> blocks = LocationUtils.getRoundBlocks(location, r, false);
						for(Block block : blocks){
							if(block.getChunk().equals(unclaim.getChunk())){
								Location location = LocationUtils.getSafeLocation(block.getLocation());
								location.getWorld().spawnParticle(Particle.SPELL_INSTANT, location, 15, .25, .5, .25, 0);
							}
						}
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater((long)i*3);
			}
			unclaim.delete();
		}
	}
	
}
