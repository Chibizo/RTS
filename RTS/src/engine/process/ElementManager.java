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
	
	private Building building;
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
		Unit unit=new Unit(zone,"temp",0,0,0,0,0,race);
		units.add(unit);
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
		Slave slave=new Slave(zone,"temp",0,0,0,0,0,race);
		units.add(slave);
		UnitStepper stepper = new UnitStepper(slave,150);
		unitSteppers.put(slave, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		
	}
	public void putSlave(Position position) {
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putSlave(new Zone(zone));
	}
	
	
	
	public void moveUnitOneStep(Unit unit) {
	    Position currentPosition = unit.getZone().getPositions().get(0);
	    int currentPositionX = currentPosition.getColumn();
	    int currentPositionY = currentPosition.getLine();
	    
	    int targetPositionX = unit.getTargetPosition().getColumn();
	    int targetPositionY = unit.getTargetPosition().getLine();
	    
	    if (currentPositionX < targetPositionX) {
	        unit.getZone().getPositions().get(0).setColumn(currentPositionX + 1);
	    } else if (currentPositionX > targetPositionX) {
	        unit.getZone().getPositions().get(0).setColumn(currentPositionX - 1);
	    }
	    
	    if (currentPositionY < targetPositionY) {
	        unit.getZone().getPositions().get(0).setLine(currentPositionY + 1);
	    } else if (currentPositionY > targetPositionY) {
	        unit.getZone().getPositions().get(0).setLine(currentPositionY - 1);
	    }
	}
	
	public boolean correctPosition(Unit unit) {
		return unit.getZone().getPositions().get(0).equals(unit.getTargetPosition());
	}
	
	public Building getBuilding() {
		return building;
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
	            if (!correctPosition(unit)) {
	                moveUnitOneStep(unit);
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

