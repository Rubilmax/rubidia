package me.pmilon.RubidiaCore.chairs;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Stairs;

@SuppressWarnings("deprecation")
public class ChairAPI {

	public static HashMap<Block, Chair> chairs = new HashMap<Block, Chair>();
	
	public static void sit(Core plugin, Player player, Block block){
		if(chairs.containsKey(block)){
			Chair chair = ChairAPI.getChair(block);
			if(chair.isSittable()){
				chair.sitPlayer(player);
			}
		}else{
			if(ChairAPI.isSittable(block)){
				Chair chair = new Chair(plugin, block);
				chair.sitPlayer(player);
				chairs.put(block, chair);
			}
		}
	}

	public static boolean isSittable(Block block){
		Stairs stairs = (Stairs) block.getState().getData();
		return !block.getLocation().clone().add(0,1,0).getBlock().getType().isSolid() && (stairs.getData() & 0x4) == 0;
	}
	
	public static Chair getChair(Block block){
		if(chairs.containsKey(block))return chairs.get(block);
		return null;
	}
	
}
