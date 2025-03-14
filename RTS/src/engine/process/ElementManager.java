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
	
	private List<Building> buildings=new ArrayList<Building>();
	private HashMap<String,Building> buildingsMainPlayer=new HashMap<String,Building>();
	private HashMap<String,Building> buildingsAIPlayer=new HashMap<String,Building>();
	private ArrayList<Unit> units = new ArrayList<Unit>(); 
	private String raceMainPlayer;
	private Player mainPlayer;
	private Player enemyPlayer;
	private HashMap<Unit, UnitStepper> unitSteppers = new HashMap<>();
	
	
	public ElementManager(Map map,Player mainPlayer,Player enemyPlayer) {
		this.map=map;
		this.mainPlayer=mainPlayer;	
		this.enemyPlayer=enemyPlayer;
				
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
	
	 public void startHarvesting(Slave slave, Position resourcePosition,Player player) {
		 
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
	        slave.setBasePosition(player.getStarterZone().getPositions().get(0));
	        slave.setTargetPosition(resourcePosition);
	 }
	 
	 public void harvestResource(Slave slave,Player player) {
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
                player.setWood(player.getWood() + slave.getResourceAmount());
            } else if (slave.getHarvestingResourceType().equals("magicOre")) {
                player.setMagicOre(player.getMagicOre() + slave.getResourceAmount());
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
		if(player.getClass().getSimpleName().equals("AIPlayer")) {

			if(type=="barracks") {
				Building building=new Building(zone,1,250,250,GameConfiguration.BARRACKS_COST,0,30000,player.getRace(),"barracks");	
				buildingsAIPlayer.put("barracks",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
				player.setWood(player.getWood()-building.getCost().getWood());
			}
			else if (type=="base") {
				Building building=new Building(zone,1,1000,1000,0,0,0,player.getRace(),"base");	
				buildingsAIPlayer.put("base",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
			}
			System.out.println(getBuildingsAIPlayer());
			
		}else {
			if(type=="barracks") {
				Building building=new Building(zone,1,250,250,GameConfiguration.BARRACKS_COST,0,30000,player.getRace(),"barracks");	
				buildingsMainPlayer.put("barracks",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
				player.setWood(player.getWood()-building.getCost().getWood());
			}
			else if (type=="base") {
				Building building=new Building(zone,1,1000,1000,0,0,0,player.getRace(),"base");	
				buildingsMainPlayer.put("base",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
			}
			System.out.println(getBuildingsMainPlayer());

		}

	}
	
	
	public synchronized void putWarrior(Zone zone,Player player) {
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Unit unit=new Unit(zone,"temp",200,200,GameConfiguration.WARRIOR_COST,0,20000,player.getRace(),"warrior",10);
		units.add(unit);
		map.addFullUnitsPosition(unit.getZone());
		player.setWood(player.getWood()-unit.getCost().getWood());
		UnitStepper stepper = new UnitStepper(unit,map,this,player);
		unitSteppers.put(unit, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		
	}
	public synchronized void putWarrior(Position position,Player player) {
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putWarrior(new Zone(zone),player);
	}
	
	public synchronized void putSlave(Zone zone,Player player) {
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Slave slave=new Slave(zone,"temp",100,100,GameConfiguration.SLAVE_COST,0,15000,player.getRace(),"slave",10);
		units.add(slave);
		map.addFullUnitsPosition(zone);
		player.setWood(player.getWood()-slave.getCost().getWood());
		UnitStepper stepper = new UnitStepper(slave,100,map,this,player);
		unitSteppers.put(slave, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		System.out.println(map.getFullUnitsPosition());
		
	}
	public synchronized void putSlave(Position position,Player player) {
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putSlave(new Zone(zone),player);
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
	   
	   
	   if (!map.isfullUnits(newPosition) && !(map.isfull(newPosition)) || (unit instanceof Slave && ((Slave)unit).isReturning()) || (unit instanceof Slave && ((Slave)unit).isHarvesting())) {
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
	
	public List<Building> getBuildings() {
		return buildings;
	}
	
	public HashMap<String,Building> getBuildingsMainPlayer() {
		return buildingsMainPlayer;
	}
	
	public HashMap<String,Building> getBuildingsAIPlayer() {
		return buildingsAIPlayer;
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
	    for (Building building : buildings) {
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
	
	
	
	
	
	public boolean areUnitsClose(Unit unit1, Unit unit2) {
	    Position pos1 = unit1.getZone().getPositions().get(0);
	    Position pos2 = unit2.getZone().getPositions().get(0);
	    
	    // Distance de Manhattan
	    int distance = Math.abs(pos1.getLine() - pos2.getLine()) + 
	                   Math.abs(pos1.getColumn() - pos2.getColumn());
	    
	    // Vérifier si dans la portée d'attaque
	    return distance <= unit1.getAttackRange();
	}
	
	
	public void checkCombat() {
	    List<Unit> unitsToRemove = new ArrayList<>();
	    
	    List<Unit> unitsCopy;
	    synchronized(this) {
	        unitsCopy = new ArrayList<>(units);
	    }
	    
	    for (Unit unit : unitsCopy) {
	        if (!unit.isUnderConstruction()) {
		        
		        Position currentPosition = unit.getZone().getPositions().get(0);
		        int line = currentPosition.getLine();
		        int column = currentPosition.getColumn();
	
		        for (int i = -1; i <= 1; i++) {
		            for (int j = -1; j <= 1; j++) {
		                if (i != 0 && j != 0) {
			                
			                int checkLine = line + i;
			                int checkColumn = column + j;
			                
			                if (checkLine >= 0 && checkLine < map.getLineCount() && 
			                    checkColumn >= 0 && checkColumn < map.getColumnCount()) {
			                    
			                    Unit enemy = MouseUtility.findEnemyAt(unitsCopy, checkLine, checkColumn, unit);
			                    
			                    if (enemy != null) {
			                        boolean killed = attack(unit, enemy);
			                        if (killed && !unitsToRemove.contains(enemy)) {
			                            unitsToRemove.add(enemy);
			                        }
			                    }
		                }
		                }
		            }
	        }
	        }
	    }
	    
	    synchronized(this) {
	        for (Unit deadUnit : unitsToRemove) {
	            removeUnit(deadUnit);
	        }
	    }
	}

	
	public boolean attack(Unit attacker, Unit defender) {
	    long currentTime = System.currentTimeMillis();
	    if (currentTime - attacker.getLastAttackTime() < 1000) { 
	        return false;
	    }
	    
	    int damageAmount = attacker.getAttackDamage();
	    
	    
	    
	    int oldHealth = defender.getCurrentHealth();
	    defender.setCurrentHealth(oldHealth - damageAmount);
	    int newHealth = defender.getCurrentHealth();
	    
	   
	    
	    attacker.setLastAttackTime(System.currentTimeMillis());
	    
	    return defender.getCurrentHealth() <= 0;
	}
	
	public void removeUnit(Unit unit) {
	    map.removeFullUnitsPosition(unit.getZone().getPositions().getFirst());
	    units.remove(unit);
	    unitSteppers.get(unit).stop();
	    unitSteppers.remove(unit);
	    System.out.println(map.getFullUnitsPosition());
	}
	
	
	
	
	
	
	
	
}