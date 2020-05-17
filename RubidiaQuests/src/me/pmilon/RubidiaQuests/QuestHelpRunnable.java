package me.pmilon.RubidiaQuests;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PNJSession;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class QuestHelpRunnable extends BukkitTask {

	private Player player;
	private RPlayer rp;
	private Quest quest;
	private Location target;
	public QuestHelpRunnable(Plugin plugin, Player player, Quest quest){
		super(plugin);
		this.player = player;
		this.rp = RPlayer.get(player);
		this.quest = quest;
	}
	
	@Override
	public void run() {
		if(this.getRPlayer().isOnline()){
			if(this.getQuest().hasFinished(this.getRPlayer())){
				PNJHandler pnj = this.getQuest().getHolder(this.getRPlayer());
				if(pnj != null){
					if(pnj.getEntity() != null){
						this.target = pnj.getEntity().getLocation();
					}
				}
			}else{
				for(Objective objective : this.getQuest().getObjectives()){
					if(this.target == null){
						if(objective.isAvailable(this.getRPlayer()) && !objective.isFilled(this.getRPlayer())){
							if(objective.getType().equals(ObjectiveType.DISCOVER) || objective.getType().equals(ObjectiveType.FOLLOW)){
								this.target = objective.getLocation();
							}else if(objective.getType().equals(ObjectiveType.KILL) || objective.getType().equals(ObjectiveType.LEASH)){
								Monster monster = objective.getMonster();
								if(monster != null){
									if(objective.getType().equals(ObjectiveType.LEASH)){
										int amount = 0;
										for(Entity entity : rp.getPlayer().getNearbyEntities(8, 8, 8)){
											if (entity instanceof LivingEntity) {
												LivingEntity living = (LivingEntity) entity;
												if(living.isLeashed()){
													if(living.getLeashHolder().equals(rp.getPlayer())){
														Monster m = Monsters.entities.get(living);
														if(m != null){
															if(m.getUUID().equals(monster.getUUID()) && m.getTamer() != null){
																if(m.getTamer().equals(rp.getPlayer())){
																	amount++;
																}
															}
														}
													}
												}
											}
										}
										if(amount >= objective.getAmount()){
											for(PNJSession session : PNJManager.pnjs.values()){
												if(session.getPNJHandler().getUniqueId().equals(objective.getTargetUUID())){
													this.target = session.getPNJHandler().getEntity().getLocation();
													break;
												}
											}
											break;
										}
									}
									Region rg = null;
									double distance = -1;
									for(Region region : Regions.regions){
										if(region.getMonsters().contains(monster)){
											if(region.getCenter().getWorld().equals(this.getPlayer().getWorld())){
												double dist = region.getCenter().distanceSquared(this.getPlayer().getLocation());
												if(dist < distance || distance == -1){
													rg = region;
													distance = dist;
												}
											}
										}
									}
									if(rg != null){
										if(!rg.isIn(this.getPlayer().getLocation())){
											this.target = rg.getCenter();
										}
									}
								}
							}else if(objective.getType().equals(ObjectiveType.TALK) || objective.getType().equals(ObjectiveType.GET)){
								LivingEntity target = null;
								for(PNJSession session : PNJManager.pnjs.values()){
									if(session.getPNJHandler().getUniqueId().equals(objective.getTargetUUID())){
										boolean ok = false;
										if(objective.getType().equals(ObjectiveType.TALK))ok = true;
										else if(objective.getType().equals(ObjectiveType.GET) && rp.getPlayer().getInventory().containsAtLeast(objective.getItemStack(),1)){
											ok = true;
										}
										if(ok){
											target = session.getPNJHandler().getEntity();
											break;
										}
									}
								}
								if(target != null){
									this.target = target.getLocation();
								}
							}
						}
					}
				}
			}
			
			Location target = this.getTarget();
			if(target != null){
				if(this.getPlayer().getWorld().equals(target.getWorld())){
					Location pLoc = this.getPlayer().getEyeLocation().add(0,.52,0);
			        Vector link = target.clone().add(0,.8,0).toVector().subtract(pLoc.toVector());
			        float length = (float) link.length();
			        link.normalize();

			        float ratio = .4F;
			        final int particles = 3;
			        int iterations = (int) (length / ratio)/particles;
			        final Vector v = link.multiply(ratio);
			        final Location loc = pLoc.clone().subtract(v);
			        for(int i = 0; i < iterations; i++){
			        	new BukkitTask(QuestsPlugin.instance){
			        		public void run(){
						        if(getPlayer().getWorld().equals(loc.getWorld())){
						        	for(int p = 0;p < particles;p++){
							            loc.add(v);
							            getPlayer().spawnParticle(Particle.DRIP_WATER, loc, 1, 0, 0, 0);
							            getPlayer().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0);
						        	}
						        }
			        		}

							@Override
							public void onCancel() {
							}
			        	}.runTaskLater(i);
			        }
				}
				
				this.target = null; //to reset objectives if finished
			}
		}else this.cancel();
	}

	private Location getTarget() {
		return target;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	@Override
	public void onCancel() {
		this.getRPlayer().setQuestHelpTask(null);
	}

	public RPlayer getRPlayer() {
		return rp;
	}

	public void setRPlayer(RPlayer rp) {
		this.rp = rp;
	}
}
