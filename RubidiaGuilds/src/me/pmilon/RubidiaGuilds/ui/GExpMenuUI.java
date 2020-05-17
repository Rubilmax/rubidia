package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.utils.LevelUtils;

public class GExpMenuUI extends UIHandler {

	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_INFOS = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
	
	private Guild guild;
	
	private final int SLOT_BACK = 8;
	private final int SLOT_INFOS = 7;
	public GExpMenuUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getGuild().getName() + (" : Offrandes"), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_EXP_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, final Player p) {
		ItemStack item = e.getCurrentItem();
		if(item != null){
			if(e.isShiftClick()){
				if(!LevelUtils.loots.contains(item.getType()) && !item.getType().equals(Material.AIR)){
					e.setCancelled(true);
					rp.sendMessage("§cCe n'est pas une bonne offrande.");
				} else {
					new BukkitTask(GuildsPlugin.instance){

						@Override
						public void run(){
							menu.setItem(SLOT_INFOS, getInfos(getValue()));
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(0);
				}
			}
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, final Player p) {
		int slot = e.getRawSlot();
		if(slot < this.SLOT_INFOS){
			ItemStack cursor = e.getCursor();
			if(cursor != null) {
				if(!LevelUtils.loots.contains(cursor.getType()) && !cursor.getType().equals(Material.AIR)){
					e.setCancelled(true);
					rp.sendMessage("§cCe n'est pas une bonne offrande.");
				} else {
					new BukkitTask(GuildsPlugin.instance){

						@Override
						public void run(){
							menu.setItem(SLOT_INFOS, getInfos(getValue()));
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(0);
				}
			}
		}else{
			e.setCancelled(true);
			if(slot == this.SLOT_INFOS){
				if(gm.getPermission(Permission.OFFER)){
					int oldLevel = this.getGuild().getLevel();
					double value = this.getValue();
					this.getGuild().setExperience(this.getGuild().getExperience()+value);
					int coloredBars = (int) (30*(this.getGuild().getExperience()/LevelUtils.getLevelExperienceAmount(this.getGuild().getLevel())));
					String xpBar = "";
					for(int i = 0;i < 30;i++){
						if(i == 0){
							if(i < coloredBars)xpBar += "§4[";
							else xpBar += "§8[";
						}else if(i == 29){
							if(i < coloredBars)xpBar += "§4]";
							else xpBar += "§8]";
						}else{
							if(i < coloredBars)xpBar += "§c|";
							else xpBar += "§7|";
						}
					}
					int newLevel = this.getGuild().getLevel();
					this.getGuild().broadcastMessage(Relation.MEMBER, "§&d>>> §&cVotre guilde a gagné §&d" + value + " §&cpoint d'XP supplémentaire !");
					if(oldLevel < newLevel)this.getGuild().broadcastMessage(Relation.MEMBER, "§&d>>> §&cNiveau supérieur atteint ! §&d" + this.getGuild().getName() + " §&cest désormais niveau §&d" + newLevel + " §&c!");
					this.getGuild().broadcastMessage(Relation.MEMBER, "§&d>>> " + xpBar);
					
					for(int invSlot = 0;invSlot < 7;invSlot++){
						this.menu.setItem(invSlot, new ItemStack(Material.AIR));
					}
					this.menu.setItem(this.SLOT_INFOS, this.getInfos(this.getValue()));
				}else rp.sendMessage("§cVous n'avez pas la permission de faire des offrandes pour votre guilde !");
			}else if(slot == this.SLOT_BACK){
				this.empty();
				Core.uiManager.requestUI(new GInfosMenuUI(this.getHolder(), this.getGuild()));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player arg1) {
		this.empty();
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_INFOS, this.getInfos(0.0));
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	public void empty(){
		for(int slot = 0;slot < this.SLOT_INFOS;slot++){
			ItemStack stack = this.menu.getItem(slot);
			if(stack != null) {
				this.getHolder().getInventory().addItem(stack);
			}
		}
	}
	
	protected ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§l" + ("Menu des informations"));
		META_BACK.setLore(Arrays.asList(("§7Retourner au menu des informations."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	protected ItemStack getInfos(double exp){
		ItemMeta META_BACK = ITEM_INFOS.getItemMeta();
		META_BACK.setDisplayName("§a§l" + ("Confirmer l'offrande"));
		
		int level = this.getGuild().getLevel();
		double experience = this.getGuild().getExperience() + exp;
		while(experience > LevelUtils.getLevelExperienceAmount(level)){
			experience -= LevelUtils.getLevelExperienceAmount(level);
			level++;
		}
		double factor = (experience/LevelUtils.getLevelExperienceAmount(level));
		int coloredBars = (int) (30*factor);
		String xpBar = "";
		for(int i = 0;i < 30;i++){
			if(i == 0){
				if(i < coloredBars)xpBar += "§4[";
				else xpBar += "§8[";
			}else if(i == 29){
				if(i < coloredBars)xpBar += "§4]";
				else xpBar += "§8]";
			}else{
				if(i < coloredBars)xpBar += "§c|";
				else xpBar += "§7|";
			}
		}
		META_BACK.setLore(Arrays.asList(("§cTous vos items disparaîtront."), "", ("§6§lNiveau final : ") + "§e" + level + "     " + xpBar + "   ", "", ("§e§lCliquez pour confirmer")));
		ITEM_INFOS.setItemMeta(META_BACK);
		return ITEM_INFOS;
	}
	
	public double getValue(){//dont forget to add type to levelUtil.loots list
		double value = 0;
		for(int slot = 0;slot < 7;slot++){
			ItemStack stack = this.menu.getItem(slot);
			if(stack != null){
				if(stack.getType().equals(Material.ROTTEN_FLESH))value += .15*stack.getAmount();
				else if(stack.getType().equals(Material.STRING))value += .19*stack.getAmount();
				else if(stack.getType().equals(Material.GUNPOWDER))value += .23*stack.getAmount();
				else if(stack.getType().equals(Material.BONE))value += .18*stack.getAmount();
				else if(stack.getType().equals(Material.BLAZE_ROD))value += .34*stack.getAmount();
				else if(stack.getType().equals(Material.SPIDER_EYE))value += .2*stack.getAmount();
				else if(stack.getType().equals(Material.ENDER_PEARL))value += .27*stack.getAmount();
				//else if(stack.getType().equals(Material.NETHER_STAR))value += 1.82*stack.getAmount();
				else if(stack.getType().equals(Material.DRAGON_EGG))value += 5.23*stack.getAmount();
				else if(stack.getType().equals(Material.NETHER_WART))value += .01*stack.getAmount();
				else if(stack.getType().equals(Material.WHEAT))value += .008*stack.getAmount();
				else if(stack.getType().equals(Material.CARROT))value += .012*stack.getAmount();
				else if(stack.getType().equals(Material.MELON_SLICE))value += .005*stack.getAmount();
				else if(stack.getType().equals(Material.PUMPKIN))value += .015*stack.getAmount();
				else if(stack.getType().equals(Material.POTATO))value += .012*stack.getAmount();
				else if(stack.getType().equals(Material.MELON))value += .03*stack.getAmount();
				else if(stack.getType().equals(Material.SUGAR_CANE))value += .01*stack.getAmount();
			}
		}
		return value;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

}
