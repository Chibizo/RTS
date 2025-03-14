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
    	if (countUnitType("slave") < 5 && aiPlayer.getWood() >= GameConfiguration.SLAVE_COST) {
    		buildSlave();
    	}
    	else if (!hasBarracks() && aiPlayer.getWood() >= GameConfiguration.BARRACKS_COST) {
            buildBarracks();
        } else if (hasBarracks() && aiPlayer.getWood() >= GameConfiguration.WARRIOR_COST) {
            buildWarrior();
        }
        
        // Gestion optimisée des ressources
        assignHarvesters();
        
        // Stratégie d'attaque plus agressive
        if (countUnitType("warrior") >= 3) {
            coordinatedAttack();
        }
    }
    
    // Méthodes utilitaires pour les actions
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
        // Implémenter la construction d'un esclave
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
        // Implémenter la construction d'un guerrier
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
        // Rechercher un emplacement valide pour construire une caserne
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        int startLine = basePos.getLine() + 5;
        int startColumn = basePos.getColumn() - 5;
        
        // Trouver une zone valide de 2x2 pour la caserne
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
    
    private void moveUnitsRandomly() {
        for (Unit unit : getAIUnits()) {
            if (random.nextInt(5) < 2 && !unit.isUnderConstruction()) {
                // Déplacer l'unité vers une position aléatoire
                Position currentPos = unit.getZone().getPositions().get(0);
                Position newPos = new Position(
                    currentPos.getLine() + random.nextInt(11) - 5,
                    currentPos.getColumn() + random.nextInt(11) - 5
                );
                
                // Vérifier que la position est valide
                if (newPos.getLine() >= 2 && newPos.getLine() < map.getLineCount() - 2 &&
                    newPos.getColumn() >= 0 && newPos.getColumn() < map.getColumnCount() - 2 &&
                    !map.isfull(newPos)) {
                    unit.setTargetPosition(newPos);
                }
            }
        }
    }
    

    
  
    
    private int calculateDistance(Position p1, Position p2) {
        return Math.abs(p1.getLine() - p2.getLine()) + Math.abs(p1.getColumn() - p2.getColumn());
    }
    
    private void assignHarvesters() {
        // Optimiser l'attribution des esclaves aux ressources
        List<Slave> idleSlaves = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit instanceof Slave && !unit.isUnderConstruction() && 
                !((Slave)unit).isHarvesting() && !((Slave)unit).isReturning()) {
                idleSlaves.add((Slave)unit);
            }
        }
        
        // Attribuer certains esclaves au bois et d'autres au minerai
        if(idleSlaves.size()==6) {
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
    
    private void attackPlayer() {
        // Faire attaquer aléatoirement les unités disponibles
        List<Unit> warriors = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals("warrior") && !unit.isUnderConstruction()) {
                warriors.add(unit);
            }
        }
        
        if (!warriors.isEmpty()) {
            // Trouver une cible (la base ou une unité du joueur)
            Position targetPos = findPlayerTarget();
            if (targetPos != null) {
                for (Unit warrior : warriors) {
                    if (random.nextBoolean()) {
                        warrior.setTargetPosition(targetPos);
                    }
                }
            }
        }
    }
    
    private void coordinatedAttack() {
        // Attaque coordonnée avec toutes les unités de combat disponibles
        List<Unit> warriors = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals("warrior") && !unit.isUnderConstruction()) {
                warriors.add(unit);
            }
        }
        
        if (warriors.size() >= 3) {
            Position targetPos = findPlayerTarget();
            if (targetPos != null) {
                for (Unit warrior : warriors) {
                    warrior.setTargetPosition(targetPos);
                }
            }
        }
    }
    
    private Position findPlayerTarget() {
        // Rechercher la base ou une unité du joueur comme cible
        Player mainPlayer = null;
        for (Unit unit : mobileManager.getAllUnits()) {
            if (!unit.getRace().getName().equals(aiPlayer.getRace().getName())) {
                mainPlayer = mobileManager.getMainPlayer();
                break;
            }
        }
        
        if (mainPlayer != null) {
            // Cibler la base du joueur en priorité
            if (!mainPlayer.getStarterZone().getPositions().isEmpty()) {
                return mainPlayer.getStarterZone().getPositions().get(0);
            }
            
            // Sinon, cibler une unité aléatoire du joueur
            List<Unit> playerUnits = new ArrayList<>();
            for (Unit unit : mobileManager.getAllUnits()) {
                if (!unit.getRace().getName().equals(aiPlayer.getRace().getName())) {
                    playerUnits.add(unit);
                }
            }
            
            if (!playerUnits.isEmpty()) {
                Unit target = playerUnits.get(random.nextInt(playerUnits.size()));
                return target.getZone().getPositions().get(0);
            }
        }
        
        return null;
    }
}