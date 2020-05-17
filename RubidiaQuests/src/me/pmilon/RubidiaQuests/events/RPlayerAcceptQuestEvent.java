package me.pmilon.RubidiaQuests.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.quests.Quest;

public class RPlayerAcceptQuestEvent extends RPlayerQuestEvent {

	private String handlerUUID;
	public RPlayerAcceptQuestEvent(RPlayer rplayer, Quest quest, String handlerUUID) {
		super(rplayer, quest);
		this.handlerUUID = handlerUUID;
	}
	
	public String getHandlerUUID() {
		return handlerUUID;
	}
	
	public void setHandlerUUID(String handlerUUID) {
		this.handlerUUID = handlerUUID;
	}

}
