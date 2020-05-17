package me.pmilon.RubidiaQuests.pathfinders;

import java.util.Random;

import org.bukkit.Location;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import net.minecraft.server.v1_15_R1.*;

public class PathfinderGoalRandomWalk extends PathfinderGoal {

    EntityCreature b;
    PNJHandler h;
    Random r = new Random();
    
    public PathfinderGoalRandomWalk(EntityCreature b, PNJHandler h){
    	this.b = b;
    	this.h = h;
    }

	@Override
	public boolean a() {
		if(!this.h.isFix()){
			if(Core.toPlayerList(this.b.getBukkitEntity().getNearbyEntities(4.2, 4.2, 4.2)).isEmpty()){
				return this.r.nextInt(1000) < 80;
			}
		}
		return true;
	}
    
    @Override
    public void c(){
		double range = 2.8;
		Location location = this.h.getLocation().clone();
		Location clone = location.clone();
		clone.add(this.r.nextDouble()*range, 0, this.r.nextDouble()*range);
		while(clone.distanceSquared(location) < 1.05){
			clone = location.clone().add(this.r.nextDouble()*range, 0, this.r.nextDouble()*range);
		}
		this.b.getNavigation().a(clone.getX(), clone.getY(), clone.getZ(), .5);
    }

}