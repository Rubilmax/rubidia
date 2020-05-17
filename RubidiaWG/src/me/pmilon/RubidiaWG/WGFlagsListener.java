package me.pmilon.RubidiaWG;

import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerAbilityEvent;
import me.pmilon.RubidiaCore.events.RPlayerRequestDuelEvent;
import me.pmilon.RubidiaGuilds.events.GMemberClaimEvent;
import me.pmilon.RubidiaMonsters.events.MonsterSpawnEvent;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;
import me.pmilon.RubidiaWG.events.RegionEnterEvent;
import me.pmilon.RubidiaWG.events.RegionLeaveEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;


public class WGFlagsListener implements Listener {
	
	@EventHandler
	public void onDuelRequest(RPlayerRequestDuelEvent e){
		RPlayer rp = e.getRPlayer();
		RPlayer requested = e.getOpponent();
		if(!WGUtils.testState(rp.getPlayer(), rp.getPlayer().getLocation(), Flags.DUELS) || !WGUtils.testState(requested.getPlayer(), requested.getPlayer().getLocation(), Flags.DUELS)){
			e.setCancelled(true);
			rp.sendMessage("§cVous ne pouvez demander un duel ici.");
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		Block block = e.getClickedBlock();
		
		if(block != null){
			List<Material> blocks = Arrays.asList(Material.CHEST, Material.HOPPER, Material.FURNACE, Material.ANVIL, Material.ENCHANTING_TABLE, Material.CAULDRON, Material.CRAFTING_TABLE, Material.DROPPER, Material.END_PORTAL, Material.BEACON, Material.BREWING_STAND, Material.DISPENSER);
			if(blocks.contains(block.getType()) && !rp.isOp()){
				if(!WGUtils.testState(p, block.getLocation(), Flags.BLOCKS)){
					e.setCancelled(true);
					rp.sendMessage("§cVous ne pouvez intéragir avec les blocs ici.");
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent e){
		Entity entity = e.getRightClicked();
		Player player = e.getPlayer();
		RPlayer rp = RPlayer.get(player);

		if(!rp.isOp()){
			if(entity instanceof Vehicle){
				if(!WGUtils.testState(player, entity.getLocation(), Flags.RIDE)){
					Pet pet = Pets.get(entity);
					if(pet != null){
						if(!pet.getOwner().equals(player)){
							e.setCancelled(true);
							rp.sendMessage("§cVous ne pouvez monter que vos compagnons ici.");
						}
					}else{
						e.setCancelled(true);
						rp.sendMessage("§cVous ne pouvez monter que vos compagnons ici.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockChange(EntityChangeBlockEvent e){
		Block block = e.getBlock();
		if(block != null){
			if(block.getType().equals(Material.FARMLAND)){
				if(!WGUtils.testState(null, block.getLocation(), Flags.SOIL_TRAMPLING)){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockFade(BlockFadeEvent e){
		Block block = e.getBlock();
		if(block != null){
			if(block.getType().equals(Material.FARMLAND)){
				if(!WGUtils.testState(null, block.getLocation(), Flags.SOIL_TRAMPLING)){
                    e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onRegionLeave(RegionLeaveEvent e){
		Player player = e.getPlayer();
		RPlayer rp = RPlayer.get(e.getPlayer());
		
		String leaveFlag = e.getRegion().getFlag(Flags.LEAVE_COMMAND);
		if(leaveFlag != null){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), leaveFlag.replaceAll("%player", player.getName()).replaceAll("%level", String.valueOf(rp.getRLevel())));
		}

		String title = e.getRegion().getFlag(Flags.LEAVE_TITLE);
		if(title != null){
			if(!title.isEmpty()){
				String[] titles = title.split("//");
				rp.sendTitle(titles[0], titles[1], 25, 60, 25);
				return;
			}
		}

		if(WGUtils.testState(player, e.getFrom(), Flags.CLAIM)){
			if(!WGUtils.testState(player, e.getTo(), Flags.CLAIM)){
				rp.sendTitle("§6§lREGION PROTEGEE", "§eTerritoire conquis", 5, 45, 10);
			}
		} else {
			if(WGUtils.testState(player, e.getTo(), Flags.CLAIM)){
				rp.sendTitle("§7§lZONE SAUVAGE", "§fTerritoire libre", 5, 45, 10);
			}
		}
	}
	
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e){
		Player player = e.getPlayer();
		RPlayer rp = RPlayer.get(player);
		
		String title = e.getRegion().getFlag(Flags.ENTER_TITLE);
		if(title != null){
			if(!title.isEmpty()){
				String[] titles = title.split("//");
				rp.sendTitle(titles[0], titles[1], 25, 60, 25);
			}
		}
		
		if(WGUtils.testState(player, e.getFrom(), Flags.CLAIM)){
			if(!WGUtils.testState(player, e.getTo(), Flags.CLAIM)){
				rp.sendTitle("§6§lREGION PROTEGEE", "§eTerritoire conquis", 5, 45, 10);
			}
		} else {
			if(WGUtils.testState(player, e.getTo(), Flags.CLAIM)){
				rp.sendTitle("§7§lZONE SAUVAGE", "§fTerritoire libre", 5, 45, 10);
			}
		}
	}

	@EventHandler
	public void onGuildClaim(GMemberClaimEvent e){
		Player p = e.getGMember().getPlayer();
		int x = e.getClaim().getX() << 4;
		int z = e.getClaim().getZ() << 4;
		if(!WGUtils.testState(p, p.getWorld(), new Vector(x, 0, z), new Vector(x + 15, 256, z + 15), Flags.CLAIM)){
			e.setCancelled(true);
			RPlayer.get(p).sendMessage("§cVous ne pouvez conquérir ce territoire.");
		}
	}

	@EventHandler
	public void onMonsterSpawn(MonsterSpawnEvent event){
		if(!WGUtils.testState(null, event.getLocation(), Flags.NATURAL_SPAWN)){
  	    	event.setCancelled(true);
		}
	}

	@EventHandler
	public void onAbility(RPlayerAbilityEvent event){
		RPlayer rp = event.getRPlayer();
		if(rp.isOnline()){
			if(!rp.isInDuel()){
				Player player = rp.getPlayer();
				if(!WGUtils.testState(player, player.getLocation(), Flags.SKILLS) && !rp.isOp()){
					event.setCancelled(true);
					rp.sendActionBar("§4§lHey! §cYou cannot cast skills here!", "§4§lHey ! §cVous ne pouvez utiliser de compétences ici !");
				}
			}
		}
	}
}
