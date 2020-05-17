package me.pmilon.RubidiaPets.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaPets.pets.Pet;
import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Wolf;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PetUI extends UIHandler{
	
  private final Pet pet;
  private final LivingEntity entity;
  private int SLOT_TOGGLE_STAY = 2;
  private int SLOT_TOGGLE_TYPE = 3;
  private int SLOT_TOGGLE_AGE = 4;
  private int SLOT_DISTINCTIONS = 6;
  private int SLOT_PEARLS = 8;
  private int SLOT_NAME = 0;
  
  private int LIST_ID_NAME = 1;
	
  	public PetUI(Player p, Pet pet) {
		super(p);
		this.pet = pet;
		this.entity = (LivingEntity) pet.getEntity();
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	@Override
	public String getType() {
		return "PET_SETTINGS_MENU";
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p){
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == SLOT_NAME){
			rp.sendMessage("§aEntrez le nom que vous souhaitez attribuer à votre compagnon (prefix codes couleurs : §l&§a) !");
			this.close(true, this.LIST_ID_NAME);
		}else if(slot == SLOT_TOGGLE_STAY){
        	if(this.getPet().canMove()){
                if ((this.getEntity() instanceof Wolf)) {
                    ((Wolf) this.getEntity()).setSitting(true);
                } else if ((this.getEntity() instanceof Cat)) {
                    ((Cat) this.getEntity()).setSitting(true);
                }
        	}else{
                if((this.getEntity() instanceof Wolf)) {
                    ((Wolf) this.getEntity()).setSitting(false);
                }else if ((this.getEntity() instanceof Cat)) {
                    ((Cat) this.getEntity()).setSitting(false);
                }
        	}
            this.getPet().setMove(!this.getPet().canMove());
            this.getMenu().setItem(this.SLOT_TOGGLE_STAY, this.getToggleStay());
        }else if(slot == SLOT_TOGGLE_TYPE){
    		  if(this.getEntity() instanceof Ocelot){
    			  Cat cat = (Cat)this.getEntity();
    			  if(cat.getCatType().equals(Cat.Type.BLACK))cat.setCatType(Cat.Type.SIAMESE);
    			  else if (cat.getCatType().equals(Cat.Type.SIAMESE))cat.setCatType(Cat.Type.ALL_BLACK);
    			  else if (cat.getCatType().equals(Cat.Type.ALL_BLACK))cat.setCatType(Cat.Type.TABBY);
    			  else if (cat.getCatType().equals(Cat.Type.TABBY))cat.setCatType(Cat.Type.BRITISH_SHORTHAIR);
    			  else if (cat.getCatType().equals(Cat.Type.BRITISH_SHORTHAIR))cat.setCatType(Cat.Type.CALICO);
    			  else if (cat.getCatType().equals(Cat.Type.CALICO))cat.setCatType(Cat.Type.JELLIE);
    			  else if (cat.getCatType().equals(Cat.Type.JELLIE))cat.setCatType(Cat.Type.PERSIAN);
    			  else if (cat.getCatType().equals(Cat.Type.PERSIAN))cat.setCatType(Cat.Type.RAGDOLL);
    			  else if (cat.getCatType().equals(Cat.Type.RAGDOLL))cat.setCatType(Cat.Type.RED);
    			  else if (cat.getCatType().equals(Cat.Type.RED))cat.setCatType(Cat.Type.WHITE);
    			  else if (cat.getCatType().equals(Cat.Type.WHITE))cat.setCatType(Cat.Type.BLACK);
    		  }else if(this.getEntity() instanceof Horse){
    			  Horse o = (Horse)this.getEntity();
    			  List<Style> styles = Arrays.asList(Horse.Style.values());
    			  List<Color> colors = Arrays.asList(Horse.Color.values());
    			  Random r = new Random();
    			  o.setStyle(styles.get(r.nextInt(Horse.Style.values().length)));
    			  o.setColor(colors.get(r.nextInt(Horse.Color.values().length)));
    		  }else if(this.getEntity() instanceof Rabbit){
    			  Rabbit o = (Rabbit)this.getEntity();
    			  if(o.getRabbitType().equals(Rabbit.Type.BLACK))o.setRabbitType(Rabbit.Type.BLACK_AND_WHITE);
    			  else if(o.getRabbitType().equals(Rabbit.Type.BLACK_AND_WHITE))o.setRabbitType(Rabbit.Type.BROWN);
    			  else if(o.getRabbitType().equals(Rabbit.Type.BROWN))o.setRabbitType(Rabbit.Type.GOLD);
    			  else if(o.getRabbitType().equals(Rabbit.Type.GOLD))o.setRabbitType(Rabbit.Type.SALT_AND_PEPPER);
    			  else if(o.getRabbitType().equals(Rabbit.Type.SALT_AND_PEPPER))o.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
    			  else if(o.getRabbitType().equals(Rabbit.Type.THE_KILLER_BUNNY))o.setRabbitType(Rabbit.Type.WHITE);
    			  else if(o.getRabbitType().equals(Rabbit.Type.WHITE))o.setRabbitType(Rabbit.Type.BLACK);
    		  }else if(this.getEntity() instanceof Parrot){
    			  Parrot o = (Parrot)this.getEntity();
    			  if(o.getVariant().equals(Parrot.Variant.BLUE))o.setVariant(Parrot.Variant.CYAN);
    			  else if(o.getVariant().equals(Parrot.Variant.CYAN))o.setVariant(Parrot.Variant.GRAY);
    			  else if(o.getVariant().equals(Parrot.Variant.GRAY))o.setVariant(Parrot.Variant.GREEN);
    			  else if(o.getVariant().equals(Parrot.Variant.GREEN))o.setVariant(Parrot.Variant.RED);
    			  else if(o.getVariant().equals(Parrot.Variant.RED))o.setVariant(Parrot.Variant.BLUE);
    		  }else if(this.getEntity() instanceof Wolf || this.getEntity() instanceof Sheep){
    			  DyeColor color;
    			  if(this.getEntity() instanceof Wolf)color = ((Wolf) this.getEntity()).getCollarColor();
    			  else color = ((Sheep) this.getEntity()).getColor();

    			  if(color.equals(DyeColor.BLACK))color = DyeColor.BLUE;
    			  else if(color.equals(DyeColor.BLUE))color = DyeColor.BROWN;
    			  else if(color.equals(DyeColor.BROWN))color = DyeColor.CYAN;
    			  else if(color.equals(DyeColor.CYAN))color = DyeColor.GRAY;
    			  else if(color.equals(DyeColor.GRAY))color = DyeColor.GREEN;
    			  else if(color.equals(DyeColor.GREEN))color = DyeColor.LIGHT_BLUE;
    			  else if(color.equals(DyeColor.LIGHT_BLUE))color = DyeColor.LIME;
    			  else if(color.equals(DyeColor.LIME))color = DyeColor.MAGENTA;
    			  else if(color.equals(DyeColor.MAGENTA))color = DyeColor.ORANGE;
    			  else if(color.equals(DyeColor.ORANGE))color = DyeColor.PINK;
    			  else if(color.equals(DyeColor.PINK))color = DyeColor.PURPLE;
    			  else if(color.equals(DyeColor.PURPLE))color = DyeColor.RED;
    			  else if(color.equals(DyeColor.RED))color = DyeColor.LIGHT_GRAY;
    			  else if(color.equals(DyeColor.LIGHT_GRAY))color = DyeColor.WHITE;
    			  else if(color.equals(DyeColor.WHITE))color = DyeColor.YELLOW;
    			  else if(color.equals(DyeColor.YELLOW))color = DyeColor.BLACK;
    			  
    			  if(this.getEntity() instanceof Wolf)((Wolf) this.getEntity()).setCollarColor(color);
    			  else ((Sheep) this.getEntity()).setColor(color);
    		  }else if(this.getEntity() instanceof Snowman){
    			  ((Snowman)this.getEntity()).setDerp(!((Snowman)this.getEntity()).isDerp());
    		  }else if(this.getEntity() instanceof Pig){
    			  ((Pig)this.getEntity()).setSaddle(!((Pig)this.getEntity()).hasSaddle());
    		  }else rp.sendMessage("§cCette option n'est pas disponible pour ce type de compagnon !");
          }else if(slot == SLOT_TOGGLE_AGE){
        	  if(this.getEntity() instanceof Ageable){
        		  if(((Ageable) this.getEntity()).isAdult())((Ageable) this.getEntity()).setBaby();
        		  else ((Ageable) this.getEntity()).setAdult();
            	  e.setCurrentItem(this.getToggleAge());
        	  }else rp.sendMessage("§cCette option n'est pas disponible pour ce type de compagnon !");
          }else if(slot == SLOT_PEARLS){
        	  Core.uiManager.requestUI(new PearlsUI(this.getHolder(), this.getPet()));
          }else if(slot == SLOT_DISTINCTIONS){
        	  Core.uiManager.requestUI(new PetDistinctionsMenu(this.getHolder(), this.getPet()));
          }
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		//not listening
	}
	
	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_NAME){
					if(ChatColor.stripColor(this.getMessage()).length() > 16)rp.sendMessage("§cLe nom de votre compagnon est trop long ! Il ne doit contenir que 16 caractères.");
					else this.getPet().setName(this.getMessage());
				}
			}
		}
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getPet().getName(),32));
	    getMenu().setItem(SLOT_NAME, this.getName());
	    getMenu().setItem(SLOT_TOGGLE_STAY, this.getToggleStay());
	    getMenu().setItem(SLOT_TOGGLE_TYPE, this.getToggleType());
	    if(this.getEntity() instanceof Ageable)getMenu().setItem(SLOT_TOGGLE_AGE, this.getToggleAge());
	    getMenu().setItem(SLOT_DISTINCTIONS, this.getDistinctions());
	    getMenu().setItem(SLOT_PEARLS, this.getPearls());
		return this.getHolder().openInventory(getMenu()) != null;
	}

	private ItemStack getName(){
		  ItemStack name = new ItemStack(Material.MAP);
		    ItemMeta im = name.getItemMeta();
		    im.setDisplayName(("§e§lRENOMMER"));
		    im.setLore(Arrays.asList("§7" + ("Donnez-lui un nom à sa hauteur"), "§7" + ("en utilisant les codes couleurs minecraft !")));
		    name.setItemMeta(im);
		    return name;
	}
	private ItemStack getToggleStay(){
		  ItemStack togsta = new ItemStack(Material.LEVER);
		    ItemMeta im2 = togsta.getItemMeta();
		    if(this.getPet().canMove())im2.setDisplayName(("§e§lIMMOBILISER"));
		    else im2.setDisplayName(("§e§lMOBILISER"));
		    im2.setLore(Arrays.asList("§7" + ("Un compagnon immobilisé sera téléporté"), "§7" + ("lorsque vous serez à 50 blocs de lui.")));
		    togsta.setItemMeta(im2);
		    return togsta;
	}
	private ItemStack getToggleType(){
		ItemStack togtyp = new ItemStack(Material.APPLE);
	    ItemMeta im4 = togtyp.getItemMeta();
	    im4.setDisplayName(("§e§lAPPARENCE"));
	    im4.setLore(Arrays.asList("§7" + ("Essayez toutes les apparences"), "§7" + ("possibles pour ce type de compagnon.")));
	    togtyp.setItemMeta(im4);
	    return togtyp;
	}
	private ItemStack getToggleAge(){
		ItemStack togage = new ItemStack(Material.MILK_BUCKET);
	    ItemMeta im5 = togage.getItemMeta();
	    if(this.getEntity() instanceof Ageable){
	    	if(((Ageable)this.getEntity()).isAdult()){
	    		im5.setDisplayName(("§e§lRAJEUNIR"));
	    	}else im5.setDisplayName(("§e§lVIEILLIR"));
	    }
	    togage.setItemMeta(im5);
	    return togage;
	}
	private ItemStack getDistinctions(){
		ItemStack pearls = new ItemStack(Material.BOOK, 1);
		ItemMeta pearlsm = pearls.getItemMeta();
		pearlsm.setDisplayName("§a§lDISTINCTIONS");
		pearls.setItemMeta(pearlsm);
		return pearls;
	}
	private ItemStack getPearls(){
		ItemStack pearls = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta pearlsm = pearls.getItemMeta();
		pearlsm.setDisplayName("§a§l" + ("PERLES"));
		pearls.setItemMeta(pearlsm);
		return me.pmilon.RubidiaCore.utils.Utils.setGlowingWithoutAttributes(pearls);
	}

	public LivingEntity getEntity() {
		return entity;
	}
	
	public Pet getPet(){
		return pet;
	}
}