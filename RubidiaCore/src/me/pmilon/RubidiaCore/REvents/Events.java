package me.pmilon.RubidiaCore.REvents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.pmilon.RubidiaCore.REvents.Event.EventType;
import me.pmilon.RubidiaCore.utils.Configs;

public class Events {

	public static List<Event> currentEvents = new ArrayList<Event>();
	
	public static void onEnable(Plugin plugin){
		if(Configs.getDatabase().contains("events")){
			for(String eventID : Configs.getDatabase().getConfigurationSection("events").getKeys(false)){
				String path = "events." + eventID;
				Event event = new Event(EventType.valueOf(Configs.getDatabase().getString(path + ".type")),
						Configs.getDatabase().getString(path + ".subtitle"),
						Configs.getDatabase().getLong(path + ".startDate"),
						Configs.getDatabase().getLong(path + ".duration"),
						Configs.getDatabase().getDouble(path + ".factor"),
						Configs.getDatabase().getBoolean(path + ".started"));
				currentEvents.add(event);
			}
		}
		Bukkit.getPluginManager().registerEvents(new EventListener(), plugin);
	}
	
	public static void save(){
		Configs.getDatabase().set("events", null);
		for(int i = 0;i < currentEvents.size();i++){
			Event event = currentEvents.get(i);
			if(event.getStartDate()+event.getDuration() >= System.currentTimeMillis()){
				String path = "events." + i;
				Configs.getDatabase().set(path + ".type", event.getType().toString());
				Configs.getDatabase().set(path + ".subtitle", event.getSubtitle());
				Configs.getDatabase().set(path + ".startDate", event.getStartDate());
				Configs.getDatabase().set(path + ".duration", event.getDuration());
				Configs.getDatabase().set(path + ".factor", event.getFactor());
				Configs.getDatabase().set(path + ".started", event.isStarted());
			}
		}
	}
}
