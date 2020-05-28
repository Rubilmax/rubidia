package me.pmilon.RubidiaCore.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.mage.FakeLightning;
import me.pmilon.RubidiaCore.mage.MageMeteor;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerWorldBorder;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.LocationUtils;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.VectorUtils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public enum RAbility {
	

	PALADIN_1(new Ability("Étourdissement", Arrays.asList("La paladin frappe si fort", "qu'il étourdit souvent sa cible"),
			RClass.PALADIN, 1, true, "", "Probabilité", "%", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			if(Math.random() < this.getDamages(rp)*.01) {
				rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.BLOCK_ANVIL_HIT, 1, .5F);
				RAbility.stun(target, 30);
			}
		}
		
	}),
	PALADIN_2(new Ability("Peau de fer", Arrays.asList("Le paladin possède une défense", "naturellement surdéveloppée"),//handled in RPlayer
			RClass.PALADIN, 2, true, "", "Défense supplémentaire", "%", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
		
	}),
	PALADIN_3(new Ability("Bête de foire", Arrays.asList("Le paladin possède une santé", "naturellement surdéveloppée"),//handled in RPlayer
			RClass.PALADIN, 3, true, "", "PV supplémentaires", " PV", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	PALADIN_4(new Ability("Poids lourd", Arrays.asList("La corpulence du paladin l'oblige", "à réduire sa vitesse d'attaque"),//handled in RPlayer
			RClass.PALADIN, 4, true, "", "Vitesse d'atq", "%", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	PALADIN_5(new Ability("Rage", Arrays.asList("Le paladin entre en frénésie,", "augmentant sa force et sa vitesse", "mais également les dégâts qu'il subit"),//handled in DamageManager
			RClass.PALADIN, 5, false, "DDD,!SN", "Dégâts subis", "%", 50, false) {
		
		List<PotionEffect> effects = Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1, true, false),
				new PotionEffect(PotionEffectType.SPEED, 100, 1, true, false),
				new PotionEffect(PotionEffectType.ABSORPTION, 100, 1, true, false),
				new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0, true, false));
		
		@Override
		public void run(final RPlayer rp, Event event) {
			Player player = rp.getPlayer();
			WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
			packet.setAction(com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS);
			packet.setCenterX(player.getLocation().getX());
			packet.setCenterZ(player.getLocation().getZ());
			packet.setRadius(0);
			packet.setWarningDistance(Integer.MAX_VALUE);
			packet.sendPacket(player);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
			
			for(PotionEffect effect : effects){
				player.addPotionEffect(effect);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
			Player player = rp.getPlayer();
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
			for(PotionEffect effect : effects){
				player.addPotionEffect(effect);
			}
			player.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, player.getEyeLocation(), 5, .25, .25, .25, 0);
		}

		@Override
		public void onCancel(RPlayer rp) {
			Player player = rp.getPlayer();
			WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
			packet.setAction(com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS);
			if(player.getWorld().getWorldBorder() != null) {
				packet.setCenterX(player.getWorld().getWorldBorder().getCenter().getX());
				packet.setCenterZ(player.getWorld().getWorldBorder().getCenter().getZ());
				packet.setRadius(player.getWorld().getWorldBorder().getSize());
				packet.setWarningDistance(player.getWorld().getWorldBorder().getWarningDistance());
			} else {
				packet.setCenterX(player.getLocation().getX());
				packet.setCenterZ(player.getLocation().getZ());
				packet.setRadius(Integer.MAX_VALUE);
				packet.setWarningDistance(0);
			}
			packet.sendPacket(player);
			for(PotionEffect effect : player.getActivePotionEffects()){
				player.removePotionEffect(effect.getType());
			}
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	PALADIN_6(new Ability("Charge", Arrays.asList("Le paladin fonce droit devant lui", "jusqu'à ce qu'il rencontre", "un obstacle, qu'il explosera"),
			RClass.PALADIN, 6, false, "DGG,SP", "", "", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			new BukkitTask(Core.instance){
				int step = 0;
				@Override
				public void run() {
					if(player.isDead()){
						this.cancel();
					}else{
						if(step < 40){
							player.setSprinting(false);
							Vector v = player.getEyeLocation().getDirection();
							player.setVelocity(v.multiply(.35).setY(player.getVelocity().getY()));
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, .28F, .25F);
							player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 25, .25, .25, .25, 0);
							List<LivingEntity> near = DamageManager.toDamageableEntityList(player.getNearbyEntities(.25, 1, .25));
							if(!near.isEmpty()){
								player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 8, 2, 1, 2);
								player.getWorld().playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
								getInstance().damage(rp, player.getNearbyEntities(1.8, 1.8, 1.8));
								this.cancel();
							}
						}else{
							this.cancel();
						}
					}
					step++;
				}
				
				@Override
				public void onCancel() {
					rp.setActiveAbility(RAbility.PALADIN_6, false);
				}
				
			}.runTaskTimer(0, 1);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			Vector vdir = target.getLocation().toVector().subtract(player.getLocation().toVector());
			if(vdir.length() > 0)vdir.multiply(.4/vdir.length());
			vdir.setY(.75);
			target.setVelocity(vdir);
		}
		
	}),
	PALADIN_7(new Ability("Coup fatal", Arrays.asList("Le paladin saute en hauteur pour", "frapper le sol de toute sa force"),
			RClass.PALADIN, 7, false, "DGD,SN", "", "", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.setVelocity(new Vector(0,1.5,0));
			player.getWorld().playEffect(player.getLocation(), Effect.ENDERDRAGON_GROWL, 1);

			new BukkitTask(Core.instance){
				@Override
				public void run() {
					if(player.getVelocity().getY() < 0) {
						this.cancel();
					}
				}
				
				@Override
				public void onCancel() {
					player.setVelocity(new Vector(0,-2.5,0));
					new BukkitTask(Core.instance){
						@Override
						public void run() {
							if(player.isDead()) {
								this.cancel();
							} else if(player.isOnGround()) {
								player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 8, 2, 1, 2);
								player.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 3, 0, .6, 0);
								player.getWorld().playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
								getInstance().damage(rp, player.getNearbyEntities(1.8, 1.8, 1.8));
								this.cancel();
							}
						}
						
						@Override
						public void onCancel() {
							rp.setActiveAbility(RAbility.PALADIN_7, false);
						}
						
					}.runTaskTimer(0, 1);
				}
				
			}.runTaskTimer(0, 1);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			Vector vdir = target.getLocation().toVector().subtract(player.getLocation().toVector());
			if(vdir.length() > 0)vdir.multiply(.25/vdir.length());
			vdir.setY(1);
			target.setVelocity(vdir);
		}
		
	}),
	PALADIN_8(new Ability("Uppercut", Arrays.asList("Le paladin maîtrise les arts martiaux", "depuis son plus jeune âge"),
			RClass.PALADIN, 8, false, "GGG,SN", "", "", 0, true) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			this.takeVigor(rp);
			Player player = rp.getPlayer();
			player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, target.getLocation(), 4, .5, 1, .5);
			player.getWorld().spawnParticle(Particle.LAVA, target.getLocation(), 2, 0, .6, 0);
			player.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
			Vector vdir = player.getEyeLocation().getDirection().normalize().multiply(.3);
			vdir.setY(1.5);
			target.setVelocity(vdir);
		}
		
	}),
	PALADIN_9(new Ability("Expulsion", Arrays.asList("Le paladin peut envoyer", "son ennemi jusqu'au paradis"),
			RClass.PALADIN, 9, false, "GGD", "", "", 0, true) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			this.takeVigor(rp);
			Player player = rp.getPlayer();
			player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, target.getLocation(), 4, .5, 1, .5);
			player.getWorld().spawnParticle(Particle.LAVA, target.getLocation(), 2, 0, .6, 0);
			player.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
			Vector vdir = player.getEyeLocation().getDirection().normalize().multiply(.9);
			vdir.setY(1.05);
			target.setVelocity(vdir);
		}
		
	}),
	PALADIN_10(new Ability("Danse de lames", Arrays.asList("Le paladin entraîne ses haches", "dans une virevolte effrénée,", "causant de lourds dégâts","aux ennemis alentours"),
			RClass.PALADIN, 10, false, "DDD,SN", "", "", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final int yawOffset = 23;
			final List<LivingEntity> hurt = new ArrayList<LivingEntity>();
			new BukkitTask(Core.instance){
				int step = 0;
				
				@Override
				public void run() {
					List<LivingEntity> around = DamageManager.toDamageableEntityList(player.getNearbyEntities(2.1, 2.1, 2.1));
					around.removeAll(hurt);
					getInstance().damage(rp, around);
					hurt.addAll(around);
					if(step < Math.floor(360/yawOffset)+1){
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, .5F, 1);
						player.getWorld().spawnParticle(Particle.LAVA, player.getLocation().subtract(0,.05,0), 4, .25, .2, .2);
						Location location = player.getLocation();
						location.setYaw(player.getEyeLocation().getYaw()+yawOffset);
						player.teleport(location);
					}else this.cancel();
					step++;
				}

				@Override
				public void onCancel() {
					rp.setActiveAbility(RAbility.PALADIN_10, false);
				}
				
			}.runTaskTimer(0, 0);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			Vector vdir = target.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(.7);
			vdir.setY(.75);
			target.setVelocity(vdir);
		}
				
	}),
	PALADIN_11(new Ability("Métafusion", Arrays.asList("Le paladin concentre sa force durant", "2 secondes, après lesquelles", "il déchaîne une onde de choc"),
			RClass.PALADIN, 11, false, "DGG,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 255, true, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 3, true, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 4, true, false));
			player.setSneaking(true);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2, 1);
			new BukkitTask(Core.instance){

				int scream = 0;
				@Override
				public void run(){
					scream += 1;
					if(scream >= 5){
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2, 1);
						scream = 0;
					}

					List<Entity> near = player.getNearbyEntities(5, 5, 5);
					for(Entity e : near){
						if(e instanceof LivingEntity){
							Vector v = new Vector(player.getLocation().getX()-e.getLocation().getX(), player.getLocation().getY()-e.getLocation().getY(), player.getLocation().getZ()-e.getLocation().getZ());
							e.setVelocity(v.multiply(0.05));
							((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, true, false));
						}
					}
				}

				@Override
				public void onCancel() {
					player.setSneaking(false);
					player.getWorld().createExplosion(player.getLocation(), 0);
					getInstance().damage(rp, player.getNearbyEntities(3, 3, 3));
					player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 3, 1, 1, 1);
					rp.setActiveAbility(RAbility.PALADIN_11, false);
				}
				
			}.runTaskTimerCancelling(1, 0, 60);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			Vector vdir = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5);
			vdir.setY(.75);
			target.setVelocity(vdir);
		}
				
	}),
	PALADIN_12(new Ability("Frappe du golem", Arrays.asList("Le paladin frappe le sol", "en libérant une onde de choc"),
			RClass.PALADIN, 12, false, "GDD,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.setVelocity(new Vector(0,.6,0));
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					player.setVelocity(new Vector(0,-1,0));
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(5);

			new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(player.isDead()){
						this.cancel();
					}else if(player.isOnGround()){
						final Location center = LocationUtils.getCenter(player.getLocation().subtract(0,1,0));
						center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1, .1F);
						player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 40, .1, .1, .1);
						final List<LivingEntity> hurt = new ArrayList<LivingEntity>();

						
						for(int i = 0;i < 7;i++){
							final int length = i;
							final int degreesPerStep = 15;
							new BukkitTask(Core.instance){

								@Override
								public void run() {
									Vector vector = new Vector(length,0,0);
									
									for(int i = 0;i < 360 / degreesPerStep;i++){
										VectorUtils.rotateAroundAxisY(vector, degreesPerStep);
										Location location = LocationUtils.getSafeLocation(center.clone().add(vector)).subtract(0,1,0);
										player.getWorld().spawnParticle(Particle.BLOCK_CRACK, location.clone().add(0,.5,0), 1, .5, .5, .5, location.getBlock().getBlockData());
										
										final BlockData data = location.getBlock().getBlockData();
										final Location blockLocation = location.getBlock().getLocation();
										FallingBlock block = player.getWorld().spawnFallingBlock(location, data);
										block.setVelocity(new Vector(0, .2, 0));
										block.setDropItem(false);
										new BukkitTask(Core.instance) {

											@Override
											public void run() {
												player.sendBlockChange(blockLocation, data);
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskLater(5);
										
										List<LivingEntity> near = DamageManager.toDamageableEntityList(player.getNearbyEntities(length, length, length));
										near.removeAll(hurt);
										getInstance().damage(rp, near);
										hurt.addAll(near);
									}
								}

								@Override
								public void onCancel() {
								}
								
							}.runTaskLater(2*i);
						}
						this.cancel();
					}
				}

				@Override
				public void onCancel() {
					rp.setActiveAbility(RAbility.PALADIN_12, false);
				}
				
			}.runTaskTimer(5,0);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			Vector vdir = target.getLocation().toVector().subtract(player.getLocation().toVector());
			if(vdir.length() > 0)vdir.multiply(.88/vdir.length());
			vdir.setY(1.1);
			target.setVelocity(vdir);
		}
				
	}),
	
	RANGER_1(new Ability("Aveuglement", Arrays.asList("La ranger est si habile", "qu'il aveugle souvent sa cible"),
			RClass.RANGER, 1, true, "", "Probabilité", "%", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			if(RandomUtils.random.nextDouble() < this.getDamages(rp)*.01) {
				rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				RAbility.stun(target, 30);
			}
		}
		
	}),
	RANGER_2(new Ability("Tir double", Arrays.asList("La ranger tire parfois", "par erreur trois flèches"),
			RClass.RANGER, 2, true, "", "Probabilité", "%", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
			if(event instanceof EntityShootBowEvent) {
				Player player = rp.getPlayer();
				EntityShootBowEvent event2 = (EntityShootBowEvent) event;
				if(RandomUtils.random.nextDouble() < this.getDamages(rp)*.01) {
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					Vector vector = player.getEyeLocation().getDirection().normalize().multiply(2.8*event2.getForce());
					double factor = rp.getNextAttackFactor();
					Arrow arrow1 = rp.getPlayer().launchProjectile(Arrow.class, VectorUtils.rotateAroundAxisY(vector.clone(), Math.PI/36));
					rp.setNextAttackFactor(factor);
					Arrow arrow2 = rp.getPlayer().launchProjectile(Arrow.class, VectorUtils.rotateAroundAxisY(vector.clone(), -Math.PI/36));
					rp.setNextAttackFactor(factor);
					arrow1.setCritical(!rp.getPlayer().isOnGround());
					arrow2.setCritical(!rp.getPlayer().isOnGround());
					arrow1.setBounce(false);
					arrow2.setBounce(false);
					arrow1.setMetadata("autoremove", new FixedMetadataValue(Core.instance, true));
					arrow2.setMetadata("autoremove", new FixedMetadataValue(Core.instance, true));
				}
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
		
	}),
	RANGER_3(new Ability("Récupération", Arrays.asList("L'essence du ranger est sa capacité", "à se régénérer à une vitesse inégalable"),
			RClass.RANGER, 3, true, "", "Régénérescence supplémentaire", "%", 0, false) {

		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
		
	}),
	RANGER_4(new Ability("Flèches empoisonnées", Arrays.asList("Les flèches du ranger sont toutes", "trempées dans une solution d'arsenic", "pour qu'elles empoisonnent leurs cibles"),
			RClass.RANGER, 4, true, "", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(final RPlayer rp, final LivingEntity target) {
			final Player player = rp.getPlayer();

			if(target.hasMetadata("RANGER_4")) {
				List<MetadataValue> metadata = player.getMetadata("RANGER_4");
				if(metadata.size() > 0) {
					BukkitTask task = BukkitTask.tasks.get(metadata.get(0).asInt());
					if(task != null) {
						task.cancel();
					}
				}
			}
			
			target.setMetadata("RANGER_4", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

				public void run() {
					if(!target.isDead() && target.isValid()){
						DamageManager.damage(target, rp.getPlayer(), getInstance().getDamages(rp), RDamageCause.ABILITY);
						target.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().add(0, target.getHeight()/2, 0), 10, .2, .5, .2, new Particle.DustOptions(Color.fromRGB(0, 255, 0), 1));
						target.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().add(0, target.getHeight()/2, 0), 10, .2, .5, .2, new Particle.DustOptions(Color.fromRGB(0, 212, 0), 1));
						target.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().add(0, target.getHeight()/2, 0), 10, .2, .5, .2, new Particle.DustOptions(Color.fromRGB(24, 223, 0), 1));
						target.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().add(0, target.getHeight()/2, 0), 10, .2, .5, .2, new Particle.DustOptions(Color.fromRGB(10, 241, 0), 1));
						target.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().add(0, target.getHeight()/2, 0), 10, .2, .5, .2, new Particle.DustOptions(Color.fromRGB(0, 200, 0), 1));
					} else this.cancel();
				}

				@Override
				public void onCancel() {
					target.removeMetadata("RANGER_4", Core.instance);
				}
				
			}.runTaskTimerCancelling(0, 20, 100).getTaskId()));
		}
				
	}),
	RANGER_5(new Ability("Salve", Arrays.asList("Le ranger lance un grand nombre", "de flèches en un cours laps de temps"),
			RClass.RANGER, 5, false, "GDG,SN", "Flèches", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final double damages = getInstance().getDamages(rp);
			new BukkitTask(Core.instance){
				int step = 0;

				@Override
				public void run() {
					if(player.isDead()){
						this.cancel();
					}else{
						if(step < damages){
							final Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().multiply(2));
							arrow.setCritical(!rp.getPlayer().isOnGround());
							arrow.setBounce(false);
							arrow.setMetadata("autoremove", new FixedMetadataValue(Core.instance, true));
							player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 5, .01, .01, .01);
							player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
							player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(-.05));
							rp.setNextAttackFactor(1.0);
						}else this.cancel();
					}
					step++;
				}

				@Override
				public void onCancel() {
					rp.setActiveAbility(RAbility.RANGER_5, false);
				}
				
			}.runTaskTimer(0, 5);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	RANGER_6(new Ability("Flèche explosive", Arrays.asList("Le ranger prépare une flèche qui", "explosera au premier contact"),
			RClass.RANGER, 6, false, "DGD,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			if(event instanceof EntityShootBowEvent) {
				EntityShootBowEvent event2 = (EntityShootBowEvent)event;
				event2.setCancelled(true);
				final Player player = rp.getPlayer();
				final Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().normalize().multiply(3*event2.getForce()));
				arrow.setCritical(!rp.getPlayer().isOnGround());
				arrow.setBounce(false);
				player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
				arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
				arrow.setMetadata("RANGER_6", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

					@Override
					public void run() {
						if(arrow.isOnGround()){
							this.cancel();
						}
						arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 1, 0, 0, 0, 0);
					}

					@Override
					public void onCancel() {
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .1F);
								arrow.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, arrow.getLocation(), 1, 0, 0, 0);
								getInstance().damage(rp, arrow.getNearbyEntities(3.4, 3.4, 3.4));
								arrow.remove();
								arrow.removeMetadata("RANGER_6", Core.instance);
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(14);
					}
					
				}.runTaskTimer(0, 1).getTaskId()));
				rp.setActiveAbility(RAbility.RANGER_6, false);
			} else {
				this.takeVigor(rp);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	RANGER_7(new Ability("Flèche grappin", Arrays.asList("Le ranger prépare une flèche qui", "l'entraînera dans sa course"),
			RClass.RANGER, 7, false, "DDD,SN", "Tension", "%", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			if(event instanceof EntityShootBowEvent) {
				EntityShootBowEvent event2 = (EntityShootBowEvent)event;
				event2.setCancelled(true);
				final Player player = rp.getPlayer();
				final float force = 3*event2.getForce();
				final Vector vector = player.getEyeLocation().getDirection().normalize();
				final Arrow arrow = player.launchProjectile(Arrow.class, vector.clone().multiply(force));
				arrow.setCritical(!rp.getPlayer().isOnGround());
				arrow.setBounce(false);
				player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
				arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 1, 1);
				arrow.setMetadata("RANGER_7", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

					@Override
					public void run() {
						if(arrow.isOnGround()){
							this.cancel();
						}
						arrow.getWorld().spawnParticle(Particle.END_ROD, arrow.getLocation(), 1, 0, 0, 0, 0);
					}

					@Override
					public void onCancel() {
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_LEASH_KNOT_BREAK, 2, 1);
								Vector link = arrow.getLocation().toVector().subtract(player.getLocation().toVector());
								player.setVelocity(vector.clone().multiply(link.length()*.165*getInstance().getDamages(rp)*.01));
								arrow.remove();
								arrow.removeMetadata("RANGER_7", Core.instance);
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(14);
					}
					
				}.runTaskTimer(0, 1).getTaskId()));
				rp.setActiveAbility(RAbility.RANGER_7, false);
			} else {
				this.takeVigor(rp);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	RANGER_8(new Ability("Flèche de foudre", Arrays.asList("Le ranger prépare une flèche qui fera", "s'abattre la foudre au premier contact"),
			RClass.RANGER, 8, false, "DDG,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			if(event instanceof EntityShootBowEvent) {
				EntityShootBowEvent event2 = (EntityShootBowEvent)event;
				event2.setCancelled(true);
				final Player player = rp.getPlayer();
				final Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().normalize().multiply(3*event2.getForce()));
				arrow.setCritical(!rp.getPlayer().isOnGround());
				arrow.setBounce(false);
				player.getWorld().playEffect(player.getLocation(), Effect.GHAST_SHRIEK, 1);
				arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
				arrow.setMetadata("RANGER_8", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

					@Override
					public void run() {
						if(arrow.isOnGround()){
							this.cancel();
						}
						arrow.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, arrow.getLocation(), 1, 0, 0, 0, 0);
					}

					@Override
					public void onCancel() {
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								FakeLightning lightning = new FakeLightning(false);
								lightning.strike(arrow.getLocation());
								getInstance().damage(rp, arrow.getNearbyEntities(3.4, 3.4, 3.4));
								arrow.remove();
								arrow.removeMetadata("RANGER_8", Core.instance);
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(14);
					}
					
				}.runTaskTimer(0, 1).getTaskId()));
				rp.setActiveAbility(RAbility.RANGER_8, false);
			} else {
				this.takeVigor(rp);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			FakeLightning lightning = new FakeLightning(false);
			lightning.strike(target.getLocation());
			target.setFireTicks(90);
		}
				
	}),
	RANGER_9(new Ability("Cercle de flèches", Arrays.asList("Le ranger décoche trois paquets de flèches", "pour toucher tous les ennemis dans ses alentours"),
			RClass.RANGER, 9, false, "GDD,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			new BukkitTask(Core.instance){
				
				@Override
				public void run() {
					player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
					Vector vector = player.getEyeLocation().getDirection().setY(0).normalize();
					int arrows = 15;
					final Arrow arrow = player.launchProjectile(Arrow.class, vector);
					arrow.setCritical(!rp.getPlayer().isOnGround());
					arrow.setBounce(false);
					arrow.setKnockbackStrength(0);
					arrow.setMetadata("RANGER_9", new FixedMetadataValue(Core.instance, true));
					arrow.setMetadata("autoremove", new FixedMetadataValue(Core.instance, true));
					for(int i = 0;i < arrows;i++) {
						VectorUtils.rotateAroundAxisY(vector, 2*Math.PI/arrows);
						final Arrow arrow2 = player.launchProjectile(Arrow.class, vector);
						arrow2.setCritical(!rp.getPlayer().isOnGround());
						arrow2.setBounce(false);
						arrow2.setKnockbackStrength(0);
						arrow2.setMetadata("RANGER_9", new FixedMetadataValue(Core.instance, true));
						arrow2.setMetadata("autoremove", new FixedMetadataValue(Core.instance, true));
					}
				}

				@Override
				public void onCancel() {
					rp.setActiveAbility(RAbility.RANGER_9, false);
				}
				
			}.runTaskTimerCancelling(0, 7, 21);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
		
	}),
	RANGER_10(new Ability("Flèche glaçante", Arrays.asList("Le ranger prépare une flèche qui", "glacera immédiatement la cible"),
			RClass.RANGER, 10, false, "DGD,!SN", "Durée de l'effet", "sec", 0, false) {
		int duration = 0;
		
		@Override
		public void run(final RPlayer rp, Event event) {
			if(event instanceof EntityShootBowEvent) {
				duration = (int) Math.round(this.getDamages(rp)*20);
				EntityShootBowEvent event2 = (EntityShootBowEvent)event;
				event2.setCancelled(true);
				final Player player = rp.getPlayer();
				final Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().normalize().multiply(3*event2.getForce()));
				arrow.setCritical(!rp.getPlayer().isOnGround());
				arrow.setBounce(false);
				arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_GLASS_HIT, 1, 1);
				arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
				arrow.setMetadata("RANGER_10", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

					@Override
					public void run() {
						if(arrow.isOnGround()){
							this.cancel();
						}
						arrow.getWorld().spawnParticle(Particle.DRIP_WATER, arrow.getLocation(), 1, 0, 0, 0, 0);
					}

					@Override
					public void onCancel() {
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								Location center = LocationUtils.getCenter(arrow.getLocation());
								List<Block> blocks = new ArrayList<Block>();
								for(int i = 0;i < 5;i++){
									final int length = i;
									Vector vector = new Vector(length,0,0);
									
									for(int j = 0;j < 360;j++){
										VectorUtils.rotateAroundAxisY(vector, 1);
										final Location location = LocationUtils.getSafeLocation(center.clone().add(vector));
										if(!blocks.contains(location.getBlock())) {
											final Location blockLocation = location.clone();
											Snow snow = (Snow) Bukkit.getServer().createBlockData(Material.SNOW);
											snow.setLayers(RandomUtils.random.nextInt(3)+snow.getMinimumLayers());
											for(final Player pp : Core.toPlayerList(LocationUtils.getNearbyEntities(location, 64))) {
												pp.sendBlockChange(location, snow);
												new BukkitTask(Core.instance) {

													@Override
													public void run() {
														pp.sendBlockChange(blockLocation, blockLocation.getBlock().getBlockData());
													}

													@Override
													public void onCancel() {
													}
													
												}.runTaskLater(duration);
												blocks.add(location.getBlock());
												BlockData data = location.subtract(0,1,0).getBlock().getBlockData();
												if(data instanceof Snowable) {
													Snowable snowable = (Snowable) data;
													snowable.setSnowy(true);
													pp.sendBlockChange(location, data);
													new BukkitTask(Core.instance) {

														@Override
														public void run() {
															pp.sendBlockChange(location, location.getBlock().getBlockData());
														}

														@Override
														public void onCancel() {
														}
														
													}.runTaskLater(duration);
												}
											}
										}
									}
								}
								arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 2);
								arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_GLASS_BREAK, 2, 1);

								for(LivingEntity target : DamageManager.toDamageableEntityList(arrow.getNearbyEntities(3.4, 3.4, 3.4))) {
									if(!target.equals(rp.getPlayer())) {
										if(DamageManager.canDamage(target, rp.getPlayer(), RDamageCause.ABILITY)) {
											getInstance().animate(rp, target);
										}
									}
								}
								arrow.remove();
								arrow.removeMetadata("RANGER_10", Core.instance);
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(14);
					}
					
				}.runTaskTimer(0, 1).getTaskId()));
				rp.setActiveAbility(RAbility.RANGER_10, false);
			} else {
				this.takeVigor(rp);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, final LivingEntity target) {
			final AttributeModifier modifier = new AttributeModifier("RubidiaTrap", -10000.0, Operation.ADD_NUMBER);
			if(target instanceof Player){
				((Player) target).setWalkSpeed(0);
			}else{
				target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(modifier);
			}
			target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 444, true, false));
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					Block block = target.getLocation().subtract(0,1,0).getBlock();
					target.getWorld().spawnParticle(Particle.DRIP_WATER, target.getLocation(), 4, .3, .1, .3, 0);
					target.getWorld().spawnParticle(Particle.SPELL_INSTANT, target.getLocation(), 1, .1, .1, .1);
					target.getWorld().spawnParticle(Particle.BLOCK_CRACK, target.getLocation(), 4, .3, .1, .3, block.getBlockData());
				}

				@Override
				public void onCancel() {
					if(target instanceof Player){
						((Player) target).setWalkSpeed(Settings.DEFAULT_WALK_SPEED);
					}else{
						target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
					}
					target.removePotionEffect(PotionEffectType.JUMP);
				}
				
			}.runTaskTimerCancelling(0, 1, duration);
		}
				
	}),
	RANGER_11(new Ability("Tir de précision", Arrays.asList("Le ranger augmente sa précision", "pendant 3 secondes, après lesquelles", "il décoche une flèche incroyablement puissante"),
			RClass.RANGER, 11, false, "DGG,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3, true, false));
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					Vector target = player.getEyeLocation().getDirection();
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1, 1);
					Arrow shot = player.launchProjectile(Arrow.class, target.multiply(25));
					shot.setBounce(false);
					shot.setCritical(true);
					shot.setTicksLived(12000);
					shot.setKnockbackStrength(5);
					shot.setMetadata("snipershot", new FixedMetadataValue(Core.instance, player.getUniqueId().toString()));
					
					player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(-.5));
					rp.setActiveAbility(RAbility.RANGER_11, false);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(60);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	RANGER_12(new Ability("Flèche légendaire", Arrays.asList("Le ranger concentre son agilité", "dans sa prochaine flèche,", "pulvérisant la cible touchée"),
			RClass.RANGER, 12, false, "DDD,!SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			if(event instanceof EntityShootBowEvent) {
				EntityShootBowEvent event2 = (EntityShootBowEvent)event;
				event2.setCancelled(true);
				final Player player = rp.getPlayer();
				final Arrow arrow = player.launchProjectile(Arrow.class, player.getEyeLocation().getDirection().normalize().multiply(3*event2.getForce()));
				arrow.setCritical(!rp.getPlayer().isOnGround());
				arrow.setBounce(false);
				player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 1);
				arrow.setMetadata("RANGER_12", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

					@Override
					public void run() {
						if(arrow.isOnGround()){
							this.cancel();
						}
						arrow.getWorld().spawnParticle(Particle.SPELL_WITCH, arrow.getLocation(), 1, 0, 0, 0, 0);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskTimer(0, 1).getTaskId()));
				BukkitTask.tasks.get(player.getMetadata("RANGER_12").get(0).asInt()).cancel();
				rp.setActiveAbility(RAbility.RANGER_12, false);
			} else {
				this.takeVigor(rp);
				final Player player = rp.getPlayer();
				player.setMetadata("RANGER_12", new FixedMetadataValue(Core.instance, new BukkitTask(Core.instance){

					@Override
					public void run() {
						if(player.isOnline() && !player.isDead()) {
							player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation().add(0,.4,0), 2, .2, .8, .2);
						} else this.cancel();
					}

					@Override
					public void onCancel() {
						player.removeMetadata("RANGER_12", Core.instance);
						rp.setActiveAbility(RAbility.RANGER_12, false);
					}
					
				}.runTaskTimer(0, 0).getTaskId()));
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(final RPlayer rp, final LivingEntity target) {
			target.setFallDistance(-255);
			final Player player = rp.getPlayer();
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					target.setVelocity(new Vector(0,2,0));
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							final double number = 9;
							for(int i = 0;i < number;i++){
								final int index = i;
								new BukkitTask(Core.instance){

									@Override
									public void run() {
										if(index < 5){
											Location toSpawn = target.getLocation().toVector().add(new Vector(RandomUtils.random.nextDouble(), RandomUtils.random.nextDouble(), RandomUtils.random.nextDouble()).normalize().multiply(3)).toLocation(target.getWorld());
											Vector direction = new Vector(target.getLocation().getX()-toSpawn.getX(), target.getLocation().getY()-toSpawn.getY(), target.getLocation().getZ()-toSpawn.getZ());
											final Arrow arrow = target.getWorld().spawnArrow(toSpawn, direction, .6F, 12);
											arrow.setCritical(false);
											arrow.setBounce(false);
											arrow.setKnockbackStrength(0);
											new BukkitTask(Core.instance){

												@Override
												public void run() {
													arrow.remove();
													target.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, target.getLocation(), 5, 1, 1, 1, 0);
													target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .8F);
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskLater(5);
										}else if(index == number-1){
											new BukkitTask(Core.instance){

												@Override
												public void run() {
													target.setVelocity(new Vector(0,-1.75,0));
													target.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, target.getLocation(), 5, 1, 1, 1, 0);
													target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .5F);
													new BukkitTask(Core.instance){

														@Override
														public void run() {
															if(target.isDead())this.cancel();
															else if(target.isOnGround()){
																this.cancel();
																target.setFallDistance(0);
																target.getWorld().spawnParticle(Particle.LAVA, target.getLocation(), 23, .3, .3, .3, 1);
																target.getWorld().spawnParticle(Particle.CLOUD, target.getLocation(), 46, .5, .5, .5, .1);
																target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
																DamageManager.damage(target, player, getInstance().getDamages(rp), RDamageCause.ABILITY);
															}
														}

														@Override
														public void onCancel() {
															rp.setActiveAbility(RAbility.RANGER_12, false);
														}
														
													}.runTaskTimer(0, 0);
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskLater(5);
										}
									}

									@Override
									public void onCancel() {
									}
									
								}.runTaskLater(i*2);
							}
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(10);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(1);
		}
		
	}),	

	MAGE_1(new Ability("Manteau de feu", Arrays.asList("Le mage est entouré d'une aura de feu", "qui peut enflammer les ennemis l'assaillant"),
			RClass.MAGE, 1, true, "", "Probabilité", "%", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			if(Math.random() < this.getDamages(rp)*.01) {
				rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
				target.setFireTicks(100);
			}
		}
		
	}),
	MAGE_2(new Ability("Manteau de glace", Arrays.asList("Le mage est entouré d'une aura de glace", "qui peut piéger les ennemis l'assaillant"),
			RClass.MAGE, 2, true, "", "Probabilité", "%", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, final LivingEntity target) {
			if(Math.random() < this.getDamages(rp)*.01) {
				rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.BLOCK_GLASS_HIT, 1, 1);
				final AttributeModifier modifier = new AttributeModifier("RubidiaTrap", -10000.0, Operation.ADD_NUMBER);
				if(target instanceof Player){
					((Player) target).setWalkSpeed(0);
				}else{
					target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(modifier);
				}
				target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 444, true, false));
				final BlockData iceData = Bukkit.getServer().createBlockData(Material.ICE);
				for(Player player : Core.toPlayerList(target.getNearbyEntities(64, 64, 64))) {
					player.sendBlockChange(target.getLocation(), iceData);
					player.sendBlockChange(target.getLocation().add(0,1,0), iceData);
				}
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						target.getWorld().spawnParticle(Particle.DRIP_WATER, target.getLocation(), 4, .3, .1, .3, 0);
						target.getWorld().spawnParticle(Particle.SPELL_INSTANT, target.getLocation(), 1, .1, .1, .1);
						target.getWorld().spawnParticle(Particle.BLOCK_CRACK, target.getLocation(), 4, .3, .1, .3, iceData);
					}

					@Override
					public void onCancel() {
						for(Player player : Core.toPlayerList(target.getNearbyEntities(64, 64, 64))) {
							player.sendBlockChange(target.getLocation(), target.getLocation().getBlock().getBlockData());
							player.sendBlockChange(target.getLocation().add(0,1,0), target.getLocation().add(0,1,0).getBlock().getBlockData());
						}
						if(target instanceof Player){
							((Player) target).setWalkSpeed(Settings.DEFAULT_WALK_SPEED);
						}else{
							target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
						}
						target.removePotionEffect(PotionEffectType.JUMP);
					}
					
				}.runTaskTimerCancelling(0, 1, 70);
			}
		}
				
	}),
	MAGE_3(new Ability("Manteau de foudre", Arrays.asList("Le mage est entouré d'une aura de foudre", "qui peut foudroyer les ennemis l'assaillant"),
			RClass.MAGE, 3, true, "", "Probabilité", "%", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			if(Math.random() < this.getDamages(rp) * .01) {
				FakeLightning lightning = new FakeLightning(false);
				lightning.strike(target);
				DamageManager.damage(target, rp.getPlayer(), 2, RDamageCause.ABILITY);
			}
		}
				
	}),
	MAGE_4(new Ability("Aura protectrice", Arrays.asList("Le mage est naturellement protégé", "contre son propre élément : la magie"),
			RClass.MAGE, 4, true, "", "Résistance magique supp.", "%", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_5(new Ability("Soin", Arrays.asList("Le mage crée une zone de soin", "instantanée autour de lui"),
			RClass.MAGE, 5, false, "DDD,SN", "Vie restaurée", "PV", 0, false) {
		
		@Override
		public void run(RPlayer rp, Event event) {
			this.takeVigor(rp);
			Player player = rp.getPlayer();
			GMember member = GMember.get(player);
			double x = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - player.getHealth();
			final double y = this.getDamages(rp);
			player.setHealth(player.getHealth() + (y > x ? x : y));
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
			player.getWorld().spawnParticle(Particle.HEART, player.getEyeLocation().add(0,.5,0), 1, 0, 0, 0, 1);
			player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0,1,0), 60, .4, .4, .4, 1);
			List<Entity> near = player.getNearbyEntities(3.5, 3.5, 3.5);
			for(Player pnear : Core.toPlayerList(near)){
				GMember mtarget = GMember.get(pnear);
				if(!member.hasGuild() || (member.hasGuild() && mtarget.hasGuild())){
					if(!member.hasGuild() || member.getGuild().equals(mtarget.getGuild()) || member.getGuild().getRelationTo(mtarget.getGuild()).equals(Relation.ALLY)){
						double x1 = pnear.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - pnear.getHealth();
						pnear.setHealth(pnear.getHealth() + (y > x1 ? x1 : y));
						pnear.playSound(pnear.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
						player.getWorld().spawnParticle(Particle.HEART, pnear.getEyeLocation().add(0,.5,0), 1, 0, 0, 0, 1);
						player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, pnear.getLocation().add(0,1,0), 60, .4, .4, .4, 1);
					}
				}
			}
			rp.setActiveAbility(RAbility.MAGE_5, false);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_6(new Ability("Pluie de météores", Arrays.asList("Le mage invoque une pluie de météores", "au dessus de la cible"),
			RClass.MAGE, 6, false, "DGG,!SN", "Météores", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			final double damages = this.getDamages(rp);
			final double x = player.getTargetBlock(null, 15).getX();
			final double y = player.getTargetBlock(null, 15).getY() - 1;
			final double z = player.getTargetBlock(null, 15).getZ();
			
			Location loc = new Location(player.getWorld(), x, y+5, z);
			MageMeteor explo = new MageMeteor(loc.clone().add(0, 3, 0), loc.clone().subtract(0, 3, 0));
			explo.setPlayer(player);
			explo.run();
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					new BukkitTask(Core.instance){
						int step = 0;

						@Override
						public void run() {
							if(step <= (int)damages){
								double a = RandomUtils.random.nextDouble() * 2 * Math.PI;
								double dist = RandomUtils.random.nextDouble() * 3;
								Location loc = (new Location(player.getWorld(), x, y+5, z)).clone().add(dist * Math.sin(a), 0, dist * Math.cos(a));
								Fireball f = player.getWorld().spawn(loc, Fireball.class);
								f.setMetadata("mageMeteor", new FixedMetadataValue(Core.instance, player.getUniqueId().toString()));
								f.setDirection(new Vector(0, -2.5, 0));
								f.setYield(0);
								f.setShooter(player);
							}else this.cancel();
							step++;
						}

						@Override
						public void onCancel() {
							rp.setActiveAbility(RAbility.MAGE_6, false);
						}
						
					}.runTaskTimer(0, 6);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(10);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_7(new Ability("Téléportation", Arrays.asList("Le mage courbe l'epace-temps pour", "se téléporter là où il regarde"),
			RClass.MAGE, 7, false, "DDD,SP", "Portée", " blocs", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			final Player player = rp.getPlayer();
			int range = (int) this.getDamages(rp);
			Block targetBlock = LocationUtils.getTargetBlock(player, range);
			if (targetBlock.getType().isSolid()) {
				this.takeVigor(rp);
				
				double x = targetBlock.getX();
				double y = targetBlock.getY();
				double z = targetBlock.getZ();
				float yaw = player.getEyeLocation().getYaw();
				float pitch = player.getEyeLocation().getPitch();
				player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 75, .5, .5, .5, .001);
				TeleportHandler.teleport(player, new Location(player.getWorld(), x, y+1, z, yaw, pitch));
				player.setFallDistance(-100);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
				player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 75, .5, .5, .5, .001);
			    new BukkitTask(Core.instance){

					@Override
					public void run() {
			        	if(player.isDead())this.cancel();
			        	else{
			                if (((LivingEntity)player).isOnGround()){
			    				player.setFallDistance((float) 0.0);
								this.cancel();
				            }
			        	}
					}

					@Override
					public void onCancel() {
					}
			    	
			    }.runTaskTimer(0, 0);
			}else{
				rp.sendMessage("§cLe bloc que vous visez n'est pas atteignable !");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
			}
			rp.setActiveAbility(RAbility.MAGE_7, false);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_8(new Ability("Vent glacé", Arrays.asList("Le mage façonne un vent glacé", "blessant et ralentissant les ennemis touchés"),
			RClass.MAGE, 8, false, "DGG", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1, true, false));
		}
				
	}),
	MAGE_9(new Ability("Cercle de foudre", Arrays.asList("Le mage apelle la foudre pour la faire", "s'abattre sur les ennemis alentours"),
			RClass.MAGE, 9, false, "DGD,SN", "", "", 0, false) {
	
		@Override
		public void run(final RPlayer rp, Event event) {
			final Player player = rp.getPlayer();
			final List<LivingEntity> targets = DamageManager.toDamageableEntityList(player.getNearbyEntities(4, 4, 4));
			if(!targets.isEmpty()){
				this.takeVigor(rp);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, .25F);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1, .25F);
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						getInstance().damage(rp, targets);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskTimerCancelling(0,4,16);
			}else{
				rp.sendMessage("§cIl n'y a personne à foudroyer ici !");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.setActiveAbility(RAbility.MAGE_9, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			FakeLightning lightning = new FakeLightning(false);
			lightning.strike(target);
		}
				
	}),
	MAGE_10(new Ability("Kinésitechnie", Arrays.asList("Le mage maîtrise les pouvoirs kinétiques,", "chamboulant la situation dans les alentours"),
			RClass.MAGE, 10, false, "GDD,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			final Player player = rp.getPlayer();
			final LivingEntity target = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 5.5);
			if(target != null && DamageManager.canDamage(player, target, RDamageCause.ABILITY)){
				this.takeVigor(rp);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 70, 255, true, false));
				final List<LivingEntity> near = DamageManager.toDamageableEntityList(target.getNearbyEntities(1, 1, 1));
				final Vector[] relatives = new Vector[near.size()];
				for(int i = 0;i < near.size();i++){
					LivingEntity e = near.get(i);
					e.setGravity(false);
					relatives[i] = e.getLocation().toVector().subtract(target.getLocation().toVector());
				}
				target.setGravity(false);
				
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.91F);
					}

					@Override
					public void onCancel() {
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.94F);
							}

							@Override
							public void onCancel() {
								new BukkitTask(Core.instance){

									@Override
									public void run() {
										player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.96F);
									}

									@Override
									public void onCancel() {
										new BukkitTask(Core.instance){

											@Override
											public void run() {
												player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 1.98F);
											}

											@Override
											public void onCancel() {
												new BukkitTask(Core.instance){

													@Override
													public void run() {
														player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, 2.0F);
													}

													@Override
													public void onCancel() {
													}
													
												}.runTaskTimerCancelling(0,1,5);
											}
											
										}.runTaskTimerCancelling(0,2,5);
									}
									
								}.runTaskTimerCancelling(0,3,15);
							}
							
						}.runTaskTimerCancelling(0,4,20);
					}
					
				}.runTaskTimerCancelling(0,5,25);
				
				new BukkitTask(Core.instance){

					@Override
					public void run() {
						Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(6.5));
						for(int i = 0;i < near.size();i++){
							LivingEntity e = near.get(i);
							e.teleport(location.clone().add(relatives[i]), TeleportCause.PLUGIN);
							player.getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation().add(0,-.15,0), 3, .2, .8, .2, .65);
							relatives[i] = relatives[i].multiply(.96);
						}
						target.teleport(location, TeleportCause.PLUGIN);
						player.getWorld().spawnParticle(Particle.SPELL_WITCH, target.getLocation().add(0,-.15,0), 3, .2, .8, .2, .65);
					}

					@Override
					public void onCancel() {
						near.add(target);
						getInstance().damage(rp, near);
						rp.setActiveAbility(RAbility.MAGE_10, false);
					}
					
				}.runTaskTimerCancelling(0,0,70);
			}else{
				rp.sendMessage("§cVous ne visez aucun ennemi atteignable !");
				rp.setActiveAbility(RAbility.MAGE_10, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
			Player player = rp.getPlayer();
			target.setGravity(true);
			target.setFallDistance(-100);
			target.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, target.getLocation(), 1);
			target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10.0F, 1F);
			target.setVelocity(target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize());
		}
				
	}),
	MAGE_11(new Ability("Vent glacé", Arrays.asList("Le mage façonne un vent glacé", "blessant et ralentissant les ennemis touchés"),
			RClass.MAGE, 11, false, "DGG", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	MAGE_12(new Ability("Armageddon", Arrays.asList("Le mage crée un cercle de feu autour de lui", "avant de détruire le terrain"),
			RClass.MAGE, 12, false, "DDD,!SN!SP", "", "", 0, false) {
	
		@Override
		public void run(final RPlayer rp, Event event) {
			rp.setActiveAbility(RAbility.MAGE_12, true);
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.setWalkSpeed(0);
			
			final Location center = player.getLocation();
			Vector vector = new Vector(3,0,0);
			final List<Block> blocks = new ArrayList<Block>();
			final List<Material> types = new ArrayList<Material>();
			for(int i = 0;i < 60;i++){
				VectorUtils.rotateAroundAxisY(vector, 6);
				Block block = LocationUtils.getSafeLocation(center.toVector().add(vector).toLocation(center.getWorld())).getBlock();
				if(!blocks.contains(block)){
					blocks.add(block);
					types.add(block.getType());
				}
			}
			
			final int duration = 50;
			final int ticksBetween = duration/(blocks.size()+1);
			new BukkitTask(Core.instance){
				int step = 0;
				int ticks = 0;
				int ticksStep = 0;

				@Override
				public void run() {
					if(step < duration){
						player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 11, 2, .1, 2, .1);
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, step);
						if(ticks == ticksBetween){
							if(blocks.size() > ticksStep){
								final int id = ticksStep;
								blocks.get(id).setType(Material.FIRE);
								new BukkitTask(Core.instance){

									@Override
									public void run() {
										this.cancel();
									}

									@Override
									public void onCancel() {
										for(int i = 0;i < blocks.size();i++){
											blocks.get(i).setType(types.get(i));
										}
									}
									
								}.runTaskLater(90);
								ticksStep++;
							}
							ticks = 0;
						}
					}else{
						this.cancel();
						
						final FakeLightning lightning = new FakeLightning(false);
						for(int i = 0;i < 9;i++){
							new BukkitTask(Core.instance){
								public void run(){
									Location destination = player.getLocation().toVector().add(new Vector(RandomUtils.random.nextDouble(), 0, RandomUtils.random.nextDouble()).multiply(RandomUtils.random.nextInt(4)*(RandomUtils.random.nextDouble()+RandomUtils.random.nextInt(2)+1))).toLocation(player.getWorld());
									lightning.strike(destination);
									player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 5, 1, 1, 1, .5);
									player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .8F);
									
									getInstance().damage(rp, player.getNearbyEntities(3, 3, 3));
								}

								@Override
								public void onCancel() {
								}
								
							}.runTaskLater(i*4);
						}
					}
					step++;
					ticks++;
				}

				@Override
				public void onCancel() {
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 5, 1, 1, 1, .5);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, .5F);

							getInstance().damage(rp, player.getNearbyEntities(3, 3, 3));

							player.setWalkSpeed(Settings.DEFAULT_WALK_SPEED);
							rp.setActiveAbility(RAbility.MAGE_12, false);
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(29);
				}
				
			}.runTaskTimer(0, 1);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	
	
	ASSASSIN_1(new Ability("Attaque furtive", Arrays.asList("L'assassin transperce l'ennemi cible", "extrêmement rapidement"),
			RClass.ASSASSIN, 1, false, "DDG,!SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			Player player = rp.getPlayer();
			LivingEntity entity = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 4.0);
			if(entity != null){
				Location edloc = entity.getLocation();
				if(edloc.distanceSquared(player.getLocation()) < 33){
					this.takeVigor(rp);
					double xedloc = edloc.getX();
					double yedloc = edloc.getY();
					double zedloc = edloc.getZ();
					Location ploc = player.getLocation();
					double xploc = ploc.getX();
					double zploc = ploc.getZ();
					Vector v = new Vector((xedloc-xploc)*1.4, 0, (zedloc-zploc)*1.4);
					double xv = xploc + v.getX();
					double yv = yedloc + v.getY();
					double zv = zploc + v.getZ();
					float yaw = player.getEyeLocation().getYaw();
					if(yaw > 0){
						yaw = yaw-180;
					}else{
						yaw = yaw+180;
					}
					player.getWorld().playEffect(player.getLocation().add(0, -1, 0), Effect.MOBSPAWNER_FLAMES, 1);
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .25F);
					Location finalloc = LocationUtils.getSafeLocation(new Location(player.getWorld(), xv, yv, zv, yaw, 0.0f));
					TeleportHandler.teleport(player, finalloc);
					player.getWorld().playEffect(player.getLocation().add(0, -1, 0), Effect.MOBSPAWNER_FLAMES, 1);

					this.damage(rp, player.getNearbyEntities(2, 2, 2));
				}else{
					rp.sendMessage("§cLa cible est trop loin !");
				}
			}else{
				rp.sendMessage("§cIl n'y a personne en face de vous !");
			}
			rp.setActiveAbility(RAbility.ASSASSIN_1, false);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_2(new Ability("Camouflage", Arrays.asList("L'assassin se camoufle entièrement", "pendant un laps de temps"),
			RClass.ASSASSIN, 2, false, "DDD,SN", "Durée", " sec", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 100, .5, .5, .5, .1);
			player.getWorld().spawnParticle(Particle.CLOUD, player.getEyeLocation(), 100, .5, .5, .5, .1);
			if(rp.isVanished())rp.setVanished(false);
			for(Player target : Bukkit.getOnlinePlayers()){
				target.hidePlayer(Core.instance, player);
			}
			final ItemStack helmet = player.getEquipment().getHelmet();
			if(helmet != null){
				ItemStack pumpkin = helmet.clone();
				pumpkin.setType(Material.PUMPKIN);
				player.getEquipment().setHelmet(pumpkin);
			}else player.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN, 1));
			rp.sendMessage("§7Viouf! Plus personne ne vous voit !");
			new BukkitTask(Core.instance){
				public void run(){
					rp.sendMessage("§cVotre camouflage commence à disparaître !");
					new BukkitTask(Core.instance){
						public void run(){
							player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 100, .5, .5, .5, .1);
							player.getWorld().spawnParticle(Particle.CLOUD, player.getEyeLocation(), 100, .5, .5, .5, .1);
							for(Player target : Bukkit.getOnlinePlayers()){
								target.showPlayer(Core.instance, player);
							}
							if(helmet != null)player.getEquipment().setHelmet(helmet);
							else player.getEquipment().setHelmet(new ItemStack(Material.AIR, 1));
							rp.sendMessage("§4Votre camouflage a disparu !");
							rp.setActiveAbility(RAbility.ASSASSIN_2, false);
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(70);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater((long) (this.getDamages(rp)*20));
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_3(new Ability("Vitesse flash", Arrays.asList("L'assassin se déplacer naturellement", "plus rapidement que quiconque"),
			RClass.ASSASSIN, 3, false, "", "Vitesse", "%", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_4(new Ability("Évasion", Arrays.asList("L'assassin s'échappe du combat en lâchant", "une grenade le propulsant haut et loin"),
			RClass.ASSASSIN, 4, false, "DGD,SN", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			this.takeVigor(rp);
			final Player player = rp.getPlayer();
			Location eyes = player.getEyeLocation().getDirection().normalize().toLocation(player.getWorld());
			double x = eyes.getX()*1.65;
			double z = eyes.getZ()*1.65;
			player.getWorld().createExplosion(player.getLocation(), 0);
			this.damage(rp, player.getNearbyEntities(3, 3, 3));
			player.setVelocity(new Vector(-x*rp.getAbLevel(4)*.21, rp.getAbilityLevel(RAbility.ASSASSIN_4)*0.07+.28, -z*rp.getAbLevel(4)*.21));
			player.setFallDistance(-100);
			new BukkitTask(Core.instance){

				@Override
				public void run() {
		        	if(player.isDead()){
		        		this.cancel();
		        	}else{
		                if (((LivingEntity)player).isOnGround()){
							player.setFallDistance((float) 0.0);
							player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2, true, false));
							this.cancel();
			            }
		        	}
				}

				@Override
				public void onCancel() {
					rp.setActiveAbility(RAbility.ASSASSIN_4, false);
				}
				
			}.runTaskTimer(25, 2);
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_5(new Ability("Piège", Arrays.asList("L'assassin enlace sa cible de sorte qu'elle", "ne puisse bouger pendant un laps de temps"),
			RClass.ASSASSIN, 5, false, "DDG,SN", "Durée", " sec", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			int range = 9;
			final Player player = rp.getPlayer();
			final LivingEntity target = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
			if(target != null){
				if(target.getLocation().distanceSquared(player.getLocation()) <= range*range){
					this.takeVigor(rp);
					RAbility.immobilize(target, (int) Math.round(this.getDamages(rp)*20));
					rp.setActiveAbility(RAbility.ASSASSIN_5, false);
				}else{
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					rp.sendMessage("§cCet ennemi est trop loin !");
					rp.setActiveAbility(RAbility.ASSASSIN_5, false);
				}
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cIl n'y a personne en face de vous !");
				rp.setActiveAbility(RAbility.ASSASSIN_5, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_6(new Ability("Diversion", Arrays.asList("L'assassin crée un épais nuage", "empoisonné autour de lui"),
			RClass.ASSASSIN, 6, false, "DDD,!SN", "", " / sec", 5, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			Player player = rp.getPlayer();
			player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0,1,0), 50, 1, 1, 1, .001);
			player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(0,1,0), 42, 1, 1, 1, 0);
			player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 22, 1, 1, 1, .001);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
		}

		@Override
		public void onEffect(RPlayer rp) {
			Player player = rp.getPlayer();
			player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0,1,0), 50, 1, 1, 1, .001);
			player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(0,1,0), 42, 1, 1, 1, 0);
			player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 22, 1, 1, 1, .001);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
			this.damage(rp, player.getNearbyEntities(3, 3, 3), .25);
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_7(new Ability("Attaque perçante", Arrays.asList("L'assassin traverse sa cible,", "l'obligeant à reculer de quelques blocs"),
			RClass.ASSASSIN, 7, false, "GGD,SP", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			double range = 7;
			
			final Player player = rp.getPlayer();
			final LivingEntity e = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
			if(e != null){
				if(e.getLocation().distanceSquared(player.getLocation()) <= range*range){
					this.takeVigor(rp);
					Location entityLoc = e.getLocation();
					Location pLoc = player.getLocation();
					Vector motion = new Vector(entityLoc.getX()-pLoc.getX(), entityLoc.getY()-pLoc.getY(),entityLoc.getZ()-pLoc.getZ()).multiply(.45);
					player.setMetadata("assassinGamemode", new FixedMetadataValue(Core.instance, player.getGameMode().toString()));
					player.setGameMode(GameMode.SPECTATOR);
					player.setVelocity(motion);
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							if(player.getLocation().distanceSquared(e.getLocation()) < 3){
								player.setGameMode(GameMode.valueOf(player.getMetadata("assassinGamemode").get(0).asString()));
								DamageManager.damage(e, player, getInstance().getDamages(rp), RDamageCause.ABILITY);
								e.getWorld().createExplosion(e.getLocation(), 0);
								player.getWorld().spawnParticle(Particle.LAVA, e.getLocation(), 16, .3, .3, .3);
								e.setVelocity(new Vector(1/(e.getLocation().getX()-player.getLocation().getX()),1,1/(e.getLocation().getZ()-player.getLocation().getZ())));
								this.cancel();
							}
						}

						@Override
						public void onCancel() {
							rp.setActiveAbility(RAbility.ASSASSIN_7, false);
						}
						
					}.runTaskTimer(0, 0);
				}else{
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					rp.sendMessage("§cCet ennemi est trop loin !");
					rp.setActiveAbility(RAbility.ASSASSIN_7, true);
				}
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cIl n'y a personne en face de vous !");
				rp.setActiveAbility(RAbility.ASSASSIN_7, true);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	}),
	ASSASSIN_8(new Ability("Super Smash", Arrays.asList("L'assassin dash très violemment sur sa cible"),
			RClass.ASSASSIN, 8, false, "DGD", "", "", 0, false) {
		
		@Override
		public void run(final RPlayer rp, Event event) {
			double range = 6;
			
			final Player player = rp.getPlayer();
			final double damages = this.getDamages(rp);
			final LivingEntity e = Utils.getInSightDamageableEntity(player, RDamageCause.ABILITY, 50.0);
			if(e != null){
				if(e.getLocation().distanceSquared(player.getLocation()) <= range*range){
					player.setSprinting(false);
					this.takeVigor(rp);
					Vector toe = e.getLocation().toVector().subtract(player.getLocation().toVector());
					player.setVelocity(toe.multiply(.5).add(new Vector(0,1,0)));
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							Vector toe = e.getLocation().toVector().subtract(player.getLocation().toVector());
							player.setVelocity(toe);
							new BukkitTask(Core.instance){

								@Override
								public void run() {
									player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 20, .5, .5, .5);
									if(player.isOnGround()){
										Block block = player.getLocation().subtract(0,1,0).getBlock();
										player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 50, .5, .5, .5, block.getBlockData());
										player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 1);
										final List<LivingEntity> nearest = DamageManager.toDamageableEntityList(player.getNearbyEntities(2, 2, 2));
										if(!nearest.isEmpty()){
											for(LivingEntity enear : nearest){
												DamageManager.damage(enear, player, damages/2., RDamageCause.ABILITY);
												enear.setVelocity(new Vector(0,1.5,0));
												enear.setFallDistance(-100);
											}
											TeleportHandler.teleport(player, new Location(nearest.get(0).getWorld(), nearest.get(0).getLocation().getX(),nearest.get(0).getLocation().getY(),nearest.get(0).getLocation().getZ(), player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch()));
											player.setVelocity(new Vector(0,1.62,0));
											player.setFallDistance(-100);
											new BukkitTask(Core.instance){

												@Override
												public void run() {
													player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 7, 1, 1, 1);
													player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
													
													for(LivingEntity enear : nearest){
														enear.setVelocity(new Vector(0,-2,0));
													}

													final List<LivingEntity> onGround = new ArrayList<LivingEntity>();
													new BukkitTask(Core.instance){

														@Override
														public void run() {
															if(!onGround.containsAll(nearest)){
																for(LivingEntity enear : nearest){
																	if(enear.isOnGround()){
																		if(!onGround.contains(enear)){
																			player.getWorld().spawnParticle(Particle.LAVA, enear.getLocation(), 50, .5, .5, .5);
																			enear.getWorld().playSound(enear.getLocation(), Sound.ENTITY_SKELETON_HURT, 1, 1);
																			DamageManager.damage(enear, player, damages/2., RDamageCause.ABILITY);
																			enear.setFallDistance(0);
																			onGround.add(enear);
																		}
																	}
																}
															}else this.cancel();
														}

														@Override
														public void onCancel() {
															player.setFallDistance(0);
															rp.setActiveAbility(RAbility.ASSASSIN_8, false);
														}
														
													}.runTaskTimerCancelling(0, 0, 100);
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskLater(20);
										}
										this.cancel();
									}
								}

								@Override
								public void onCancel() {
								}
								
							}.runTaskTimer(0, 0);
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(10);
				}else{
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					rp.sendMessage("§cCet ennemi est trop loin !");
					rp.setActiveAbility(RAbility.ASSASSIN_8, false);
				}
			}else{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				rp.sendMessage("§cIl n'y a personne en face de vous !");
				rp.setActiveAbility(RAbility.ASSASSIN_8, false);
			}
		}

		@Override
		public void onEffect(RPlayer rp) {
		}

		@Override
		public void onCancel(RPlayer rp) {
		}

		@Override
		public void animate(RPlayer rp, LivingEntity target) {
		}
				
	});
	
	private final Ability ability;
	private RAbility(Ability ability) {
		this.ability = ability;
	}
	
	
	public Ability getAbility() {
		return ability;
	}
	
	public List<String> getDescription() {
		return ability.getDescription();
	}

	public boolean isPassive() {
		return ability.isPassive();
	}

	public String getSequence() {
		return ability.getSequence();
	}

	public String getName() {
		return ability.getName();
	}

	public Mastery getMastery() {
		return ability.getMastery();
	}

	public String getSuppInfo() {
		return ability.getSuppInfo();
	}

	public String getUnit() {
		return ability.getUnit();
	}

	public int getToggleableTicks() {
		return ability.getToggleableTicks();
	}
	
	public boolean isToggleable() {
		return ability.isToggleable();
	}

	public RClass getRClass() {
		return ability.getRClass();
	}

	public int getIndex() {
		return ability.getIndex();
	}
	
	public void execute(RPlayer rp, Event event) {
		ability.execute(rp, event);
	}

	public double getDamages(RPlayer rp) {
		return ability.getDamages(rp);
	}
	
	public double getVigorCost(RPlayer rp) {
		return ability.getVigorCost(rp);
	}

	public RAbilitySettings getSettings() {
		return ability.getSettings();
	}
	
	public boolean isMelee() {
		return ability.isMelee();
	}
	
	public void damage(RPlayer rp, List<LivingEntity> targets, double... factor) {
		ability.damage(rp, targets, factor);
	}

	public static List<RAbility> getAvailable(RPlayer rp) {
		List<RAbility> abilities = new ArrayList<RAbility>();
		for(RAbility ability : RAbility.values()){
			if(rp.getRClass().equals(ability.getRClass()) && rp.isAtLeast(ability.getMastery()) && rp.getAbilityLevel(ability) > 0){
				abilities.add(ability);
			}
		}
		return abilities;
	}
	
	
	public static List<RAbility> getAvailable(RClass rClass){
		List<RAbility> abilities = new ArrayList<RAbility>(/*RAbility.VAGRANT_1*/);
		for(RAbility ability : RAbility.values()) {
			if(ability.getRClass().equals(rClass) || ability.getRClass().equals(RClass.VAGRANT)) {
				abilities.add(ability);
			}
		}
		return abilities;
	}
	
	public static void stun(LivingEntity target, int ticks) {
		if(target instanceof Player) {
			final Player player = (Player) target;
			final WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
			packet.setAction(com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS);
			packet.setCenterX(player.getLocation().getX());
			packet.setCenterZ(player.getLocation().getZ());
			packet.setRadius(0);
			packet.setWarningDistance(Integer.MAX_VALUE);
			packet.sendPacket(player);
			player.setWalkSpeed(0);
			new BukkitTask(Core.instance) {

				@Override
				public void run() {
					if(player.getWorld().getWorldBorder() != null) {
						packet.setCenterX(player.getWorld().getWorldBorder().getCenter().getX());
						packet.setCenterZ(player.getWorld().getWorldBorder().getCenter().getZ());
						packet.setRadius(player.getWorld().getWorldBorder().getSize());
						packet.setWarningDistance(player.getWorld().getWorldBorder().getWarningDistance());
					} else {
						packet.setRadius(Integer.MAX_VALUE);
						packet.setWarningDistance(0);
					}
					packet.sendPacket(player);
					player.setWalkSpeed(Settings.DEFAULT_WALK_SPEED);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(ticks);
		} else {
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 5, false, false, true));
		}
		target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false, true));
		target.getWorld().spawnParticle(Particle.TOTEM, target.getLocation().add(0,target.getHeight(),0), 12, target.getWidth(), target.getHeight()/10., target.getWidth(), 0);
	}
	
	public static void immobilize(final LivingEntity target, int ticks) {
		final AttributeModifier modifier = new AttributeModifier("RubidiaTrap", -10000.0, Operation.ADD_NUMBER);
		if(target instanceof Player){
			((Player) target).setWalkSpeed(0);
		}else{
			target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(modifier);
		}
		target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 444, true, false));
		new BukkitTask(Core.instance){

			@Override
			public void run() {
				Block block = target.getLocation().subtract(0,1,0).getBlock();
				target.getWorld().spawnParticle(Particle.SPELL_WITCH, target.getLocation(), 4, .3, .1, .3);
				target.getWorld().spawnParticle(Particle.SPELL_INSTANT, target.getLocation(), 1, .1, .1, .1);
				target.getWorld().spawnParticle(Particle.BLOCK_CRACK, target.getLocation(), 4, .3, .1, .3, block.getBlockData());
			}

			@Override
			public void onCancel() {
				if(target instanceof Player){
					((Player) target).setWalkSpeed(Settings.DEFAULT_WALK_SPEED);
				}else{
					target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
				}
				target.removePotionEffect(PotionEffectType.JUMP);
			}
			
		}.runTaskTimerCancelling(0, 1, ticks);
	}

}
