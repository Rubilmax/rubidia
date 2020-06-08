package me.pmilon.RubidiaCore.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Particle;

public class Settings {

	public static final int ABILITY_CLICK_TICKS = 17;
	
	public static final float DEFAULT_WALK_SPEED = .2F;
	public static final float DEFAULT_FLY_SPEED = .1F;
	
	public static final int LEVEL_JOB = 10;
	public static final int LEVEL_MAX = 150;
	
	public static final double DEFAULT_CRITICAL_STRIKE_CHANCE = 0.15;
	public static final double DEFAULT_BLOCK_CHANCE = 0.13;

	public static final double STRENGTH_FACTOR_MELEE_DAMAGES_ON_MELEE = .004;
	public static final double STRENGTH_FACTOR_MELEE_RANGE_DAMAGES_ON_MELEE = .0025;
	public static final double STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE = .25;
	public static final double ENDURANCE_FACTOR_MAXHEALTH = .2;
	public static final double ENDURANCE_FACTOR_NRJREGEN = .02;
	public static final double ENDURANCE_FACTOR_DEFENSE = .0025;
	public static final double ENDURANCE_FACTOR_ABILITY_DEF = .004;
	public static final double AGILITY_FACTOR_RANGE_DAMAGES_ON_RANGE = .004;
	public static final double AGILITY_FACTOR_ATTACK_SPEED = .004;
	public static final double AGILITY_FACTOR_CRITICAL_STRIKE_DAMAGES = .006;
	public static final double AGILITY_FACTOR_CRITICAL_STRIKE_CHANCE = .0025;
	public static final double INTELLIGENCE_FACTOR_MAGIC_DAMAGES_ON_MAGIC = .004;
	public static final double INTELLIGENCE_FACTOR_ABILITY_DAMAGES = .0025;
	public static final double INTELLIGENCE_FACTOR_MAXNRJ = 2;
	public static final double PERCEPTION_FACTOR_REGENERATION = .05;
	public static final double PERCEPTION_FACTOR_BLOCK_CHANCE = .002;
	public static final double PERCEPTION_FACTOR_LOOT_CHANCE = .002;
	public static final double PERCEPTION_FACTOR_LIFT_COST = .0035;

	public static final int SWORDS_DMG_RANGE_MAX = 29;
	public static final int AXES_DMG_RANGE_MAX = 61;
	public static final int BOWS_DMG_RANGE_MAX = 18;
	public static final int WANDS_DMG_RANGE_MAX = 43;
	public static final int WEAPONS_DMG_MAX = 390;
	public static final double WEAPONS_SPD_MAX = 1.6;
	public static final double WEAPONS_SPD_MIN = .3334;
	public static final int HELMETS_DEF_MAX = 160;
	public static final int HELMETS_DEF_RANGE_MAX = 35;
	public static final int ARMORS_DEF_MAX = 210;
	public static final int ARMORS_DEF_RANGE_MAX = 56;
	public static final int GAUNTLETS_DEF_MAX = 90;
	public static final int GAUNTLETS_DEF_RANGE_MAX = 19;
	public static final int BOOTS_DEF_MAX = 130;
	public static final int BOOTS_DEF_RANGE_MAX = 27;
	public static final int SHIELDS_DEF_MAX = 120;
	public static final int SHIELDS_DEF_RANGE_MAX = 47;
	public static final double PALADIN_DEF_FACTOR = 1.31;
	public static final double RANGER_DEF_FACTOR = 1.12;
	public static final double MAGE_DEF_FACTOR = 1.04;
	public static final double ASSASSIN_DEF_FACTOR = .93;
	public static final double PALADIN_DMG_FACTOR = .92;
	public static final double RANGER_DMG_FACTOR = 1.36;
	public static final double MAGE_DMG_FACTOR = 1.01;
	public static final double ASSASSIN_DMG_FACTOR = 1.12;
	public static final double PALADIN_SPD_FACTOR = .83;
	public static final double RANGER_SPD_FACTOR = 1.03;
	public static final double MAGE_SPD_FACTOR = .91;
	public static final double ASSASSIN_SPD_FACTOR = 1.08;
	
	public static final double ENCHANTMENT_DAMAGE_ALL_FACTOR = .025;
	public static final double ENCHANTMENT_SPECIFIC_DAMAGES_FACTOR = .025;
	public static final double ENCHANTMENT_POWER_FACTOR = .025;
	public static final double ENCHANTMENT_PROTECTION_FACTOR = .04;
	public static final double ENCHANTMENT_LOOT_BONUS_FACTOR = .6;
	public static final double ENCHANTMENT_LUCK_FACTOR = .01;
	public static final double POTIONEFFECT_DAMAGES_FACTOR = .025;
	
