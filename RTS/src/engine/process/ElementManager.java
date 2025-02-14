package engine.process;

import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.Building;


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
		building=new Building(zone);		
	}
	
	public Building getBuilding() {
		return building;
	}
}

