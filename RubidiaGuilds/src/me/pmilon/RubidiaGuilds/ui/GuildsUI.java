package me.pmilon.RubidiaGuilds.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.utils.LevelUtils;

public class GuildsUI extends ListMenuUIHandler<Guild> {

	private int DESC_LIMIT = 30;
	private int LIST_ID_SEARCH = 1;
	
	public GuildsUI(Player p) {
		super(p, "Existing guilds", "Guildes existantes", 3);
		for(Guild guild : GuildsPlugin.gcoll.data()){
			if(!guild.equals(Guild.getNone()) && guild.isConnected()){
				this.list.add(guild);
			}
		}
		Collections.sort(list, new Comparator<Guild>(){
			@Override
			public int compare(Guild g1, Guild g2) {
				return g2.getLevel()-g1.getLevel();
			}
		});
		List<Guild> notCo = new ArrayList<Guild>();
		for(Guild guild : GuildsPlugin.gcoll.data()){
			if(!guild.equals(Guild.getNone()) && !guild.isConnected() && guild.isActive()){
				notCo.add(guild);
			}
		}
		Collections.sort(notCo, new Comparator<Guild>(){
			@Override
			public int compare(Guild g1, Guild g2) {
				return Long.compare(g2.getLastConnection(), g1.getLastConnection());
			}
		});
		this.list.addAll(notCo);
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList("§7" + ("Cliquez gauche sur une guild pour demander une adhésion"), "§7" + ("Cliquez droit pour gérer les relations que vous entretenez"), "§8" + ("Cliquez ici pour rechercher une guilde")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected ItemStack getItem(Guild guild) {
		ItemStack item = guild.getCape();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((guild.isConnected() ? "§2[✔] §a" : "§4[✘] §c") + guild.getName());
		double ratio = guild.getExperience()/LevelUtils.getLevelExperienceAmount(guild.getLevel());
		meta.setLore(Arrays.asList(guild.isPeaceful() ? "§6§o" + ("Guilde paisible") : (gm.hasGuild() ? (gm.getGuild().getRelationTo(guild).equals(Relation.ENEMY) ? "§c§o" + ("Guilde ennemie") : (gm.getGuild().getRelationTo(guild).equals(Relation.ALLY) ? "§d§o" + ("Guilde alliée") : "§e§o" + ("Guilde belliqueuse"))) : "§8§o" + ("Guilde belliqueuse")), "", "§7" + StringUtils.abbreviate(guild.getDescription(), this.DESC_LIMIT), "§f§m-----------------------", "§8" + ("Niveau ") + "§7" + guild.getLevel(), "§8" + ("Expérience ") + "§7" + String.valueOf(Utils.round(ratio, 2)) + "%", "§8" + ("Territoires ") + "§7" + guild.getClaims().size(), "§8" + ("Membres ") + "§7" + guild.getMembers().size()));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return guild.isGlowing() ? Utils.setGlowingWithoutAttributes(item) : item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player p, ItemStack is) {
		Guild guild = this.get(e.getRawSlot());
		if(guild != null) {
			if(e.isLeftClick()){
				if(rp.isOp()){
					Core.uiManager.requestUI(new GMenuUI(this.getHolder(), guild));
				}else{
					if(gm.hasGuild())rp.sendMessage("§cVous êtes déjà dans une guilde !");
					else guild.ask(gm);
				}
			}else{
				if(gm.hasGuild()){
					if(!gm.getGuild().equals(guild)){
						Core.uiManager.requestUI(new GRelationsUI(this.getHolder(), guild));
					}else rp.sendMessage("§cVous ne pouvez gérer les relations entre votre guilde et votre guilde !");
				}
			}
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		this.close(true, this.LIST_ID_SEARCH);
		rp.sendMessage("Entrez le nom de la guilde que vous recherchez.");
	}

	@Override
	public String getType() {
		return "GUILDS_LIST_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected void onOpen() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_SEARCH){
					this.list.clear();
					for(Guild guild : GuildsPlugin.gcoll.data()){
						if(!guild.equals(Guild.getNone()) && guild.getName().toLowerCase().contains(this.getMessage().toLowerCase())){
							this.list.add(guild);
						}
					}
				}
			}
		}
	}

	@Override
	protected void onPageTurn() {
	}

}
