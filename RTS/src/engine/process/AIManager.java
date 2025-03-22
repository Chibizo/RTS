package engine.process;

import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.Building;
import data.mobile.Slave;
import data.mobile.Unit;
import data.model.AIPlayer;
import data.model.Player;
import log.LoggerUtility;
import config.GameConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class AIManager {
    
    private AIPlayer aiPlayer;
    private MobileInterface mobileManager;
    private Map map;
    private Random random = new Random();
    private Position defensivePosition;     
    private static Logger logger = LoggerUtility.getLogger(Map.class, "html");

    
    public AIManager(AIPlayer aiPlayer, MobileInterface mobileManager, Map map) {
        this.aiPlayer = aiPlayer;
        this.mobileManager = mobileManager;
        this.map = map;
        logger.info("Gestionnaire IA initialisé pour le joueur: " + aiPlayer.getRace().getName());
        
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        this.defensivePosition = new Position(basePos.getLine() +10, basePos.getColumn()-10);
    }
    
    public void update() {
        if (!aiPlayer.canMakeDecision()) {
            return;
        }
        makeHardDecisions();
        logger.debug("L'IA prend une décision...");
    }
    
    private void makeHardDecisions() {
        int currentSlaveCount = countUnitType("slave");
        
        if (currentSlaveCount < 4 && aiPlayer.getWood() >= GameConfiguration.SLAVE_COST) {
            buildSlave();
            logger.info("L'IA décide de construire un esclave (total: " + (currentSlaveCount + 1) + "/4)");
        }
        else if (!hasBarracks() && aiPlayer.getWood() >= GameConfiguration.BARRACKS_COST) {
            buildBarracks();
            logger.info("L'IA décide de construire une caserne");
        } else if (hasBarracks() && aiPlayer.getWood() >= GameConfiguration.WARRIOR_COST) {
            buildWarrior();
            logger.info("L'IA décide de construire un guerrier (total: " + (countUnitType("warrior") + 1) + "/10)");
        }
        
        assignHarvesters();
        positionDefensiveWarriors();
        
        int warriorCount = countUnitType("warrior");
        if (warriorCount > 15) {
            coordinatedAttack(warriorCount - 10);
            logger.info("L'IA lance une attaque coordonnée avec " + (warriorCount - 10) + " guerriers en surplus");
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
    
    private void positionDefensiveWarriors() {
        List<Unit> warriors = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals("warrior") && !unit.isUnderConstruction() && 
                !unit.isManuallyCommanded() && unit.getTargetUnit() == null && unit.getTargetBuilding() == null) {
                warriors.add(unit);
            }
        }
        
        int totalWarriors = warriors.size();
        int maxDefenders = Math.min(10, totalWarriors);
        
        for (int i = 0; i < maxDefenders; i++) {
            Unit warrior = warriors.get(i);
            Position defPos = new Position(
                defensivePosition.getLine(),
                defensivePosition.getColumn() + (i - maxDefenders/2)
            );
            
            if (!warrior.getZone().getPositions().get(0).equals(defPos)) {
                warrior.setTargetPosition(defPos);
                warrior.setTargetUnit(null);
                warrior.setTargetBuilding(null);
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
        
        int woodHarvesters = 0;
        int oreHarvesters = 0;
        
        for (Unit unit : getAIUnits()) {
            if (unit instanceof Slave && !unit.isUnderConstruction()) {
                Slave slave = (Slave)unit;
                if ((slave.isHarvesting() || slave.isReturning()) && slave.getResourcePosition() != null) {
                    boolean isWoodPosition = false;
                    for (Position woodPos : map.getWoodLocations().getPositions()) {
                        if (woodPos.equals(slave.getResourcePosition())) {
                            isWoodPosition = true;
                            woodHarvesters++;
                            break;
                        }
                    }
                    
                    if (!isWoodPosition) {
                        oreHarvesters++;
                    }
                }
            }
        }
        
        logger.debug("Esclaves actuellement assignés - Bois: " + woodHarvesters + ", Minerai: " + oreHarvesters + 
                     ", Inactifs à assigner: " + idleSlaves.size());
        
        for (Slave slave : idleSlaves) {
            if (woodHarvesters < 2) {
                List<Position> woodPositions = map.getWoodLocations().getPositions();
                if (!woodPositions.isEmpty()) {
                    Position woodPos = woodPositions.get(woodPositions.size() - 1);
                    slave.setHarvestingResourceType("wood");  
                    mobileManager.startHarvesting(slave, woodPos, aiPlayer);
                    woodHarvesters++;
                    logger.info("Nouvel esclave assigné à la récolte de bois");
                }
            } else if (oreHarvesters < 2) {
                List<Position> magicOrePositions = map.getMagicOreLocations().getPositions();
                if (!magicOrePositions.isEmpty()) {
                    Position orePos = magicOrePositions.get(magicOrePositions.size() - 1);
                    slave.setHarvestingResourceType("magicOre"); 
                    mobileManager.startHarvesting(slave, orePos, aiPlayer);
                    oreHarvesters++;
                    logger.info("Nouvel esclave assigné à la récolte de minerai");
                }
            } else {
                if (random.nextBoolean()) {
                    List<Position> woodPositions = map.getWoodLocations().getPositions();
                    if (!woodPositions.isEmpty()) {
                        Position woodPos = woodPositions.get(woodPositions.size() - 1);
                        slave.setHarvestingResourceType("wood");  
                        mobileManager.startHarvesting(slave, woodPos, aiPlayer);
                        logger.info("Esclave supplémentaire assigné à la récolte de bois");
                    }
                } else {
                    List<Position> magicOrePositions = map.getMagicOreLocations().getPositions();
                    if (!magicOrePositions.isEmpty()) {
                        Position orePos = magicOrePositions.get(magicOrePositions.size() - 1);
                        slave.setHarvestingResourceType("magicOre");
                        mobileManager.startHarvesting(slave, orePos, aiPlayer);
                        logger.info("Esclave supplémentaire assigné à la récolte de minerai");
                    }
                }
            }
        }
    }
    
    private void coordinatedAttack(int attackForceSize) {
        List<Unit> allWarriors = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals("warrior") && !unit.isUnderConstruction()) {
                allWarriors.add(unit);
            }
        }
        
        if (allWarriors.size() <= 10) {
            return;
        }
        
        List<Unit> attackForce = new ArrayList<>();
        for (int i = 0; i < attackForceSize && i < allWarriors.size() - 10; i++) {
            attackForce.add(allWarriors.get(allWarriors.size() - 1 - i));
        }
        
        if (!attackForce.isEmpty()) {
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
                
                logger.info("L'IA cible une unité ennemie à la position: " + targetPos + " avec " + attackForce.size() + " guerriers");
                
                for (Unit warrior : attackForce) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(targetUnit);
                    warrior.setTargetBuilding(null);
                    warrior.setManuallyCommanded(true);
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
                        logger.info("L'IA cible un bâtiment ennemi: " + targetBuilding.getName() + " avec " + attackForce.size() + " guerriers");
                        break;
                    }
                }
                
                for (Unit warrior : attackForce) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(null);
                    warrior.setTargetBuilding(targetBuilding);
                    warrior.setManuallyCommanded(true);
                }
            }
        }
    }
}