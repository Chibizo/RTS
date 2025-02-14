package engine.process;

import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.Building;
import data.model.*;


public class ElementManager implements MobileInterface {
	private Map map;
	
	private Building building;
	
	public ElementManager(Map map) {
		this.map=map;
	}
	
	public void putBuilding(Zone zone) {
		for(Position position : zone.getPositions()) {
			if (map.isOnBorder(position)) {
				return;
			}
		}
		Race race=new Race("temporaier");
		building=new Building(zone,0,0,0,0,0,0,race);		
	}
	
	public Building getBuilding() {
		return building;
	}
}

