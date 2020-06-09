package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.GHome;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GHomeListUI extends UIHandler {

	private Guild guild;
	
	private int SLOT_BACK = 8;
	
	public GHomeListUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getGuild().getName() + (" : liste des PR"), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_HOME_LIST_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK){
			Core.uiManager.requestUI(new GClaimsMenuUI(this.getHolder(), this.getGuild(), Claim.get(this.getHolder().getLocation())));
		}else{
			if(e.isLeftClick()){
				if(gm.canHome(slot)){
					GHome home = this.getGuild().getHomes()[slot];
					if(home != null){
						TeleportHandler.startTeleportation(rp, home.getLocation(), new RTeleportCause(RTeleportType.GUILD_HOME, null, null, null));
						this.close(false);
						return;
					}
				}else{
					rp.sendMessage("§cVous n'avez pas la permission d'utiliser les PR pour votre guilde !");
					return;
				}
				
				if(slot < this.getGuild().getLevel()){
					if(gm.canSetHome(slot)){
						this.close(true, slot);
						rp.sendMessage("§aDéplacez-vous vers le PR souhaité et prenez dans vos mains un item représentatif qui sera affiché dans la liste des PR de votre guilde. Enfin, entrez dans le chat le nom de votre PR.");
					}else rp.sendMessage("§cVous n'avez pas la permission de définir le PR #" + slot + " pour votre guilde !");
				}else rp.sendMessage("§cVous devez augmenter le niveau de votre guilde jusqu'au niveau §e" + (slot+1) + " §cpour débloquer ce PR.");
			}else{
				if(slot < this.getGuild().getLevel()){
					if(gm.canSetHome(slot)){
						if(this.getGuild().getHomes()[slot] != null) {
							Core.uiManager.requestUI(new HomeRemovalConfirmationUI(rp, this.getGuild(), slot));
						}
					}else rp.sendMessage("§cVous n'avez pas la permission de définir le PR #" + slot + " pour votre guilde !");
				}else rp.sendMessage("§cVous devez augmenter le niveau de votre guilde jusqu'au niveau §e" + (slot+1) + " §cpour débloquer ce PR.");
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				Location location = this.getHolder().getLocation();
				Claim claim = Claim.get(location);
				if(claim != null){
					if(claim.getGuild().equals(this.getGuild())){
						ItemStack item = this.getHolder().getInventory().getItemInMainHand().clone();
						if(item.getType().equals(Material.AIR))item = new ItemStack(Material.SLIME_BALL,1);
						item.setAmount(1);
						ItemMeta meta = item.getItemMeta();
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
						item.setItemMeta(meta);
						GHome home = new GHome(this.getListeningId(),
								this.getMessage(),
								location,
								item);
						this.getGuild().getHomes()[this.getListeningId()] = home;
					}else rp.sendMessage("§cVous ne pouvez définir de PR qu'à l'intérieur du territoire de votre guilde.");
				}else rp.sendMessage("§cVous ne pouvez définir de PR qu'à l'intérieur du territoire de votre guilde.");
			}
		}
		
		this.getMenu().clear();
		for(int i = 0;i < guild.getHomes().length;i++){
			GHome home = this.getGuild().getHomes()[i];
			ItemStack item = null;
			if(home != null){
				item = home.getDisplay();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§f§l" + home.getName());
				meta.setLore(Arrays.asList("§8§o" + ("Cliquez gauche pour utiliser"), "§8§o" + ("Cliquez droit pour supprimer")));
				item.setItemMeta(meta);
			}else{
				if(i < this.getGuild().getLevel()){
					item = this.getDefault(i);
				}else{
					item = this.getLocked();
				}
			}
			this.getMenu().setItem(i, item);
		}
		this.getMenu().setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	private ItemStack getBack(){
		ItemStack item = new ItemStack(Material.MELON, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Menu des territoires"));
		meta.setLore(Arrays.asList(("§7Retourner au menu principal."), "", ("§e§lCliquez pour ouvrir")));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getDefault(int index){
		ItemStack item = new ItemStack(Material.SLIME_BALL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f§l" + ("PR ") + "#" + index);
		meta.setLore(Arrays.asList("§8§o" + ("Cliquez pour définir")));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getLocked(){
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(("§c§lPR bloqué"));
		meta.setLore(Arrays.asList(("§8§oAugmentez le niveau de votre guilde")));
		item.setItemMeta(meta);
		return item;
	}
}
