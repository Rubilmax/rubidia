package me.pmilon.RubidiaCore.scrolls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Scroll {

	private ScrollType type;
	private String arg;
	public Scroll(ScrollType type, String arg){
		this.type = type;
		this.arg = arg;
	}
	
	public ScrollType getType() {
		return type;
	}
	
	public boolean use(Player player){
		ScrollTask task = this.getType().getScrollTask();
		task.setPlayer(player);
		task.setScroll(this);
		task.run();
		return task.isUsed();
	}
	
	public void cancel(Player player){
		this.give(player);
	}
	
	public ItemStack getNewItemStack(RPlayer rp){
		ItemStack item = new ItemStack(Material.MAP, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§o" + (this.getType().equals(ScrollType.CITYTP) ? this.getArg() : "") + (this.getType().getNameFr()));
		List<String> lore = new ArrayList<String>();
		for(int i = 0;i < this.getType().getLoreEn().size();i++){
			lore.add("§7" + (this.getType().getLoreFr().get(i)));
		}
		if(this.getType().equals(ScrollType.SKPADD))lore.add("§2+§a" + this.getArg() + " §7" + ("points de compétence"));
		else if(this.getType().equals(ScrollType.SKDADD))lore.add("§2+§a" + this.getArg() + " §7" + ("points de distinction"));
		lore.addAll(Arrays.asList("",("§6§lUtilisation : §e" + this.getType().getUsage().getFr())));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public void give(Player player){
		RPlayer rp = RPlayer.get(player);
		player.getInventory().addItem(this.getNewItemStack(rp));
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}
	
}
