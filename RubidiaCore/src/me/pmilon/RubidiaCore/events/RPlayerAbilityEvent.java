package me.pmilon.RubidiaCore.events;

import org.bukkit.event.Cancellable;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.abilities.Ability;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;

public class RPlayerAbilityEvent extends RPlayerEvent implements Cancellable {

	protected boolean cancelled = false;
	protected Ability ability;
	public RPlayerAbilityEvent(RPlayer rplayer, Ability ability) {
		super(rplayer);
		this.ability = ability;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}

}
