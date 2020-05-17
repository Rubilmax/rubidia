package me.pmilon.RubidiaCore.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.levels.RLevels;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RPlayerManagerMenu extends UIHandler {

	private RPlayer rpp;
	private int SLOT_PROFILE = 0,SLOT_CRCS = 2,SLOT_MARRIAGE = 3,SLOT_OPTIONS = 6,SLOT_VARS = 8;
	public RPlayerManagerMenu(Player p, RPlayer rpp) {
		super(p);
		this.rpp = rpp;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rpp.getName() + "'s profile");
	}

	@Override
	public String getType() {
		return "RPLAYER_MANAGER_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_PROFILE, this.getHead());
		this.getMenu().setItem(this.SLOT_CRCS, this.getCrcs());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	private ItemStack getHead(){
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setDisplayName((rpp.isOnline() ? "§2[✔] §a" : "§4[✘] §c") + rpp.getName() + (rpp.isVip() ? " §8[§6§lVIP§8]" : ""));
		List<String> lore = new ArrayList<String>();
		lore.add("§f§m-------------------");
		if(!rpp.isPublicData()){
			lore.add("§4§l/!\\ §c" + ("Données confidentielles"));
		}
		if(!rpp.isProfileUpdated()){
			lore.add("§8" + ("Profil non à jour"));
		}
		double age = (double) ((long)(System.currentTimeMillis()-rpp.getBirthDate()))/Utils.MILLIS_IN_YEAR;
		lore.add("§8" + ("Sexe ") + "§7" + (rpp.getSex().getName().toLowerCase()));
		lore.add("§8" + ("Âge ") + "§7" + String.valueOf(Utils.round(age, 2)) +  (" ans"));
		lore.add("§8" + ("Date de naissance ") + "§7" + new SimpleDateFormat("dd/MM/yyyy").format(rpp.getBirthDate()));
		lore.add("");
		long time = System.currentTimeMillis()-rpp.getLastDivorce();
		lore.addAll(Arrays.asList("§8" + ("Meurtres ") + "§7" + rpp.getKills(), "§8" + ("Temps de jeu ") + "§7" + TimeUnit.MILLISECONDS.toHours(rpp.getGamingTime()) + "h", "§8" + (rpp.getCouple() == null ? (time >= Settings.TIME_BEFORE_WEDDING_PROPOSAL ? ("Célibataire") : ("Divorcé depuis ") + "§7" + TimeUnit.MILLISECONDS.toHours(time) + "h") : ("Marié à ") + "§7" + rpp.getCouple().getCompanion(rpp).getName())));
		GMember member = GMember.get(rpp);
		if(member.hasGuild()){
			Guild guild = member.getGuild();
			lore.addAll(Arrays.asList("", "§8" + ("Guilde ") + "§7" + guild.getName(), "§8" + ("Rang ") + "§7" + member.getRank().getName(), ""));
		}
		Date date = new Date(rpp.getLastConnectionDate());
		lore.addAll(Arrays.asList("§8" + ("Dernière connexion le "), "§7" + new SimpleDateFormat("dd/MM/yyyy").format(date) + (" §8à§7 ") + new SimpleDateFormat("HH:mm").format(date)));
		meta.setLore(lore);
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(rpp.getUniqueId())));
		skull.setItemMeta(meta);
		return skull;
	}
	
	private ItemStack getCrcs(){
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		int n = 0;
		for(int i = 0;i < rpp.getSaves().length;i++){
			if(rpp.getSaves()[i] != null){
				n++;
			}
		}
		SPlayer sp = rpp.getSaves()[rpp.getLastLoadedSPlayerId()];
		double ratio = sp.getRExp()/RLevels.getRLevelTotalExp(sp.getRLevel());
		meta.setDisplayName("§6" + ("Personnages") + "§8(" + n + ")");
		meta.setLore(Arrays.asList("§8" + ("Niveau ") + "§7" + sp.getRLevel(), "§8" + ("XP ") + "§7" + sp.getRExp() + " (" + Utils.round(ratio, 2) + "%)", "§8" + ("Maîtrise ") + "§7" + (sp.getMastery().getName()), "§8" + ("Métier ") + "§7" + ChatColor.stripColor((sp.getRJob().getName())), "§8" + ("Points de compétence ") + "§7" + sp.getSkp(), "§8" + ("Points de distinction ") + "§7" + sp.getSkd(), "§8" + ("Force ") + "§7" + sp.getStrength(), "§8" + ("Endurance ") + "§7" + sp.getEndurance(), "§8" + ("Agilité ") + "§7" + sp.getAgility(), "§8" + ("Intelligence ") + "§7" + sp.getIntelligence(), "§8" + ("Perception ") + "§7" + sp.getPerception(), "§8" + ("Meurtres ") + "§7" + sp.getKills(), "§8" + ("Renom ") + "§7" + sp.getRenom()));
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

}
