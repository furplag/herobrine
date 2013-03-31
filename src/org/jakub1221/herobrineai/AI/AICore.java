package org.jakub1221.herobrineai.AI;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.AI.cores.Attack;
import org.jakub1221.herobrineai.AI.cores.Book;
import org.jakub1221.herobrineai.AI.cores.BuildStuff;
import org.jakub1221.herobrineai.AI.cores.Burn;
import org.jakub1221.herobrineai.AI.cores.BuryPlayer;
import org.jakub1221.herobrineai.AI.cores.Curse;
import org.jakub1221.herobrineai.AI.cores.DestroyTorches;
import org.jakub1221.herobrineai.AI.cores.Graveyard;
import org.jakub1221.herobrineai.AI.cores.Haunt;
import org.jakub1221.herobrineai.AI.cores.Heads;
import org.jakub1221.herobrineai.AI.cores.Pyramid;
import org.jakub1221.herobrineai.AI.cores.RandomExplosion;
import org.jakub1221.herobrineai.AI.cores.RandomPosition;
import org.jakub1221.herobrineai.AI.cores.RandomSound;
import org.jakub1221.herobrineai.AI.cores.Signs;
import org.jakub1221.herobrineai.AI.cores.SoundF;
import org.jakub1221.herobrineai.AI.cores.Temple;
import org.jakub1221.herobrineai.AI.cores.Totem;
import org.jakub1221.herobrineai.entity.MobType;
import org.jakub1221.herobrineai.misc.ItemName;

public class AICore {
	
	public static ConsoleLogger log = new ConsoleLogger();
	
	private ArrayList<Core> AllCores = new ArrayList<Core>();
	private CoreType CoreNow = CoreType.ANY;
	public static HerobrineAI plugin;
	public static Player PlayerTarget;
	public static boolean isTarget=false;
	public static int ticksToEnd=0;
	public static boolean isDiscCalled=false;
	public static boolean isTotemCalled=false;
	public static int _ticks=0;
	private boolean BuildINT=false;
	private boolean MainINT=false;
	private boolean RandomPositionINT=false;
	private boolean RandomMoveINT=false;
	private boolean RandomSeeINT=false;
	private boolean CheckGravityINT=false;
	private boolean RandomCoreINT=false;
	private int RP_INT=0;
	private int RM_INT=0;
	private int RS_INT=0;
	private int CG_INT=0;
	private int MAIN_INT=0;
	private int BD_INT=0;
	private int RC_INT=0;
	
	public Core getCore(CoreType type){
		for (Core  c : AllCores){
			if (c.getCoreType()==type){
				return c;
			}
		}
		return null;
	}
	

	
	public AICore (){
		
		/* Cores init */
		AllCores.add(new Attack());
		AllCores.add(new Book());
		AllCores.add(new BuildStuff());
		AllCores.add(new BuryPlayer());
		AllCores.add(new DestroyTorches());
		AllCores.add(new Graveyard());
		AllCores.add(new Haunt());
		AllCores.add(new Pyramid());
		AllCores.add(new RandomPosition());
		AllCores.add(new Signs());
		AllCores.add(new SoundF());
		AllCores.add(new Temple());
		AllCores.add(new Totem());
		AllCores.add(new Heads());
		AllCores.add(new RandomSound());
		AllCores.add(new RandomExplosion());
		AllCores.add(new Burn());
		AllCores.add(new Curse());
		
		plugin = HerobrineAI.getPluginCore();
		log.info("[HerobrineAI] Debug mode enabled!");
		FindPlayer();
		StartIntervals();
		
		
	}

public Graveyard getGraveyard(){return ((Graveyard)getCore(CoreType.GRAVEYARD));}
public RandomPosition getRandomPosition(){return ((RandomPosition)getCore(CoreType.RANDOM_POSITION));}
public void setCoreTypeNow(CoreType c){CoreNow=c;}
public CoreType getCoreTypeNow(){return CoreNow;}

	public static String getStringWalkingMode(){
		
		String result="";
		
		if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION){result="Yes";}else{result="No";}
		
