package engine.process;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.*;
import data.model.*;


public class ElementManager implements MobileInterface {
	private Map map;
	
	private HashMap<String,Building> buildings=new HashMap<String,Building>();
	private ArrayList<Unit> units = new ArrayList<Unit>(); 
	private String raceMainPlayer;
	private Player mainPlayer;
	private HashMap<Unit, UnitStepper> unitSteppers = new HashMap<>();
	
	
	public ElementManager(Map map,Player mainPlayer) {
		this.map=map;
		this.mainPlayer=mainPlayer;	
				
	}
	
	public String getResourceTypeAt(Position position) {
        for (Position pos : map.getWoodLocations().getPositions()) {
		    if (pos.equals(position)) {
		        return "wood";
		    }
        }
        for (Position pos : map.getMagicOreLocations().getPositions()) {
            if (pos.equals(position)) {
                return "magicOre";
            }
        }
        
        return null;
    }
	
	 public void startHarvesting(Slave slave, Position resourcePosition) {
		 
		 	if(slave.isUnderConstruction()) {
		 		return ;
		 	}
		 	
	        String resourceType = getResourceTypeAt(resourcePosition);
	        if (resourceType == null) {
	            return; 
	        }
	        
	       
	        slave.setHarvesting(true);
	        slave.setHarvestingResourceType(resourceType);
	        slave.setResourcePosition(resourcePosition);
	        slave.setBasePosition(mainPlayer.getStarterZone().getPositions().get(0));
	        slave.setTargetPosition(resourcePosition);
	 }
	 
	 public void harvestResource(Slave slave) {
        if (slave.isHarvesting() == true && 
            slave.getZone().getPositions().get(0).equals(slave.getResourcePosition())) {
            
        	if (slave.getHarvestingResourceType().equals("wood")) {
                slave.addResourceAmount(GameConfiguration.WOOD_HARVEST_AMOUNT);
            } else if (slave.getHarvestingResourceType().equals("magicOre")) {
                slave.addResourceAmount(GameConfiguration.MAGIC_ORE_HARVEST_AMOUNT);
            }
            
            if (slave.getResourceAmount() >= 50) {
                slave.setReturning(true);
                slave.setHarvesting(false);
                slave.setTargetPosition(slave.getBasePosition());
            }
        }
        if (slave.isReturning() && slave.getZone().getPositions().get(0).equals(slave.getBasePosition())) {
            
            if (slave.getHarvestingResourceType().equals("wood")) {
                mainPlayer.setWood(mainPlayer.getWood() + slave.getResourceAmount());
            } else if (slave.getHarvestingResourceType().equals("magicOre")) {
                mainPlayer.setMagicOre(mainPlayer.getMagicOre() + slave.getResourceAmount());
            }
            
            slave.setResourceAmount(0);
            slave.setHarvesting(true);
            slave.setReturning(false);
            slave.setTargetPosition(slave.getResourcePosition());
        }
    }
 
     
	
	public void putBuilding(Zone zone,String type,Player player) {
		for(Position position : zone.getPositions()) {
			if (map.isOnBorder(position)) {
				return;
			}
		}
		Race race=new Race("temporaier");
		if(type=="barracks") {
			Building building=new Building(zone,1,250,250,0,0,30000,race,"barracks");	
			buildings.put("barracks",building);
			player.addBuilding(building);
			map.addFullPosition(zone);
			mainPlayer.setWood(mainPlayer.getWood()-800);
		}
		else if (type=="base") {
			Building building=new Building(zone,1,1000,1000,0,0,0,race,"base");	
			buildings.put("base",building);
			player.addBuilding(building);
			map.addFullPosition(zone);
		}
		System.out.println(player.getBuildings());

	}
	
	
	public void putWarrior(Zone zone) {
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Race race=new Race("temp");
		Unit unit=new Unit(zone,"temp",200,200,0,0,20000,race,"warrior");
		units.add(unit);
		map.addFullUnitsPosition(unit.getZone());
		mainPlayer.setWood(mainPlayer.getWood()-200);
		UnitStepper stepper = new UnitStepper(unit,map,this);
		unitSteppers.put(unit, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		
	}
	public void putWarrior(Position position) {
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putWarrior(new Zone(zone));
	}
	
	public void putSlave(Zone zone) {
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Race race=new Race("temp");
		Slave slave=new Slave(zone,"temp",100,100,0,0,15000,race,"slave");
		units.add(slave);
		map.addFullUnitsPosition(zone);
		mainPlayer.setWood(mainPlayer.getWood()-100);
		UnitStepper stepper = new UnitStepper(slave,100,map,this);
		unitSteppers.put(slave, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		
	}
	public void putSlave(Position position) {
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putSlave(new Zone(zone));
	}
	
	
	
	public synchronized void moveUnitOneStep(Unit unit) {
		
		if(unit.isUnderConstruction()) {
			return;
		}
		
	    Position currentPosition = unit.getZone().getPositions().get(0);
	    Position targetPosition = unit.getTargetPosition();
	    Position newPosition = new Position(currentPosition.getLine(), currentPosition.getColumn());
	    
	    if (currentPosition.getColumn() < targetPosition.getColumn()) {
	        newPosition.setColumn(currentPosition.getColumn() + 1);
	    } else if (currentPosition.getColumn() > targetPosition.getColumn()) {
	    	newPosition.setColumn(currentPosition.getColumn() - 1);
	    }
	    
	   if (currentPosition.getLine() < targetPosition.getLine()) {
		   newPosition.setLine(currentPosition.getLine() + 1);
	    } else if (currentPosition.getLine() > targetPosition.getLine()) {
	    	newPosition.setLine(currentPosition.getLine() - 1);
	    }
	   if (!map.isfullUnits(newPosition) || (unit instanceof Slave && ((Slave)unit).isReturning()) || (unit instanceof Slave && ((Slave)unit).isHarvesting())) {
	        currentPosition.setColumn(newPosition.getColumn());
	        currentPosition.setLine(newPosition.getLine());
	   }
	 
	}
	
	public boolean correctPosition(Unit unit) {
		if(unit.isUnderConstruction()) {
			return true;
		}
		return unit.getZone().getPositions().get(0).equals(unit.getTargetPosition());
	}
	
	public HashMap<String,Building> getBuildings() {
		return buildings;
	}
	
	public Unit getUnit() {
		if (units.isEmpty()) {
	        return null;
	    }
	    return units.get(units.size() - 1);
	}
	
	public List<Unit> getAllUnits() {
	    return units;
	}
	
	public void selectMostRecentUnit() {
	    for (Unit unit : units) {
	        unit.setSelected(false);
	    }
	    if (!units.isEmpty()) {
	        units.get(units.size() - 1).setSelected(true);
	    }
	}
	
	public Player getMainPlayer() {
		return mainPlayer;
	}
	
	public List<Unit> getSelectedUnits() {
	    List<Unit> selectedUnits = new ArrayList<>();
	    for (Unit unit : units) {
	        if (unit.isSelected()) {
	            selectedUnits.add(unit);
	        }
	    }
	    return selectedUnits;
	}
	
	
	public void updateConstruction() {
	    for (Building building : buildings.values()) {
	        if (building.isUnderConstruction()) {
	            if (building.getConstructionProgress() >= 1.0f) {
	                building.setUnderConstruction(false);
	            }
	        }
	    }
	    
	    for (Unit unit : units) {
	        if (unit.isUnderConstruction()) {
	            if (unit.getConstructionProgress() >= 1.0f) {
	                unit.setUnderConstruction(false);
	            }
	        }
	    }
	}
	
	
	
	
	
	
}