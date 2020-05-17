package me.pmilon.RubidiaCore.scrolls;


import org.bukkit.entity.Player;

public abstract class ScrollTask implements Runnable{

	private Scroll scroll;
	private Player player;
	private boolean used = true;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Scroll getScroll() {
		return scroll;
	}

	public void setScroll(Scroll scroll) {
		this.scroll = scroll;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
