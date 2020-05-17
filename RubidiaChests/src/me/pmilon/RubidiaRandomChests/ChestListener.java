package me.pmilon.RubidiaRandomChests;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestListener implements Listener {

	public HashMap<Player, Chest> openedChests = new HashMap<Player, Chest>();
	
	private final RandomChestsPlugin plugin;
	public ChestListener(RandomChestsPlugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getHand() != null){
			if(event.getHand().equals(EquipmentSlot.HAND)){
				Player player = event.getPlayer();
				RPlayer rplayer = RPlayer.get(player);
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
					Block block = event.getClickedBlock();
					if(block != null){
						if(block.getType().equals(Material.TRAPPED_CHEST)){
							Chest chest = (Chest)block.getState();
							if(rplayer.isOp() && player.isSneaking()){
								if(RandomChestsPlugin.getChests().containsKey(block.getLocation())){
									if(rplayer.isOp() && player.isSneaking()){
										LuckyChest luckyChest = RandomChestsPlugin.getChests().get(block.getLocation());
										luckyChest.despawn(false);
										luckyChest.getSpawnTask().cancel();
										RandomChestsPlugin.getChests().remove(block.getLocation());
										rplayer.sendMessage("§aVous avez supprimé ce lieu des lieux de coffre chance.");
										event.setCancelled(true);
										return;
									}
								}else{
									Directional directional = (Directional) chest.getBlockData();
									LuckyChest luckyChest = new LuckyChest(block.getLocation(), directional.getFacing(), true);
									RandomChestsPlugin.getChests().put(block.getLocation(), luckyChest);
									luckyChest.handleSpawn();
									rplayer.sendMessage("§aVous avez ajouté ce lieu comme lieu de coffre chance.");
									event.setCancelled(true);
								}
							}

							if(RandomChestsPlugin.getChests().containsKey(block.getLocation())){
								LuckyChest luckyChest = RandomChestsPlugin.getChests().get(block.getLocation());
								if(luckyChest.isActive()) {
									this.openedChests.put(player, chest);
									luckyChest.open(player);
									rplayer.sendMessage("§aVous avez trouvé un coffre chance !");
									Core.console.sendMessage("§6" + rplayer.getName() + " §efound a lucky chest!");
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		final RPlayer rp = RPlayer.get(player);
		final Block block = event.getBlock();
		if(block != null){
			ItemStack is = player.getEquipment().getItemInMainHand();
			if(is.getType().equals(Material.TRAPPED_CHEST)){
				if(is.hasItemMeta()){
					ItemMeta meta = is.getItemMeta();
					if(meta.hasDisplayName()){
						if(meta.getDisplayName().contains("chance")){
							for(BlockFace face : new BlockFace[]{BlockFace.EAST,BlockFace.NORTH,BlockFace.WEST,BlockFace.SOUTH}){
								if(block.getRelative(face).getType().equals(Material.TRAPPED_CHEST)){
									Directional directional1 = (Directional) block.getBlockData();
									Directional directional2 = (Directional) block.getRelative(face).getBlockData();
									if(directional1.getFacing().equals(directional2.getFacing())) {
										rp.sendMessage("§cVous ne pouvez poser un coffre chance ici !");
										event.setCancelled(true);
										return;
									}
								}
							}

							event.setCancelled(false);
							Bukkit.getScheduler().runTaskLater(RandomChestsPlugin.getInstance(), new Runnable(){
								public void run(){
									Chest chest = (Chest) block.getState();
									Directional directional = (Directional) chest.getBlockData();
									LuckyChest luckyChest = new LuckyChest(block.getLocation(), directional.getFacing(), false);
									RandomChestsPlugin.getChests().put(block.getLocation(), luckyChest);
									luckyChest.spawn();
									rp.sendMessage("§aVous avez posé un coffre chance, ouvrez-le !");
								}
							}, 0);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		Player player = (Player) event.getPlayer();
		if(event.getView().getTitle().contains("chance")){
			if(this.openedChests.containsKey(player)){
				Chest chest = this.openedChests.get(player);
				if(RandomChestsPlugin.getChests().containsKey(chest.getLocation())) {
					RandomChestsPlugin.getChests().get(chest.getLocation()).despawn(true);
					this.openedChests.remove(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		if(RandomChestsPlugin.getChests().containsKey(block.getLocation())) {
			event.setCancelled(true);
		}
	}

	public RandomChestsPlugin getPlugin() {
		return plugin;
	}
	
}
