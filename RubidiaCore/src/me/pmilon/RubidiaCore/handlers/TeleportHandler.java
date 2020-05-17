package me.pmilon.RubidiaCore.handlers;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RPlayerMoveEvent;
import me.pmilon.RubidiaCore.events.RTeleportEvent;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.ScrollType;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaPets.pets.Pet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportHandler implements Listener{

	public static HashMap<RPlayer, Integer> invoke_tasks = new HashMap<RPlayer, Integer>();
	public static HashMap<RPlayer, Integer> tp_tasks = new HashMap<RPlayer, Integer>();
	public static HashMap<RPlayer, Integer> teleportationtask = new HashMap<RPlayer, Integer>();
	
	static Core plugin;
	public TeleportHandler(Core core){
		plugin = core;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTeleport(final PlayerTeleportEvent e){
		final Player p = e.getPlayer();
		RPlayer rp = RPlayer.get(p);
		Location to = e.getTo();
		if(to.getWorld().getEnvironment().equals(Environment.THE_END)){
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, true, false));
			rp.sendTitle(("§7Le Néant"), "", 5, 30, 20);
		}
		
		if(!e.getFrom().getWorld().equals(to.getWorld())){
			if(p.getGameMode().equals(GameMode.CREATIVE)){
				new BukkitTask(TeleportHandler.plugin){

					@Override
					public void run() {
						p.setGameMode(GameMode.CREATIVE);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(8);
			}
		}
		
		if(!p.isInsideVehicle()){//avoid infinite loop while player mounts a horse (because mount == teleport)
			for(Pet pet : rp.getPets()){
				if(pet.isActive()){
					if(pet.getEntity() != null){
						if(pet.getEntity().getWorld().equals(p.getWorld())){
							if(pet.getEntity().getLocation().distanceSquared(p.getLocation()) > 4*4){
								teleport(pet.getEntity(), e.getTo());
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(RPlayerMoveEvent e){
		RPlayer rp = e.getRPlayer();
		if(teleportationtask.containsKey(rp)){
			BukkitTask.tasks.get(teleportationtask.get(rp)).cancel();
			rp.sendMessage("§cVous avez bougé ! Téléportation annulée !");
		}
	}
	
	public static void startTeleportation(final RPlayer rp, final Location location, final RTeleportCause cause){
		rp.sendMessage("§eNe bougez pas, ou la téléportation sera annulée.");
		teleportationtask.put(rp, new BukkitTask(Core.instance){

			@Override
			public void run() {
				if(rp.isOnline()) {
					teleportationtask.remove(rp);
					RTeleportEvent event = new RTeleportEvent(rp, rp.getPlayer().getLocation(), location, cause);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						TeleportHandler.teleport(rp.getPlayer(), event.getTo());
					}else this.cancel();
				}
			}

			@Override
			public void onCancel() {
				Scroll scroll = cause.getScroll();
				if(scroll != null){
					RPlayer teleporter = cause.getTeleporter();
					RPlayer teleported = cause.getTeleported();
					if(teleporter != null){
						if(scroll.getType().equals(ScrollType.FRDCALL)){
							scroll.cancel(teleporter.getPlayer());
							teleporter.sendMessage("§4" + teleported.getName() + "§c a bougé avant la fin de l'invocation !");
						}else if(scroll.getType().equals(ScrollType.FRDTP)){
							scroll.cancel(teleported.getPlayer());
							teleported.sendMessage("§4" + teleporter.getName() + "§c a bougé avant la fin de la téléportation !");
						}
					}else scroll.cancel(rp.getPlayer());
				}
				teleportationtask.remove(rp);
			}
			
		}.runTaskLater(rp.isOp() ? 0 : 65).getTaskId());
	}
	
	public static void teleport(final Entity e, Location location) {
		if(e != null){
	    	e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,e.getHeight()/2.,0), 50, .5, .5, .5);
			handleVehicleTeleportation(e, location);
			e.teleport(location);
	    	e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,e.getHeight()/2.,0), 50, .5, .5, .5);
		}
	}

	public static void handleVehicleTeleportation(Entity e, Location location){
		if(e != null){
			Entity vehicle = e;
			Entity passenger = e;
			for(int i = e.getPassengers().size();i > 0;i--){
				passenger = e;
				while(!passenger.getPassengers().isEmpty()){
					passenger = passenger.getPassengers().get(0);
				}
				vehicle = passenger.getVehicle();
				passenger.eject();
				passenger.teleport(location);
				final Entity v = vehicle;
				final Entity p = passenger;
				new BukkitTask(Core.instance){
					public void run(){
						v.addPassenger(p);
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(i);
			}
		}
	}
	
	public static void requestInvocation(final RPlayer invoked, final RPlayer invocator, final Scroll scroll) {
		TeleportHandler.invoke_tasks.put(invoked, new BukkitTask(Core.instance){
			@Override
			public void run(){
				invocator.sendMessage("§4" + invoked.getName() + " §cn'a pas répondu à votre invocation.");
				TeleportHandler.invoke_tasks.remove(invoked);
			}

			@Override
			public void onCancel() {
				TeleportHandler.startTeleportation(invoked, invocator.getPlayer().getLocation(), new RTeleportCause(RTeleportType.FRIEND_INVOCATION,scroll,invocator,invoked));
				TeleportHandler.invoke_tasks.remove(invoked);
			}
		}.runTaskLater(15*20).getTaskId());
		invocator.sendMessage("§eInvocation...");
		invoked.sendMessage("§6" + invocator.getName() + " §evous a invoqué ! Tapez §6/tp §epour accepter : vous avez §615 §esecondes.");
	}
	
	public static void requestTeleportation(final RPlayer teleported, final RPlayer teleporter, final Scroll scroll) {
		TeleportHandler.tp_tasks.put(teleporter, new BukkitTask(Core.instance){
			@Override
			public void run(){
				teleported.sendMessage("§4" + teleporter.getName() + " §cn'a pas répondu à votre demande de téléportation.");
				TeleportHandler.tp_tasks.remove(teleporter);
			}

			@Override
			public void onCancel() {
				TeleportHandler.startTeleportation(teleported, teleporter.getPlayer().getLocation(), new RTeleportCause(RTeleportType.FRIEND_TELEPORTATION, scroll,teleporter,teleported));
				TeleportHandler.tp_tasks.remove(teleporter);
			}
		}.runTaskLater(15*20).getTaskId());
		teleported.sendMessage("§eTéléportation...");
		teleporter.sendMessage("§6" + teleported.getName() + " §ea invoqué les puissances divines pour se téléporter vers vous ! Tapez §6/tp §epour accepter : vous avez §615 §esecondes.");
	}
}
