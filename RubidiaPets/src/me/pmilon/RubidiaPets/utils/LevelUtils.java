package me.pmilon.RubidiaPets.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class LevelUtils {

	public static double getPLevelTotalExp(int level){
		if(level < 5)return (level+1)*10.3+21.2;
		else if(level < 15)return (level+1)*27.4+53.5;
		else if(level < 30)return (level+1)*47.9+94.8;
		else return (level+1)*61.3+148.4;
	}

	public static void firework(Location location) {
		Firework f = (Firework) location.getWorld().spawn(location, Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.STAR)
				.withColor(Color.LIME)
				.withColor(Color.MAROON)
				.withFade(Color.ORANGE)
				.withFade(Color.GREEN)
				.build());
		fm.setPower(0);
		f.setFireworkMeta(fm);
	}
}
