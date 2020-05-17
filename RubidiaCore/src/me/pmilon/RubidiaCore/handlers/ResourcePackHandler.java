package me.pmilon.RubidiaCore.handlers;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

public class ResourcePackHandler implements Listener {
	
	public static final String RESOURCE_PACK_VERSION = "2.4.6";
	
	@EventHandler
	public void onResourcePackStatus(PlayerResourcePackStatusEvent event){
		final Player player = event.getPlayer();
		Status status = event.getStatus();
		if(!player.isOp()){
			if(status.equals(Status.FAILED_DOWNLOAD)){
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						player.kickPlayer("§4§lLe téléchargement de notre resource pack a échoué.\n\n\n§cTentez de vous reconnecter.\nSi le problème persiste, supprimez votre dossier :\n\n§e§o.minecraft/server-resource-packs/\n\n§cpuis reconnectez-vous.");
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}else if(status.equals(Status.DECLINED)){
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						player.kickPlayer("§4§lNotre resource pack est indispensable.\n\n\n§cAutorisez-le dans le menu :\n\n§e§oMultijoueur > Rubidia > Modifier > Packs de ressources");
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}
		}
	}
}