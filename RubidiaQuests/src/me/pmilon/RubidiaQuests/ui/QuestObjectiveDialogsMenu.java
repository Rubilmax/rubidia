package me.pmilon.RubidiaQuests.ui;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.entity.Player;

public class QuestObjectiveDialogsMenu extends QuestDialogsEditionMenu {

	private Objective objective;
	public QuestObjectiveDialogsMenu(Player p, QuestPNJ pnj, Quest quest,
			Objective objective) {
		super(p, pnj, quest, quest.getTitle() + " | Objective " + objective.getIndex(), objective.getDialogs());
		this.objective = objective;
	}

	@Override
	protected void saveDialogs() {
		this.getObjective().setDialogs(this.getDialogs());
	}

	@Override
	protected void back(Player player) {
		Core.uiManager.requestUI(new ObjectiveEditionMenu(player, this.getQuest(), this.getPnj(), this.getObjective()));
	}

	public Objective getObjective() {
		return objective;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

}
