package me.pmilon.RubidiaMonsters.entities;

import me.pmilon.RubidiaMonsters.pathfinders.PathfinderGoalCustomAttack;
import me.pmilon.RubidiaMonsters.pathfinders.PathfinderGoalMeleeAttack;
import me.pmilon.RubidiaMonsters.regions.Monster;
import net.minecraft.server.v1_13_R2.*;

public class SDefaultEntity {
	
	public static void setAttackPathfinders(Monster monster, EntityInsentient entity) {
		entity.targetSelector = new PathfinderGoalSelector(entity.world.methodProfiler);
		entity.goalSelector = new PathfinderGoalSelector(entity.world.methodProfiler);
        
		if(entity instanceof EntityCreature){
			EntityCreature creature = (EntityCreature)entity;
			boolean flag = monster.getAttacks().isEmpty();
			if(entity instanceof EntityAnimal || !flag){
				creature.goalSelector = new PathfinderGoalSelector(creature.world.methodProfiler);
				//creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
				creature.goalSelector.a(2, new PathfinderGoalMeleeAttack(creature, 1.48D, flag));
				creature.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(creature, 1.0D));
				creature.goalSelector.a(7, new PathfinderGoalRandomStroll(creature, 1.0D));
				creature.goalSelector.a(8, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 8.0F));
				if(flag)creature.goalSelector.a(8, new PathfinderGoalRandomLookaround(creature));
				if(!flag)creature.goalSelector.a(2, new PathfinderGoalCustomAttack(monster, creature));
			}
			if(monster.isEnraged())creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(creature, EntityHuman.class, true));
		}
	}
	
}
