package me.pmilon.RubidiaCore.commands;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaCore.ui.ItemListUI;
import me.pmilon.RubidiaCore.utils.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCommandExecutor extends PlayerAdminCommandExecutor {

	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			ItemStack item = player.getEquipment().getItemInMainHand();
			if(!item.getType().equals(Material.AIR)){
				ItemMeta meta = item.getItemMeta();
				String message = "";
				if(args[0].equalsIgnoreCase("name")){
					if(args.length > 1){
						for(int i = 1;i < args.length;i++){
							message += args[i];
							if(i != args.length-1)message += " ";
						}
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', message));
					}else rp.sendMessage("§cUtilisez /item name [message]");
				}else if(args[0].equalsIgnoreCase("lore")){
					if(args.length > 2){
						for(int i = 2;i < args.length;i++){
							message += args[i];
							if(i != args.length-1)message += " ";
						}
						if(Utils.isInteger(args[1])){
							int id = Integer.valueOf(args[1]);
							message = ChatColor.translateAlternateColorCodes('&', message);
							List<String> lore = meta.hasLore() ? Utils.getModifiableCopy(meta.getLore()) : new ArrayList<String>();
							if(lore.size() > id)lore.set(id, message);
							else {
								for(int i = lore.size();i < id;i++){
									lore.add("");
								}
								lore.add(message);
							}
							meta.setLore(lore);
						}else rp.sendMessage("§cUtilisez /item lore (id) " + message);
					}else rp.sendMessage("§cUtilisez /item lore (id) [message]");
				}else if(args[0].equalsIgnoreCase("flag")){
					if(args.length > 1){
						ItemFlag flag = null;
						for(ItemFlag flg : ItemFlag.values()){
							if(flg.toString().equalsIgnoreCase(args[1])){
								flag = flg;
								break;
							}
						}
						if(flag != null){
							if(meta.getItemFlags().contains(flag))meta.removeItemFlags(flag);
							else meta.addItemFlags(flag);
						}else{
							String flags = "";
							for(ItemFlag flg : ItemFlag.values()){
								flags += flg.toString();
								flags += ",";
							}
							rp.sendMessage("§cUtilisez /item flag " + flags);
						}
					}else rp.sendMessage("§cUtilisez /item flag (flag)");
				}else if(args[0].equalsIgnoreCase("list")){
					Core.uiManager.requestUI(new ItemListUI(player));
				}else rp.sendMessage("§cUtilisez /item [name (message)/lore (id) (message)/flag (flag)/list]");
				item.setItemMeta(meta);
				player.getEquipment().setItemInMainHand(item);
			}else if(args[0].equalsIgnoreCase("list")){
				Core.uiManager.requestUI(new ItemListUI(player));
			}else rp.sendMessage("§cVous n'avez pas d'item en main !");
		}else rp.sendMessage("§cUtilisez /item [name (message)/lore (id) (message)/flag (flag)]");
	}

}
