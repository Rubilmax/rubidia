package me.pmilon.RubidiaCore.events;

import org.bukkit.event.Cancellable;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;

public class RPlayerUseWeaponEvent extends RPlayerEvent implements Cancellable{

	private Weapon weapon;
	private boolean cancelled;
	public RPlayerUseWeaponEvent(RPlayer rplayer, Weapon weapon) {
		super(rplayer);
		this.weapon = weapon;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
