package me.pmilon.RubidiaGuilds.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaWG.Flags;
import me.pmilon.RubidiaWG.WGUtils;

public class MapUI extends UIHandler {

	private Chunk center;
	private int p = 1;
	public MapUI(Player p) {
		super(p);
		Location location = this.getHolder().getLocation();
		if(location.getYaw() > 225 && location.getYaw() <= 315){
			this.p = 2;
		}else if(location.getYaw() > 315 || location.getYaw() <= 45){
			this.p = 3;
		}else if(location.getYaw() > 45 && location.getYaw() <= 135){
			this.p = 4;
		}
		this.center = location.getChunk();
		this.menu = Bukkit.createInventory(this.getHolder(), 45, ("Carte des territoires"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		boolean b = this.getHolder().openInventory(this.getMenu()) != null;
		for(int i = 0;i < 5;i++){
			final int j = i;
			new BukkitTask(GuildsPlugin.instance){

				@Override
				public void run() {
					print(j);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(1+2*i);
		}
		return b;
	}
	
	private void print(int i){
		for(int j = 0;j < 9;j++){
			int x = this.center.getX() + (p == 1 ? j-4 : (p == 2 ? 2-i : (p == 3 ? 4-j : i-2 )));
			int z = this.center.getZ() + (p == 1 ? i-2 : (p == 2 ? j-4 : (p == 3 ? 2-i : 4-j )));
			Chunk chunk = this.center.getWorld().getChunkAt(x, z);
			Claim claim = Claim.get(chunk);
			ItemStack item = null;
			ItemMeta meta = null;
			List<String> lore = new ArrayList<String>();
			if(claim == null){
				if(WGUtils.testState(null, this.getHolder().getWorld(), new Vector(x*16,0,z*16), new Vector(x*16+15,255,z*16+15), Flags.CLAIM)){
					item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
					meta = item.getItemMeta();
					meta.setDisplayName("§f§l" + ("Territoire libre"));
					item.setItemMeta(meta);
				}else{
					item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1);
					meta = item.getItemMeta();
					meta.setDisplayName("§6§l" + ("Territoire protégé"));
					item.setItemMeta(meta);
				}
			}else{
				Guild claimGuild = claim.getGuild();
				if(claimGuild.isPeaceful()){
					item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
					meta = item.getItemMeta();
					meta.setDisplayName("§6§l" + ("Territoire revendiqué"));
					lore.add("§e§o" + ("par") + " " + claimGuild.getName());
				}else{
					if(gm.hasGuild()){
						Guild guild = gm.getGuild();
						if(guild.getRelationTo(claimGuild).equals(Relation.ALLY)){
							item = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE, 1);
							meta = item.getItemMeta();
							meta.setDisplayName("§5§l" + ("Territoire revendiqué"));
							lore.add("§d§o" + ("par") + " " + claimGuild.getName());
						}else if(guild.getRelationTo(claimGuild).equals(Relation.ENEMY)){
							item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
							meta = item.getItemMeta();
							meta.setDisplayName("§4§l" + ("Territoire revendiqué"));
							lore.add("§c§o" + ("par") + " " + claimGuild.getName());
						}else if(guild.getRelationTo(claimGuild).equals(Relation.MEMBER)){
							item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
							meta = item.getItemMeta();
							meta.setDisplayName("§2§l" + ("Territoire revendiqué"));
							lore.add("§a§o" + ("par") + " " + claimGuild.getName());
						}else if(guild.getRelationTo(claimGuild).equals(Relation.NEUTRAL)){
							item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
							meta = item.getItemMeta();
							meta.setDisplayName("§9§l" + ("Territoire revendiqué"));
							lore.add("§b§o" + ("par") + " " + claimGuild.getName());
						}
					}else{
						item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
						meta = item.getItemMeta();
						meta.setDisplayName("§9§l" + ("Territoire revendiqué"));
						lore.add("§b§o" + ("par") + " " + claimGuild.getName());
					}
				}
			}

			if(i == 2 && j == 4){
				item = new ItemStack(Material.BROWN_STAINED_GLASS_PANE, 1);
				lore.add("");
				lore.add("§7" + ("Vous êtes ici"));
			}
			lore.add("§8(" + x + "," + z + ")");
			if(meta == null)meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
			this.getMenu().setItem(9*i+j, item);
		}
	}

}
