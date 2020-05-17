package me.pmilon.RubidiaCore.mage;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MageMeteor extends BukkitTask {
	
	private final Location origin;
	private final Location target;
	
    public MageMeteor(Location origin, Location target) {
		super(Core.instance);
		this.origin = origin;
		this.target = target;
	}
    
    public int particles = 2;
    
    public Player player;
    
    public void setPlayer(Player en){
    	if(en != null){
    		player = en;
    	}else{
    		player = null;
    	}
    }
    
	@Override
	public void run() {
        if (target == null) {
            cancel();
            return;
        }
        Vector link = target.toVector().subtract(origin.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        final Vector v = link.multiply(ratio);
        final Location loc = origin.clone().subtract(v);
        
        new BukkitTask(Core.instance){

			@Override
			public void run() {
                loc.add(v);
                loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1, 0, 0, 0, 1);
			}

			@Override
			public void onCancel() {
			}
        	
        }.runTaskTimerCancelling(0, 2, particles*2);
	}
	
	@Override
	public void onCancel() {
	}
}