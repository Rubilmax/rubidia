package me.pmilon.RubidiaQuests.dialogs;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class DialogManager {

	private static final HashMap<Player, PNJDialog> dialogs = new HashMap<Player, PNJDialog>();
	
	public static boolean isInDialog(Player p){
		return dialogs.containsKey(p);
	}
	
	public static void setInDialog(Player p, PNJDialog dialog){
		if(!isInDialog(p)){
			dialogs.put(p, dialog);
			p.setGravity(false);
			if(p.isOnGround())p.teleport(p.getLocation().add(0,.3,0));
		}
	}
	
	public static void setNoDialog(Player p){
		if(isInDialog(p)){
			p.removePotionEffect(PotionEffectType.BLINDNESS);
			p.setGravity(true);
			PNJDialog dialog = dialogs.remove(p);
			if(dialog != null){
				dialog.tpTask.cancel();
			}
		}
	}
	
	public static PNJDialog getDialog(Player p){
		if(isInDialog(p)){
			return dialogs.get(p);
		}
		return null;
	}
}
