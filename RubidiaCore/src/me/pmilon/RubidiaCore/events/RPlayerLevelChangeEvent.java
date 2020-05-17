package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerCancellableEvent;

public class RPlayerLevelChangeEvent extends RPlayerCancellableEvent{

	private int oldRlevel;
	private int newRlevel;
	private RXPSource source;
	
	public RPlayerLevelChangeEvent(RPlayer rplayer, int oldLevel, int newLevel, RXPSource source){
		super(rplayer);
		this.oldRlevel = oldLevel;
		this.newRlevel = newLevel;
		this.source = source;
	}
	
	public int getOldRLevel(){
		return this.oldRlevel;
	}
	
	public int getNewRLevel(){
		return this.newRlevel;
	}
	
	public void setNewRLevel(int level){
		this.newRlevel= level;
	}
	
	public RXPSource getSource(){
		return this.source;
	}
	
	public void setSource(RXPSource source){
		this.source = source;
	}
}
