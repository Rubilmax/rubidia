package me.pmilon.RubidiaCore.scrolls;

import java.util.HashMap;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Scrolls {
	
	public static HashMap<ItemStack, Scroll> scrolls = new HashMap<ItemStack, Scroll>();
	
	public static void newScroll(Player player, ScrollType type, String arg){
		RPlayer rp = RPlayer.get(player);
		Scroll scroll = new Scroll(type, arg);
		ItemStack item = scroll.getNewItemStack(rp);
		scrolls.put(item, scroll);
		player.getInventory().addItem(item);
	}
	
	public static Scroll get(ItemStack item){
		if(scrolls.containsKey(item))return scrolls.get(item);
		ScrollType type = Scrolls.getScrollType(item);
		Scroll scroll = new Scroll(type, "");
		if(type.toString().contains("ADD"))scroll.setArg(String.valueOf(Scrolls.getArgAmount(item)));
		else if(type.equals(ScrollType.CITYTP))scroll.setArg(Scrolls.getArgCity(item));
		return scroll;
	}
	
	public static ScrollType getScrollType(ItemStack scroll){
		if(scroll.hasItemMeta()){
			ItemMeta meta = scroll.getItemMeta();
			if(meta.hasDisplayName()){
				String scrollname = ChatColor.stripColor(meta.getDisplayName());
				return ScrollType.fromName(scrollname);
			}
		}
		return null;
	}
	
	public static int getArgAmount(ItemStack scroll){
		if(scroll.hasItemMeta()){
			ItemMeta meta = scroll.getItemMeta();
			if(meta.getLore().size() > 2){
				String lore = ChatColor.stripColor(meta.getLore().get(2));
				String[] lorep = lore.split(" ");
				return Integer.valueOf(lorep[0].split("\\+")[1]);
			}
		}
		return 0;
	}
	
	public static String getArgCity(ItemStack scroll){
		if(scroll.hasItemMeta()){
			ItemMeta meta = scroll.getItemMeta();
			if(meta.hasDisplayName()){
				String scrollname = scroll.getItemMeta().getDisplayName();
				String scrollnamewithoutcolors = ChatColor.stripColor(scrollname);
				String[] scrollparts = scrollnamewithoutcolors.split(" ");
				if(scrollparts.length > 0)return scrollparts[0];
			}
		}
		return "";
	}
	
}
