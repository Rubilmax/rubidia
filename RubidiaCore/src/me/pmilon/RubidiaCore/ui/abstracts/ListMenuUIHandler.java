package me.pmilon.RubidiaCore.ui.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ListMenuUIHandler<E> extends UIHandler {

	protected List<E> list = new ArrayList<E>();
	protected int page_index = 0;
	private int lines = 0;
	private int SLOT_INFOS,SLOT_NEXT,SLOT_PREV;
	public ListMenuUIHandler(Player p, String titleen, String titlefr, int lines) {
		super(p);
		this.lines = lines;
		this.SLOT_INFOS = 4+9*lines;
		this.SLOT_NEXT = 8+9*lines;
		this.SLOT_PREV = 9*lines;
		this.menu = Bukkit.createInventory(this.getHolder(), lines < 5 ? 9*(lines+1) : 54, StringUtils.abbreviate((titlefr),32));
	}

	@Override
	protected boolean openWindow() {
		this.onOpen();
		this.setPage(this.page_index);
		return this.getHolder().openInventory(this.menu) != null;
	}

	protected abstract void onOpen();
	
	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player){
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == SLOT_PREV){
			if(this.page_index > 0){
				this.setPage(this.page_index-1);
				this.onPageTurn();
			}
		}else if(slot == SLOT_NEXT){
			if(this.page_index < this.getPages().size()){
				this.setPage(this.page_index+1);
				this.onPageTurn();
			}
		}else if(slot == SLOT_INFOS){
			this.onInfosClick(e);
		}else if(slot < this.lines*9){
			ItemStack item = e.getCurrentItem();
			if(item != null){
				this.onClick(e, player, item);
			}
		}
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent e,Player p){
		if(e.isShiftClick())e.setCancelled(true);
	}
	
	protected List<List<E>> getPages(){
		List<List<E>> pages = new ArrayList<List<E>>();
		if(list.size() >= this.lines*9){
			int i = 0;
			for(E e : list){
				if(pages.size() > i){
					if(pages.get(i).size() >= this.lines*9){
						List<E> newList = new ArrayList<E>();
						pages.add(newList);
						i++;
					}
				}else{
					List<E> newList = new ArrayList<E>();
					pages.add(newList);
				}
				
				pages.get(i).add(e);
			}
		}else pages.add(list);
		return pages;
	}
	protected void setPage(int index){
		this.menu.clear();
		int slot = 0;
		List<List<E>> pages = this.getPages();
		if(pages.size() > index){
			for(E e : pages.get(index)){
				this.menu.setItem(slot, this.getItem(e));
				slot++;
			}
		}
		if(index > 0){
			ItemStack prev = new ItemStack(Material.MELON, 1);
			ItemMeta meta = prev.getItemMeta();
			meta.setDisplayName(("§cPage précédente"));
			prev.setItemMeta(meta);
			this.menu.setItem(this.SLOT_PREV, prev);
		}
		if(index < pages.size()-1){
			ItemStack next = new ItemStack(Material.ARROW, 1);
			ItemMeta meta = next.getItemMeta();
			meta.setDisplayName(("§aPage suivante"));
			next.setItemMeta(meta);
			this.menu.setItem(this.SLOT_NEXT, next);
		}
		this.menu.setItem(this.SLOT_INFOS, this.getInformations());
		this.page_index = index;
	}

	protected E get(int slot){
		if(this.list.size() > slot+(this.page_index*this.lines*9)){
			return this.list.get(slot+(this.page_index*this.lines*9));
		}
		return null;
	}
	
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	protected abstract ItemStack getItem(E e);
	
	protected abstract void onClick(InventoryClickEvent e, Player player, ItemStack is);

	protected abstract ItemStack getInformations();
	
	protected abstract void onInfosClick(InventoryClickEvent e);
	
	protected abstract void onPageTurn();
	
}
