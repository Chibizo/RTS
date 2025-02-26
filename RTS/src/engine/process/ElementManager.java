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
	
	public void putUnit(Position position) {
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putUnit(new Zone(zone));
	}
	
	public void moveUnit() {
		if(!correctPosition()) {
			Position currentPosition=unit.getZone().getPositions().get(0);
			System.out.println('a');
			int currentPositionX=unit.getZone().getPositions().get(0).getColumn();
			int currentPositionY=unit.getZone().getPositions().get(0).getLine();
			
			int targetPositionX=unit.getTargetPosition().getColumn();
			int targetPositionY=unit.getTargetPosition().getLine();
			
			if(currentPositionX < targetPositionX ) {
				unit.getZone().getPositions().get(0).setColumn(currentPositionX+1);
			}
			else if(currentPositionX > targetPositionX ) {
				unit.getZone().getPositions().get(0).setColumn(currentPositionX-1);
			}
			
			if(currentPositionY < targetPositionY ) {
				unit.getZone().getPositions().get(0).setLine(currentPositionY+1);
			}
			else if(currentPositionY > targetPositionY ) {
				unit.getZone().getPositions().get(0).setLine(currentPositionY-1);
			}
			
			
			
		}
	}
	
	public boolean correctPosition() {
		return unit.getZone().getPositions().get(0).equals(unit.getTargetPosition());
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

