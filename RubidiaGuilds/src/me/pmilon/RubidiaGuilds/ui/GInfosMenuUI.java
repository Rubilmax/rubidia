package me.pmilon.RubidiaGuilds.ui;

import java.util.ArrayList;
import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

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
import me.pmilon.RubidiaGuilds.events.GMemberChangeGuildNameEvent;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.utils.Settings;

public class GInfosMenuUI extends UIHandler {

	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_NAME = new ItemStack(Material.PAPER, 1);
	private ItemStack ITEM_DESC = new ItemStack(Material.MAP, 1);
	private ItemStack ITEM_OFFER = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
	
	private int SLOT_BACK = 0;
	private int SLOT_NAME = 2;
	private int SLOT_DESC = 3;
	private int SLOT_DISPLAY = 5;
	private int SLOT_GLOWING = 6;
	private int SLOT_OFFER = 8;
	
	private int LISTENINGID_NAME = 1;
	private int LISTENINGID_DESC = 2;
	private int LISTID_CAPE = 3;
	
	private Guild guild;
	public GInfosMenuUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_INFOS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK){
			Core.uiManager.requestUI(new GMenuUI(this.getHolder(), this.getGuild()));
		}else if(slot == this.SLOT_NAME){
			if(gm.getPermission(Permission.RENAME)){
				rp.sendMessage("§aRenommez votre guilde en entrant le nom désiré dans le chat ! (MAX : " + Guild.NAME_LENGTH + "caractères)");
				this.close(true, this.LISTENINGID_NAME);
			}else rp.sendMessage("§cVous n'avez pas la permission de renommer votre guilde.");
		}else if(slot == this.SLOT_DESC){
			if(gm.getPermission(Permission.RENAME)){
				rp.sendMessage("§aModifiez votre description de guilde en entrant celle désirée dans le chat !");
				this.close(true, this.LISTENINGID_DESC);
			}else rp.sendMessage("§cVous n'avez pas la permission de modifier la description de votre guilde.");
		}else if(slot == this.SLOT_DISPLAY){
			if(e.isLeftClick()){
				if(gm.getPermission(Permission.CAPE) || gm.isLeader()){
					this.close(true, this.LISTID_CAPE);
					rp.sendMessage("§aPrenez une bannière entre vos mains et entrez son coût (les membres paieront ce montant pour chaque cape commandée).");
				}else rp.sendMessage("§cVous n'avez pas la permission de modifier l'item de votre guilde !");
			}else{
				if(this.getGuild().getBank() >= Settings.CAPE_COST || rp.isOp()){
					this.getGuild().broadcastMessage(Relation.MEMBER, "§&d" + gm.getName() + " §&ca acheté une cape de guilde !");
					this.getHolder().getInventory().addItem(this.getGuild().getCape());
					if(!rp.isOp()){
						this.getGuild().withdraw(Settings.CAPE_COST);
					}
				}else rp.sendMessage("§cVotre guilde n'a pas suffisamment d'émeraudes !");
			}
		}else if(slot == this.SLOT_GLOWING){
			if(gm.getPermission(Permission.CAPE) || gm.isLeader()) {
				this.getGuild().setGlowing(!this.getGuild().isGlowing());
			}
			this.menu.setItem(this.SLOT_DISPLAY, this.getInfos());
			this.menu.setItem(this.SLOT_GLOWING, this.getGlowing());
		}else if(slot == this.SLOT_OFFER) {
			Core.uiManager.requestUI(new GExpMenuUI(this.getHolder(), this.getGuild()));
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LISTENINGID_NAME){
					String[] chars = ChatColor.stripColor(this.getMessage()).split("");
					String name = "";
					for(int i = 0;i < Guild.NAME_LENGTH;i++){
						if(i == chars.length)break;
						name += chars[i];
					}
					if(Guild.getByName(name) == null){
						GMemberChangeGuildNameEvent event = new GMemberChangeGuildNameEvent(this.getGuild(), gm, name);
						Bukkit.getPluginManager().callEvent(event);
						if(!event.isCancelled())event.getGuild().setName(name);
					}else rp.sendMessage("§cUne guilde avec le même nom existe déjà !");
				}else if(this.getListeningId() == this.LISTENINGID_DESC){
					this.getGuild().setDescription(this.getMessage().replace('§', '?'));
				}else if(this.getListeningId() == this.LISTID_CAPE){
					ItemStack item = this.getHolder().getInventory().getItemInMainHand();
					if(item.getType().toString().contains("_BANNER")){
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName("§fCape de " + this.getGuild().getName());
						meta.setLore(new ArrayList<String>());
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_POTION_EFFECTS);
						item.setItemMeta(meta);
						ItemStack cape = item.clone();
						cape.setAmount(1);
						this.getGuild().setCape(cape);
					}else rp.sendMessage("§cVous ne pouvez utiliser qu'une bannière en tant que cape de votre guilde !");
				}
			}
		}
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getGuild().getName() + " : Informations", 32));
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_NAME, this.getGuildName());
		this.menu.setItem(this.SLOT_DESC, this.getGuildDesc());
		this.menu.setItem(this.SLOT_DISPLAY, this.getInfos());
		this.menu.setItem(this.SLOT_GLOWING, this.getGlowing());
		this.menu.setItem(this.SLOT_OFFER, this.getOffer());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§l" + ("Menu principal"));
		META_BACK.setLore(Arrays.asList(("§7Retourner au menu principal."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getGuildName(){
		ItemMeta paperm = ITEM_NAME.getItemMeta();
		paperm.setDisplayName("§a§l" + ("Nom de Guilde"));
		paperm.setLore(Arrays.asList(("§7Changez votre nom de guilde"), "", ("§6§lNom actuel : ") + "§e" + this.getGuild().getName()));
		ITEM_NAME.setItemMeta(paperm);
		return ITEM_NAME;
	}
	private ItemStack getGuildDesc(){
		ItemMeta paperm = ITEM_DESC.getItemMeta();
		String description  = this.getGuild().getDescription();
		paperm.setDisplayName("§a§l" + ("Description de Guilde"));
		paperm.setLore(Arrays.asList(("§7Changez votre description de guilde"), "", ("§6§lDescription actuelle : ") + "§e" + StringUtils.abbreviate(description, 22)));
		ITEM_DESC.setItemMeta(paperm);
		return ITEM_DESC;
	}
	private ItemStack getInfos(){
		ItemStack infos = this.getGuild().getCape();
		ItemMeta paperm = infos.getItemMeta();
		paperm.setDisplayName("§a§l" + ("Cape de " + this.getGuild().getName()));
		paperm.setLore(Arrays.asList(("§7La cape de votre guilde (cliquez droit pour en commander une !)"), ("§7Remplacez-la par une autre bannière en cliquant gauche."), "", ("§7Prix : §f" + Settings.CAPE_COST + "⟡"), ("§7§oReversées dans la banque de guilde")));
		infos.setItemMeta(paperm);
		return this.getGuild().isGlowing() ? Utils.setGlowingWithoutAttributes(infos) : infos;
	}
	private ItemStack getGlowing(){
		ItemStack ITEM_GLOWING = new ItemStack(Material.GRAY_DYE, 1);
		if(this.getGuild().isGlowing())ITEM_GLOWING = new ItemStack(Material.LIME_DYE, 1);
		ItemMeta META = ITEM_GLOWING.getItemMeta();
		META.setDisplayName((this.getGuild().isGlowing() ? "§a§l" : "§c§l") + ("Item brillant"));
		META.setLore(Arrays.asList(("§7Activer ou désactiver l'effet brillant"), ("§7de votre item de guilde.")));
		ITEM_GLOWING.setItemMeta(META);
		return ITEM_GLOWING;
	}
	private ItemStack getOffer(){
		ItemMeta META = ITEM_OFFER.getItemMeta();
		META.setDisplayName("§6§l" + ("Menu des offrandes"));
		META.setLore(Arrays.asList(("§7Ouvrir le menu vous permettant de faire des offrandes"), ("§7pour votre guilde (à partir du butin des monstres),"), ("§7lui octroyant de l'expérience et de la puissance.")));
		ITEM_OFFER.setItemMeta(META);
		return ITEM_OFFER;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

}
