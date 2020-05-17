package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Gender;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaCore.ranks.Ranks;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.command.CommandSender;

public class StatisticsCommandExecutor extends HybridAdminCommandExecutor {
	
	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		double[] sex = new double[]{0,0,0}, classes = new double[]{0,0,0,0,0,0};
		double sum_age = 0,refd_age = 0,min_age = 100,max_age = 0,sum_lv = 0;
		int sum = 0,saves = 0;
		for(RPlayer rp : Core.rcoll.data()){
			if(System.currentTimeMillis()-rp.getLastConnectionDate() <= Ranks.TIMEOUT){
				if(rp.getSex().equals(Gender.MALE))sex[0]++;
				else if(rp.getSex().equals(Gender.FEMALE))sex[1]++;
				else if(rp.getSex().equals(Gender.UNKNOWN))sex[2]++;
				if(rp.getBirthDate() > 10*Utils.MILLIS_IN_YEAR && rp.getBirthDate() < System.currentTimeMillis()-6*Utils.MILLIS_IN_YEAR){
					double age = (double) ((long)(System.currentTimeMillis()-rp.getBirthDate()))/Utils.MILLIS_IN_YEAR;
					if(age < min_age)min_age = age;
					else if(age > max_age)max_age = age;
					sum_age += age;
					refd_age += 1.0;
				}
				for(SPlayer sp : rp.getSaves()){
					if(sp != null){
						if(sp.getRClass().equals(RClass.VAGRANT))classes[0]++;
						else{
							if(rp.getRClass().equals(RClass.MAGE))classes[3]++;
							else if(rp.getRClass().equals(RClass.RANGER))classes[2]++;
							else if(rp.getRClass().equals(RClass.PALADIN))classes[1]++;
							else if(rp.getRClass().equals(RClass.ASSASSIN))classes[4]++;
							classes[5]++;
						}
						sum_lv += sp.getRLevel();
						saves++;
					}
				}
				sum++;
			}
		}
		sender.sendMessage("§6Percentage of unknown: §a" + Utils.round(sex[2]*100/sum, 2) + "%");
		sender.sendMessage("§6Percentage of male: §a" + Utils.round(sex[0]*100/(sex[0]+sex[1]), 2) + "%");
		sender.sendMessage("§6Percentage of female: §a" + Utils.round(sex[1]*100/(sex[0]+sex[1]), 2) + "%");
		sender.sendMessage("§6Average age: §a" + Utils.round(sum_age/refd_age, 2) + " (" + Utils.round(min_age, 2) + " - " + Utils.round(max_age, 2) + ")");
		sender.sendMessage("§6Classes repartition: §a" + Utils.round(classes[1]*100/classes[5], 2) + "% §b" + Utils.round(classes[2]*100/classes[5], 2) + "% §e" + Utils.round(classes[3]*100/classes[5], 2) + "% §c" + Utils.round(classes[4]*100/classes[5], 2) + "%");
		sender.sendMessage("§6Average level: §a" + Utils.round(sum_lv/saves, 2));
	}

}