		return result;
		
	}
	
	public void playerBedEnter(Player player){
		int chance = new Random().nextInt(100);
		if (chance<25){
			GraveyardTeleport(player);
		}else if (chance<50){
			setHauntTarget(player);
		}else{
			if (HerobrineAI.getPluginCore().getConfigDB().UseNPC_Demon){
		    HerobrineAI.getPluginCore().getEntityManager().spawnCustomSkeleton(player.getLocation(), MobType.DEMON);
			}
		}
	}
	
	public void FindPlayer(){
		if (HerobrineAI.getPluginCore().getConfigDB().OnlyWalkingMode==false){
			
		if (isTarget==false){
			
		int att_chance= new Random().nextInt(100);
		log.info("[HerobrineAI] Generating find chance...");
		
		if (att_chance-(HerobrineAI.getPluginCore().getConfigDB().ShowRate*4)<55){
			
			if (Bukkit.getServer().getOnlinePlayers().length>0){
				
				log.info("[HerobrineAI] Finding target...");
				Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
				int player_rolled= new Random().nextInt(Bukkit.getServer().getOnlinePlayers().length-1);	
					
				if (AllOnPlayers[player_rolled].getEntityId()!=HerobrineAI.HerobrineEntityID){
					
					if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName()) && HerobrineAI.getPluginCore().canAttackPlayerNoMSG(AllOnPlayers[player_rolled])){
               
				CancelTarget(CoreType.ANY);
				PlayerTarget=AllOnPlayers[player_rolled];
                isTarget=true;
                log.info("[HerobrineAI] Target founded, starting AI now! ("+PlayerTarget.getName()+")");
                setCoreTypeNow(CoreType.START);
                StartAI();
					
					}else{ log.info("[HerobrineAI] Target is in the safe world! ("+AllOnPlayers[player_rolled].getLocation().getWorld().getName()+")");FindPlayer();}
				
				
				}
				
				
				}
			
			}
		
		}
		}
	}
	
	public void CancelTarget(CoreType coreType){
		
		if (coreType == CoreNow || coreType == CoreType.ANY){
			
			if (CoreNow == CoreType.RANDOM_POSITION){		
				Stop_RM();
				Stop_RS();
				Stop_CG();
				Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0),(float) 0,(float) -20,(float) 0);
				nowloc.setYaw((float) 1);
				nowloc.setPitch((float) 1);
				HerobrineAI.HerobrineNPC.moveTo(nowloc);
				CoreNow=CoreType.ANY;
				HerobrineAI.getPluginCore().getPathManager().setPath(null);
				}
			
		if (isTarget==true){
			if (CoreNow == CoreType.ATTACK){
				((Attack) getCore(CoreType.ATTACK)).StopHandler();
			}
			if (CoreNow == CoreType.HAUNT){
				((Haunt) getCore(CoreType.HAUNT)).StopHandler();
			}
			
			
		_ticks=0;
		isTarget=false;
		HerobrineAI.HerobrineHP=HerobrineAI.HerobrineMaxHP;
		log.info("[HerobrineAI] Target cancelled.");
		Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0),(float) 0,(float) -20,(float) 0);
		nowloc.setYaw((float) 1);
		nowloc.setPitch((float) 1);
		HerobrineAI.HerobrineNPC.moveTo(nowloc);
		CoreNow=CoreType.ANY;
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	FindPlayer();
			    }
	        }, (6/HerobrineAI.getPluginCore().getConfigDB().ShowRate) * (HerobrineAI.getPluginCore().getConfigDB().ShowInterval * 1L));
		
		}
	}
	}
	
	public void StartAI(){
		if (PlayerTarget.isOnline() && isTarget){
			 if (PlayerTarget.isDead()==false){
				 Object[] data = {PlayerTarget};
					int chance= new Random().nextInt(100);
					 if (chance<=10){
						 if(HerobrineAI.getPluginCore().getConfigDB().UseGraveyardWorld==true){
						 log.info("[HerobrineAI] Teleporting target to Graveyard world.");
						 
						 getCore(CoreType.GRAVEYARD).RunCore(data);
					
						 }
					 }else if (chance<=25){
					
	             getCore(CoreType.ATTACK).RunCore(data);
				 }else{
					 getCore(CoreType.HAUNT).RunCore(data);
				 }
			 }else{CancelTarget(CoreType.START);}	
		}else{CancelTarget(CoreType.START);}
	}
	
	public CoreResult setAttackTarget(Player player){
		Object[] data = {player};
		return getCore(CoreType.ATTACK).RunCore(data);
	}
	
	public CoreResult setHauntTarget(Player player){
		Object[] data = {player};
		return getCore(CoreType.HAUNT).RunCore(data);
	}
	
	
	
	

	
	public void GraveyardTeleport(final Player player){
		
		 if (player.isOnline()){
			 CancelTarget(CoreType.ANY);
			 
			  	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
		   	        public void run() {
		   	        	Object[] data = {player};
		   	        	getCore(CoreType.GRAVEYARD).RunCore(data);
		   			    }
		   	        }, 1 * 10L);
			 
		 
		 }
		
		
	}
	
	
	
	
   public void PlayerCallTotem(Player player){
	   final String playername=player.getName();
	   final Location loc = (Location) player.getLocation();
	   isTotemCalled=true;
	   CancelTarget(CoreType.ANY);
	   Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	CancelTarget(CoreType.ANY);
	         	Object[] data = {loc, playername};
   	        	getCore(CoreType.TOTEM).RunCore(data);
			    }
	        }, 1 * 40L);
   }
   
