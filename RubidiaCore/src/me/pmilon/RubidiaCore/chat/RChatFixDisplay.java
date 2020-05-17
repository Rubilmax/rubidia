package me.pmilon.RubidiaCore.chat;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class RChatFixDisplay {

	private final List<WrappedChatComponent> fixDisplays = new ArrayList<WrappedChatComponent>();
	
	private RPlayer rp;
	private int delay;
	private boolean closable = true;
	private final BukkitTask onClose;
	
	public RChatFixDisplay(RPlayer rp, int delay, BukkitTask onClose, String... lines){
		this.rp = rp;
		this.delay = delay;
		this.onClose = onClose;
		for(String line : lines){
			this.addLine(line);
		}
	}

	public List<WrappedChatComponent> getFixDisplays() {
		return fixDisplays;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public RChatFixDisplay addText(TextComponent display){
		this.fixDisplays.add(WrappedChatComponent.fromJson(ComponentSerializer.toString(display)));
		return this;
	}
	
	public RChatFixDisplay addLine(String message){
		for(String line : RChatUtils.filter(message, rp.getChatboxWidth()*4+1,0)){
			this.fixDisplays.add(WrappedChatComponent.fromText(line));
		}
		return this;
	}
	
	public RChatFixDisplay addLines(List<String> displays) {
		for(String display : displays){
			addLine(display);
		}
		return this;
	}
	
	public RChatFixDisplay addLines(String... displays) {
		for(String display : displays){
			addLine(display);
		}
		return this;
	}

	public RPlayer getRP() {
		return rp;
	}

	public void setRP(RPlayer rp) {
		this.rp = rp;
	}

	public boolean isClosable() {
		return closable;
	}

	public RChatFixDisplay setClosable(boolean closable) {
		this.closable = closable;
		return this;
	}

	public BukkitTask getOnClose() {
		return onClose;
	}
}
