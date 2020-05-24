package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.ui.GCreateMenuUI;
import me.pmilon.RubidiaGuilds.ui.GMenuUI;
import me.pmilon.RubidiaPets.ui.PetsUI;
import me.pmilon.RubidiaQuests.ui.PlayerQuestList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SPlayerManager extends UIHandler{

	private int SLOT_SKT = 0, SLOT_CRC = 1, SLOT_GLD = 3, SLOT_QST = 4, SLOT_BOOSTERS = 5, SLOT_PETS = 6, SLOT_PLAY = 7, SLOT_PROFILE = 8;
	public SPlayerManager(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, ("Menu du personnage"));
	}

	@Override
	public String getType() {
		return "SPLAYER_MANAGEMENT_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(SLOT_SKT, this.getSkt());
		this.menu.setItem(SLOT_CRC, this.getCrc());
		this.menu.setItem(SLOT_GLD, this.getGuild());
		this.menu.setItem(SLOT_QST, this.getQuests());
		this.menu.setItem(SLOT_BOOSTERS, this.getBoosters());
		this.menu.setItem(SLOT_PETS, this.getPets());
		this.menu.setItem(SLOT_PLAY, this.getPlay());
		this.menu.setItem(SLOT_PROFILE, this.getProfile());
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == SLOT_SKT)Core.uiManager.requestUI(new SkillTree(this.getHolder()));
		else if(slot == SLOT_CRC)Core.uiManager.requestUI(new DistinctionsMenu(this.getHolder()));
		else if(slot == SLOT_GLD){
			if(gm.hasGuild())Core.uiManager.requestUI(new GMenuUI(this.getHolder(), gm.getGuild()));
			else Core.uiManager.requestUI(new GCreateMenuUI(this.getHolder()));
		}else if(slot == SLOT_QST)Core.uiManager.requestUI(new PlayerQuestList(this.getHolder()));
		else if(slot == SLOT_BOOSTERS)Core.uiManager.requestUI(new RBoostersUI(this.getHolder()));
		else if(slot == SLOT_PETS)Core.uiManager.requestUI(new PetsUI(this.getHolder()));
		else if(slot == SLOT_PLAY)Core.uiManager.requestUI(new SPlayerSelectionMenu(this.getHolder()));
		else if(slot == SLOT_PROFILE){
			rp.getChat().addInfo("");
			rp.getChat().addInfo(("    §6§l/!\\  §eLisez attentivement les informations en slot §l#1  §6§l/!\\"));
			rp.getChat().addInfo("");
			rp.getChat().update();
			Core.uiManager.requestUI(new ProfileUI(this.getHolder()));
		}
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	private ItemStack getSkt(){
		ItemStack item = new ItemStack(Material.JUNGLE_SAPLING,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§lArbre des compétences");
		meta.setLore(Arrays.asList("§7Améliorez vos compétences"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getCrc(){
		ItemStack item = new ItemStack(Material.ARMOR_STAND,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§lMenu des distinctions");
		meta.setLore(Arrays.asList("§7Améliorez vos distinctions"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getGuild(){
		ItemStack item = gm.hasGuild() ? gm.getGuild().getCape() : new ItemStack(Material.ENCHANTING_TABLE,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§lMenu de guilde");
		meta.setLore(Arrays.asList("§7Gérez votre guilde"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getQuests(){
		ItemStack item = new ItemStack(Material.BOOK,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Menu des quêtes"));
		meta.setLore(Arrays.asList("§7Ouvrez la liste de vos quêtes actives"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getBoosters(){
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§lMenu des boosters");
		meta.setLore(Arrays.asList("§7Activez ou désactivez des boosters"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getPets(){
		ItemStack item = new ItemStack(Material.DRAGON_EGG,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§lMenu des compagnons");
		meta.setLore(Arrays.asList("§7Gérez vos compagnons depuis ce menu !"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getPlay(){
		ItemStack item = new ItemStack(Material.END_CRYSTAL,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Menu de sélection du personnage"));
		meta.setLore(Arrays.asList("§7Sélectionnez un autre personnage avec lequel jouer"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getProfile(){
		ItemStack item = new ItemStack(Material.MILK_BUCKET,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Menu du profil"));
		meta.setLore(Arrays.asList("§7" + ("Gérez les informations de votre profil")));
		item.setItemMeta(meta);
		return item;
	}
}
