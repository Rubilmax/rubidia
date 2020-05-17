package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

public class QuestDeletionConfirmationUI extends ConfirmationUI {

	private Quest quest;
	private QuestPNJ pnj;
	public QuestDeletionConfirmationUI(RPlayer rp, Quest quest, QuestPNJ pnj) {
		super(rp.getPlayer(), ("Suppression de qu�te"), new String[]{("�aSupprimer la qu�te"), ("�cConserver la qu�te")},
				quest.getColoredTitle(), Arrays.asList(quest.getColoredSubtitle(), ("�8�tes-vous s�r de vouloir supprimer cette qu�te ?")));
		this.quest = quest;
		this.pnj = pnj;
	}
	
	@Override
	protected void no() {
		Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getQuest(), this.getPnj()));
	}
	
	@Override
	protected void yes() {
		this.getQuest().delete();
		Core.uiManager.requestUI(new PNJQuests(this.getHolder(), this.getPnj()));
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
