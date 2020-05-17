package me.pmilon.RubidiaManager.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

public class EndRegenTask extends AbstractTask {
	
	public EndRegenTask(RubidiaManagerPlugin plugin) {
		super(plugin);
	}

	@Override
	public void runTaskSynchronously() {
		RPlayer.broadcastMessage("§4RÉGÉNÉRATION DES ENFERS...");
		RPlayer.broadcastTitle("§4RÉGÉNÉRATION DES ENFERS...","",0,100,40);
		Core.console.sendMessage("§eRegenerating The End...");
		if(RubidiaManagerPlugin.multiverseCore.getCore().getMVWorldManager().regenWorld("Rubidia_the_end", true, true, null)) {
			World world = Bukkit.getWorld("Rubidia_the_end");
			if(world != null){
				for(Entity entity : world.getEntities()){
					if(entity.getType().equals(EntityType.ENDER_DRAGON)){
						((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(4444.0);
					}
				}
			}
			Core.console.sendMessage("§eThe End regenerated!");
			RPlayer.broadcastMessage("§2RÉGÉNÉRATION TERMINÉE");
			RPlayer.broadcastTitle("§2RÉGÉNÉRATION TERMINÉE","",0,100,40);
		} else Core.console.sendMessage("§cUnable to regen The End.");
	}

}
