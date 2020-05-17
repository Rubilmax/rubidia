package me.pmilon.RubidiaCore.chairs;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.events.DelayedPlayerUnsitEvent;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ChairListener implements Listener {

	private Core plugin;
	public ChairListener(Core plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(!event.useItemInHand().equals(Result.DENY)){
			if(event.getHand() != null){
				if(event.getHand().equals(EquipmentSlot.HAND)){
					if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
						Block clicked = event.getClickedBlock();
						Player p = event.getPlayer();
						if(clicked.getType().toString().contains("STAIRS") && !p.isSneaking()){
							if(p.getEquipment().getItemInMainHand().getType().equals(Material.AIR)){
								ChairAPI.sit(this.getPlugin(), p, clicked);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onUnsit(DelayedPlayerUnsitEvent event){
		Chair chair = event.getChair();
		Player player = event.getPlayer();
		int index = chair.getPlayers().indexOf(player);
		chair.getPlayers().remove(player);
		chair.getChairStands().get(index).remove();
		chair.getChairStands().remove(index);
		chair.getSitLocations().remove(index);
		chair.stopTask(index);

		if(chair.getPlayers().size() > 0){
			Player player1 = chair.getPlayers().get(0);
			chair.unsitPlayer(player1);
			chair.sitPlayer(player1);
		}
	}

	public Core getPlugin() {
		return plugin;
	}

	public void setPlugin(Core plugin) {
		this.plugin = plugin;
	}
	
}
