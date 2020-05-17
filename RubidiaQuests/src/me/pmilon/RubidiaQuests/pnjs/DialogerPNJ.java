package me.pmilon.RubidiaQuests.pnjs;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.utils.Configs;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class DialogerPNJ extends PNJHandler {

	private List<String> dialogs;
	private final boolean keepDialogOnEnd;
	public DialogerPNJ(String uuid, String title, String titlePrefix,
			String name, String namePrefix, PNJType type, Location loc,
			int age, boolean fix, List<String> dialogs, boolean keepDialogOnEnd) {
		super(uuid, title, titlePrefix, name, namePrefix, type, loc, age, fix);
		this.dialogs = dialogs == null ? new ArrayList<String>() : dialogs;
		this.keepDialogOnEnd = keepDialogOnEnd;
	}

	@Override
	protected void onRightClick(PlayerInteractEntityEvent e, final Player p, Villager villager) {
		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
		PNJDialog dialog = new PNJDialog(p, this, villager, this.getDialogs(), DialogType.AMBIENT, new Runnable(){
			
			public void run(){
				onTalk(p);
			}
			
		}, this.keepDialogOnEnd, true);
		dialog.start();
	}

	@Override
	protected void onSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".dialogs", this.getDialogs());
		onSubSave();
	}

	protected abstract void onDelete();
	
	protected abstract void onTalk(Player player);
	
	protected abstract void onSubSave();

	public List<String> getDialogs() {
		return dialogs;
	}

	public void setDialogs(List<String> dialogs) {
		this.dialogs = dialogs;
	}

	public boolean isKeepDialogOnEnd() {
		return keepDialogOnEnd;
	}

}
