package me.pmilon.RubidiaQuests.ui;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.entity.Player;

public class QuestNonDialogsMenu extends QuestDialogsEditionMenu {

	public QuestNonDialogsMenu(Player p, QuestPNJ pnj, Quest quest) {
		super(p, pnj, quest, quest.getTitle() + " | Non Dialogs", quest.getNonDialogs());
	}

	@Override
	protected void saveDialogs() {
		this.getQuest().setNonDialogs(this.getDialogs());
	}

	@Override
	protected void back(Player player) {
		Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getQuest(), this.getPnj()));
	}
}
