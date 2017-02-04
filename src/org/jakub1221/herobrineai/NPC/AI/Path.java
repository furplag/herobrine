package org.jakub1221.herobrineai.NPC.AI;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;

public class Path {
	
	private float x;
	private float z;
	private boolean xNegative;
	private boolean zNegative;
	private boolean canContinue=true;
	private boolean isCompleted=false;
	private int stepNow=0;
	private int maxSteps=new Random().nextInt(3)+3;
	
	public Path(float _x,float _z){
		x=_x;
	    z=_z;
	    if ((x-HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getX())<0){xNegative=true;}else{xNegative=false;}
	    if ((z-HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getZ())<0){zNegative=true;}else{zNegative=false;}
	}
	
	public void update(){
		if (stepNow<=maxSteps){
		if (!isCompleted){
			
			
			
	    if ((x-HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getX())<0){xNegative=true;}else{xNegative=false;}
	    if ((z-HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getZ())<0){zNegative=true;}else{zNegative=false;}
		Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		World world = loc.getWorld();
		if (loc.getBlockX()>(int)x-1 && loc.getBlockX()<(int)x+1 && loc.getBlockZ()>(int)z-1 && loc.getBlockZ()<(int)z+1){
			isCompleted=true;
			return;
		}
		float nX=(float) loc.getX();
		float nY=(float) loc.getY();
		float nZ=(float) loc.getZ();
		
		float pre_finalX=0.3f;
		float pre_finalZ=0.3f;
		if (xNegative){pre_finalX=-0.3f;}
		if (zNegative){pre_finalZ=-0.3f;}
		
		boolean canGoX=true;
		boolean canGoZ=true;
		
		if (world.getHighestBlockYAt((int)(nX+pre_finalX),(int)nZ)>nY+1){
			canGoX=false;
		}
		if (world.getHighestBlockYAt((int)nX,(int)(nZ+pre_finalZ))>nY+1){
			canGoZ=false;
		}
		if (canGoX && canGoZ){
			if (world.getHighestBlockYAt((int)(nX+pre_finalX),(int)(nZ+pre_finalZ))>nY+1){
				canGoX=false;
				canGoZ=false;
			}else if (world.getHighestBlockYAt((int)(nX+pre_finalX),(int)(nZ+pre_finalZ))<nY-2){
				canGoX=false;
				canGoZ=false;
			}
		}
		
		Location newloc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		
		if (canGoX){
			newloc.setX(newloc.getX()+pre_finalX);
		}
		if (canGoZ){
			newloc.setZ(newloc.getZ()+pre_finalZ);
		}
		
		if (canGoX && canGoZ){
			if (xNegative){
				
			}
		}
		
		newloc.setY(world.getHighestBlockYAt(newloc)-1);
		
		if (HerobrineAI.StandBlocks.contains(world.getBlockAt(newloc).getType())){
			
		
		
		newloc.setY(newloc.getWorld().getHighestBlockYAt(newloc)+1.5f);
		HerobrineAI.HerobrineNPC.lookAtPoint(newloc);
		newloc.setY(newloc.getWorld().getHighestBlockYAt(newloc));
		HerobrineAI.HerobrineNPC.moveTo(newloc);
		}
		stepNow++;
		
		
	}
	}else{
		if (new Random().nextInt(7)==3){

			int yaw=new Random().nextInt(360);
			
			HerobrineAI.HerobrineNPC.setYaw(yaw);
			HerobrineAI.HerobrineNPC.setYawA(yaw);
			HerobrineAI.HerobrineNPC.setPitch(0);
		
		}
	}
	}
	
	public boolean canContinue(){
		return this.canContinue;
	}
	

}
