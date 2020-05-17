package me.pmilon.RubidiaCore.handlers;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;

public class PlaymodeHandler {
	
	public static void savePlaymodeCreativeInventory(Player p){
		RPlayer rp = RPlayer.get(p);
		Inventory inv = p.getInventory();
		for(int slot = 0;slot < 9;slot++){
			rp.getCreativeHM().put(slot, inv.getItem(slot));
		}
	}
	
	public static void setPlaymodeCreativeInventory(Player p){
		RPlayer rp = RPlayer.get(p);
		for(int slot = 0;slot < 9;slot++){
			if(rp.getCreativeHM().containsKey(slot))p.getInventory().setItem(slot, rp.getCreativeHM().get(slot));
		}
	}

	public static void savePlaymodeSurvivalInventory(Player p){
		RPlayer rp = RPlayer.get(p);
		Inventory inv = p.getInventory();
		for(int slot = 0;slot < inv.getSize();slot++){
			rp.getSurvivalHM().put(slot, inv.getItem(slot));
		}
		rp.getSurvivalHM().put(101, p.getEquipment().getHelmet());
		rp.getSurvivalHM().put(102, p.getEquipment().getChestplate());
		rp.getSurvivalHM().put(103, p.getEquipment().getLeggings());
		rp.getSurvivalHM().put(104, p.getEquipment().getBoots());
	}
	
	public static void setPlaymodeSurvivalInventory(Player p){
		RPlayer rp = RPlayer.get(p);
		Inventory inv = p.getInventory();
		for(int slot = 0;slot < inv.getSize();slot++){
			if(rp.getSurvivalHM().containsKey(slot))inv.setItem(slot, rp.getSurvivalHM().get(slot));
		}
		EntityEquipment equipment = p.getEquipment();
		if(rp.getSurvivalHM().containsKey(101))equipment.setHelmet(rp.getSurvivalHM().get(101));
		if(rp.getSurvivalHM().containsKey(102))equipment.setChestplate(rp.getSurvivalHM().get(102));
		if(rp.getSurvivalHM().containsKey(103))equipment.setLeggings(rp.getSurvivalHM().get(103));
		if(rp.getSurvivalHM().containsKey(104))equipment.setBoots(rp.getSurvivalHM().get(104));
	}
	
}
