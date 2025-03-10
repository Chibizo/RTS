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
 
     
	
	public void putBuilding(Zone zone,String type) {
		for(Position position : zone.getPositions()) {
			if (map.isOnBorder(position)) {
				return;
			}
		}
		Race race=new Race("temporaier");
		if(type=="barracks") {
			Building building=new Building(zone,1,250,250,0,0,45000,race);	
			buildings.put("barracks",building);
			map.addFullPosition(zone);
			mainPlayer.setWood(mainPlayer.getWood()-700);
		}
		else if (type=="base") {
			Building building=new Building(zone,1,1000,1000,0,0,0,race);	
			buildings.put("base",building);
			map.addFullPosition(zone);
		}

	}
	
	
	public void putUnit(Zone zone) {
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Race race=new Race("temp");
		Unit unit=new Unit(zone,"temp",0,0,0,0,15000,race);
		units.add(unit);
		map.addFullUnitsPosition(unit.getZone());
		mainPlayer.setWood(mainPlayer.getWood()-100);
		UnitStepper stepper = new UnitStepper(unit);
		unitSteppers.put(unit, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		
	}
	public void putUnit(Position position) {
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putUnit(new Zone(zone));
	}
	
	public void putSlave(Zone zone) {
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Race race=new Race("temp");
		Slave slave=new Slave(zone,"temp",100,100,0,0,15000,race);
		units.add(slave);
		map.addFullUnitsPosition(zone);
		mainPlayer.setWood(mainPlayer.getWood()-100);
		UnitStepper stepper = new UnitStepper(slave,100);
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
	   if (!map.isfullUnits(newPosition)) {
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
	
	
	private class UnitStepper implements Runnable {
	    private Unit unit;
	    private volatile boolean running = true;
	    private int speed ;
	    private long lastHarvestTime = 0;

	    public UnitStepper(Unit unit,int speed) {
	        this.unit = unit;
	        this.speed=speed;
	        
	    }
	    public UnitStepper(Unit unit) {
	    	this(unit,100);
	    }

	    public void stop() {
	        running = false;
	    }

	    @Override
	    public void run() {
	        while (running) {
	            if (unit.isUnderConstruction()) {
	                try {
	                    Thread.sleep(speed);
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt();
	                    return;
	                }
	                continue;
	            }
	            if (!correctPosition(unit)) {
	                Position oldPosition = new Position(
	                    unit.getZone().getPositions().get(0).getLine(),
	                    unit.getZone().getPositions().get(0).getColumn()
	                );
	                
	                moveUnitOneStep(unit);
	                
	                if (!oldPosition.equals(unit.getZone().getPositions().get(0))) {
	                    map.removeFullUnitsPosition(oldPosition);
	                    map.addFullUnitsPosition(unit.getZone());
	                }
	            }
	            
	            if(unit instanceof Slave) {
	            	if(((Slave) unit).isHarvesting() || ((Slave) unit).isReturning()) {
	            		long currentTime = System.currentTimeMillis();
	            		if(currentTime - lastHarvestTime > GameConfiguration.HARVEST_TIME) {
	            			harvestResource((Slave)unit);
	            			lastHarvestTime=currentTime;
	            		}
	            	}
	            	
	            }
	            
	            try {
	                Thread.sleep(speed); 
                } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                return;
	            }
	        }
	    }
	}
	
	
	
}