package me.pmilon.RubidiaCore.duels;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.chat.RChatUtils;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.RandomUtils;

public class RDuel {

	public enum RDuelOutcome {
		
		CHALLENGER_WINNER, TIE, CHALLENGER_LOSER, CHALLENGER_FORFAIT, CHALLENGED_FORFAIT;
		
	}
	
	private final RPlayer challenger;
	private final RPlayer challenged;
	private final boolean competitive;
	private Location center;
	private double radius;
	private double height;
	private BukkitTask requestTimeout;
	private BukkitTask duelTimeout;
	private BukkitTask duelVisualizer;
	private boolean started = false;
	
	private double challengerHealth;
	private double challengedHealth;
	private double challengerMana;
	private double challengedMana;
	private int challengerFood;
	private int challengedFood;
	public RDuel(RPlayer challenger, RPlayer challenged, boolean competitive){
		this.challenger = challenger;
		this.challenged = challenged;
		this.competitive = competitive;
	}
	
	public RPlayer getChallenger() {
		return challenger;
	}

	public RPlayer getChallenged() {
		return challenged;
	}

	public Location getCenter() {
		return center;
	}
	
	public void setCenter(Location center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public void request(){
		this.getChallenger().getDuels().add(this);
		final RDuel instance = this;
		this.setRequestTimeout(new BukkitTask(Core.instance){

			@Override
			public void run() {
				getChallenger().getDuels().remove(instance);
				getChallenger().sendMessage("§4" + getChallenged().getName() + "§c n'a pas répondu à votre invitation en duel à temps...");
				getChallenged().sendMessage("§cVous n'avez pas répondu à l'invitation en duel de §4" + getChallenger().getName() + "§c à temps...");
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(Settings.DUEL_REQUEST_TIME*20));
		this.getChallenged().sendTitle("§6§lNouveau duel" + (this.isCompetitive() ? " compétitif" : ""), ("§eVous avez été défié par ") + this.getChallenger().getName(), 10, 160, 10);
		this.getChallenger().sendMessage("§eVous venez de défier §6" + this.getChallenged().getName() + "§e en duel" + (this.isCompetitive() ? " compétitif" : "") + ".");
	}
	
	public void cancelRequest(){
		if(this.getRequestTimeout() != null){
			this.getRequestTimeout().cancel();
			this.setRequestTimeout(null);
		}
		this.getChallenger().getDuels().remove(this);
		this.getChallenger().sendMessage("§cVotre invitation en duel contre §4" + getChallenged().getName() + "§c a été annulée.");
		this.getChallenged().sendMessage("§4" + getChallenger().getName() + "§c a annulé sont invitation en duel.");
	}
	
	public void start(){
		if(this.getRequestTimeout() != null){
			this.getRequestTimeout().cancel();
			this.setRequestTimeout(null);
		}
		this.getChallenger().getDuels().remove(this);
		this.getChallenger().setCurrentDuel(this);
		this.getChallenged().setCurrentDuel(this);
		if(this.getChallenged().isOnline() && this.getChallenger().isOnline()){
			final Player p1 = this.getChallenger().getPlayer();
			final Player p2 = this.getChallenged().getPlayer();
			p1.setNoDamageTicks(110);
			p2.setNoDamageTicks(110);
			this.challengerHealth = p1.getHealth();
			this.challengedHealth = p2.getHealth();
			this.challengerMana = this.getChallenger().getVigor();
			this.challengedMana = this.getChallenged().getVigor();
			this.challengerFood = p1.getFoodLevel();
			this.challengedFood = p2.getFoodLevel();
			this.getChallenger().heal();
			this.getChallenged().heal();
			this.getChallenger().setVigor(this.getChallenger().getMaxVigor());
			this.getChallenged().setVigor(this.getChallenged().getMaxVigor());
			p1.setFoodLevel(20);
			p2.setFoodLevel(20);
			this.count();
		}
	}
	
	private void count(){
		final Player p1 = this.getChallenger().getPlayer();
		final Player p2 = this.getChallenged().getPlayer();
		this.getChallenger().sendTitle(("§eLe duel va débuter..."), "§6" + this.getChallenger().getName() + " §eVS §6" + this.getChallenged().getName(), 0, 30, 5);
		this.getChallenged().sendTitle(("§eLe duel va débuter..."), "§6" + this.getChallenged().getName() + " §eVS §6" + this.getChallenger().getName(), 0, 30, 5);
		Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
			public void run(){
				getChallenger().sendTitle("§c3...", "", 0, 10, 5);
				getChallenged().sendTitle("§c3...", "", 0, 10, 5);
				p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
					public void run(){
						getChallenger().sendTitle("§c2...", "", 0, 10, 5);
						getChallenged().sendTitle("§c2...", "", 0, 10, 5);
						p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
						p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
						Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
							public void run(){
								getChallenger().sendTitle("§41...", "", 0, 10, 5);
								getChallenged().sendTitle("§41...", "", 0, 10, 5);
								p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
								p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
								Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
									public void run(){
										getChallenger().sendTitle("§4FIGHT!", "", 0, 10, 5);
										getChallenged().sendTitle("§4FIGHT!", "", 0, 10, 5);
										p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2F);
										p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2F);
										setStarted(true);
										Vector link = p2.getLocation().toVector().subtract(p1.getLocation().toVector()).multiply(.5);
										setCenter(p1.getLocation().add(link));
										setRadius(Math.max(link.length()+4.0,8.0));
										setHeight(Math.abs(link.getY())+40.0);
										final List<Location> locations = new ArrayList<Location>();
										for(double i = 0;i <= Math.PI/2;i += Math.PI/(getRadius()*120/8.0)){
											double x = getRadius()*Math.cos(i);
											double z = getRadius()*Math.sin(i);
											locations.add(getCenter().clone().add(x,0,z));
											locations.add(getCenter().clone().add(x,0,-z));
											locations.add(getCenter().clone().add(-x,0,-z));
											locations.add(getCenter().clone().add(-x,0,z));
										}
										setDuelVisualizer(new BukkitTask(Core.instance){

											@Override
											public void run() {
												int baseY1 = p1.getLocation().getBlockY();
												int baseY2 = p2.getLocation().getBlockY();
												for(Location location : locations){
													Location location1 = location.clone().add(0,baseY1-location.getY()+3+RandomUtils.random.nextDouble(),0);
													if(location1.distanceSquared(p1.getLocation()) <= 81){
														p1.spawnParticle(Settings.DUEL_WALL_PARTICLE, location1, 1, 0, 0, 0, RandomUtils.random.nextDouble()*.2);
													}
													Location location2 = location.clone().add(0,baseY2-location.getY()+3+RandomUtils.random.nextDouble(),0);
													if(location2.distanceSquared(p2.getLocation()) <= 81){
														p2.spawnParticle(Settings.DUEL_WALL_PARTICLE, location2, 1, 0, 0, 0, RandomUtils.random.nextDouble()*.2);
													}
												}
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskTimer(0, 6));
										setDuelTimeout(new BukkitTask(Core.instance){

											@Override
											public void run() {
												reward(RDuelOutcome.TIE);
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskLater(Settings.DUEL_TIMEOUT*60*20));
										
									}
								}, 20);
							}
						}, 20);
					}
				}, 20);
			}
		}, 40);
	}
	
	public void reward(RDuelOutcome outcome){
		if(this.getChallenger().isOnline()){
			final Player p1 = this.getChallenger().getPlayer();
			this.resetStats(p1);
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(p1 != null)p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5F);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskTimerCancelling(0, 4, 12);
		}
		if(this.getChallenged().isOnline()){
			final Player p2 = this.getChallenged().getPlayer();
			this.resetStats(p2);
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(p2 != null)p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5F);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskTimerCancelling(0, 5, 15);
		}
		this.getChallenger().setCurrentDuel(null);
		this.getChallenged().setCurrentDuel(null);
		if(this.getDuelTimeout() != null){
			this.getDuelTimeout().cancel();
			this.setDuelTimeout(null);
		}
		if(this.getDuelVisualizer() != null){
			this.getDuelVisualizer().cancel();
			this.setDuelVisualizer(null);
		}
		int old_renom1 = this.getChallenger().getRenom();
		int old_renom2 = this.getChallenged().getRenom();
		switch(outcome){
		case CHALLENGER_WINNER:
			this.getChallenger().sendTitle(("§2§lVictoire !"), ("§aVous remportez le duel"), 10, 160, 10);
			this.getChallenged().sendTitle(("§4§lDéfaite..."), ("§cVous perdez le duel"), 10, 160, 10);
			if(this.isCompetitive()){
				int bonus = 0;
				if(this.getChallenged().getRenom() > this.getChallenger().getRenom()){
					bonus = (int) Math.round(Settings.COMPETITIVE_DUEL_FACTOR_BONUS*this.getChallenged().getRenom());
				}
				int winpool = (int) Math.round((1-Math.pow(((double)Math.abs(this.getChallenger().getRLevel()-this.getChallenged().getRLevel()))/Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX,Settings.COMPETITIVE_DUEL_FACTOR_POWER))*Settings.COMPETITIVE_DUEL_WIN_RENOM_MAX);
				this.getChallenger().setRenom(this.getChallenger().getRenom()+winpool+bonus+Settings.COMPETITIVE_DUEL_WIN_RENOM_MIN);
				this.getChallenged().setRenom(this.getChallenged().getRenom()-winpool);
				this.getChallenger().sendMessage("§eVotre victoire vous a fait gagner §6" + (this.getChallenger().getRenom()-old_renom1) + " §epoints de renom.");
				this.getChallenged().sendMessage("§eVotre défaite vous a fait perdre §6" + (old_renom2-this.getChallenged().getRenom()) + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenger().getName() + "  §e§o(+" + (this.getChallenger().getRenom()-old_renom1) + ") (" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenger().getName());
			}
			return;
		case TIE:
			RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Égalité !");
			this.getChallenger().sendTitle(("§6§lÉgalité !"), ("§eAucun meurtre en 3 minutes..."), 10, 160, 10);
			this.getChallenged().sendTitle(("§6§lÉgalité !"), ("§eAucun meurtre en 3 minutes..."), 10, 160, 10);
			if(this.isCompetitive()){
				this.getChallenger().setRenom(this.getChallenger().getRenom()+Settings.COMPETITIVE_DUEL_TIE_RENOM);
				this.getChallenged().setRenom(this.getChallenged().getRenom()+Settings.COMPETITIVE_DUEL_TIE_RENOM);
				this.getChallenger().sendMessage("§eEn tant que loyaux guerriers, vous gagnez tous deux §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
				this.getChallenged().sendMessage("§eEn tant que loyaux guerriers, vous gagnez tous deux §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
			}
			return;
		case CHALLENGER_LOSER:
			this.getChallenged().sendTitle(("§2§lVictoire !"), ("§aVous remportez le duel"), 10, 160, 10);
			this.getChallenger().sendTitle(("§4§lDéfaite..."), ("§cVous perdez le duel"), 10, 160, 10);
			if(this.isCompetitive()){
				int bonus = 0;
				if(this.getChallenger().getRenom() > this.getChallenged().getRenom()){
					bonus = (int) Math.round(Settings.COMPETITIVE_DUEL_FACTOR_BONUS*this.getChallenger().getRenom());
				}
				int winpool = (int) Math.round((1-Math.pow(((double)Math.abs(this.getChallenger().getRLevel()-this.getChallenged().getRLevel()))/Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX,Settings.COMPETITIVE_DUEL_FACTOR_POWER))*Settings.COMPETITIVE_DUEL_WIN_RENOM_MAX);
				this.getChallenged().setRenom(this.getChallenged().getRenom()+winpool+bonus+Settings.COMPETITIVE_DUEL_WIN_RENOM_MIN);
				this.getChallenger().setRenom(this.getChallenger().getRenom()-winpool);
				this.getChallenged().sendMessage("§eVotre victoire vous a fait gagner §6" + (this.getChallenged().getRenom()-old_renom2) + " §epoints de renom.");
				this.getChallenger().sendMessage("§eVotre défaite vous a fait perdre §6" + (old_renom1-this.getChallenger().getRenom()) + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenged().getName() + "  §e§o(" + (this.getChallenger().getRenom()-old_renom1) + ") (+" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenged().getName());
			}
			return;
		case CHALLENGED_FORFAIT:
			this.getChallenger().sendTitle(("§6§lForfait !"), ("§eQuel lâche..."), 10, 160, 10);
			if(this.isCompetitive()){
				this.getChallenger().setRenom(this.getChallenger().getRenom()+Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenged().setRenom(this.getChallenged().getRenom()-2*Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenger().sendMessage("§eLe forfait de " + this.getChallenged().getName() + " vous a fait gagner §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenged().getName() + "  §e§o(+" + (this.getChallenger().getRenom()-old_renom1) + ") (" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenged().getName());
			}
			return;
		case CHALLENGER_FORFAIT:
			this.getChallenged().sendTitle(("§6§lForfait !"), ("§eQuel lâche..."), 10, 160, 10);
			if(this.isCompetitive()){
				this.getChallenged().setRenom(this.getChallenged().getRenom()+Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenger().setRenom(this.getChallenger().getRenom()-2*Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenged().sendMessage("§eLe forfait de " + this.getChallenger().getName() + " vous a fait gagner §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenger().getName() + "  §e§o(" + (this.getChallenger().getRenom()-old_renom1) + ") (+" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenger().getName());
			}
			return;
		}
		if(this.isCompetitive()){
			this.getChallenger().getLastCompetitiveDuelDates().put(this.getChallenged().getUniqueId(), System.currentTimeMillis());
			this.getChallenged().getLastCompetitiveDuelDates().put(this.getChallenger().getUniqueId(), System.currentTimeMillis());
		}
	}
	
	public void resetStats(Player player){
		RPlayer rp = RPlayer.get(player);
		if(rp.equals(this.getChallenger())){
			rp.setVigor(this.challengerMana);
			player.setHealth(this.challengerHealth);
			player.setFoodLevel(this.challengerFood);
		}else if(rp.equals(this.getChallenged())){
			rp.setVigor(this.challengedMana);
			player.setHealth(this.challengedHealth);
			player.setFoodLevel(this.challengedFood);
		}
		player.setFireTicks(0);
		if(player.hasMetadata("rangerPoison")) {
			BukkitTask task = BukkitTask.tasks.get(player.getMetadata("rangerPoison").get(0).asInt());
			if(task != null)task.cancel();
		}
	}

	public BukkitTask getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(BukkitTask requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public BukkitTask getDuelTimeout() {
		return duelTimeout;
	}

	public void setDuelTimeout(BukkitTask duelTimeout) {
		this.duelTimeout = duelTimeout;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public BukkitTask getDuelVisualizer() {
		return duelVisualizer;
	}

	public void setDuelVisualizer(BukkitTask duelVisualizer) {
		this.duelVisualizer = duelVisualizer;
	}

	public boolean isCompetitive() {
		return competitive;
	}
}
