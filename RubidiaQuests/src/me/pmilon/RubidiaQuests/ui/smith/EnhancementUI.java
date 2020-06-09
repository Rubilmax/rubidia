package me.pmilon.RubidiaQuests.ui.smith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.general.RItemStacks;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.ScrollType;
import me.pmilon.RubidiaCore.scrolls.ScrollType.ScrollUsage;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaQuests.QuestsPlugin;

public class EnhancementUI extends UIHandler {

	private ItemStack FAILURE = new ItemStack(Material.BARRIER, 1);
	private ItemStack HELP = new ItemStack(Material.HOPPER, 1);
	private Weapon weapon;
	private Random random = new Random();
	private int[] orderedSlots = new int[]{5,8,7,6,3,0,1,2};
	private List<Integer> failSlots = new ArrayList<Integer>();
	private boolean win;
	private List<Scroll> scroll = new ArrayList<Scroll>();
	private BukkitTask endTask = null;
	private boolean startStone = false;
	
	public EnhancementUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.DISPENSER, ("Renforcement"));
		
		ItemMeta meta = this.FAILURE.getItemMeta();
		meta.setDisplayName("§4§l" + ("ECHEC"));
		meta.setLore(Arrays.asList("§c" + ("L'opération échoue si la pierre étoile s'arrête ici")));
		FAILURE.setItemMeta(meta);
		
		meta.setDisplayName("§8§lINFORMATIONS");
		meta.setLore(Arrays.asList("§7" + ("Choisissez dans votre inventaire une arme/armure à renforcer"), "§7" + ("et une pierre étoile à utiliser (nécessaire)."), "§7" + ("Vous pouvez également sélectionner un parchemin approprié à utiliser."), "", "§f" + ("Cliquez ensuite sur la pierre étoile pour débuter le renforcement !")));
		HELP.setItemMeta(meta);
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "ENHANCEMENT_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		ItemStack item = e.getCurrentItem();
		if(item != null){
			RItem rItem = new RItem(item);
			if(rItem.isWeapon()){
				Weapon weapon = rItem.getWeapon();
				if(weapon.getSuppLevel() < 7){
					if(!this.scroll.isEmpty()){
						for(Scroll scroll : this.scroll){
							scroll.give(this.getHolder());
						}
						this.scroll.clear();
					}
					e.setCurrentItem(new ItemStack(Material.AIR));
					if(this.weapon != null){
						this.getHolder().getInventory().addItem(this.weapon.getNewItemStack(rp));
					}
					this.weapon = weapon;
					this.update();
				}else rp.sendMessage("§cVotre " + (this.weapon.isAttack() ? "arme" : "armure") + " est déjà trop puissante pour un renforcement classique !");
			}else if(rItem.isScroll()){
				Scroll scroll = rItem.getScroll();
				if(scroll.getType().getUsage().equals(ScrollUsage.ENHANCEMENT)){
					if(this.weapon != null){
						if(!this.isScrollActive(scroll.getType())){
							if(scroll.getType().equals(ScrollType.ENHANCEMENT_AMPLIFICATION) && weapon.getSuppLevel() >= 6){
								rp.sendMessage("§cVous ne pouvez utiliser ce parchemin sur une " + (this.weapon.isAttack() ? "arme" : "armure") + " si puissante !");
								return;
							}
							this.scroll.add(scroll);
							item.setAmount(item.getAmount()-1);
							if(item.getAmount() <= 0)item = new ItemStack(Material.AIR);
							e.setCurrentItem(item);
							this.update();
						}
					}else rp.sendMessage("§cChoisissez d'abord une arme/armure à renforcer.");
				}else rp.sendMessage("§cCe parchemin ne peut être utilisé durant un renforcement.");
			}else if(item.isSimilar(RItemStacks.STAR_STONE.getItemStack())){
				if(this.weapon != null){
					if(!this.startStone){
						item.setAmount(item.getAmount()-1);
						if(item.getAmount() <= 0)item = new ItemStack(Material.AIR);
						e.setCurrentItem(item);
						this.startStone = true;
						this.update();
					}
				}else rp.sendMessage("§cChoisissez d'abord une arme/armure à renforcer.");
			}
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == 4){
			if(this.endTask == null){
				if(this.startStone){
					this.getHolder().getInventory().addItem(RItemStacks.STAR_STONE.getItemStack());
					this.startStone = false;
				}
				if(!this.scroll.isEmpty()){
					for(Scroll scroll : this.scroll){
						scroll.give(this.getHolder());
					}
					this.scroll.clear();
				}
				if(this.weapon != null){
					this.getHolder().getInventory().addItem(this.weapon.getNewItemStack(rp));
					this.weapon = null;
				}
				this.update();
			}
		}else if(slot == 5){
			this.endTask = new BukkitTask(QuestsPlugin.instance){

				@Override
				public void run() {
					closeUI();
				}

				@Override
				public void onCancel() {
				}
				
			};
			double iterations = random.nextInt(12)+24;
			long waitTime = 0;
			for(int j = 0;j <= iterations;j++){
				final int sloti = orderedSlots[j%8];
				final int slotf = orderedSlots[(j+1)%8];
				waitTime += (long) Math.round(Math.pow(j/iterations, 1.74)*9+3);
				final boolean end = j == iterations;
				final UIHandler instance = this;
				new BukkitTask(QuestsPlugin.instance){
					@Override
					public void run() {
						if(Core.uiManager.hasActiveSession(getHolder())){
							if(Core.uiManager.getSession(getHolder()).getUIHandler().equals(instance)){
								getMenu().setItem(sloti, failSlots.contains(sloti) ? FAILURE.clone() : new ItemStack(Material.AIR));
								getMenu().setItem(slotf, RItemStacks.STAR_STONE.getItemStack());
								getHolder().playNote(getHolder().getLocation(), Instrument.PIANO, Note.flat(0, Tone.D));
								if(end){
									endTask.runTaskLater(20);
								}
							}
						}
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(waitTime);
				if(j == iterations)this.win = this.getMenu().getItem(slotf) != null ? !this.getMenu().getItem(slotf).isSimilar(FAILURE) : true;
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(this.endTask != null){
			this.endTask = null;
			if(this.win){
				weapon.setSuppLevel(weapon.getSuppLevel()+1);
				if(this.scroll != null){
					if(this.isScrollActive(ScrollType.ENHANCEMENT_AMPLIFICATION)){
						weapon.setSuppLevel(weapon.getSuppLevel()+1);
					}
				}
				this.getHolder().getInventory().addItem(weapon.getNewItemStack(rp));
				Firework f = (Firework) this.getHolder().getWorld().spawn(this.getHolder().getLocation(), Firework.class);
				FireworkMeta fm = f.getFireworkMeta();
				fm.addEffect(FireworkEffect.builder()
						.flicker(false)
						.trail(false)
						.with(Type.BALL_LARGE)
						.withColor(Color.RED)
						.withColor(Color.ORANGE)
						.withFade(Color.YELLOW)
						.withFade(Color.FUCHSIA)
						.withFade(Color.WHITE)
						.build());
				fm.setPower(1);
				f.setFireworkMeta(fm);
				rp.sendMessage("§aVotre " + (this.weapon.isAttack() ? "arme" : "armure") + " a été renforcée !");
			}else{
				this.getHolder().getWorld().playSound(this.getHolder().getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
				if(this.scroll == null){
					rp.sendMessage("§cEchec du renforcement ! Votre " + (this.weapon.isAttack() ? "arme" : "armure") + " a été cassée...");
					rp.sendMessage("§cLa prochaine fois, utilisez un parchemin afin de protéger votre item !");
				}else{
					if(this.isScrollActive(ScrollType.ENHANCEMENT_PROTECTION)){
						this.getHolder().getInventory().addItem(this.weapon.getNewItemStack(rp));
						rp.sendMessage("§eLe renforcement a échoué ! Mais votre " + (this.weapon.isAttack() ? "arme" : "armure") + " a été conservée grâce à votre parchemin de protection.");
					}else{
						rp.sendMessage("§cEchec du renforcement ! Votre " + (this.weapon.isAttack() ? "arme" : "armure") + " a été cassée...");
						rp.sendMessage("§cLa prochaine fois, utilisez un parchemin afin de protéger votre item !");
					}
				}
			}
		}else{
			if(this.weapon != null)this.getHolder().getInventory().addItem(this.weapon.getNewItemStack(rp));
			if(this.startStone)this.getHolder().getInventory().addItem(RItemStacks.STAR_STONE.getItemStack());
			if(!this.scroll.isEmpty()){
				for(Scroll scroll : this.scroll){
					scroll.give(this.getHolder());
				}
			}
		}
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(4, HELP.clone());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private void update(){
		if(this.weapon != null){
			this.getMenu().setItem(4, this.weapon.updateState(rp, this.weapon.getNewItemStack(rp)));
			if(!this.scroll.isEmpty()){
				this.getMenu().setItem(4, Utils.setGlowingWithoutAttributes(this.getMenu().getItem(4)));
			}
		}else this.getMenu().setItem(4, HELP.clone());
		failSlots.clear();
		int barriers = this.getBarrierAmount();
		for(int i = 0;i < (barriers > orderedSlots.length - 1 ? orderedSlots.length - 1 : barriers);i++){
			int r = orderedSlots[random.nextInt(orderedSlots.length - 1) + 1];
			while(failSlots.contains(r)){
				r = orderedSlots[random.nextInt(orderedSlots.length - 1) + 1];
			}
			failSlots.add(r);
			this.getMenu().setItem(r, FAILURE.clone());
		}
		for(int i : orderedSlots){
			if(!failSlots.contains(i)){
				this.menu.setItem(i, new ItemStack(Material.AIR));
			}
		}
		if(this.startStone){
			ItemStack startStone = RItemStacks.STAR_STONE.getItemStack();
			ItemMeta meta = startStone.getItemMeta();
			meta.setDisplayName("§f§lPierre étoile");
			List<String> lore = new ArrayList<String>();
			lore.addAll(Arrays.asList(("§7Durant le renforcement, cette pierre étoile"), ("§7va tourner autour de votre " + (this.weapon.isAttack() ? "arme" : "armure") + "."), ("§7Si elle s'arrête au dessus d'un item échec,"), ("§7le renforcement échoue et votre item est cassé à jamais."), ("§7Vous pouvez utiliser des parchemins appropriés pour l'éviter."), ""));
			if(!this.scroll.isEmpty()){
				lore.add("§6§l" + ("Parchemins actifs :"));
				for(Scroll scroll : this.scroll){
					lore.add("  §e" + (scroll.getType().getNameFr()));
				}
				lore.add("");
			}
			lore.add(("§fCliquez ici pour débuter le renforcement."));
			meta.setLore(lore);
			startStone.setItemMeta(meta);
			this.getMenu().setItem(5, startStone);
		}
	}
	
	private int getBarrierAmount(){
		int amount = 0;
		if(this.weapon != null){
			amount += weapon.getSuppLevel()+1;
			if(this.scroll != null){
				if(this.isScrollActive(ScrollType.ENHANCEMENT_REDUCTION_S)){
					amount -= 2;
				}
				if(this.isScrollActive(ScrollType.ENHANCEMENT_REDUCTION_A)){
					amount -= 1;
				}
				if(this.isScrollActive(ScrollType.ENHANCEMENT_AMPLIFICATION)){
					amount += 2;
				}
			}
		}
		return amount;
	}
	
	private boolean isScrollActive(ScrollType type){
		for(Scroll scroll : this.scroll){
			if(scroll.getType().equals(type)){
				return true;
			}
		}
		return false;
	}
}
