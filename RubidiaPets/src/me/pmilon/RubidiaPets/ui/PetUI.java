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
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Wolf;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PetUI extends UIHandler{
	
  private final Pet pet;
  private final LivingEntity entity;
  private int SLOT_NAME = 0;
  private int SLOT_TOGGLE_STAY = 2;
  private int SLOT_TOGGLE_TYPE = 3;
  private int SLOT_TOGGLE_AGE = 4;
  private int SLOT_DISTINCTIONS = 6;
  private int SLOT_PEARLS = 7;
  private int SLOT_STATE = 8;
  
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
                } else if ((this.getEntity() instanceof Ocelot)) {
                    ((Ocelot) this.getEntity()).setSitting(true);
                }
        	}else{
                if((this.getEntity() instanceof Wolf)) {
                    ((Wolf) this.getEntity()).setSitting(false);
                }else if ((this.getEntity() instanceof Ocelot)) {
                    ((Ocelot) this.getEntity()).setSitting(false);
                }
        	}
            this.getPet().setMove(!this.getPet().canMove());
            this.getMenu().setItem(this.SLOT_TOGGLE_STAY, this.getToggleStay());
        }else if(slot == SLOT_TOGGLE_TYPE){
    		  if(this.getEntity() instanceof Ocelot){
    			  Ocelot ocelot = (Ocelot)this.getEntity();
    			  List<Ocelot.Type> types = Arrays.asList(Ocelot.Type.values());
    			  int index = types.indexOf(ocelot.getCatType());
    			  ocelot.setCatType(types.get((index + 1) % types.size()));
    		  }else if(this.getEntity() instanceof Horse){
    			  Horse o = (Horse)this.getEntity();
    			  List<Style> styles = Arrays.asList(Horse.Style.values());
    			  List<Color> colors = Arrays.asList(Horse.Color.values());
    			  Random r = new Random();
    			  o.setStyle(styles.get(r.nextInt(Horse.Style.values().length)));
    			  o.setColor(colors.get(r.nextInt(Horse.Color.values().length)));
    		  }else if(this.getEntity() instanceof Villager){
    			  Villager villager = (Villager)this.getEntity();
    			  List<Profession> professions = Arrays.asList(Profession.values());
    			  int index = professions.indexOf(villager.getProfession());
    			  Profession newProfession = professions.get((index + 1) % professions.size());
    			  while (index < 2 * professions.size()) {
        			  try {
            			  villager.setProfession(newProfession);
            			  break;
        			  } catch (Exception ex) {
        				  index += 1;
        				  newProfession = professions.get((index + 1) % professions.size());
        			  }
    			  }
    		  }else if(this.getEntity() instanceof Rabbit){
    			  Rabbit rabbit = (Rabbit)this.getEntity();
    			  List<Rabbit.Type> types = Arrays.asList(Rabbit.Type.values());
    			  int index = types.indexOf(rabbit.getRabbitType());
    			  rabbit.setRabbitType(types.get((index + 1) % types.size()));
    		  }else if(this.getEntity() instanceof Llama){
    			  Llama llama = (Llama)this.getEntity();
    			  List<Llama.Color> colors = Arrays.asList(Llama.Color.values());
    			  int index = colors.indexOf(llama.getColor());
    			  llama.setColor(colors.get((index + 1) % colors.size()));
    		  }else if(this.getEntity() instanceof Parrot){
    			  Parrot parrot = (Parrot)this.getEntity();
    			  List<Parrot.Variant> variants = Arrays.asList(Parrot.Variant.values());
    			  int index = variants.indexOf(parrot.getVariant());
    			  parrot.setVariant(variants.get((index + 1) % variants.size()));
    		  }else if(this.getEntity() instanceof Wolf || this.getEntity() instanceof Sheep){
    			  DyeColor color;
    			  if(this.getEntity() instanceof Wolf)color = ((Wolf) this.getEntity()).getCollarColor();
    			  else color = ((Sheep) this.getEntity()).getColor();
    			  
    			  List<DyeColor> colors = Arrays.asList(DyeColor.values());
    			  int index = colors.indexOf(color);
    			  color = colors.get((index + 1) % colors.size());
    			  
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
          }else if(slot == SLOT_STATE){
				if(this.getPet().isActive()){
					this.getPet().despawn();
					this.getPet().setActive(false);
					rp.sendMessage("§cVous avez rangé §4" + pet.getName() + "§c.");
				}else{
					this.getPet().spawn(this.getHolder());
					this.getPet().setActive(true);
					rp.sendMessage("§aVous avez fait sortir §2" + pet.getName() + "§a.");
				}
				this.menu.setItem(slot, this.getToggleState());
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
	    getMenu().setItem(SLOT_STATE, this.getToggleState());
		return this.getHolder().openInventory(getMenu()) != null;
	}

	private ItemStack getName(){
		  ItemStack name = new ItemStack(Material.MAP);
		    ItemMeta im = name.getItemMeta();
		    im.setDisplayName("§6§lRenommer");
		    im.setLore(Arrays.asList("§eDonnez-lui un nom à sa hauteur", "§een utilisant les codes couleurs minecraft !"));
		    name.setItemMeta(im);
		    return name;
	}
	private ItemStack getToggleStay(){
		  ItemStack togsta = new ItemStack(Material.LEVER);
		    ItemMeta im2 = togsta.getItemMeta();
		    if(this.getPet().canMove())im2.setDisplayName(("§e§lIMMOBILISER"));
		    else im2.setDisplayName("§6§lMobiliser");
		    im2.setLore(Arrays.asList("§eUn compagnon immobilisé sera téléporté", "§elorsque vous serez à 50 blocs de lui."));
		    togsta.setItemMeta(im2);
		    return togsta;
	}
	private ItemStack getToggleType(){
		ItemStack togtyp = new ItemStack(Material.CREEPER_HEAD);
	    ItemMeta im4 = togtyp.getItemMeta();
	    im4.setDisplayName("§6§lApparence");
	    im4.setLore(Arrays.asList("§eEssayez toutes les apparences", "§epossibles pour ce type de compagnon."));
	    togtyp.setItemMeta(im4);
	    return togtyp;
	}
	private ItemStack getToggleAge(){
		ItemStack togage = new ItemStack(Material.EGG);
	    ItemMeta im5 = togage.getItemMeta();
	    if(this.getEntity() instanceof Ageable){
	    	if(((Ageable)this.getEntity()).isAdult()){
	    		im5.setDisplayName(("§6§lRajeunir"));
	    	}else im5.setDisplayName(("§6§lViellir"));
	    }
	    togage.setItemMeta(im5);
	    return togage;
	}
	private ItemStack getDistinctions(){
		ItemStack pearls = new ItemStack(Material.BOOK, 1);
		ItemMeta pearlsm = pearls.getItemMeta();
		pearlsm.setDisplayName("§a§lDinstinctions");
		pearlsm.setLore(Arrays.asList("§eAméliorez les distinctions de votre compagnon", "§epour en faire votre meilleur équipié !"));
		pearls.setItemMeta(pearlsm);
		return pearls;
	}
	private ItemStack getPearls(){
		ItemStack pearls = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta pearlsm = pearls.getItemMeta();
		pearlsm.setDisplayName("§a§lPerle");
		pearlsm.setLore(Arrays.asList("§eActivez vos perles de compagnons ici", "§epour profiter de leurs bonus !"));
		pearls.setItemMeta(pearlsm);
		return me.pmilon.RubidiaCore.utils.Utils.setGlowingWithoutAttributes(pearls);
	}
	private ItemStack getToggleState(){
		ItemStack back = new ItemStack(Material.BROWN_SHULKER_BOX, 1);
		ItemMeta backm = back.getItemMeta();
		backm.setDisplayName(this.getPet().isActive() ? "§c§lRanger" : "§a§lSortir");
		back.setItemMeta(backm);
		return me.pmilon.RubidiaCore.utils.Utils.setGlowingWithoutAttributes(back);
	}

	public LivingEntity getEntity() {
		return entity;
	}
	
	public Pet getPet(){
		return pet;
	}
}