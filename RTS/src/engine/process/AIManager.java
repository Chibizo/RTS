package engine.process;

import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.Building;
import data.mobile.Slave;
import data.mobile.Unit;
import data.model.AIPlayer;
import data.model.Player;
import config.GameConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIManager {
    
    private AIPlayer aiPlayer;
    private MobileInterface mobileManager;
    private Map map;
    private Random random = new Random();
    
    public AIManager(AIPlayer aiPlayer, MobileInterface mobileManager, Map map) {
        this.aiPlayer = aiPlayer;
        this.mobileManager = mobileManager;
        this.map = map;
    }
    
    public void update() {
        if (!aiPlayer.canMakeDecision()) {
            return;
        }
        makeHardDecisions();
     
    }
    
   
    
    private void makeHardDecisions() {
    	if (countUnitType("slave") < 4 && aiPlayer.getWood() >= GameConfiguration.SLAVE_COST) {
    		buildSlave();
    	}
    	else if (!hasBarracks() && aiPlayer.getWood() >= GameConfiguration.BARRACKS_COST) {
            buildBarracks();
        } else if (hasBarracks() && aiPlayer.getWood() >= GameConfiguration.WARRIOR_COST) {
            buildWarrior();
        }
        
        assignHarvesters();
        
        if (countUnitType("warrior") >= 3) {
            coordinatedAttack();
        }
    }
    
    private boolean hasBarracks() {
        return aiPlayer.getBuildings("barracks") != null;
    }
    
    private int countUnitType(String unitType) {
        int count = 0;
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals(unitType)) {
                count++;
            }
        }
        return count;
    }
    
    private List<Unit> getAIUnits() {
        List<Unit> aiUnits = new ArrayList<>();
        for (Unit unit : mobileManager.getAllUnits()) {
            if (unit.getRace().getName().equals(aiPlayer.getRace().getName())) {
                aiUnits.add(unit);
            }
        }
        return aiUnits;
    }
    

    
    private void buildSlave() {
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        Position unitPos = new Position(
            basePos.getLine() + 3,
            basePos.getColumn() + getAIUnits().size() % 10
        );
        
        if (!map.isfull(unitPos) && !map.isOnBorder(unitPos)) {
            mobileManager.putSlave(unitPos, aiPlayer);
        }
    }
    
    private void buildWarrior() {
        Building barracks = aiPlayer.getBuildings("barracks");
        if (barracks != null && !barracks.isUnderConstruction()) {
            Position barrackPos = barracks.getZone().getPositions().get(0);
            Position unitPos = new Position(
                barrackPos.getLine() + 3,
                barrackPos.getColumn() + getAIUnits().size() % 10
            );
            
            if (!map.isfull(unitPos) && !map.isOnBorder(unitPos)) {
                mobileManager.putWarrior(unitPos, aiPlayer);
            }
        }
    }
    
    private void buildBarracks() {
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        int startLine = basePos.getLine() + 5;
        int startColumn = basePos.getColumn() - 5;
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Position pos = new Position(startLine + i, startColumn + j);
                
                ArrayList<Position> listPosition = new ArrayList<>();
                listPosition.add(pos);
                listPosition.add(new Position(pos.getLine() + 1, pos.getColumn()));
                listPosition.add(new Position(pos.getLine(), pos.getColumn() + 1));
                listPosition.add(new Position(pos.getLine() + 1, pos.getColumn() + 1));
                
                boolean validZone = true;
                for (Position p : listPosition) {
                    if (map.isfull(p) || map.isOnBorder(p)) {
                        validZone = false;
                        break;
                    }
                }
                
                if (validZone) {
                    Zone zone = new Zone(listPosition);
                    mobileManager.putBuilding(zone, "barracks", aiPlayer);
                    return;
                }
            }
        }
    }
    
    
    

    
  
    
    
    
    private void assignHarvesters() {
       List<Slave> idleSlaves = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit instanceof Slave && !unit.isUnderConstruction() && 
                !((Slave)unit).isHarvesting() && !((Slave)unit).isReturning()) {
                idleSlaves.add((Slave)unit);
            }
        }
        
        if(idleSlaves.size()==4) {
	        for (int i = 0; i < idleSlaves.size(); i++) {
	            Slave slave = idleSlaves.get(i);
	            if (i % 2 == 0 ) {
	                mobileManager.startHarvesting(slave, map.getWoodLocations().getPositions().getLast(),aiPlayer);
	            } else  {
	                mobileManager.startHarvesting(slave, map.getMagicOreLocations().getPositions().getLast(),aiPlayer);
	            }
	        }
        }
    }
    
    
    private void coordinatedAttack() {
        List<Unit> warriors = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals("warrior") && !unit.isUnderConstruction()) {
                warriors.add(unit);
            }
        }
        
        if (warriors.size() >= 3) {
            Unit targetUnit = null;
            Position targetPos = null;
            
            List<Unit> playerUnits = new ArrayList<>();
            for (Unit unit : mobileManager.getAllUnits()) {
                if (!unit.getRace().getName().equals(aiPlayer.getRace().getName())) {
                    playerUnits.add(unit);
                }
            }
            
            if (!playerUnits.isEmpty()) {
                targetUnit = playerUnits.get(0);
                targetPos = targetUnit.getZone().getPositions().get(0);
                
                for (Unit warrior : warriors) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(targetUnit);
                    warrior.setTargetBuilding(null);
                }
                return;
            }
            
            Player mainPlayer = mobileManager.getMainPlayer();
            if (mainPlayer != null && !mainPlayer.getStarterZone().getPositions().isEmpty()) {
                targetPos = mainPlayer.getStarterZone().getPositions().get(0);
                
                Building targetBuilding = null;
                for (Building building : mobileManager.getBuildings()) {
                    if (!building.getRace().getName().equals(aiPlayer.getRace().getName())) {
                        targetBuilding = building;
                        break;
                    }
                }
                
                for (Unit warrior : warriors) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(null);
                    warrior.setTargetBuilding(targetBuilding);
                }
            }
        }
    }
    
    
}