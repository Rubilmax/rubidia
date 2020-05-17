package me.pmilon.RubidiaCore.events;

import org.bukkit.event.Cancellable;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.chat.ChatType;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerEvent;

public class RPlayerPreChatMessageEvent extends RPlayerEvent implements Cancellable {

	protected String message;
	private RPlayer privateTarget;
	private ChatType messageType;
	private boolean cancelled;
	
	public RPlayerPreChatMessageEvent(RPlayer rplayer, RPlayer privateTarget, String message, ChatType messageType) {
		super(rplayer);
		this.message = message;
		this.messageType = messageType;
		this.privateTarget = privateTarget;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public ChatType getMessageType() {
		return messageType;
	}

	public void setMessageType(ChatType messageType) {
		this.messageType = messageType;
	}

	public RPlayer getPrivateTarget() {
		return privateTarget;
	}

	public void setPrivateTarget(RPlayer privateTarget) {
		this.privateTarget = privateTarget;
	}

}
