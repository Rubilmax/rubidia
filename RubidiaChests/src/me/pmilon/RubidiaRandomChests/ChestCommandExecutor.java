package me.pmilon.RubidiaRandomChests;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender.isOp()){
			if(args.length > 1){
				Player player = Bukkit.getPlayer(args[0]);
				if(player != null){
					if(Utils.isInteger(args[1])){
						int amount = Integer.valueOf(args[1]);
						ItemStack chest = new ItemStack(Material.TRAPPED_CHEST,amount > 64 ? 64 : amount);
						ItemMeta meta = chest.getItemMeta();
						meta.setDisplayName("§fCoffre chance");
						chest.setItemMeta(meta);
						player.getInventory().addItem(chest);
						RPlayer rp = RPlayer.get(player);
						sender.sendMessage("§2" + args[0] + " §areceived §2" + amount + " §alucky chest" + (amount > 1 ? "s" : "") + "!");
						rp.sendMessage("§aVous avez reçu §2" + amount + " §acoffre" + (amount > 1 ? "s" : "") + " chance !");
					}
				}else sender.sendMessage("§cCan't find player with name §4" + args[0]);
			}else sender.sendMessage("§cUtilisez /chest [nom] [nombre]");
		}else sender.sendMessage("§cVous croyiez vraiment pouvoir faire ça sans être opérateur ?");
		return false;
	}

}
