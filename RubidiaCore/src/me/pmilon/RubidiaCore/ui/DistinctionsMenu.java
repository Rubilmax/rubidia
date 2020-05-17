package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.books.BookUtils;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class DistinctionsMenu extends UIHandler {

	private int SLOT_SKD = 0, SLOT_STR = 2, SLOT_END = 3, SLOT_AGT = 4, SLOT_INT = 5, SLOT_PER = 6, SLOT_CRC = 8;
	private boolean canClick = true;
	public DistinctionsMenu(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, "Menu des distinctions");
	}

	@Override
	public String getType() {
		return "DISTINCTIONS_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.SLOT_SKD, this.getDistinctionPoints());
		this.getMenu().setItem(this.SLOT_STR, this.getStrength());
		this.getMenu().setItem(this.SLOT_END, this.getEndurance());
		this.getMenu().setItem(this.SLOT_AGT, this.getAgility());
		this.getMenu().setItem(this.SLOT_INT, this.getIntelligence());
		this.getMenu().setItem(this.SLOT_PER, this.getPerception());
		this.getMenu().setItem(this.SLOT_CRC, this.getCrc());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot > 1 && slot < 7){
			if(canClick){
				canClick = false;
				new BukkitTask(Core.instance){
					public void run(){
						canClick = true;
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(6);
				int amount = 1;
				if(e.isShiftClick())amount = 5;
				if(rp.getSkillDistinctionPoints() >= amount){
					rp.setSkillDistinctionPoints(rp.getSkillDistinctionPoints()-amount);
					if(slot == this.SLOT_STR){
						rp.addStrength(amount);
						this.menu.setItem(slot, this.getStrength());
					}else if(slot == this.SLOT_END){
						rp.addEndurance(amount);
						this.menu.setItem(slot, this.getEndurance());
						rp.heal();
					}else if(slot == this.SLOT_AGT){
						rp.addAgility(amount);
						this.menu.setItem(slot, this.getAgility());
					}else if(slot == this.SLOT_INT){
						rp.addIntelligence(amount);
						this.menu.setItem(slot, this.getIntelligence());
					}else if(slot == this.SLOT_PER){
						rp.addPerception(amount);
						this.menu.setItem(slot, this.getPerception());
					}
					this.menu.setItem(this.SLOT_SKD, this.getDistinctionPoints());
				}else{
					this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					rp.sendMessage("§cVous n'avez pas assez de points de distinction !");
				}
			}
		}else if(slot == this.SLOT_CRC){
			BookUtils.openCharacteristicsBook(Core.instance, p, p.getEquipment().getItemInMainHand());
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
	
	private ItemStack getDistinctionPoints(){
		ItemStack item = new ItemStack(Material.BOOK, rp.getSkillDistinctionPoints() > 64 || rp.getSkillDistinctionPoints() < 1 ? 1 : rp.getSkillDistinctionPoints());
		ItemMeta meta = item.getItemMeta();
		String color = rp.getSkillDistinctionPoints() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getSkillDistinctionPoints() + color + " " + ("point") + (rp.getSkillDistinctionPoints() > 1 ? "s" : "") + (" de distinction"));
		meta.setLore(Arrays.asList("§7" + ("Les points de distinction sont gagnés à chaque niveau."), "§7" + ("Ils vous permettent d'augmenter le niveau des distinctions suivantes."), "§7" + ("Les détails sur les effets de chaque distinction sont disponibles au survol.")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getStrength(){
		ItemStack item = new ItemStack(Material.RED_DYE, rp.getStrength() > 0 ? (rp.getStrength() > 64 ? 64 : rp.getStrength()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getStrength() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getStrength() + color + " " + ("point") + (rp.getStrength() > 1 ? "s" : "") + (" de force"));
		meta.setLore(Arrays.asList("§7La force augmente les dégâts de §lmêlée§7 que vous infligez",
				"§7en fonction du type d'arme que vous utilisez.",
				"§4+" + Utils.round(rp.getStrength()*Settings.STRENGTH_FACTOR_MELEE_DAMAGES_ON_MELEE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.STRENGTH_FACTOR_MELEE_DAMAGES_ON_MELEE*100, 2) + "%)" : "") + "§l §8dégâts d'une arme de mêlée",
				"§4" + Utils.round(rp.getStrength()*Settings.STRENGTH_FACTOR_MELEE_RANGE_DAMAGES_ON_MELEE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.STRENGTH_FACTOR_MELEE_RANGE_DAMAGES_ON_MELEE*100, 2) + "%)" : "") + "§l §8des dégâts totaux d'une arme polyvalente",
				"§4" + Utils.round(rp.getStrength()*Settings.STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE, 2) + ")" : "") + "§l §8dégâts à mains nues"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getEndurance(){
		ItemStack item = new ItemStack(Material.LIME_DYE, rp.getEndurance() > 0 ? (rp.getEndurance() > 64 ? 64 : rp.getEndurance()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getEndurance() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getEndurance() + color + " " + ("point") + (rp.getEndurance() > 1 ? "s" : "") + (" d'endurance"));
		meta.setLore(Arrays.asList("§7L'endurance augmente votre vie maximale, vitesse",
				"§7de régénération de la vigueur et défense.",
				"§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_MAXHEALTH, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_MAXHEALTH, 2) + ")" : "") + "§l §8points de vie",
				"§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_NRJREGEN, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_NRJREGEN, 2) + ")" : "") + "§l §8points de vigueur par seconde",
				"§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_DEFENSE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_DEFENSE*100, 2) + "%)" : "") + "§l §8défense physique",
				"§4+" + Utils.round(rp.getEndurance()*Settings.ENDURANCE_FACTOR_ABILITY_DEF*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.ENDURANCE_FACTOR_ABILITY_DEF*100, 2) + "%)" : "") + "§l §8défense magique"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getAgility(){
		ItemStack item = new ItemStack(Material.DANDELION, rp.getAgility() > 0 ? (rp.getAgility() > 64 ? 64 : rp.getAgility()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getAgility() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getAgility() + color + " " + ("point") + (rp.getEndurance() > 1 ? "s" : "") + (" d'agilité"));
		meta.setLore(Arrays.asList("§7L'agilité améliore vos dégâts des coups critiques,",
				"§7vitesse d'attaque et dégâts infligés à distance.",
				"§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_RANGE_DAMAGES_ON_RANGE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_RANGE_DAMAGES_ON_RANGE*100, 2) + "%)" : "") + "§l §8dégâts à distance",
				"§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_ATTACK_SPEED*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_ATTACK_SPEED*100, 2) + "%)" : "") + "§l §8vitesse d'attaque",
				"§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_CRITICAL_STRIKE_DAMAGES*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_CRITICAL_STRIKE_DAMAGES*100, 2) + "%)" : "") + "§l §8dégâts des coups critiques",
				"§4+" + Utils.round(rp.getAgility()*Settings.AGILITY_FACTOR_CRITICAL_STRIKE_CHANCE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.AGILITY_FACTOR_CRITICAL_STRIKE_CHANCE*100, 2) + "%)" : "") + "§l §8chance de coup critique"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getIntelligence(){
		ItemStack item = new ItemStack(Material.LIGHT_BLUE_DYE, rp.getIntelligence() > 0 ? (rp.getIntelligence() > 64 ? 64 : rp.getIntelligence()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getIntelligence() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getIntelligence() + color + " " + ("point") + (rp.getEndurance() > 1 ? "s" : "") + (" d'intelligence"));
		meta.setLore(Arrays.asList("§7L'intelligence augmente vos dégâts magiques,",
				"§7dégâts des compétences et vigueur maximum.",
				"§4+" + Utils.round(rp.getIntelligence()*Settings.INTELLIGENCE_FACTOR_MAGIC_DAMAGES_ON_MAGIC*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.INTELLIGENCE_FACTOR_MAGIC_DAMAGES_ON_MAGIC*100, 2) + "%)" : "") + "§l §8dégâts magiques",
				"§4+" + Utils.round(rp.getIntelligence()*Settings.INTELLIGENCE_FACTOR_ABILITY_DAMAGES*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.INTELLIGENCE_FACTOR_ABILITY_DAMAGES*100, 2) + "%)" : "") + "§l §8dégâts des compétences",
				"§4+" + Utils.round(rp.getIntelligence()*Settings.INTELLIGENCE_FACTOR_MAXNRJ, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.INTELLIGENCE_FACTOR_MAXNRJ, 2) + ")" : "") + "§l §8points de vigueur"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getPerception(){
		ItemStack item = new ItemStack(Material.MAGENTA_DYE, rp.getPerception() > 0 ? (rp.getPerception() > 64 ? 64 : rp.getPerception()) : 1);
		ItemMeta meta = item.getItemMeta();
		String color = rp.getPerception() > 0 ? "§2" : "§4";
		meta.setDisplayName(color + "§l" + rp.getPerception() + color + " " + ("point") + (rp.getEndurance() > 1 ? "s" : "") + (" de perception"));
		meta.setLore(Arrays.asList("§7La perception augmente votre régénération, votre chance",
				"§7de blocage, chance de butin rare et vitesse en élytres.",
				"§4+" + Utils.round(rp.getPerception()*Settings.PERCEPTION_FACTOR_REGENERATION, 2) + " " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.PERCEPTION_FACTOR_REGENERATION, 2) + ")" : "") + "§l §8points de vie par seconde",
				"§4+" + Utils.round(rp.getPerception()*Settings.PERCEPTION_FACTOR_BLOCK_CHANCE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.PERCEPTION_FACTOR_BLOCK_CHANCE*100, 2) + "%)" : "") + "§l §8chance de blocage",
				"§4+" + Utils.round(rp.getPerception()*Settings.PERCEPTION_FACTOR_LOOT_CHANCE*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(+" + Utils.round(Settings.PERCEPTION_FACTOR_LOOT_CHANCE*100, 2) + "%)" : "") + "§l §8chance de butin rare",
				"§4-" + Utils.round(rp.getPerception()*Settings.PERCEPTION_FACTOR_LIFT_COST*100, 2) + "% " + (rp.getSkillDistinctionPoints() > 0 ? "§c§o(-" + Utils.round(Settings.PERCEPTION_FACTOR_LIFT_COST*100, 2) + "%)" : "") + "§l §8coût d'élévation en élytres"));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getCrc(){
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(this.getHolder().getUniqueId()));
		meta.setDisplayName("§f§l" + rp.getRClass().getName().toUpperCase());
		meta.setLore(Arrays.asList("§7" + ("Cliquez pour obtenir vos caractéristiques détaillées."), "§7" + ("Les valeurs d'attaque et de défense"), "§7" + ("dépendent de votre arme et armure actuelles.")));
		item.setItemMeta(meta);
		return item;
	}

}
