package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.duels.RDuel;
import me.pmilon.RubidiaCore.handlers.TradingHandler;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.ui.GRelationsUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerMenu extends UIHandler {

	private final Player from;
	private final RPlayer rpfrom;
	private final GMember mfrom;
	
	private int SLOT_DUEL = 0;
	private int SLOT_TRADE = 2;
	private int SLOT_INVITE = 4;
	private int SLOT_INSPECT = 6;
	private int SLOT_MARRY = 8;
	
	private boolean showFiancer = true;
	public PlayerMenu(Player holder, Player from) {
		super(holder);
		this.from = from;
		this.rpfrom = RPlayer.get(from);
		this.mfrom = GMember.get(from);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, ("Menu de " + from.getName()));
		if(rp.getCouple() != null){
			if(!rp.getCouple().getCompanion1().equals(rpfrom) && !rp.getCouple().getCompanion2().equals(rpfrom)){
				this.showFiancer = false;
				this.SLOT_TRADE++;
				this.SLOT_INVITE++;
				this.SLOT_INSPECT+=2;
			}
		}
	}

	@Override
	public String getType() {
		return "PLAYER_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(SLOT_DUEL, this.getDuel());
		this.menu.setItem(SLOT_TRADE, this.getTrade());
		this.menu.setItem(SLOT_INVITE, this.getInvite());
		this.menu.setItem(SLOT_INSPECT, this.getInspect());
		if(this.showFiancer)this.menu.setItem(SLOT_MARRY, this.getMarry());
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		if(e.getRawSlot() == SLOT_DUEL){
			int deviation = Math.abs(rp.getRLevel()-this.rpfrom.getRLevel());
			if(deviation <= Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX && System.currentTimeMillis()-rp.getLastCompetitiveDuelDateAgainst(rpfrom) > Settings.COMPETITIVE_DUEL_DELAY_MAX*60*1000L*Math.pow(deviation/Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX,1.8) && rpfrom.getRequestedDuelTo(rp) == null && rp.getRequestedDuelTo(rpfrom) == null)Core.uiManager.requestUI(new DuelCompetitiveUI(this.rp, this.rpfrom));
			else{
				rp.requestDuel(this.rpfrom,false);
				this.getMenu().setItem(this.SLOT_DUEL, this.getDuel());
				if(rpfrom.getRequestedDuelTo(rp) != null)this.close(false);
			}
		}else if(e.getRawSlot() == SLOT_TRADE){
			TradingHandler.requestTrade(this.getHolder(), this.from);
			this.close(false);
		}else if(e.getRawSlot() == SLOT_INSPECT)Core.uiManager.requestUI(new InspectUI(this.getHolder(), this.from));
		else if(e.getRawSlot() == SLOT_INVITE){
			if(mfrom.hasGuild()){
				if(!gm.hasGuild())mfrom.getGuild().ask(gm);
				else if(!mfrom.getGuild().equals(gm.getGuild()))Core.uiManager.requestUI(new GRelationsUI(this.getHolder(), mfrom.getGuild()));
			}else if(gm.hasGuild()){
				if(gm.getPermission(Permission.INVITE) || gm.isLeader()){
					gm.getGuild().invite(mfrom);
					if(mfrom.isInvited(gm.getGuild()))rp.sendMessage("§eVous avez invité §6" + mfrom.getName() + " §eà rejoindre votre guilde.");
					else rp.sendMessage("§eVous avez annulé l'invitation à rejoindre votre guilde envoyée à §6" + mfrom.getName() + "§e.");
				}else rp.sendMessage("§cVous n'avez pas la permission d'inviter des joueurs à rejoindre votre guilde !");
			}else rp.sendMessage("§cVous n'appartenez à aucune guilde !");
			this.getMenu().setItem(this.SLOT_INVITE, this.getInvite());
		}else if(e.getRawSlot() == SLOT_MARRY){
			if(rp.getCouple() != null && this.showFiancer){
				Core.uiManager.requestUI(new DivorceConfirmationUI(rp));
			}else{
				if(rpfrom.equals(rp.fiance)){
					rp.fiance(null);
					rpfrom.fiance(null);
					rp.sendMessage("§cVous avez annulé votre demande en mariage...");
					rpfrom.sendMessage("§4" + rp.getName() + " §ca annulé la demande en mariage...");
				}else{
					if(rp.isOp() || rp.getLastDivorce()+Settings.TIME_BEFORE_WEDDING_PROPOSAL <= System.currentTimeMillis()){
						rp.fiance(rpfrom);
						if(rp.equals(rpfrom.fiance)){
							rp.sendMessage("§aVous êtes désormais fiancé(e) à §2" + rpfrom.getName() + " §a!");
							rpfrom.sendMessage("§aVous êtes désormais fiancé(e) à §2" + rp.getName() + " §a!");
							
							rp.sendMessage("§eVous avez maintenant besoin de l'aide d'un pasteur pour organiser votre mariage !");
							rpfrom.sendMessage("§eVous avez désormais besoin de demander à un pasteur d'organiser votre mariage !");
						}else{
							rp.sendMessage("§eVous avez demandé §6" + rpfrom.getName() + " §een mariage...");
							rpfrom.sendMessage("§6" + rp.getName() + " §evous a demandé en mariage !");
						}
					}else{
						long time1 = rp.getLastDivorce()+Settings.TIME_BEFORE_WEDDING_PROPOSAL-System.currentTimeMillis();
						long hours1 = TimeUnit.MILLISECONDS.toHours(time1);
						time1 -= TimeUnit.HOURS.toMillis(hours1);
						long minutes1 = TimeUnit.MILLISECONDS.toMinutes(time1);
						time1 -= TimeUnit.MINUTES.toMillis(minutes1);
						long seconds1 = TimeUnit.MILLISECONDS.toSeconds(time1);
						time1 -= TimeUnit.SECONDS.toMillis(seconds1);
						String time = String.format("§4%02d §ch. §4%02d §cmin. §4%02d §cs.", hours1, minutes1, seconds1);
						rp.sendMessage("§cVous venez de divorcer ! Attendez encore " + time);
					}
				}
			}
			this.getMenu().setItem(this.SLOT_MARRY, this.getMarry());
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
	
	private ItemStack getDuel(){
		ItemStack duel = new ItemStack(Material.SHIELD, 1);
		ItemMeta duelm = duel.getItemMeta();
		duelm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		RDuel rduel = rpfrom.getRequestedDuelTo(rp);
		if(rduel != null){
			duelm.setDisplayName(("§a§lAccepter le duel" + (rduel.isCompetitive() ? " compétitif" : "")));
			duelm.setLore(Arrays.asList(("§7Accepter l'invitation en duel de " + rpfrom.getName() + "."), ("§7Faîtes-le seulement lorsque vous êtes prêt !"), "", ("§eLe duel se terminera automatiquement après " + Settings.DUEL_TIMEOUT + " min.")));
		}else{
			if(rp.getRequestedDuelTo(rpfrom) != null){
				duelm.setDisplayName(("§4§lAnnuler l'invitation"));
				duelm.setLore(Arrays.asList(("§7Annuler l'invitation en duel envoyée à " + rpfrom.getName() + ".")));
			}else{
				duelm.setDisplayName(("§6§lInviter en duel"));
				duelm.setLore(Arrays.asList(("§7Envoyer une demande en duel à " + from.getName() + "."), ("§7Il a " + Settings.DUEL_REQUEST_TIME + " secondes pour accepter !"), "§7" + ("Si disponible, un duel compétitif vous sera proposé :"), "§7" + ("Il met en jeu votre renom. Soyez prudent !")));
			}
		}
		duel.setItemMeta(duelm);
		return duel;
	}
	private ItemStack getTrade(){
		ItemStack trade = new ItemStack(Material.ENDER_CHEST, 1);
		ItemMeta tradem = trade.getItemMeta();
		if(this.getHolder().equals(rpfrom.getTradeRequestOpponent())){
			tradem.setDisplayName(("§a§lAccepter l'échange"));
			tradem.setLore(Arrays.asList(("§7Accepter la requête d'échange de " + from.getName() + "."), ("§7Ce système d'échange est 100% sans arnaque !"), "", ("§7Vous pouvez refuser un échange en fermant l'inventaire.")));
		}else{
			tradem.setDisplayName(("§6§lDemander un échange"));
			tradem.setLore(Arrays.asList(("§7Envoyer une demande d'échange à " + from.getName() + "."), ("§7Il a " + TradingHandler.REQUEST_TIME + " secondes pour accepter !")));
		}
		trade.setItemMeta(tradem);
		return trade;
	}
	private ItemStack getInspect(){
		ItemStack item = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Inspecter ") + rpfrom.getName());
		meta.setLore(Arrays.asList(("§7Obtenez des informations sur l'armure"), ("§7et les items portés par " + rpfrom.getName())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getInvite(){
		ItemStack invite = new ItemStack(Material.BEACON, 1);
		ItemMeta invitem = invite.getItemMeta();
		if(mfrom.hasGuild()){
			if(gm.hasGuild()){
				if(gm.getGuild().equals(mfrom.getGuild())){
					invitem.setDisplayName("§2§l" + mfrom.getGuild().getName());
					invitem.setLore(Arrays.asList(("§7Vous êtes déjà dans la guilde de " + mfrom.getName() + ".")));
				}else{
					invitem.setDisplayName("§6§l" + ("Menu des relations de guilde"));
					invitem.setLore(Arrays.asList(("§7Ouvrir le menu des relations de guilde pour"), ("§7gérer celles que vous entretenez avec la guilde " + mfrom.getGuild().getName())));
				}
			}else if(gm.isInvited(mfrom.getGuild())){
				invitem.setDisplayName(("§a§lAccepter l'invitation de §2§l" + mfrom.getGuild().getName()));
				invitem.setLore(Arrays.asList(("§7Rejoidre les membres de la guilde " + mfrom.getGuild().getName() + ".")));
			}else if(gm.hasAsked(mfrom.getGuild())){
				invitem.setDisplayName(("§c§lAnnuler la requête envoyée à §4§l" + mfrom.getGuild().getName()));
				invitem.setLore(Arrays.asList(("§7Annuler la requête d'adhésion envoyée à la guilde " + mfrom.getGuild().getName())));
			}else{
				invitem.setDisplayName(("§a§lDemander à §2§l" + mfrom.getGuild().getName() + " §a§lde vous ajouter"));
				invitem.setLore(Arrays.asList(("§7Demandez à la guilde " + mfrom.getGuild().getName() + " de"), ("§7de vous ajouter parmis ses rangs.")));
			}
		}else{
			if(mfrom.hasAsked(gm.getGuild())){
				invitem.setDisplayName(("§a§lAjouter §2§l" + from.getName() + " §a§là la guilde"));
				invitem.setLore(Arrays.asList(("§7Accepter " + from.getName() + " dans la guilde.")));
			}else if(mfrom.isInvited(gm.getGuild())){
				invitem.setDisplayName(("§c§lAnnuler l'invitation de §4§l" + from.getName()));
				invitem.setLore(Arrays.asList(("§7Annuler l'invitation envoyée à " + from.getName()), ("§7à rejoindre votre guilde.")));
			}else{
				invitem.setDisplayName(("§a§lInviter §2§l" + from.getName() + " §a§ldans la guilde"));
				invitem.setLore(Arrays.asList(("§7Demandez à " + from.getName() + " de se joindre"), ("§7à vous et votre guilde.")));
			}
		}
		invite.setItemMeta(invitem);
		return invite;
	}
	private ItemStack getMarry(){
		ItemStack item = new ItemStack(Material.ROSE_RED, 1);
		ItemMeta meta = item.getItemMeta();
		if(rp.getCouple() != null && this.showFiancer){
			meta.setDisplayName("§4§l" + ("Divorcer"));
			meta.setLore(Arrays.asList(("§7Divorcer avec " + from.getName() + ".")));
		}else{
			if(rpfrom.equals(rp.fiance)){
				meta.setDisplayName("§c§l" + ("Annuler la demande en mariage"));
				meta.setLore(Arrays.asList(("§7Annuler la demande de mariage faîte à " + from.getName() + ".")));
			}else{
				if(rp.equals(rpfrom.fiance)){
					meta.setDisplayName("§a§l" + ("Marier ") + rpfrom.getName());
					meta.setLore(Arrays.asList(("§7Accepter la demande en marriage de " + rpfrom.getName() + ".")));
				}else{
					meta.setDisplayName("§6§l" + ("Demander ") + rpfrom.getName() + (" en mariage"));
					meta.setLore(Arrays.asList(("§7Envoyer une demande de mariage à " + from.getName() + "."), ("§7Pour confirmer le mariage, vous devez ensuite aller voir un pasteur.")));
				}
			}
		}
		item.setItemMeta(meta);
		return item;
	}

	public void updateDuel() {
		this.getHolder().getOpenInventory().getTopInventory().setItem(SLOT_DUEL, this.getDuel());
	}

	public void updateTrade() {
		this.getHolder().getOpenInventory().getTopInventory().setItem(SLOT_TRADE, this.getTrade());
	}

	public void updateInvite() {
		this.getHolder().getOpenInventory().getTopInventory().setItem(SLOT_INVITE, this.getInvite());
	}

}
