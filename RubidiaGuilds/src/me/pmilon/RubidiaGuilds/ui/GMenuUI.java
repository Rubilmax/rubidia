package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.events.GMemberLeaveGuildEvent;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;

public class GMenuUI extends UIHandler {
	
	private ItemStack ITEM_BANK = new ItemStack(Material.ENDER_CHEST, 1);
	private ItemStack ITEM_CLAIMS = new ItemStack(Material.CHEST, 1);
	private ItemStack ITEM_MEMBERS = new ItemStack(Material.PLAYER_HEAD, 1);
	private ItemStack ITEM_RANKS = new ItemStack(Material.LADDER, 1);
	private ItemStack ITEM_LEAVE = new ItemStack(Material.REDSTONE_BLOCK, 1);
	private ItemStack ITEM_DISBAND = new ItemStack(Material.BARRIER, 1);
	
	private int SLOT_INFOS = 0;
	private int SLOT_BANK = 2;
	private int SLOT_CLAIMS = 3;
	private int SLOT_MEMBERS = 4;
	private int SLOT_RANKS = 5;
	private int SLOT_LEAVE = 7;
	private int SLOT_DISBAND = 8;
	
	private int DESC_LIMIT = 40;
	
	private Guild guild;
	public GMenuUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getGuild().getName() + (" : Menu de guilde"), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_INFOS){
			Core.uiManager.requestUI(new GInfosMenuUI(this.getHolder(), this.getGuild()));
		}else if(slot == this.SLOT_BANK){
			if(gm.getPermission(Permission.BANK_DEPOSIT) || rp.isOp()) {
				Core.uiManager.requestUI(new GBankUI(this.getHolder(), this.getGuild()));
			}else rp.sendMessage("§cVous n'avez pas la permission de déposer des émeraudes dans la banque de votre guilde.");
		}else if(slot == this.SLOT_CLAIMS){
			Core.uiManager.requestUI(new GClaimsMenuUI(this.getHolder(), this.getGuild(), Claim.get(this.getHolder().getLocation())));
		}else if(slot == this.SLOT_MEMBERS){
			Core.uiManager.requestUI(new GMembersUI(this.getHolder(), this.getGuild()));
		}else if(slot == this.SLOT_RANKS){
			Core.uiManager.requestUI(new GRanksUI(this.getHolder(), this.getGuild()));
		}else if(slot == this.SLOT_LEAVE){
			if(!gm.isLeader()){
				GMemberLeaveGuildEvent event = new GMemberLeaveGuildEvent(this.getGuild(), gm);
				Bukkit.getPluginManager().callEvent(event);
				if(!event.isCancelled()){
					event.getGuild().removeMember(gm);
					this.close(false);
					rp.sendMessage("§cVous avez quitté la guilde §4§l" + event.getGuild().getName() + "§c.");
				}
			}else rp.sendMessage("§cVous devez d'abord léguer la direction !");
		}else if(slot == this.SLOT_DISBAND){
			if(gm.isLeader() || rp.isOp()){
				Core.uiManager.requestUI(new GuildDisbandConfirmationUI(rp, this.getGuild()));
			}else rp.sendMessage("§cVous devez être chef pour dissoudre cette guilde !");
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_INFOS, this.getGuild().isGlowing() ? Utils.setGlowingWithoutAttributes(this.getInfos()) : this.getInfos());
		this.menu.setItem(this.SLOT_BANK, this.getBank());
		this.menu.setItem(this.SLOT_CLAIMS, this.getClaims());
		this.menu.setItem(this.SLOT_MEMBERS, this.getMembers());
		this.menu.setItem(this.SLOT_RANKS, this.getRanks());
		this.menu.setItem(this.SLOT_LEAVE, this.getLeave());
		this.menu.setItem(this.SLOT_DISBAND, this.getDisband());
		return this.getHolder().openInventory(this.menu) != null;
	}
	

	private ItemStack getInfos(){
		ItemStack infos = this.getGuild().getCape();
		ItemMeta paperm = infos.getItemMeta();
		paperm.setDisplayName("§6§l" + ("Menu des informations"));
		paperm.setLore(Arrays.asList(("§7Aller au menu des informations (Statistiques, Affichage...)"), "", ("§6§lNom de guilde : ") + "§e" + this.getGuild().getName(), ("§6§lDescription : ") + "§e" + StringUtils.abbreviate(this.getGuild().getDescription(), this.DESC_LIMIT)));
		paperm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_POTION_EFFECTS);
		infos.setItemMeta(paperm);
		return this.getGuild().isGlowing() ? Utils.setGlowingWithoutAttributes(infos) : infos;
	}
	private ItemStack getClaims(){
		ItemMeta paperm = ITEM_CLAIMS.getItemMeta();
		paperm.setDisplayName("§a§l" + ("Menu des territoires"));
		paperm.setLore(Arrays.asList(("§7Aller au menu des Claims (Gestion, Préférences...)"), "", ("§6§lNombre de territoires : ") + "§e" + this.getGuild().getClaims().size()));
		ITEM_CLAIMS.setItemMeta(paperm);
		return ITEM_CLAIMS;
	}
	private ItemStack getMembers(){
		ItemMeta paperm = ITEM_MEMBERS.getItemMeta();
		paperm.setDisplayName("§a§l" + ("Menu des membres"));
		paperm.setLore(Arrays.asList(("§7Aller au menu des Membres (Préférences par membre)"), "", ("§6§lNombre de membres : ") + "§e" + this.getGuild().getMembers().size()));
		ITEM_MEMBERS.setItemMeta(paperm);
		return ITEM_MEMBERS;
	}
	private ItemStack getRanks(){
		ItemMeta paperm = ITEM_RANKS.getItemMeta();
		paperm.setDisplayName("§a§l" + ("Menu des rangs"));
		paperm.setLore(Arrays.asList(("§7Aller au menu des Rangs (Préférences par rang)"), "", ("§6§lVotre rang : ") + "§e" + gm.getRank().getName()));
		ITEM_RANKS.setItemMeta(paperm);
		return ITEM_RANKS;
	}
	private ItemStack getLeave(){
		ItemMeta paperm = ITEM_LEAVE.getItemMeta();
		paperm.setDisplayName("§c§l" + ("Quitter la guilde"));
		paperm.setLore(Arrays.asList(("§7Si vous êtes le LEADER, vous devez donner la direction à un membre.")));
		ITEM_LEAVE.setItemMeta(paperm);
		return ITEM_LEAVE;
	}
	private ItemStack getBank(){
		ItemMeta paperm = ITEM_BANK.getItemMeta();
		paperm.setDisplayName("§a§l" + ("Banque de guilde"));
		paperm.setLore(Arrays.asList(("§7Ouvrir la banque de guilde pour partager des émeraudes.")));
		ITEM_BANK.setItemMeta(paperm);
		return ITEM_BANK;
	}
	private ItemStack getDisband(){
		ItemMeta paperm = ITEM_DISBAND.getItemMeta();
		paperm.setDisplayName("§4§l" + ("Dissoudre la guilde"));
		paperm.setLore(Arrays.asList(("§7Tout le monde se retrouvera sans guilde...")));
		ITEM_DISBAND.setItemMeta(paperm);
		return ITEM_DISBAND;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

}
