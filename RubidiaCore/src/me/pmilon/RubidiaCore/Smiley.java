package me.pmilon.RubidiaCore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Smiley {

	public static List<String> smileys = Arrays.asList("D:","o:)",":s",":*",":(","8)",":o","x.x",":'(",":D",";)",":d",":3",":p","^^");
	public static List<String> urls = Arrays.asList("636e26c44659e8148ed58aa79e4d60db595f426442116f81b5415c2446ed8","3e1debc73231f8ed4b69d5c3ac1b1f18f3656a8988e23f2e1bdbc4e85f6d46a","f720df911c052377065408db78a25c678f791eb944c063935ae86dbe51c71b","545bd18a2aaf469fad72e52cde6cfb02bfbaa5bfed2a8151277f779ebcdcec1","14968ac5af3146826fa2b0d4dd114fda197f8b28f4750553f3f88836a21fac9","868f4cef949f32e33ec5ae845f9c56983cbe13375a4dec46e5bbfb7dcb6","bc2b9b9ae622bd68adff7180f8206ec4494abbfa130e94a584ec692e8984ab2","b371e4e1cf6a1a36fdae27137fd9b8748e6169299925f9af2be301e54298c73","1f1b875de49c587e3b4023ce24d472ff27583a1f054f37e73a1154b5b5498","5059d59eb4e59c31eecf9ece2f9cf3934e45c0ec476fc86bfaef8ea913ea710","f4ea2d6f939fefeff5d122e63dd26fa8a427df90b2928bc1fa89a8252a7e","3baabe724eae59c5d13f442c7dc5d2b1c6b70c2f83364a488ce5973ae80b4c3","3636f2724aa6aa4de7ac46c19f3c845fb14847a518c8f7e03d792c82effb1","7ffaccf17879b17891fc5ef66472cc066a85bfa31b6d786c32afee4796068d","52e98165deef4ed621953921c1ef817dc638af71c1934a4287b69d7a31f6b8");
	private static List<Player> smileying = new ArrayList<Player>();

	public static boolean hasSmiley(String message){
		return getSmiley(message) != null;
	}
	
	public static String getSmiley(String message){
		for(String smiley : Smiley.smileys){
			for(String string : message.split(" ")){
				if(string.equals(smiley))return smiley;
			}
		}
		return null;
	}
	
	public static boolean isSmileying(Player player){
		return Smiley.smileying.contains(player);
	}
	
	public static void setSmileying(Player player, boolean value){
		if(value)Smiley.smileying.add(player);
		else if(Smiley.isSmileying(player))Smiley.smileying.remove(player);
	}
	
	public static ItemStack base(String url){
	    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	    profile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + url + "\"}}}")));
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
	}

}
