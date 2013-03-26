package org.jakub1221.herobrineai.AI.extensions;

import java.util.Random;

import org.bukkit.Location;
import org.jakub1221.herobrineai.HerobrineAI;


public class Position{

	public static  Location getTeleportPosition(Location ploc){
		
		Location newloc = (Location) ploc;

		int chance= new Random().nextInt(3);
		
		if (chance==0){
		
		if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
		newloc.setX(newloc.getX()-2);
		}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
			newloc.setX(newloc.getX()+2);
			}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
				newloc.setZ(newloc.getZ()-2);
			}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
				newloc.setZ(newloc.getZ()+2);
			}else{
				
				if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
					newloc.setX(newloc.getX()-2);
					}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
						newloc.setX(newloc.getX()+2);
						}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
							newloc.setZ(newloc.getZ()-2);
						}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
							newloc.setZ(newloc.getZ()+2);
						}else{
							
						}
				
			}
		}else if(chance==1){
			
			if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
				newloc.setX(newloc.getX()+2);
				}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
					newloc.setX(newloc.getX()-2);
					}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
						newloc.setZ(newloc.getZ()-2);
					}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
						newloc.setZ(newloc.getZ()+2);
					}else{
						
						if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
							newloc.setX(newloc.getX()+2);
							}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
								newloc.setX(newloc.getX()-2);
								}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
									newloc.setZ(newloc.getZ()-2);
								}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
									newloc.setZ(newloc.getZ()+2);
								}else{
									
								}
						
					}
			
		}else if(chance==2){
			
			 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
				newloc.setZ(newloc.getZ()-2);
			}
			 	else if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
				newloc.setX(newloc.getX()+2);
				}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
					newloc.setX(newloc.getX()-2);
					}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
						newloc.setZ(newloc.getZ()+2);
					}else{
						
						if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
							newloc.setZ(newloc.getZ()-2);
						}
						
						else if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
							newloc.setX(newloc.getX()+2);
							}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
								newloc.setX(newloc.getX()-2);
								}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
									newloc.setZ(newloc.getZ()+2);
								}else{
									
								}
						
					}
			
		}		if (chance==3){
			
		if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
				newloc.setZ(newloc.getZ()+2);
			}
			else if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
			newloc.setX(newloc.getX()-2);
			}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
				newloc.setX(newloc.getX()+2);
				}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
					newloc.setZ(newloc.getZ()-2);
				}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
					newloc.setZ(newloc.getZ()+2);
				}else{
					
					if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()+2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()+2).getType())){
						newloc.setZ(newloc.getZ()+2);
					}
					else if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()-2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
						newloc.setX(newloc.getX()-2);
						}else	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY(), ploc.getBlockZ()).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX()+2, ploc.getBlockY()+1, ploc.getBlockZ()).getType())){
							newloc.setX(newloc.getX()+2);
							}else 	if (HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ()-2).getType()) && HerobrineAI.AllowedBlocks.contains(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY()+1, ploc.getBlockZ()-2).getType())){
								newloc.setZ(newloc.getZ()-2);
							}else{
								
							}
					
				}
			}
		
		return newloc;
		
	}
	
}
