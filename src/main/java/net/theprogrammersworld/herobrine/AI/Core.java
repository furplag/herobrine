package net.theprogrammersworld.herobrine.AI;

import net.theprogrammersworld.herobrine.Herobrine;

public abstract class Core {

	private final AppearType Appear;
	private final Type coreType;
	private CoreResult nowData = null;
	protected Herobrine PluginCore = null;

	public Core(Type cp,AppearType ap, Herobrine hb){
		this.coreType=cp;
		this.Appear=ap;
		this.PluginCore = hb;
	}

	public AppearType getAppear(){
		return Appear;
		}

	public Type getCoreType(){
		return coreType;
		}

	protected abstract CoreResult CallCore(Object[] data);

	public CoreResult RunCore(Object[] data){

		nowData=this.CallCore(data);
		if (nowData.getResult() && Appear == AppearType.APPEAR){
			Herobrine.getPluginCore().getAICore().setCurrent(this.coreType);

		}
		return nowData;
		}

	public enum Type{
		ATTACK,
		HAUNT,
		BOOK,
		BUILD_CAVE,
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
		BURN,
		CURSE,
		STARE;

	}

	public enum AppearType{
		APPEAR,
		NORMAL,
	}
}
