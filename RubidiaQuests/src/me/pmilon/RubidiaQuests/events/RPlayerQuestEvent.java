package me.pmilon.RubidiaQuests.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RPlayerQuestEvent extends RPlayerEvent implements Cancellable {

	protected Quest quest;
	protected boolean cancelled;
	protected RPlayerQuestEvent(RPlayer rplayer, Quest quest){
		super(rplayer);
		this.quest = quest;
	}
	
	public Quest getQuest(){
		return quest;
	}
	
	public void setQuest(Quest quest){
		this.quest = quest;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
