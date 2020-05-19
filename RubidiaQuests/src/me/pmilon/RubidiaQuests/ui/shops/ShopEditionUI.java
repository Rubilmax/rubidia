package me.pmilon.RubidiaQuests.ui.shops;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.chat.ChatType;
import me.pmilon.RubidiaCore.chat.RChatMessage;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.tags.TagStand;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.shops.PlayerShop;
import me.pmilon.RubidiaQuests.shops.Shop;

public class ShopEditionUI extends UIHandler{

	private Shop shop;
	private int SLOT_INVTITLE = 8, SLOT_TITLE = 17, SLOT_CLEAR = 26, SLOT_SHOP = 30, SLOT_MSG = 32, SLOT_NEXT = 35, SLOT_PREV = 27, page_index = 0;
	private int LIST_ID_INVTITLE = 2, LIST_ID_TITLE = 3, LIST_ID_MSG = 4;
	private List<Integer> item_slots = Arrays.asList(0,1,2,3,4,5,6,7);
	private List<Integer> item_bigprice = Arrays.asList(9,10,11,12,13,14,15,16);
	private List<Integer> item_price = Arrays.asList(18,19,20,21,22,23,24,25);
	private int pageLimit;
	private boolean canClick = true;
	public ShopEditionUI(Player p, Shop shop, int page_index) {
		super(p);
		this.pageLimit = !rp.isVip() && shop instanceof PlayerShop ? 2 : 44;
		this.shop = shop;
		this.page_index = page_index;
		this.keepWindowAfterEditMode = true;
	}

	@Override
	protected void closeUI() {
		if(!Core.uiManager.isInTempSession(this.getHolder())){
			this.getShop().close();
		}
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "SHOP_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(this.getShop() instanceof PlayerShop){
			if(((PlayerShop)this.getShop()).isStart()){
				return;
			}
		}

