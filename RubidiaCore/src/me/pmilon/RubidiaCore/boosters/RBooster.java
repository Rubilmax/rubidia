package me.pmilon.RubidiaCore.boosters;

import org.bukkit.Location;
import org.bukkit.Particle;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

public class RBooster extends Buff {
	
	public enum RBoosterType {
		
		XP(Particle.VILLAGER_HAPPY,BuffType.XP,100,20),
		HP(Particle.HEART,BuffType.MAX_HEALTH,30,6),
		ATQ(Particle.FLAME,BuffType.ATTACK_SPEED,20,5),
		DEF(Particle.ENCHANTMENT_TABLE,BuffType.DEFENSE,20,4),
		VOL(Particle.CRIT_MAGIC,BuffType.LIFT_COST,-25,3);

		private final Particle particle;
		private final BuffType type;
		private final int level;
		private final int cost;
		private RBoosterType(Particle particle, BuffType type, int level, int cost) {
			this.particle = particle;
			this.type = type;
			this.level = level;
			this.cost = cost;
		}
		
		public Particle getParticle() {
			return particle;
		}

		public BuffType getType() {
			return type;
		}

		public int getLevel() {
			return level;
		}

		public int getCost() {
			return cost;
		}
		
	}
	
	private final RPlayer rp;
	private final RBoosterType boosterType;
	private BukkitTask task;
	public RBooster(RPlayer rp, RBoosterType type) {
		super(0, type.getType(), type.getLevel());
		this.rp = rp;
		this.boosterType = type;
	}
	
	public void play(){
		for(double i = 0;i <= 4*Math.PI;i += Math.PI/8){
			final double j = i;
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					Location location = getRP().getPlayer().getLocation().add(.6*Math.cos(j),.15*j,.6*Math.sin(j));
					location.getWorld().spawnParticle(getBoosterType().getParticle(), location, 1, 0, 0, 0, .05);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater((long) (i*2));
		}
	}
	
	public void start(){
		getRP().getActiveRBoosters().add(this);
		/*if(this.getBoosterType().equals(RBoosterType.HP)) {
			getRP().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getRP().getMaxHealth());
		}*///handled in Core.task3
		this.setTask(new BukkitTask(Core.instance){
			int step = 0;
			int timer = (int) (60*20/2.);
			double angle = 0.0;

			@Override
			public void run() {
				if(getRP().isOnline()){
					Location location = getRP().getPlayer().getLocation().add(.6*Math.cos(angle),.1,.6*Math.sin(angle));
					location.getWorld().spawnParticle(getBoosterType().getParticle(), location, 1, 0, 0, 0, .05);
					angle += Math.PI/8;
					if(step % timer == 0){
						if(getRP().getRenom() >= getBoosterType().getCost() || getRP().isOp()){
							if(!getRP().isOp()) {
								getRP().setRenom(getRP().getRenom()-getBoosterType().getCost());
							}
						}else stop();
					}
					step++;
				} else stop();
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0,2));
	}
	
	public void stop(){
		play();
		getRP().getActiveRBoosters().remove(this);
		/*if(getRP().isOnline() && this.getBoosterType().equals(RBoosterType.HP)) {
			getRP().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getRP().getMaxHealth());
		}*///handled in Core.task3
		if(this.getTask() != null){
			this.getTask().cancel();
			this.setTask(null);
		}
	}

	public RBoosterType getBoosterType() {
		return boosterType;
	}

	public BukkitTask getTask() {
		return task;
	}

	public void setTask(BukkitTask task) {
		this.task = task;
	}

	public RPlayer getRP() {
		return rp;
	}
	
	
}
