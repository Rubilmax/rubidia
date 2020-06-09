package me.pmilon.RubidiaManager.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;
import me.pmilon.RubidiaManager.chunks.Chunk;
import me.pmilon.RubidiaManager.chunks.ChunkColl;
import me.pmilon.RubidiaManager.chunks.ChunkManager;
import me.pmilon.RubidiaManager.chunks.NChunk;
import me.pmilon.RubidiaManager.chunks.RChunk;
import me.pmilon.RubidiaWG.Flags;
import me.pmilon.RubidiaWG.WGUtils;

public class ChunksUI extends UIHandler {

	private Location location;
	private int p = 1;
	public ChunksUI(Player p) {
		super(p);
		this.location = this.getHolder().getLocation();
		if(location.getYaw() > 225 && location.getYaw() <= 315){
			this.p = 2;
		}else if(location.getYaw() > 315 || location.getYaw() <= 45){
			this.p = 3;
		}else if(location.getYaw() > 45 && location.getYaw() <= 135){
			this.p = 4;
		}
		this.menu = Bukkit.createInventory(this.getHolder(), 54, ("Carte des chunks"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "CHUNKS_MAP_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		int i = slot / 9;
		int j = slot % 9;
		Location location = new Location(this.location.getWorld(), this.location.getX() + (p == 1 ? j-4 : (p == 2 ? 2-i : (p == 3 ? 4-j : i-2 )))*16, this.location.getY(), this.location.getZ() + (p == 1 ? i-2 : (p == 2 ? j-4 : (p == 3 ? 2-i : 4-j )))*16);
		Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(location.getChunk());
		if(e.isLeftClick()){
			if(WGUtils.testState(null, this.getHolder().getWorld(), new Vector(chunk.getOrigin().getX(),chunk.getOrigin().getY(),chunk.getOrigin().getZ()), new Vector(chunk.getDestination().getX(),chunk.getDestination().getY(),chunk.getDestination().getZ()), Flags.REGEN)){
				if(!e.isShiftClick()){
					if(chunk instanceof RChunk){
						RubidiaManagerPlugin.getChunkColl().delete(chunk);
						NChunk nchunk = new NChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
						ChunkColl.chunks.add(nchunk);
						
						ChunkManager manager = ChunkManager.getManager(nchunk);
						manager.saveToFile();
						
						rp.sendMessage("§2Chunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §4NoChunk §e| §2Sauvegardé §e| §4Pas de régénération");
					}else if(chunk instanceof NChunk){
						RubidiaManagerPlugin.getChunkColl().delete(chunk);
						RChunk rchunk = new RChunk(chunk.getWorld(), chunk.getX(), chunk.getZ(), true);
						ChunkColl.chunks.add(rchunk);
						
						ChunkManager manager = ChunkManager.getManager(rchunk);
						manager.saveToFile();
						
						rp.sendMessage("§4NoChunk §6" + chunk.getX() + "§e,§6" + chunk.getZ() + "§e,§6" + chunk.getWorld().getName() + " §e-> §2Chunk §e| §2Sauvegardé §e| §2Régénération");
					}
				}else{
					String regen = chunk.isRegenable();
					if(regen.equals("true")){
						rp.sendMessage("§eRégénération du chunk...");
						ChunkManager.getManager(chunk).pasteFromFile();
						rp.sendMessage("§eChunk régénéré");
					}else rp.sendMessage("§cRégénération impossible : " + regen);
				}
			}else rp.sendMessage("§cCe chunk est protégé par une région !");
		}else{
			if(!e.isShiftClick()){
				if(chunk instanceof RChunk){
					RChunk rch = (RChunk) chunk;
					if(rch.isRegenerated()){
						ChunkManager.getManager(rch).undoPaste();
						rp.sendMessage("§eDernier état du chunk enregistré chargé");
					}
				}
			}else{
				rp.sendMessage("§eRégénération forcée du chunk...");
				ChunkManager.getManager(chunk).pasteFromFile();
				rp.sendMessage("§eChunk régénéré de force");
			}
		}
		this.getMenu().setItem(slot, this.get(i, j));
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < 5;i++){
			for(int j = 0;j < 9;j++){
				this.getMenu().setItem(9*i+j, this.get(i, j));
			}
		}
		this.getMenu().setItem(49, this.getInfos());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	
	private ItemStack getInfos(){
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList("§7" + ("Clic gauche : active ou désactive l'option de régénération"), "§7" + ("Sneak + Clic gauche : régénère le chunk"), "§7" + ("Clic droit : annule la régénération"), "§7" + ("Sneak + clic droit : force la régénération"), "§4§l" + ("ATTENTION") + "§c " + ("Cette option peut rapidement virer au désastre !")));
		infos.setItemMeta(meta);
		return infos;
	}
	private ItemStack get(int i, int j){
		Location location = new Location(this.location.getWorld(), this.location.getX() + (p == 1 ? j-4 : (p == 2 ? 2-i : (p == 3 ? 4-j : i-2 )))*16, this.location.getY(), this.location.getZ() + (p == 1 ? i-2 : (p == 2 ? j-4 : (p == 3 ? 2-i : 4-j )))*16);
		Chunk chunk = RubidiaManagerPlugin.getChunkColl().get(location.getChunk());
		ItemStack item = null;
		ItemMeta meta = null;
		List<String> lore = new ArrayList<String>();
		if(chunk == null){
			chunk = new RChunk(location.getWorld(), location.getChunk().getX(), location.getChunk().getZ(), true);
			ChunkColl.chunks.add(chunk);
		}
		
		if(!WGUtils.testState(null, this.getHolder().getWorld(),
				new Vector(chunk.getOrigin().getX(),chunk.getOrigin().getY(),chunk.getOrigin().getZ()),
				new Vector(chunk.getDestination().getX(),chunk.getDestination().getY(),chunk.getDestination().getZ()), Flags.REGEN)){
			item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
			meta = item.getItemMeta();
			meta.setDisplayName("§7§l" + ("Région protégée"));
		}else{
			if(chunk instanceof NChunk){
				item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
				meta = item.getItemMeta();
				meta.setDisplayName("§4§lNChunk");
				lore.add("§c§o" + ("Régénération désactivée"));
			}else{
				if(((RChunk)chunk).isRegenerated()){
					item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
					meta = item.getItemMeta();
					meta.setDisplayName("§2§lRChunk");
					lore.add("§a§o" + ("Régénération activée | Chunk régénéré"));
				}else{
					item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
					meta = item.getItemMeta();
					meta.setDisplayName("§6§lRChunk");
					lore.add("§e§o" + ("Régénération activée | Chunk endommagé"));
				}
			}
		}
		Claim claim = Claim.get(chunk.getBukkitChunk());
		if(claim != null){
			Guild guild = claim.getGuild();
			item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1);
			lore.add("§d" + ("Chunk revendiqué par") + " §o" + guild.getName());
			if(!guild.isActive()){
				lore.add("§c" + ("Guilde inactive"));
			}
		}

		if(i == 2 && j == 4){
			item = new ItemStack(Material.BROWN_STAINED_GLASS_PANE, 1);
			lore.add("");
			lore.add("§7Vous êtes ici");
		}
		lore.add("§8(" + chunk.getX() + "," + chunk.getZ() + ")");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
}
