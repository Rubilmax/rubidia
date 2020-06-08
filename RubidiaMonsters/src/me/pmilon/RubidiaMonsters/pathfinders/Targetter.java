package me.pmilon.RubidiaMonsters.pathfinders;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_13_R2.EntityLiving;

public class Targetter {
	
	protected LivingEntity target;
	public Targetter(LivingEntity target) {
		this.setTarget(target);
	}
	
	public LivingEntity getTarget() {
		return target;
	}
	
	public void setTarget(LivingEntity target) {
		this.target = target;
	}
	
	public EntityLiving getTargetHandle() {
		if (this.getTarget() != null) return (EntityLiving) ((CraftEntity) this.getTarget()).getHandle();
		return null;
	}

}
