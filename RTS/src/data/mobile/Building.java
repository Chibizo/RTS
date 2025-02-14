package data.mobile;

import data.map.Position;
import data.map.Zone;
import data.model.Race;

public class Building extends MobileElement{
	

	private Zone zone;
	private int tier;
	
	public Building(Zone zone,int tier,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race) {
		super(zone,maxHealth,currentHealth,wood,magicOre,constructTime,race);
		this.tier=tier;
	}

	public Zone getZone() {
		return super.getZone();
	}
	
	
}