private void RandomPositionInterval(){
if (CoreNow==CoreType.ANY){
	 ((RandomPosition)getCore(CoreType.RANDOM_POSITION)).setRandomTicks(0);
     int count = HerobrineAI.getPluginCore().getConfigDB().useWorlds.size();
		int chance=new Random().nextInt(count);
		Object[] data = {Bukkit.getServer().getWorld(HerobrineAI.getPluginCore().getConfigDB().useWorlds.get(chance))};
		getCore(CoreType.RANDOM_POSITION).RunCore(data);
	
}
}
   
  private void CheckGravityInterval(){
	  if (this.CoreNow==CoreType.RANDOM_POSITION){
		  ((RandomPosition)getCore(CoreType.RANDOM_POSITION)).CheckGravity();
	  }

  }
   private void RandomMoveInterval(){
	   ((RandomPosition)getCore(CoreType.RANDOM_POSITION)).RandomMove();

   }
	
   private void RandomSeeInterval(){
		  if (CoreNow == CoreType.RANDOM_POSITION){
			  ((RandomPosition)getCore(CoreType.RANDOM_POSITION)).CheckPlayerPosition();
		  }
	
	   }
   private void PyramidInterval(){

				if (new Random().nextBoolean()){
			  if (Bukkit.getServer().getOnlinePlayers().length>0){
					log.info("[HerobrineAI] Finding pyramid target...");
					Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();

				    int player_rolled= new Random().nextInt(Bukkit.getServer().getOnlinePlayers().length-1);
					if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName())){
						
						int chance2=new Random().nextInt(100);
						if (chance2<30){
							  if (HerobrineAI.getPluginCore().getConfigDB().BuildPyramids==true){
								  Object[] data = {AllOnPlayers[player_rolled]};
							getCore(CoreType.PYRAMID).RunCore(data);
							  }
						}else if(chance2<70){
							if (HerobrineAI.getPluginCore().getConfigDB().BuryPlayers){
								  Object[] data = {AllOnPlayers[player_rolled]};
									getCore(CoreType.BURY_PLAYER).RunCore(data);
						}
					}else{
						if (HerobrineAI.getPluginCore().getConfigDB().UseHeads){
							  Object[] data = {AllOnPlayers[player_rolled].getName()};
								getCore(CoreType.HEADS).RunCore(data);
					}
					}
				}
			  }
		  
			  }
		   
	   }
   

