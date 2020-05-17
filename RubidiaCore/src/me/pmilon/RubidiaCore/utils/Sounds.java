package me.pmilon.RubidiaCore.utils;

import me.pmilon.RubidiaCore.Core;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;

public class Sounds {

	public static void playFoundTreasure(final Player player){
		for(int i = 0;i < 7;i++){
			final int index = i;
			Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
				public void run(){
					player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.values()[index]));
				}
			}, i);
		}
		Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
			public void run(){
				player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.C));
			}
		}, 11);
		Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
			public void run(){
				player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.F));
			}
		}, 14);
	}

	public static void playQuestGiveUp(final Player player){
		for(int i = 3;i >= 0;i--){
			final int index = i;
			Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
				public void run(){
					player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.values()[index*2]));
				}
			}, (long) (7.5-i*2.5));
			Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
				public void run(){
					player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.values()[index*2]));
				}
			}, (long) (7.5-i*2.5)*2);
		}
	}
	
}
