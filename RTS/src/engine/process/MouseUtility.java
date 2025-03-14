package engine.process;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import config.GameConfiguration;
import data.map.Position;
import data.map.Zone;
import data.mobile.Building;
import data.mobile.Unit;
import data.model.Player;

public class MouseUtility {
	

	
	
	public static Rectangle createSelectionRectangle(Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(end.x - start.x);
		int height = Math.abs(end.y - start.y);
		return new Rectangle(x, y, width, height);
	}
	
	public static boolean selectUnitsInRectangle(List<Unit> units,Rectangle selectionRect,Player player) {
		for (Unit unit : units) {
			unit.setSelected(false);
		}
		boolean unitsSelected = false;
		for (Unit unit : units) {
			if(!unit.isUnderConstruction() && unit.getRace().getName()==player.getRace().getName()) {
				Position unitPos = unit.getZone().getPositions().get(0);
				int unitX = unitPos.getColumn() * GameConfiguration.BLOCK_SIZE;
				int unitY = unitPos.getLine() * GameConfiguration.BLOCK_SIZE;
				
				if (selectionRect.contains(unitX, unitY)) {
					unit.setSelected(true);
					unitsSelected = true;
				}
			}
		}
		
		if (unitsSelected) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String checkBuilding(Position clickedPosition,Player player) {
		List<Building> builds=player.getBuildings();
		if(isInBaseZone(clickedPosition,player.getStarterZone())) {
			return "base";
		}
		for(Building build : builds) {
			for(Position pos : build.getZone().getPositions()) {
				if(pos.equals(clickedPosition)) {
					return build.getName();
				}
			}
		}
		return "";
	}
	
	public static boolean isInBaseZone(Position clickedPosition,Zone baseZone) {
		
	    for (Position position : baseZone.getPositions()) {
	        if (position.equals(clickedPosition)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	public static Unit findUnitAtPosition(List<Unit> units,int x, int y,Player player) {
	    for (Unit unit : units) {
	        Position unitPos = unit.getZone().getPositions().get(0);
	        if (unitPos.getColumn() == x && unitPos.getLine() == y && unit.getRace().getName()==player.getRace().getName()) {
	            return unit;
	        }
	    }
	    return null;
	}
	public static Unit findEnemyAtPosition(List<Unit> units,int x, int y,Player player) {
	    for (Unit unit : units) {
	        Position unitPos = unit.getZone().getPositions().get(0);
	        if (unitPos.getColumn() == x && unitPos.getLine() == y && (unit.getRace().getName()!=player.getRace().getName())) {
	            return unit;
	        }
	    }
	    return null;
	}

	
}
