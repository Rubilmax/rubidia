package me.pmilon.RubidiaQuests.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

public class WE {

	public static List<Block> getBlocks(World world, Region region){
		List<Block> blocks = new ArrayList<Block>();
		
		BlockVector3 min = region.getMinimumPoint();
		BlockVector3 max = region.getMaximumPoint();
		int xMin = min.getBlockX();
		int yMin = min.getBlockY();
		int zMin = min.getBlockZ();
		
		int xMax = max.getBlockX();
		int yMax = max.getBlockY();
		int zMax = max.getBlockZ();
		
		for(int x = xMin;x <= xMax;x++){
			for(int y = yMin;y <= yMax;y++){
				for(int z = zMin;z <= zMax;z++){
					blocks.add(world.getBlockAt(x,y,z));
				}
			}
		}
		return blocks;
	}
	
}
