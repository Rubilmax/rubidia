package me.pmilon.RubidiaGuilds;

import me.pmilon.RubidiaCore.Smiley;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerDeathEvent;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.events.GMemberClaimedChunkEnterEvent;
import me.pmilon.RubidiaGuilds.events.GMemberClaimedChunkLeaveEvent;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.raids.Raid;
import me.pmilon.RubidiaGuilds.raids.Raid.RaidTitle;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainListener implements Listener {
	
	private final GuildsPlugin plugin;
	
	public MainListener(GuildsPlugin plugin){
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public GuildsPlugin getPlugin() {
		return plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e){
		Player player = e.getPlayer();
		RPlayer rp = RPlayer.get(player);
		GMember gm = GMember.get(player);
		if(gm == null)gm = GuildsPlugin.gmembercoll.addDefault(player.getUniqueId().toString());
		gm.setName(player.getName());
		gm.setPlayer(player);
		GMember.getOnlines().add(gm);
		if(gm.hasGuild()){
			if(!gm.getGuild().isActive())rp.sendMessage("§cVotre guilde était inactive ! Tous vos territoires étaient marqués comme libre d'être conquis et peuvent avoir été détruit. Désormais, tous les territoires laissés intacts sont de nouveau vôtre.");
			gm.getGuild().setLastConnection(System.currentTimeMillis());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		GMember gm = GMember.get(event.getPlayer());
		gm.setPlayer(null);
		GMember.getOnlines().remove(gm);
		if(gm.hasGuild())gm.getGuild().setLastConnection(System.currentTimeMillis());
	}
	
	@EventHandler
	private void onMove(PlayerMoveEvent e){
		Player player = e.getPlayer();
		GMember member = GMember.get(player);
		Location lastLocation = e.getFrom();
		Location newLocation = e.getTo();
		if(!lastLocation.getWorld().equals(newLocation.getWorld()) || !lastLocation.getChunk().equals(newLocation.getChunk())){
			Claim lastClaim = Claim.get(lastLocation);
			Claim newClaim = Claim.get(newLocation);
			if(member.hasGuild()){
				Guild guild = member.getGuild();
				if(guild.isRaiding()){
					Raid raid = guild.getCurrentRaid();
					if(raid.isStarted()){
						RPlayer rp = RPlayer.get(member);
						raid.updatePositions();
						if(raid.getClaim().equals(lastClaim)){
							if(guild.equals(raid.getOffensive())){
								String title = "§4§l[-] §c" + (raid.getOffendersInside().size()-1);
								raid.titles.put(rp, new RaidTitle(title, 1));
								rp.sendTitle(title, raid.getSubtitle(), 0, 20, 0);
							}else{
								String title = "§4§l[-] §c" + (raid.getDefendersInside().size()-1);
								raid.titles.put(rp, new RaidTitle(title, 1));
								rp.sendTitle(title, raid.getSubtitle(), 0, 20, 0);
							}
						}else if(raid.getClaim().equals(newClaim)){
							if(guild.equals(raid.getOffensive())){
								String title = "§6§l[+] §e" + (raid.getOffendersInside().size()+1);
								raid.titles.put(rp, new RaidTitle(title, 1));
								rp.sendTitle(title, raid.getSubtitle(), 0, 20, 0);
							}else{
								String title = "§6§l[+] §e" + (raid.getDefendersInside().size()+1);
								raid.titles.put(rp, new RaidTitle(title, 1));
								rp.sendTitle(title, raid.getSubtitle(), 0, 20, 0);
							}
						}
					}
					return;
				}
			}
			if(newClaim != null){
				GMemberClaimedChunkEnterEvent event = new GMemberClaimedChunkEnterEvent(newClaim.getGuild(), member, newClaim, newLocation);
				Bukkit.getPluginManager().callEvent(event);
				if(!event.isCancelled()){
					member = event.getGMember();
					RPlayer rp = RPlayer.get(member);
					Guild from = event.getGuild();
					if(from.isActive()){
						if(member.hasGuild()){
							if(lastClaim != null){
								if(lastClaim.getName().equalsIgnoreCase(event.getClaim().getName())){
									return;
								}
							}
							
							if(member.getGuild().equals(from) || member.getGuild().getRelationTo(from).equals(Relation.ALLY)){
								rp.sendTitle(MessageManager.dcolorCode(from, member.getGuild()) + "§l" + from.getName().toUpperCase(), MessageManager.ccolorCode(from, member.getGuild()) + event.getClaim().getName(), 5, 55, 10);
								return;
							}
						}
						
						if(lastClaim == null){
							rp.sendTitle(MessageManager.dcolorCode(from, member.getGuild()) + "§l" + from.getName().toUpperCase(), MessageManager.ccolorCode(from, member.getGuild()) + from.getDescription(), 5, 55, 10);
						}
					}
				}else e.setCancelled(true);
			}else{
				if(lastClaim != null){
					GMemberClaimedChunkLeaveEvent event = new GMemberClaimedChunkLeaveEvent(lastClaim.getGuild(), member, lastClaim, lastLocation);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						if(event.getClaim().getGuild().isActive()){
							MessageManager.left(event.getGMember());
						}
					}else e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event){
		for(final Block block : event.blockList()){
			Claim claim = Claim.get(block.getLocation());
			if(claim != null){
				Guild guild = claim.getGuild();
				if(guild != null){
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(RPlayerDeathEvent event){
		RPlayer rp = event.getRPlayer();
		GMember member = GMember.get(rp);
		if(member != null){
			if(member.hasGuild()){
				Guild guild = member.getGuild();
				if(guild.isRaiding()){
					Raid raid = guild.getCurrentRaid();
					RPlayer killer = event.getKiller();
					if(killer != null){
						GMember gkiller = GMember.get(killer);
						if(gkiller.getGuild().equals(raid.getDefensive()) && raid.getOffensive().equals(guild)){
							raid.setPoints(raid.getPoints()-5);
						}else if(gkiller.getGuild().equals(raid.getOffensive()) && raid.getDefensive().equals(guild)){
							raid.setPoints(raid.getPoints()+5);
						}
					}
					rp.sendMessage("§eTapez §6/raid§e pour être téléporté au champ de bataille !");
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				Player p = event.getPlayer();
				RPlayer rp = RPlayer.get(p);
				GMember member = GMember.get(p);
				
				if(event.getAction().toString().contains("RIGHT_CLICK")){
					ItemStack item = p.getEquipment().getItemInMainHand();
					if(item.hasItemMeta()){
						ItemMeta meta = item.getItemMeta();
						if(meta.hasDisplayName()){
							if(meta.getDisplayName().contains("§fCape")){
								String[] name = ChatColor.stripColor(meta.getDisplayName()).split(" ");
								if(name.length > 2){
									Guild guild = GuildsPlugin.gcoll.getByName(name[2]);
									if(guild != null){
										if(member.hasGuild()){
											if(member.getGuild().equals(guild)){
												event.setCancelled(true);
												if(Smiley.isSmileying(p))rp.smileyTask.run();
												ItemStack cape = item.clone();
												cape.setAmount(1);
												ItemStack helmet = p.getEquipment().getHelmet();
												p.getEquipment().setHelmet(cape);
												item.setAmount(item.getAmount()-1);
												if(item.getAmount() < 1)p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
												if(helmet != null)p.getInventory().addItem(helmet);
												Utils.updateInventory(p);
											}else rp.sendMessage("§cVous n'appartenez pas à la guilde §4§l" + name[2] + "§c !");
										}else rp.sendMessage("§cVous n'appartenez pas à la guilde §4§l" + name[2] + "§c !");
									}else rp.sendMessage("§cVous n'appartenez pas à la guilde §4§l" + name[2] + "§c !");
								}
							}
						}
					}
				}
			}
		}
	}
}
