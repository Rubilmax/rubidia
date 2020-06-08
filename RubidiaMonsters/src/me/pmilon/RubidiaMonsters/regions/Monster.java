package me.pmilon.RubidiaMonsters.regions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RXPSource;
import me.pmilon.RubidiaCore.events.RXPSourceType;
import me.pmilon.RubidiaCore.packets.FakeArmorStand;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.attacks.AbstractAttack;
import me.pmilon.RubidiaMonsters.events.MonsterKillEvent;
import me.pmilon.RubidiaMonsters.events.MonsterSpawnEvent;
import me.pmilon.RubidiaMonsters.events.MonsterTameChangeEvent;
import me.pmilon.RubidiaMonsters.pathfinders.PathfinderGoalMeleeAttack;
import me.pmilon.RubidiaMonsters.pathfinders.Targetter;
import me.pmilon.RubidiaMonsters.utils.Settings;
import net.minecraft.server.v1_13_R2.EntityAnimal;
import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_13_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_13_R2.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_13_R2.PathfinderGoalSelector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Monster extends Targetter {

	private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
	
	private String UUID;
	private String name;
	private EntityType type;
	private double xpFactor;
	private double healthFactor;
	private double damagesFactor;
	private List<Drop> drops;
	private boolean average;
	private List<AbstractAttack> attacks;
	
	private int level;
	private boolean enraged;
	private Creature entity;
	private Region registeredRegion;
	private double baseXP;
	
	private Player tamer = null;
	public Monster(String UUID, String name, EntityType type, double xpFactor, double healthFactor, double damagesFactor, List<Drop> drops, boolean average, List<AbstractAttack> attacks){
		super(null);
		this.UUID = UUID;
		this.name = name;
		this.type = type;
		this.xpFactor = xpFactor;
		this.healthFactor = healthFactor;
		this.damagesFactor = damagesFactor;
		this.drops = drops;
		this.average = average;
		this.attacks = attacks;
	}

	public EntityType getType() {
		return type;
	}
	
	public void setType(EntityType type) {
		this.type = type;
	}

	public List<Drop> getDrops() {
		return drops;
	}

	public void setDrops(List<Drop> drops) {
		this.drops = drops;
	}

	public double getXPFactor() {
		return xpFactor;
	}

	public void setXPFactor(double experience) {
		this.xpFactor = experience;
	}

	public double getHealthFactor() {
		return healthFactor;
	}

	public void setHealthFactor(double healthFactor) {
		this.healthFactor = healthFactor;
	}

	public double getDamagesFactor() {
		return damagesFactor;
	}

	public void setDamagesFactor(double damagesFactor) {
		this.damagesFactor = damagesFactor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void spawnInRegion(Region region){
		Location location = region.getRandomSpawnLocation(this);
		
		if(location != null) {
			boolean enraged = Math.random() < region.getRageProbability();
			MonsterSpawnEvent event = new MonsterSpawnEvent(this, region, location, enraged);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				int baseLevel = region.getBaseLevel(event.getLocation());
				int levelRandomShift = (RubidiaMonstersPlugin.random.nextBoolean() ? 1 : -1) * RubidiaMonstersPlugin.random.nextInt(3);
				int level = Math.max(1, baseLevel + levelRandomShift);

				Monster monster = event.getMonster().spawn(event.getLocation(), level, event.isEnraged());
				monster.setRegisteredRegion(event.getRegion());
				event.getRegion().entities.add(monster);
			}
		}
	}

	public Monster spawn(final Location location, int level, boolean enraged) {
		location.setYaw(RubidiaMonstersPlugin.random.nextFloat()*180*(RubidiaMonstersPlugin.random.nextBoolean() ? -1 : 1));
		location.setPitch(0);
		
		final Creature entity = (Creature) location.getWorld().spawnEntity(location.clone().add(0,-3,0), this.getType());
		entity.setRemoveWhenFarAway(false);
		entity.setNoDamageTicks(50);
		entity.setGravity(false);
		entity.setMetadata("monster", new FixedMetadataValue(RubidiaMonstersPlugin.instance, this.getUUID()));
		
		Monster monster = new Monster(this, level, entity);
		monster.setEnraged(enraged);
		Monsters.entities.put(entity.getUniqueId(), monster);
		
		EntityInsentient insentient = (EntityInsentient) ((CraftEntity)entity).getHandle();
		insentient.targetSelector = new PathfinderGoalSelector(insentient.world.methodProfiler);
        
		if(insentient instanceof EntityCreature){
			EntityCreature creature = (EntityCreature) insentient;
			if (insentient instanceof EntityAnimal) {
				creature.goalSelector = new PathfinderGoalSelector(creature.world.methodProfiler);
				
				creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
				creature.goalSelector.a(1, new PathfinderGoalMeleeAttack(monster, creature, 1.36D));
				creature.goalSelector.a(2, new PathfinderGoalMoveTowardsRestriction(creature, 1.0D));
				creature.goalSelector.a(3, new PathfinderGoalRandomStroll(creature, 1.0D));
				creature.goalSelector.a(4, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 8.0F));
				creature.goalSelector.a(5, new PathfinderGoalRandomLookaround(creature));
			}
			
			if(enraged) {
				creature.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<EntityHuman>(creature, EntityHuman.class, false));
			}
		}
		
		final Location blockLocation = location.clone().add(0,-1,0);
		new BukkitTask(RubidiaMonstersPlugin.instance){

			@Override
			public void run() {
				entity.teleport(location.clone().add(0,-entity.getHeight(),0), TeleportCause.PLUGIN);
				final double step = entity.getHeight()/40.0;
				new BukkitTask(RubidiaMonstersPlugin.instance){

					@Override
					public void run() {
						entity.teleport(entity.getLocation().add(0,step,0), TeleportCause.PLUGIN);
						location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 3, .5, .5, .5, 1, blockLocation.getBlock().getBlockData());
					}

					@Override
					public void onCancel() {
						entity.setGravity(true);
						entity.teleport(location);
					}
					
				}.runTaskTimerCancelling(0, 0, 40);
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(0);
		
		if(calendar.get(Calendar.DAY_OF_MONTH) == 31 && calendar.get(Calendar.MONTH) == Calendar.OCTOBER)entity.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN, 1));
		else{
			Weapon temp = Weapons.nearest(level, false, RClass.VAGRANT, "_HELMET", Rarity.COMMON, Rarity.UNCOMMON);
			if(temp != null)entity.getEquipment().setHelmet(temp.getNewItemStack(null));
		}
		if(level > 3){
			if(RubidiaMonstersPlugin.random.nextInt(100) < 15*(level/12.0)){
				Weapon temp1 = Weapons.nearest(level+5, false, RClass.VAGRANT, "_CHESTPLATE", Rarity.COMMON, Rarity.UNCOMMON);
				if(temp1 != null)entity.getEquipment().setChestplate(temp1.getNewItemStack(null));
			}
			if(RubidiaMonstersPlugin.random.nextInt(100) < 17*(level/12.0)){
				Weapon temp1 = Weapons.nearest(level+5, false, RClass.VAGRANT, "_LEGGINGS", Rarity.COMMON, Rarity.UNCOMMON);
				if(temp1 != null)entity.getEquipment().setChestplate(temp1.getNewItemStack(null));
			}
			if(RubidiaMonstersPlugin.random.nextInt(100) < 24*(level/12.0)){
				Weapon temp1 = Weapons.nearest(level+5, false, RClass.VAGRANT, "_BOOTS", Rarity.COMMON, Rarity.UNCOMMON);
				if(temp1 != null)entity.getEquipment().setChestplate(temp1.getNewItemStack(null));
			}
			if(RubidiaMonstersPlugin.random.nextInt(100) < 16*(level/12.0)){
				Weapon temp1 = Weapons.nearest(level+5, false, RClass.VAGRANT, "SHIELD", Rarity.COMMON, Rarity.UNCOMMON);
				if(temp1 != null)entity.getEquipment().setChestplate(temp1.getNewItemStack(null));
			}
		}
		
		Weapon temp2 = Weapons.nearest(level, true, null, this.getType().equals(EntityType.SKELETON) ? "BOW" : RubidiaMonstersPlugin.random.nextBoolean() ? "_SWORD" : "_AXE", Rarity.COMMON, Rarity.UNCOMMON);
		if(temp2 != null)entity.getEquipment().setItemInMainHand(temp2.getNewItemStack(null));
		
		entity.getEquipment().setBootsDropChance(0);
		entity.getEquipment().setLeggingsDropChance(0);
		entity.getEquipment().setChestplateDropChance(0);
		entity.getEquipment().setHelmetDropChance(0);
		entity.getEquipment().setItemInMainHandDropChance(0);
		entity.getEquipment().setItemInOffHandDropChance(0);
		
		double maxHealth = 10 + level * this.getHealthFactor();
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
		entity.setCustomName((this.getTamer() != null ? "§2" : "§6") + "[N." + level + "] " + (enraged ? "§c" : (this.getTamer() != null ? "§a" : "§e")) + this.getName());
		entity.setCustomNameVisible(true);
		
		return monster;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
	//////////////////////////////////
	// INSTANCE METHODS
	//////////////////////////////////

	
	public Monster(Monster monster, int level, Creature entity) {
		super(monster.getTarget());
		this.UUID = monster.UUID;
		this.name = monster.name;
		this.type = monster.type;
		this.drops = monster.drops;
		this.enraged = monster.enraged;
		this.average = monster.average;
		this.level = level;
		this.entity = entity;
		if(this.average){
			this.healthFactor = me.pmilon.RubidiaMonsters.utils.Utils.getHPFactor(level);
			this.xpFactor = me.pmilon.RubidiaMonsters.utils.Utils.getXPFactor(level);
			this.damagesFactor = me.pmilon.RubidiaMonsters.utils.Utils.getDFactor(level);
		}
		
		this.baseXP = Utils.round(this.level * this.xpFactor + RubidiaMonstersPlugin.random.nextInt(40) * .01, 2);
	}
	
	public void kill(boolean removeEntity){
		if(this.getEntity() != null){
			if(removeEntity) this.getEntity().remove();
			else{
				Player killer = this.getEntity().getKiller();
				if(killer != null) {
					HashMap<String, Double> killers = new HashMap<String, Double>();
					killers.put(killer.getName(), getXP(killer.getLevel()) * Settings.KILLER_EXPERIENCE_FACTOR);
					
					GMember member = GMember.get(killer);
					if(member.hasGuild()) {
						Guild guild = member.getGuild();
						for (Player player : Core.toPlayerList(killer.getNearbyEntities(24, 24, 24))) {
							GMember mb = GMember.get(player);
							
							if (mb.hasGuild()) {
								Relation relation = mb.getGuild().getRelationTo(guild);
								if(relation.equals(Relation.MEMBER)) killers.put(player.getName(), getXP(player.getLevel()) * Settings.MEMBER_EXPERIENCE_FACTOR);
								else if(relation.equals(Relation.ALLY)) killers.put(player.getName(), getXP(player.getLevel()) * Settings.ALLY_EXPERIENCE_FACTOR);
							}
						}
					}
					
					MonsterKillEvent event = new MonsterKillEvent(this, killer, killers);
					Bukkit.getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						List<String> names = new ArrayList<String>();
						for(String name : killers.keySet()){
							double xp = killers.get(name);
							
							RPlayer rp = RPlayer.getFromName(name);
							if (rp != null) {
								xp = rp.addRExp(xp, new RXPSource(RXPSourceType.MONSTER, null, this.getEntity()));
							}

							names.add("§8[§7" + name + " §8|§f +" + Utils.round(xp, 3) + " XP§8]");
						}
						
						new BukkitTask(RubidiaMonstersPlugin.getInstance()) {
							
							@Override
							public void run(){
								Location targetLoc = getEntity().getLocation().clone().add(0, .85, 0);
								
								for (String name : names) {
									FakeArmorStand stand = new FakeArmorStand(getEntity().getWorld(), name, true).spawn(targetLoc.add(0, .3, 0));
									
									new BukkitTask(RubidiaMonstersPlugin.getInstance()) {
										
										@Override
										public void run(){
											stand.destroy();
										}

										@Override
										public void onCancel() {
										}
										
									}.runTaskLater(60);
								}
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(20);
					}
				}
			}
			
			java.util.UUID uniqueId = this.getEntity().getUniqueId();
			if(Monsters.entities.containsKey(uniqueId)) Monsters.entities.remove(uniqueId);
			if(this.getRegisteredRegion() != null) this.getRegisteredRegion().entities.remove(this);
		}
	}
	
	public double getXP(int level){
		double diff = Math.abs(this.getLevel()-level);
		if(diff > 6){
			return Utils.round(this.getBaseXP()*(7.0/diff), 2);
		}
		return this.getBaseXP();
	}

	public Creature getEntity() {
		return entity;
	}

	public void setEntity(Creature entity) {
		this.entity = entity;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Region getRegisteredRegion() {
		return registeredRegion;
	}

	public void setRegisteredRegion(Region registeredRegion) {
		this.registeredRegion = registeredRegion;
	}

	public Player getTamer() {
		return tamer;
	}

	public void setTamer(Player tamer) {
		MonsterTameChangeEvent event = new MonsterTameChangeEvent(this, tamer);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			this.tamer = tamer;
			if(this.getEntity() != null){
				this.getEntity().setCustomName((this.getTamer() != null ? "§2" : "§6") + "[N." + level + "] " + (this.isEnraged() ? "§c" : (this.getTamer() != null ? "§a" : "§e")) + this.getName());
				this.setTarget(null);
				
				new BukkitTask(RubidiaMonstersPlugin.getInstance()){

					@Override
					public void run() {
						getEntity().setLeashHolder(getTamer());
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(0);
			}
		}
	}
	
	public boolean isEnraged() {
		return enraged;
	}

	public void setEnraged(boolean enraged) {
		this.enraged = enraged;
	}

	public double getDamages(){
		return this.getLevel()*this.getDamagesFactor();
	}

	public double getBaseXP() {
		return baseXP;
	}

	public void setBaseXP(double baseXP) {
		this.baseXP = baseXP;
	}

	public boolean isAverage() {
		return average;
	}

	public void setAverage(boolean average) {
		this.average = average;
	}

	public List<AbstractAttack> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<AbstractAttack> attacks) {
		this.attacks = attacks;
	}
	
	@Override
	public void setTarget(LivingEntity target) {
		this.target = target;
		if (this.getEntity() != null) {
			this.getEntity().setTarget(target);
		}
	}
	
}
