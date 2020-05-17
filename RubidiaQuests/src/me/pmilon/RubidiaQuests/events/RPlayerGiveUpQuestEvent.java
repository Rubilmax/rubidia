package me.pmilon.RubidiaQuests.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.quests.Quest;

public class RPlayerGiveUpQuestEvent extends RPlayerQuestEvent{

	public RPlayerGiveUpQuestEvent(RPlayer rplayer, Quest quest) {
		super(rplayer, quest);
	}

}
