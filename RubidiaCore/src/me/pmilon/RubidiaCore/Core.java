package me.pmilon.RubidiaCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.pmilon.RubidiaCore.REvents.Event;
import me.pmilon.RubidiaCore.REvents.Events;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.RPlayerColl;
import me.pmilon.RubidiaCore.abilities.AbilitiesListener;
import me.pmilon.RubidiaCore.abilities.RAbility;
import me.pmilon.RubidiaCore.aeroplane.AeroplaneListener;
import me.pmilon.RubidiaCore.chairs.ChairListener;
import me.pmilon.RubidiaCore.chat.RChatFixDisplay;
import me.pmilon.RubidiaCore.chat.RChatListener;
import me.pmilon.RubidiaCore.commands.BienvenueCommandExecutor;
import me.pmilon.RubidiaCore.commands.BoostersCommandExecutor;
import me.pmilon.RubidiaCore.commands.ChatCommandExecutor;
import me.pmilon.RubidiaCore.commands.ClassCommandExecutor;
import me.pmilon.RubidiaCore.commands.CoupleCommandExecutor;
import me.pmilon.RubidiaCore.commands.EventsCommandExecutor;
import me.pmilon.RubidiaCore.commands.HMPCommandExecutor;
import me.pmilon.RubidiaCore.commands.HelpCommandExecutor;
import me.pmilon.RubidiaCore.commands.ItemCommandExecutor;
import me.pmilon.RubidiaCore.commands.LevelCommandExecutor;
import me.pmilon.RubidiaCore.commands.MarryCommandExecutor;
import me.pmilon.RubidiaCore.commands.MasteryCommandExecutor;
import me.pmilon.RubidiaCore.commands.MoneyCommandExecutor;
import me.pmilon.RubidiaCore.commands.MuteCommandExecutor;
import me.pmilon.RubidiaCore.commands.ProfileCommandExecutor;
import me.pmilon.RubidiaCore.commands.RPlayersCommandExecutor;
import me.pmilon.RubidiaCore.commands.RankingsCommandExecutor;
import me.pmilon.RubidiaCore.commands.RebootCommandExecutor;
import me.pmilon.RubidiaCore.commands.ResetCommandExecutor;
import me.pmilon.RubidiaCore.commands.SKDCommandExecutor;
import me.pmilon.RubidiaCore.commands.SKPCommandExecutor;
import me.pmilon.RubidiaCore.commands.ScrollCommandExecutor;
import me.pmilon.RubidiaCore.commands.SpawnCommandExecutor;
import me.pmilon.RubidiaCore.commands.StatisticsCommandExecutor;
import me.pmilon.RubidiaCore.commands.VIPCommandExecutor;
import me.pmilon.RubidiaCore.commands.VanishCommandExecutor;
import me.pmilon.RubidiaCore.commands.VoteCommandExecutor;
import me.pmilon.RubidiaCore.couples.Couple;
import me.pmilon.RubidiaCore.couples.CoupleColl;
import me.pmilon.RubidiaCore.crafts.Crafts;
import me.pmilon.RubidiaCore.duels.RDuelListener;
import me.pmilon.RubidiaCore.events.RPlayerDeathEvent;
import me.pmilon.RubidiaCore.events.RPlayerMoveEvent;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.handlers.EntityHandler;
import me.pmilon.RubidiaCore.handlers.GamePlayEffectsHandler;
import me.pmilon.RubidiaCore.handlers.HealthBarHandler;
import me.pmilon.RubidiaCore.handlers.NetherHandler;
import me.pmilon.RubidiaCore.handlers.PlaymodeHandler;
import me.pmilon.RubidiaCore.handlers.ResourcePackHandler;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.levels.RLevelListener;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerPlayerListHeaderFooter;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerWindowItems;
import me.pmilon.RubidiaCore.ranks.Ranks;
import me.pmilon.RubidiaCore.ritems.general.ItemListener;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.general.RItemStack;
import me.pmilon.RubidiaCore.ritems.general.RItemStacks;
import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.ritems.weapons.REnchantment;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.ritems.weapons.WeaponsListener;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.ScrollType;
import me.pmilon.RubidiaCore.tags.NameTags;
import me.pmilon.RubidiaCore.tags.TagStandListener;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.AnvilUI;
import me.pmilon.RubidiaCore.ui.DistinctionsMenu;
import me.pmilon.RubidiaCore.ui.EnchantmentUI;
import me.pmilon.RubidiaCore.ui.EnderChestUI;
import me.pmilon.RubidiaCore.ui.PlayerMenu;
import me.pmilon.RubidiaCore.ui.PrefsUI;
import me.pmilon.RubidiaCore.ui.SPlayerManager;
import me.pmilon.RubidiaCore.ui.SPlayerSelectionMenu;
import me.pmilon.RubidiaCore.ui.SkillTree;
import me.pmilon.RubidiaCore.ui.managers.UIManager;
import me.pmilon.RubidiaCore.ui.weapons.WeaponsUI;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.JSONUtils;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaPets.PetsPlugin;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaWG.Flags;
import me.pmilon.RubidiaWG.WGUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Core extends JavaPlugin implements Listener {

	public static ConsoleCommandSender console;

	public static WorldEditPlugin we;
	public static WorldGuardPlugin wg;

	public static UIManager uiManager;
	public static HealthBarHandler barHandler;
	public static ItemMessage itemMessage;
	public static RPlayerColl rcoll;
	public static CoupleColl coupleColl;

	public static FileConfiguration playerConfig = null;
	public static File playerConfigFile = null;
	public static FileConfiguration backpackConfig = null;
	public static File backpackConfigFile = null;
	public static FileConfiguration cityConfig = null;
	public static File cityConfigFile = null;
	public static FileConfiguration database = null;
	public static File databaseFile = null;
	public static FileConfiguration pathConfig = null;
	public static File pathConfigFile = null;
	public static FileConfiguration weaponConfig = null;
	public static File weaponConfigFile = null;
	public static FileConfiguration coupleConfig = null;
	public static File coupleConfigFile = null;

	public static Core instance;
	public HashMap<Player, Block> anvil = new HashMap<Player, Block>();
	public static List<Player> drunk = new ArrayList<Player>();
	public static List<Player> glitch = new ArrayList<Player>();
	public static HashMap<Player, List<BukkitTask>> pesanteur = new HashMap<Player, List<BukkitTask>>();
	public static List<Material> pesTypes = Arrays.asList(Material.LADDER, Material.LAVA, Material.WATER);
	public static boolean restarting = false;
	private static int playersMax = 1;
	
	public static final String firstMotd = "§r                       §8play.§9§lRubidia§8.fr";

	////////////////////////////////////////
	// MAIN PLUGIN BEHAVIOR //
	////////////////////////////////////////

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		final String s = p.getName();

		if (Core.isInMaintenance()) {
			if (!p.isOp()
					&& !Bukkit.getServer().getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(p.getUniqueId()))) {
				p.kickPlayer("§4§lR§4ubidia §cest en §4maintenance");
				return;
			}
		}

		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

		final RPlayer rp;
		if (!rcoll.contains(p)) {
			rp = rcoll.addDefault(p.getUniqueId().toString());
			new BukkitTask(this) {
				public void run() {
					Bukkit.broadcastMessage("§2§lBIENVENUE §a§l" + s + " §2§lSUR RUBIDIA");
				}

				@Override
				public void onCancel() {
				}
			}.runTaskLater(0);
			p.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD, 1));
			p.getInventory().addItem(new ItemStack(Material.EMERALD, 20));
			p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 4));
			p.getInventory().addItem(RItemStacks.STAR_STONE.getItemStack());
			for (RPlayer rpp : rcoll.data()) {
				if (!rpp.equals(rp)) {
					rpp.lastWelcome = rp;
				}
			}
		} else
			rp = RPlayer.get(p);
		RPlayer.getOnlines().add(rp);
		rp.setPlayer(p);
		rp.setName(p.getName());
		final boolean gamemode = rp.isOp();
		if (rp.getLastConnectionDate() < Core.database.getLong("lastReset")) {
			p.getInventory().clear();
		}

		new BukkitTask(this) {
			public void run() {
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10F);
				rp.refreshRLevelDisplay();
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(2);

		WrapperPlayServerPlayerListHeaderFooter packet = new WrapperPlayServerPlayerListHeaderFooter();
		packet.setHeader(WrappedChatComponent.fromText("§a§l§m                                     §r\n§oBienvenue sur Rubidia !§r\n§a§l§m                                     §r"));
		packet.setFooter(WrappedChatComponent.fromText("§c§l§m                                     §r\n§lhttps://www.rubidia.fr§r\n§c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r §c§l-§r"));
		packet.sendPacket(p);

		new BukkitTask(this) {
			public void run() {
				for (RPlayer rpo : RPlayer.getOnlines()) {
					if (rpo.knows(rp)) {
						if (rpo.getNotifOnFriendJoin()) {
							rpo.getPlayer().playSound(rpo.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10F);
							rpo.sendMessage("§e" + s + " §avient de se connecter !");
						}
					}
				}

				NameTags.update();

				if (!rp.isOp() && rp.getBank() > 120 * (rp.getRLevel() + 1)) {
					for (RPlayer rpo : RPlayer.getOnlines()) {
						if (rpo.isOp()) {
							rpo.sendMessage("§4" + rp.getName() + " §ca trop d'émeraudes pour son niveau (§4"
									+ rp.getBank() + "§c)...");
						}
					}
				}

				if (gamemode) p.setGameMode(GameMode.CREATIVE);

				new BukkitTask(this.getPlugin()) {
					public void run() {						
						if (rp.getGamingTime() < 40 * 1000L) {
							p.teleport(Core.getSpawn());
						}

						Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
						if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
							rp.sendTitle("§5§lSAMED'XP DE FOLIE", "§dXP ×2", 0, 100, 40);
						} else if (!Events.currentEvents.isEmpty()) {
							for (Event event : Events.currentEvents) {
								if (event.isActive()) {
									rp.sendTitle("§5§lEVENEMENT EN COURS", "§d" + event.getSubtitle(), 0, 100, 40);
								}
							}
						}

						rp.sendMessage("§eInstallation de §6§lRubidiaPack§e (v" + ResourcePackHandler.RESOURCE_PACK_VERSION + ")...");
						rp.getPlayer().setResourcePack("http://r.milon.pro/downloads/RubidiaPack" + ResourcePackHandler.RESOURCE_PACK_VERSION + ".zip");
						if (!rp.isProfileUpdated()) rp.sendMessage("§dMettez à jour votre profil de joueur : §l/profile");

						if (!rp.isVip() && rp.getLastLoadedSPlayerId() == 3) {
							Core.uiManager.requestUI(new SPlayerSelectionMenu(p));
						}
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(20);
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(4);

		if (rp.isVanished()) {
			rp.sendMessage("§7§oVous êtes invisible !");
			for (RPlayer rpp : RPlayer.getOnlines()) {
				if (!rpp.isOp() && !rpp.equals(rp))
					rpp.getPlayer().hidePlayer(this, p);
			}
		}

		if (RPlayer.getOnlines().size() > playersMax) {
			playersMax = RPlayer.getOnlines().size();
		}

		p.setWalkSpeed(Settings.DEFAULT_WALK_SPEED);// to reset speed

		RChatListener.onPlayerJoin(e, rp);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);

		if (rp != null) {
			if (Smiley.isSmileying(p))
				rp.smileyTask.run();
			rp.getLoadedSPlayer().setLoaded(false);
			rp.setLastConnectionDate(System.currentTimeMillis());
			RPlayer.getOnlines().remove(rp);
			rp.setPlayer(null);
		}

		if (p.getVehicle() != null) {
			p.leaveVehicle();
		}

		for (Objective objective : p.getScoreboard().getObjectives()) {
			objective.unregister();
		}
		for (Team team : p.getScoreboard().getTeams()) {
			team.unregister();
		}

		RChatListener.onPlayerQuit(e, rp);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		RPlayer rp = RPlayer.get(p);
		e.setKeepLevel(true);
		e.setKeepInventory(true);
		e.setDroppedExp(0);
		e.getDrops().clear();

		RPlayer killer = null;
		if (p.getKiller() != null) killer = RPlayer.get(p.getKiller());
		RPlayerDeathEvent event = new RPlayerDeathEvent(e, rp, killer, new ArrayList<ItemStack>(), p.getInventory().getContents(), p.getInventory().getArmorContents());
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			rp = event.getRPlayer();
			p = event.getRPlayer().getPlayer();
			if (Smiley.isSmileying(p)) rp.smileyTask.run();
			if (!rp.isInDuel()) {
				if (!event.isKeepInventory()) {
					ItemStack[] contents = p.getInventory().getContents();
					for (int i = 0; i < contents.length; i++) {
						if (i == 8 || i == 17 && event.getRPlayer().getRClass().equals(RClass.RANGER))
							continue;
						if (contents[i] != null) {
							if (event.getInventoryDrops().length > i) {
								if (contents[i].equals(event.getInventoryDrops()[i])) {
									event.getDrops().add(contents[i]);
									contents[i] = null;
								}
							}
						}
					}
					p.getInventory().setContents(contents);
					
					ItemStack[] contents1 = p.getInventory().getArmorContents();
					for (int i = 0; i < contents1.length; i++) {
						if (contents1[i] != null) {
							if (event.getArmorDrops().length > i) {
								if (contents1[i].equals(event.getArmorDrops()[i])) {
									event.getDrops().add(contents1[i]);
									contents1[i] = null;
								}
							}
						}
					}
					p.getInventory().setArmorContents(contents1);
					
					for (ItemStack item : event.getDrops()) {
						if (!item.getType().equals(Material.AIR)) {
							p.getWorld().dropItemNaturally(p.getLocation(), item);
						}
					}
				}
				if (event.getKiller() != null) event.getKiller().setKills(event.getKiller().getKills() + 1);
			}

			rp.setLastCombat(System.currentTimeMillis() - 4444 * 1000);
		} else {
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - .001);
		}

		event.getSuperEvent().setDeathMessage("");// to avoid custom chat bugs >> see RChatListener
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onRPlayerDeath(RPlayerDeathEvent event) {
		final RPlayer rp = event.getRPlayer();
		if (!event.isCancelled()) {
			if (rp.isOnline()) {
				final Player p = rp.getPlayer();

				if (!rp.isInDuel()) {
					if (rp.getResurrectionTask() == null) {
						for (ItemStack item : p.getInventory().getContents()) {
							if (item != null) {
								RItem rItem = new RItem(item);
								if (rItem.isScroll()) {
									if (rItem.getScroll().getType().equals(ScrollType.RESURRECTION)) {
										event.setCancelled(true);
										p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 180, 255, true, false));
										p.setHealth(.01);
										p.teleport(p.getLocation().add(0, .3, 0));
										p.setAllowFlight(true);
										p.setFlying(true);
										p.setFlySpeed(0F);
										p.setWalkSpeed(0);
										p.setVelocity(new Vector(0, 0, 0));
										for (Player player : Bukkit.getOnlinePlayers()) {
											player.hidePlayer(this, p);
										}
										p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2, 1);
										rp.setResurrectionTask(new BukkitTask(this) {
											int step = 8;

											@Override
											public void run() {
												p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5, .8F);
												rp.sendTitle(("§6Utilisez votre parchemin de résurrection !"),
														(step > 3 ? "§e" : (step > 1 ? "§c" : "§4")) + step + "...", 0,
														25, 0);
												step--;
											}

											@Override
											public void onCancel() {
												if (step == 0)
													p.damage(4444.0);
											}

										}.runTaskTimerCancelling(0, 20, 160));
										break;
									}
								}
							}
						}
					} else {
						rp.sendTitle("", "", 0, 1, 0);
						p.setFlying(false);
						p.setAllowFlight(false);
						p.setFlySpeed(Settings.DEFAULT_FLY_SPEED);
						p.setWalkSpeed(Settings.DEFAULT_WALK_SPEED);
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.showPlayer(this, p);
						}
						p.removePotionEffect(PotionEffectType.BLINDNESS);
						rp.getResurrectionTask().cancel();
						rp.setResurrectionTask(null);
						if (p.getKiller() != null) {
							RPlayer rpp = RPlayer.get(p.getKiller());
							rpp.setKills(rpp.getKills() + 1);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onFlightAttempt(PlayerToggleFlightEvent event) {
		if (!event.isFlying()) {
			if (RPlayer.get(event.getPlayer()).getResurrectionTask() != null) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);

		if (e.getFrom().distanceSquared(e.getTo()) > LocationUtils.MOVE_DISTANCE_MIN_SQUARED) {
			RPlayerMoveEvent event = new RPlayerMoveEvent(rp, e);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				e.setCancelled(true);
			}
		}

		if (rp.isLoading()) {
			e.setCancelled(true);
		}

		if (drunk.contains(p)) {
			Random r = new Random();
			Vector v = new Vector(r.nextDouble() * 2 - 1, 0, r.nextDouble() * 2 - 1);
			p.setVelocity(v.normalize().multiply(.23));
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent ie) {
		final Player p = (Player) ie.getWhoClicked();
		final RPlayer rp = RPlayer.get(p);

		int slot = ie.getSlot();// do not put raw slot
		ItemStack is = ie.getCurrentItem();
		if (is != null) {
			RItem rItem = new RItem(is);
			if (ie.getClickedInventory().getType().equals(InventoryType.CRAFTING)) {
				if (is.getType().equals(Material.BOOKSHELF)) {
					ItemStack skt = ie.getCurrentItem();
					if (skt.hasItemMeta()) {
						if (skt.getItemMeta().getDisplayName().contains("Character menu")
								|| skt.getItemMeta().getDisplayName().contains("Menu du personnage")) {
							ie.setCancelled(true);
							Core.uiManager.requestUI(new SPlayerManager(p));
						}
					}
				}
			} else if (p.getOpenInventory().getTopInventory().getType().equals(InventoryType.ANVIL)) {
				if (rItem.isScroll() || rItem.isCustom() || rItem.isWeapon()) {
					ie.setCancelled(true);
					rp.sendMessage("§cVous ne pouvez travailler cet item !");
				}

				if (ie.getClickedInventory().getType().equals(InventoryType.ANVIL)) {
					if (ie.isShiftClick()) {
						ie.setCancelled(true);
					} else {
						if (ie.getRawSlot() == 2) {
							if (ie.getCurrentItem() != null) {
								if (!ie.getCurrentItem().getType().equals(Material.AIR)) {
									ie.setCancelled(true);
									ItemStack is_0 = ie.getClickedInventory().getItem(0);
									ItemStack is_1 = ie.getClickedInventory().getItem(1);
									ItemStack is_2 = ie.getClickedInventory().getItem(2);
									ie.getClickedInventory().setItem(0, new ItemStack(Material.AIR, 1));
									ie.getClickedInventory().setItem(1, new ItemStack(Material.AIR, 1));
									p.closeInventory();
									RItem rItem1 = new RItem(is_0);
									if (rItem1.isWeapon()) {
										Weapon weapon = rItem1.getWeapon();
										ItemMeta meta = is_2.getItemMeta();
										meta.setDisplayName(weapon.getRarity().getPrefix() + "§l" + weapon.getName()
												+ (weapon.getSuppLevel() > 0 ? " §7(+" + weapon.getSuppLevel() + ")"
														: ""));
										is_2.setItemMeta(meta);
									}
									uiManager.requestUI(new AnvilUI(p, anvil.get(p), is_0, is_1, is_2));
								}
							}
						}
					}
				}
			}

			if (ie.getSlotType().equals(SlotType.ARMOR) && ie.getCurrentItem().getType().equals(Material.PLAYER_HEAD)
					&& Smiley.isSmileying(p))
				ie.setCancelled(true);
		}
		if (!rp.isOp() && ie.getClickedInventory() != null) {
			if (ie.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
				if (ie.getCursor() != null) {
					ItemStack item = ie.getCursor();
					if (item.getType().equals(Material.BLACK_STAINED_GLASS_PANE) && item.hasItemMeta()) {
						ItemMeta meta = item.getItemMeta();
						if (meta.hasDisplayName()) {
							if (meta.getDisplayName().equals("vigor")) {
								ie.setCancelled(true);
							}
						}
					}
				}
			} else if (ie.getClickedInventory().equals(p.getOpenInventory().getBottomInventory())) {
				if (rp.getRClass().equals(RClass.RANGER) && slot == 17 || slot == 8) {
					ie.setCancelled(true);
				}
			}
		}
		if (!rp.isOp())
			Utils.updateInventory(p);
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		new BukkitTask(this) {

			@Override
			public void run() {
				rp.updateVigor();
			}

			@Override
			public void onCancel() {
			}

		}.runTaskLater(0);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		new BukkitTask(this) {

			@Override
			public void run() {
				rp.updateVigor();
			}

			@Override
			public void onCancel() {
			}

		}.runTaskLater(0);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rubis")) {
			if (sender.isOp()) {
				if (args.length > 1) {
					RPlayer rp = RPlayer.getFromName(args[0]);
					if (rp != null) {
						if (Utils.isInteger(args[1])) {
							int amount = Integer.valueOf(args[1]);
							rp.setPendingRubis(rp.getPendingRubis() + amount);
							if (rp.isOnline()) {
								rp.sendMessage("§aVous avez reçu §e" + amount
										+ " §arubis utilisables dans la boutique du site !");
								rp.getPlayer().sendMessage("§2§l>>>    §7http://www.rubidia.pw/shop/");
								rp.sendMessage("§e§o(Reconnectez-vous sur le site pour mettre à jour votre compte)");
							}
							sender.sendMessage("§2" + rp.getName() + " §areceived §e" + amount + " §arubis.");
						} else
							sender.sendMessage("§cPlease use /rubis " + args[0] + " [amount]");
					} else
						sender.sendMessage("§4" + args[0] + " §ccouldn't be found.");
				} else
					sender.sendMessage("§cPlease use /rubis [player] [amount]");
			} else
				sender.sendMessage("§cYou really thought you could do that without being Operator?");
		} else if (cmd.getName().equalsIgnoreCase("maintenance")) {
			if (sender.isOp()) {
				if (Core.isInMaintenance()) {
					Configs.getDatabase().set("maintenance.active", false);
					Configs.getDatabase().set("maintenance.message", null);
					sender.sendMessage("§eMaintenance terminée");
					for (RPlayer rp : RPlayer.getOnlines()) {
						rp.sendTitle("§6Maintenance terminée", "§eLe jeu reprend son cours", 0, 150, 5);
					}
				} else {
					Configs.getDatabase().set("maintenance.active", true);
					if (args.length > 0) {
						Configs.getDatabase().set("maintenance.message", StringUtils.join(args, " "));
					}
					sender.sendMessage("§eMaintenance débutée");
					for (RPlayer rp : RPlayer.getOnlines()) {
						rp.sendTitle((rp.isOp() ? "§6" : "§4") + "Maintenance",
								(rp.isOp() ? "§eDébutée par " + sender.getName()
										: "§eVous serez expulsé dans 30 secondes"),
								0, 150, 5);
					}
					Bukkit.getScheduler().runTaskLater(this, new Runnable() {
						public void run() {
							if (Core.isInMaintenance()) {
								for (RPlayer rp : RPlayer.getOnlines()) {
									if (!rp.isOp()) {
										rp.getPlayer().kickPlayer("§4§lR§4ubidia §cest en §4maintenance");
									}
								}
							}
						}
					}, 30 * 20L);
				}
				Bukkit.getServer().setWhitelist(Core.isInMaintenance());
			} else
				sender.sendMessage("§cYou really thought you could do that without being Operator!");
		} else if (cmd.getName().equalsIgnoreCase("tp")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				RPlayer rp = RPlayer.get(player);
				if (args.length < 1) {
					if (TeleportHandler.invoke_tasks.containsKey(rp)) {
						BukkitTask.tasks.get(TeleportHandler.invoke_tasks.get(rp)).cancel();
						return true;
					}
					if (TeleportHandler.tp_tasks.containsKey(rp)) {
						BukkitTask.tasks.get(TeleportHandler.tp_tasks.get(rp)).cancel();
						return true;
					}
				}
			}
			if (sender.isOp()) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					RPlayer rp = RPlayer.get(p);
					if (args.length == 1) {
						if (Bukkit.getPlayer(args[0]) != null) {
							TeleportHandler.teleport(p, Bukkit.getPlayer(args[0]).getLocation());
						} else {
							rp.sendMessage("§4" + args[0] + " §cdoit être en ligne !");
						}
					} else if (args.length == 2) {
						if (Bukkit.getPlayer(args[0]) != null) {
							if (Bukkit.getPlayer(args[1]) != null) {
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]),
										Bukkit.getPlayer(args[1]).getLocation());
							} else {
								rp.sendMessage("§4" + args[1] + " §cdoit être en ligne !");
							}
						} else {
							rp.sendMessage("§4" + args[0] + " §cdoit être en ligne !");
						}
					} else if (args.length == 3) {
						try {
							TeleportHandler.teleport(p,
									new Location(p.getWorld(), Integer.valueOf(args[0]), Integer.valueOf(args[1]),
											Integer.valueOf(args[2]), p.getEyeLocation().getYaw(),
											p.getEyeLocation().getPitch()));
						} catch (Exception e) {
							rp.sendMessage("§cVous n'avez pas donné une location exacte !");
						}
					} else if (args.length == 4) {
						try {
							if (Bukkit.getPlayer(args[0]) != null) {
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]),
										new Location(p.getWorld(), Integer.valueOf(args[1]), Integer.valueOf(args[2]),
												Integer.valueOf(args[3]),
												Bukkit.getPlayer(args[0]).getEyeLocation().getYaw(),
												Bukkit.getPlayer(args[0]).getEyeLocation().getPitch()));
							} else {
								rp.sendMessage("§4" + args[0] + " §cdoit être en ligne !");
							}
						} catch (Exception e) {
							rp.sendMessage("§cVous n'avez pas donné une location exacte !");
						}
					} else {
						rp.sendMessage("§cUtilisez /tp <Cible> | <Joueur> <Cible> | <x y z> | <x y z (Cible)>");
					}
				} else {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[0]) != null) {
							if (Bukkit.getPlayer(args[1]) != null) {
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]),
										Bukkit.getPlayer(args[1]).getLocation());
							} else {
								sender.sendMessage("§4" + args[1] + " §cmust be online!");
							}
						} else {
							sender.sendMessage("§4" + args[0] + " §cmust be online!");
						}
					} else if (args.length == 4) {
						try {
							if (Bukkit.getPlayer(args[0]) != null) {
								TeleportHandler.teleport(Bukkit.getPlayer(args[0]),
										new Location(Bukkit.getPlayer(args[0]).getWorld(), Integer.valueOf(args[1]),
												Integer.valueOf(args[2]), Integer.valueOf(args[3]),
												Bukkit.getPlayer(args[0]).getEyeLocation().getYaw(),
												Bukkit.getPlayer(args[0]).getEyeLocation().getPitch()));
							} else {
								sender.sendMessage("§4" + args[0] + " §cmust be online!");
							}
						} catch (Exception e) {
							sender.sendMessage("§cYou did not give a valid location!");
						}
					} else {
						sender.sendMessage(
								"§cPlease use /tp <Target> | <Player> <Target> | <x y z> | <Player> <x y z (Target)>");
					}
				}
			} else {
				sender.sendMessage("§cYou really thought you could do that without being Operator!");
			}
		} else if (cmd.getName().equalsIgnoreCase("top")) {
			sender.sendMessage(
					"§6Allocated memory: §a" + Math.round((float) (Runtime.getRuntime().maxMemory()) / 1000) + " MB");
			sender.sendMessage("§6Used memory: §a" + Math.round((float) (Runtime.getRuntime().freeMemory()) / 1000)
					+ " MB §e("
					+ Math.round(((float) (Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory()) * 100)
					+ "%)");
		}

		if (sender instanceof Player) {
			final Player p = (Player) sender;
			final RPlayer rp = RPlayer.get(p);
			if (Core.uiManager.isInTempSession(p) && !rp.isOp()) {
				rp.sendMessage("§cVous ne pouvez faire ça tant que vous êtes en mode d'édition !");
				return true;
			}
			if (rp.getResurrectionTask() != null && !rp.isOp()) {
				rp.sendMessage("§cVous ne pouvez faire ça tant que vous vous ressuscitez !");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("skilltree")) {
				uiManager.requestUI(new SkillTree(p));
			} else if (cmd.getName().equalsIgnoreCase("nremove")) {
				p.setMetadata("removingEntity", new FixedMetadataValue(this, true));
				rp.sendMessage("§cVous supprimerez la prochaine entité touchée.");
			} else if (cmd.getName().equalsIgnoreCase("playmode")) {
				if (p.isOp()) {
					if (!p.getGameMode().equals(GameMode.CREATIVE)) {
						PlaymodeHandler.savePlaymodeSurvivalInventory(p);
						p.setGameMode(GameMode.CREATIVE);
						rp.sendMessage("§eVous êtes désormais en mode Admin !");
						p.getInventory().clear();
						PlaymodeHandler.setPlaymodeCreativeInventory(p);
					} else if (p.getGameMode().equals(GameMode.CREATIVE)) {
						PlaymodeHandler.savePlaymodeCreativeInventory(p);
						p.setGameMode(GameMode.SURVIVAL);
						rp.sendMessage("§eVous êtes désormais en mode Joueur !");
						p.getInventory().clear();
						PlaymodeHandler.setPlaymodeSurvivalInventory(p);
					}
				} else {
					rp.sendMessage("§cCette commande est réservée aux admins !");
				}
			} else if (cmd.getName().equalsIgnoreCase("setcity")) {
				if (p.isOp()) {
					if (args.length == 1) {
						Configs.getCitiesConfig().set("cities." + args[0] + ".location", p.getLocation());
					} else {
						rp.sendMessage("§cUtilisez /setcity [nom]");
					}
				} else {
					rp.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être Opérateur !");
				}
			} else if (cmd.getName().equalsIgnoreCase("city")) {
				if (p.isOp()) {
					if (args.length == 1) {
						if (Configs.getCitiesConfig().contains("cities." + args[0])) {
							TeleportHandler.teleport(p, (Location) Configs.getCitiesConfig()
									.get("cities." + args[0] + ".location", p.getLocation()));
						} else {
							rp.sendMessage("§cAucune cité ne se nomme §4" + args[0] + " §c!");
						}
					} else {
						rp.sendMessage("§cUtilisez /city [nom]");
					}
				} else {
					rp.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être Opérateur !");
				}
			} else if (cmd.getName().equalsIgnoreCase("beer")) {
				if (p.isOp()) {
					ItemStack beer = new ItemStack(Material.POTION);
					ItemMeta beerm = beer.getItemMeta();
					beerm.setDisplayName(("§rBière"));
					beerm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
					beer.setItemMeta(beerm);
					p.getInventory().addItem(beer);
				} else
					rp.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être Opérateur !");
			} else if (cmd.getName().equalsIgnoreCase("prefs")) {
				Core.uiManager.requestUI(new PrefsUI(p));
			} else if (cmd.getName().equalsIgnoreCase("tutorial")) {
				TeleportHandler.startTeleportation(rp, Bukkit.getWorld("Tutorial").getSpawnLocation(),
						new RTeleportCause(RTeleportType.DELAYED_TELEPORTATION, null, null, null));
			} else if (cmd.getName().equalsIgnoreCase("weapons")) {
				// if(p.isOp()){
				Core.uiManager.requestUI(new WeaponsUI(p));
				// }else rp.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être
				// Opérateur !");
			} else if (cmd.getName().equalsIgnoreCase("character")) {
				Core.uiManager.requestUI(new DistinctionsMenu(p));
			} else if (cmd.getName().equalsIgnoreCase("invsee")) {
				if (rp.isOp()) {
					if (args.length > 0) {
						Player player = Bukkit.getPlayer(args[0]);
						if (player != null) {
							p.openInventory(player.getInventory());
						} else
							rp.sendMessage("§cImpossible de trouver un joueur avec le nom §4" + args[0]);
					} else
						rp.sendMessage("§cUtilisez /invsee [joueur]");
				} else
					rp.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être opérateur ?");
			} else if (cmd.getName().equalsIgnoreCase("play")) {
				Core.uiManager.requestUI(new SPlayerSelectionMenu(p));
			} else if (cmd.getName().equalsIgnoreCase("glitch")) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("tp")) {
						if (glitch.contains(p)) {
							glitch.remove(p);
							p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
							return true;
						}
					}
				}
				rp.getChat()
						.addFixDisplay(new RChatFixDisplay(rp, -1, null).addLines(("§a§l          AIDE GLITCH"),
								("     §6Dans un moment, vous recevrez un second message de ce type."),
								("   §eVous pourrez alors être instantanément téléporté à Mearwood.")));
				new BukkitTask(this) {

					@Override
					public void run() {
						RChatFixDisplay fixDisplay = new RChatFixDisplay(rp, 100, null).addLines(
								("§a§l          AIDE GLITCH"), ("   §eCliquez sur le bouton suivant."),
								("     §4ATTENTION! §cVous n'avez que 5 secondes pour l'utiliser !"), "");
						TextComponent tp = new TextComponent(("§2[§aSORTEZ-MOI DE CE MACHIN§2]"));
						ClickEvent tpEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/glitch tp");
						tp.setClickEvent(tpEvent);
						TextComponent text = new TextComponent("                  ");
						text.addExtra(tp);
						fixDisplay.addText(text);
						fixDisplay.addLine("");
						rp.getChat().addFixDisplay(fixDisplay);
						glitch.add(p);
						new BukkitTask(this.getPlugin()) {

							@Override
							public void run() {
								glitch.remove(p);
							}

							@Override
							public void onCancel() {
							}

						}.runTaskLater(5 * 20);
					}

					@Override
					public void onCancel() {
					}

				}.runTaskLater(RandomUtils.random.nextInt(6 * 20) + 9 * 20);
			}
			return true;
		}
		return false;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getHand() != null) {
			if (e.getHand().equals(EquipmentSlot.HAND)) {
				final Player p = e.getPlayer();
				final RPlayer rp = RPlayer.get(p);

				if (e.getAction().toString().contains("RIGHT_CLICK")) {
					ItemStack item = p.getEquipment().getItemInMainHand();
					RItem rItem = new RItem(item);
					if (rItem.isScroll()) {
						e.setCancelled(true);
						Scroll scroll = rItem.getScroll();
						if (scroll.use(p)) {
							item.setAmount(item.getAmount() - 1);
							if (item.getAmount() < 1)
								p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
						}
						Utils.updateInventory(p);
					} else if (item.getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) {
						e.setCancelled(true);
						rp.sendMessage("§cLes pommes d'or aux pouvoirs surpuissants sont interdites.");
					} else {
						for (RItemStack stack : RItemStacks.ITEMS) {
							if (stack.getItemStack().isSimilar(item)) {
								e.setCancelled(true);
								break;
							}
						}
					}

					if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
						final Block clicked = e.getClickedBlock();
						if (clicked.getType().equals(Material.ENCHANTING_TABLE)) {
							if (!e.useInteractedBlock().equals(Result.DENY)) {
								e.setCancelled(true);
								if (!WGUtils.testState(p, clicked.getLocation(), Flags.BLOCKS)) {
									return;
								}

								if (!p.getEquipment().getItemInMainHand().getType().equals(Material.AIR))
									uiManager.requestUI(new EnchantmentUI(clicked.getLocation().add(.5, 0, .5), p));
								else
									rp.sendMessage(
											"§cPrenez un item dans vos main pour utiliser la table d'enchantement.");
							}
						} else if (clicked.getType().equals(Material.BOOKSHELF) && !(p.isSneaking())) {
							e.setCancelled(true);
							uiManager.requestUI(new SPlayerManager(p));
						} else if (clicked.getType().equals(Material.ANVIL)) {
							anvil.put(p, e.getClickedBlock());
						} else if (clicked.getType().equals(Material.ENDER_CHEST)) {
							if (!e.useInteractedBlock().equals(Result.DENY)) {
								e.setCancelled(true);
								if (!p.isSneaking()) {
									Core.uiManager.requestUI(new EnderChestUI(p, clicked));
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		if (e.getHand() != null) {
			if (e.getHand().equals(EquipmentSlot.HAND)) {
				Player player = e.getPlayer();
				RPlayer rp = RPlayer.get(player);
				Entity en = e.getRightClicked();
				if (en instanceof Player) {
					if (!rp.isInCombat()) {
						Player target = (Player) en;
						if (player.isSneaking() && !DialogManager.isInDialog(player)) {
							Core.uiManager.requestUI(new PlayerMenu(player, target));
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity ec = e.getDamager();
		Entity ed = e.getEntity();

		if (ec instanceof Player) {
			Player pc = (Player) ec;
			if (!(ed instanceof Player)) {
				if (pc.hasMetadata("removingEntity")) {
					ed.remove();
					pc.removeMetadata("removingEntity", this);
				}
			}
		} else if (ec instanceof Firework) {
			e.setCancelled(true);
		}

		if (ed instanceof Player) {
			Player pd = (Player) ed;
			if (RPlayer.get(pd).getResurrectionTask() != null) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityCombust(EntityCombustEvent e) {
		Entity en = e.getEntity();
		if (en instanceof LivingEntity) {
			if (en.getType().equals(EntityType.ZOMBIE) && en.getCustomName() != null
					&& Monsters.get((LivingEntity) en) == null) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		ItemStack is = e.getCurrentItem();
		HumanEntity he = e.getWhoClicked();
		if (he instanceof Player) {
			Player p = (Player) he;
			RPlayer rp = RPlayer.get(p);

			if (is != null) {
				if (is.getType().equals(Material.SHIELD) && is.hasItemMeta()) {
					e.setCancelled(true);
					rp.sendMessage("§cVous ne pouvez pour le moment pas personnaliser votre bouclier.");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (!e.isCancelled()) {
			if (!p.getGameMode().equals(GameMode.CREATIVE)) {
				ItemStack inHand = p.getEquipment().getItemInMainHand();
				if (inHand.getType().equals(Material.GOLDEN_PICKAXE) || inHand.getType().equals(Material.IRON_PICKAXE)
						|| inHand.getType().equals(Material.DIAMOND_PICKAXE)) {
					if (!inHand.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
						if (b.getType().equals(Material.EMERALD_ORE)) {
							Random r = new Random();
							int drops = r.nextInt(4);
							for (int i = 1; i <= drops; i++) {
								b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.EMERALD, 1));
							}
						}
					}
				}
			}
		}
		e.setExpToDrop(0);
	}

	@EventHandler
	public void onBeerDrink(PlayerItemConsumeEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if (item.getType().equals(Material.POTION)) {
			if (item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				if (meta.hasDisplayName()) {
					if (meta.getDisplayName().contains("§rBeer") || meta.getDisplayName().contains("§rBière")) {
						Bukkit.getScheduler().runTaskLater(this, new Runnable() {
							public void run() {
								p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
							}
						}, 2);
						Utils.addSafeBuff(p, new PotionEffect(PotionEffectType.CONFUSION, 30 * 20, 1, true, true));
						Utils.addSafeBuff(p, new PotionEffect(PotionEffectType.SLOW, 30 * 20, 1, true, true));
						Core.drunk.add(p);
						Bukkit.getScheduler().runTaskLater(this, new Runnable() {
							public void run() {
								if (Core.drunk.contains(p))
									Core.drunk.remove(p);
							}
						}, 30 * 20);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.isSneaking()) {
			RPlayer rp = RPlayer.get(player);
			Item item = event.getItemDrop();
			RItem rItem = new RItem(item.getItemStack());
			if (rItem.isWeapon()) {
				Weapon weapon = rItem.getWeapon();
				if (weapon.canUse(rp).isEmpty()) {
					event.setCancelled(true);
					Core.uiManager.requestUI(new DistinctionsMenu(player));
				}
			}
		}
	}

	@EventHandler
	public void onPing(ServerListPingEvent event) {
		event.setMaxPlayers(playersMax + 1);
		if (RandomUtils.random.nextBoolean()) {
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				event.setMotd("             §5>>>  §d§lSAMED'XP DE FOLIE§5  <<<\n                             §dXP ×2");
				return;
			} else if (!Events.currentEvents.isEmpty()) {
				for (Event e : Events.currentEvents) {
					if (e.isActive()) {
						event.setMotd("           §5>>>  §d§lEVENEMENT EN COURS§5  <<<");
						return;
					}
				}
			}
		}
		event.setMotd(Core.firstMotd + "\n        §e<§e§l  §6§lO§e§lpen-§6§lW§e§lorld §6§lA§e§ldventure §6§lRPG  §e>");
	}

	@EventHandler
	public void onManipulate(PlayerArmorStandManipulateEvent e) {
		if (e.getRightClicked().isMarker() && !e.getPlayer().isOp())
			e.setCancelled(true);
	}

	@EventHandler
	public void onItemChange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		RPlayer rp = RPlayer.get(player);
		int slot = event.getNewSlot();
		if (!rp.isOp()) {
			if (slot == 8) {
				if (rp.isUsingCycle()) {
					for (int i = 0; i < 8; i++) {
						ItemStack hold = player.getInventory().getItem(i);
						for (int z = 3; z > 0; z--) {
							int prev = i + z * 9;
							int next = (prev + 9) % 36;
							player.getInventory().setItem(next, player.getInventory().getItem(prev));
						}
						player.getInventory().setItem(i + 9, hold);
					}
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockChange(EntityChangeBlockEvent e) {
		Entity entity = e.getEntity();
		Block block = e.getBlock();
		if (block != null) {
			if (block.getType().equals(Material.FARMLAND)) {
				if (entity instanceof LivingEntity) {
					EntityEquipment equipment = ((LivingEntity) entity).getEquipment();
					ItemStack item = equipment.getBoots();
					if (item != null) {
						if (item.hasItemMeta()) {
							ItemMeta meta = item.getItemMeta();
							if (meta.hasEnchant(Enchantment.PROTECTION_FALL)) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		final Player player = event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		if (!player.getGameMode().equals(event.getNewGameMode())) {
			if (event.getNewGameMode().equals(GameMode.SURVIVAL) || event.getNewGameMode().equals(GameMode.CREATIVE)) {
				new BukkitTask(this) {

					@Override
					public void run() {
						NameTags.update();
						if (rp.isOp()) {
							for (RPlayer rpp : RPlayer.getOnlines()) {
								if (rpp.isVanished() && !rpp.equals(rp)) {
									player.showPlayer(Core.instance, rpp.getPlayer());
								}
							}
						} else {
							for (RPlayer rpp : RPlayer.getOnlines()) {
								if (rpp.isVanished() && !rpp.equals(rp)) {
									player.hidePlayer(Core.instance, rpp.getPlayer());
								}
							}
						}
					}

					@Override
					public void onCancel() {
					}

				}.runTaskLater(0);
			}
		}
	}

	////////////////////////////////////////
	// CUSTOM METHODS //
	////////////////////////////////////////

	public static List<Player> toPlayerList(List<? extends Entity> near) {
		Stream<? extends Entity> players = near.stream().filter((Entity entity) -> entity instanceof Player);
		return players.map((Entity entity) -> (Player) entity).collect(Collectors.toList());
	}

	public static File getSavesFolder() {
		File file = new File(instance.getDataFolder().getAbsolutePath().replace("RubidiaCore", "Rubidia.saves"));
		if (!file.exists())
			file.mkdirs();
		return file;
	}

	public static Location getSpawn() {
		if (Configs.getDatabase().contains("spawnLocation")) {
			return (Location) Configs.getDatabase().get("spawnLocation");
		}
		World defaultWorld = Bukkit.getServer().getWorld("Rubidia");
		if (defaultWorld == null) {
			console.sendMessage("Default world Rubidia couldn't be found!");
			defaultWorld = Bukkit.getServer().getWorlds().get(0);
		}
		return defaultWorld.getSpawnLocation();
	}

	public static void setSpawn(Location location) {
		Configs.getDatabase().set("spawnLocation", location);
	}

	////////////////////////////////////////
	// CITY SYSTEM //
	////////////////////////////////////////

	public static void registerCity(Location loc, String name) {
		Configs.getCitiesConfig().set("cities." + name + ".location", loc);
	}

	////////////////////////////////////////
	// JSONAPI METHODS //
	////////////////////////////////////////

	public String getAllWeapons(String rClass, String weaponUse) {
		List<Weapon> available = new ArrayList<Weapon>(Weapons.weapons);
		if (rClass != null) {
			for (Weapon weapon : Weapons.weapons) {
				if (available.contains(weapon) && !weapon.getRClass().toString().contains(rClass)) {
					available.remove(weapon);
				}
			}
		}
		if (weaponUse != null) {
			for (Weapon weapon : Weapons.weapons) {
				if (available.contains(weapon) && !weapon.getWeaponUse().toString().contains(weaponUse)) {
					available.remove(weapon);
				}
			}
		}
		String json = available.size() > 1 ? "[" : "";
		for (Weapon weapon : available) {
			json += "{";
			json += JSONUtils.toJSON("name") + ":" + JSONUtils.toJSON(weapon.getName()) + ",";
			json += JSONUtils.toJSON("level") + ":" + weapon.getLevel() + ",";
			json += JSONUtils.toJSON("weaponUse") + ":" + JSONUtils.toJSON(weapon.getWeaponUse().getDisplayFr()) + ",";
			String use = weapon.getWeaponUse().toString();
			json += JSONUtils.toJSON("dataWeaponUse") + ":"
					+ JSONUtils.toJSON(weapon.isAttack() ? "ARMURE"
							: (use.equals("MELEE_RANGE") ? "polyvalente" : (use.equals("MAGIC") ? "magique" : use)))
					+ ",";
			json += JSONUtils.toJSON("minDamages") + ":" + weapon.getMinDamages() + ",";
			json += JSONUtils.toJSON("maxDamages") + ":" + weapon.getMaxDamages() + ",";
			json += JSONUtils.toJSON("rarity") + ":" + JSONUtils.toJSON(weapon.getRarity().getDisplayFr()) + ",";
			json += JSONUtils.toJSON("dataRarity") + ":" + JSONUtils.toJSON(weapon.getRarity().getDisplayFr()
					.replaceAll(" ", "#").replaceAll("é", "e").replaceAll("É", "E").replaceAll("ommun", "ommune"))
					+ ",";
			json += JSONUtils.toJSON("dropChance") + ":"
					+ JSONUtils.toJSON(String
							.valueOf(Utils.round(weapon.getDropChance() * weapon.getRarity().getFactor() * 100, 3)))
					+ ",";
			json += JSONUtils.toJSON("attackSpeed") + ":"
					+ JSONUtils.toJSON(String.valueOf(Utils.round(weapon.getAttackSpeed(), 3))) + ",";
			json += JSONUtils.toJSON("skinId") + ":" + JSONUtils.toJSON(String.valueOf(weapon.getSkinId())) + ",";
			json += JSONUtils.toJSON("type") + ":" + JSONUtils.toJSON(weapon.getType().toString()) + ",";
			json += JSONUtils.toJSON("rclass") + ":" + JSONUtils.toJSON(weapon.getRClass().getName()) + ",";
			String setName = "null";
			String setBuffs = "null";
			String setWeapons = "null";
			if (weapon.getSet() != null) {
				setName = JSONUtils.toJSON(weapon.getSet().getName());
				List<String> state = weapon.getSet().getBuffState(null);
				String[] buffs = new String[state.size()];
				for (int i = 0; i < buffs.length; i++) {
					buffs[i] = state.get(i);
				}
				setBuffs = JSONUtils.toJSON(buffs);
				List<Weapon> weapons = weapon.getSet().getWeapons();
				String[] names = new String[weapons.size()];
				for (int i = 0; i < names.length; i++) {
					names[i] = weapons.get(i).getName();
				}
				setWeapons = JSONUtils.toJSON(names);
			}
			json += JSONUtils.toJSON("setName") + ":" + setName + ",";
			json += JSONUtils.toJSON("setBuffs") + ":" + setBuffs + ",";
			json += JSONUtils.toJSON("setWeapons") + ":" + setWeapons;
			json += "}";
			if (available.indexOf(weapon) != available.size() - 1)
				json += ",";
		}
		json += available.size() > 1 ? "]" : "";
		return json;
	}

	public int getPendingRubis(String player) {
		RPlayer rp = RPlayer.getFromName(player);
		if (rp != null) {
			int amount = rp.getPendingRubis();
			rp.setPendingRubis(0);
			return amount;
		}
		return 0;
	}

	////////////////////////////////////////
	// ON AND OFF BEHAVIOR //
	////////////////////////////////////////

	public static boolean isInMaintenance() {
		return Configs.getDatabase().getBoolean("maintenance.active");
	}

	public static void restart() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			RPlayer rp = RPlayer.get(player);
			rp.sendMessage("§e§l   RUBIDIA REDEMARRE DANS 20 SECONDES...");
			rp.sendTitle("§aRubidia " + ("redémarre") + "...", "§6>  §e20 second" + ("e") + "s  §6<", 0, 140, 40);
		}
		new BukkitTask(Core.instance) {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					RPlayer rp = RPlayer.get(player);
					rp.sendMessage("§e§l   RUBIDIA REDEMARRE DANS 5 SECONDES...");
					rp.sendTitle("§aRubidia " + ("redémarre") + "...", "§6>  §e5 second" + ("e") + "s  §6<", 0, 200, 0);
				}
				new BukkitTask(Core.instance) {

					@Override
					public void run() {
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.kickPlayer("§6>>  §eRubidia redémarre et sera rapidement de nouveau disponible !");
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
					}

					@Override
					public void onCancel() {
					}

				}.runTaskLater(5 * 20);
			}

			@Override
			public void onCancel() {
			}

		}.runTaskLater(15 * 20);
		restarting = true;
	}

	public void onEnable() {
		console = Bukkit.getConsoleSender();

		wg = (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
		we = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");

		instance = this;
		uiManager = new UIManager(this);
		barHandler = new HealthBarHandler(this);
		
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		Configs.saveDefaultPlayerConfig();
		Configs.saveDefaultCitiesConfig();
		Configs.saveDefaultDatabase();
		Configs.saveDefaultPathConfig();
		Configs.saveDefaultWeaponsConfig();
		Configs.saveDefaultCouplesConfig();
		
		Bukkit.getServer().setWhitelist(Core.isInMaintenance());
		playersMax = Configs.getDatabase().getInt("playersMax");
		GuildsPlugin.onStart();
		me.pmilon.RubidiaGuilds.utils.Configs.saveMembersConfig();
		me.pmilon.RubidiaGuilds.utils.Configs.saveGuildConfig();
		QuestsPlugin.onStart();
		RubidiaMonstersPlugin.onStart();
		console.sendMessage("§a   Loading RPlayers...");
		rcoll = new RPlayerColl();
		console.sendMessage("§a   Loading Couples...");
		coupleColl = new CoupleColl();
		console.sendMessage("§a   Loading Weapons...");
		Weapons.onEnable(true);
		Bukkit.getServer().getPluginManager().registerEvents(new WeaponsListener(), this);
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new TagStandListener(this), this);
		Bukkit.getPluginManager().registerEvents(new TeleportHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new RLevelListener(), this);
		Bukkit.getPluginManager().registerEvents(new GamePlayEffectsHandler(), this);
		Bukkit.getPluginManager().registerEvents(new ChairListener(this), this);
		Bukkit.getPluginManager().registerEvents(new AeroplaneListener(this), this);
		Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);
		Bukkit.getPluginManager().registerEvents(new ResourcePackHandler(), this);
		Bukkit.getPluginManager().registerEvents(new RChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new RDuelListener(), this);
		Bukkit.getPluginManager().registerEvents(new AbilitiesListener(), this);
		Bukkit.getPluginManager().registerEvents(new NetherHandler(), this);

		this.getCommand("item").setExecutor(new ItemCommandExecutor());
		this.getCommand("profile").setExecutor(new ProfileCommandExecutor());
		this.getCommand("marry").setExecutor(new MarryCommandExecutor());
		this.getCommand("couple").setExecutor(new CoupleCommandExecutor());
		this.getCommand("events").setExecutor(new EventsCommandExecutor());
		this.getCommand("class").setExecutor(new ClassCommandExecutor());
		this.getCommand("mastery").setExecutor(new MasteryCommandExecutor());
		this.getCommand("vip").setExecutor(new VIPCommandExecutor());
		this.getCommand("reboot").setExecutor(new RebootCommandExecutor());
		this.getCommand("level").setExecutor(new LevelCommandExecutor());
		this.getCommand("scroll").setExecutor(new ScrollCommandExecutor());
		this.getCommand("howmanyplayers").setExecutor(new HMPCommandExecutor());
		this.getCommand("money").setExecutor(new MoneyCommandExecutor());
		this.getCommand("skd").setExecutor(new SKDCommandExecutor());
		this.getCommand("skp").setExecutor(new SKPCommandExecutor());
		this.getCommand("chat").setExecutor(new ChatCommandExecutor());
		this.getCommand("vanish").setExecutor(new VanishCommandExecutor());
		this.getCommand("mute").setExecutor(new MuteCommandExecutor());
		this.getCommand("bienvenue").setExecutor(new BienvenueCommandExecutor());
		this.getCommand("help").setExecutor(new HelpCommandExecutor());
		this.getCommand("statistics").setExecutor(new StatisticsCommandExecutor());
		this.getCommand("boosters").setExecutor(new BoostersCommandExecutor());
		this.getCommand("rankings").setExecutor(new RankingsCommandExecutor());
		this.getCommand("vote").setExecutor(new VoteCommandExecutor());
		this.getCommand("rplayers").setExecutor(new RPlayersCommandExecutor());
		this.getCommand("reset").setExecutor(new ResetCommandExecutor());
		this.getCommand("spawn").setExecutor(new SpawnCommandExecutor());

		REnchantment.registerEnchantments();
		Events.onEnable(this);
		EntityHandler.onEnable(this);
		Crafts.initialize();// must be initialized after weapons initialization

		RItemStacks.enable();
		console.sendMessage("§a   Rubidia Core plugin enabled!");

		//1/min task
		new BukkitTask(this) {

			@Override
			public void run() {
				World world = Bukkit.getWorld("Tutorial");
				if (world != null) {
					if (world.getTime() != 16000) {
						world.setTime(16000);
						world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
					}
				}

				for (RPlayer rp : RPlayer.getOnlines()) {
					if (!rp.isOp()) {
						rp.setRenom(rp.getRenom() + 1);
					}
				}
			}

			@Override
			public void onCancel() {
			}

		}.runTaskTimer(0, 60 * 20);
		
		// 1/sec task
		new BukkitTask(this) {

			@Override
			public void run() {
				for (RPlayer rp : RPlayer.getOnlines()) {
					if (rp.getLoadedSPlayer() != null) {
						rp.addVigor(rp.getVigorPerSecond());
						rp.addHealth(rp.getHealthPerSecond());
						rp.setGamingTime(rp.getGamingTime() + 1000L);
						rp.setLastConnectionDate(System.currentTimeMillis());

						for (Pet pet : rp.getPets()) {
							pet.update(rp.getPlayer());
						}

						if (rp.isVip()) {
							if (rp.getVip() < System.currentTimeMillis()) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "vip " + rp.getName() + " 0");
							}
						}

						Player player = rp.getPlayer();
						ItemStack item = player.getEquipment().getItemInMainHand();
						if (item != null) {
							RItem rItem = new RItem(item);
							if (rItem.isWeapon()) {
								if (player.getGameMode().equals(GameMode.SURVIVAL)) {
									player.setGameMode(GameMode.ADVENTURE);
								}
							} else {
								if (player.getGameMode().equals(GameMode.ADVENTURE)) {
									player.setGameMode(GameMode.SURVIVAL);
								}
							}
						}
					}

					Couple couple = rp.getCouple();
					if (couple != null) {
						if (couple.isOnline()) {
							couple.setXPTime(couple.getXPTime() + 500L);
						}
					}
				}

				for (Event event : Events.currentEvents) {
					if (!event.isStarted() && System.currentTimeMillis() >= event.getStartDate()
							&& System.currentTimeMillis() <= event.getStartDate() + event.getDuration()) {
						event.start();
					} else if (event.isStarted()
							&& System.currentTimeMillis() > event.getStartDate() + event.getDuration()) {
						event.finish();
					}
				}
			}

			@Override
			public void onCancel() {
			}
		}.runTaskTimer(0, 20);
		
		// 4/sec Task
		new BukkitTask(this) {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					RPlayer rp = RPlayer.get(player);

					if (rp.getLoadedSPlayer() != null) {
						if (player.getOpenInventory() != null) {
							if (player.getOpenInventory().getTopInventory() != null) {
								if (player.getOpenInventory().getTopInventory().getType()
										.equals(InventoryType.CRAFTING)) {
									if (!Core.uiManager.hasActiveSession(player)) {
										boolean recipe_empty = true;
										for (int i = 0; i < 5; i++) {
											if (player.getOpenInventory().getTopInventory().getItem(i) != null) {
												recipe_empty = false;
												break;
											}
										}
										if (recipe_empty) {
											ItemStack skt = new ItemStack(Material.BOOKSHELF, 1);
											ItemMeta meta = skt.getItemMeta();
											meta.setDisplayName(("§6§lMenu du personnage"));
											meta.setLore(Arrays.asList(("§7Ouvrir le menu du personnage")));
											skt.setItemMeta(meta);
											player.getOpenInventory().getTopInventory().setItem(0, skt);
											player.updateInventory();
										}
									}
								}
							}
						}

						double speedFactor = Set.getAdditionalFactor(player, BuffType.WALK_SPEED);

						if (rp.getRClass().equals(RClass.RANGER)) {
							if (player.getInventory().getItem(17) != null) {
								if (!(player.getInventory().getItem(17).getType().toString().contains("ARROW"))
										|| player.getInventory().getItem(17).getAmount() < 3) {
									player.getInventory().setItem(17, new ItemStack(Material.ARROW, 3));
								}
							} else {
								player.getInventory().setItem(17, new ItemStack(Material.ARROW, 3));
							}
						} else if (rp.getRClass().equals(RClass.ASSASSIN)) {
							speedFactor += RAbility.ASSASSIN_3.getDamages(rp) * .01 - 1;
						}

						if (Weapons.checkEquipment(rp)) {// handle equipment removal and returns whether the player is wearing elytras or not
							player.setAllowFlight(!player.isGliding());
						} else if ((player.getGameMode().equals(GameMode.SURVIVAL)
								|| player.getGameMode().equals(GameMode.ADVENTURE))
								&& !DialogManager.isInDialog(player)) {
							player.setAllowFlight(false);
						}

						Couple couple = rp.getCouple();
						if (couple != null) {
							for (Buff buff : couple.getAvailableBuffs()) {
								if (buff.getType().equals(BuffType.WALK_SPEED)) {
									speedFactor += buff.getFactor();
								}
							}
						}

						if (player.getWalkSpeed() > 0) {
							float speed = (float) (Settings.DEFAULT_WALK_SPEED * (1 + speedFactor));
							if (Math.abs(player.getWalkSpeed() - speed) >= .001) {
								player.setWalkSpeed(speed);
							}
						}

						rp.updateMaxHealth();
					}
				}
			}

			@Override
			public void onCancel() {
			}

		}.runTaskTimer(0, 5);
		
		int interval = getConfig().getInt("saveinterval") * 20;
		new BukkitTask(this) {

			@Override
			public void run() {
				/*for (RPlayer rp : RPlayer.getOnlines()) {
					rp.sendTitle("§5SAUVEGARDE DU SERVEUR", "§dRalentissement attendu dans 5 secondes", 0, 100, 20);
				}
				new BukkitTask(Core.instance) {

					@Override
					public void run() {
					}

					@Override
					public void onCancel() {
					}

				}.runTaskLater(5 * 20);*/
				// No need to alert for lags if we save files asynchronously!
				Core.console.sendMessage("§eSaving configs...");

				Ranks.update(); // strangely works asynchronously, whereas we update blocks...

				rcoll.saveAll(false);
				coupleColl.saveAll(false);
				Weapons.onDisable();
				QuestsPlugin.questColl.saveAll(false);
				QuestsPlugin.houseColl.saveAll(false);
				QuestsPlugin.shopColl.saveAll(false);
				PNJManager.save(false);
				Configs.saveCitiesConfig();
				Configs.saveDatabase();
				Configs.savePathConfig();
				GuildsPlugin.instance.saveConfig();
				GuildsPlugin.gcoll.saveAll(false);
				GuildsPlugin.gmembercoll.saveAll(false);
				GuildsPlugin.claimcoll.saveAll(false);
				GuildsPlugin.raidcoll.saveAll(false);
				Monsters.save(false);
				Regions.save(false);
				Pets.save(false);

				if (getConfig().getBoolean("saveconfigs")) {
					Core.console.sendMessage("§eSaving backup configs...");
					Configs.saveConfigs();
					me.pmilon.RubidiaGuilds.utils.Configs.saveConfigs();
					me.pmilon.RubidiaQuests.utils.Configs.saveConfigs();
					me.pmilon.RubidiaMonsters.utils.Configs.saveConfigs();
					me.pmilon.RubidiaPets.utils.Configs.saveConfigs();
					Core.console.sendMessage("§eSaved backup configs!");
				}
				Core.console.sendMessage("§eSaved configs!");
				
				try (PrintWriter out = new PrintWriter("restart_state.txt")) {
					out.println(RandomUtils.random.nextInt());
				} catch (FileNotFoundException e) {
					Core.console.sendMessage("Couldn't write state to restart_state.txt!");
				}
			}

			@Override
			public void onCancel() {
			}

		}.runTaskTimerAsynchronously(interval, interval);

		int annoucementsInterval = this.getConfig().getInt("announcementsInterval") * 20;
		new BukkitTask(this) {

			@Override
			public void run() {
				List<String> annoucements = getConfig().getStringList("announcements");
				String[] announce = annoucements.get(RandomUtils.random.nextInt(annoucements.size())).split("--");
				String message = "\n   §e" + announce[0];
				if (announce.length > 1) {
					message += "\n            §8>> §7§l" + announce[1];
				}
				message += "\n ";
				for (RPlayer rp : RPlayer.getOnlines()) {
					rp.getChat().addFixDisplay(new RChatFixDisplay(rp, 200, null).addLine(message));
				}
			}

			@Override
			public void onCancel() {
			}

		}.runTaskTimer(annoucementsInterval, annoucementsInterval);
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Status.Server.SERVER_INFO) {
			@Override
			public void onPacketSending(PacketEvent e) {
				if (Core.isInMaintenance()) {
					if (Configs.getDatabase().contains("maintenance.message")) {
						String message = Configs.getDatabase().getString("maintenance.message");
						if (message.equalsIgnoreCase("alpha")) {
							WrappedServerPing ping = e.getPacket().getServerPings().read(0);
							ping.setPlayersMaximum(0);
							ping.setMotD(Core.firstMotd + "\n                   §e<§e§l  §6VERSION §lALPHA  §e>");
							return;
						}
					}
					
					String message = Configs.getDatabase().contains("maintenance.message")
							? Configs.getDatabase().getString("maintenance.message").toUpperCase()
							: "MAINTENANCE EN COURS";
					
					WrappedServerPing ping = e.getPacket().getServerPings().read(0);
					ping.setVersionProtocol(999);
					ping.setPlayersMaximum(0);
					ping.setVersionName("§cMAINTENANCE");
					ping.setMotD(Core.firstMotd + "\n            §e<§e§l  §6§l" + message + "  §e>");
					
					UUID uuid = UUID.fromString("0-0-0-0-0");
					Iterable<WrappedGameProfile> players = Arrays.asList(
							new WrappedGameProfile(uuid, "§8§m §7§m  §c§l§m[         §r§l        §6§lR§e§lUBIDIA        §c§l§m         ]§7§m  §8§m "),
							new WrappedGameProfile(uuid, ""),
							new WrappedGameProfile(uuid, "         §7Nous travaillons dans le seul but"),
							new WrappedGameProfile(uuid, "      §7d'améliorer votre expérience de jeu !"),
							new WrappedGameProfile(uuid, ""),
							new WrappedGameProfile(uuid, ""),
							new WrappedGameProfile(uuid, "        §e<§e§l  §6§lO§e§lpen-§6§lW§e§lorld §6§lA§e§ldventure §6§lRPG  §e>"),
							new WrappedGameProfile(uuid, ""),
							new WrappedGameProfile(uuid, "§8§m §7§m  §c§l§m[                                           ]§7§m  §8§m "));
					
					ping.setPlayers(players);
				}
			}
		});
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.WINDOW_ITEMS) {
			@Override
			public void onPacketSending(PacketEvent e) {
				WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(e.getPacket());
				List<ItemStack> stacks = packet.getSlotData();
				if (packet.getWindowId() == 0 && stacks.size() > 44) {
					RPlayer rp = RPlayer.get(e.getPlayer());
					if (rp != null) {
						if (!rp.isOp()) {
							stacks.set(44, rp.getVigorItem());
							packet.setSlotData(stacks);
						}
					}
				}
			}
		});
	}

	public void onDisable() {
		for (RPlayer rp : RPlayer.getOnlines()) {
			if (!rp.isOp()) rp.getPlayer().kickPlayer("§2§lRubidia §aredémarre et sera très rapidement de nouveau disponible !");
		}

		for (Team team : Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
			team.unregister();
		}
		
		for (BukkitTask task : new ArrayList<BukkitTask>(BukkitTask.tasks.values())) {
			task.cancel();
		}

		console.sendMessage("§a   Saving RPlayers...");
		rcoll.saveAll(true);
		console.sendMessage("§a   Saving Couples...");
		coupleColl.saveAll(true);
		console.sendMessage("§a   Saving Weapons...");
		Weapons.onDisable();
		console.sendMessage("§a   Saving Guilds...");
		GuildsPlugin.onEnd();
		console.sendMessage("§a   Saving Quests...");
		QuestsPlugin.onEnd();
		RubidiaMonstersPlugin.onEnd();
		PetsPlugin.instance.onEnd();
		console.sendMessage("§a   Saving Cities...");
		Configs.saveCitiesConfig();
		console.sendMessage("§a   Saving Database...");
		Events.save();
		Configs.getDatabase().set("playersMax", playersMax);
		Configs.saveDatabase();
		console.sendMessage("§a   Rubidia Core Plugin Disabled");
		Weapons.onDisable();
	}
}
