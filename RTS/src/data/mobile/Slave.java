package data.mobile;

import data.map.*;
import data.model.Race;

public class Slave extends Unit{
	
	private boolean harvesting=false;
	private boolean returning=false;
	private String harvestingResourceType;
	private int  resourceAmount=0 ;
	private int maxResourceAmount = 50;
    private Position basePosition;
    private Position resourcePosition;
	
	
	
	public Slave(Zone zone,String type,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race,String name) {
		super(zone,type,maxHealth,currentHealth,wood,magicOre,constructTime,race,name);
	}
	
	public String getHarvestingResourceType() {
        return harvestingResourceType;
    }
	
	
	public void setHarvestingResourceType(String type) {
        this.harvestingResourceType = type;
    }
	
	public int getResourceAmount() {
        return resourceAmount;
    }
    
    public void setResourceAmount(int amount) {
        this.resourceAmount = amount;
    }
    
    public void addResourceAmount(int amount) {
        this.resourceAmount = Math.min(this.resourceAmount + amount, maxResourceAmount);
    }
    
    public void setBasePosition(Position basePosition) {
        this.basePosition = basePosition;
    }
    
    public Position getBasePosition() {
        return basePosition;
    }
    
    public void setResourcePosition(Position resourcePosition) {
        this.resourcePosition = resourcePosition;
    }
    
    public Position getResourcePosition() {
        return resourcePosition;
    }

	public void setHarvesting(boolean harvesting) {
		this.harvesting = harvesting;
	}

	public void setReturning(boolean returning) {
		this.returning = returning;
	}

	public boolean isHarvesting() {
		return harvesting;
	}

	public boolean isReturning() {
		return returning;
	}

	public int getMaxResourceAmount() {
		return maxResourceAmount;
	}
	
}
