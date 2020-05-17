package me.pmilon.RubidiaManager.tasks;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

public class NetherRegenTask extends AbstractTask {

	public NetherRegenTask(RubidiaManagerPlugin plugin){
		super(plugin);
	}
	
	@Override
	public void runTaskSynchronously() {
		RPlayer.broadcastMessage("�4R�G�N�RATION DES ENFERS...");
		RPlayer.broadcastTitle("�4R�G�N�RATION DES ENFERS...","",0,100,40);
		Core.console.sendMessage("�eRegenerating nether...");
		if(RubidiaManagerPlugin.multiverseCore.getCore().getMVWorldManager().regenWorld("Rubidia_nether", true, true, null)) {
			RPlayer.broadcastMessage("�2R�G�N�RATION TERMIN�E");
			RPlayer.broadcastTitle("�2R�G�N�RATION TERMIN�E","",0,100,40);
			Core.console.sendMessage("�eNether regenerated!");
		} else Core.console.sendMessage("�cUnable to regen nether.");
	}

}