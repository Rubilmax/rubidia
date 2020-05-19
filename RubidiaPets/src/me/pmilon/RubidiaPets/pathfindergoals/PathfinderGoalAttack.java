package me.pmilon.RubidiaPets.pathfindergoals;

import org.bukkit.entity.LivingEntity;

import me.pmilon.RubidiaPets.pets.Pet;
import net.minecraft.server.v1_13_R2.*;

public class PathfinderGoalAttack extends PathfinderGoal {

    EntityCreature b;
    Pet p;
    int t = 30;

    public PathfinderGoalAttack(EntityCreature c, Pet p) {
        this.b = c;
        this.p = p;
    }

    @Override
    //TODO test new Pathfinder
    public boolean a() {
    	this.t++;
        EntityLiving entityliving = this.b.getGoalTarget();
        if(entityliving != null){
            double d0 = this.b.d(entityliving.locX, (entityliving.getBoundingBox().maxY+entityliving.getBoundingBox().minY)/2./* entityliving.getBoundingBox().b */, entityliving.locZ);
            double d1 = (double) (this.b.width * 2.0F * this.b.width * 2.0F + entityliving.width);
            return d0 <= d1 && this.b.getHealth() > 1.0 && this.t > 20.0/this.p.getAttackSpeed() && this.b.getEntitySenses().a(entityliving);
        }
    	return false;
    }
    
    @Override
    public void c(){
    	this.t = 0;
    	((LivingEntity)this.b.getGoalTarget().getBukkitEntity()).damage(1.0, this.b.getBukkitEntity());
    }
}