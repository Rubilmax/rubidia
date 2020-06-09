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
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Rank;

public class GMemberPrefsUI extends UIHandler {

	private final GMember subject;
	private int page_id;
	private int start_page;
	
	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_KICK = new ItemStack(Material.BARRIER, 1);
	private ItemStack ITEM_CLAIMS = new ItemStack(Material.OAK_FENCE, 1);
	private ItemStack ITEM_HOMES = new ItemStack(Material.COMPASS, 1);
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
	private ItemStack ITEM_LEADERSHIP = new ItemStack(Material.REDSTONE_BLOCK, 1);
	private ItemStack ITEM_BANK = new ItemStack(Material.ENDER_CHEST, 1);
	private ItemStack ITEM_OFFER = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
	private ItemStack ITEM_MOBSDAMAGE = new ItemStack(Material.EGG, 1);
	
	ItemStack ITEM_DISABLED = new ItemStack(Material.GRAY_DYE, 1);
	ItemStack ITEM_RANKINFO = new ItemStack(Material.PINK_DYE, 1);
	ItemStack ITEM_ENABLED = new ItemStack(Material.LIME_DYE, 1);
	ItemStack ITEM_INFO = new ItemStack(Material.MAGENTA_DYE, 1);
	
	private int SLOT_BACK = 0;
	private int SLOT_NEXT = 8;
	
	//PAGE 1
	private int SLOT_RANK = 1;
	private int SLOT_NAME = 2;
	private int SLOT_DESC = 3;
	private int SLOT_DISPLAY = 4;
	private int SLOT_GVBANK = 6;
	private int SLOT_OFFER = 7;
	
	//PAGE 2
	private int SLOT_INVITE = 1;
	private int SLOT_RELATIONS = 2;
	private int SLOT_CLAIMS = 3;
	private int SLOT_HOMES = 4;
	private int SLOT_CLAIMSPREFS = 6;
	private int SLOT_DEFAULTRANK = 7;
	
	//PAGE 3
	private int SLOT_BUILD = 1;
	private int SLOT_USEDOORS = 2;
	private int SLOT_USECHESTS = 3;
	private int SLOT_MOBSDAMAGE = 4;
	private int SLOT_RANKSPREFS = 5;
	private int SLOT_MEMBERSPREFS = 6;
	private int SLOT_KICK = 7;
	private int SLOT_LEADERSHIP = 8;
	
