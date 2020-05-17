package me.pmilon.RubidiaCore.REvents;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.Material;

public class Event {

	public enum EventType {
		
		XP(Material.EXPERIENCE_BOTTLE),
		RUBIS(Material.EMERALD);
		
		private final Material material;
		private EventType(Material material){
			this.material = material;
		}
		
		public Material getMaterial() {
			return material;
		}
		
	}
	
	private EventType type;
	private String subtitle;
	private long startDate;
	private long duration;
	private double factor;
	private boolean started;
	public Event(EventType type, String subtitle, long startDate, long duration, double factor, boolean started){
		this.type = type;
		this.subtitle = subtitle;
		this.startDate = startDate;
		this.duration = duration;
		this.factor = factor;
		this.started = started;
	}
	
	public EventType getType() {
		return type;
	}
	
	public void setType(EventType type) {
		this.type = type;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
		this.started = false;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}
	
	public boolean isActive(){
		return this.isStarted() && System.currentTimeMillis() <= this.getStartDate()+this.getDuration();
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public void start(){
		this.startDate = System.currentTimeMillis();
		this.started = true;
		for(RPlayer rp : RPlayer.getOnlines()){
			rp.sendTitle(("§5§lEVENEMENT EN COURS"), "§d" + this.getSubtitle(), 0, 100, 40);
		}
	}
	
	public void finish(){
		for(RPlayer rp : RPlayer.getOnlines()){
			rp.sendTitle(("§5§lEVENEMENT EN COURS"), "§d" + this.getSubtitle(), 0, 100, 40);
		}
		this.started = false;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
}
