package me.pmilon.RubidiaQuests.ui;

import java.util.List;

import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.entity.Player;

public abstract class QuestDialogsEditionMenu extends DialogsEditionMenu {

	private Quest quest;
	private QuestPNJ pnj;
	public QuestDialogsEditionMenu(Player p, QuestPNJ pnj, Quest quest,
			String title, List<String> dialogs) {
		super(p, title, dialogs);
		this.pnj = pnj;
		this.quest = quest;
	}
	public QuestPNJ getPnj() {
		return pnj;
	}
	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}
	public Quest getQuest() {
		return quest;
	}
	public void setQuest(Quest quest) {
		this.quest = quest;
	}

}
