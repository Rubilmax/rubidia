package me.pmilon.RubidiaMonsters.attacks;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class AbstractAttack {

	public static abstract class Attack {

		private final int defaultCooldown;
		private final double[] defaultWeights;
		private final double[] defaultFitnessFactors;
		public Attack(int defaultCooldown, double[] defaultWeights, double[] defaultFitnessFactors){
			this.defaultCooldown = defaultCooldown;
			this.defaultWeights = defaultWeights;
			this.defaultFitnessFactors = defaultFitnessFactors;
		}
		
		public abstract void run(AbstractAttack attack, LivingEntity attacker, LivingEntity target, double learningFactor, double[] weights);
		
		public abstract double getFitness(LivingEntity attacker, LivingEntity target, double[] factors);

		public int getDefaultCooldown() {
			return defaultCooldown;
		}

		public double[] getDefaultFitnessFactors() {
			return defaultFitnessFactors;
		}

		public double[] getDefaultWeights() {
			return defaultWeights;
		}
		
	}
	
	public static abstract class AttackTracker implements Listener {

		private final Plugin plugin;
		public AttackTracker(Plugin plugin) {
			this.plugin = plugin;
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
		
		public abstract void track();
		
		public void untrack(){
			HandlerList.unregisterAll(this);
		}
		
		public void backPropagate(AbstractAttack attack, LivingEntity attacker, LivingEntity target, double learningFactor){
			this.propagateError(attack, learningFactor, this.getErrors(attacker, target));
			this.untrack();
		}
		
		public abstract void propagateError(AbstractAttack attack, double learningFactor, double[] errors);
		
		public abstract double[] getErrors(LivingEntity attacker, LivingEntity target);
		
		public double plug(double expected, double result){
			double r = this.sigma(result);
			double x = this.sigma(expected);
			return r*(1-r)*(x-r);
		}
		
		public double sigma(double value){
			return 1.0/(1.0+Math.exp(-value));
		}

		public Plugin getPlugin() {
			return plugin;
		}
		
	}
	
	public static enum AttackType {
		
		LEAP_ATTACK(new Attack(20, new double[]{.5,.5,.5,.5,.5,.5}, new double[]{0.0}){

			@Override
			public void run(final AbstractAttack attack, final LivingEntity attacker, final LivingEntity target, final double learningFactor, final double[] weights) {
				final Vector link = target.getLocation().toVector().subtract(attacker.getLocation().toVector());
				final double startHeight = target.getLocation().getY();
				link.add(target.getVelocity().multiply(weights[2]));
				link.normalize().multiply(weights[1]);
				link.setY(link.getY()+weights[0]);
				attacker.setVelocity(link);
				new AttackTracker(RubidiaMonstersPlugin.instance){
					double maxHeightDiff = 0.0;

					@Override
					public double[] getErrors(LivingEntity attacker, LivingEntity target) {
						double heightError = this.plug(1.45, maxHeightDiff);
						double distanceError = this.plug(2.0, target.getLocation().distanceSquared(attacker.getLocation()));
						return new double[]{heightError, distanceError};
					}

					@Override
					public void propagateError(AbstractAttack attack, double learningFactor, double[] errors) {
						double[] weights = attack.getWeights();
						weights[0] = weights[0] + errors[0]*learningFactor;
						weights[1] = weights[1] + errors[1]*learningFactor;
						weights[2] = weights[2] + errors[1]*learningFactor;
						attack.setWeights(weights);
					}

					@Override
					public void track() {
						new BukkitTask(RubidiaMonstersPlugin.instance){

							@Override
							public void run() {
								if(attacker.isOnGround()){
									this.cancel();
								}else{
									double y = target.getLocation().getY()-startHeight;
									if(y > maxHeightDiff){
										maxHeightDiff = y;
									}
								}
							}

							@Override
							public void onCancel() {
								backPropagate(attack, attacker, target, learningFactor);
							}
							
						}.runTaskTimer(0,0);
					}
					
				}.track();
			}

			@Override
			public double getFitness(LivingEntity attacker, LivingEntity target, double[] factors) {
				double distance = attacker.getLocation().distanceSquared(target.getLocation());
				if(distance > 10*10 || distance < 4*4){
					return 0.0;
				}
				return distance/64.0;
			}
			
		});
		
		private final Attack attack;
		private AttackType(Attack attack){
			this.attack = attack;
		}
		
		public Attack getAttack() {
			return attack;
		}
		
	}
	
	private AttackType type;
	private int cooldown;
	private double learningFactor;
	private double[] weights;
	private double[] factors;
	public AbstractAttack(AttackType type, int cooldown, double learningFactor, double[] weights, double[] factors){
		this.type = type;
		this.cooldown = cooldown;
		this.learningFactor = learningFactor;
		this.factors = factors;
		this.weights = weights;
	}
	
	public AbstractAttack(AttackType type){
		this.type = type;
		this.cooldown = type.getAttack().getDefaultCooldown();
		this.factors = type.getAttack().getDefaultFitnessFactors();
		this.weights = type.getAttack().getDefaultWeights();
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public double[] getFactors() {
		return factors;
	}

	public void setFactors(double[] factors) {
		this.factors = factors;
	}

	public AttackType getType() {
		return type;
	}

	public void setType(AttackType type) {
		this.type = type;
	}
	
	public void run(LivingEntity attacker, LivingEntity target){
		this.getType().getAttack().run(this, attacker, target, this.getLearningFactor(), this.getWeights());
	}
	
	public double getFitness(LivingEntity attacker, LivingEntity target){
		return this.getType().getAttack().getFitness(attacker, target, this.getFactors());
	}

	public double getLearningFactor() {
		return learningFactor;
	}

	public void setLearningFactor(double learningFactor) {
		this.learningFactor = learningFactor;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
}
