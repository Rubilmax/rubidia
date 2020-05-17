package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PrefsUI extends UIHandler {

	protected int page_id;

	static ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	static ItemStack ITEM_NEXT = new ItemStack(Material.ARROW, 1);
	static ItemStack ITEM_CHATSETTINGS = new ItemStack(Material.MAP, 1);
	static ItemStack ITEM_CHAT = new ItemStack(Material.SHULKER_SHELL, 1);
	static ItemStack ITEM_COMBAT = new ItemStack(Material.SKELETON_SKULL, 1);
	static ItemStack ITEM_NOTIFJOIN = new ItemStack(Material.PLAYER_HEAD, 1);
	static ItemStack ITEM_CLICKSOUND = new ItemStack(Material.DISPENSER, 1);
	static ItemStack ITEM_INVOCATION = new ItemStack(Material.BLAZE_POWDER, 1);
	static ItemStack ITEM_TELEPORTATION = new ItemStack(Material.ENDER_PEARL, 1);
	static ItemStack ITEM_MUSIC = new ItemStack(Material.NOTE_BLOCK, 1);
	static ItemStack ITEM_CYCLE = new ItemStack(Material.MAGMA_CREAM, 1);
	
	static ItemStack ITEM_DISABLED = new ItemStack(Material.GRAY_DYE, 1);
	static ItemStack ITEM_ENABLED = new ItemStack(Material.LIME_DYE, 1);
	static ItemStack ITEM_INFO = new ItemStack(Material.MAGENTA_DYE);
	static ItemStack ITEM_CHOOSE = new ItemStack(Material.MAGENTA_DYE);
	static ItemStack ITEM_RANKINFO = new ItemStack(Material.PINK_DYE, 1);

	static int SLOT_BACK = 0;
	static int SLOT_NEXT = 8;

	//PAGE 1
	static int SLOT_NOTIFJOIN = 0;
	static int SLOT_COMBAT = 1;
	static int SLOT_CLICKSOUND = 2;
	static int SLOT_INVOCATION = 4;
	static int SLOT_TELEPORTATION = 5;
	static int SLOT_MUSIC = 7;
	
	//PAGE 2
	static int SLOT_CYCLE = 1;
	static int SLOT_CHATSETTINGS = 5;
	static int SLOT_CHAT = 7;
	
	public PrefsUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 18, ("Préférences"));
		ItemMeta META = ITEM_DISABLED.getItemMeta();
		META.setDisplayName(("§e§lCliquez pour basculer"));
		ITEM_DISABLED.setItemMeta(META);
		ITEM_ENABLED.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour ouvrir"));
		ITEM_INFO.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour cycler"));
		ITEM_RANKINFO.setItemMeta(META);
		META.setDisplayName(("§e§lCliquez pour choisir"));
		ITEM_CHOOSE.setItemMeta(META);
	}

	@Override
	public String getType() {
		return "PREFS_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.setPage(1);
		
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		int slot = e.getRawSlot();
		ItemStack is = e.getCurrentItem();
		e.setCancelled(true);
		if(is != null){
			if(!is.getType().equals(Material.AIR)){
				if(this.page_id == 1){
					if(slot == SLOT_NEXT || slot == SLOT_NEXT+9){
						this.setPage(2);
					}else if(slot == SLOT_NOTIFJOIN || slot == SLOT_NOTIFJOIN+9){
						rp.setNotifOnFriendJoin(!rp.getNotifOnFriendJoin());
						this.getMenu().setItem(SLOT_NOTIFJOIN, this.getNotifOnFriendJoin());
						this.getMenu().setItem(SLOT_NOTIFJOIN+9, rp.getNotifOnFriendJoin() ? ITEM_ENABLED : ITEM_DISABLED);
					}else if(slot == SLOT_CLICKSOUND || slot == SLOT_CLICKSOUND+9){
						rp.setClickSound(!rp.getClickSound());
						this.getMenu().setItem(SLOT_CLICKSOUND, this.getClickSound());
						this.getMenu().setItem(SLOT_CLICKSOUND+9, rp.getClickSound() ? ITEM_ENABLED : ITEM_DISABLED);
					}else if(slot == SLOT_COMBAT || slot == SLOT_COMBAT+9){
						rp.setCombatLevel(rp.getCombatLevel()+1);
						if(rp.getCombatLevel() > 3)rp.setCombatLevel(0);
						getMenu().setItem(SLOT_COMBAT, this.getCombatLevel());
						getMenu().setItem(SLOT_COMBAT+9, rp.getCombatLevel() > 0 ? ITEM_RANKINFO : ITEM_DISABLED);
					}else if(slot == SLOT_INVOCATION || slot == SLOT_INVOCATION+9){
						rp.setWouldLikeInvocation(!rp.getWouldLikeInvocation());
						this.getMenu().setItem(SLOT_INVOCATION, this.getInvocation());
						this.getMenu().setItem(SLOT_INVOCATION+9, rp.getWouldLikeInvocation() ? ITEM_ENABLED : ITEM_DISABLED);
					}else if(slot == SLOT_TELEPORTATION || slot == SLOT_TELEPORTATION+9){
						rp.setWouldLikeTeleportation(!rp.getWouldLikeTeleportation());
						this.getMenu().setItem(SLOT_TELEPORTATION, this.getTeleportation());
						this.getMenu().setItem(SLOT_TELEPORTATION+9, rp.getWouldLikeTeleportation() ? ITEM_ENABLED : ITEM_DISABLED);
					}else if(slot == SLOT_MUSIC || slot == SLOT_MUSIC+9){
						rp.setMusic(!rp.getMusic());
						//TODO remove music with AudioMc
						this.getMenu().setItem(SLOT_MUSIC, this.getMusic());
						this.getMenu().setItem(SLOT_MUSIC+9, rp.getMusic() ? ITEM_ENABLED : ITEM_DISABLED);
					}
				}else if(this.page_id == 2){
					if(slot == SLOT_BACK || slot == SLOT_BACK+9){
						this.setPage(1);
					}else if(slot == SLOT_NEXT || slot == SLOT_NEXT+9){
						this.setPage(3);
					}else if(slot == SLOT_CHATSETTINGS || slot == SLOT_CHATSETTINGS+9){
						this.close(false);
						rp.getChat().setEditMode(true);
						rp.getChat().update();
					}else if(slot == SLOT_CYCLE || slot == SLOT_CYCLE+9){
						rp.setUsingCycle(!rp.isUsingCycle());
						this.getMenu().setItem(SLOT_CYCLE, this.getCycle());
						this.getMenu().setItem(SLOT_CYCLE+9, rp.isUsingCycle() ? ITEM_ENABLED : ITEM_DISABLED);
					}else if(slot == SLOT_CHAT || slot == SLOT_CHAT+9){
						rp.setUsingChat(!rp.isUsingChat());
						rp.getChat().setUsed(rp.isUsingChat());
						rp.getChat().update();
						this.getMenu().setItem(SLOT_CHAT, this.getChat());
						this.getMenu().setItem(SLOT_CHAT+9, rp.isUsingChat() ? ITEM_ENABLED : ITEM_DISABLED);
					}
				}
			}
		}
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		//not listening
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	private void setPage(int index){
		if(index == 1){
			this.getMenu().clear();
			getMenu().setItem(SLOT_NEXT, this.getNext());
			getMenu().setItem(SLOT_NEXT+9, ITEM_INFO);
			
			getMenu().setItem(SLOT_NOTIFJOIN, this.getNotifOnFriendJoin());
			getMenu().setItem(SLOT_NOTIFJOIN+9, rp.getNotifOnFriendJoin() ? ITEM_ENABLED : ITEM_DISABLED);
			
			getMenu().setItem(SLOT_CLICKSOUND, this.getClickSound());
			getMenu().setItem(SLOT_CLICKSOUND+9, rp.getClickSound() ? ITEM_ENABLED : ITEM_DISABLED);
			
			getMenu().setItem(SLOT_INVOCATION, this.getInvocation());
			getMenu().setItem(SLOT_INVOCATION+9, rp.getWouldLikeInvocation() ? ITEM_ENABLED : ITEM_DISABLED);
			
			getMenu().setItem(SLOT_TELEPORTATION, this.getTeleportation());
			getMenu().setItem(SLOT_TELEPORTATION+9, rp.getWouldLikeTeleportation() ? ITEM_ENABLED : ITEM_DISABLED);
			
			getMenu().setItem(SLOT_COMBAT, this.getCombatLevel());
			getMenu().setItem(SLOT_COMBAT+9, rp.getCombatLevel() > 0 ? ITEM_RANKINFO : ITEM_DISABLED);
			
			getMenu().setItem(SLOT_MUSIC, this.getMusic());
			getMenu().setItem(SLOT_MUSIC+9, rp.getMusic() ? ITEM_ENABLED : ITEM_DISABLED);
		}else if(index == 2){
			getMenu().clear();
			getMenu().setItem(SLOT_BACK, this.getBack());
			getMenu().setItem(SLOT_BACK+9, ITEM_INFO);
			
			getMenu().setItem(SLOT_CYCLE, this.getCycle());
			getMenu().setItem(SLOT_CYCLE+9, rp.isUsingCycle() ? ITEM_ENABLED : ITEM_DISABLED);
			
			getMenu().setItem(SLOT_CHATSETTINGS, this.getChatSize());
			getMenu().setItem(SLOT_CHATSETTINGS+9, ITEM_CHOOSE);
			
			getMenu().setItem(SLOT_CHAT, this.getChat());
			getMenu().setItem(SLOT_CHAT+9, rp.isUsingChat() ? ITEM_ENABLED : ITEM_DISABLED);
		}
		this.page_id = index;
	}

	private ItemStack getNotifOnFriendJoin(){
		SkullMeta META_NOTIFJOIN = (SkullMeta) ITEM_NOTIFJOIN.getItemMeta();
		META_NOTIFJOIN.setDisplayName((rp.getNotifOnFriendJoin() ? "§a" : "§c") + ("§lNotification de connexion"));
		META_NOTIFJOIN.setLore(Arrays.asList(("§7Soyez notifié de la connexion d'un équipier"), ("§7par un doux son et un message !"), "", ("§e§lCliquez pour basculer")));
		META_NOTIFJOIN.setOwningPlayer(Bukkit.getOfflinePlayer(this.getHolder().getUniqueId()));
		ITEM_NOTIFJOIN.setItemMeta(META_NOTIFJOIN);
		return ITEM_NOTIFJOIN;
	}
	private ItemStack getClickSound(){
		ItemMeta META_INVOCATION = ITEM_CLICKSOUND.getItemMeta();
		META_INVOCATION.setDisplayName((rp.getClickSound() ? "§a" : "§c") + ("§lSon de clic"));
		META_INVOCATION.setLore(Arrays.asList(("§7Activez ou non le son de clic - rapidement"), ("§7ennuyant - produit lorsque vous cliquez avec"), ("§7votre item de classe."), "", ("§e§lCliquez pour basculer")));
		ITEM_CLICKSOUND.setItemMeta(META_INVOCATION);
		return ITEM_CLICKSOUND;
	}
	private ItemStack getInvocation(){
		ItemMeta META_INVOCATION = ITEM_INVOCATION.getItemMeta();
		META_INVOCATION.setDisplayName((rp.getWouldLikeInvocation() ? "§a" : "§c") + "§lInvocation");
		META_INVOCATION.setLore(Arrays.asList(("§7Activez ou non votre invocation par parchemin."), ("§7Si désactivé, vous ne serez pas visible"), ("§7dans la liste des joueurs invocables."), "", ("§e§lCliquez pour basculer")));
		ITEM_INVOCATION.setItemMeta(META_INVOCATION);
		return ITEM_INVOCATION;
	}
	private ItemStack getTeleportation(){
		ItemMeta META_TELEPORTATION = ITEM_TELEPORTATION.getItemMeta();
		META_TELEPORTATION.setDisplayName((rp.getWouldLikeTeleportation() ? "§a" : "§c") + ("§lTéléportation"));
		META_TELEPORTATION.setLore(Arrays.asList(("§7Activez ou non la téléportation à vous par parchemin."), ("§7Si désactivé, vous ne serez pas visible"), ("§7dans la liste des joueurs cibles."), "", ("§e§lCliquez pour basculer")));
		ITEM_TELEPORTATION.setItemMeta(META_TELEPORTATION);
		return ITEM_TELEPORTATION;
	}
	private ItemStack getMusic(){
		ItemMeta META_MUSIC = ITEM_MUSIC.getItemMeta();
		META_MUSIC.setDisplayName((rp.getMusic() ? "§a" : "§c") + "§l" + ("Musique"));
		META_MUSIC.setLore(Arrays.asList(("§7Activer ou désactiver la musique des lieux communs."), "", ("§e§lCliquez pour basculer")));
		ITEM_MUSIC.setItemMeta(META_MUSIC);
		return ITEM_MUSIC;
	}
	private ItemStack getNext() {
		ItemMeta META_CHATSETTINGS = ITEM_NEXT.getItemMeta();
		META_CHATSETTINGS.setDisplayName(("§6§lParamètres du Chat"));
		META_CHATSETTINGS.setLore(Arrays.asList(("§7Choisissez quels messages afficher et lesquels cacher !"), "", ("§e§lCliquez pour ouvrir")));
		ITEM_NEXT.setItemMeta(META_CHATSETTINGS);
		return ITEM_NEXT;
	}
	private ItemStack getCombatLevel() {
		ItemMeta META_BLOOD = ITEM_COMBAT.getItemMeta();
		META_BLOOD.setDisplayName((rp.getCombatLevel() == 0 ? "§c" : rp.getCombatLevel() == 1 ? "§6" : "§a") + "§l" + ("Ambiance de combat"));
		META_BLOOD.setLore(Arrays.asList(("§7Cyclez parmis les différents niveaux d'ambiance de combat :"), (rp.getCombatLevel() == 0 ? "§8>>" : "§7-") + " 0 :: " + ("Aucune ambiance"), (rp.getCombatLevel() == 1 ? "§8>>" : "§7-") + " 1 :: " + ("Barre de vie"), (rp.getCombatLevel() == 2 ? "§8>>" : "§7-") + " 2 :: " + ("Dégâts dynamiques"), (rp.getCombatLevel() == 3 ? "§8>>" : "§7-") + " 3 :: " + ("Sang"), "", ("§e§lCliquez pour cycler")));
		ITEM_COMBAT.setItemMeta(META_BLOOD);
		return ITEM_COMBAT;
	}
	private ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName(("§6§lPréférences"));
		META_BACK.setLore(Arrays.asList(("§7Retourner au menu des préférences."), "", ("§e§lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	private ItemStack getChatSize(){
		ItemMeta META_CHAT = ITEM_CHATSETTINGS.getItemMeta();
		META_CHAT.setDisplayName("§6§l" + ("Taille du chat"));
		META_CHAT.setLore(Arrays.asList(("§7Modifiez la taille de la chatbox en fonction"), ("§7de vos paramètres de discussion minecraft."), "", ("§e§lCliquez pour choisir")));
		ITEM_CHATSETTINGS.setItemMeta(META_CHAT);
		return ITEM_CHATSETTINGS;
	}
	private ItemStack getChat(){
		ItemMeta META_CHAT = ITEM_CHAT.getItemMeta();
		META_CHAT.setDisplayName("§6§l" + ("Chat personnalisé"));
		META_CHAT.setLore(Arrays.asList(("§7Activez ou désactivez notre chat personnalisé."), "", ("§e§lCliquez pour basculer")));
		ITEM_CHAT.setItemMeta(META_CHAT);
		return ITEM_CHAT;
	}
	private ItemStack getCycle(){
		ItemMeta META_CYCLE = ITEM_CYCLE.getItemMeta();
		META_CYCLE.setDisplayName("§6§l" + ("Cycle d'inventaire"));
		META_CYCLE.setLore(Arrays.asList(("§7Activez ou désactivez le cycle d'inventaire"), ("§7au survol du dernier slot de la hotbar."), "", ("§e§lCliquez pour basculer")));
		ITEM_CYCLE.setItemMeta(META_CYCLE);
		return ITEM_CYCLE;
	}
	
}
