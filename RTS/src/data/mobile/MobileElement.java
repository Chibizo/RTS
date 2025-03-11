package data.mobile;

import java.util.ArrayList;
import data.map.*;
import data.model.*;

public abstract class MobileElement {
	
	private String name;
	private Zone zone;
	private int maxHealth;
	private int currentHealth;
	private Cost cost;
	private Race race;
	
	private int constructionTime;
	private boolean underConstruction = true;
	private long constructionStartTime;

	public MobileElement(Zone zone,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race,String name) {
		this.cost=new Cost(wood,magicOre);
		this.zone=zone;
		this.maxHealth=maxHealth;
		this.currentHealth=currentHealth;
		this.race=race;
		this.name=name;

		this.constructionTime=constructTime;
	    this.constructionStartTime = System.currentTimeMillis();
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
		return constructionTime;
	}

	public Race getRace() {
		return race;
	}
	public void setPosition(Position position) {
		ArrayList<Position> pos=new ArrayList<Position>();
		pos.add(position);
		Zone newZone=new Zone(pos);
		zone=newZone;
	}
	
	public boolean isUnderConstruction() {
	    return underConstruction;
	}

	public void setUnderConstruction(boolean underConstruction) {
	    this.underConstruction = underConstruction;
	}

	public long getConstructionStartTime() {
	    return constructionStartTime;
	}

	public long getConstructionTime() {
	    return constructionTime;
	}

	public String getName() {
		return name;
	}

	public float getConstructionProgress() {
	    long timePassed = System.currentTimeMillis() - constructionStartTime;
	    float progress = (float) timePassed/constructionTime;
	    return Math.min(progress, 1.0f);
	}

	public int getRemainingConstructionTime() {
	    long timePassed = System.currentTimeMillis() - constructionStartTime;
	    long timeRemaining = constructionTime - timePassed;
	    return (int) Math.max(0, Math.ceil(timeRemaining / 1000.0)); 
	}
	
	
}
