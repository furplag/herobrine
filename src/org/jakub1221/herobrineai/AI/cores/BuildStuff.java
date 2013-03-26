package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.*;


public class BuildStuff extends Core{

	public BuildStuff(){
		super(CoreType.BUILD_STUFF,AppearType.NORMAL);
	}
	public CoreResult CallCore(Object[] data){
		if (data.length==2){
			return BuildCave((Location) data[0],true);
		}
		return BuildCave((Location) data[0]);
	}
	
	public CoreResult BuildCave(Location loc){
		
		if (HerobrineAI.getPluginCore().getConfigDB().BuildStuff==true){
			if (HerobrineAI.getPluginCore().getSupport().checkBuild(loc)){
		if (loc.getBlockY()<60){
		
		Random randgen = new Random();
		int chance=randgen.nextInt(100);
		if (chance>(100-HerobrineAI.getPluginCore().getConfigDB().CaveChance)){
			AICore.log.info("Creating cave...");
			
			GenerateCave(loc);
			
			return new CoreResult(false,"Cave created!");       
			
		}else{
			return new CoreResult(false,"Roll failed!");
		}
		}else{
			return new CoreResult(false,"Location must be under 60 of Y.");
		}
		}else{
			return new CoreResult(false,"Cannot build stuff.");
		}
		}else{
			return new CoreResult(false,"Player is in secure location.");
		}
		
		
	}
	
	public CoreResult BuildCave(Location loc,boolean cmd){
		

			if (HerobrineAI.getPluginCore().getSupport().checkBuild(loc)){
		if (loc.getBlockY()<60){
		
	
			AICore.log.info("Creating cave...");
			
			GenerateCave(loc);
			
			return new CoreResult(false,"Cave created!");       
			
		
		}else{
			return new CoreResult(false,"Location must be under 60 of Y.");
		}
			}else{
				return new CoreResult(false,"Player is in secure location.");
			}

	}
	
	public void GenerateCave(Location loc){
		
		if (HerobrineAI.getPluginCore().getSupport().checkBuild(loc)){
		
		ArrayList<Location> redstoneTorchList = new ArrayList<Location>();
		boolean goByX=new Random().nextBoolean();
		boolean goNegative=new Random().nextBoolean();
		
		int baseX=loc.getBlockX();
		int baseZ=loc.getBlockZ();
		int baseY=loc.getBlockY();
		
		int finalX=0;
		int finalZ=0;
		
		int maxL=new Random().nextInt(10)+4;
		int iR=new Random().nextInt(3)+4;
		int iNow=0;
		while(iNow!=iR){
		iNow++;
		goByX=new Random().nextBoolean();
		goNegative=new Random().nextBoolean();
		int i=0;
		for (i=0;i<=maxL;i++){
			finalX=0;
			finalZ=0;
			if (goNegative){
				if (goByX){
					finalX=-1;
				}else{
					finalZ=-1;
				}
			}else{
				if (goByX){
					finalX=1;
				}else{
					finalZ=1;
				}
			}
			
			baseX=baseX+finalX;
			baseZ=baseZ+finalZ;
			
			loc.getWorld().getBlockAt(baseX,baseY,baseZ).breakNaturally(null);
			loc.getWorld().getBlockAt(baseX,baseY+1,baseZ).breakNaturally(null);
			if (new Random().nextBoolean()){
				redstoneTorchList.add(new Location(loc.getWorld(),baseX,baseY+1,baseZ));
			}
		}
		}
		
		for (Location _loc : redstoneTorchList){
			PlaceRedstoneTorch(_loc.getWorld(),_loc.getBlockX(),_loc.getBlockY(),_loc.getBlockZ());
		}
		
		AICore.log.info("Cave created!");
	
	}
	}
	
	public void PlaceRedstoneTorch(World world,int x,int y,int z){
		Random randgen = new Random();
		int chance=randgen.nextInt(100);
		if (chance>70){
		world.getBlockAt(x, y, z).setType(Material.REDSTONE_TORCH_ON);
		}
	}
	
	
}
