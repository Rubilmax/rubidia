package me.pmilon.RubidiaMonsters.pathfinders;

import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_15_R1.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PathfinderGoalMeleeAttack extends PathfinderGoal {

    World a;
    EntityCreature b;
    int c;
    double d;
    boolean e;
    PathEntity f;
	Class g;
    private int h;
    private double i;
    private double j;
    private double k;

    public PathfinderGoalMeleeAttack(EntityCreature entitycreature, Class oclass, double d0, boolean flag) {
        this(entitycreature, d0, flag);
        this.g = oclass;
    }

    public PathfinderGoalMeleeAttack(EntityCreature entitycreature, double d0, boolean flag) {
        this.b = entitycreature;
        this.a = entitycreature.world;
        this.d = d0;
        this.e = flag;
        //this.a(3);
    }

    public boolean a() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else if (!this.b.getEntitySenses().a(entityliving)) {
            return false;
        } else if (this.g != null && !this.g.isAssignableFrom(entityliving.getClass())) {
            return false;
        } else {
        	this.f = this.b.getNavigation().a(entityliving, 1);
            return this.f != null;
        }
    }

    public void c() {
        if(this.e)this.b.getNavigation().a(this.f, this.d);
        this.h = 0;
    }

    public void e() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if(entityliving != null){
            this.b.getControllerLook().a(entityliving, 30.0F, 30.0F);
            double d0 = this.b.g(entityliving.locX(), entityliving.locY(), entityliving.locZ());
            double d1 = (double) (this.b.getWidth() + entityliving.getWidth() * 2.0F);

            --this.h;
            if (this.e && this.h <= 0 && (this.i == 0.0D && this.j == 0.0D && this.k == 0.0D || entityliving.g(this.i, this.j, this.k) >= 1.0D || this.b.getRandom().nextFloat() < 0.05F)) {
                this.i = entityliving.locX();
                this.j = entityliving.getWidth();
                this.k = entityliving.locZ();
                this.h = 4 + this.b.getRandom().nextInt(7);
                if (d0 > 1024.0D) {
                    this.h += 10;
                } else if (d0 > 256.0D) {
                    this.h += 5;
                }

                if (!this.b.getNavigation().a((Entity) entityliving, this.d)) {
                    this.h += 15;
                }
            }

            this.c = Math.max(this.c - 1, 0);
            if (d0 <= d1 && this.c <= 0) {
                this.c = 20;

            	((LivingEntity)entityliving.getBukkitEntity()).damage(1.0, this.b.getBukkitEntity());
            }
        }
    }
}