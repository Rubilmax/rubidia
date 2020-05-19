package me.pmilon.RubidiaCore.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import me.pmilon.RubidiaCore.RManager.Gender;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ProfileUI extends UIHandler {

	private int LIST_ID_BIRTH = 4;
	
	private int SLOT_INFOS = 0;
	private int SLOT_GENDER = 1;
	private int SLOT_BIRTH = 2;
	private int SLOT_DATA = 3;
	private int SLOT_UPDATE = 4;
	
	public ProfileUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, ("Mon profil"));
	}

	@Override
	public String getType() {
		return "PROFILE_MENU";
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_BIRTH){
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					try {
					    Date date = formatter.parse(this.getMessage());
					    rp.setBirthDate(date.getTime());
					} catch (ParseException e) {
						rp.sendMessage("§cFormat de date invalide.");
					}
				}
			}
		}
		
		this.getMenu().setItem(this.SLOT_INFOS, this.getInfos());
		this.getMenu().setItem(this.SLOT_GENDER, this.getGender());
		this.getMenu().setItem(this.SLOT_BIRTH, this.getBirthDate());
		this.getMenu().setItem(this.SLOT_DATA, this.getData());
		this.getMenu().setItem(this.SLOT_UPDATE, this.getUpdate());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_GENDER){
			if(rp.getSex().equals(Gender.UNKNOWN))rp.setSex(Gender.MALE);
			else if(rp.getSex().equals(Gender.MALE))rp.setSex(Gender.FEMALE);
			else if(rp.getSex().equals(Gender.FEMALE))rp.setSex(Gender.UNKNOWN);
			this.getMenu().setItem(this.SLOT_GENDER, this.getGender());
		}else if(slot == this.SLOT_BIRTH){
			this.close(true, this.LIST_ID_BIRTH);
			rp.sendMessage("§aEntrez votre date de naissance dans le format suivant : §ljj/mm/aaaa§a.");
		}else if(slot == this.SLOT_DATA){
			rp.setPublicData(!rp.isPublicData());
			this.getMenu().setItem(this.SLOT_DATA, this.getData());
		}else if(slot == this.SLOT_UPDATE){
			rp.setProfileUpdated(!rp.isProfileUpdated());
			this.getMenu().setItem(this.SLOT_UPDATE, this.getUpdate());
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

	public ItemStack getInfos(){
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7§lInformations");
		meta.setLore(Arrays.asList("§8" + ("Les données suivantes sont recueillies à des fins statistiques."), "§8" + ("Aucun joueur n'est forcé à nous fournir des données."), "", "§8" + ("Le cas échéant, fournissez-nous des données valides !"), "§8" + ("Vous serez toujours capable de modifier ces données ici."), "§c" + ("Ces données resteront toujours notre propriété.")));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getGender(){
		ItemStack item = new ItemStack(rp.getSex().equals(Gender.UNKNOWN) ? Material.BUCKET : (rp.getSex().equals(Gender.MALE) ? Material.LAVA_BUCKET : Material.WATER_BUCKET), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + (rp.getSex().getName().toUpperCase()));
		meta.setLore(Arrays.asList("§7" + ("Cliquez pour cycler à travers les 3 genres disponibles.")));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getBirthDate(){
		ItemStack item = new ItemStack(Material.CAKE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + rp.getFormattedBirthDate());
		meta.setLore(Arrays.asList("§7" + ("Votre date de naissance (par défaut : 01/01/1970).")));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getData(){
		ItemStack item = new ItemStack(rp.isPublicData() ? Material.LIME_DYE : Material.ROSE_RED, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Confidentialité : ") + (rp.isPublicData() ? "§a" + ("publique") : "§c" + ("privée")));
		meta.setLore(Arrays.asList("§7" + ("Basculez la confidentialité de votre profil."), "§7" + ("Privée : aucun joueur ne peut accéder à ces données"), "§7" + ("Publique : tous les joueurs peuvent accéder à ces données (/rplayers)")));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getUpdate(){
		ItemStack item = new ItemStack(rp.isProfileUpdated() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Profil à jour"));
		meta.setLore(Arrays.asList("§7" + ("Activez cette option une fois votre profil mis à jour.")));
		item.setItemMeta(meta);
		return item;
	}

}
