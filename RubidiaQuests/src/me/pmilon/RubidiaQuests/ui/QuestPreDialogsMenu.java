package me.pmilon.RubidiaQuests.ui;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

public class QuestPreDialogsMenu extends QuestDialogsEditionMenu {

	private Quest quest;
	private QuestPNJ pnj;
	public QuestPreDialogsMenu(Player p, Quest quest, QuestPNJ pnj) {
		super(p, pnj, quest, quest.getTitle() + " | Pre Dialogs", quest.getPreDialogs());
		this.quest = quest;
		this.pnj = pnj;
	}

	@Override
	protected void saveDialogs() {
		this.getQuest().setPreDialogs(this.getDialogs());
	}

	@Override
	protected void back(Player player) {
		Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getQuest(), this.getPnj()));
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}

}
