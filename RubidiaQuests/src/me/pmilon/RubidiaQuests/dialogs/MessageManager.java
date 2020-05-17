package me.pmilon.RubidiaQuests.dialogs;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJManager;
import me.pmilon.RubidiaQuests.pnjs.PNJSession;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler.PNJType;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MessageManager {

	public static List<String> SPECIAL_WORDS = Arrays.asList("être","quête","enquête","requête","château","forêt","peut-être","même","âme","âne","arrêt","arrête","arrêter","aussitôt","bâtiment","bientôt","blême","châtiment","contrôle","contrôler","côtoyer","dégât","dégâts","gâcher","gâteau","hâte","honnête","impôt","intérêt","mâcher","ôter","pêche","pêcher","plutôt","tôt","relâcher","rêve","rôle","théâtre","tantôt","trêve","âge","ancêtre","apparaître","appât","arôme","bâton","bêtise","blâme","blâmer","boîte","brûler","bûche","bûcheron");
	public static List<String> SPECIAL_WORDS_RAW = normalize(SPECIAL_WORDS);
	
	public static List<String> normalize(List<String> strings){
		List<String> raw = new ArrayList<String>();
		for(String string : strings){
			raw.add(Normalizer.normalize(string, Normalizer.Form.NFD));
		}
		return raw;
	}
	
	public static String pnjDialog(PNJHandler pnj, DialogType type, Player p, String name, String dialog){
		RPlayer rp = RPlayer.get(p);
		GMember member = GMember.get(p);
		float pitch = 1;
		if(pnj.isBaby())pitch = 1.3F;
		if(dialog.contains("%yes"))p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, pitch);
		else if(dialog.contains("%no"))p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, pitch);
		else if(dialog.contains("%hit"))p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, pitch);
		else if(dialog.contains("%death"))p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1, pitch);
		else if(dialog.contains("%idle"))p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, pitch);
		else if(dialog.contains("%haggle"))p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, pitch);
		else p.playSound(p.getLocation(), type.getSound(), 1, pitch);
		String guildName = "";
		if(member.hasGuild()){
			guildName = "§e" + member.getGuild().getName() + "§a";
		}
		String companion = "";
		if(rp.fiance != null){
			companion = rp.fiance.getName();
		}else if(rp.getCompanion() != null){
			companion = "§e" + rp.getCompanion().getName() + "§a";
		}
		return "§2§l[" + ChatColor.stripColor(name) + "] §a" + dialog.replaceAll("%player", "§e" + p.getName() + "§a").replaceAll("%level", "§e" + String.valueOf(rp.getRLevel()) + "§a").replaceAll("%class", "§e" + rp.getClassName() + "§a").replaceAll("%evolution", "§e" + rp.getEvolutionClassName() + "§a").replaceAll("%guild", guildName).replaceAll("%companion", companion).replaceAll("%yes", "").replaceAll("%no", "").replaceAll("%hit", "").replaceAll("%death", "").replaceAll("%idle", "").replaceAll("%haggle", "").replaceAll("§r", "§a");
	}
	
	public static String filterDialog(String message){
		String dialog = "";
		if(message != null){
			for(String code : new String[]{"a","b","c","d","e","f","0","1","2","3","4","5","6","7","8","9"}){//every color but formatting codes
				message = message.replaceAll("§" + code, "");
			}
			String[] ponctuations = new String[]{"...",".",",","\"","\'"};
			String[] words = message.split(" ");
			for(int i = 0;i < words.length;i++){
				String word = words[i];
				String w_raw = Normalizer.normalize(word, Normalizer.Form.NFD);
				if(SPECIAL_WORDS_RAW.contains(w_raw))word = SPECIAL_WORDS.get(SPECIAL_WORDS_RAW.indexOf(w_raw));
				String prefix = "";
				if(word.startsWith("d\'")){
					prefix = "d'";
					word = word.split("d\'")[1];
				}
					
				String ponctuation = "";
				for(String ponct : ponctuations){
					if(word.endsWith(ponct)){
						word = word.replaceAll(Pattern.quote(ponct), "");
						ponctuation = ponct;
						break;
					}
				}
				if(!word.equals("?") && !word.equals("!") && !word.equals(":")){
					for(PNJSession session : PNJManager.pnjs.values()){
						PNJHandler pnj = session.getPNJHandler();
						if(!pnj.getType().equals(PNJType.SHOP)){
							if(pnj.getName().equalsIgnoreCase(word)){
								word = "§e" + pnj.getName() + "§r";
								break;
							}
						}
					}
					if(Configs.getCitiesConfig().contains("cities")){
						for(String city : Configs.getCitiesConfig().getConfigurationSection("cities").getKeys(false)){
							if(city.equalsIgnoreCase(word)){
								word = "§e" + city + "§r";
								break;
							}
						}
					}
				}
				dialog += prefix + word + ponctuation + (i < words.length-1 ? " " : "");
			}
		}
		return dialog;
	}
	
}
