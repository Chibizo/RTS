package data.mobile;

import data.map.*;
import data.model.*;

public abstract class MobileElement {
	
	private Zone zone;
	private int maxHealth;
	private int currentHealth;
	private Cost cost;
	private int constructTime;
	private Race race;

	public MobileElement(Zone zone,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race) {
		this.cost=new Cost(wood,magicOre);
		this.zone=zone;
		this.maxHealth=maxHealth;
		this.currentHealth=currentHealth;
		this.constructTime=constructTime;
		this.race=race;
	}

	public Zone getZone() {
		return zone;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public Cost getCost() {
		return cost;
	}

	public int getConstructTime() {
		return constructTime;
	}

	public Race getRace() {
		return race;
	}
	
	
}
