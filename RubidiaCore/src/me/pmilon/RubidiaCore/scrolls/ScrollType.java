package me.pmilon.RubidiaCore.scrolls;

import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause;
import me.pmilon.RubidiaCore.events.RTeleportEvent.RTeleportCause.RTeleportType;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.FriendCallUI;
import me.pmilon.RubidiaCore.ui.FriendTPUI;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaCore.utils.Settings;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public enum ScrollType {
	
	CITYTP(" - City scroll"," - Parchemin de cité", Arrays.asList("This scroll will teleport you to the city."), Arrays.asList("Ce parchemin vous téléportera en ville."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			if(Configs.getCitiesConfig().contains("cities." + this.getScroll().getArg())){
				TeleportHandler.startTeleportation(rp, (Location) Configs.getCitiesConfig().get("cities." + this.getScroll().getArg() + ".location"), new RTeleportCause(RTeleportType.CITY_TELEPORTATION,this.getScroll(),null,null));
			}
			else rp.sendMessage("§cContactez un opérateur : cette ville n'existe pas.");
		}
		
	}),/* TODO reimplement this feature
	WILDTP("Wild teleportation scroll","Parchemin de téléportation aléatoire", Arrays.asList("This scroll will teleport you to a random place."), Arrays.asList("Ce parchemin vous téléportera aléatoirement."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			final Player p = this.getPlayer();
			final RPlayer rp = RPlayer.get(p);
			if(!p.getWorld().getName().contains("_nether")){
				if(!p.getWorld().getName().contains("_end")){
					rp.sendMessage("§6Patientez pendant le chargement du terrain...");
					new BukkitTask(Core.instance){
						int x;
						int z;
						Block yb;
						List<Material> nospawn = Arrays.asList(Material.LAVA, Material.WATER);

						@Override
						public void run() {
							x = (p.getWorld().getSpawnLocation().getBlockX()+RandomUtils.random.nextInt(2300)+500)*(RandomUtils.random.nextBoolean() ? -1 : 1);
							z = (p.getWorld().getSpawnLocation().getBlockZ()+RandomUtils.random.nextInt(2300)+500)*(RandomUtils.random.nextBoolean() ? -1 : 1);
							if(p.getWorld().loadChunk((int) (x/16.0), (int) (z/16.0), true)){
								yb = p.getWorld().getHighestBlockAt(x, z);
								if(!nospawn.contains(yb.getType()) && yb.getType().isBlock()){
									this.cancel();
								}
							}
						}

						@Override
						public void onCancel() {
							Location finalLoc = new Location(p.getWorld(), x, yb.getY()+1, z);
							Chunk c = finalLoc.getChunk();
							c.load(true);
							p.getWorld().loadChunk(c);
							
							TeleportHandler.startTeleportation(rp, finalLoc, new RTeleportCause(RTeleportType.WILD_TELEPORTATION,getScroll(),null,null));
						}
						
					}.runTaskTimer(0,0);
				}else rp.sendMessage("§cVous ne pouvez pas utiliser ce parchemin dans l'End !");			
			}else rp.sendMessage("§cVous ne pouvez pas utiliser ce parchemin dans le Néant !");
		}
		
	}),*/
	ABRESET("Partial redistribution scroll (Abilities)","Parchemin de redistribution partielle (Compétences)", Arrays.asList("This scroll will instantly reset all of your","abilities, allowing you to redistribute","skillpoints you assigned."), Arrays.asList("Ce parchemin réinitialisera instantanément toutes vos","compétences, vous permettant de redistribuer les","points de compétence que vous aviez attribué."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			RPlayer rp = RPlayer.get(this.getPlayer());
			rp.resetAbilities();
		}
		
	}),
	STATRESET("Partial redistribution scroll (Distinctions)", "Parchemin de redistribution partielle (Distinctions)", Arrays.asList("This scroll will instantly reset all of your","characteristics, allowing you to redistribute","distinction points you assigned."), Arrays.asList("Ce parchemin réinitialisera instantanément toutes","vos caractéristiques, vous permettant de redistribuer","les points de distinction que vous aviez attribué."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			RPlayer rp = RPlayer.get(this.getPlayer());
			rp.resetStats();
		}
		
	}),
	RESETALL("Total redistribution scroll", "Parchemin de redistribution totale", Arrays.asList("This scroll will instantly reset all of your","statistics, allowing you to redistribute all","of your skillpoints and distinction points."), Arrays.asList("Ce parchemin réinitialisera instantanément toutes","vos statistiques, vous permettant de redistribuer","vos points de compétence et points de distinction."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			RPlayer rp = RPlayer.get(this.getPlayer());
			rp.resetAbilities();
			rp.resetStats();
		}
		
	}),
	FRDCALL("Player invocation scroll", "Parchemin d'invocation", Arrays.asList("This scroll will begin the invocation of a","player who has to accept to teleport."), Arrays.asList("Ce parchemin débutera l'invocation d'un","joueur qui devra accepter la téléportation."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			Core.uiManager.requestUI(new FriendCallUI(this.getPlayer(), this.getScroll()));
		}
		
	}),
	FRDTP("Player teleportation scroll", "Parchemin de téléportation", Arrays.asList("This scroll allows you to ask a player","the permission to teleport to him."), Arrays.asList("Ce parchemin vous permet de demander à un","joueur la permission de se téléporter à lui."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			Core.uiManager.requestUI(new FriendTPUI(this.getPlayer(), this.getScroll()));
		}
		
	}),
	SKPADD("Superiority scroll (Abilities)", "Parchemin de supériorité (Compétences)", Arrays.asList("This scroll will instantly give you a","certain amount of skillpoints."), Arrays.asList("Ce parchemin vous octroiera instantanément un","certain montant de points de compétence."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			RPlayer rp = RPlayer.get(this.getPlayer());
			int nb = Integer.valueOf(this.getScroll().getArg());
			rp.setSkillPoints(rp.getSkillPoints()+nb);
			rp.sendMessage("§aVous avez gagné §2" + nb + " §apoint" + (nb > 1 ? "s" : "") + " de compétence !");
		}
		
	}),
	SKDADD("Superiority scroll (Distinctions)", "Parchemin de supériorité (Distinctions)", Arrays.asList("This scroll will instantly give you a","certain amount of distinction points."), Arrays.asList("Ce parchemin vous octroiera instantanément un","certain montant de points de distinction."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			RPlayer rp = RPlayer.get(this.getPlayer());
			int nb = Integer.valueOf(this.getScroll().getArg());
			rp.setSkillDistinctionPoints(rp.getSkillDistinctionPoints()+nb);
			rp.sendMessage("§aVous avez gagné §2" + nb + " §apoints de distinction !");
		}
		
	}),
	RESURRECTION("Resurrection scroll", "Parchemin de résurrection", Arrays.asList("This scroll allows you to get a","second life after your death."), Arrays.asList("Ce parchemin vous octroie une","seconde vie après la mort."), ScrollUsage.RIGHT_CLICK, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			if(rp.getResurrectionTask() != null){
				rp.getResurrectionTask().cancel();
				rp.setResurrectionTask(null);
				p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*.6);
				p.setFlying(false);
				p.setFlySpeed(Settings.DEFAULT_FLY_SPEED);
				p.setWalkSpeed(Settings.DEFAULT_WALK_SPEED);
				for(Player player : Bukkit.getOnlinePlayers()){
					player.showPlayer(Core.instance, p);
				}
				p.removePotionEffect(PotionEffectType.BLINDNESS);
				rp.sendTitle(("§6Vous avez été réanimé !"), ("§eAvec uniquement 60% de votre vie"), 5, 30, 15);
				this.setUsed(true);
			}else{
				this.setUsed(false);
				rp.sendMessage("§cCe parchemin est consommable une fois mort.");
			}
		}
		
	}),
	ENHANCEMENT_PROTECTION("Enhancement scroll - Protection", "Parchemin de renforcement - Protection", Arrays.asList("This scroll will protect your weapon/armor","from breaking after a failed enhancement."), Arrays.asList("Ce parchemin protègera votre arme/armure","de se casser après l'échec d'un renforcement."), ScrollUsage.ENHANCEMENT, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			this.setUsed(false);
			rp.sendMessage("§cCe parchemin est consommable uniquement avec un FORGERON durant un renforcement.");
		}
		
	}),
	ENHANCEMENT_REDUCTION_A("Enhancement scroll - Reduction (A)", "Parchemin de renforcement - Réduction (A)", Arrays.asList("This scroll will decrease the number of","FAILURE chances during an enhancement by 1.", "One and only scroll per enhancement."), Arrays.asList("Ce parchemin diminue le nombre de cases","ECHEC durant un renforcement de 1.", "Un seul parchemin par renforcement."), ScrollUsage.ENHANCEMENT, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			this.setUsed(false);
			rp.sendMessage("§cCe parchemin est consommable uniquement avec un FORGERON durant un renforcement.");
		}
		
	}),
	ENHANCEMENT_REDUCTION_S("Enhancement scroll - Reduction (S)", "Parchemin de renforcement - Réduction (S)", Arrays.asList("This scroll will decrease the number of","FAILURE chances during an enhancement by 2.", "One and only scroll per enhancement."), Arrays.asList("Ce parchemin diminue le nombre de cases","ECHEC durant un renforcement de 2.", "Un seul parchemin par renforcement."), ScrollUsage.ENHANCEMENT, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			this.setUsed(false);
			rp.sendMessage("§cCe parchemin est consommable uniquement avec un FORGERON durant un renforcement.");
		}
		
	}),
	ENHANCEMENT_AMPLIFICATION("Enhancement scroll - Amplification", "Parchemin de renforcement - Amplification", Arrays.asList("This scroll will enhance a weapon/armor","twice (if successful), increasing FAILURE chances by 1.","One and only scroll per enhancement."), Arrays.asList("Ce parchemin renforcera votre arme/armure deux fois","(si succès), augmentant les chances d'ECHEC de 2.", "Un seul parchemin par renforcement."), ScrollUsage.ENHANCEMENT, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			this.setUsed(false);
			rp.sendMessage("§cCe parchemin est consommable uniquement avec un FORGERON durant un renforcement.");
		}
		
	}),
	PIERCING_PROTECTION_B("Piercing scroll - Protection (B)", "Parchemin de piercing - Protection (B)", Arrays.asList("This scroll will protect the slot","over which the Orichalque will stop","in case it is filled (only)."), Arrays.asList("Ce parchemin protègera le pore","au-dessus duquel l'Orichalque s'arrêtera,", "dans le cas où il est scellé (seulement)."), ScrollUsage.PIERCING, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			this.setUsed(false);
			rp.sendMessage("§cCe parchemin est consommable uniquement avec un FORGERON durant un piercing.");
		}
		
	}),
	PIERCING_PROTECTION_A("Piercing scroll - Protection (A)", "Parchemin de piercing - Protection (A)", Arrays.asList("This scroll will protect the slot","over which the Orichalque will stop","in case it is open (only)."), Arrays.asList("Ce parchemin protègera le pore","au-dessus duquel l'Orichalque s'arrêtera,", "dans le cas où il est ouvert (seulement)."), ScrollUsage.PIERCING, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			this.setUsed(false);
			rp.sendMessage("§cCe parchemin est consommable uniquement avec un FORGERON durant un piercing.");
		}
		
	}),
	PIERCING_PROTECTION_S("Piercing scroll - Protection (S)", "Parchemin de piercing - Protection (S)", Arrays.asList("This scroll will protect the slot","over which the Orichalque will stop","in case it is open or filled."), Arrays.asList("Ce parchemin protègera le pore","au-dessus duquel l'Orichalque s'arrêtera,", "dans le cas où il est ouvert ou scellé."), ScrollUsage.PIERCING, new ScrollTask(){

		@Override
		public void run() {
			Player p = this.getPlayer();
			RPlayer rp = RPlayer.get(p);
			this.setUsed(false);
			rp.sendMessage("§cCe parchemin est consommable uniquement avec un FORGERON durant un piercing.");
		}
		
	});
	
	public enum ScrollUsage {
		
		RIGHT_CLICK("Right click", "Clic droit"),
		ENHANCEMENT("Activation during enhancement", "Activation durant renforcement"),
		PIERCING("Activation during piercing", "Activation durant piercing");
		
		private String en;
		private String fr;
		private ScrollUsage(String en, String fr){
			this.en = en;
			this.fr = fr;
		}
		
		public String getFr(){
			return this.fr;
		}
		
		public String getEn(){
			return this.en;
		}
		
	}
	
	private String nameEn;
	private String nameFr;
	private List<String> loreEn;
	private List<String> loreFr;
	private ScrollUsage usage;
	private ScrollTask scrollTask;
	private ScrollType(String nameEn, String nameFr, List<String> loreEn, List<String> loreFr, ScrollUsage usage, ScrollTask scrollTask){
		this.nameEn = nameEn;
		this.nameFr = nameFr;
		this.loreEn = loreEn;
		this.loreFr = loreFr;
		this.usage = usage;
		this.scrollTask = scrollTask;
	}
	
	public String getNameEn() {
		return nameEn;
	}
	
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}
	
	public static ScrollType fromName(String name){
		for(ScrollType type : ScrollType.values()){
			if(name.contains(type.getNameEn()) || name.contains(type.getNameFr()))return type;
		}
		return null;
	}

	public List<String> getLoreEn() {
		return loreEn;
	}

	public void setLoreEn(List<String> loreEn) {
		this.loreEn = loreEn;
	}

	public List<String> getLoreFr() {
		return loreFr;
	}

	public void setLoreFr(List<String> loreFr) {
		this.loreFr = loreFr;
	}

	public ScrollTask getScrollTask() {
		return scrollTask;
	}

	public void setScrollTask(ScrollTask scrollTask) {
		this.scrollTask = scrollTask;
	}

	public ScrollUsage getUsage() {
		return usage;
	}

	public void setUsage(ScrollUsage usage) {
		this.usage = usage;
	}
}
