package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;

import org.bukkit.entity.Player;

public class RPlayerRequestedPlayerTradeEvent extends RPlayerEvent {

	private final Player trading;
	public RPlayerRequestedPlayerTradeEvent(RPlayer rplayer, Player trading) {
		super(rplayer);
		this.trading = trading;
	}
	
	public Player getTrading() {
		return trading;
	}
}
