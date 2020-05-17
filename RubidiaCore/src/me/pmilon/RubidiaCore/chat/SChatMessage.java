package me.pmilon.RubidiaCore.chat;

import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.RManager.RPlayer;

public class SChatMessage {

	private RPlayer sender;
	private String fullMessage;
	private ItemStack item;
	public SChatMessage(RPlayer sender, String fullMessage, ItemStack item){
		this.sender = sender;
		this.fullMessage = fullMessage;
		this.item = item;
	}
	
	public RPlayer getSender() {
		return sender;
	}
	
	public void setSender(RPlayer sender) {
		this.sender = sender;
	}

	public String getFullMessage() {
		return fullMessage;
	}

	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
	
}
