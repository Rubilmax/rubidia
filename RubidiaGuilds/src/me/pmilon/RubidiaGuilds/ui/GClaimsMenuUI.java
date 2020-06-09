package me.pmilon.RubidiaGuilds.ui;

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
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.claims.Claims;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;

public class GClaimsMenuUI extends UIHandler {

	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_CLAIMER = new ItemStack(Material.CHEST, 1);
	private ItemStack ITEM_CLAIMNAME = new ItemStack(Material.MAP, 1);
	private ItemStack ITEM_HOMES = new ItemStack(Material.COMPASS, 1);
	private ItemStack ITEM_BUILDABLE = new ItemStack(Material.ENDER_CHEST, 1);
	private ItemStack ITEM_DOORS_USABLE = new ItemStack(Material.SPRUCE_DOOR, 1);
	private ItemStack ITEM_CHESTS_USABLE = new ItemStack(Material.CHEST, 1);
	private ItemStack ITEM_MOBS_DAMAGEABLE = new ItemStack(Material.EGG, 1);
	
	private ItemStack ITEM_DISABLED = new ItemStack(Material.GRAY_DYE, 1);
	private ItemStack ITEM_ENABLED = new ItemStack(Material.LIME_DYE, 1);
	private ItemStack ITEM_INFO = new ItemStack(Material.MAGENTA_DYE, 1);
	private ItemStack ITEM_CLAIMED;
	private ItemStack ITEM_NOT_CLAIMED;
	private ItemStack ITEM_SET = new ItemStack(Material.MAGENTA_DYE, 1);
	private ItemStack ITEM_NOTSET = new ItemStack(Material.GRAY_DYE, 1);
	private ItemStack ITEM_UNAVAILABLE = new ItemStack(Material.BARRIER, 1);
	private ItemStack ITEM_STARTRAID = new ItemStack(Material.GOLDEN_SWORD, 1);
	
	private int SLOT_BACK = 0;
	private int SLOT_CLAIMER = 1;
	private int SLOT_CLAIMNAME = 2;
	private int SLOT_HOMES = 3;
	private int SLOT_BUILDABLE = 5;
	private int SLOT_DOORSUSABLE = 6;
	private int SLOT_CHESTSUSABLE = 7;
	private int SLOT_MOBSDAMAGE = 8;
	
	private int LIST_ID_CLAIM = 1;

