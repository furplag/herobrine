package net.theprogrammersworld.herobrine.AI;

public class CoreResult {

	private final boolean bo;
	private final String text;

	public CoreResult(boolean b, String t) {
		this.bo = b;
		this.text = t;
	}

	public boolean getResult() {
		return this.bo;
	}

	public String getResultString() {
		return this.text;
	}
}
