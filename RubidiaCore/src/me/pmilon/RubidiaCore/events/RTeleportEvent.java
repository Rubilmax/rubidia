package me.pmilon.RubidiaCore.events;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.abstracts.RPlayerCancellableEvent;
import me.pmilon.RubidiaCore.scrolls.Scroll;

import org.bukkit.Location;

public class RTeleportEvent extends RPlayerCancellableEvent {

	public static class RTeleportCause {

		public static enum RTeleportType {
			DELAYED_TELEPORTATION,
			FRIEND_INVOCATION,
			CITY_TELEPORTATION,
			WILD_TELEPORTATION,
			FRIEND_TELEPORTATION,
			GUILD_HOME,
			RAID_CENTER;
		}
		
		private RTeleportType type;
		private Scroll scroll;
		private RPlayer teleporter;
		private RPlayer teleported;
		public RTeleportCause(RTeleportType type, Scroll scroll, RPlayer teleporter, RPlayer teleported){
			this.type = type;
			this.scroll = scroll;
			this.teleporter = teleporter;
			this.teleported = teleported;
		}
		
		public RTeleportType getType() {
			return type;
		}
		
		public void setType(RTeleportType type) {
			this.type = type;
		}

		public Scroll getScroll() {
			return scroll;
		}

		public void setScroll(Scroll scroll) {
			this.scroll = scroll;
		}

		public RPlayer getTeleporter() {
			return teleporter;
		}

		public void setTeleporter(RPlayer teleporter) {
			this.teleporter = teleporter;
		}

		public RPlayer getTeleported() {
			return teleported;
		}

		public void setTeleported(RPlayer teleported) {
			this.teleported = teleported;
		}
		
	}
	
	private Location from;
	private Location to;
	private RTeleportCause cause;
	public RTeleportEvent(RPlayer rp, Location from, Location to, RTeleportCause cause){
		super(rp);
		this.from = from;
		this.to = to;
		this.cause = cause;
	}

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		this.to = to;
	}

	public RTeleportCause getCause() {
		return cause;
	}

	public void setCause(RTeleportCause cause) {
		this.cause = cause;
	}
	
}
