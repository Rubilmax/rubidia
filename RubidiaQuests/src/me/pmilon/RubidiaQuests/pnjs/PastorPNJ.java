package me.pmilon.RubidiaQuests.pnjs;

import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.chat.RChatFixDisplay;
import me.pmilon.RubidiaCore.RManager.Gender;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.utils.Configs;
import me.pmilon.RubidiaQuests.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PastorPNJ extends PNJHandler {

	private Location location1;
	private Location location2;
	
	private boolean taken = false;
	public PastorPNJ(String uuid, String name, Location loc, int age, boolean fix, Location l1, Location l2) {
		super(uuid, "PASTEUR", "§5§l", name, "§d", PNJType.PASTOR, loc, age, fix);
		this.location1 = l1;
		this.location2 = l2;
	}

	@Override
	protected void onRightClick(PlayerInteractEntityEvent e, final Player p, final Villager villager) {
		final RPlayer rp = RPlayer.get(p);
		if(rp != null){
			if(rp.fiance != null){
				final RPlayer rpp = rp.fiance;
				if(rpp.fiance == rp){
					if(rpp.isOnline()){
						final Player pp = rpp.getPlayer();
						if(pp.getWorld().equals(villager.getWorld())){
							if(pp.getLocation().distanceSquared(villager.getLocation()) <= Math.pow(8, 2)){
								if(!this.isTaken()){
									this.setTaken(true);
									final PastorPNJ instance = this;
									
									final List<String> speech = Arrays.asList("Mesdames, Mesdemoiselles, Messieurs, nous célébrons aujourd'hui l'union de deux êtres chers au royaume.", "§e" + rp.getName() + " §aet §e" + rpp.getName() + "§a, deux courageux guerriers se battant jours et nuits pour la sécurité de tous.", "Ils ont déjà fait beaucoup de chemin séparés.", "Mais dès aujourd'hui, ils avanceront §lensemble§a, jusqu'à ce que la mort les sépare.", "%rapidDès aujourd'hui, nous", "%hitJe ne vais quand même pas lire tout ça...", "%hit...", "Passons.");
									
									p.teleport(instance.getLocation1());
									new PNJDialog(p, this, villager, Arrays.asList("%player ! Enchanté.", "J'ai entendu parler de vos récentes fiançailles.", "Vous venez donc ici dans le but de vous marier ?", "Bien. Tout est prêt, nous pouvons commencer la cérémonie..."), DialogType.YES, new Runnable(){

										@Override
										public void run() {
											new BukkitTask(QuestsPlugin.instance){

												@Override
												public void run() {
													PNJDialog dialog1 = new PNJDialog(p, instance, villager, speech, DialogType.AMBIENT, new Runnable(){

														@Override
														public void run() {
															p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
															Utils.sendQuestion(rp, "Souhaitez-vous prendre pour épou" + (rpp.getSex().equals(Gender.MALE) ? "x" : (rpp.getSex().equals(Gender.FEMALE) ? "se" : "x(se)")) + " " + rpp.getName() + " ?",
																	"/marry " + uuid + " yes",
																	"/marry " + uuid + " no");
														}
														
													}, true, false);
													dialog1.setTarget(pp.getEyeLocation());
													dialog1.start();
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskLater(20);
										}
										
									}, true, true).start();

									pp.teleport(instance.getLocation2());
									new PNJDialog(pp, this, villager, Arrays.asList("%player ! Enchanté.", "J'ai entendu parler de vos récentes fiançailles.", "Vous venez donc ici dans le but de vous marier ?", "Bien. Tout est prêt, nous pouvons commencer la cérémonie..."), DialogType.YES, new Runnable(){

										@Override
										public void run() {
											new BukkitTask(QuestsPlugin.instance){

												@Override
												public void run() {
													for(Player player : Bukkit.getOnlinePlayers()){
														if(!player.equals(p) && !player.equals(pp) && player.getLocation().distanceSquared(villager.getLocation()) <= 64){
															PNJDialog dialog = new PNJDialog(player, instance, villager, speech, DialogType.AMBIENT, null, false, false);
															dialog.start();
														}
													}
													
													PNJDialog dialog2 = new PNJDialog(pp, instance, villager, speech, DialogType.AMBIENT, new Runnable(){

														@Override
														public void run() {
															pp.playSound(pp.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
														    TextComponent accept = new TextComponent(("§a[ ✔  Accepter]"));
														    TextComponent refuse = new TextComponent(("§c[ ✘  Refuser]"));
														    ClickEvent acceptEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/marry " + uuid + " yes");
														    ClickEvent refuseEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/marry " + uuid + " no");
														    accept.setClickEvent(acceptEvent);
														    refuse.setClickEvent(refuseEvent);
														    TextComponent text = new TextComponent("        ");
														    text.addExtra(accept);
														    text.addExtra(new TextComponent("     "));
														    text.addExtra(refuse);
														    rpp.getChat().addFixDisplay(new RChatFixDisplay(rpp,-1,null).setClosable(false).addLines("",("   §e> Souhaitez-vous prendre pour épou" + (rp.getSex().equals(Gender.MALE) ? "x" : (rp.getSex().equals(Gender.FEMALE) ? "se" : "x(se)")) + " " + rp.getName() + " ?")).addText(text).addLine(""));
														}
														
													}, true, false);
													dialog2.setTarget(p.getEyeLocation());
													dialog2.start();
												}

												@Override
												public void onCancel() {
												}
												
											}.runTaskLater(20);
										}
										
									}, false, true).start();
									return;
								}else{
									new PNJDialog(p, this, villager, Arrays.asList("Silence, nous sommes en pleine cérémonie de mariage !"), DialogType.AMBIENT, new Runnable(){

										@Override
										public void run() {
										}
										
									}, false, true).start();
									return;
								}
							}
						}
					}
					
					new PNJDialog(p, this, villager, Arrays.asList("La présence de %companion est nécessaire !"), DialogType.AMBIENT, new Runnable(){

						@Override
						public void run() {
						}
						
					}, false, true).start();
				}else{
					new PNJDialog(p, this, villager, Arrays.asList("Je n'aime pas briser un coeur, mais...", "%companion n'a pas encore accepté votre demande en mariage."), DialogType.AMBIENT, new Runnable(){

						@Override
						public void run() {
						}
						
					}, false, true).start();
				}
			}else{
				new PNJDialog(p, this, villager, Arrays.asList("Je suis ici pour réaliser le mariage de vos rêves.", "Revenez me voir lorsque vous serez fiancé à quelqu'un."), DialogType.AMBIENT, new Runnable(){

					@Override
					public void run() {
					}
					
				}, false, true).start();
			}
		}
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

	@Override
	protected void onSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".location1", this.getLocation1());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".location2", this.getLocation2());
	}

	@Override
	protected void onDelete() {
	}

	public Location getLocation1() {
		return location1.clone();
	}

	public void setLocation1(Location location1) {
		this.location1 = location1;
	}

	public Location getLocation2() {
		return location2.clone();
	}

	public void setLocation2(Location location2) {
		this.location2 = location2;
	}

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

}
