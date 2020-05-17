package me.pmilon.RubidiaQuests.pnjs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler.PNJType;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.shops.PNJShop;
import me.pmilon.RubidiaQuests.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.Location;

public class PNJManager {

	public static int loaded = 0;
	
	public final static HashMap<Player, List<Villager>> pnjTemps = new HashMap<Player, List<Villager>>();
	public final static HashMap<Player, List<Villager>> pnjTokillTemps = new HashMap<Player, List<Villager>>();
	
	public final static HashMap<String, PNJSession> pnjs = new HashMap<String, PNJSession>();
	private final QuestsPlugin plugin;
	
	public PNJManager(QuestsPlugin core){
		this.plugin = core;
		Bukkit.getPluginManager().registerEvents(new PNJListener(), core);
		this.onEnable();
	}
	
	public void onEnable(){
		if(Configs.getPNJConfig().contains("pnjs")){
			for(String uuid : Configs.getPNJConfig().getConfigurationSection("pnjs").getKeys(false)){
				String path = "pnjs." + uuid;
				PNJType type = PNJType.valueOf(Configs.getPNJConfig().getString(path + ".type"));
				String title = Configs.getPNJConfig().getString(path + ".title");
				String name = Configs.getPNJConfig().getString(path + ".name");
				int age = Configs.getPNJConfig().getInt(path + ".age");
				boolean fix = Configs.getPNJConfig().getBoolean(path + ".fix");
				Location location = (Location) Configs.getPNJConfig().get(path + ".location");
				location.getChunk().load();
				if(type.equals(PNJType.QUEST)){
					List<Quest> quests = new ArrayList<Quest>();
					for(String quest : Configs.getPNJConfig().getStringList(path + ".quests")){
						quests.add(Quest.get(quest));
					}
					this.spawn(new QuestPNJ(uuid, title, name, location, age, quests, Configs.getPNJConfig().getString(path + ".noQuestDialog"), fix));
				}else if(type.equals(PNJType.INHABITANT)){
					this.spawn(new InhabitantPNJ(uuid, title, name, location, age, Configs.getPNJConfig().getStringList(path + ".dialogs"), fix));
				}else if(type.equals(PNJType.SHOP)){
					PNJShop shop = QuestsPlugin.shopColl.get(Configs.getPNJConfig().getString(path + ".shopUUID"));
					if(shop == null)shop = QuestsPlugin.shopColl.addDefault(UUID.randomUUID().toString());
					List<String> dialogs;
					if(Configs.getPNJConfig().contains(path + ".dialog")) {
						dialogs = Arrays.asList(Configs.getPNJConfig().getString(path + ".dialog"));
						Configs.getPNJConfig().set(path + ".dialog", null);
					} else {
						dialogs = Configs.getPNJConfig().getStringList(path + ".dialogs");
					}
					this.spawn(new ShopPNJ(uuid, title, name, location, age, fix, shop, dialogs));
				}else if(type.equals(PNJType.BANK)){
					List<String> dialogs;
					if(Configs.getPNJConfig().contains(path + ".dialog")) {
						dialogs = Arrays.asList(Configs.getPNJConfig().getString(path + ".dialog"));
						Configs.getPNJConfig().set(path + ".dialog", null);
					} else {
						dialogs = Configs.getPNJConfig().getStringList(path + ".dialogs");
					}
					this.spawn(new BankPNJ(uuid, name, location, age, fix, dialogs));
				}else if(type.equals(PNJType.SMITH)){
					List<String> dialogs;
					if(Configs.getPNJConfig().contains(path + ".dialog")) {
						dialogs = Arrays.asList(Configs.getPNJConfig().getString(path + ".dialog"));
						Configs.getPNJConfig().set(path + ".dialog", null);
					} else {
						dialogs = Configs.getPNJConfig().getStringList(path + ".dialogs");
					}
					this.spawn(new SmithPNJ(uuid, name, location, age, fix, dialogs));
				}else if(type.equals(PNJType.PASTOR)){
					this.spawn(new PastorPNJ(uuid, name, location, age, fix, (Location)Configs.getPNJConfig().get(path + ".location1"), (Location)Configs.getPNJConfig().get(path + ".location2")));
				}else if(type.equals(PNJType.PASSER)){
					this.spawn(new PasserPNJ(uuid, name, location, age, Configs.getPNJConfig().getStringList(path + ".dialogs"), fix, Configs.getPNJConfig().getString(path + ".targetName"), (Location)Configs.getPNJConfig().get(path + ".targetLocation")));
				}else if(type.equals(PNJType.HOST)){
					this.spawn(new HostPNJ(uuid, name, location, age, Configs.getPNJConfig().getString(path + ".houseUniqueId")));
				}
				PNJManager.loaded++;
				if(PNJManager.loaded%100 == 0){
					QuestsPlugin.console.sendMessage("§6LOADED §e" + PNJManager.loaded + " §6PNJs");
				}
			}
		}
		QuestsPlugin.console.sendMessage("§6LOADED §e" + PNJManager.loaded + " §6PNJs");
	}

	public static void save(boolean stop) {
		if (stop) QuestsPlugin.console.sendMessage("§a   Saving PNJs...");
		if(!pnjs.isEmpty()){
			for(PNJSession handler : pnjs.values()){
				if(stop) handler.getPNJHandler().remove();
				else handler.getPNJHandler().save();
				if(stop) QuestsPlugin.console.sendMessage("§6Saved §e" + handler.getPNJHandler().getName());
			}
		}
		Configs.savePNJConfig();
	}
	
	public boolean spawn(PNJHandler pnj){
		boolean spawn = pnj.spawn(false);
		if(pnjs.containsKey(pnj.getEntityUUID().toString()))pnjs.get(pnj.getEntityUUID().toString()).getPNJHandler().remove();
		PNJSession session = new PNJSession(pnj);
		pnjs.put(pnj.getEntityUUID().toString(), session);
		return spawn;
	}
	
	public static PNJSession getSession(Villager pnj){
		if(PNJManager.isPNJ(pnj)){
			if(pnjs.containsKey(pnj.getUniqueId().toString())){
				return pnjs.get(pnj.getUniqueId().toString());
			}
		}
		return null;
	}
	
	public static boolean isPNJ(Villager pnj){
		return pnj.hasMetadata("PNJ");
	}
	
	public QuestsPlugin getPlugin(){
		return this.plugin;
	}
	
	public void remove(String pnj){
		if(pnjs.containsKey(pnj)){
			pnjs.remove(pnj);
		}
	}

	public static PNJHandler getPNJ(String name){
		for(PNJSession session : pnjs.values()){
			if(session.getPNJHandler().getName().equals(name))return session.getPNJHandler();
		}
		return null;
	}
	public static PNJHandler getPNJByUniqueId(String uuid){
		for(PNJSession session : pnjs.values()){
			if(session.getPNJHandler().getUniqueId().equals(uuid))return session.getPNJHandler();
		}
		return null;
	}

	public String getPNJUUID(String name){
		for(PNJSession session : pnjs.values()){
			if(session.getPNJHandler().getName().equals(name))return session.getPNJHandler().getUniqueId();
		}
		return null;
	}
}
