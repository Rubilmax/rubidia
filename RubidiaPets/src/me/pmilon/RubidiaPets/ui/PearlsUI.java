package me.pmilon.RubidiaPets.ui;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaPets.PetsPlugin;
import me.pmilon.RubidiaPets.pets.Pearl;
import me.pmilon.RubidiaPets.pets.Pearl.PearlType;
import me.pmilon.RubidiaPets.pets.Pet;

public class PearlsUI extends UIHandler {

	//TODO update this menu
	
	private BukkitTask task;
	
	private final Pet pet;
	private final LivingEntity entity;
	private final int SLOT_BACK = 4;
	public PearlsUI(Player p, Pet pet) {
		super(p);
		this.pet = pet;
		this.entity = (LivingEntity) pet.getEntity();
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, StringUtils.abbreviate(pet.getName() + (" §r: Perles"), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "PET_PEARLS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
		ItemStack is = e.getCurrentItem();
		if(is != null){
			if(!is.getType().equals(Material.AIR)){
				if(is.hasItemMeta()){
					ItemMeta meta = is.getItemMeta();
					if(meta.hasDisplayName()){
						if(meta.getDisplayName().contains("Perle de")){
							e.setCancelled(true);
							String[] name = ChatColor.stripColor(meta.getDisplayName()).split(" ");
							if(name.length >= 5){
								PearlType type = null;
								for(PearlType tp : PearlType.values()){
									if(meta.getDisplayName().contains(tp.getDisplay()[1])){
										type = tp;
									}
								}
								if(type != null){
									if(!this.containsPearlType(type)){
										if(getMenu().firstEmpty() != -1){
											String[] chars = name[4+type.getShift()].split("");
											String amount = "";
											for(int i = 0;i < chars.length;i++){
												if(!(chars[i].equalsIgnoreCase("(") || chars[i].equalsIgnoreCase(")") || chars[i].equalsIgnoreCase("h"))){
													amount += chars[i];
												}
											}
											if(Utils.isInteger(amount)){
												is.setAmount(is.getAmount()-1);
												if(is.getAmount() <= 0)is = new ItemStack(Material.AIR);
												e.setCurrentItem(is);
												long duration = Long.valueOf(amount)*3600000;
												int amplifier = name[3+type.getShift()].length();
												Pearl pearl = new Pearl(type, System.currentTimeMillis(), duration, amplifier);
												this.getPet().getActivePearls().add(pearl);
												new BukkitTask(PetsPlugin.instance){

													@Override
													public void run() {
														Core.uiManager.requestUI(new PearlsUI(getHolder(), getPet()));
													}

													@Override
													public void onCancel() {
													}
													
												}.runTaskLater(0);
											}
										}else{
											this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
											rp.sendMessage("§cIl n'y a plus de slot disponible.");
											e.setCancelled(true);
										}
									}else{
										this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
										rp.sendMessage("§cVotre compagnon porte déjà une perle active de ce type !");
										e.setCancelled(true);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(this.getPet().getActivePearls().size() > slot){
			rp.sendMessage("§cVous ne pouvez enlever une perle active !");
		}else if(slot == SLOT_BACK){
			this.getUIManager().requestUI(new PetUI(this.getHolder(), this.pet));
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(this.task != null)this.task.cancel();
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < this.getPet().getActivePearls().size();i++){
			Pearl pearl = this.getPet().getActivePearls().get(i);
			ItemStack stack = new ItemStack(Material.ENDER_EYE, 1);
			ItemMeta meta = stack.getItemMeta();
			String lvl = "";
			for(int j = 0;j < pearl.getAmplifier();j++){
				lvl += "I";
			}
			meta.setDisplayName("§a" + ("Perle de " + pearl.getType().getDisplay()[1]) + " " + lvl + " §2(" + (int)(pearl.getDuration()/3600000.0) + "h)");
			stack.setItemMeta(meta);
			getMenu().setItem(i, stack);
		}
		getMenu().setItem(SLOT_BACK, this.getBack());
		this.task = new BukkitTask(PetsPlugin.instance){
			
			@Override
			public void run(){
				for(int slot = 0;slot < SLOT_BACK;slot++){
					ItemStack item = getMenu().getItem(slot);
					if(item != null){
						if(!item.getType().equals(Material.AIR)){
							ItemStack stack = item.clone();
							if(getPet().getActivePearls().size() > slot){
								Pearl pearl = getPet().getActivePearls().get(slot);
								if(pearl.isValid()){
									ItemMeta meta = stack.getItemMeta();
									meta.setLore(Arrays.asList("§8" + convertPearlDurationToString(pearl.getStartTime()+pearl.getDuration()-System.currentTimeMillis())));
									stack.setItemMeta(meta);
									getMenu().setItem(slot, me.pmilon.RubidiaCore.utils.Utils.setGlowingWithoutAttributes(stack));
								}else getMenu().setItem(slot, new ItemStack(Material.AIR, 1));
							}else getMenu().setItem(slot, new ItemStack(Material.AIR, 1));
						}
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0, 20);
		return this.getHolder().openInventory(getMenu()) != null;
	}
	
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.ARROW, 1);
		ItemMeta backm = back.getItemMeta();
		backm.setDisplayName(("§7Retour au menu du compagnon"));
		back.setItemMeta(backm);
		return back;
	}
	
	public boolean containsPearlType(PearlType type){
		for(Pearl pearl : this.getPet().getActivePearls()){
			if(pearl.getType().equals(type))return true;
		}
		return false;
	}

	public static String convertPearlDurationToString(Long time) {
		long hours = TimeUnit.MILLISECONDS.toHours(time);
		time -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		time -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		time -= TimeUnit.SECONDS.toMillis(seconds);
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public LivingEntity getEntity() {
		return entity;
	}
	
	public Pet getPet(){
		return pet;
	}
}
