package me.pmilon.RubidiaPets.pets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.tags.TagStand;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaMonsters.pathfinders.PathfinderGoalMeleeAttack;
import me.pmilon.RubidiaPets.PetsPlugin;
import me.pmilon.RubidiaPets.utils.Configs;
import me.pmilon.RubidiaPets.utils.LevelUtils;
import me.pmilon.RubidiaPets.utils.Settings;
import net.minecraft.server.v1_13_R2.*;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftCreature;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Pet {

	private String uuid;
	private String name;
	private int level;
	private double exp;
	private double health;
	private int distinctionPoints;
	private int ardor;
	private int patience;
	private int acuity;
	private EntityType type;
	private int age;
	private boolean saddle;
	private List<Pearl> activePearls;
	private boolean active;
	private DyeColor collarColor;
	private Color color;
	private Llama.Color llamaColor;
	private Style style;
	private int domestication;
	private ItemStack armor;
	private Rabbit.Type rabbitType;
	private Ocelot.Type catType;
	private Parrot.Variant parrotType;
	private Profession profession;
	
	private TagStand stand;
	private Creature entity;
	private Player owner;
	private boolean move = true;
	private BukkitTask task;
	private boolean canBeFood = true;
	public Pet(String UUID, String name, int level, double exp, double health, int distinctionPoints, int ardor, int patience, int acuity, EntityType type, int age, boolean saddle, List<Pearl> activePearls, boolean active, DyeColor collarColor, Color color, Style style, int domestication, ItemStack armor, Rabbit.Type rabbitType, Ocelot.Type catType, Parrot.Variant parrotType, Profession profession, Llama.Color llamaColor){
		this.uuid = UUID;
		this.name = name;
		this.level = level;
		this.exp = exp;
		this.health = health;
		this.distinctionPoints = distinctionPoints;
		this.ardor = ardor;
		this.patience = patience;
		this.acuity = acuity;
		this.type = type;
		this.age = age;
		this.saddle = saddle;
		this.activePearls = activePearls;
		this.active = active;
		this.collarColor = collarColor;
		this.color = color;
		this.style = style;
		this.domestication = domestication;
		this.armor = armor;
		this.rabbitType = rabbitType;
		this.catType = catType;
		this.parrotType = parrotType;
		this.profession = profession;
		this.llamaColor = llamaColor;
	}
	
	public static boolean isPet(Entity entity){
		if(entity.hasMetadata("pet")){
			if(!entity.getMetadata("pet").isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public void setUUID(String uUID) {
		uuid = uUID;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Pearl> getActivePearls() {
		return activePearls;
	}

	public void setActivePearls(List<Pearl> activePearls) {
		this.activePearls = activePearls;
	}

	public boolean isSaddle() {
		return saddle;
	}

	public void setSaddle(boolean saddle) {
		this.saddle = saddle;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if(this.getStand() != null)this.getStand().setLayers(new String[]{this.getName(),"§7[" + getLevel() + "] " + getHealthDisplay()}, true);
	}
	
	public Creature getEntity(){
		return this.entity;
	}
	
	public void addPearlsEffects(Player player){
		for(Pearl pearl : this.getActivePearls()){
			if(pearl.isValid()){
				pearl.addEffect(player);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Object getPrivateField(String fieldName, Class fromClass, Object object) {
		try {
			Field field = fromClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public void spawn(Player owner){
		this.owner = owner;
		Creature pet = (Creature) owner.getWorld().spawnEntity(owner.getLocation().add(new Vector(RandomUtils.random.nextDouble(),0,RandomUtils.random.nextDouble()).normalize().multiply(2.5)), this.getType());
		EntityCreature creature = ((CraftCreature)pet).getHandle();
		
		creature.targetSelector = new PathfinderGoalSelector(creature.world.methodProfiler);
		creature.goalSelector = new PathfinderGoalSelector(creature.world.methodProfiler);
		
		creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
		creature.goalSelector.a(2, new PathfinderGoalMeleeAttack(creature, 1.44D, false));
		creature.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(creature, 1.0D));
		creature.goalSelector.a(8, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 8.0F));
		creature.goalSelector.a(8, new PathfinderGoalRandomLookaround(creature));
		
	    this.entity = pet;
	    pet.setMetadata("pet", new FixedMetadataValue(PetsPlugin.instance, this.getUUID()));
	    pet.setRemoveWhenFarAway(false);
	    pet.setCanPickupItems(false);
	    pet.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getMaxHealth());
	    pet.setHealth(this.getHealth());
	    if(pet instanceof Ageable){
	    	Ageable ageable = (Ageable) pet;
	    	ageable.setAge(this.getAge());
	    	ageable.setAgeLock(true);
	    }
	    this.stand = new TagStand(pet, new String[]{this.getName(),"§7[" + getLevel() + "] " + getHealthDisplay()}, false);
	    this.stand.display();
	    if(pet instanceof Tameable){
	    	((Tameable) pet).setTamed(true);
	    	((Tameable) pet).setOwner(owner);
	    }
	    if(pet instanceof Wolf){
	    	((Wolf) pet).setCollarColor(this.getCollarColor());
	    }else if(pet instanceof Sheep){
	    	((Sheep) pet).setColor(this.getCollarColor());
	    }else if(pet instanceof Horse){
	    	((Horse) pet).setColor(this.getColor());
	    	((Horse) pet).setStyle(this.getStyle());
	    	((Horse) pet).setDomestication(this.getDomestication());
    		if(this.isSaddle())((Horse) pet).getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
    		((Horse) pet).getInventory().setArmor(this.getArmor());
	    }else if(pet instanceof Pig){
        	((Pig) pet).setSaddle(this.isSaddle());
	    }else if(pet instanceof Snowman){
        	((Snowman) pet).setDerp(this.isSaddle());
	    }else if(pet instanceof Rabbit){
        	((Rabbit) pet).setRabbitType(this.getRabbitType());
	    }else if(pet instanceof Ocelot){
        	((Ocelot) pet).setCatType(this.getCatType());
	    }else if(pet instanceof Parrot){
        	((Parrot) pet).setVariant(this.getParrotType());
	    } else if (pet instanceof Villager) {
	    	((Villager) pet).setProfession(this.getProfession());
	    } else if (pet instanceof Llama) {
	    	((Llama) pet).setColor(this.getLlamaColor());
	    }
	}
	
	public void despawn(){
		Entity pet = this.getEntity();
		if(pet != null){
			if(pet instanceof Horse){
	        	if(((Horse) pet).getInventory().getSaddle() != null)this.setSaddle(true);
	        	if(((Horse) pet).getInventory().getArmor() != null)this.setArmor((((Horse) pet).getInventory().getArmor()));
	        	this.setColor(((Horse) pet).getColor());
	        	this.setStyle(((Horse) pet).getStyle());
	        	this.setDomestication(((Horse) pet).getDomestication());
	        }else if(pet instanceof Pig){
	        	this.setSaddle(((Pig) pet).hasSaddle());
	        }else if(pet instanceof Wolf){
	        	this.setCollarColor(((Wolf) pet).getCollarColor());
	        }else if(pet instanceof Sheep){
	        	this.setCollarColor(((Sheep) pet).getColor());
	        }else if(pet instanceof Rabbit){
	        	this.setRabbitType(((Rabbit) pet).getRabbitType());
	        }else if(pet instanceof Ocelot){
	        	this.setCatType(((Ocelot) pet).getCatType());
	        }else if(pet instanceof Parrot){
	        	this.setParrotType(((Parrot) pet).getVariant());
	        }else if(pet instanceof Snowman){
	        	this.setSaddle(((Snowman) pet).isDerp());
	        } else if (pet instanceof Villager) {
		    	this.setProfession(((Villager) pet).getProfession());
		    } else if (pet instanceof Llama) {
		    	this.setLlamaColor(((Llama) pet).getColor());
		    }
	        if(pet instanceof Ageable){
	        	this.setAge(((Ageable) pet).getAge());
	        }else this.setAge(0);
	        this.getStand().remove();
	        pet.remove();
	        this.entity = null;
	        this.stand = null;
		}
	}

	public DyeColor getCollarColor() {
		return collarColor;
	}

	public void setCollarColor(DyeColor collarColor) {
		this.collarColor = collarColor;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public int getDomestication() {
		return domestication;
	}

	public void setDomestication(int domestication) {
		this.domestication = domestication;
	}

	public ItemStack getArmor() {
		return armor;
	}

	public void setArmor(ItemStack armor) {
		this.armor = armor;
	}
	
	public void destroy(){
		if(this.getEntity() != null)this.getEntity().remove();
		Pets.pets.remove(this);
		Configs.getPetsConfig().set("pets." + this.getUUID(), null);
	}
	
	public boolean canMove(){
		return move;
	}

	public Rabbit.Type getRabbitType() {
		return rabbitType;
	}

	public void setRabbitType(Rabbit.Type rabbitType) {
		this.rabbitType = rabbitType;
	}

	public Ocelot.Type getCatType() {
		return catType;
	}

	public void setCatType(Ocelot.Type catType) {
		this.catType = catType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	// called 1/sec by RubidiaCore
	public void update(Player owner){
		if(owner != null && this.getEntity() != null){
			if(this.getHealth() <= 1.0){
				Location location = this.getEntity().getLocation();
				location.setPitch(35.0F);
				this.getEntity().teleport(location);
			}
			
			if(this.getEntity().getTarget() == null || this.getEntity().getTarget().isDead() || !this.getEntity().getTarget().isValid()){
				if(this.getEntity().isValid() && !this.getEntity().isDead()){
					Location location = this.getOwner().getLocation().add(new Vector(RandomUtils.random.nextDouble(), 0, RandomUtils.random.nextDouble()).normalize().multiply(RandomUtils.random.nextInt(3)*RandomUtils.random.nextDouble()+1.9));
					if(this.getEntity().getWorld().equals(this.getOwner().getWorld())){
						double distance = this.getEntity().getLocation().distanceSquared(this.getOwner().getLocation());
						if(this.canMove()){
							if(distance > Math.pow(3.9, 2)){
								if(distance < Math.pow(20, 2)){
									double speed = 1.57;
									if (distance > 121) speed = 1.98;
									if (this.getEntity() instanceof Villager) speed *= .45;
									((CraftCreature)this.getEntity()).getHandle().getNavigation().a(location.getX(), location.getY(), location.getZ(), speed);
								}else TeleportHandler.teleport(this.getEntity(), location);
							}
						}else if(distance > Math.pow(50, 2))TeleportHandler.teleport(this.getEntity(), location);
					}else TeleportHandler.teleport(this.getEntity(), location);
				}
				this.getEntity().setTarget(null);
			}
			
			this.addPearlsEffects(owner);
			for(Pearl pearl : new ArrayList<Pearl>(this.getActivePearls())){
				if(System.currentTimeMillis() > pearl.getStartTime()+pearl.getDuration()){
					this.getActivePearls().remove(pearl);
				}
			}
		}
	}

	public double getExp() {
		return exp;
	}

	public void setExp(double exp) {
		this.exp = exp;
		if(this.exp >= LevelUtils.getPLevelTotalExp(this.level)){
			int newLevel = this.level;
			while(this.exp >= LevelUtils.getPLevelTotalExp(newLevel)){
				this.exp -= LevelUtils.getPLevelTotalExp(newLevel);
				newLevel++;
			}
			this.setLevel(newLevel);
		}
	}

	public void addExp(double exp, boolean addLine){
		int level = this.getLevel();
		this.setExp(this.getExp()+exp);
		if(this.getStand() != null && this.getEntity() != null){
			if(this.getLevel() == level){
				if(addLine)this.getStand().setLayers(new String[]{this.getName(), this.getHealthDisplay(), this.getLevelDisplay()}, true);
				else this.getStand().setLayers(new String[]{this.getName(), this.getLevelDisplay()}, true);
			}else{
				this.setDistinctionPoints(this.getDistinctionPoints()+(this.getLevel()-level));
				if(addLine)this.getStand().setLayers(new String[]{this.getName(), this.getHealthDisplay(), "§6>  Niveau supérieur !  <"}, true);
				else this.getStand().setLayers(new String[]{this.getName(), "§6>  Niveau §lsupérieur§6 !  <"}, true);
				LevelUtils.firework(this.getEntity().getLocation());
			}
			if(this.task != null)this.task.cancel();
			this.task = new BukkitTask(PetsPlugin.instance){

				@Override
				public void run() {
					if(getEntity() != null){
						if(getEntity().isValid()){
							updateHealth();
						}
					}
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(60);
		}
	}
	
	public TagStand getStand(){
		return stand;
	}
	
	public String getLevelDisplay(){
		String bar = "§2";
		String[] display = ("[|||||||||||||| " + this.getLevel() + " ||||||||||||||]").split("");
		for(int i = 0;i < display.length;i++){
			if(i == (int) Math.round(display.length*(this.getExp()/LevelUtils.getPLevelTotalExp(this.getLevel()))))bar += "§8";
			bar += display[i];
		}
		return bar;
	}
	public String getHealthDisplay(){
		String bar = "§c";
		String health = "";
		for(int i = 0;i < this.getMaxHealth()/25.0;i++){
			health += "❤";
		}
		String[] display = health.split("");
		for(int i = 0;i < display.length;i++){
			if(i == (int) Math.round(display.length*(this.getEntity().getHealth()/this.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())))bar += "§8";
			bar += display[i];
		}
		return bar;
	}

	public int getArdor() {
		return ardor;
	}

	public void setArdor(int ardor) {
		this.ardor = ardor;
	}

	public int getPatience() {
		return patience;
	}

	public void setPatience(int patience) {
		this.patience = patience;
	}

	public int getAcuity() {
		return acuity;
	}

	public void setAcuity(int acuity) {
		this.acuity = acuity;
	}

	public int getDistinctionPoints() {
		return distinctionPoints;
	}

	public void setDistinctionPoints(int distinctionPoints) {
		this.distinctionPoints = distinctionPoints;
	}

	public double getDamages(){
		return 15+this.getArdor()*Settings.ARDOR_DAMAGES_FACTOR;
	}
	
	public double getMaxHealth(){
		return 100+this.getPatience()*Settings.PATIENCE_HEALTH_FACTOR;
	}
	
	public double getAttackSpeed(){
		return .5+this.getAcuity()*Settings.ACUITY_ATTACKSPEED_FACTOR;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setMove(boolean move){
		this.move = move;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}
	
	public void updateHealth(){
		double health = Math.round(this.getEntity().getHealth() * 10) / 10.;
		this.setHealth(health);
		this.getStand().setLayers(new String[]{getName(), "§7[" + getLevel() + "] " + getHealthDisplay()}, true);
		if(this.getEntity().getHealth() <= 1.0)this.getEntity().setTarget(null);
	}

	public boolean canBeFood() {
		return canBeFood;
	}

	public void setCanBeFood(boolean canBeFood) {
		this.canBeFood = canBeFood;
	}

	public Parrot.Variant getParrotType() {
		return parrotType;
	}

	public void setParrotType(Parrot.Variant parrotType) {
		this.parrotType = parrotType;
	}
	
	public void heal(double healthPoints) {
		double oldHealth = this.getHealth();
		double newHealth = Math.min(this.getMaxHealth(), this.getHealth() + healthPoints);
		this.setHealth(newHealth);
		
		if (this.getEntity() != null) {
			double newEntityHealth = Math.min(this.getHealth(), this.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.0001);
			this.getEntity().setHealth(newEntityHealth);
			if (oldHealth < newHealth) this.getEntity().getWorld().spawnParticle(Particle.HEART, this.getEntity().getLocation(), (int) (newHealth - oldHealth), .4, 1, .4);
			
			final Pet pet = this;
			// synchronous health update
			new BukkitTask(PetsPlugin.instance){
				
				@Override
				public void run() {
					pet.updateHealth();
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(0);
		}
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public Llama.Color getLlamaColor() {
		return llamaColor;
	}

	public void setLlamaColor(Llama.Color llamaColor) {
		this.llamaColor = llamaColor;
	}
}
