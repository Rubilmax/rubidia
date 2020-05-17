package me.pmilon.RubidiaQuests.pnjs;

public class PNJSession {

	private final String identifier;
	private final PNJHandler pnjHandler;
	
	public PNJSession(PNJHandler pnj){
		this.identifier = pnj.getName();
		this.pnjHandler = pnj;
	}
	
	public String getIdentifier(){
		return this.identifier;
	}
	
	public PNJHandler getPNJHandler(){
		return this.pnjHandler;
	}
	
}
