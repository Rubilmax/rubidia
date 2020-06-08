package me.pmilon.RubidiaMonsters.pathfinders;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathEntity;
import net.minecraft.server.v1_13_R2.PathfinderGoal;

public class PathfinderGoalMeleeAttack extends PathfinderGoal {

	final Targetter targetter;
    final EntityCreature creature;
    final double speed;
    
    int cooldown;
    int defaultCooldown;
    PathEntity pathEntity;
    private int counter;
    
    private double i;
    private double j;
    private double k;

    public PathfinderGoalMeleeAttack(Targetter targetter, EntityCreature entitycreature, double d0) {
        this(targetter, entitycreature, d0, 30);
    }
    public PathfinderGoalMeleeAttack(Targetter targetter, EntityCreature entitycreature, double d0, int defaultCooldown) {
    	this.targetter = targetter;
        this.creature = entitycreature;
        this.defaultCooldown = defaultCooldown;
        this.speed = d0;
        this.a(3);
    }

    public boolean a() {
        EntityLiving entityliving = this.targetter.getTargetHandle();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else {
            this.pathEntity = this.creature.getNavigation().a(entityliving);
            return this.pathEntity != null;
        }
    }

    public void c() {
        this.creature.getNavigation().a(this.pathEntity, this.speed);
        this.counter = 0;
    }

    public void e() {
        EntityLiving entityliving = this.targetter.getTargetHandle();

        this.creature.getControllerLook().a(entityliving, 30.0F, 30.0F);
        double d0 = this.creature.d(entityliving.locX, entityliving.locY, entityliving.locZ);
        double d1 = (double) (this.creature.width * 2.0F * this.creature.width * 2.0F + entityliving.length);

        --this.counter;
        if (this.creature.getEntitySenses().a(entityliving) && this.counter <= 0 && (this.i == 0.0D && this.j == 0.0D && this.k == 0.0D || entityliving.e(this.i, this.j, this.k) >= 1.0D || this.creature.getRandom().nextFloat() < 0.05F)) {
            this.i = entityliving.locX;
            this.j = entityliving.length;
            this.k = entityliving.locZ;
            
            this.counter = 4 + this.creature.getRandom().nextInt(7);
            if (d0 > 1024.0D) {
                this.counter += 10;
            } else if (d0 > 256.0D) {
                this.counter += 5;
            }

            if (!this.creature.getNavigation().a(entityliving, this.speed)) {
                this.counter += 15;
            }
        }

        this.cooldown = Math.max(this.cooldown - 1, 0);
        if (d0 <= d1 && this.cooldown <= 0) {
            this.cooldown = this.defaultCooldown;

        	this.targetter.getTarget().damage(1.0, this.creature.getBukkitEntity());
        }
    }
}