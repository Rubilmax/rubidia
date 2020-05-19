package me.pmilon.RubidiaMonsters.pathfinders;

import java.util.List;

import org.bukkit.entity.LivingEntity;

import me.pmilon.RubidiaMonsters.attacks.AbstractAttack;
import me.pmilon.RubidiaMonsters.regions.Monster;
import net.minecraft.server.v1_13_R2.*;

public class PathfinderGoalCustomAttack extends PathfinderGoal {

	List<AbstractAttack> attacks;
	EntityCreature c;
	AbstractAttack a;
	int t = 0;
	public PathfinderGoalCustomAttack(Monster monster, EntityCreature creature){
		this.c = creature;
		this.attacks = monster.getAttacks();
	}
	
	@Override
	public boolean a() {
		this.t++;
		EntityLiving entityliving = this.c.getGoalTarget();
		if(entityliving == null){
			return false;
		}else if(!entityliving.isAlive()){
			return false;
		}else if(!this.c.getEntitySenses().a(entityliving)){
			return false;
		}else{
			int size = this.attacks.size();
			double s = 0.0;
			double[] fitness = new double[size];
			for(int i = 0;i < size;i++){
				AbstractAttack a = this.attacks.get(i);
				fitness[i] = this.t > a.getCooldown() ? a.getFitness((LivingEntity)this.c.getBukkitEntity(), (LivingEntity)entityliving.getBukkitEntity()) : 0;
				s += fitness[i];
			}
			if(s > 0){
				double x = this.c.getRandom().nextDouble();
				double t = 0.0;
				for(int i = 0;i < size;i++){
					AbstractAttack a = this.attacks.get(i);
					t += fitness[i]/s;
					if(x < t){
						this.a = a;
						break;
					}
				}
			}
			return this.a != null;
		}
	}
	
	@Override
	public void c(){
		this.a.run((LivingEntity)this.c.getBukkitEntity(), (LivingEntity)this.c.getGoalTarget().getBukkitEntity());
		this.t = 0;
		this.a = null;
	}

}
