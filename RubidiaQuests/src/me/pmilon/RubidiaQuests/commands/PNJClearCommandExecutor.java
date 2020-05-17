package me.pmilon.RubidiaQuests.commands;

import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PNJSession;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

public class PNJClearCommandExecutor implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender.isOp()){
			for(PNJSession session : PNJManager.pnjs.values()){
				PNJHandler pnj = session.getPNJHandler();
				Villager villager = pnj.getEntity();
				if(villager != null){
					for(Entity entity : villager.getNearbyEntities(5, 5, 5)){
						if(entity.hasMetadata("display")){
							entity.remove();
						}else if(entity.getCustomName() != null){
							if(entity.getCustomName().startsWith(session.getPNJHandler().getTitlePrefix()) || entity.getCustomName().startsWith(session.getPNJHandler().getNamePrefix())){
								entity.remove();
							}
						}
					}
				}
				pnj.kill();
			}
		}
		return false;
	}

}