package me.pmilon.RubidiaPaths.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.sqlite.util.StringUtils;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.math.BlockVector3;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaPaths.SecretPathsManager;
import me.pmilon.RubidiaPaths.paths.SecretPath;
import me.pmilon.RubidiaPaths.paths.SecretPathColl;

public class PortalCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("create")){
				if(args.length > 3){
					LocalSession session = SecretPathsManager.we.getSession(player);
					if(session != null) {
						if(session.getSelectionWorld() != null) {
							if(session.isSelectionDefined(session.getSelectionWorld())){
								try {
									BlockVector3 b = session.getSelection(session.getSelectionWorld()).getMinimumPoint();
									BlockVector3 t = session.getSelection(session.getSelectionWorld()).getMaximumPoint();
									Location bottom = new Location(player.getWorld(), b.getBlockX(), b.getBlockY(), b.getBlockZ());
									Location top = new Location(player.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ());
									String argtitles = StringUtils.join(Arrays.asList(args).subList(3, args.length), " ");
									String[] titles = argtitles.split("&&");
									if(titles.length > 1) {
										SecretPath path = SecretPathColl.get(args[1]);
										if(path != null) {
											SecretPathColl.paths.remove(path);
										}
										SecretPathColl.paths.add(new SecretPath(args[1], titles[0].trim(), titles[1].trim(), args[2], player.getLocation(), bottom, top));
										rp.sendMessage("§aLe portail §2" + args[1] + " §aa été créé !");
									} else rp.sendMessage("§cSpécifiez un titre et un sous-titre : [title.../null&&subtitle.../null]");
								} catch (IncompleteRegionException e) {
									rp.sendMessage("§cSélectionnez une région complète");
								}
							}else rp.sendMessage("§cVous n'avez pas sélectionné de région");
						}else rp.sendMessage("§cVous n'avez pas sélectionné de région");
					}else rp.sendMessage("§cVous n'avez pas sélectionné de région");
				}else{
					rp.sendMessage("§cUtilisez /portal create [Nom] [Cible] [title.../null&&subtitle.../null]");
				}
			}else if(args[0].equalsIgnoreCase("remove")){
				if(args.length > 1){
					SecretPath path = SecretPath.get(args[1]);
					if(path != null){
						SecretPathColl.paths.remove(path);
						Configs.getPathConfig().set("portals." + args[1], null);
						Configs.savePathConfig();
						rp.sendMessage("§2" + args[1] + " §aa été supprimé !");
					}else{
						rp.sendMessage("§4" + args[1] + "§c n'existe pas !");
					}
				}else{
					rp.sendMessage("§cUtilisez /portal remove [Nom]");
				}
			}else if(args[0].equalsIgnoreCase("list")){
				List<String> paths = new ArrayList<String>();
				for(SecretPath path : SecretPathColl.paths) {
					if(!paths.contains(path.getName()) && !paths.contains(path.getTargetName())) {
						SecretPath target = SecretPathColl.get(path.getTargetName());
						if(target == null) {
							rp.sendMessage("§7" + path.getName() + " §f> §7" + path.getTargetName() + " §f: " + ChatColor.translateAlternateColorCodes('&', path.getTitle()) + "§r " + ChatColor.translateAlternateColorCodes('&', path.getSubtitle()));
						} else {
							rp.sendMessage("§7" + path.getName() + " §f> §7" + path.getTargetName() + " §f: " + ChatColor.translateAlternateColorCodes('&', path.getTitle()) + "§r " + ChatColor.translateAlternateColorCodes('&', path.getSubtitle()) + " §7> " + ChatColor.translateAlternateColorCodes('&', target.getTitle()) + "§r " + ChatColor.translateAlternateColorCodes('&', target.getSubtitle()));
						}
						paths.add(path.getName());
						paths.add(path.getTargetName());
					}
				}
			}else{
				rp.sendMessage("§cUtilisez /portal [create/remove/list]");
			}
		}else{
			rp.sendMessage("§cUtilisez /portal [create/remove/list]");
		}
	}

}
