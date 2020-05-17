package me.pmilon.RubidiaMonsters.utils;

import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.levels.RLevels;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.utils.Settings;

public class Utils {

	public static double getXPFactor(double level){
		double xpAtAverageLevel = RLevels.getRLevelTotalExp((int) level);
		double suggestXPPercent = 1.0/(10+470.0*(level/Settings.LEVEL_MAX));
		return xpAtAverageLevel*suggestXPPercent/level;
	}
	public static double getHPFactor(double level){
		double defenseAtAverageLevel = getDefenseAtAverageLevel(level);
		Weapon weapon = Weapons.nearest((int)level, true, null, null, Rarity.COMMON);
		double damagesAtAverageLevel = weapon != null ? defenseAtAverageLevel > 0 ? ((weapon.getMinDamages()+weapon.getMaxDamages())/2.0)*(10.0/defenseAtAverageLevel) : 0.0 : 0.0;
		double suggestHit = 1.5+6.5*(level/Settings.LEVEL_MAX);
		return suggestHit*damagesAtAverageLevel/level;
	}
	public static double getDFactor(double level){
		double suggestMobHit = 12.0-8.0*(level/Settings.LEVEL_MAX);
		double defenseAtAverageLevel = getDefenseAtAverageLevel(level);
		double suggestMobDamages = defenseAtAverageLevel > 0 ? (30.0/suggestMobHit)*(defenseAtAverageLevel/10.0) : 0;
		return suggestMobDamages/level;
	}
	public static double getDefenseAtAverageLevel(double level){
		double defenseAtAverageLevel = 0;
		for(String type : new String[]{"_HELMET","_CHESTPLATE","_LEGGINGS","_BOOTS"}){
			Weapon weapon = Weapons.nearest((int)level, false, null, type, Rarity.COMMON);
			if(weapon != null)defenseAtAverageLevel+=DamageManager.getDefensePoint(weapon.getNewItemStack(null), RDamageCause.MELEE, true);
		}
		return defenseAtAverageLevel;
	}
	
}
