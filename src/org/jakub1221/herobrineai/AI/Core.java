package org.jakub1221.herobrineai.AI;

import org.jakub1221.herobrineai.HerobrineAI;

public abstract class Core {
	
	private final AppearType Appear;
	private final CoreType coreType;
	private CoreResult nowData=null;
	
	public Core(CoreType cp,AppearType ap){
		this.coreType=cp;
		this.Appear=ap;
	}
	
	public AppearType getAppear(){
		return Appear;
		}
	
	public CoreType getCoreType(){
		return coreType;
		}

	public abstract CoreResult CallCore(Object[] data);
	
	public CoreResult RunCore(Object[] data){

		nowData=this.CallCore(data);
		if (nowData.getResult() && Appear == AppearType.APPEAR){
			HerobrineAI.getPluginCore().getAICore().setCoreTypeNow(this.coreType);
			
		}
		return nowData;
		}
	
	public enum CoreType{
		ATTACK,
		HAUNT,
		BOOK,
		BUILD_STUFF,
		BURY_PLAYER,
		DESTROY_TORCHES,
		GRAVEYARD,
		PYRAMID,
		RANDOM_POSITION,
		SIGNS,
		SOUNDF,
		TOTEM,
		ANY,
		START,
		TEMPLE,
		HEADS,
		RANDOM_SOUND,
		RANDOM_EXPLOSION,
		BURN;
		
	}
	public enum AppearType{
		APPEAR,
		NORMAL,

	}
}
