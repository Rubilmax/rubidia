package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GMembersUI extends ListMenuUIHandler<GMember> {

	private Guild guild;
	public GMembersUI(Player p, Guild guild) {
		super(p, "Guild Members", "Membres de la guilde", 2);
		this.guild = guild;
		for(GMember membr : this.getGuild().getMembers()){
			this.list.add(membr);
		}
	}

	@Override
	protected ItemStack getItem(GMember membr) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setDisplayName("§r§6" + membr.getName());
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(membr.getUniqueId())));
		skull.setItemMeta(meta);
		return skull;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack is) {
		GMember member = this.get(e.getRawSlot());
		if(member != null) {
			this.getUIManager().requestUI(new GMemberPrefsUI(this.getHolder(), this.getGuild(), member, 1));
		}
	}

	@Override
	public String getType() {
		return "GUILD_MEMBERS_LIST_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.MAP, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Choisissez un membre dont vous souhaitez éditer les permissions"), ("§7ou cliquez pour revenir au menu précédent")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		this.getUIManager().requestUI(new GMenuUI(this.getHolder(), this.getGuild()));
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	@Override
	protected void onOpen() {
		// nothing
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}

}
