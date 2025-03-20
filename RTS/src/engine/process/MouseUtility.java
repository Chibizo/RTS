package engine.process;

import org.apache.log4j.Logger;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import config.GameConfiguration;
import data.map.Position;
import data.map.Zone;
import data.mobile.Building;
import data.mobile.Unit;
import data.model.Player;
import log.LoggerUtility;

public class MouseUtility {
	
	private static Logger logger = LoggerUtility.getLogger(MouseUtility.class, "html");

	
	public static Rectangle createSelectionRectangle(Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(end.x - start.x);
		int height = Math.abs(end.y - start.y);
		return new Rectangle(x, y, width, height);
	}
	
	public static boolean selectUnitsInRectangle(List<Unit> units,Rectangle selectionRect,Player player) {
	    logger.debug("Sélection d'unités dans le rectangle: " + selectionRect);
		for (Unit unit : units) {
			unit.setSelected(false);
			unit.setTargeted(false); // Reset targeting as well
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
	        logger.debug("Unités sélectionnées pour " + player.getRace().getName());
			return true;
		}
		else {
	        logger.debug("Aucune unité sélectionnée pour " + player.getRace().getName());
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
	
	public static Building findEnemyBuildingAt(List<Building> buildings,int line,int column,Unit unit) {
		for (Building potentialEnemyBuilding : buildings) {	        
	        Zone enemyBuildingZone = potentialEnemyBuilding.getZone();
	        for(Position pos : enemyBuildingZone.getPositions()) {
		        if (pos.getLine() == line && 
		            pos.getColumn() == column && 
		            !potentialEnemyBuilding.getRace().getName().equals(unit.getRace().getName())) {
		            return potentialEnemyBuilding;
		        }
        
	        }
		}
	    return null;
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
	
	public static Unit findEnemyAt(List<Unit> unitList, int line, int column, Unit currentUnit) {
	    for (Unit potentialEnemy : unitList) {
	        if (potentialEnemy != currentUnit) { 
	        
		        Position enemyPos = potentialEnemy.getZone().getPositions().get(0);
		        if (enemyPos.getLine() == line && 
		            enemyPos.getColumn() == column && 
		            !potentialEnemy.getRace().getName().equals(currentUnit.getRace().getName())) {
		            logger.debug("Ennemi trouvé à la position [" + line + "," + column + "]: " + potentialEnemy.getName());
		            return potentialEnemy;
		        }
	        }
	    }
	    return null;
	}
	
	public static Unit findEnemyUnitAtPosition(List<Unit> units, int x, int y, Player currentPlayer) {
	    for (Unit unit : units) {
	        Position unitPos = unit.getZone().getPositions().get(0);
	        if (unitPos.getColumn() == x && unitPos.getLine() == y && 
	            unit.getRace().getName() != currentPlayer.getRace().getName()) {
	            return unit;
	        }
	    }
	    return null;
	}
	
	public static Building findEnemyBuildingAtPosition(List<Building> buildings, int x, int y, Player currentPlayer) {
	    for (Building building : buildings) {
	        for (Position pos : building.getZone().getPositions()) {
	            if (pos.getColumn() == x && pos.getLine() == y && 
	                building.getRace().getName() != currentPlayer.getRace().getName()) {
	                return building;
	            }
	        }
	    }
	    return null;
	}
	
	
}
