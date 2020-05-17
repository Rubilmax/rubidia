package me.pmilon.RubidiaCore.mage;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.LocationUtils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MageAttack extends BukkitTask {
    public int particles = 30;
    
    private Player player;
    private RPlayer rp;
    private ItemStack item;

    protected boolean critical;
    
    private final Location origin;
    private final Location target;
    
    public MageAttack(Player player, ItemStack item, boolean critical) {
        super(Core.instance);
        this.player = player;
        this.rp = RPlayer.get(player);
        this.item = item;
        this.critical = critical;

		this.origin = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().normalize());
		this.target = this.origin.clone().add(player.getEyeLocation().getDirection().normalize().multiply(9.5));
		this.setDamager(player);
    }
    
    public void setDamager(Player player){
		this.player = player;
        this.rp = RPlayer.get(player);
    }
    
	@Override
	public void run() {
        Vector link = this.target.toVector().subtract(this.origin.toVector());
        double length = link.length();
        Vector step = link.normalize().multiply(length / particles);
        Location location = this.origin.clone();
        for (int i = 0; i < particles; i++) {
            location.add(step);
            if(rp.getNextAttackFactor() > .9999)location.getWorld().spawnParticle(Particle.END_ROD, location, 1, 0, 0, 0, 0);
            location.getWorld().spawnParticle(Particle.SNOWBALL, location, 1, 0, 0, 0, 0);
            if(critical) {
            	location.getWorld().spawnParticle(Particle.CRIT_MAGIC, location, 1, 0, 0, 0, 0);
            }
            
            if(location.getBlock().getType().isSolid()) {
            	break;
            }

            boolean damaged = false;
            for(LivingEntity en : DamageManager.toDamageableEntityList(LocationUtils.getNearbyEntities(location, .8))) {
            	if(en.getLocation().distanceSquared(location) <= 1
            			|| en.getLocation().add(0,1,0).distanceSquared(location) <= 1) {
                	double damages = DamageManager.getDamages(player, en, item, RDamageCause.MAGIC, critical, false);
            		if(DamageManager.damage(en, player, damages, RDamageCause.MAGIC)) {
                		for(Enchantment enchant : item.getEnchantments().keySet()){
                			int level = item.getEnchantmentLevel(enchant);
                			if(enchant.equals(Enchantment.FIRE_ASPECT)){
                				en.setFireTicks(40*level);
                			}else if(enchant.equals(Enchantment.KNOCKBACK)){
                				en.setVelocity(en.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(level*.75));
                			}
                		}
                		damaged = true;
            		}
            	}
            	
                if(damaged)break;
            }
            
            if(damaged)break;
        }
	}

	@Override
	public void onCancel() {
	}
}

