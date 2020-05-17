package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class FriendTPUI extends ListMenuUIHandler<Player> {

	private Scroll scroll;
	private boolean cancel = true;
	public FriendTPUI(Player p, Scroll scroll) {
		super(p, "Target players", "Joueurs cibles", 2);
		this.scroll = scroll;
		for(RPlayer rpp : RPlayer.getOnlines()){
			if(rpp.getWouldLikeTeleportation() && !rpp.equals(rp))list.add(rpp.getPlayer());
		}
	}

	@Override
	public String getType() {
		return "FRIEND_TP_MENU";
	}

	@Override
	protected ItemStack getItem(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setDisplayName("§r§6" + player.getName());
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
		skull.setItemMeta(meta);
		return skull;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player p, ItemStack is) {
		Player player = this.get(e.getRawSlot());
		if(player != null){
			TeleportHandler.requestTeleportation(rp, RPlayer.get(player), this.getScroll());
			cancel = false;
			this.close(false);
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(cancel){
			this.getScroll().cancel(this.getHolder());
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.MAP, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Choisissez quelqu'un vers qui se téléporter")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		//nothing to do
	}

	@Override
	protected void onOpen() {
		// nothing
	}

	public Scroll getScroll() {
		return scroll;
	}

	public void setScroll(Scroll scroll) {
		this.scroll = scroll;
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}
}
