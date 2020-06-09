package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

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
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Rank;

public class GRankPrefsUI extends UIHandler {

	private final Rank rank;
	private int page_id;
	private int start_page;

	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_HOMES = new ItemStack(Material.COMPASS, 1);
	private ItemStack ITEM_CLAIMS = new ItemStack(Material.OAK_FENCE, 1);
	private ItemStack ITEM_INVITE = new ItemStack(Material.CAKE, 1);
	private ItemStack ITEM_BUILD = new ItemStack(Material.ENDER_CHEST, 1);
	private ItemStack ITEM_USEDOORS = new ItemStack(Material.SPRUCE_DOOR, 1);
	private ItemStack ITEM_USECHESTS = new ItemStack(Material.CHEST, 1);
	private ItemStack ITEM_NEXT = new ItemStack(Material.ARROW, 1);
	private ItemStack ITEM_NAME = new ItemStack(Material.PAPER, 1);
	private ItemStack ITEM_DESC = new ItemStack(Material.MAP, 1);
	private ItemStack ITEM_CLAIMSPREFS = new ItemStack(Material.DARK_OAK_FENCE, 1);
	private ItemStack ITEM_RANKSPREFS = new ItemStack(Material.LADDER, 1);
	private ItemStack ITEM_MEMBERSPREFS = new ItemStack(Material.PLAYER_HEAD, 1);
	private ItemStack ITEM_RELATIONS = new ItemStack(Material.CLOCK, 1);
	private ItemStack ITEM_BANK = new ItemStack(Material.ENDER_CHEST, 1);
	private ItemStack ITEM_OFFER = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
	private ItemStack ITEM_MOBSDAMAGE = new ItemStack(Material.EGG, 1);
	
	ItemStack ITEM_DISABLED = new ItemStack(Material.GRAY_DYE, 1);
	ItemStack ITEM_ENABLED = new ItemStack(Material.LIME_DYE, 1);
	ItemStack ITEM_INFO = new ItemStack(Material.MAGENTA_DYE, 1);

	private int SLOT_BACK = 0;
	private int SLOT_NEXT = 8;
	
	//PAGE 1
	private int SLOT_INVITE = 1;
	private int SLOT_CLAIMS = 2;
	private int SLOT_HOMES = 3;
	private int SLOT_BUILD = 5;
	private int SLOT_USEDOORS = 6;
	private int SLOT_USECHESTS = 7;
	
	//PAGE 2
	private int SLOT_NAME = 1;
	private int SLOT_DESC = 2;
	private int SLOT_DISPLAY = 3;
	private int SLOT_CLAIMSPREFS = 4;
	private int SLOT_RANKSPREFS = 5;
	private int SLOT_MEMBERSPREFS = 6;
	private int SLOT_DEFAULTRANK = 7;
	
	//PAGE 3
	private int SLOT_GVBANK = 2;
	private int SLOT_OFFER = 4;
	private int SLOT_MOBSDAMAGE = 6;
	private int SLOT_RELATIONS = 8;
	