private void TempleInterval(){
	  if (HerobrineAI.getPluginCore().getConfigDB().BuildTemples==true){
			if (new Random().nextBoolean()){
		  if (Bukkit.getServer().getOnlinePlayers().length>0){
				log.info("[HerobrineAI] Finding temple target...");
				Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			int player_rolled= new Random().nextInt(Bukkit.getServer().getOnlinePlayers().length-1);
				if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName())){
					if (new Random().nextBoolean()){
						  Object[] data = {AllOnPlayers[player_rolled]};
							getCore(CoreType.TEMPLE).RunCore(data);
					
					}
				}
			}
	  }
	  }
	  

	   
}

private void BuildCave(){
	  if (HerobrineAI.getPluginCore().getConfigDB().BuildStuff==true){
		if (new Random().nextBoolean()){
		  if (Bukkit.getServer().getOnlinePlayers().length>0){
				Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			int player_rolled= new Random().nextInt(Bukkit.getServer().getOnlinePlayers().length-1);
				if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName())){
					
					if (new Random().nextBoolean()){
						  Object[] data = {AllOnPlayers[player_rolled].getLocation()};
							getCore(CoreType.BUILD_STUFF).RunCore(data);
					
					}
				}
			}  
	  }
}   
}
   
   
  public void callByDisc(Player player){
	  isDiscCalled=false;
	  if (player.isOnline()){
		  CancelTarget(CoreType.ANY);
		  setHauntTarget(player);
	  }
  }

  public void RandomCoreINT(){
	 
			if (new Random().nextBoolean()){
		  if (Bukkit.getServer().getOnlinePlayers().length>0){
				Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			int player_rolled= new Random().nextInt(Bukkit.getServer().getOnlinePlayers().length-1);
			
			if (AllOnPlayers[player_rolled].getEntityId()!=HerobrineAI.HerobrineEntityID){
				if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName())){
					Object[] data = {AllOnPlayers[player_rolled]};
					
					if (new Random().nextInt(100)<30){
						  
							getCore(CoreType.RANDOM_SOUND).RunCore(data);		
					}else if (new Random().nextInt(100)<60){
						  if (HerobrineAI.getPluginCore().getConfigDB().Burn){
						    getCore(CoreType.BURN).RunCore(data);		
						    }
				    }else if (new Random().nextInt(100)<80){
						  if (HerobrineAI.getPluginCore().getConfigDB().Curse){
					        getCore(CoreType.CURSE).RunCore(data);		
						  }
			        }else{
						 
							getCore(CoreType.RANDOM_EXPLOSION).RunCore(data);		
					}
				}
			}
	  }
    }
  }
  
   
  public void DisappearEffect(){
	   
   	Location ploc = (Location) PlayerTarget.getLocation();
    
	Location hbloc1 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
	hbloc1.setY(hbloc1.getY()+1);
	Location hbloc2 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
	hbloc2.setY(hbloc2.getY()+0);
	Location hbloc3 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
	hbloc3.setY(hbloc3.getY()+0.5);
	Location hbloc4 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
	hbloc4.setY(hbloc4.getY()+1.5);

	
	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
	
	ploc.setY(-20);
	HerobrineAI.HerobrineNPC.moveTo(ploc);

   }
  
  private void BuildInterval(){
	  if(new Random().nextInt(100)<75){
		  PyramidInterval();
	  }else{
		  TempleInterval();
	  }
	  
	  if (new Random().nextBoolean()){
		  BuildCave();
	  }
  }
   
   private void StartIntervals(){
	   Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
	        public void run() {
	    		Start_RP();
	    		Start_MAIN();
	    	    Start_BD();
	    	    Start_RC();
			    }
	        }, 1 * 5L);

   }
   public void Start_RP(){
	   RandomPositionINT=true;
	   RP_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	RandomPositionInterval();
			    }
	        },1 * 300L, 1 * 300L);
   }
   public void Start_BD(){
	   BuildINT=true;
	   BD_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	BuildInterval();
			    }
	        }, 1 * 1L * HerobrineAI.getPluginCore().getConfigDB().BuildInterval, 1 * 1L * HerobrineAI.getPluginCore().getConfigDB().BuildInterval);
   }
   public void Start_MAIN(){
	   MainINT=true;
	   MAIN_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
	        public void run() {
                   FindPlayer();

			    }
	        },(6/HerobrineAI.getPluginCore().getConfigDB().ShowRate) * (HerobrineAI.getPluginCore().getConfigDB().ShowInterval * 1L),(6/HerobrineAI.getPluginCore().getConfigDB().ShowRate) * (HerobrineAI.getPluginCore().getConfigDB().ShowInterval * 1L));
   }
   public void Start_RM(){
	   RandomMoveINT=true;
		 
			  RM_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			        public void run() {
			        	RandomMoveInterval();
					    }
			        },1 * 50L ,1 * 50L);
		   
   }
   public void Start_RS(){
	   RandomSeeINT=true;
	   RS_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	RandomSeeInterval();
			    }
	        }, 1 * 15L,1 * 15L); 
   }
   
   public void Start_RC(){
	   RandomCoreINT=true;
	   RC_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	RandomCoreINT();
			    }
	        }, (long)(HerobrineAI.getPluginCore().getConfigDB().ShowInterval/1.5),(long)(HerobrineAI.getPluginCore().getConfigDB().ShowInterval/1.5)); 
   }
   
   public void Start_CG(){
	   CheckGravityINT=true;
	   CG_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	CheckGravityInterval();
			    }
	        }, 1 * 10L,1 * 10L);
   }
   
   public void Stop_RP(){
	   if (RandomPositionINT){
	   RandomPositionINT=false;
	   Bukkit.getServer().getScheduler().cancelTask(RP_INT);
   }
   }
   public void Stop_BD(){
	   if (BuildINT){
	   BuildINT=false;
	   Bukkit.getServer().getScheduler().cancelTask(BD_INT);
   }
   }
   public void Stop_RS(){
	   if (RandomSeeINT){
	   RandomSeeINT=false;
	   Bukkit.getServer().getScheduler().cancelTask(RS_INT);
   }
   }
   public void Stop_RM(){
	   if (RandomMoveINT){
	   RandomMoveINT=false;
	   Bukkit.getServer().getScheduler().cancelTask(RM_INT);
   }
   }
   public void Stop_RC(){
	   if (RandomCoreINT){
	   RandomCoreINT=false;
	   Bukkit.getServer().getScheduler().cancelTask(RC_INT);
   }
   }
   public void Stop_CG(){
	   if (CheckGravityINT){
	   CheckGravityINT=false;
	   Bukkit.getServer().getScheduler().cancelTask(CG_INT);
   }
   }
   public void Stop_MAIN(){
	   if (MainINT){
	   MainINT=false;
	   Bukkit.getServer().getScheduler().cancelTask(MAIN_INT);
   }
   }
   
   public ItemStack createAncientSword(){
	   ItemStack item = new ItemStack(Material.GOLD_SWORD);
	   String name = "Ancient Sword";
	   ArrayList<String> lore = new ArrayList<String>();
	   lore.add("AncientSword");
	   lore.add("Very old and mysterious sword.");
	   lore.add("This will protect you aganist Herobrine.");
	   item = ItemName.setNameAndLore(item, name, lore);
	   return item;
   }
   
   public boolean isAncientSword(ItemStack item){
	   ArrayList<String> lore = new ArrayList<String>();
	   lore.add("AncientSword");
	   lore.add("Very old and mysterious sword.");
	   lore.add("This will protect you aganist Herobrine.");
	   if (item!=null){
	   if (item.getItemMeta()!=null){
		   if (item.getItemMeta().getLore()!=null){
	   ArrayList<String> ilore = (ArrayList<String>) item.getItemMeta().getLore();
	   if (ilore.containsAll(lore)){
		   return true;
		   
	   }
	   }
	   }
	   }
	   
	   return false;
   }
   
   public boolean checkAncientSword(Inventory inv){
	   ItemStack[] itemlist = inv.getContents();
	   ItemStack item = null;
	   int i=0;
	   for (i=0;i<=itemlist.length-1;i++){
		   item=itemlist[i];
		   if (isAncientSword(item)){
			   return true;
		   }
	   }
	   
	   return false;
   }
   
}
