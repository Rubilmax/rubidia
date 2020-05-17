package me.pmilon.RubidiaQuests.dialogs;

import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PNJDialog {

	private Player player;
	private RPlayer rp;
	private PNJHandler pnj;
	private Villager talker;
	private List<String> dialogs;
	private DialogType type;
	private Runnable dialogEnd;
	private boolean keepDialogOnEnd;
	private BukkitTask[] dialogTasks;
	private int dialogIndex;
	private boolean blind;
	
	public BukkitTask tpTask;
	private Location target = null;
	
	public PNJDialog(Player player, PNJHandler pnj, Villager talker, List<String> dialogs, DialogType type, Runnable dialogEnd, boolean keepDialogOnEnd, boolean blind){
		this.player = player;
		this.rp = RPlayer.get(player);
		this.pnj = pnj;
		this.talker = talker;
		this.dialogs = dialogs;
		this.type = type;
		this.dialogEnd = dialogEnd;
		this.keepDialogOnEnd = keepDialogOnEnd;
		this.dialogTasks = new BukkitTask[dialogs.size()+1];
		this.dialogIndex = -1;
		this.blind = blind;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PNJHandler getPnj() {
		return pnj;
	}

	public void setPnj(PNJHandler pnj) {
		this.pnj = pnj;
	}

	public List<String> getDialogs() {
		return dialogs;
	}

	public void setDialogs(List<String> dialogs) {
		this.dialogs = dialogs;
	}

	public DialogType getType() {
		return type;
	}

	public void setType(DialogType type) {
		this.type = type;
	}

	public Runnable getDialogEnd() {
		return dialogEnd;
	}

	public void setDialogEnd(Runnable dialogEnd) {
		this.dialogEnd = dialogEnd;
	}

	public boolean isKeepDialogOnEnd() {
		return keepDialogOnEnd;
	}

	public void setKeepDialogOnEnd(boolean keepDialogOnEnd) {
		this.keepDialogOnEnd = keepDialogOnEnd;
	}
	
	public int getDialogIndex(){
		return dialogIndex;
	}
	
	public void setDialogIndex(int index){
		this.dialogIndex = index;
	}
	
	public void start(){
		DialogManager.setInDialog(getPlayer(), this);
		int relativeTicks = 0;
		for(int i = dialogIndex+1;i < this.getDialogs().size();i++){
			final int index = i;
			final String dialog = this.getDialogs().get(i);
			boolean rapid = dialog.contains("%rapid");
			dialogTasks[i] = new BukkitTask(QuestsPlugin.instance){
				public void run(){
					setDialogIndex(index);
					getRP().getChat().addPNJMessage(MessageManager.pnjDialog(getPnj(), getType(), getPlayer(), getPnj().getName(), dialog.replaceAll("%rapid", "")));
					getRP().getChat().update();
				}

				@Override
				public void onCancel() {
				}
			}.runTaskLater(relativeTicks);
			if(rapid)relativeTicks += 9+dialog.length()*.21;
			else relativeTicks += 18+dialog.length()*.46;
		}
		tpTask = new BukkitTask(QuestsPlugin.instance){

			@Override
			public void run() {
				if(getTalker() != null){
					Location target = getTarget();
					Location destination = target != null ? target.clone() : getTalker().getLocation().clone().add(0, getTalker().getHeight() * .9, 0);
					getPlayer().teleport(getPlayer().getLocation().clone().setDirection(destination.subtract(getPlayer().getEyeLocation()).toVector()));
				}
				if(isBlind()){
					if(!getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS)){
						getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999999, 444, true, false));
					}
				}else if(getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS)){
					getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0, 0);
		dialogTasks[this.getDialogs().size()] = new BukkitTask(QuestsPlugin.instance){
			public void run(){
				dialogIndex = getDialogs().size();
				if(getDialogEnd() != null)getDialogEnd().run();
				if(!isKeepDialogOnEnd()){
					tpTask.cancel();
					if(getRP().getChat() != null){
						getRP().getChat().clearPNJMessages();
					}
					DialogManager.setNoDialog(getPlayer());
				}
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(relativeTicks);
	}
	
	public void stepNext(){
		if(dialogIndex != dialogs.size()){
			for(BukkitTask task : dialogTasks){
				task.cancel();
			}
			tpTask.cancel();
			this.start();
		}
	}

	public Villager getTalker() {
		return talker;
	}

	public void setTalker(Villager talker) {
		this.talker = talker;
	}

	public Location getTarget() {
		return target;
	}

	public void setTarget(Location target) {
		this.target = target;
	}

	public boolean isBlind() {
		return blind;
	}

	public void setBlind(boolean blind) {
		this.blind = blind;
	}

	public RPlayer getRP() {
		return rp;
	}

	public void setRp(RPlayer rp) {
		this.rp = rp;
	}
	
}