	private Guild guild;
	private Claim claim;
	public GClaimsMenuUI(Player p, Guild guild, Claim claim) {
		super(p);
		this.guild = guild;
		this.claim = claim;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getGuild().getName() + (" : Menu des territoires"), 32));
		
		ItemMeta META = ITEM_DISABLED.getItemMeta();
		META.setDisplayName(("§e§lCliquez pour basculer"));
		ITEM_DISABLED.setItemMeta(META);
		ITEM_ENABLED.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour ouvrir"));
		ITEM_INFO.setItemMeta(META);
		ITEM_CLAIMED = ITEM_ENABLED.clone();
		ITEM_NOT_CLAIMED = ITEM_DISABLED.clone();
		META.setDisplayName(("§e§lCliquez pour céder"));
		ITEM_CLAIMED.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour conquérir"));
		ITEM_NOT_CLAIMED.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour redéfinir"));
		ITEM_SET.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour définir"));
		ITEM_NOTSET.setItemMeta(META);
		META.setDisplayName(("§c§lNon disponible"));
		ITEM_UNAVAILABLE.setItemMeta(META);
		META.setDisplayName(("§6§lLancer une offensive"));
		META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ITEM_STARTRAID.setItemMeta(META);
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_PREFS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9)Core.uiManager.requestUI(new GMenuUI(this.getHolder(), this.getGuild()));
		else if(slot == this.SLOT_CLAIMER || slot == this.SLOT_CLAIMER+9){
			Claims.manageClaim("claim_" + this.getGuild().getClaims().size(), this.getGuild(), this.getHolder().getLocation(), gm);
			this.setClaim(Claim.get(this.getHolder().getLocation()));
			
			this.getMenu().setItem(this.SLOT_CLAIMER, this.getClaimer());
			this.getMenu().setItem(this.SLOT_CLAIMER+9, this.getClaim() != null ? (this.getGuild().equals(this.getClaim().getGuild()) ? this.ITEM_CLAIMED : this.ITEM_STARTRAID) : this.ITEM_NOT_CLAIMED);
			
			this.getMenu().setItem(this.SLOT_CLAIMNAME, this.getClaimName());
			this.getMenu().setItem(this.SLOT_CLAIMNAME+9, this.getClaim() != null ? (this.getGuild().equals(this.getClaim().getGuild()) ? (this.getClaim().getName().contains("claim_") ? this.ITEM_NOTSET : this.ITEM_SET) : this.ITEM_UNAVAILABLE) : this.ITEM_UNAVAILABLE);
		}else if(slot == this.SLOT_CLAIMNAME || slot == this.SLOT_CLAIMNAME+9){
			if(this.getClaim() != null){
				if(this.getClaim().getGuild().equals(this.getGuild())){
					if(gm.getPermission(Permission.CLAIM)){
						this.close(true, this.LIST_ID_CLAIM);
					}else rp.sendMessage("§cVous n'avez pas la permission de gérer les territoires de votre guilde !");
				}else rp.sendMessage("§cCe territoire n'appartient pas à votre guilde !");
			}else rp.sendMessage("§cCe territoire n'est pas conquis !");
		}else if(slot == this.SLOT_HOMES || slot == this.SLOT_HOMES+9){
			Core.uiManager.requestUI(new GHomeListUI(this.getHolder(), this.getGuild()));
		}else if(gm.getPermission(Permission.CLAIM_PREFS)){
			if(slot == this.SLOT_BUILDABLE || slot == this.SLOT_BUILDABLE+9){
				this.getGuild().setClaimBuildable(!this.getGuild().isClaimBuildable());
				this.getMenu().setItem(this.SLOT_BUILDABLE, this.getBuildable());
				this.getMenu().setItem(this.SLOT_BUILDABLE+9, !this.getGuild().isClaimBuildable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			}else if(slot == this.SLOT_DOORSUSABLE || slot == this.SLOT_DOORSUSABLE+9){
				this.getGuild().setClaimDoorsUsable(!this.getGuild().isClaimDoorsUsable());
				this.getMenu().setItem(this.SLOT_DOORSUSABLE, this.getDoorsUsable());
				this.getMenu().setItem(this.SLOT_DOORSUSABLE+9, !this.getGuild().isClaimDoorsUsable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			}else if(slot == this.SLOT_CHESTSUSABLE || slot == this.SLOT_CHESTSUSABLE+9){
				this.getGuild().setClaimChestsUsable(!this.getGuild().isClaimChestsUsable());
				this.getMenu().setItem(this.SLOT_CHESTSUSABLE, this.getChestsUsable());
				this.getMenu().setItem(this.SLOT_CHESTSUSABLE+9, !this.getGuild().isClaimChestsUsable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			}else if(slot == this.SLOT_MOBSDAMAGE || slot == this.SLOT_MOBSDAMAGE+9){
				this.getGuild().setClaimMobsDamageable(!this.getGuild().isClaimMobsDamageable());
				this.getMenu().setItem(this.SLOT_MOBSDAMAGE, this.getMobsDamageable());
				this.getMenu().setItem(this.SLOT_MOBSDAMAGE+9, !this.getGuild().isClaimMobsDamageable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			}
		}else rp.sendMessage("§cVous n'avez pas la permission de modifier les préférences de territoire de votre guilde !");
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_CLAIM){
					this.getClaim().setName(ChatColor.stripColor(this.getMessage()));
				}
			}
		}
		
		this.getMenu().setItem(this.SLOT_BACK, this.getBack());
		this.getMenu().setItem(this.SLOT_BACK+9, this.ITEM_INFO);
		
		this.getMenu().setItem(this.SLOT_BUILDABLE, this.getBuildable());
		this.getMenu().setItem(this.SLOT_BUILDABLE+9, !this.getGuild().isClaimBuildable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
		
		this.getMenu().setItem(this.SLOT_DOORSUSABLE, this.getDoorsUsable());
		this.getMenu().setItem(this.SLOT_DOORSUSABLE+9, !this.getGuild().isClaimDoorsUsable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
		
		this.getMenu().setItem(this.SLOT_CHESTSUSABLE, this.getChestsUsable());
		this.getMenu().setItem(this.SLOT_CHESTSUSABLE+9, !this.getGuild().isClaimChestsUsable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
		
		this.getMenu().setItem(this.SLOT_MOBSDAMAGE, this.getMobsDamageable());
		this.getMenu().setItem(this.SLOT_MOBSDAMAGE+9, !this.getGuild().isClaimMobsDamageable() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
		
		this.getMenu().setItem(this.SLOT_CLAIMER, this.getClaimer());
		this.getMenu().setItem(this.SLOT_CLAIMER+9, this.getClaim() != null ? (this.getGuild().equals(this.getClaim().getGuild()) ? this.ITEM_CLAIMED : this.ITEM_STARTRAID) : this.ITEM_NOT_CLAIMED);
		
		this.getMenu().setItem(this.SLOT_CLAIMNAME, this.getClaimName());
		this.getMenu().setItem(this.SLOT_CLAIMNAME+9, this.getClaim() != null ? (this.getGuild().equals(this.getClaim().getGuild()) ? (this.getClaim().getName().contains("claim_") ? this.ITEM_NOTSET : this.ITEM_SET) : this.ITEM_UNAVAILABLE) : this.ITEM_UNAVAILABLE);
		
		this.getMenu().setItem(this.SLOT_HOMES, this.getHomes());
		this.getMenu().setItem(this.SLOT_HOMES+9, this.ITEM_INFO);
		
		return this.getHolder().openInventory(getMenu()) != null;
	}

	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§l" + ("Menu principal"));
		META_BACK.setLore(Arrays.asList(("§7Retourner au menu principal."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getClaimer(){
		ItemMeta META_CLAIMER = ITEM_CLAIMER.getItemMeta();
		META_CLAIMER.setDisplayName((this.getClaim() != null ? (this.getGuild().equals(this.getClaim().getGuild()) ? "§a§l" : "§c§l") : "§a§l") + ("Conquérir le territoire"));
		META_CLAIMER.setLore(Arrays.asList(("§7Conquérir ou céder le territoire sur lequel vous vous trouvez."), ("§6§lTerritoires : §e") + this.getGuild().getClaims().size(), ("§6§lConquêtes disponibles : ") + "§e" + (this.getGuild().getLevel()-this.getGuild().getClaims().size()), "", ("§e§lCliquez pour conquérir")));
		ITEM_CLAIMER.setItemMeta(META_CLAIMER);
		return ITEM_CLAIMER;
	}
	private ItemStack getClaimName(){
		ItemMeta META_CLAIMNAME = ITEM_CLAIMNAME.getItemMeta();
		META_CLAIMNAME.setDisplayName((this.getClaim() != null ? (this.getGuild().equals(this.getClaim().getGuild()) ? "§a§l" : "§c§l") : "§a§l") + ("Nommer le territoire"));
		META_CLAIMNAME.setLore(Arrays.asList(("§7Nommer le territoire conquis dans lequel vous êtes."), ("§7Ce nom sera affiché aux membres et aux alliés."), ("§6§lNom du territoire : ") + "§e" + (this.getClaim() != null ? this.getClaim().getName() : "non conquis"), "", ("§e§lCliquez pour nommer")));
		ITEM_CLAIMNAME.setItemMeta(META_CLAIMNAME);
		return ITEM_CLAIMNAME;
	}
	private ItemStack getBuildable(){
		ItemMeta META_BUILDABLE = ITEM_BUILDABLE.getItemMeta();
		META_BUILDABLE.setDisplayName((this.getGuild().isClaimBuildable() ? "§c§l" : "§a§l") + ("Protection de construction"));
		META_BUILDABLE.setLore(Arrays.asList(("§7Interdire les joueurs non membres à construire sur votre territoire."), "", ("§e§lCliquez pour basculer")));
		ITEM_BUILDABLE.setItemMeta(META_BUILDABLE);
		return ITEM_BUILDABLE;
	}
	private ItemStack getDoorsUsable(){
		ItemMeta META_BUILDABLE = ITEM_DOORS_USABLE.getItemMeta();
		META_BUILDABLE.setDisplayName((this.getGuild().isClaimDoorsUsable() ? "§c§l" : "§a§l") + ("Protection d'intéraction"));
		META_BUILDABLE.setLore(Arrays.asList(("§7Interdire les joueurs non membres à intéragir sur votre territoire."), ("§7(Applicable sur les portes, boutons, plaques de pression...)"), "", ("§e§lCliquez pour basculer")));
		ITEM_DOORS_USABLE.setItemMeta(META_BUILDABLE);
		return ITEM_DOORS_USABLE;
	}
	private ItemStack getChestsUsable(){
		ItemMeta META_BUILDABLE = ITEM_CHESTS_USABLE.getItemMeta();
		META_BUILDABLE.setDisplayName((this.getGuild().isClaimChestsUsable() ? "§c§l" : "§a§l") + ("Protection d'utilisation du matériel"));
		META_BUILDABLE.setLore(Arrays.asList(("§7Interdire les joueurs non membres à utiliser votre matériel."), ("§7(Applicable sur les coffres, fours, tables d'enchantement...)"), "", ("§e§lCliquez pour basculer")));
		ITEM_CHESTS_USABLE.setItemMeta(META_BUILDABLE);
		return ITEM_CHESTS_USABLE;
	}
	private ItemStack getMobsDamageable(){
		ItemMeta META_BUILDABLE = ITEM_MOBS_DAMAGEABLE.getItemMeta();
		META_BUILDABLE.setDisplayName((this.getGuild().isClaimMobsDamageable() ? "§c§l" : "§a§l") + ("Protection de dégâts aux monstres apprivoisés"));
		META_BUILDABLE.setLore(Arrays.asList(("§7Interdire les joueurs non membres à endommager vos monstres apprivoisés."), ("§7(Applicable sur n'importe quel monstre en laisse)"), "", ("§e§lCliquez pour basculer")));
		ITEM_MOBS_DAMAGEABLE.setItemMeta(META_BUILDABLE);
		return ITEM_MOBS_DAMAGEABLE;
	}
	private ItemStack getHomes(){
		ItemMeta META_BUILDABLE = ITEM_HOMES.getItemMeta();
		META_BUILDABLE.setDisplayName("§6§l" + ("Liste des PR de guilde"));
		META_BUILDABLE.setLore(Arrays.asList(("§7Ouvrir le menu des PR de guilde pour en définir/utiliser."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_HOMES.setItemMeta(META_BUILDABLE);
		return ITEM_HOMES;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

}
