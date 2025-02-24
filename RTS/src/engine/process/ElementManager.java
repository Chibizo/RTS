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
	
	
	public ElementManager(Map map,String raceMainPlayer) {
		this.map=map;
		this.raceMainPlayer=raceMainPlayer;
		ArrayList<Position> starterPositionBase=new ArrayList<Position>();
		starterPositionBase.add(new Position(50,12));
		mainPlayer=new Player(500,500,new Race("raceMainPlayer"),new Zone(starterPositionBase));
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

