package me.pmilon.RubidiaQuests.ui.houses;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaQuests.BlockViewer;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.houses.House;
import me.pmilon.RubidiaQuests.utils.WE;

public class HouseManagerUI extends UIHandler {

	private House house;
	
	private int SLOT_VIEW = 0;
	
	private int SLOT_BLOCKS = 1;
	private int SLOT_BLOCK_PRICE = 2;
	private int SLOT_BLOCK_RESET = 4;
	
	private int SLOT_SELL = 4;
	
	private int LIST_ID_PRICE = 1;
	private int LIST_ID_BLOCKS_ADD = 2;
	private int LIST_ID_BLOCKS_REMOVE = 3;
	public HouseManagerUI(Player p, House house) {
		super(p);
		this.house = house;
		this.menu = Bukkit.createInventory(this.getHolder(), InventoryType.HOPPER, "Gestion de la maison");
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "HOUSE_MANAGER_UI";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event, Player arg1) {
		event.setCancelled(true);
		int slot = event.getRawSlot();
		if(slot == this.SLOT_VIEW) {
			if(BlockViewer.isViewing(rp)) {
				BlockViewer.cancel(rp);
				rp.sendMessage("§eLes blocs constituant la maison sont désormais masqués.");
			} else {
				BlockViewer.show(rp, this.getHouse().getBlocks());
				rp.sendMessage("§eLes blocs constituant la maison sont désormais affichés.");
			}
		} else {
			if(this.getHouse().isInhabited() && this.getHouse().getOwner().equals(rp)) {
				if(slot == this.SLOT_SELL) {
					Core.uiManager.requestUI(new HouseSellingConfirmationUI(this.getHolder(), this.getHouse()));
				}
			} else if(rp.isOp()) {
				if(slot == this.SLOT_BLOCKS) {
					if(event.isRightClick()) {
						this.close(true, this.LIST_ID_BLOCKS_REMOVE);
						rp.sendMessage("§aSélectionnez des blocs à supprimer.");
					} else {
						this.close(true, this.LIST_ID_BLOCKS_ADD);
						rp.sendMessage("§aSélectionnez des blocs à ajouter.");
					}
				} else if(slot == this.SLOT_BLOCK_PRICE)this.close(true, this.LIST_ID_PRICE);
				else if(slot == this.SLOT_BLOCK_RESET) {
					this.getHouse().reset(this.getHolder().getLocation());
					this.getMenu().setItem(this.SLOT_BLOCK_PRICE, this.getBlockPrice());
				}
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
				if(this.getListeningId() == this.LIST_ID_PRICE){
					if(Utils.isDouble(this.getMessage())) {
						this.getHouse().setBlockPrice(Double.valueOf(this.getMessage()));
					}
				} else if(this.getListeningId() == this.LIST_ID_BLOCKS_ADD) {
					LocalSession session = QuestsPlugin.worldEdit.getSession(this.getHolder());
					if(session != null) {
						if(session.getSelectionWorld() != null){
							if(session.isSelectionDefined(session.getSelectionWorld())){
								try {
									this.getHouse().getBlocks().addAll(WE.getBlocks(this.getHolder().getWorld(), session.getSelection(session.getSelectionWorld())));
									this.getHouse().updateBlocks();
								} catch (IncompleteRegionException e) {
									rp.sendMessage("§cSélectionnez une région complète");
								}
							}else rp.sendMessage("§cSélectionnez une région complète");
						}else rp.sendMessage("§cSélectionnez une région complète");
					}else rp.sendMessage("§cSélectionnez une région complète");
				} else if(this.getListeningId() == this.LIST_ID_BLOCKS_REMOVE) {
					LocalSession session = QuestsPlugin.worldEdit.getSession(this.getHolder());
					if(session != null) {
						if(session.getSelectionWorld() != null){
							if(session.isSelectionDefined(session.getSelectionWorld())){
								try {
									this.getHouse().getBlocks().removeAll(WE.getBlocks(this.getHolder().getWorld(), session.getSelection(session.getSelectionWorld())));
									this.getHouse().updateBlocks();
								} catch (IncompleteRegionException e) {
									rp.sendMessage("§cSélectionnez une région complète");
								}
							}else rp.sendMessage("§cSélectionnez une région complète");
						}else rp.sendMessage("§cSélectionnez une région complète");
					}else rp.sendMessage("§cSélectionnez une région complète");
				}
			}
		}
		
		this.getMenu().setItem(this.SLOT_VIEW, this.getView());
		
		if(this.getHouse().isInhabited() && this.getHouse().getOwner().equals(rp)) {
			this.getMenu().setItem(this.SLOT_SELL, this.getSell());
		} else if(rp.isOp()) {
			this.getMenu().setItem(this.SLOT_BLOCKS, this.getBlocks());
			this.getMenu().setItem(this.SLOT_BLOCK_PRICE, this.getBlockPrice());
			this.getMenu().setItem(this.SLOT_BLOCK_RESET, this.getReset());
		}
		
		return this.getHolder().openInventory(this.getMenu()) != null;
	}
	
	public ItemStack getView() {
		ItemStack item = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f§lVision des blocs");
		meta.setLore(Arrays.asList("§7Activez ou désactivez la vision des blocs de la maison"));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getBlocks() {
		ItemStack item = new ItemStack(Material.BRICK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Modify house's blocks");
		meta.setLore(Arrays.asList("Clic gauche : ajouter des blocs depuis la sélection WE","Clic droit : supprimer des blocs depuis la sélection WE"));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getBlockPrice() {
		ItemStack item = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Block price");
		meta.setLore(Arrays.asList(String.valueOf(this.getHouse().getBlockPrice()),"Prix total : " + this.getHouse().getPrice()));
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getReset() {
		ItemStack item = new ItemStack(Material.BRICKS, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Reset house blocks from your location");
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSell() {
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4§lVendre la maison");
		meta.setLore(Arrays.asList("§7Vendez votre maison à hauteur de §l70%", "§7du prix de l'immobilier actuel du quartier", "§4§lATTENTION : §cvotre hôte s'occupera de vider votre maison"));
		item.setItemMeta(meta);
		return item;
	}
	
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

}
