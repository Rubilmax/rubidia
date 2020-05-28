package me.pmilon.RubidiaMonsters.regions;

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
import me.pmilon.RubidiaMonsters.entities.SDefaultEntity;
import me.pmilon.RubidiaMonsters.events.MonsterKillEvent;
import me.pmilon.RubidiaMonsters.events.MonsterSpawnEvent;
import me.pmilon.RubidiaMonsters.events.MonsterTameChangeEvent;
import net.minecraft.server.v1_13_R2.EntityInsentient;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Monster {

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
	private LivingEntity entity;
	private Region registeredRegion;
	private double baseXP;
	
	private Player tamer = null;
	public Monster(String UUID, String name, EntityType type, double xpFactor, double healthFactor, double damagesFactor, List<Drop> drops, boolean average, List<AbstractAttack> attacks){
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
	
	public void spawnInRegion(Location location){
		Region region = Regions.get(location);
		if (region != null) {
			boolean enraged = RubidiaMonstersPlugin.random.nextInt(1000) < region.getRageProbability()*1000;
			MonsterSpawnEvent event = new MonsterSpawnEvent(this, region, location, enraged);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				Monster monster = event.getMonster().spawn(event.getLocation(), region.getBaseLevel(event.getLocation()), event.isEnraged());
				monster.setRegisteredRegion(event.getRegion());
				event.getRegion().entities.add(monster);
			}
		}
	}

	public Monster spawn(final Location location, int level, boolean enraged){
		level += (RubidiaMonstersPlugin.random.nextBoolean() ? 1 : -1)*RubidiaMonstersPlugin.random.nextInt(3);
		this.setEnraged(enraged);
		if(level < 1)level = 1;
		location.setYaw(RubidiaMonstersPlugin.random.nextFloat()*180*(RubidiaMonstersPlugin.random.nextBoolean() ? -1 : 1));
		location.setPitch(0);
		final LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location.clone().add(0,-3,0), this.getType());
		SDefaultEntity.setAttackPathfinders(this, (EntityInsentient)((CraftEntity)entity).getHandle());
		entity.setRemoveWhenFarAway(false);
		entity.setNoDamageTicks(50);
		entity.setGravity(false);
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
		
		double maxHealth = 10+level*this.getHealthFactor();
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
		entity.setCustomName((this.getTamer() != null ? "§2" : "§6") + "[N." + level + "] " + (this.isEnraged() ? "§c" : (this.getTamer() != null ? "§a" : "§e")) + this.getName());
		entity.setCustomNameVisible(true);
		Monster monster = this.newInstance(level, entity, null);
		Monsters.entities.put(entity.getUniqueId(), monster);
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

	
	public Monster(Monster monster, int level, LivingEntity entity, Region registeredRegion) {
		this.UUID = monster.UUID;
		this.name = monster.name;
		this.type = monster.type;
		this.drops = monster.drops;
		this.enraged = monster.enraged;
		this.average = monster.average;
		this.level = level;
		this.entity = entity;
		this.registeredRegion = registeredRegion;
		if(this.average){
			this.healthFactor = me.pmilon.RubidiaMonsters.utils.Utils.getHPFactor(level);
			this.xpFactor = me.pmilon.RubidiaMonsters.utils.Utils.getXPFactor(level);
			this.damagesFactor = me.pmilon.RubidiaMonsters.utils.Utils.getDFactor(level);
		}
		
		this.baseXP = Utils.round(this.level*this.xpFactor+RubidiaMonstersPlugin.random.nextInt(40)*.01,2);
	}
	
	public Monster newInstance(int level, LivingEntity entity, Region registeredRegion){
		return new Monster(this, level, entity, registeredRegion);
	}
	
	public void kill(boolean removeEntity){
		if(this.getEntity() != null){
			if(removeEntity) this.getEntity().remove();
			else{
				final Player killer = this.getEntity().getKiller();
				if(killer != null) {
					MonsterKillEvent event = new MonsterKillEvent(this, killer);
					Bukkit.getPluginManager().callEvent(event);
					HashMap<Player, Double> players = new HashMap<Player, Double>();
					players.put(killer, 1.0);
					GMember member = GMember.get(killer);
					if(member.hasGuild()){
						Guild guild = member.getGuild();
						for(Player player : Core.toPlayerList(killer.getNearbyEntities(32, 32, 32))){
							GMember mb = GMember.get(player);
							if(mb.hasGuild()){
								Relation relation = mb.getGuild().getRelationTo(guild);
								if(relation.equals(Relation.MEMBER))players.put(player, .35);
								else if(relation.equals(Relation.ALLY))players.put(player, .15);
							}
						}
					}
					
					final String[] names = new String[players.keySet().size()];
					int i = 0;
					for(Player player : players.keySet()){
						double factor = players.get(player);
						double xp = RPlayer.get(player).addRExp(getXP(player.getLevel())*factor, new RXPSource(RXPSourceType.MONSTER, null, this.getEntity()));
						names[i] = "§8[§7" + player.getName() + " §8|§f +" + Utils.round(xp, 3) + " XP §7(" + Utils.round(factor*100, 0) + "%)§8]";
						i++;
					}

					int lootFactor = 1;
					ItemStack item = killer.getInventory().getItemInMainHand();
					if(item.hasItemMeta()){
						ItemMeta meta = item.getItemMeta();
						if(meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)){
							lootFactor += meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS)*me.pmilon.RubidiaCore.utils.Settings.ENCHANTMENT_LOOT_BONUS_FACTOR;
						}
					}
					
					for(final Drop drop : this.getDrops()){
						if(Math.random() < drop.getProbability()){
							ItemStack dropItem = drop.getItem();
							dropItem.setAmount(dropItem.getAmount()*lootFactor);
							entity.getWorld().dropItemNaturally(entity.getLocation(), dropItem);
						}
					}
					
					new BukkitTask(RubidiaMonstersPlugin.getInstance()) {
						
						@Override
						public void run(){
							Location targetLoc = getEntity().getLocation().clone().add(0, .4, 0);
							
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

	public LivingEntity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
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
				((Creature) this.getEntity()).setTarget(null);
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
	
}
