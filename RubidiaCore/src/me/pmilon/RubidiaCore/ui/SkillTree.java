package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.abilities.RAbility;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerSetSlot;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Note.Tone;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SkillTree extends UIHandler {

	RClass rClass = rp.getRClass().equals(RClass.VAGRANT) ? RClass.PALADIN : rp.getRClass();
	List<RAbility> abilities = RAbility.getAvailable(rClass);//to get all available
	List<RAbility> leveledUp = RAbility.getAvailable(rp);//to get all abilities with enough level
	private boolean canClick = true;
	
	private final int SLOT_SKP = 0, SLOT_PASSIVE = 18, SLOT_ACTIVE = 27;
	private final HashMap<Integer, RAbility> slots = new HashMap<Integer, RAbility>();
	public SkillTree(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 36, "Arbre des compétences");
	}

	@Override
	public String getType(){
		return "SKILLTREE_MENU";
	}
	
	@Override
	protected boolean openWindow() {
		this.getMenu().clear();
		this.slots.clear();
		this.getMenu().setItem(this.SLOT_SKP, this.getSkp());
		this.getMenu().setItem(this.SLOT_PASSIVE, this.getPassive());
		this.getMenu().setItem(this.SLOT_ACTIVE, this.getActive());
		for(RAbility ability : RAbility.getAvailable(RClass.VAGRANT)){
			int slot = this.SLOT_SKP + ability.getIndex();
			slots.put(slot, ability);
			getMenu().setItem(slot, this.getAbility(ability));
		}
		abilities = RAbility.getAvailable(rClass);
		leveledUp = RAbility.getAvailable(rp);
		int passive = 0, active = 0;
		for(RAbility ability : abilities){
			if(ability.isPassive()) {
				passive++;
				int slot = this.SLOT_PASSIVE + passive;
				slots.put(slot, ability);
				getMenu().setItem(slot, this.getAbility(ability));
			} else {
				active++;
				int slot = this.SLOT_ACTIVE + active;
				slots.put(slot, ability);
				getMenu().setItem(slot, this.getAbility(ability));
			}
		}
		return this.getHolder().openInventory(getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(canClick){
			canClick = false;
			new BukkitTask(Core.instance){
				public void run(){
					canClick = true;
				}

				@Override
				public void onCancel() {
				}
			}.runTaskLater(4);
		}
		if(slot == this.SLOT_ACTIVE || slot == this.SLOT_PASSIVE){
			if(rp.getRClass().equals(RClass.VAGRANT)) {
				rClass = RClass.values()[RClass.indexOf(rClass)%(RClass.values().length - 1) + 1];//to dodge VAGRANT
				rp.sendMessage("§ePrésentation des compétences des " + rClass.getName().toLowerCase() + "s");
				Core.uiManager.requestUI(this);
			}
		} else {
			if(!rp.getRClass().equals(RClass.VAGRANT) || slot < this.SLOT_PASSIVE){
				int amount = 1;
				if(e.isShiftClick())amount = 5;
				RAbility ability = this.slots.get(slot);
				if(ability.getSettings().getLevelMax() < rp.getAbilityLevel(ability)+amount){
					amount = ability.getSettings().getLevelMax()-rp.getAbilityLevel(ability);
				}
				if(amount == 0){
					rp.sendMessage("§cVous avez déjà atteint le niveau maximal pour cette compétence.");
					this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				}else{
					if(rp.isAtLeast(ability.getMastery())){
						if(rp.getSkillPoints() >= amount){
							if(e.isShiftClick()){
								this.doMegaUpSound();
							}else{
								this.doUpSound();
							}
							rp.setAbilityLevel(ability, rp.getAbilityLevel(ability)+amount);
							rp.setSkillPoints(rp.getSkillPoints()-amount);
							leveledUp = RAbility.getAvailable(rp);
							getMenu().setItem(slot, this.getAbility(ability));
							getMenu().setItem(this.SLOT_SKP, this.getSkp());
							if(rp.getSkillPoints() < 1){
								Core.uiManager.requestUI(new SkillTree(this.getHolder()));
							}
							if(ability.getSettings().getLevelMax() == rp.getAbilityLevel(ability)) {
								rp.sendMessage("§aVous avez atteint le niveau maximal pour cette compétence.");
							}
						} else {
							this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
							rp.sendMessage("§cVous n'avez pas assez de points de compétences !");
						}
					} else rp.sendMessage("§cCette compétence est bloquée ! Débloquez-la au niveau " + ability.getMastery().getLevel() + " en devenant " + ability.getMastery().getName().toLowerCase() + ".");
				}
			} else rp.sendMessage("§cVous êtes vagabond ! Ces compétences ne sont qu'en présentation.");
		}
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	private ItemStack getSkp(){
		ItemStack item = new ItemStack(rp.getSkillPoints() > 0 ? Material.BOOK : Material.WRITABLE_BOOK, rp.getSkillPoints() > 64  || rp.getSkillPoints() < 1 ? 1 : rp.getSkillPoints());
		ItemMeta meta = item.getItemMeta();
		String color = (rp.getSkillPoints() > 0 ? "§2" : "§4");
		String ccolor = (rp.getSkillPoints() > 0 ? "§a" : "§c");
		meta.setDisplayName(ccolor + "§l" + rp.getSkillPoints() + color + " " + ("point" + (rp.getSkillPoints() > 1 ? "s" : "") + " de compétences"));
		meta.setLore(Arrays.asList("§7Les points de compétences sont gagnés à chaque niveau.", "§7Ils vous permettent d'augmenter le niveau d'une des", "§7compétences suivantes, modifiant ses paramètres et son coût."));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getPassive(){
		ItemStack item = new ItemStack(Material.SHIELD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName("§f§lCompétences passives");
		meta.setLore(Arrays.asList("§7Ne nécessitent aucune activation", "", rp.getRClass().equals(RClass.VAGRANT) ? ("§6Présentation des compétences des " + rClass.getName().toLowerCase() + "s.") : ("§8Clic gauche pour augmenter d'un niveau."), rp.getRClass().equals(RClass.VAGRANT) ? ("§e§lCliquez pour changer de classe présentée.") : ("§8Sneak + clic gauche pour augmenter de 5 niveaux.")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getActive(){
		ItemStack item = new ItemStack(rClass.getDisplay(), 1);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName("§f§lCompétences actives");
		meta.setLore(Arrays.asList("§7Nécessitent une combinaison de clics", "§7avec une arme de classe en main", "", rp.getRClass().equals(RClass.VAGRANT) ? ("§6Présentation des compétences des " + rClass.getName().toLowerCase() + "s.") : ("§8Clic gauche pour augmenter d'un niveau."), rp.getRClass().equals(RClass.VAGRANT) ? ("§e§lCliquez pour changer de classe présentée.") : ("§8Sneak + clic gauche pour augmenter de 5 niveaux.")));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getAbility(RAbility ability){
		boolean has = leveledUp.contains(ability);
		ItemStack item = new ItemStack(Material.SHEARS, rp.getAbilityLevel(ability) < 1 ? 1 : rp.getAbilityLevel(ability));
		ItemMeta meta = item.getItemMeta();
		((Damageable) meta).setDamage((int) Math.ceil(Material.SHEARS.getMaxDurability()*(.005*ability.getIndex() + .14*(RClass.indexOf(ability.getRClass()) - 1) + (has ? .075 : .01))));
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
		String color = has ? "§2" : "§4";
		String ccolor = has ? "§a" : "§c";
		meta.setDisplayName(color + (ability.getMastery().equals(Mastery.VAGRANT) ? "" : "§l[" + ability.getMastery().getName().toUpperCase() + "] ") + ccolor + ability.getName());
		List<String> lore = new ArrayList<String>();
		for(String line : ability.getDescription()) {
			lore.add("§7" + line);
		}
		String keystroke = "";
		if(ability.isPassive()){
			keystroke = "Passive";
		}else{
			String seq = ability.getSequence();
			String[] part = seq.split(",");
			if(part.length > 1){
				if(part[1].contains("SN") && !part[1].contains("!SN"))keystroke = "Sneak + ";
				if(part[1].contains("SP") && !part[1].contains("!SP"))keystroke = "Sprint + ";
			}
			String[] clicks = part[0].split("");
			if(clicks.length > 0)keystroke += ("Clic ");
			for(int i = 0;i < clicks.length;i++){
				if(clicks[i].equals("D"))keystroke += ("Droit");
				else if(clicks[i].equals("G"))keystroke += ("Gauche");
				if(i != clicks.length-1)keystroke += "/";
			}
			if(clicks.length > 0)keystroke += ("");
		}
		double damages = Utils.round(ability.getDamages(rp)*(ability.isPassive() ? 1 : rp.getAbilityDamagesFactor()), 2);
		double upDamages = Utils.round(((rp.getAbilityLevel(ability)+1)*ability.getSettings().getDamagesPerLevel()+ability.getSettings().getDamagesMin())*(ability.isPassive() ? 1 : rp.getAbilityDamagesFactor()), 2);
		double cost = Utils.round(ability.getVigorCost(rp), 2);
		double upCost = Utils.round(((rp.getAbilityLevel(ability)+1)*ability.getSettings().getVigorPerLevel()+ability.getSettings().getVigorMin()), 2);
		String cdamColor = damages-upDamages > 0 ? "§c" : "§a";
		String ddamColor = damages-upDamages > 0 ? "§4" : "§2";
		String ccostColor = cost-upCost < 0 ? "§c" : "§a";
		String dcostColor = cost-upCost < 0 ? "§4" : "§2";
		lore.addAll(Arrays.asList("", "§6§lUtilisation : §7" + keystroke,
				"§6§lCoût : §7" + cost + " EP" + (rp.getSkillPoints() > 0 && ability.getSettings().getLevelMax() > rp.getAbilityLevel(ability) ? " §e§l>>§a " + ccostColor + upCost + " " + dcostColor + "EP" : ""),
				"§6§l" + (ability.getSuppInfo().isEmpty() ? "Dégâts" : ability.getSuppInfo()) + " : §7" + damages + ability.getUnit() + (rp.getSkillPoints() > 0 && ability.getSettings().getLevelMax() > rp.getAbilityLevel(ability) ? " §e§l>>§a " + cdamColor + upDamages + ddamColor + ability.getUnit() : ""),
				"", "§eNiveau " + rp.getAbilityLevel(ability) + "/" + ability.getSettings().getLevelMax()));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private void doUpSound(){
		final Player player = this.getHolder();
		player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.D));
		new BukkitTask(Core.instance){
			public void run(){
				player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.F));
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(2);
	}

	private void doMegaUpSound() {
		final Player player = this.getHolder();
		player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.D));
		new BukkitTask(Core.instance){
			public void run(){
				player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.G));
				new BukkitTask(Core.instance){
					public void run(){
						player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.A));
						new BukkitTask(Core.instance){
							public void run(){
								player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.B));
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(2);
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(2);
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(2);
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}
	
	public void update(){
		new BukkitTask(Core.instance){

			@Override
			public void run() {
				if(getHolder().getOpenInventory().getTopInventory() == null)return;
				if(rp.getSkillPoints() > 64){
					WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot();
					packet.setWindowId(((CraftPlayer)getHolder()).getHandle().activeContainer.windowId);
					packet.setSlot(0);
					packet.setSlotData(getSkp());
					packet.sendPacket(getHolder());
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(1);//needs to a tick after inventory update
	}
}
