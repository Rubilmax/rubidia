package me.pmilon.RubidiaCore.abilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.metadata.FixedMetadataValue;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ItemMessage;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.events.RPlayerAbilityEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;

public abstract class Ability {
	
	private final String name;
	private final List<String> description;
	private final RClass rClass;
	private final int index;
	private final boolean passive;
	private final String sequence;
	private final String suppInfo;
	private final String unit;
	private final int toggleableTicks;
	private final boolean melee;
	public Ability(String name, List<String> description, RClass rClass, int index, boolean passive,
			String sequence, String suppInfo, String unit, int toggleableTicks, boolean melee){
		this.name = name;
		this.description = description;
		this.rClass = rClass;
		this.index = index;
		this.passive = passive;
		this.sequence = sequence;
		this.suppInfo = suppInfo;
		this.unit = unit;
		this.toggleableTicks = toggleableTicks;
		this.melee = melee;
	}
	
	public List<String> getDescription() {
		return description;
	}

	public boolean isPassive() {
		return passive;
	}

	public String getSequence() {
		return sequence;
	}

	public String getName() {
		return name;
	}

	public void execute(final RPlayer rp, Event event){
		final Player player = rp.getPlayer();
		double vigorCost = this.getVigorCost(rp);
		if(rp.hasVigor(vigorCost)){
			RPlayerAbilityEvent rEvent = new RPlayerAbilityEvent(rp, this);
			Bukkit.getPluginManager().callEvent(rEvent);
			if(!rEvent.isCancelled()){
				if(rp.isActiveAbility(getInstance().getRAbility())){
					if(this.isToggleable()) {
						BukkitTask.tasks.get(player.getMetadata("abilityTask" + this.getIndex()).get(0).asInt()).cancel();
					}
				}else{
					if(!getInstance().isPassive()) {
						ItemMessage.sendMessage(player, "§2§l> Compétence   §a" + this.getName() + "§l   " + (this.isToggleable() ? (!rp.isActiveAbility(getInstance().getRAbility()) ? "§cdésactivée" : "§eactivée") : "§e-" + Utils.round(vigorCost, 1) + " §6§lEP"), 2);
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						rp.setActiveAbility(getInstance().getRAbility(), true);
					}
					
					this.run(rp, event);

					if(this.isToggleable()) {
						player.setMetadata("abilityTask" + this.getIndex(), new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){
							int step2 = 0;

							@Override
							public void run() {
								double vigor = getInstance().getVigorCost(rp)/4.;
								if(rp.hasVigor(vigor)){
									rp.addVigor(-vigor);
								}else{
									getInstance().vigorless(player);
									this.cancel();
								}
								
								if(step2 >= getInstance().getToggleableTicks()/5.){
									getInstance().onEffect(rp);
									step2 = 0;
								}
								
								step2++;
							}

							@Override
							public void onCancel() {
								getInstance().onCancel(rp);
								rp.setActiveAbility(getInstance().getRAbility(), false);
							}
							
						}.runTaskTimer(0, 5).getTaskId()));
					} else if(!getInstance().isPassive()) {
						this.takeVigor(rp);
					}
				}
			}
		}else this.vigorless(player);
	}
	
	public void vigorless(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
		ItemMessage.sendMessage(player, "§cPas assez de vigueur !", 40);
	}
	
	public abstract void run(RPlayer rp, Event event);
	
	public abstract void animate(RPlayer rp, LivingEntity target);
	
	public void damage(RPlayer rp, List<? extends Entity> targets, double... factor) {
		for(LivingEntity target : DamageManager.toDamageableEntityList(targets)) {
			if(!target.equals(rp.getPlayer())) {
				if(DamageManager.damage(target, rp.getPlayer(), this.getDamages(rp)*(factor.length > 0 ? factor[0] : 1.), RDamageCause.ABILITY)) {
					this.animate(rp, target);
				}
			}
		}
	}
	
	//for toggleable abilities only
	public abstract void onEffect(RPlayer rp);
	public abstract void onCancel(RPlayer rp);
	
	public void takeVigor(RPlayer rp) {
		rp.addVigor(-this.getVigorCost(rp));
	}

	public Mastery getMastery() {
		if(this.getIndex() <= 4) {
			return Mastery.ADVENTURER;
		} else if(this.getIndex() <= 6) {
			return Mastery.ASPIRANT;
		} else if(this.getIndex() <= 8) {
			return Mastery.SPECIALIST;
		} else if(this.getIndex() <= 10) {
			return Mastery.EXPERT;
		} else if(this.getIndex() <= 11) {
			return Mastery.MASTER;
		} else if(this.getIndex() <= 12) {
			return Mastery.HERO;
		}
		return Mastery.VAGRANT;
	}

	public String getSuppInfo() {
		return suppInfo;
	}

	public String getUnit() {
		return unit;
	}

	public int getToggleableTicks() {
		return toggleableTicks;
	}
	
	public boolean isToggleable() {
		return toggleableTicks > 0;
	}

	public double getDamages(RPlayer rp) {
		return rp.getAbilityLevel(this.getRAbility())*this.getSettings().getDamagesPerLevel()+this.getSettings().getDamagesMin();
	}
	
	public double getVigorCost(RPlayer rp) {
		return rp.getAbilityLevel(this.getRAbility())*this.getSettings().getVigorPerLevel()+this.getSettings().getVigorMin();
	}

	public RAbilitySettings getSettings() {
		return RAbilitySettings.getSettings(this.getRClass(), this.getIndex());
	}

	public RClass getRClass() {
		return rClass;
	}

	public int getIndex() {
		return index;
	}
	
	public Ability getInstance() {
		return this;
	}
	
	public RAbility getRAbility() {
		return RAbility.valueOf(this.getRClass().toString() + "_" + this.getIndex());
	}

	public boolean isMelee() {
		return melee;
	}
	
}
