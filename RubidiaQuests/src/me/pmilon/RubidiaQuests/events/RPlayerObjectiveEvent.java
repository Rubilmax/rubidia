package me.pmilon.RubidiaQuests.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;

public class RPlayerObjectiveEvent extends RPlayerQuestEvent {

	private Objective objective;
	public RPlayerObjectiveEvent(RPlayer rplayer, Quest quest, Objective objective) {
		super(rplayer, quest);
		this.objective = objective;
	}
	
	public Objective getObjective() {
		return objective;
	}
	
	public void setObjective(Objective objective) {
		this.objective = objective;
	}

}
