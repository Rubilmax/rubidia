package me.pmilon.RubidiaQuests.houses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaQuests.QuestsPlugin;

public class House {

	private final String uniqueId;
	private String ownerUniqueId;
	private List<Block> blocks;
	private double blockPrice;
	public House(String uniqueId, String ownerUniqueId, List<Block> blocks, double blockPrice) {
		this.uniqueId = uniqueId;
		this.ownerUniqueId = ownerUniqueId;
		this.blocks = blocks;
		this.blockPrice = blockPrice;
	}
	
	public static House get(Block block) {
		return QuestsPlugin.houseColl.get(block.getLocation());
	}
	
	public static House get(RPlayer owner) {
		return QuestsPlugin.houseColl.get(owner);
	}
	
	public static House get(String uniqueId) {
		return QuestsPlugin.houseColl.get(uniqueId);
	}
	
	public void reset(Location location){
		BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
		List<Block> blocks = new ArrayList<Block>();
		Queue<Block> queue = new LinkedList<Block>();
		Block first = location.getBlock().getRelative(BlockFace.DOWN);
		if(first.getType().toString().contains("_PLANKS") || first.getType().toString().contains("_SLAB")){
			String woodType = first.getType().toString().split("_")[0];
			queue.add(first);
			Set<Block> lastFloor = new HashSet<Block>();
			while(!queue.isEmpty() && lastFloor.size() < 5000) {
				Block check = queue.poll();
				lastFloor.add(check);
				for(BlockFace face : faces) {
					Block relative = check.getRelative(face);
					if((relative.getType().toString().equals(woodType + "_PLANKS") || relative.getType().toString().equals(woodType + "_SLAB"))
							&& relative.getRelative(BlockFace.UP).getType().equals(Material.AIR)
							&& !lastFloor.contains(relative)) {
						queue.add(relative);
					}
				}
			}
			
			int y = 0;
			while(y < 50 && !lastFloor.isEmpty()) {
				List<Block> floor = new ArrayList<Block>(lastFloor);
				lastFloor.clear();
				for(Block floorBlock : floor) {
					Location location2 = floorBlock.getLocation();
					location2.add(new Vector(0,1,0));
					Block block = location.getWorld().getBlockAt(location2);
					if(block.getType().equals(Material.AIR)) {
						lastFloor.add(block);
					}
				}
				blocks.addAll(lastFloor);
				y++;
			}
		}
		
		this.setBlocks(blocks);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public String getOwnerUniqueId() {
		return ownerUniqueId;
	}
	
	public RPlayer getOwner() {
		return RPlayer.get(this.ownerUniqueId);
	}

	public void setOwnerUniqueId(String ownerUniqueId) {
		this.ownerUniqueId = ownerUniqueId;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		for(Block block : this.blocks) {
			HouseColl.houses.remove(block.getLocation());
		}
		this.blocks = blocks;
		this.updateBlocks();
	}
	
	public void updateBlocks() {
		for(Location location : HouseColl.houses.keySet()) {
			if(!this.getBlocks().contains(location.getBlock()) && HouseColl.houses.get(location).equals(this)) {
				HouseColl.houses.remove(location);
			}
		}
		for(Block block : this.blocks) {
			HouseColl.houses.put(block.getLocation(), this);
		}
	}

	public double getBlockPrice() {
		return blockPrice;
	}

	public void setBlockPrice(double blockPrice) {
		this.blockPrice = blockPrice;
	}
	
	public int getPrice() {
		return (int) Math.round(this.getBlocks().size()*this.getBlockPrice());
	}
	
	public boolean isInhabited() {
		return !this.getOwnerUniqueId().equals("");
	}
	
	public void empty() {
		for(Block block : this.getBlocks()) {
			if(block.getType().equals(Material.CHEST)) {
				Chest chest = (Chest) block.getState();
				chest.getInventory().clear();
			}
			block.setType(Material.AIR);
		}
	}
	
	public void sell() {
		this.empty();
		EconomyHandler.deposit(this.getOwner(), (int) Math.round(this.getPrice()*.7));
		this.setOwnerUniqueId("");
	}
}
