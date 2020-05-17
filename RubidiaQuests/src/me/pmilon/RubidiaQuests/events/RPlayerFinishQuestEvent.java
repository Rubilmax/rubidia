package me.pmilon.RubidiaQuests.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.quests.Quest;

public class RPlayerFinishQuestEvent extends RPlayerQuestEvent{

	public RPlayerFinishQuestEvent(RPlayer rplayer, Quest quest) {
		super(rplayer, quest);
	}

}
