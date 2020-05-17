package me.pmilon.RubidiaQuests.pnjs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EntityHandler;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.tags.TagStand;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaQuests.pathfinders.PathfinderGoalRandomWalk;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.ui.PNJSettings;
import me.pmilon.RubidiaQuests.utils.Configs;
import net.minecraft.server.v1_15_R1.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class PNJHandler {

	protected final String uuid;
	private UUID entityUUID;
	protected String title;
	protected String titlePrefix;
	protected String name;
	protected String namePrefix;
	protected Villager pnj;
	protected Location loc;
	protected PNJType type;
	private TagStand tag;
	protected int age;
	protected boolean fix;
	
	protected Random random = new Random();
	protected BukkitTask task;
	protected BukkitTask fixTask;
	
	public HashMap<Player, Villager> pnjTemp = new HashMap<Player, Villager>();
	
	public enum PNJType {
		SHOP(Profession.FISHERMAN),
		INHABITANT(Profession.FARMER),
		QUEST(Profession.CARTOGRAPHER),
		PASTOR(Profession.CLERIC),
		SMITH(Profession.WEAPONSMITH),
		BANK(Profession.LIBRARIAN),
		PASSER(Profession.NITWIT),
		HOST(Profession.SHEPHERD);
		
		private final Profession value;
		private PNJType(Profession prof){
			this.value = prof;
		}
		
		public Profession getProfession(){
			return this.value;
		}
	}
	
	public void resetPathfinders(Villager pnj, boolean fake){
		EntityVillager villager = (EntityVillager) ((CraftEntity)pnj).getHandle();
		villager.goalSelector = new PathfinderGoalSelector(villager.getWorld().getMethodProfiler());
		villager.targetSelector = new PathfinderGoalSelector(villager.getWorld().getMethodProfiler());
		villager.goalSelector.a(0, new PathfinderGoalFloat(villager));
		villager.goalSelector.a(1, new PathfinderGoalLookAtTradingPlayer(villager));
		//villager.goalSelector.a(8, new PathfinderGoalPlay(villager, 0.32D));
		villager.goalSelector.a(9, new PathfinderGoalInteract(villager, EntityHuman.class, 4.2F, 1.0F));
		villager.goalSelector.a(10, new PathfinderGoalLookAtPlayer(villager, EntityInsentient.class, 1.0F));
		if(!fake)villager.goalSelector.a(2, new PathfinderGoalRandomWalk(villager, this));
	}
	
	public PNJHandler(String uuid, String title, String titlePrefix, String name, String namePrefix, PNJType type, Location loc, int age, boolean fix){
		this.uuid = uuid;
		this.titlePrefix = titlePrefix;
		this.namePrefix = namePrefix;
		this.title = title;
		this.name = name;
		this.type = type;
		this.loc = loc;
		this.age = age;
		this.fix = fix;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
		this.updateTag();
	}
	
	public void setLocation(Location location){
		TeleportHandler.teleport(this.getEntity(), location);
		this.loc = location;
		getTag().update();
	}
	
	public Location getLocation(){
		return this.loc;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title.toUpperCase();
		this.updateTag();
	}
	
	public PNJType getType(){
		return this.type;
	}
	
	public void setType(PNJType type){
		this.type = type;
	}
	
	public String getUniqueId(){
		return this.uuid;
	}
	
	public int getAge(){
		return this.age;
	}
	
	public boolean isBaby(){
		return this.getAge() < 0;
	}
	
	public void setBaby(boolean baby){
		this.age = baby ? -24000 : 0;
		if(baby)this.getEntity().setBaby();
		else this.getEntity().setAdult();
		this.updateTag();
	}
	
	public void removeTag(){
		this.getTag().remove();
	}
	
	public TagStand getTag() {
		return tag;
	}

	public void requestTag(){
		this.tag = new TagStand(this.getEntity(), new String[]{this.getTitlePrefix() + this.getTitle(), this.getNamePrefix() + this.getName()}, this.isFix());
		this.getTag().display();
	}
	
	public void updateTag(){
		if(this.getTag() != null){
			this.getTag().setLayers(new String[]{this.getTitlePrefix() + this.getTitle(), this.getNamePrefix() + this.getName()}, true);
		}
	}
	
	public Villager getEntity(){
		if(this.pnj == null){
			for(Entity entity : this.loc.getChunk().getEntities()){
				if(entity instanceof Villager){
					if(!entity.isDead()){
						if(entity.getUniqueId().equals(this.getEntityUUID())){
							return (Villager) entity;
						}
					}
				}
			}
		}
		return this.pnj;
	}
	
	public boolean spawn(boolean respawn){
		this.pnj = this.getLocation().getWorld().spawn(this.getLocation(), Villager.class);
		this.resetPathfinders(this.pnj, false);
		this.pnj.setAge(this.getAge());
		this.pnj.setAgeLock(true);
		this.pnj.setCanPickupItems(false);
		this.pnj.setCustomNameVisible(false);
		this.pnj.setFallDistance(-1);
		this.pnj.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(666.0);
		this.pnj.setRemoveWhenFarAway(false);
		this.pnj.setMetadata("PNJ", new FixedMetadataValue(QuestsPlugin.instance, type.toString()));
		this.pnj.setProfession(this.getType().getProfession());
		if(respawn)QuestsPlugin.pnjManager.remove(getEntityUUID().toString());
		this.entityUUID = this.pnj.getUniqueId();
		if(respawn)PNJManager.pnjs.put(this.getEntityUUID().toString(), new PNJSession(this));

		this.requestTag();//after registering uuid
		this.pnj.setMetadata("PNJ", new FixedMetadataValue(QuestsPlugin.instance, type.toString()));
		this.setFix(this.isFix());//after tag creation
		
		this.task = new BukkitTask(Core.instance){
			public void run(){
				if(!isSpawned()) spawn(true);
			}

			@Override
			public void onCancel() {
			}
		}.runTaskTimer(0, 3*20);
		
		this.onSpawn(this.pnj);
		return this.isSpawned();
	}
	
	public void kill(){
		if(this.pnj != null){
			this.pnj.remove();
			this.pnj = null;
		}
	}
	
	public boolean isSpawned(){
		return this.getEntity() != null;
	}
	
	protected abstract void onRightClick(PlayerInteractEntityEvent e, Player p, Villager villager);
	
	protected abstract void onSpawn(Villager villager);
	
	protected void onSetting(PlayerInteractEntityEvent e, Player p){
		Core.uiManager.requestUI(new PNJSettings(p, this));
	}
	
	protected void remove(){
		this.task.cancel();
		if(this.isSpawned()){
			this.getTag().remove();
			this.getEntity().remove();
		}
		this.save();
	}
	
	protected void save(){
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".title", this.getTitle());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".name", this.getName());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".type", this.getType().toString());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".location", this.loc);
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".age", this.getAge());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".fix", this.isFix());
		this.onSave();
	}
	
	protected abstract void onSave();
	
	protected abstract void onDelete();
	
	public void delete(){
		this.remove();
		PNJManager.pnjs.remove(this.getEntityUUID().toString());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId(), null);
		this.onDelete();
	}

	public UUID getEntityUUID() {
		return entityUUID;
	}

	public void setEntityUUID(UUID entityUUID) {
		this.entityUUID = entityUUID;
	}

	public boolean isFix() {
		return fix;
	}

	public void setFix(boolean fix) {
		this.fix = fix;
		if(fix){
			this.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999999, 255, true, false));
			this.fixTask = new BukkitTask(QuestsPlugin.instance) {

				@Override
				public void run() {
					if(getEntity() != null) {
						if(getEntity().getLocation().distanceSquared(getLocation()) > 1) {
							TeleportHandler.teleport(getEntity(), getLocation());
						}
					} else cancel();
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskTimer(0, 20);
		}else{
			if(this.fixTask != null) {
				this.fixTask.cancel();
				this.fixTask = null;
			}
			this.getEntity().removePotionEffect(PotionEffectType.SLOW);
		}
		if(this.getTag().isFix() != this.isFix()) {
			this.getTag().setFix(this.isFix());
		}
	}

	public String getTitlePrefix() {
		return titlePrefix;
	}

	public void setTitlePrefix(String titlePrefix) {
		this.titlePrefix = titlePrefix;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	
	public void createFake(Player player){
		if(this.getEntity() != null)EntityHandler.hideEntity(player, this.getEntity());
		this.getTag().hide(player);
		
		Villager villager = this.getEntity().getWorld().spawn(this.getEntity().getLocation(), Villager.class);
		this.resetPathfinders(villager,true);
		villager.setAge(this.getAge());
		villager.setAgeLock(true);
		villager.setCanPickupItems(false);
		villager.setCustomNameVisible(false);
		villager.setFallDistance(-1);
		villager.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(666.0);
		villager.setRemoveWhenFarAway(false);
		villager.setMetadata("FakeNPCVillager", new FixedMetadataValue(QuestsPlugin.instance, this.getUniqueId()));
		villager.setProfession(this.getType().getProfession());
		TagStand tag = new TagStand(villager, new String[]{this.getTitlePrefix()+this.getTitle(),this.getNamePrefix()+this.getName()}, false);
		tag.display();
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.equals(player)){
				if(villager != null) EntityHandler.hideEntity(p, villager);
				this.getTag().hide(player);
			}
		}
		List<Villager> villagers = PNJManager.pnjTemps.containsKey(player) ? PNJManager.pnjTemps.get(player) : new ArrayList<Villager>();
		villagers.add(villager);
		PNJManager.pnjTemps.put(player, villagers);
	}
	
	public void destroyFake(Player player){
		Villager villager = this.getFakePNJ(player);
		if(villager != null){
			PNJManager.pnjTemps.get(player).remove(villager);
			List<Villager> villagers = PNJManager.pnjTokillTemps.containsKey(player) ? PNJManager.pnjTokillTemps.get(player) : new ArrayList<Villager>();
			villagers.add(villager);
			PNJManager.pnjTokillTemps.put(player, villagers);
		}
	}
	
	public Villager getFakePNJ(Player player){
		if(PNJManager.pnjTemps.containsKey(player)){
			for(Villager villager : PNJManager.pnjTemps.get(player)){
				if(villager.getMetadata("FakeNPCVillager").get(0).asString().equals(this.getUniqueId())){
					return villager;
				}
			}
		}
		return null;
	}

	public void updateColor(){
		if(this.getEntity() != null){
			for(Player player : Core.toPlayerList(this.getEntity().getNearbyEntities(16, 16, 16))){
				RPlayer rp = RPlayer.get(player);
				Quest qst = null;
				Particle particle = null;
				List<Quest> quests = rp.getQuestsOfType(ObjectiveType.TALK, ObjectiveType.GET, ObjectiveType.LEASH);
				if(!quests.isEmpty()){
					for(Quest quest : quests){
						for(Objective objective : quest.getObjectivesByType(ObjectiveType.TALK, ObjectiveType.GET, ObjectiveType.LEASH)){
							if(objective.isAvailable(rp) && !objective.isFilled(rp)){
								if(objective.getTargetUUID().equals(this.getUniqueId())){
									if(objective.getType().equals(ObjectiveType.GET) && !rp.getPlayer().getInventory().containsAtLeast(objective.getItemStack(),1)){
										continue;
									}else if(objective.getType().equals(ObjectiveType.LEASH)){
										int amount = 0;
										for(Entity entity : rp.getPlayer().getNearbyEntities(8, 8, 8)){
											if (entity instanceof LivingEntity) {
												LivingEntity living = (LivingEntity) entity;
												if(living.isLeashed()){
													if(living.getLeashHolder().equals(rp.getPlayer())){
														Monster monster = Monsters.entities.get(living);
														if(monster != null){
															if(monster.getUUID().equals(monster.getUUID()) && monster.getTamer() != null){
																if(monster.getTamer().equals(rp.getPlayer())){
																	amount++;
																}
															}
														}
													}
												}
											}
										}
										if(amount < objective.getAmount())continue;
									}
									qst = quest;
									if(qst != null)break;
								}
							}
						}
						if(qst != null)break;
					}
				}
				if(qst != null){
					particle = Particle.SPELL_INSTANT;
				}else{
					if(this instanceof QuestPNJ){
						QuestPNJ pnjQ = (QuestPNJ)this;
						qst = pnjQ.getActiveQuest(rp);
						if(qst != null){
							if(qst.hasFinished(rp))particle = Particle.DRIP_LAVA;
							else particle = Particle.WATER_WAKE;
						}else{
							qst = pnjQ.getAvailableQuest(rp);
							if(qst != null){
								if(qst.isMain())particle = Particle.CRIT_MAGIC;
								else particle = Particle.VILLAGER_HAPPY;
							}
						}
					}
				}
				if(particle != null){
					player.spawnParticle(particle, this.getEntity().getLocation().add(0,.75,0), 8, .25, .6, .25, 0);
				}
			}
		}
	}
}
