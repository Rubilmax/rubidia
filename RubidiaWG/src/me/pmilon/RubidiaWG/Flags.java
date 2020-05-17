package me.pmilon.RubidiaWG;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;

public class Flags {

	public static StateFlag DUELS = new StateFlag("duel-requests", true);
	public static StateFlag SOIL_TRAMPLING = new StateFlag("soil-trampling", true);
	//public static StringFlag MUSIC = new StringFlag("music");
	public static StateFlag BLOCKS = new StateFlag("use-blocks", true);
	public static StateFlag SKILLS = new StateFlag("use-skills", true);
	public static StateFlag RIDE = new StateFlag("ride-vehicles", true);
	public static StateFlag CLAIM = new StateFlag("claim", true);
	public static StringFlag LEAVE_COMMAND = new StringFlag("leave-command");
	public static StateFlag NATURAL_SPAWN = new StateFlag("natural-mob-spawning", true);
	public static StringFlag ENTER_TITLE = new StringFlag("enter-title");
	public static StringFlag LEAVE_TITLE = new StringFlag("leave-title");
	public static StateFlag REGEN = new StateFlag("auto-regen", true);
	
}