	private Guild guild;
	public GMemberPrefsUI(Player p, Guild guild, GMember subject, int page) {
		super(p);
		this.guild = guild;
		this.subject = subject;
		this.start_page = page;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate(subject.getName()+ " : Permissions", 32));
		ItemMeta META = ITEM_DISABLED.getItemMeta();
		META.setDisplayName(("§e§lCliquez pour basculer"));
		ITEM_DISABLED.setItemMeta(META);
		ITEM_ENABLED.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour ouvrir"));
		ITEM_INFO.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour cycler"));
		ITEM_RANKINFO.setItemMeta(META);
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_MEMBERS_PREFS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(this.getGuild().equals(subject.getGuild())){
			if(this.page_id == 1){
				if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9)this.getUIManager().requestUI(new GMembersUI(this.getHolder(), this.getGuild()));
				else if(slot == this.SLOT_NEXT || slot == this.SLOT_NEXT+9){
					this.setPage(2);
				}else if(gm.getPermission(Permission.MEMBER_PREFS) || gm.isLeader()){
					if(!subject.isLeader() || gm.isLeader() || rp.isOp()){
						if(slot == this.SLOT_RANK || slot == this.SLOT_RANK+9){
							if(!subject.isLeader()){
								Rank rank = null;
								for(int i = subject.getRankId()+1;i < this.getGuild().getRanks().length;i++){
									Rank rk = this.getGuild().getRanks()[i];
									if(rk != null){
										rank = rk;
										break;
									}
								}
								if(rank == null){
									for(int i = 1;i < subject.getRankId();i++){
										Rank rk = this.getGuild().getRanks()[i];
										if(rk != null){
											rank = rk;
											break;
										}
									}
								}
								if(rank != null)subject.setRank(rank);
								this.setPage(1);
							}else rp.sendMessage("§cVous ne pouvez modifier le rang du chef de cette façon !");
						}else if(slot == this.SLOT_NAME || slot == this.SLOT_NAME+9){
							subject.setPermission(Permission.RENAME, !subject.getPermission(Permission.RENAME));
							getMenu().setItem(SLOT_NAME, this.getCanRename());
							getMenu().setItem(SLOT_NAME+9, subject.getPermission(Permission.RENAME) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_DESC || slot == this.SLOT_DESC+9){
							subject.setPermission(Permission.DESCRIPTION, !subject.getPermission(Permission.DESCRIPTION));
							getMenu().setItem(SLOT_DESC, this.getCanChangeDescription());
							getMenu().setItem(SLOT_DESC+9, subject.getPermission(Permission.DESCRIPTION) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_DISPLAY || slot == this.SLOT_DISPLAY+9){
							subject.setPermission(Permission.CAPE, !subject.getPermission(Permission.CAPE));
							getMenu().setItem(SLOT_DISPLAY, this.getCanModifyDisplay());
							getMenu().setItem(SLOT_DISPLAY+9, subject.getPermission(Permission.CAPE) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_GVBANK || slot == this.SLOT_GVBANK+9){
							subject.setPermission(Permission.BANK_DEPOSIT, !subject.getPermission(Permission.BANK_DEPOSIT));
							getMenu().setItem(SLOT_GVBANK, this.getCanGiveBank());
							getMenu().setItem(SLOT_GVBANK+9, subject.getPermission(Permission.BANK_DEPOSIT) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_OFFER || slot == this.SLOT_OFFER+9){
							subject.setPermission(Permission.OFFER, !subject.getPermission(Permission.OFFER));
							getMenu().setItem(SLOT_OFFER, this.getCanOffer());
							getMenu().setItem(SLOT_OFFER+9, subject.getPermission(Permission.OFFER) ? ITEM_ENABLED : ITEM_DISABLED);
						}
					}else rp.sendMessage("§cVous ne pouvez modifier les permissions du chef !");
				}else rp.sendMessage("§cVous n'avez pas la permission de modifier les préférences par membre de votre guilde !");
			}else if(this.page_id == 2){
				if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
					this.setPage(1);
				}else if(slot == this.SLOT_NEXT || slot == this.SLOT_NEXT+9){
					this.setPage(3);
				}else if(gm.getPermission(Permission.MEMBER_PREFS) || gm.isLeader()){
					if(!subject.isLeader() || gm.isLeader() || rp.isOp()){
						if(slot == this.SLOT_CLAIMS || slot == this.SLOT_CLAIMS+9){
							subject.setPermission(Permission.CLAIM, !subject.getPermission(Permission.CLAIM));
							getMenu().setItem(SLOT_CLAIMS, this.getCanClaim());
							getMenu().setItem(SLOT_CLAIMS+9, subject.getPermission(Permission.CLAIM) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_INVITE || slot == this.SLOT_INVITE+9){
							subject.setPermission(Permission.INVITE, !subject.getPermission(Permission.INVITE));
							getMenu().setItem(SLOT_INVITE, this.getCanInvite());
							getMenu().setItem(SLOT_INVITE+9, subject.getPermission(Permission.INVITE) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_HOMES || slot == this.SLOT_HOMES+9){
							Core.uiManager.requestUI(new HomePermissionsUI(this.getHolder(),this.getGuild(), subject, this.page_id));
						}else if(slot == this.SLOT_RELATIONS || slot == this.SLOT_RELATIONS+9){
							subject.setPermission(Permission.RELATIONS, !subject.getPermission(Permission.RELATIONS));
							getMenu().setItem(SLOT_RELATIONS, this.getCanManageRelations());
							getMenu().setItem(SLOT_RELATIONS+9, subject.getPermission(Permission.RELATIONS) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_DEFAULTRANK || slot == this.SLOT_DEFAULTRANK+9){
							subject.setPermission(Permission.DEFAULT_RANK, !subject.getPermission(Permission.DEFAULT_RANK));
							getMenu().setItem(SLOT_DEFAULTRANK, this.getCanSetDefaultRank());
							getMenu().setItem(SLOT_DEFAULTRANK+9, subject.getPermission(Permission.DEFAULT_RANK) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_CLAIMSPREFS || slot == this.SLOT_CLAIMSPREFS+9){
							subject.setPermission(Permission.CLAIM_PREFS, !subject.getPermission(Permission.CLAIM_PREFS));
							getMenu().setItem(SLOT_CLAIMSPREFS, this.getCanModifyClaimsPrefs());
							getMenu().setItem(SLOT_CLAIMSPREFS+9, subject.getPermission(Permission.CLAIM_PREFS) ? ITEM_ENABLED : ITEM_DISABLED);
						}
					}else rp.sendMessage("§cVous ne pouvez modifier les permissions du chef !");
				}else rp.sendMessage("§cVous n'avez pas la permission de modifier les préférences par membre de votre guilde !");
			}else if(this.page_id == 3){
				if(slot == this.SLOT_BACK || slot == this.SLOT_BACK+9){
					this.setPage(2);
				}else if(gm.getPermission(Permission.MEMBER_PREFS) || gm.isLeader()){
					if(!subject.isLeader() || gm.isLeader() || rp.isOp()){
						if(slot == this.SLOT_RANKSPREFS || slot == this.SLOT_RANKSPREFS+9){
							subject.setPermission(Permission.RANK_PREFS, !subject.getPermission(Permission.RANK_PREFS));
							getMenu().setItem(SLOT_RANKSPREFS, this.getCanModifyRanksUnderPrefs());
							getMenu().setItem(SLOT_RANKSPREFS+9, subject.getPermission(Permission.RANK_PREFS) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_MEMBERSPREFS || slot == this.SLOT_MEMBERSPREFS+9){
							subject.setPermission(Permission.MEMBER_PREFS, !subject.getPermission(Permission.MEMBER_PREFS));
							getMenu().setItem(SLOT_MEMBERSPREFS, this.getCanModifyPerMemberPrefs());
							getMenu().setItem(SLOT_MEMBERSPREFS+9, subject.getPermission(Permission.MEMBER_PREFS) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_BUILD || slot == this.SLOT_BUILD+9){
							subject.setPermission(Permission.BUILD, !subject.getPermission(Permission.BUILD));
							getMenu().setItem(SLOT_BUILD, this.getCanBuild());
							getMenu().setItem(SLOT_BUILD+9, subject.getPermission(Permission.BUILD) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_USEDOORS || slot == this.SLOT_USEDOORS+9){
							subject.setPermission(Permission.USE_DOORS, !subject.getPermission(Permission.USE_DOORS));
							getMenu().setItem(SLOT_USEDOORS, this.getCanUseDoors());
							getMenu().setItem(SLOT_USEDOORS+9, subject.getPermission(Permission.USE_DOORS) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_USECHESTS || slot == this.SLOT_USECHESTS+9){
							subject.setPermission(Permission.USE_CHESTS, !subject.getPermission(Permission.USE_CHESTS));
							getMenu().setItem(SLOT_USECHESTS, this.getCanUseChests());
							getMenu().setItem(SLOT_USECHESTS+9, subject.getPermission(Permission.USE_CHESTS) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_MOBSDAMAGE || slot == this.SLOT_MOBSDAMAGE+9){
							subject.setPermission(Permission.DAMAGE_MOBS, !subject.getPermission(Permission.DAMAGE_MOBS));
							getMenu().setItem(SLOT_MOBSDAMAGE, this.getCanDamageMobs());
							getMenu().setItem(SLOT_MOBSDAMAGE+9, subject.getPermission(Permission.DAMAGE_MOBS) ? ITEM_ENABLED : ITEM_DISABLED);
						}else if(slot == this.SLOT_KICK || slot == this.SLOT_KICK+9){
							if(!subject.isLeader()){
								Core.uiManager.requestUI(new KickConfirmationUI(rp, this.getGuild(), subject));
							}else rp.sendMessage("§cVous ne pouvez éjecter le chef de guilde !");
						}else if(slot == this.SLOT_LEADERSHIP || slot == this.SLOT_LEADERSHIP+9){
							if(gm.isLeader() || rp.isOp()){
								if(!this.getGuild().getLeader().equals(subject)){
									Core.uiManager.requestUI(new LeadLegacyConfirmationUI(rp, this.getGuild(), subject));
								}else rp.sendMessage("§4" + subject.getName() + " §cest déjà chef de la guilde !");
							}else rp.sendMessage("§cSeul le leader peut faire ça !");
						}
					}else rp.sendMessage("§cVous ne pouvez modifier les permissions du leader !");
				}else rp.sendMessage("§cVous n'avez pas la permission de modifier les préférences par membre de votre guilde !");
			}
		}else{
			this.close(false);
			rp.sendMessage("§4" + subject.getName() + " §cn'appartient plus à votre guilde !");
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//not listening
	}

	@Override
	protected boolean openWindow() {
		this.setPage(start_page);

		return this.getHolder().openInventory(getMenu()) != null;
	}
	
	private void setPage(int index){
		if(index == 1){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack());
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			getMenu().setItem(SLOT_NEXT, this.getNext());
			getMenu().setItem(SLOT_NEXT+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_RANK, this.getRank());
			getMenu().setItem(SLOT_RANK+9, this.ITEM_RANKINFO);
			
			getMenu().setItem(SLOT_NAME, this.getCanRename());
			getMenu().setItem(SLOT_NAME+9, subject.getPermission(Permission.RENAME) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_DESC, this.getCanChangeDescription());
			getMenu().setItem(SLOT_DESC+9, subject.getPermission(Permission.DESCRIPTION) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_DISPLAY, this.getCanModifyDisplay());
			getMenu().setItem(SLOT_DISPLAY+9, subject.getPermission(Permission.CAPE) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_GVBANK, this.getCanGiveBank());
			getMenu().setItem(SLOT_GVBANK+9, subject.getPermission(Permission.BANK_DEPOSIT) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_OFFER, this.getCanOffer());
			getMenu().setItem(SLOT_OFFER+9, subject.getPermission(Permission.OFFER) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
		}else if(index == 2){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack2());
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			getMenu().setItem(SLOT_NEXT, this.getNext());
			getMenu().setItem(SLOT_NEXT+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_CLAIMS, this.getCanClaim());
			getMenu().setItem(SLOT_CLAIMS+9, subject.getPermission(Permission.CLAIM) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_HOMES, this.getHomes());
			getMenu().setItem(SLOT_HOMES+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_CLAIMSPREFS, this.getCanModifyClaimsPrefs());
			getMenu().setItem(SLOT_CLAIMSPREFS+9, subject.getPermission(Permission.CLAIM_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_DEFAULTRANK, this.getCanSetDefaultRank());
			getMenu().setItem(SLOT_DEFAULTRANK+9, subject.getPermission(Permission.DEFAULT_RANK) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_INVITE, this.getCanInvite());
			getMenu().setItem(SLOT_INVITE+9, subject.getPermission(Permission.INVITE) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_RELATIONS, this.getCanManageRelations());
			getMenu().setItem(SLOT_RELATIONS+9, subject.getPermission(Permission.RELATIONS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
		}else if(index == 3){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack2());
			getMenu().setItem(SLOT_BACK+9, this.ITEM_INFO);
			
			getMenu().setItem(SLOT_RANKSPREFS, this.getCanModifyRanksUnderPrefs());
			getMenu().setItem(SLOT_RANKSPREFS+9, subject.getPermission(Permission.RANK_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_MEMBERSPREFS, this.getCanModifyPerMemberPrefs());
			getMenu().setItem(SLOT_MEMBERSPREFS+9, subject.getPermission(Permission.MEMBER_PREFS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_BUILD, this.getCanBuild());
			getMenu().setItem(SLOT_BUILD+9, subject.getPermission(Permission.BUILD) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_USEDOORS, this.getCanUseDoors());
			getMenu().setItem(SLOT_USEDOORS+9, subject.getPermission(Permission.USE_DOORS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_USECHESTS, this.getCanUseChests());
			getMenu().setItem(SLOT_USECHESTS+9, subject.getPermission(Permission.USE_CHESTS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_MOBSDAMAGE, this.getCanDamageMobs());
			getMenu().setItem(SLOT_MOBSDAMAGE+9, subject.getPermission(Permission.DAMAGE_MOBS) ? this.ITEM_ENABLED : this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_KICK, this.getKick());
			getMenu().setItem(SLOT_KICK+9, this.ITEM_DISABLED);
			
			getMenu().setItem(SLOT_LEADERSHIP, this.getLeadership());
			getMenu().setItem(SLOT_LEADERSHIP+9, subject.isLeader() ? this.ITEM_ENABLED : this.ITEM_DISABLED);
		}
		this.page_id = index;
	}
	
	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName(("§6§lListe des membres"));
		META_BACK.setLore(Arrays.asList(("§7Retourner à la liste des membres."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getBack2(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("§6§lPage 1");
		META_BACK.setLore(Arrays.asList(("§7Retourner à la page 1."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getNext(){
		ItemMeta META = ITEM_NEXT.getItemMeta();
		META.setDisplayName("§6§lPage 2");
		META.setLore(Arrays.asList(("§7Aller à la page 2."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_NEXT.setItemMeta(META);
		return ITEM_NEXT;
	}
	private ItemStack getRank(){
		ItemStack ITEM_RANK = subject.getRank().getItemStack();
		ItemMeta META = ITEM_RANK.getItemMeta();
		META.setDisplayName("§6§l" + subject.getRank().getName());
		META.setLore(Arrays.asList(("§7Cycler parmis les 4 différents rangs que " + subject.getName() + " peut avoir."), "", ("§e§lCliquez pour cycler")));
		META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ITEM_RANK.setItemMeta(META);
		return ITEM_RANK;
	}
	private ItemStack getKick(){
		ItemMeta META = ITEM_KICK.getItemMeta();
		META.setDisplayName("§c§l" + ("Ejecter " + subject.getName()));
		META.setLore(Arrays.asList(("§7Ejecter " + subject.getName() + " de la guilde."), "", ("§e§lCliquez pour éjecter")));
		ITEM_KICK.setItemMeta(META);
		return ITEM_KICK;
	}
	private ItemStack getCanClaim(){
		ItemMeta META_CLAIMS = ITEM_CLAIMS.getItemMeta();
		META_CLAIMS.setDisplayName((subject.getPermission(Permission.CLAIM) ? "§a§l" : "§c§l") + ("Permission de conquérir"));
		META_CLAIMS.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à gérer"), ("§7les territoires de votre guilde."), ("§7Inclut la permission de lancer un raid."), "", ("§e§lCliquez pour basculer")));
		ITEM_CLAIMS.setItemMeta(META_CLAIMS);
		return ITEM_CLAIMS;
	}
	private ItemStack getCanBuild(){
		ItemMeta META = ITEM_BUILD.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.BUILD) ? "§a§l" : "§c§l") + ("Permission de construire"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à construire"), ("§7à l'intérieur du territoire de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_BUILD.setItemMeta(META);
		return ITEM_BUILD;
	}
	private ItemStack getCanInvite(){
		ItemMeta META = ITEM_INVITE.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.INVITE) ? "§a§l" : "§c§l") + ("Permission d'invitation"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à inviter"), ("§7d'autres joueurs à rejoindre votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_INVITE.setItemMeta(META);
		return ITEM_INVITE;
	}
	private ItemStack getCanUseDoors(){
		ItemMeta META = ITEM_USEDOORS.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.USE_DOORS) ? "§a§l" : "§c§l") + ("Permission d'intéraction"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à utiliser"), ("§7les portes, boutons, plaques de pression..."), "", ("§e§lCliquez pour basculer")));
		ITEM_USEDOORS.setItemMeta(META);
		return ITEM_USEDOORS;
	}
	private ItemStack getCanUseChests(){
		ItemMeta META = ITEM_USECHESTS.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.USE_CHESTS) ? "§a§l" : "§c§l") + ("Permission d'utilisation du matériel"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à utiliser"), ("§7les coffres, fours, tables d'enchantement..."), ("§7Les coffres piégés seront §ntoujours§7 accessibles !"), "", ("§e§lCliquez pour basculer")));
		ITEM_USECHESTS.setItemMeta(META);
		return ITEM_USECHESTS;
	}
	private ItemStack getCanDamageMobs(){
		ItemMeta META = ITEM_MOBSDAMAGE.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.DAMAGE_MOBS) ? "§a§l" : "§c§l") + ("Permission de dégâts aux monstres apprivoisés"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à infliger des dégâts"), ("§7à un monstre apprivoisé."), "", ("§e§lCliquez pour basculer")));
		ITEM_MOBSDAMAGE.setItemMeta(META);
		return ITEM_MOBSDAMAGE;
	}
	private ItemStack getCanRename(){
		ItemMeta META = ITEM_NAME.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.RENAME) ? "§a§l" : "§c§l") + ("Permission de modification du nom"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à renommer votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_NAME.setItemMeta(META);
		return ITEM_NAME;
	}
	private ItemStack getCanChangeDescription(){
		ItemMeta META = ITEM_DESC.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.DESCRIPTION) ? "§a§l" : "§c§l") + ("Permission de modification de description"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à changer"), ("§7la description de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_DESC.setItemMeta(META);
		return ITEM_DESC;
	}
	private ItemStack getCanModifyClaimsPrefs(){
		ItemMeta META = ITEM_CLAIMSPREFS.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.CLAIM_PREFS) ? "§a§l" : "§c§l") + ("Permission de modification des préférences de territoire"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à modifier"), ("§7les préférences de protection du territoire."), "", ("§e§lCliquez pour basculer")));
		ITEM_CLAIMSPREFS.setItemMeta(META);
		return ITEM_CLAIMSPREFS;
	}
	private ItemStack getCanModifyRanksUnderPrefs(){
		ItemMeta META = ITEM_RANKSPREFS.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.RANK_PREFS) ? "§a§l" : "§c§l") + ("Permission de gestion des permissions des rangs inférieurs"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à gérer"), ("§7les permissions des rangs de position inférieure."), "", ("§e§lCliquez pour basculer")));
		ITEM_RANKSPREFS.setItemMeta(META);
		return ITEM_RANKSPREFS;
	}
	private ItemStack getCanModifyPerMemberPrefs(){
		ItemMeta META = ITEM_MEMBERSPREFS.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.MEMBER_PREFS) ? "§a§l" : "§c§l") + ("Permission de modification des préférences par membre"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à modifier"), ("§7les préférences par membre de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_MEMBERSPREFS.setItemMeta(META);
		return ITEM_MEMBERSPREFS;
	}
	private ItemStack getCanModifyDisplay(){
		ItemStack ITEM_DISPLAY = this.getGuild().getCape();
		ItemMeta META = ITEM_DISPLAY.getItemMeta();
		META.setDisplayName((subject.getPermission(Permission.CAPE) ? "§a§l" : "§c§l") + ("Permission de modification de la cape de guilde"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à modifier"), ("§7la cape de votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_DISPLAY.setItemMeta(META);
		return ITEM_DISPLAY;
	}
	private ItemStack getLeadership(){
		ItemMeta META = ITEM_LEADERSHIP.getItemMeta();
		META.setDisplayName("§4§l" + ("Léguer la direction"));
		META.setLore(Arrays.asList(("§cLéguer la direction à " + subject.getName()), "", ("§e§lCliquez pour basculer")));
		ITEM_LEADERSHIP.setItemMeta(META);
		return ITEM_LEADERSHIP;
	}
	private ItemStack getHomes(){
		ItemMeta META = ITEM_HOMES.getItemMeta();
		META.setDisplayName("§6§l" + ("Permissions des points de rassemblement"));
		META.setLore(Arrays.asList(("§7Gérer les permissions par PR de " + subject.getName() + "."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_HOMES.setItemMeta(META);
		return ITEM_HOMES;
	}
	private ItemStack getCanSetDefaultRank(){
		ItemStack ITEM = this.getGuild().getDefaultRank().getItemStack();
		ItemMeta META = ITEM.getItemMeta();
		META.setDisplayName((gm.getPermission(Permission.DEFAULT_RANK) ? "§a§l" : "§c§l") + ("Permission de modification du rang par défaut"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à modifier"), ("§7le rang par défaut de votre guilde."), "", ("§e§lCliquez pour basculer")));
		META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ITEM.setItemMeta(META);
		return ITEM;
	}
	private ItemStack getCanManageRelations(){
		ItemMeta META = ITEM_RELATIONS.getItemMeta();
		META.setDisplayName((gm.getPermission(Permission.RELATIONS) ? "§a§l" : "§c§l") + ("Permission de gestion des relations"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à gérer"), ("§7les relations de votre guilde (alliances/oppositions)."), "", ("§e§lCliquez pour basculer")));
		ITEM_RELATIONS.setItemMeta(META);
		return ITEM_RELATIONS;
	}
	private ItemStack getCanGiveBank(){
		ItemStack item = ITEM_BANK.clone();
		ItemMeta META = item.getItemMeta();
		META.setDisplayName((gm.getPermission(Permission.BANK_DEPOSIT) ? "§a§l" : "§c§l") + ("Permission de dépôt dans la banque"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à déposer"), ("§7des émeraudes dans la banque de votre guilde."), "", ("§e§lCliquez pour basculer")));
		item.setItemMeta(META);
		return item;
	}
	private ItemStack getCanOffer(){
		ItemMeta META = ITEM_OFFER.getItemMeta();
		META.setDisplayName((gm.getPermission(Permission.OFFER) ? "§a§l" : "§c§l") + ("Permission d'offrande"));
		META.setLore(Arrays.asList(("§7Autoriser " + subject.getName() + " à faire"), ("§7des offrandes pour votre guilde."), "", ("§e§lCliquez pour basculer")));
		ITEM_OFFER.setItemMeta(META);
		return ITEM_OFFER;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}
	
}
