package me.pmilon.RubidiaCore;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import me.pmilon.RubidiaCore.packets.WrapperPlayServerSetSlot;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemMessage {
	
	//TODO delete all this shite
	
	private static final int interval = 20;  // ticks
	private static final int DEFAULT_DURATION = 2; // seconds

	private static final String DEF_FORMAT_1 = "%s";
	private static final String DEF_FORMAT_2 = " %s ";
	private static final String[] formats = new String[] { DEF_FORMAT_1, DEF_FORMAT_2 };
	private static final Material emptyHandReplacement = Material.LIGHT_GRAY_STAINED_GLASS_PANE;

	private static final HashMap<Player, NamerTask> namerTasks = new HashMap<Player, NamerTask>();
	
	public static void sendMessage(Player player, String message) {
		sendMessage(player, message, DEFAULT_DURATION);
	}

	public static void sendMessage(Player player, String message, int duration) {
		if (player.getGameMode() == GameMode.CREATIVE) {
			// TODO: this doesn't work properly in creative mode.  Need to investigate further
			// if it can be made to work, but for now, just send an old-fashioned chat message.
			//player.sendMessage(message);
		} else {
			if(namerTasks.containsKey(player)){
				NamerTask task = namerTasks.get(player);
				task.setMessage(message);
				task.setIterations(Math.max(1, duration / interval));
				task.refresh(player);
			}else{
				namerTasks.put(player, new NamerTask(player, message, duration));
				namerTasks.get(player).runTaskTimer(Core.instance, 1L, interval);
			}
		}
	}

	private static class NamerTask extends BukkitRunnable implements Listener	{
		private final WeakReference<Player> playerRef;
		private String message;
		private int slot;
		private int iterations;

		public NamerTask(Player player, String message, int duration) {
			this.playerRef = new WeakReference<Player>(player);
			this.iterations = Math.max(1, duration / interval);
			this.slot = player.getInventory().getHeldItemSlot();
			this.message = message;
			Bukkit.getPluginManager().registerEvents(this, Core.instance);
		}

		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void onItemHeldChange(PlayerItemHeldEvent event) {
			Player player = event.getPlayer();
			if (player.equals(playerRef.get())) {
				sendItemSlotChange(player, event.getPreviousSlot(), player.getInventory().getItem(event.getPreviousSlot()));
				slot = event.getNewSlot();
				refresh(event.getPlayer());
			}
		}

		@EventHandler
		public void onPluginDisable(PluginDisableEvent event) {
			Player player = playerRef.get();
			if (event.getPlugin() == Core.instance && player != null) {
				finish(playerRef.get());
			}
		}

		@Override
		public void run() {
			Player player = playerRef.get();
			if (player != null) {
				if (iterations-- <= 0) {
					// finished - restore the previous item data and tidy up
					finish(player);
				} else {
					// refresh the item data
					refresh(player);
				}
			} else {
				// player probably disconnected - whatever, we're done here
				cleanup();
			}
		}

		private void refresh(Player player) {
			sendItemSlotChange(player, slot, makeStack(player));
		}

		private void finish(Player player) {
			sendItemSlotChange(player, slot, player.getInventory().getItem(slot));
			cleanup();
		}

		private void cleanup() {
			cancel();
			HandlerList.unregisterAll(this);
		}

		private ItemStack makeStack(Player player) {
			ItemStack stack0 = player.getInventory().getItem(slot);
			ItemStack stack;
			if (stack0 == null || stack0.getType() == Material.AIR) {
				// an empty slot can't display any custom item name, so we need to fake an item
				// a snow layer is a good choice, since it's visually quite unobtrusive
				stack = new ItemStack(emptyHandReplacement, 1);
			} else {
				stack = new ItemStack(stack0.getType(), stack0.getAmount());
				stack.setData(stack0.getData());
			}
			ItemMeta meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
			// fool the client into thinking the item name has changed, so it actually (re)displays it
			meta.setDisplayName(String.format(formats[iterations % formats.length], message));
			stack.setItemMeta(meta);
			return stack;
		}

		private void sendItemSlotChange(Player player, int slot, ItemStack stack) {
			WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot();
			// int field 0: window id (0 = player inventory)
			// int field 1: slot number (36 - 44 for player hotbar)
			packet.setWindowId(0);
			packet.setSlot(slot + 36);
			packet.setSlotData(stack);
			packet.sendPacket(player);
		}
		
		public void setMessage(String message){
			this.message = message;
		}
		
		public void setIterations(int iterations){
			this.iterations = iterations;
		}
	}

}