	public static final double GLOBAL_WEAPON_DROP_REDUCTION = 3.5;
	public static final double WEAPON_RARITY_MIN = .001;
	public static final double WEAPON_RARITY_MAX = .02;
	
	public static final double TOOLS_DAMAGE_CORRECTOR = 1.5;

	public static final int DUEL_REQUEST_TIME = 30;
	public static final int DUEL_TIMEOUT = 3;
	public static final int COMPETITIVE_DUEL_LEVEL_SHIFT_MAX = 15;
	public static final int COMPETITIVE_DUEL_DELAY_MAX = 30;
	public static final int COMPETITIVE_DUEL_FORFAIT_RENOM = 10;
	public static final int COMPETITIVE_DUEL_TIE_RENOM = 15;
	public static final int COMPETITIVE_DUEL_WIN_RENOM_MIN = 10;
	public static final int COMPETITIVE_DUEL_WIN_RENOM_MAX = 50;
	public static final double COMPETITIVE_DUEL_FACTOR_POWER = .8;
	public static final double COMPETITIVE_DUEL_FACTOR_BONUS = .01;
	public static final Particle DUEL_WALL_PARTICLE = Particle.DRAGON_BREATH;
	
	public static final int SHOUT_LIMIT = 5;
	
	public static final long TIME_BEFORE_WEDDING_PROPOSAL = 24*60*60*1000L;
	
	public static final List<String> HELP_FR = Arrays.asList(
			"         §8§lCOMMANDES PRINCIPALES",
			"§4§l /tutorial §c(§l/tuto§c)§l §6- §eSe téléporter au tutoriel",
			"§4§l /skilltree §c(§l/skt§c)§l §6- §eOuvrir l'arbre des compétences",
			"§4§l /characteristics §c(§l/crc§c)§l §6- §eOuvrir le menu des caractéristiques",
			"§4§l /guild §c(§l/g§c)§l §6- §eOuvrir le menu de gestion de votre guilde",
			"§4§l /guilds §c(§l/gs§c)§l §6- §eOuvrir la liste des guildes",
			"§4§l /quests §c(§l/q§c)§l §6- §eOuvrir la liste des quêtes actives",
			"§4§l /friend §c(§l/frd§c)§l §6- §eGérer ses amis",
			"§4§l /friends §c(§l/frds§c)§l §6- §eObtenir sa liste d'amis",
			"§4§l /spawn §6- §eSe téléporter à Mearwood",
			"§4§l /suggest §c(§l/sgt§c)§l §6- §eSuggérer une idée",
			"§4§l /suggests §c(§l/sgts§c)§l §6- §eObtenir la liste des suggestions",
			"§4§l /money §c(§l/m§c)§l §6- §eGérer son argent (banque et inventaire)",
			"§4§l /howmanyplayers §c(§l/hmp§c)§l §6- §eObtenir le nombre de joueurs enregistrés",
			"§4§l /prefs §6- §eModifier les préférences de jeu",
			"§4§l /shop §6- §eOuvrir une boutique",
			"§4§l /shout §c(§l/s§c)§l §6- §eCrier un message",
			"§4§l /whisper §c(§l/w§c)§l §6- §eChuchoter un message à un joueur",
			"§4§l /answer §c(§l/r§c)§l §6- §eRépondre au dernier message chuchoté",
			"§4§l /guild §c(§l/g§c)§l §6- §eEnvoyer un message à votre guilde",
			"§4§l /play §6- §eOuvrir le menu de sélection du personnage",
			"§4§l /bienvenue §c(§l/b§c)§l §6- §eSouhaiter la bienvenue au dernier nouveau joueur",
			"§4§l /profile §c(§l/prof§c)§l §6- §eOuvrir le profil de joueur",
			"         §8§lRACCOURCIS",
			"§4§l SNEAK §c§lClic droit §7sur §c§lJOUEUR §6- §eOuvrir le menu du joueur",
			"§4§l SNEAK §c§lClic droit §7sur §c§lCOMPAGNON §6- §eOuvrir le menu du compagnon",
			"§4§l SNEAK §c§lClic droit §7sur §c§lBIBLIOTHEQUE §6- §eOuvrir le menu du personnage");
	
}