		e.setCancelled(true);
		ItemStack current = e.getCurrentItem();
		if(current != null){
			if(!rp.getRClass().equals(RClass.RANGER) || e.getRawSlot() != 17){
				if(e.getClick().toString().contains("LEFT")){
					ItemStack is = current.clone();
					if(!this.getShop().getItemStacks().contains(is)){
						if((int) ((this.getShop().getItemStacks().size()+1)*.124) < this.pageLimit){
							Integer[] prices = new Integer[]{0,1};
							this.getShop().getItemStacks().add(is);
							this.getShop().getPrices().add(prices);
							this.addSellingIS(is, prices);
							int size = (int) (this.getShop().getItemStacks().size()*.124);
							if(size > this.page_index){
								this.setPage(size);
							}
						}else rp.sendMessage("§cDevenez VIP pour obtenir un nombre illimité de ventes !");
					}else rp.sendMessage("§cCes items sont déjà en vente !");
				}
			}
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		if(!canClick)return;
		canClick = false;
		Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){
			public void run(){
				canClick = true;
			}
		}, 4);
		
		int slot = e.getRawSlot();
		
		if(slot == SLOT_PREV){
			this.setPage(this.page_index-1);
			return;
		}else if(slot == SLOT_NEXT){
			this.setPage(this.page_index+1);
			return;
		}else if(this.getShop() instanceof PlayerShop && slot != SLOT_SHOP && slot != SLOT_MSG){
			if(((PlayerShop)this.getShop()).isStart())return;
		}
		
		if(slot == SLOT_INVTITLE){
			rp.sendMessage("§aRenommez votre menu en tapant son titre dans le chat !");
			this.close(true, this.LIST_ID_INVTITLE);
		}else if(slot == SLOT_TITLE){
			rp.sendMessage("§aRenommez votre boutique en tapant son nom dans le chat !");
			this.close(true, this.LIST_ID_TITLE);
		}else if(slot == SLOT_SHOP){
			PlayerShop shop = (PlayerShop)this.getShop();
			if(shop.isStart())shop.close();
			else{
				shop.setStart(true);
				new TagStand(this.getHolder(), new String[]{"§2§lMAGASIN",shop.getTitle()}, true).display();
				shop.setEditionUI(this);
			}
			e.setCurrentItem(this.getShopIS());
		}else if(slot == SLOT_CLEAR){
			this.getShop().getItemStacks().clear();
			Core.uiManager.requestUI(new ShopEditionUI(this.getHolder(), this.getShop(),0));
		}else if(slot == SLOT_MSG)this.close(true, this.LIST_ID_MSG);
		else{
			int amount = 1;
			if(e.isRightClick())amount*=-1;
			if(e.isShiftClick())amount*=10;
			this.add(amount, slot);
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		if(this.getShop() instanceof PlayerShop){
			final PlayerShop shop = (PlayerShop)this.getShop();
			if(shop.isStart() && !Core.uiManager.isInTempSession(this.getHolder())){
				Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){
					public void run(){
						if(getHolder().isOnline()){
							ShopEditionUI ui = new ShopEditionUI(getHolder(), getShop(), page_index);
							Core.uiManager.requestUI(ui);
							shop.setEditionUI(ui);
						}else shop.close();
					}
				}, 0);
			}
		}
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(!this.getMessage().equalsIgnoreCase("-")){
					if(this.getListeningId() == this.LIST_ID_INVTITLE){
						this.getShop().setInventoryTitle(this.getMessage());
					}else if(this.getListeningId() == this.LIST_ID_TITLE){
						((PlayerShop)this.getShop()).setTitle(this.getMessage());
					}else if(this.getListeningId() == this.LIST_ID_MSG){
						new RChatMessage(rp, null, gm, gm.getGuild(), ChatType.SHOUT, "[VENTE] " + ChatColor.stripColor(this.getMessage()), player.getEquipment().getItemInMainHand()).send();
					}
				}
			}
		}
		this.menu = Bukkit.createInventory(this.getHolder(), 36, StringUtils.abbreviate(this.getShop().getInventoryTitle(), 32));
		this.setPage(this.page_index);
		return this.getHolder().openInventory(this.menu) != null;
	}

	public ItemStack getInvTitle(){
		ItemStack RENAME = new ItemStack(Material.NAME_TAG, 1);
		ItemMeta mRENAME = RENAME.getItemMeta();
		mRENAME.setDisplayName(("§6§lRenommer le menu boutique"));
		mRENAME.setLore(Arrays.asList(("§7Donnez un nom accrochant à votre menu !"), "", "§r" + this.getShop().getInventoryTitle()));
		RENAME.setItemMeta(mRENAME);
		return RENAME;
	}
	public ItemStack getTitle(){
		ItemStack RENAME = new ItemStack(Material.SIGN, 1);
		ItemMeta mRENAME = RENAME.getItemMeta();
		mRENAME.setDisplayName(("§6§lRenommer la boutique"));
		mRENAME.setLore(Arrays.asList(("§7Donnez un nom accrochant à votre boutique !"), "", "§r" + ((PlayerShop) this.getShop()).getTitle()));
		RENAME.setItemMeta(mRENAME);
		return RENAME;
	}
	public ItemStack getClearIS(){
		ItemStack CLEAR = new ItemStack(Material.REDSTONE_BLOCK, 1);
		ItemMeta mCLEAR = CLEAR.getItemMeta();
		mCLEAR.setDisplayName(("§4§lVider la boutique"));
		mCLEAR.setLore(Arrays.asList(("§7Vider entièrement le contenu de la boutique")));
		CLEAR.setItemMeta(mCLEAR);
		return CLEAR;
	}
	public ItemStack getShopIS(){
		PlayerShop shop = (PlayerShop)this.getShop();
		ItemStack START = new ItemStack(shop.isStart() ? Material.BOOK : Material.WRITABLE_BOOK, 1);
		ItemMeta mSTART = START.getItemMeta();
		if(shop.isStart()){
			mSTART.setDisplayName(("§6§lFermer la boutique"));
			mSTART.setLore(Arrays.asList(("§7Terminer la vente et interrompre les transactions en cours")));
		}else {
			mSTART.setDisplayName(("§6§lOuvrir la boutique"));
			mSTART.setLore(Arrays.asList(("§7Cliquez sur les items dans votre inventaire pour les ajouter à la boutique."), ("§7Cliquez ici pour ouvrir votre boutique !")));
		}
		START.setItemMeta(mSTART);
		return START;
	}
	public ItemStack getShopMsg(){
		ItemStack CLEAR = new ItemStack(Material.PAPER, 1);
		ItemMeta mCLEAR = CLEAR.getItemMeta();
		mCLEAR.setDisplayName(("§6§lEnvoyer un message au serveur"));
		mCLEAR.setLore(Arrays.asList(("§7Lancer un message au serveur pour laisser"), ("§7les joueurs voir ce que vous vendez !")));
		CLEAR.setItemMeta(mCLEAR);
		return CLEAR;
	}
	
	public ItemStack bigPriceIS(int amount){
		ItemStack BIGPRICE = new ItemStack(Material.EMERALD_BLOCK, amount);
		ItemMeta mBIGPRICE = BIGPRICE.getItemMeta();
		mBIGPRICE.setDisplayName(("§2§lPrix"));
		BIGPRICE.setItemMeta(mBIGPRICE);
		return BIGPRICE;
	}
	public ItemStack priceIS(int amount){
		ItemStack PRICE = new ItemStack(Material.EMERALD, amount);
		ItemMeta mPRICE = PRICE.getItemMeta();
		mPRICE.setDisplayName(("§a§lPrix"));
		PRICE.setItemMeta(mPRICE);
		return PRICE;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public void addSellingIS(ItemStack is, Integer[] prices){
		int slot = this.getShop().getItemStacks().indexOf(is)-this.page_index*8;
		if(slot >= 0 && slot < 8){
			this.menu.setItem(slot, is);
			this.menu.setItem(slot+9, this.bigPriceIS(prices[0]));
			this.menu.setItem(slot+18, this.priceIS(prices[1]));
		}
	}
	public void removeSellingIS(int slot){
		ItemStack[] is = new ItemStack[3];
		for(int i = 0;i < 3;i++){
			is[i] = this.menu.getItem(slot+9*i);
			if(is[i] == null){
				is[i] = new ItemStack(Material.AIR, 0);
			}
		}
		this.getShop().removeItemStack(is);
		this.getHolder().closeInventory();
	}
	

	public void add(int amount, int slot){
		ItemStack item = this.getShop().getItemStacks().get(slot%9+8*this.page_index);
		int index = this.getShop().getItemStacks().indexOf(item);
		if(item_slots.contains(slot)){
			int item_amount = item.getAmount() + amount;
			if(item_amount < 1){
				this.getShop().getPrices().remove(index);
				this.getShop().getItemStacks().remove(item);
				Core.uiManager.requestUI(new ShopEditionUI(this.getHolder(), this.getShop(), this.page_index));
			}else{
				if(item_amount > 64)item_amount = 64;
				item.setAmount(item_amount);
				this.menu.setItem(slot, item);
			}
		}else{
			ItemStack stack = this.menu.getItem(slot);
			int item_amount = amount;
			if(stack == null || stack.getAmount() < 1){
				if(amount < 0)amount = 0;
				if(item_bigprice.contains(slot))stack = this.bigPriceIS(amount);
				else if(item_price.contains(slot))stack = this.priceIS(amount);
			}else{
				item_amount += stack.getAmount();
				if(item_amount < 0)item_amount = 0;
				if(item_amount > 64){
					if(item_price.contains(slot)){
						item_amount -= 64;
						this.add(1, slot-9);
					}
				}
				stack.setAmount(item_amount);
			}
			this.getShop().getPrices().get(index)[(int) ((slot-9)*.124)] = item_amount;
			this.menu.setItem(slot, stack);
		}
	}
	public int getQuantity(ItemStack stack){
		int quantity = 0;
		for(ItemStack item : this.getHolder().getInventory()){
			if(stack.isSimilar(item)){
				quantity += item.getAmount();
			}
		}
		return (int) (((double) quantity)/stack.getAmount());
	}
	public void buyItem(ItemStack item, int times){
		int quantity = item.getAmount()*times;
		for(int i = 0;i < this.getHolder().getInventory().getSize();i++){
			if(quantity > 0){
				ItemStack stack = this.getHolder().getInventory().getItem(i);
				if(item.isSimilar(stack)){
					int amount = stack.getAmount();
					quantity -= amount;
					if(quantity >= 0)this.getHolder().getInventory().setItem(i, new ItemStack(Material.AIR,1));
					else stack.setAmount(-quantity);
				}
			}else break;
		}
		if(this.getQuantity(item) <= 0)this.removeSellingIS(this.getShop().getItemStacks().indexOf(item)-8*this.page_index);
	}
	
	protected void setPage(int index){
		this.menu.clear();
		int size = (int) (this.getShop().getItemStacks().size()*.124)+1;
		if(index >= size)index = size-1;
		this.page_index = index;
		for(int i = 8*this.page_index;i < 8*this.page_index+8;i++){
			if(this.getShop().getItemStacks().size() > i){
				ItemStack is = this.getShop().getItemStacks().get(i);
				Integer[] prices = this.getShop().getPrices().get(i);
				this.addSellingIS(is, prices);
			}
		}
		if(index != 0){
			ItemStack prev = new ItemStack(Material.MELON, 1);
			ItemMeta meta = prev.getItemMeta();
			meta.setDisplayName(("§cPage précédente"));
			prev.setItemMeta(meta);
			this.menu.setItem(this.SLOT_PREV, prev);
		}
		if(index != size-1){
			ItemStack next = new ItemStack(Material.ARROW, 1);
			ItemMeta meta = next.getItemMeta();
			meta.setDisplayName(("§aPage suivante"));
			next.setItemMeta(meta);
			this.menu.setItem(this.SLOT_NEXT, next);
		}
		
		this.menu.setItem(SLOT_INVTITLE, this.getInvTitle());
		if(this.getShop() instanceof PlayerShop){
			this.menu.setItem(SLOT_TITLE, this.getTitle());
			this.menu.setItem(SLOT_SHOP, this.getShopIS());
			this.menu.setItem(SLOT_MSG, this.getShopMsg());
		}
		this.menu.setItem(SLOT_CLEAR, this.getClearIS());
	}
}
