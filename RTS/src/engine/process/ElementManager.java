package engine.process;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.*;
import data.model.*;
import log.LoggerUtility;


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
	private MovementManager movementManager;
	
	private static Logger logger = LoggerUtility.getLogger(Map.class, "html");


	
	
	public ElementManager(Map map,Player mainPlayer,Player enemyPlayer) {
		this.map=map;
		this.mainPlayer=mainPlayer;	
		this.enemyPlayer=enemyPlayer;
	    this.movementManager = new MovementManager(map);
	    logger.info("Gestionnaire d'éléments initialisé avec joueur principal: " + mainPlayer.getRace().getName());
			
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
	            logger.warn("Tentative de construction sur une bordure de carte rejetée");
				return;
			}
		}
		if(player.getClass().getSimpleName().equals("AIPlayer")) {

			if(type=="barracks") {
	            logger.info("Caserne construite par l'IA à la position: " + zone.getPositions().get(0));
				Building building=new Building(zone,1,250,250,GameConfiguration.BARRACKS_COST,0,30000,player.getRace(),"barracks");	
				buildingsAIPlayer.put("barracks",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
				player.setWood(player.getWood()-building.getCost().getWood());
			}else if (type=="runway") {
				Building building=new Building(zone,1,750,750,0,0,50000,player.getRace(),"runway");	
				buildingsAIPlayer.put("runway",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
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
	            logger.info("Caserne construite par le joueur principal à la position: " + zone.getPositions().get(0));
				Building building=new Building(zone,1,250,250,GameConfiguration.BARRACKS_COST,0,30000,player.getRace(),"barracks");	
				buildingsMainPlayer.put("barracks",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
				player.setWood(player.getWood()-building.getCost().getWood());
			}else if (type=="runway") {
				Building building=new Building(zone,1,750,750,0,0,50000,player.getRace(),"runway");	
				buildingsMainPlayer.put("runway",building);
				buildings.add(building);
				player.addBuilding(building);
				map.addFullPosition(zone);
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
		if(player.getBuildings("barracks")==null) {
			return;
		}
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
	    logger.debug("Tentative de création d'un guerrier à la position: " + position);
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putWarrior(new Zone(zone),player);
	}
	
	
	public synchronized void putWizard(Zone zone,Player player) {
		if(player.getBuildings("runway")==null) {
			return;
		}
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Unit unit=new Unit(zone,"temp",200,200,GameConfiguration.WIZARD_COST,0,10000,player.getRace(),"wizard",10);
		units.add(unit);
		map.addFullUnitsPosition(unit.getZone());
		player.setWood(player.getWood()-unit.getCost().getWood());
		UnitStepper stepper = new UnitStepper(unit,map,this,player);
		unitSteppers.put(unit, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		
	}
	public synchronized void putWizard(Position position,Player player) {
	    logger.debug("Tentative de création d'un magicien à la position: " + position);
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putWizard(new Zone(zone),player);
	}
	
	public synchronized void putSlave(Zone zone,Player player) {
		if(player.getBuildings("base")==null) {
			return;
		}
		for(Position position : zone.getPositions()) {
			if(map.isOnBorder(position) || map.isfull(position)) {
				return; 
			}
		}
		Slave slave=new Slave(zone,"temp",100,100,GameConfiguration.SLAVE_COST,0,15000,player.getRace(),"slave",10);
		units.add(slave);
		map.addFullUnitsPosition(zone);
		player.setWood(player.getWood()-slave.getCost().getWood());
		player.setSlave(player.getSlave()+1);
		System.out.println(player.getSlave());
		UnitStepper stepper = new UnitStepper(slave,100,map,this,player);
		unitSteppers.put(slave, stepper);
		Thread thread = new Thread(stepper);
		thread.start();
		System.out.println(map.getFullUnitsPosition());
		
	}
	public synchronized void putSlave(Position position,Player player) {
	    logger.debug("Tentative de création d'un esclave à la position: " + position);
		ArrayList<Position> zone=new ArrayList<Position>();
		zone.add(position);
		putSlave(new Zone(zone),player);
	}
	
	
	
	public synchronized void moveUnitOneStep(Unit unit) {
	    if (unit.isUnderConstruction()) {
	        return;
	    }
	    
	    Position currentPosition = unit.getZone().getPositions().get(0);
	    Position targetPosition = unit.getTargetPosition();
	    
	    if (unit.getTargetUnit() != null && !unit.getTargetUnit().isUnderConstruction()) {
	        if (units.contains(unit.getTargetUnit())) {
	            targetPosition = unit.getTargetUnit().getZone().getPositions().get(0);
	            unit.setTargetPosition(targetPosition);
	        } else {
	            unit.setTargetUnit(null);
	        }
	    } else if (unit.getTargetBuilding() != null && !unit.getTargetBuilding().isUnderConstruction()) {
	        if (buildings.contains(unit.getTargetBuilding())) {
	            targetPosition = unit.getTargetBuilding().getZone().getPositions().get(0);
	            unit.setTargetPosition(targetPosition);
	        } else {
	            unit.setTargetBuilding(null);
	        }
	    }
	    
	    if (currentPosition.equals(targetPosition)) {
	        return;
	    }
	    
	    Position nextPosition = movementManager.calculateNextStep(unit, currentPosition, targetPosition);
	    
	    if (!nextPosition.equals(currentPosition)) {
	        boolean canMove = !map.isfullUnits(nextPosition) && !map.isfull(nextPosition) || 
	                          (unit instanceof Slave && (((Slave)unit).isReturning() || ((Slave)unit).isHarvesting()));
	        
	        if (canMove) {
	            logger.debug(unit.getName() + " se déplace de " + currentPosition + " vers " + nextPosition);
	            currentPosition.setColumn(nextPosition.getColumn());
	            currentPosition.setLine(nextPosition.getLine());
	        }else {
	            logger.debug(unit.getName() + " ne peut pas se déplacer vers " + nextPosition + " (case occupée)");
	        }
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
	
	
	public int calculateDistance(Position p1, Position p2) {
        return Math.abs(p1.getLine() - p2.getLine()) + Math.abs(p1.getColumn() - p2.getColumn());
    }
	
	public void checkCloseEnemy() {
	    int detectionRadius = 5; 
	    
	    
	    List<Unit> unitsCopy;
	    synchronized(this) {
	        unitsCopy = new ArrayList<>(units);
	    }
	    
	    for (Unit unit : unitsCopy) {
	        if (!unit.isUnderConstruction() && unit.getTargetUnit() == null && !unit.isManuallyCommanded()) {
	        	if(unit.getName().equals("warrior")) {
	        		detectionRadius=12;
	        	}else {
	        		detectionRadius=5;
	        	}
		        
		        Position unitPosition = unit.getZone().getPositions().get(0);
		        
		        Unit closestEnemy = null;
		        int closestDistance = Integer.MAX_VALUE;
		        
		        for (Unit potentialEnemy : unitsCopy) {
		            if (!potentialEnemy.getRace().getName().equals(unit.getRace().getName()) && 
			                !potentialEnemy.isUnderConstruction()) {
			              
			            
			            Position enemyPosition = potentialEnemy.getZone().getPositions().get(0);
			            int distance = calculateDistance(unitPosition, enemyPosition);
			            
			            if (distance <= detectionRadius && distance < closestDistance) {
			                closestEnemy = potentialEnemy;
			                closestDistance = distance;
			            }
		            }
		        }
		        
		        if (closestEnemy != null) {
		            unit.setTargetUnit(closestEnemy);
		            unit.setTargetBuilding(null);
		            unit.setTargetPosition(closestEnemy.getZone().getPositions().get(0));
		            
		            if (unit instanceof Slave) {
		                ((Slave) unit).setHarvesting(false);
		                ((Slave) unit).setReturning(false);
		            }
		        }
	        }
	    }
	}
	
	
	public void checkCombat() {
	    List<Unit> unitsToRemove = new ArrayList<>();
	    List<Building> buildingsToRemove = new ArrayList<>();
	    
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
	                    if (i != 0 || j != 0) {
	                        int checkLine = line + i;
	                        int checkColumn = column + j;
	                        
	                        if (checkLine >= 0 && checkLine < map.getLineCount() && 
	                            checkColumn >= 0 && checkColumn < map.getColumnCount()) {
	                            
	                            Unit enemy = MouseUtility.findEnemyAt(unitsCopy, checkLine, checkColumn, unit);
	                            
	                            if (enemy != null) {
	                                boolean killed = attack(unit, enemy);
	                                if (killed && !unitsToRemove.contains(enemy)) {
	                                    logger.info(unit.getName() + " a tué " + enemy.getName());
	                                    unitsToRemove.add(enemy);
	                                }
	                            }
	                            
	                            Building enemyBuild = MouseUtility.findEnemyBuildingAt(buildings, checkLine, checkColumn, unit);
	                            
	                            if (enemyBuild != null) {
	                                boolean killed = attackBuilding(unit, enemyBuild);
	                                if (killed && !buildingsToRemove.contains(enemyBuild)) {
	                                    logger.info(unit.getName() + " a détruit " + enemyBuild.getName());
	                                    buildingsToRemove.add(enemyBuild);
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
	    synchronized(this) {
	        for (Building deadBuilding : buildingsToRemove) {
	            removeBuilding(deadBuilding);
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
	
	public boolean attackBuilding(Unit attacker, Building building) {
	    long currentTime = System.currentTimeMillis();
	    if (currentTime - attacker.getLastAttackTime() < 1000) { 
	        return false;
	    }
	   
	    building.setCurrentHealth(building.getCurrentHealth() - attacker.getAttackDamage());

	    attacker.setLastAttackTime(System.currentTimeMillis());
	    
	    return building.getCurrentHealth() <= 0;
	}

	
	
	public void removeUnit(Unit unit) {
	    logger.info("Unité supprimée: " + unit.getName() + " à la position " + unit.getZone().getPositions().get(0));
	    map.removeFullUnitsPosition(unit.getZone().getPositions().get(0));
	    if(unit.getRace().getName().equals(mainPlayer.getRace().getName()) && (unit instanceof Slave)) {
	    	mainPlayer.setSlave(mainPlayer.getSlave()-1);
	    }
	    units.remove(unit);
	    unitSteppers.get(unit).stop();
	    unitSteppers.remove(unit);
	    System.out.println(map.getFullUnitsPosition());
	}
	
	public void removeBuilding(Building building) {
		System.out.println(buildingsMainPlayer);
		System.out.println(buildingsAIPlayer);
	    logger.info("Bâtiment supprimé: " + building.getName() + " à la position " + building.getZone().getPositions().get(0));
	    System.out.println("Building removed: " + building.getName());
	    map.removeFullPosition(building.getZone());
	    
	    buildings.remove(building);  
	    
	    String buildingType = building.getName();
	    
	    if (buildingsMainPlayer.containsValue(building)) {
	        for (String key : buildingsMainPlayer.keySet()) {
	            if (buildingsMainPlayer.get(key) == building) {
	                buildingsMainPlayer.remove(key);
	                System.out.println("Removed " + key + " from main player buildings");
	                break;
	            }
	        }
	    }
	    
	    if (buildingsAIPlayer.containsValue(building)) {
	        for (String key : buildingsAIPlayer.keySet()) {
	            if (buildingsAIPlayer.get(key) == building) {
	                buildingsAIPlayer.remove(key);
	                System.out.println("Removed " + key + " from AI player buildings");
	                break;
	            }
	        }
	    }
	}
	
	public void clearPreviousTargeting() {
	    for(Unit unit : getAllUnits()) {
	        unit.setTargeted(false);
	    }
	    
	    for(Building building : getBuildings()) {
	        building.setTargeted(false);
	    }
	}
	
	public void attackWithSelectedUnits(Position targetPosition, Unit targetUnit, Building targetBuilding) {
	    List<Unit> selectedUnits = getSelectedUnits();
	    if(!selectedUnits.isEmpty()) {
	        for(Unit unit : selectedUnits) {
	            unit.setTargetPosition(targetPosition);
	            unit.setTargetUnit(targetUnit);
	            unit.setTargetBuilding(targetBuilding);
	            unit.setManuallyCommanded(true);
	            if(unit instanceof Slave) {
	                ((Slave) unit).setHarvesting(false);
	                ((Slave) unit).setReturning(false);
	            }
	        }
	    }
	}
	
	public void checkUnitReachedDestination(Unit unit) {
	    if (unit.isManuallyCommanded() && correctPosition(unit)) {
	        unit.setManuallyCommanded(false);
	    }
	}
	
	
	public void terminateGame() {
	    logger.info("Début de la procédure de terminaison du jeu...");
	    
	    logger.info("Arrêt des " + unitSteppers.size() + " threads d'unités en cours...");
	    for (UnitStepper stepper : unitSteppers.values()) {
	        stepper.stop();
	    }
	    
	    unitSteppers.clear();
	    units.clear();
	    buildings.clear();
	    buildingsMainPlayer.clear();
	    buildingsAIPlayer.clear();
	    
	    map = null;
	    mainPlayer = null;
	    enemyPlayer = null;
	    movementManager = null;
	    
	    logger.info("Jeu terminé correctement.");
	}
	
	
	
	
	
	
	
}