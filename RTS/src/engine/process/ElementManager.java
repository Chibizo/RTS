package engine.process;


import java.util.ArrayList;
import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.*;
import data.model.*;


public class ElementManager implements MobileInterface {
	private Map map;
	
	private Building building;
	private Unit unit;
	private String raceMainPlayer;
	private Player mainPlayer;
	
	
	public ElementManager(Map map,Player mainPlayer) {
		this.map=map;
		this.mainPlayer=mainPlayer;	
				
	}
	
	public void putBuilding(Zone zone) {
		for(Position position : zone.getPositions()) {
			if (map.isOnBorder(position)) {
				return;
			}
		}
		Race race=new Race("temporaier");
		building=new Building(zone,0,0,0,0,0,0,race);	
		map.addFullPosition(zone);
		mainPlayer.setWood(mainPlayer.getWood()-50);
	}
	
	
	public void putUnit(Zone zone) {
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Race race=new Race("temp");
		unit=new Unit(zone,"temp",0,0,0,0,0,race);
	}
	
	public void moveUnit() {
		Position position=unit.getZone().getPositions().get(0);
		

		if(!map.isOnTop(position) && !map.isfull(position)) {
			Position newPosition = map.getBlock(position.getLine() -1, position.getColumn()+1);
			unit.setPosition(newPosition);
		}
	}
	
	public Building getBuilding() {
		return building;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
	public Player getMainPlayer() {
		return mainPlayer;
	}
	
	
}