	private Guild guild;
	public GRankPrefsUI(Player p, Guild guild, Rank rank, int page) {
		super(p);
		this.guild = guild;
		this.rank = rank;
		this.start_page = page;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(this.getGuild().getName() + " : " + ("Permissions des " + this.rank.getName().toLowerCase() + "s" + "s"),32));
		ItemMeta META = ITEM_DISABLED.getItemMeta();
		META.setDisplayName(("§e§lCliquez pour basculer"));
		ITEM_DISABLED.setItemMeta(META);
		ITEM_ENABLED.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour ouvrir"));
		ITEM_INFO.setItemMeta(META);
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_RANKS_PREFS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(this.page_id == 1){
			if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
				this.getUIManager().requestUI(new GRanksUI(this.getHolder(), this.getGuild()));
			}else if(slot == this.SLOT_NEXT || slot == this.SLOT_NEXT+9){
				this.setPage(2);
			}else if((gm.getPermission(Permission.RANK_PREFS) && gm.getRank().getId() < this.rank.getId()) || gm.isLeader() || rp.isOp()){
				if(slot == this.SLOT_CLAIMS || slot == this.SLOT_CLAIMS+9){
					this.rank.setPermission(Permission.CLAIM, !this.rank.getPermission(Permission.CLAIM));
					this.getMenu().setItem(this.SLOT_CLAIMS, this.getCanClaim());
					this.getMenu().setItem(this.SLOT_CLAIMS+9, this.rank.getPermission(Permission.CLAIM) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_INVITE || slot == this.SLOT_INVITE+9){
					this.rank.setPermission(Permission.INVITE, !this.rank.getPermission(Permission.INVITE));
					this.getMenu().setItem(this.SLOT_INVITE, this.getCanInvite());
					this.getMenu().setItem(this.SLOT_INVITE+9, this.rank.getPermission(Permission.INVITE) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_BUILD || slot == this.SLOT_BUILD+9){
					this.rank.setPermission(Permission.BUILD, !this.rank.getPermission(Permission.BUILD));
					this.getMenu().setItem(this.SLOT_BUILD, this.getCanBuild());
					this.getMenu().setItem(this.SLOT_BUILD+9, this.rank.getPermission(Permission.BUILD) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_USEDOORS || slot == this.SLOT_USEDOORS+9){
					this.rank.setPermission(Permission.USE_DOORS, !this.rank.getPermission(Permission.USE_DOORS));
					this.getMenu().setItem(this.SLOT_USEDOORS, this.getCanUseDoors());
					this.getMenu().setItem(this.SLOT_USEDOORS+9, this.rank.getPermission(Permission.USE_DOORS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_USECHESTS || slot == this.SLOT_USECHESTS+9){
					this.rank.setPermission(Permission.USE_CHESTS, !this.rank.getPermission(Permission.USE_CHESTS));
					this.getMenu().setItem(this.SLOT_USECHESTS, this.getCanUseChests());
					this.getMenu().setItem(this.SLOT_USECHESTS+9, this.rank.getPermission(Permission.USE_CHESTS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_HOMES || slot == this.SLOT_HOMES+9){
					Core.uiManager.requestUI(new HomePermissionsUI(this.getHolder(),this.getGuild(), this.rank, this.page_id));
				}
				this.rank.resetPermissions(this.getGuild());
			}else rp.sendMessage("§cVous ne pouvez modifier les permissions de ce rang !");
		}else if(this.page_id == 2){
			if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
				this.setPage(1);
			}else if(slot == this.SLOT_NEXT || slot == this.SLOT_NEXT+9){
				this.setPage(3);
			}else if((gm.getPermission(Permission.RANK_PREFS) && gm.getRank().getId() < this.rank.getId()) || gm.isLeader()){
				if(slot == this.SLOT_NAME || slot == this.SLOT_NAME+9){
					this.rank.setPermission(Permission.RENAME, !this.rank.getPermission(Permission.RENAME));
					this.getMenu().setItem(this.SLOT_NAME, this.getCanRename());
					this.getMenu().setItem(this.SLOT_NAME+9, this.rank.getPermission(Permission.RENAME) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_DESC || slot == this.SLOT_DESC+9){
					this.rank.setPermission(Permission.DESCRIPTION, !this.rank.getPermission(Permission.DESCRIPTION));
					this.getMenu().setItem(this.SLOT_DESC, this.getCanChangeDescription());
					this.getMenu().setItem(this.SLOT_DESC+9, this.rank.getPermission(Permission.DESCRIPTION) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_DISPLAY || slot == this.SLOT_DISPLAY+9){
					this.rank.setPermission(Permission.CAPE, !this.rank.getPermission(Permission.CAPE));
					this.getMenu().setItem(this.SLOT_DISPLAY, this.getCanModifyDisplay());
					this.getMenu().setItem(this.SLOT_DISPLAY+9, this.rank.getPermission(Permission.CAPE) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_CLAIMSPREFS || slot == this.SLOT_CLAIMSPREFS+9){
					this.rank.setPermission(Permission.CLAIM_PREFS, !this.rank.getPermission(Permission.CLAIM_PREFS));
					this.getMenu().setItem(this.SLOT_CLAIMSPREFS, this.getCanModifyClaimsPrefs());
					this.getMenu().setItem(this.SLOT_CLAIMSPREFS+9, this.rank.getPermission(Permission.CLAIM_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_RANKSPREFS || slot == this.SLOT_RANKSPREFS+9){
					this.rank.setPermission(Permission.RANK_PREFS, !this.rank.getPermission(Permission.RANK_PREFS));
					this.getMenu().setItem(this.SLOT_RANKSPREFS, this.getCanModifyRanksUnderPrefs());
					this.getMenu().setItem(this.SLOT_RANKSPREFS+9, this.rank.getPermission(Permission.RANK_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_MEMBERSPREFS || slot == this.SLOT_MEMBERSPREFS+9){
					this.rank.setPermission(Permission.MEMBER_PREFS, !this.rank.getPermission(Permission.MEMBER_PREFS));
					this.getMenu().setItem(this.SLOT_MEMBERSPREFS, this.getCanModifyPerMemberPrefs());
					this.getMenu().setItem(this.SLOT_MEMBERSPREFS+9, this.rank.getPermission(Permission.MEMBER_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_DEFAULTRANK || slot == this.SLOT_DEFAULTRANK+9){
					this.rank.setPermission(Permission.DEFAULT_RANK, !this.rank.getPermission(Permission.DEFAULT_RANK));
					this.getMenu().setItem(this.SLOT_DEFAULTRANK, this.getCanSetDefaultRank());
					this.getMenu().setItem(this.SLOT_DEFAULTRANK+9, this.rank.getPermission(Permission.DEFAULT_RANK) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}
				this.rank.resetPermissions(this.getGuild());
			}else rp.sendMessage("§cVous ne pouvez modifier les permissions de ce rang !");
		}else if(this.page_id == 3){
			if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
				this.setPage(2);
			}else if((gm.getPermission(Permission.RANK_PREFS) && gm.getRank().getId() < this.rank.getId()) || gm.isLeader()){
				if(slot == this.SLOT_GVBANK || slot == this.SLOT_GVBANK+9){
					this.rank.setPermission(Permission.BANK_DEPOSIT, !this.rank.getPermission(Permission.BANK_DEPOSIT));
					this.getMenu().setItem(this.SLOT_GVBANK, this.getCanGiveBank());
					this.getMenu().setItem(this.SLOT_GVBANK+9, this.rank.getPermission(Permission.BANK_DEPOSIT) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_OFFER || slot == this.SLOT_OFFER+9){
					this.rank.setPermission(Permission.OFFER, !this.rank.getPermission(Permission.OFFER));
					this.getMenu().setItem(this.SLOT_OFFER, this.getCanOffer());
					this.getMenu().setItem(this.SLOT_OFFER+9, this.rank.getPermission(Permission.OFFER) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_RELATIONS || slot == this.SLOT_RELATIONS+9){
					this.rank.setPermission(Permission.RELATIONS, !this.rank.getPermission(Permission.RELATIONS));
					this.getMenu().setItem(this.SLOT_RELATIONS, this.getCanManageRelations());
					this.getMenu().setItem(this.SLOT_RELATIONS+9, this.rank.getPermission(Permission.RELATIONS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}else if(slot == this.SLOT_MOBSDAMAGE || slot == this.SLOT_MOBSDAMAGE+9){
					this.rank.setPermission(Permission.DAMAGE_MOBS, !this.rank.getPermission(Permission.DAMAGE_MOBS));
					this.getMenu().setItem(this.SLOT_MOBSDAMAGE, this.getCanDamageMobs());
					this.getMenu().setItem(this.SLOT_MOBSDAMAGE+9, this.rank.getPermission(Permission.DAMAGE_MOBS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
				}
				this.rank.resetPermissions(this.getGuild());
			}else rp.sendMessage("§cVous ne pouvez modifier les permissions de ce rang !");
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		this.setPage(start_page);
		
		return this.getHolder().openInventory(this.getMenu()) != null;
	}
	
	private void setPage(int index){
		if(index == 1){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack());
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			getMenu().setItem(SLOT_NEXT, this.getNext(2));
			getMenu().setItem(SLOT_NEXT+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_CLAIMS, this.getCanClaim());
			getMenu().setItem(SLOT_CLAIMS+9, (this.rank.getPermission(Permission.CLAIM) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_BUILD, this.getCanBuild());
			getMenu().setItem(SLOT_BUILD+9, (this.rank.getPermission(Permission.BUILD) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_INVITE, this.getCanInvite());
			getMenu().setItem(SLOT_INVITE+9, (this.rank.getPermission(Permission.INVITE) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_USEDOORS, this.getCanUseDoors());
			getMenu().setItem(SLOT_USEDOORS+9, (this.rank.getPermission(Permission.USE_DOORS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_USECHESTS, this.getCanUseChests());
			getMenu().setItem(SLOT_USECHESTS+9, (this.rank.getPermission(Permission.USE_CHESTS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_HOMES, this.getHomes());
			getMenu().setItem(SLOT_HOMES+9, this.ITEM_INFO);
			
		}else if(index == 2){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack(1));
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			getMenu().setItem(SLOT_NEXT, this.getNext(3));
			getMenu().setItem(SLOT_NEXT+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_NAME, this.getCanRename());
			getMenu().setItem(SLOT_NAME+9, (this.rank.getPermission(Permission.RENAME) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_DESC, this.getCanChangeDescription());
			getMenu().setItem(SLOT_DESC+9, (this.rank.getPermission(Permission.DESCRIPTION) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_DISPLAY, this.getCanModifyDisplay());
			getMenu().setItem(SLOT_DISPLAY+9, (this.rank.getPermission(Permission.CAPE) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_CLAIMSPREFS, this.getCanModifyClaimsPrefs());
			getMenu().setItem(SLOT_CLAIMSPREFS+9, (this.rank.getPermission(Permission.CLAIM_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_RANKSPREFS, this.getCanModifyRanksUnderPrefs());
			getMenu().setItem(SLOT_RANKSPREFS+9, (this.rank.getPermission(Permission.RANK_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_MEMBERSPREFS, this.getCanModifyPerMemberPrefs());
			getMenu().setItem(SLOT_MEMBERSPREFS+9, (this.rank.getPermission(Permission.MEMBER_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_DEFAULTRANK, this.getCanSetDefaultRank());
			getMenu().setItem(SLOT_DEFAULTRANK+9, (this.rank.getPermission(Permission.DEFAULT_RANK) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
		}else if(index == 3){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack(2));
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_GVBANK, this.getCanGiveBank());
			getMenu().setItem(SLOT_GVBANK+9, (this.rank.getPermission(Permission.BANK_DEPOSIT) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_OFFER, this.getCanOffer());
			getMenu().setItem(SLOT_OFFER+9, (this.rank.getPermission(Permission.OFFER) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_RELATIONS, this.getCanManageRelations());
			getMenu().setItem(SLOT_RELATIONS+9, (this.rank.getPermission(Permission.RELATIONS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
			
			getMenu().setItem(SLOT_MOBSDAMAGE, this.getCanDamageMobs());
			getMenu().setItem(SLOT_MOBSDAMAGE+9, (this.rank.getPermission(Permission.DAMAGE_MOBS) ? this.ITEM_ENABLED : this.ITEM_DISABLED));
		}
		this.page_id = index;
	}
	
	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName(("§6§lListe des rangs"));
		META_BACK.setLore(Arrays.asList(("§7Retourner à la liste des rangs."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getBack(int index){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§lPage #" + index);
		META_BACK.setLore(Arrays.asList(("§7Retourner à la page #" + index + "."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getNext(int index){
		ItemMeta META = ITEM_NEXT.getItemMeta();
		META.setDisplayName("§6§lPage #" + index);
		META.setLore(Arrays.asList(("§7Aller à la page #" + index + "."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_NEXT.setItemMeta(META);
		return ITEM_NEXT;
	}
	private ItemStack getCanClaim(){
		ItemMeta META = ITEM_CLAIMS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.CLAIM) ? "§a§l" : "§c§l") + ("Permission de conquérir"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à gérer"), ("§7les territoires de votre guilde."), ("§7Inclut la permission de lancer un raid."), "", ("§e§lCliquez pour basculer")));
		ITEM_CLAIMS.setItemMeta(META);
		return ITEM_CLAIMS;
	}
	private ItemStack getCanBuild(){
		ItemMeta META = ITEM_BUILD.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.BUILD) ? "§a§l" : "§c§l") + ("Permission de construire"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à construire"), ("§7à l'intérieur du territoire."), "", ("§e§lCliquez pour basculer")));
		ITEM_BUILD.setItemMeta(META);
		return ITEM_BUILD;
	}
	private ItemStack getCanInvite(){
		ItemMeta META = ITEM_INVITE.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.INVITE) ? "§a§l" : "§c§l") + ("Permission d'invitation"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à inviter"), ("§7d'autres joueurs à rejoindre votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_INVITE.setItemMeta(META);
		return ITEM_INVITE;
	}
	private ItemStack getCanUseDoors(){
		ItemMeta META = ITEM_USEDOORS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.USE_DOORS) ? "§a§l" : "§c§l") + ("Permission d'intéraction"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à utiliser"), ("§7les portes, boutons, plaques de pression..."), "", ("§e§lCliquez pour basculer")));
		ITEM_USEDOORS.setItemMeta(META);
		return ITEM_USEDOORS;
	}
	private ItemStack getCanUseChests(){
		ItemMeta META = ITEM_USECHESTS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.USE_CHESTS) ? "§a§l" : "§c§l") + ("Permission d'utilisation du matériel"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à utiliser"), ("§7les coffres, fours, tables d'enchantement..."), ("§7Les coffres piégés seront §ntoujours§7 accessibles !"), "", ("§e§lCliquez pour basculer")));
		ITEM_USECHESTS.setItemMeta(META);
		return ITEM_USECHESTS;
	}
	private ItemStack getCanRename(){
		ItemMeta META = ITEM_NAME.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.RENAME) ? "§a§l" : "§c§l") + ("Permission de renommer"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s"), ("§7à renommer votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_NAME.setItemMeta(META);
		return ITEM_NAME;
	}
	private ItemStack getCanChangeDescription(){
		ItemMeta META = ITEM_DESC.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.DESCRIPTION) ? "§a§l" : "§c§l") + ("Permission de modification de description"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), ("§7la description de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_DESC.setItemMeta(META);
		return ITEM_DESC;
	}
	private ItemStack getCanModifyClaimsPrefs(){
		ItemMeta META = ITEM_CLAIMSPREFS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.CLAIM_PREFS) ? "§a§l" : "§c§l") + ("Permission de modification des préférences de territoire"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), ("§7les préférences de protection du territoire."), "", ("§e§lCliquez pour basculer")));
		ITEM_CLAIMSPREFS.setItemMeta(META);
		return ITEM_CLAIMSPREFS;
	}
	private ItemStack getCanModifyRanksUnderPrefs(){
		ItemMeta META = ITEM_RANKSPREFS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.RANK_PREFS) ? "§a§l" : "§c§l") + ("Permission de gestion des permissions de rangs inférieurs"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à gérer"), ("§7les permissions des rangs de position inférieure."), "", ("§e§lCliquez pour basculer")));
		ITEM_RANKSPREFS.setItemMeta(META);
		return ITEM_RANKSPREFS;
	}
	private ItemStack getCanModifyPerMemberPrefs(){
		ItemMeta META = ITEM_MEMBERSPREFS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.MEMBER_PREFS) ? "§a§l" : "§c§l") + ("Permission de gestion des permissions par membre"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), ("§7les permissions par membre de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_MEMBERSPREFS.setItemMeta(META);
		return ITEM_MEMBERSPREFS;
	}
	private ItemStack getCanModifyDisplay(){
		ItemStack ITEM_DISPLAY = this.getGuild().getCape();
		ItemMeta META = ITEM_DISPLAY.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.CAPE) ? "§a§l" : "§c§l") + ("Permission de modification de la cape de guilde"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), ("§7la cape de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_DISPLAY.setItemMeta(META);
		return ITEM_DISPLAY;
	}
	private ItemStack getHomes(){
		ItemMeta META = ITEM_HOMES.getItemMeta();
		META.setDisplayName("§6§l" + ("Permissions des points de rassemblement"));
		META.setLore(Arrays.asList(("§7Gérer les permissions par PR des " + this.rank.getName().toLowerCase() + "s."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_HOMES.setItemMeta(META);
		return ITEM_HOMES;
	}
	private ItemStack getCanSetDefaultRank(){
		ItemStack ITEM = this.getGuild().getDefaultRank().getItemStack();
		ItemMeta META = ITEM.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.DEFAULT_RANK) ? "§a§l" : "§c§l") + ("Permission de modification du rang par défaut"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), ("§7le rang par défaut de votre guilde."), "", ("§e§lCliquez pour basculer")));
		META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ITEM.setItemMeta(META);
		return ITEM;
	}
	private ItemStack getCanManageRelations(){
		ItemMeta META = ITEM_RELATIONS.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.RELATIONS) ? "§a§l" : "§c§l") + ("Permission de gestion des relations"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à modifier"), ("§7les relations de votre guilde (alliances/oppositions)."), "", ("§e§lCliquez pour basculer")));
		ITEM_RELATIONS.setItemMeta(META);
		return ITEM_RELATIONS;
	}
	private ItemStack getCanGiveBank(){
		ItemMeta META = ITEM_BANK.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.BANK_DEPOSIT) ? "§a§l" : "§c§l") + ("Permission de dépôt dans la banque"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à déposer"), ("§7des émeraudes dans la banque de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_BANK.setItemMeta(META);
		return ITEM_BANK;
	}
	private ItemStack getCanOffer(){
		ItemMeta META = ITEM_OFFER.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.OFFER) ? "§a§l" : "§c§l") + ("Permission d'offrande"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à faire"), ("§7des offrandes pour votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_OFFER.setItemMeta(META);
		return ITEM_OFFER;
	}
	private ItemStack getCanDamageMobs(){
		ItemMeta META = ITEM_MOBSDAMAGE.getItemMeta();
		META.setDisplayName((this.rank.getPermission(Permission.DAMAGE_MOBS) ? "§a§l" : "§c§l") + ("Permission de dégâts aux monstres apprivoisés"));
		META.setLore(Arrays.asList(("§7Autoriser les " + this.rank.getName().toLowerCase() + "s à infliger"), ("§7des dégâts aux monstres apprivoisés."), "", ("§e§lCliquez pour basculer")));
		ITEM_MOBSDAMAGE.setItemMeta(META);
		return ITEM_MOBSDAMAGE;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}
}
