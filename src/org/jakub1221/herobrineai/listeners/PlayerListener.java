package org.jakub1221.herobrineai.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.misc.ItemName;

public class PlayerListener implements Listener{
	
	private ItemStack itemInHand=null;
	private ArrayList<String> equalsLoreS = new ArrayList<String>();
	private ArrayList<String> equalsLoreA = new ArrayList<String>();
	private ArrayList<String> getLore = new ArrayList<String>();
	private ArrayList<LivingEntity> LivingEntities = new ArrayList<LivingEntity>();
	private Location le_loc = null;
	private Location p_loc = null;
	private long timestamp = 0;
	private boolean canUse = false;
	
	public PlayerListener(){
		equalsLoreS.add("Herobrine´s artifact");
		equalsLoreS.add("Sword of Lighting");
		equalsLoreA.add("Herobrine´s artifact");
		equalsLoreA.add("Apple of Death");
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
	if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
		if (event.getClickedBlock()!=null && event.getPlayer().getItemInHand()!=null){
		ItemStack itemInHand = event.getPlayer().getItemInHand();
		if (event.getPlayer().getItemInHand().getType()!=null){
		if (itemInHand.getType() == Material.DIAMOND_SWORD || itemInHand.getType() == Material.GOLDEN_APPLE){
		if (ItemName.getLore(itemInHand)!=null){
			if (ItemName.getLore(itemInHand).containsAll(equalsLoreS) && HerobrineAI.getPluginCore().getConfigDB().UseArtifactSword){
			if (new Random().nextBoolean()){
			event.getPlayer().getLocation().getWorld().strikeLightning(event.getClickedBlock().getLocation());
			}
			}else if (ItemName.getLore(itemInHand).containsAll(equalsLoreA) && HerobrineAI.getPluginCore().getConfigDB().UseArtifactApple){
			 timestamp = System.currentTimeMillis() / 1000;
			 canUse=false;
			 if (HerobrineAI.getPluginCore().PlayerApple.containsKey(event.getPlayer())){
				 if (HerobrineAI.getPluginCore().PlayerApple.get(event.getPlayer()) < timestamp){
					 HerobrineAI.getPluginCore().PlayerApple.remove(event.getPlayer());
					 canUse=true;
				 }else{canUse=false;}
			 }else{canUse=true;}
			 
			 if (canUse==true){
				 event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 0F);
					LivingEntities =  (ArrayList<LivingEntity>) event.getPlayer().getLocation().getWorld().getLivingEntities();
					HerobrineAI.getPluginCore().PlayerApple.put(event.getPlayer(), timestamp+60);
					for (int i=0;i<=LivingEntities.size()-1;i++){
						
						if (LivingEntities.get(i) instanceof Player || LivingEntities.get(i).getEntityId() == HerobrineAI.HerobrineEntityID){}else{
                                le_loc=LivingEntities.get(i).getLocation();  
                                p_loc=event.getPlayer().getLocation();
							if (le_loc.getBlockX() < p_loc.getBlockX()+20 && le_loc.getBlockX() > p_loc.getBlockX()-20){
								if (le_loc.getBlockY() < p_loc.getBlockY()+10 && le_loc.getBlockY() > p_loc.getBlockY()-10){
									if (le_loc.getBlockZ() < p_loc.getBlockZ()+20 && le_loc.getBlockZ() > p_loc.getBlockZ()-20){
										 event.getPlayer().getWorld().createExplosion(LivingEntities.get(i).getLocation(), 0F);
										LivingEntities.get(i).damage(10000);
									}
								}	
							}
							
						}
						
					}
					
			 }else{
				 event.getPlayer().sendMessage(ChatColor.RED+"Apple of Death is recharging!");
			 }
		}
	}
		}
		}
		}
	}
	
	
	if (event.getClickedBlock()!=null){
		if (event.getPlayer().getItemInHand()!=null){
			if (event.getClickedBlock().getType()==Material.JUKEBOX){
				ItemStack item = event.getPlayer().getItemInHand();
                Jukebox block = (Jukebox) event.getClickedBlock().getState();
                if (!block.isPlaying()){
                	if (item.getType()==Material.getMaterial(2266)){
                		HerobrineAI.getPluginCore().getAICore();
						if (!AICore.isDiscCalled){
                			final Player player = event.getPlayer();
                			HerobrineAI.getPluginCore().getAICore();
							AICore.isDiscCalled=true;
                			HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ANY);
                			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
                			        public void run() {
                			        	HerobrineAI.getPluginCore().getAICore().callByDisc(player);
                					    }
                			        }, 1 * 50L);
                		}
                	}
                }
			}
		}
	}
	
	}
	
	@EventHandler
	public void onPlayerEnterBed(PlayerBedEnterEvent event){
		if (new Random().nextInt(100)>75){
		Player player = event.getPlayer();
		((CraftPlayer) player).getHandle().a(true, false, false);
		HerobrineAI.getPluginCore().getAICore().playerBedEnter(player);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event){
		if(event.getPlayer().getEntityId()!=HerobrineAI.HerobrineEntityID){
			if (HerobrineAI.getPluginCore().getAICore().PlayerTarget==event.getPlayer() && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.GRAVEYARD && event.getPlayer().getLocation().getWorld()==Bukkit.getServer().getWorld("world_herobrineai_graveyard") && HerobrineAI.getPluginCore().getAICore().isTarget){	
				if (new Random().nextBoolean()){
				event.getPlayer().teleport( HerobrineAI.getPluginCore().getAICore().getGraveyard().getSavedLocation());
			
				}
				}
	}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event){
		if (event.getPlayer().getEntityId()==HerobrineAI.HerobrineEntityID){
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if(event.getPlayer().getEntityId()==HerobrineAI.HerobrineEntityID){
			if (event.getFrom().getWorld()!=event.getTo().getWorld()){
			HerobrineAI.getPluginCore().HerobrineRemove();
			HerobrineAI.getPluginCore().HerobrineSpawn(event.getTo());
			event.setCancelled(true);
			return;
			}
			
			if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION){
				if (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockX() > HerobrineAI.getPluginCore().getConfigDB().WalkingModeXRadius){
					if (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockX() < (-HerobrineAI.getPluginCore().getConfigDB().WalkingModeXRadius)){
						if (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockZ() > HerobrineAI.getPluginCore().getConfigDB().WalkingModeZRadius){
							if (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockZ() < (-HerobrineAI.getPluginCore().getConfigDB().WalkingModeZRadius)){
								HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.RANDOM_POSITION);
								HerobrineAI.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorlds().get(0),0,-20,0));
							}
					}	
				}
				}
				}
			}
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event){
		if (event.getEntity().getEntityId() == HerobrineAI.HerobrineEntityID){
		event.setDeathMessage("");
	
		HerobrineAI.getPluginCore().HerobrineRemove();

		Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0),(float) 0,(float) -20,(float) 0);
		   nowloc.setYaw((float) 1);
		    nowloc.setPitch((float) 1);
		HerobrineAI.getPluginCore().HerobrineSpawn(nowloc);
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		if (event.getPlayer().getEntityId()!=HerobrineAI.HerobrineEntityID){
		if (event.getPlayer().getWorld()==Bukkit.getServer().getWorld("world_herobrineai_graveyard")){
			Player player = (Player) event.getPlayer();
			player.teleport(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"),-2.49,4,10.69,(float)-179.85,(float) 0.44999));
		}
	}
	
		
	
	}
	

}
