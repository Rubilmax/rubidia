package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.PermissionsHolder;
import me.pmilon.RubidiaGuilds.guilds.Rank;

public class HomePermissionsUI extends UIHandler {

	private int SLOT_BACK = 17;
	private int SLOT_HEAD = 8;
	
	private Guild guild;
	private PermissionsHolder subject;
	public int page;
	public HomePermissionsUI(Player p, Guild guild, PermissionsHolder subject, int page) {
		super(p);
		this.guild = guild;
		this.subject = subject;
		this.page = page;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getName() + " : " + ("Permissions de PR"), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_HOME_PERMISSIONS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK){
			if(this.isGMember())Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), this.getGuild(), (GMember)this.getSubject(), this.page));
			else Core.uiManager.requestUI(new GRankPrefsUI(this.getHolder(), this.getGuild(), (Rank)this.getSubject(), this.page));
		}else if(slot != this.SLOT_HEAD){
			if(slot < 9){
				this.getSubject().setCanHome(slot, !this.getSubject().canHome(slot));
				this.getMenu().setItem(slot, this.getCanHome(slot));
			}else{
				this.getSubject().setCanSetHome(slot-9, !this.getSubject().canSetHome(slot-9));
				this.getMenu().setItem(slot, this.getCanSetHome(slot-9));
			}
			if(!this.isGMember())((Rank)this.getSubject()).resetPermissions(this.getGuild());
			Utils.updateInventory(this.getHolder());
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_BACK, this.getBack());
		this.getMenu().setItem(this.SLOT_HEAD, this.getHead());
		for(int i = 0;i < 8;i++){
			this.getMenu().setItem(i, this.getCanHome(i));
			this.getMenu().setItem(i+9, this.getCanSetHome(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	public Guild getGuild() {
		return guild;
	}
	
	private ItemStack getCanHome(int index){
		ItemStack item = new ItemStack(this.getSubject().canHome(index) ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((this.getSubject().canHome(index) ? "§a§l" : "§c§l") + ("Permission d'utilisation"));
		String name = this.getGuild().getHomes()[index] != null ? " (§f§l" + this.getGuild().getHomes()[index].getName() + "§7)" : "";
		meta.setLore(Arrays.asList("§7" + ("Autoriser " + this.getName() + " à utiliser le PR #" + index + name)));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getCanSetHome(int index){
		ItemStack item = new ItemStack(this.getSubject().canSetHome(index) ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((this.getSubject().canSetHome(index) ? "§a§l" : "§c§l") + ("Permission de définition"));
		String name = this.getGuild().getHomes()[index] != null ? " (§f§l" + this.getGuild().getHomes()[index].getName() + "§7)" : "";
		meta.setLore(Arrays.asList("§7" + ("Autoriser " + this.getName() + " à définir le PR #" + index + name)));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getBack(){
		ItemStack item = new ItemStack(Material.MELON, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Autres permissions"));
		meta.setLore(Arrays.asList(("§7Retourner au menu des permissions."), "", ("§e§lCliquez pour ouvrir")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getHead(){
		ItemStack item = null;
		if(this.isGMember()){
			item = new ItemStack(Material.PLAYER_HEAD, 1);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setDisplayName("§f" + this.getName());
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(((GMember)this.getSubject()).getUniqueId())));
			item.setItemMeta(meta);
		}else{
			item = ((Rank)this.getSubject()).getItemStack();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f" + this.getName());
			item.setItemMeta(meta);
		}
		return item;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public PermissionsHolder getSubject() {
		return subject;
	}

	public void setSubject(PermissionsHolder subject) {
		this.subject = subject;
	}

	public boolean isGMember(){
		return this.getSubject() instanceof GMember;
	}
	
	public String getName(){
		if(this.isGMember())return ((GMember)this.getSubject()).getName();
		else return ((Rank)this.getSubject()).getName().toUpperCase();
	}
}
