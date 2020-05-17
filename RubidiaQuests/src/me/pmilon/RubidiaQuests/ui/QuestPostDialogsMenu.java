package me.pmilon.RubidiaQuests.ui;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

public class QuestPostDialogsMenu extends QuestDialogsEditionMenu {

	public QuestPostDialogsMenu(Player p, Quest quest, QuestPNJ pnj) {
		super(p, pnj, quest, quest.getTitle() + " | Post Dialogs", quest.getPostDialogs());
	}

	@Override
	protected void saveDialogs() {
		this.getQuest().setPostDialogs(this.getDialogs());
	}

	@Override
	protected void back(Player player) {
		Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getQuest(), this.getPnj()));
	}

}
