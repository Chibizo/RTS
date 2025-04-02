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
	
	private boolean targeted;
	


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
	
	public boolean isTargeted() {
        return targeted;
    }
    
    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
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
	
	

	public void setName(String name) {
		this.name = name;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setCost(Cost cost) {
		this.cost = cost;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public void setConstructionTime(int constructionTime) {
		this.constructionTime = constructionTime;
	}

	public void setConstructionStartTime(long constructionStartTime) {
		this.constructionStartTime = constructionStartTime;
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

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}
	
	
	
	
}
