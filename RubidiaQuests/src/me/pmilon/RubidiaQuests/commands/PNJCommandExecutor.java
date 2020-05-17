package me.pmilon.RubidiaQuests.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.houses.House;
import me.pmilon.RubidiaQuests.pnjs.BankPNJ;
import me.pmilon.RubidiaQuests.pnjs.HostPNJ;
import me.pmilon.RubidiaQuests.pnjs.InhabitantPNJ;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler.PNJType;
import me.pmilon.RubidiaQuests.pnjs.PasserPNJ;
import me.pmilon.RubidiaQuests.pnjs.PastorPNJ;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.pnjs.ShopPNJ;
import me.pmilon.RubidiaQuests.pnjs.SmithPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.utils.Utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.sqlite.util.StringUtils;

public class PNJCommandExecutor extends PlayerAdminCommandExecutor {

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			PNJType type = null;
			try{
				type = PNJType.valueOf(args[0].toUpperCase());
			}catch(Exception ex){
			}
			
			if(type != null){
				String uuid = UUID.randomUUID().toString();
				String name = Utils.generateName();
				Location location = player.getLocation();
				switch (type){
				case QUEST:
					List<Quest> quests = new ArrayList<Quest>();
					QuestsPlugin.pnjManager.spawn(new QuestPNJ(uuid, "CITOYEN", name, location, 0, quests, "Aucune quête disponible", false));
					break;
				case INHABITANT:
					QuestsPlugin.pnjManager.spawn(new InhabitantPNJ(uuid, "CITOYEN", name, location, 0, new ArrayList<String>(), false));
					break;
				case SHOP:
					QuestsPlugin.pnjManager.spawn(new ShopPNJ(uuid, "MARCHAND", name, location, 0, false, QuestsPlugin.shopColl.addDefault(uuid), new ArrayList<String>()));
					break;
				case BANK:
					QuestsPlugin.pnjManager.spawn(new BankPNJ(uuid, name, location, 0, false, new ArrayList<String>()));
					break;
				case SMITH:
					QuestsPlugin.pnjManager.spawn(new SmithPNJ(uuid, name, location, 0, false, new ArrayList<String>()));
					break;
				case PASTOR:
					QuestsPlugin.pnjManager.spawn(new PastorPNJ(uuid, name, location, 0, false, location, location));
					break;
				case PASSER:
					QuestsPlugin.pnjManager.spawn(new PasserPNJ(uuid, name, location, 0, new ArrayList<String>(), false, "ici", location));
					break;
				case HOST:
					House house = QuestsPlugin.houseColl.addDefault(UUID.randomUUID().toString());
					house.reset(location);
					QuestsPlugin.pnjManager.spawn(new HostPNJ(uuid, name, location, 0, house.getUniqueId()));
					break;
				}
			}else rp.sendMessage("§cType de PNJ valides : " + StringUtils.join(Arrays.asList(PNJType.values()).stream().map(PNJType::toString).collect(Collectors.toList()), ", "));
		}else rp.sendMessage("§cUtilisez /pnj [TYPE]");
	}

}
