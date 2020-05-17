package me.pmilon.RubidiaPets.pets;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.pmilon.RubidiaCore.utils.Utils;

public class Pearl {

	public enum PearlType {

		INCREASE_DAMAGE("strength", "force", 0),
		DAMAGE_RESISTANCE("resistance", "résistance", 0),
		SPEED("speed", "vitesse", 0),
		JUMP("jump", "saut", 0),
		FAST_DIGGING("haste","célérité", 0),
		FIRE_RESISTANCE("fire resistance","résistance au feu", 2),
		WATER_BREATHING("water breathing","respiration aquatique", 1),
		HEALTH_BOOST("health","vie", 0),
		NIGHT_VISION("night vision","vision nocturne", 1),
		LUCK("luck","chance", 0);

		private final String displayen;
		private final String displayfr;
		private final int shift;
		private PearlType(String displayen, String displayfr, int shift){
			this.displayen = displayen;
			this.displayfr = displayfr;
			this.shift = shift;//only concerns fr display
		}
		
		public String[] getDisplay(){
			String[] display = new String[2];
			display[0] = this.displayen;
			display[1] = this.displayfr;
			return display;
		}
		
		public int getShift(){
			return shift;
		}
		
	}
	
	private PearlType type;
	private long startTime;
	private long duration;
	private int amplifier;
	public Pearl(PearlType type, long startTime, long duration, int amplifier){
		this.type = type;
		this.startTime = startTime;
		this.duration = duration;
		this.amplifier = amplifier;
	}
	
	public static Pearl fromString(String string){
		String[] type = string.split("\\{");
		if(type.length > 1){
			String[] parts = type[1].split("\\}")[0].split(",");
			if(parts.length > 2){
				try {
					return new Pearl(PearlType.valueOf(type[0]),Long.valueOf(parts[1]), Long.valueOf(parts[2]), Integer.valueOf(parts[0]));
				}catch (Exception ex){
				}
			}
		}
		return null;
	}

	public PearlType getType() {
		return type;
	}

	public void setType(PearlType type) {
		this.type = type;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}
	
	public void addEffect(Player player){
		PotionEffectType type = PotionEffectType.getByName(this.getType().toString());
		if(type != null){
			Utils.addSafeBuff(player, new PotionEffect(type, 60, this.getAmplifier()-1, true, false));
		}
	}
	
	public String toString(){
		return this.getType().toString() + "{" + this.getAmplifier() + "," + this.getStartTime() + "," + this.getDuration() + "}";
	}
	
	public boolean isValid(){
		return System.currentTimeMillis() <= this.getStartTime()+this.getDuration();
	}
}
