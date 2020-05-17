package me.pmilon.RubidiaCore.entities.pathfinders;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import net.minecraft.server.v1_15_R1.*;

public class PathfinderGoalMinionTarget extends PathfinderGoalTarget {

	private final RPlayer k;
    private final EntityCreature m;
    private final double r;
    private EntityLiving g;

    public PathfinderGoalMinionTarget(RPlayer master, EntityCreature entitycreature, double r, boolean flag) {
        this(master, entitycreature, r, flag, false);
    }

    public PathfinderGoalMinionTarget(RPlayer master, EntityCreature entitycreature, double r, boolean flag, boolean flag1) {
        super(entitycreature, flag, flag1);
        this.k = master;
        this.m = entitycreature;
        this.r = r;
        this.a(1);
    }

    public boolean a() {
        if (this.m.getGoalTarget() != null && this.m.getGoalTarget().isAlive()) {
            return false;
        } else {
            final Creature creature = (Creature) this.m.getBukkitEntity();
			LivingEntity target = null;
			double d0 = this.r*this.r;
			for(Entity entity : creature.getNearbyEntities(this.r, this.r, this.r)){
				if(entity instanceof LivingEntity){
					if(entity instanceof Player){
						RPlayer trp = RPlayer.get((Player)entity);
						GMember tmember = GMember.get(trp);
						GMember member = GMember.get(this.k);
						if((!this.k.isInDuel() || !this.k.getDuelOpponent().equals(trp))
								&& (!member.hasGuild() || !tmember.hasGuild() || !member.getGuild().isRaiding() || (!member.getGuild().getCurrentRaid().getDefensive().equals(tmember.getGuild()) && !member.getGuild().getCurrentRaid().getOffensive().equals(tmember.getGuild())))){
							continue;
						}
					}else if(entity instanceof Villager){
						if(PNJManager.isPNJ((Villager)entity)){
							continue;
						}
					}else if(entity.hasMetadata("Minion")){
						continue;
					}
					
					double d1 = entity.getLocation().distanceSquared(creature.getLocation());
					if(d1 < d0){
						d0 = d1;
						target = (LivingEntity) entity;
					}
				}
			}

            if (target == null) {
                return false;
            } else {
                this.g = ((CraftLivingEntity) target).getHandle();
                return true;
            }
        }
    }

    public void c() {
    	this.m.setGoalTarget(this.g, this.m.getGoalTarget() != null && !this.m.getGoalTarget().isAlive() ? TargetReason.TARGET_DIED : TargetReason.CLOSEST_ENTITY, true);
        super.c();
    }
}