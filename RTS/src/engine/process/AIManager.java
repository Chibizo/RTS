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
    private int stage = 1;
    private boolean defense=true;
    private int number;
    private String targetRace;

    
    public AIManager(AIPlayer aiPlayer, MobileInterface mobileManager, Map map,int number) {
        this.aiPlayer = aiPlayer;
        this.mobileManager = mobileManager;
        this.map = map;
        logger.info("Gestionnaire IA initialisé pour le joueur: " + aiPlayer.getRace().getName());
        this.number=number;
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        if(number==1) {
        	this.defensivePosition=new Position(basePos.getLine() +8, basePos.getColumn()-18);
        }else {
        	this.defensivePosition=new Position(basePos.getLine()+2,basePos.getColumn()-8);
        }
        this.targetRace = GameBuilder.getRandomRace(aiPlayer.getRace().getName(), null);
        logger.info("Race initiale ciblée par l'IA: " + targetRace);
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
        
        int warriorCount = countUnitType("warrior");
        int bowmanCount = countUnitType("bowman");
        int wizardCount = countUnitType("wizard");

        
        if (hasBase() && currentSlaveCount < 4 && aiPlayer.getWood() >= GameConfiguration.SLAVE_COST_WOOD) {
            buildSlave();
            logger.info("L'IA décide de construire un esclave (total: " + (currentSlaveCount + 1) + "/4)");
            System.out.println(aiPlayer.getBuildings());

        }
        else if (!hasBarracks() && aiPlayer.getWood() >= GameConfiguration.BARRACKS_COST_WOOD) {
            buildBarracks();
            logger.info("L'IA décide de construire une caserne");
        } else if (hasBarracks() && aiPlayer.getWood() >= GameConfiguration.WARRIOR_COST_WOOD && ((warriorCount<10 || stage==1) || (bowmanCount>17 && stage==2))  && defense) {
            buildWarrior();
            System.out.println(hasBarracks());
            System.out.println(aiPlayer.getBuildings());

            logger.info("L'IA décide de construire un guerrier (total: " + (warriorCount + 1) + "/10)");
        }else if (!hasArchery() && aiPlayer.getWood() >= GameConfiguration.ARCHERY_COST_WOOD) {
        	buildArchery();
            logger.info("L'IA décide de construire un camp d'archers");
        }else if(hasArchery() && aiPlayer.getWood() >= GameConfiguration.BOWMAN_COST_WOOD && (bowmanCount<10 || stage ==2) && defense) {
        	buildBowman();
        }else if(!hasRunway() && aiPlayer.getWood() >= GameConfiguration.RUNWAY_COST_WOOD && stage==3) {
        	buildRunway();
        }else if(hasRunway() && aiPlayer.getWood() >= GameConfiguration.PLANE_COST_WOOD && (wizardCount<9 || (stage==3 && wizardCount<=16)) && defense) {
        	buildPlane();
        }
        
        assignHarvesters();
        if(defense) {
        	positionDefensiveWarriors();
        }

        if (warriorCount > 15 && stage==1) {
            targetRace = GameBuilder.getRandomRace(aiPlayer.getRace().getName(), null);

            coordinatedAttack(warriorCount - 10,"warrior");
            stage=2;
            logger.info("L'IA lance une attaque coordonnée avec " + (warriorCount - 10) + " guerriers en surplus");
        }
        if(stage==2 && warriorCount>10 && bowmanCount==0) {
            coordinatedAttack(warriorCount - 10,"warrior");
        }
        if( warriorCount>17 && bowmanCount>17) {
            targetRace = GameBuilder.getRandomRace(aiPlayer.getRace().getName(), null);
        	coordinatedAttack2(warriorCount - 10,bowmanCount-10);
        	stage=3;
        }
        if(stage==3 && warriorCount>10 && bowmanCount>10) {
        	coordinatedAttack2(warriorCount - 10,bowmanCount-10);
        }
        if(stage==3 && wizardCount>=14) {
            targetRace = GameBuilder.getRandomRace(aiPlayer.getRace().getName(), null);
            coordinatedAttack(wizardCount-6,"wizard");
            stage=4;
        }
        if(stage==4 && wizardCount>=9) {
            targetRace = GameBuilder.getRandomRace(aiPlayer.getRace().getName(), null);
        	coordinatedAttack3();
        }
        if(stage==4 && wizardCount<=1) {
        	defense=true;
        }
    }
    
    private List<Unit> getEnemyUnitsByRace(String targetRace) {
        List<Unit> enemyUnits = new ArrayList<>();
        for (Unit unit : mobileManager.getAllUnits()) {
            if (unit.getRace().getName().equals(targetRace)) {
                enemyUnits.add(unit);
            }
        }
        return enemyUnits;
    }
    
    private List<Building> getEnemyBuildingsByRace(String targetRace) {
        List<Building> enemyBuildings = new ArrayList<>();
        for (Building building : mobileManager.getBuildings()) {
            if (building.getRace().getName().equals(targetRace)) {
                enemyBuildings.add(building);
            }
        }
        return enemyBuildings;
    }
    
    private boolean hasBarracks() {
        return aiPlayer.getBuildings("barracks") != null;
        
    }
    private boolean hasBase() {
        return aiPlayer.getBuildings("base") != null;
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
    
    private boolean hasArchery() {
        return aiPlayer.getBuildings("archery") != null;
    }
    
    private boolean hasRunway() {
        return aiPlayer.getBuildings("runway") != null;
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
            basePos.getLine() + 5,
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
                barrackPos.getLine() + 4,
                barrackPos.getColumn() + getAIUnits().size() % 10
            );
            
            if (!map.isfull(unitPos) && !map.isOnBorder(unitPos)) {
                mobileManager.putWarrior(unitPos, aiPlayer);
            }
        }
    }
    
    private void buildBowman() {
        Building archery = aiPlayer.getBuildings("archery");
        if (archery != null && !archery.isUnderConstruction()) {
            Position archeryPos = archery.getZone().getPositions().get(0);
            Position unitPos = new Position(
                archeryPos.getLine() + 4,
                archeryPos.getColumn() + getAIUnits().size() % 10
            );
            
            if (!map.isfull(unitPos) && !map.isOnBorder(unitPos)) {
                mobileManager.putBowman(unitPos, aiPlayer);
            }
        }
    }
    
    private void buildPlane() {
        Building runway = aiPlayer.getBuildings("runway");
        if (runway != null && !runway.isUnderConstruction()) {
            Position runwayPos = runway.getZone().getPositions().get(0);
            Position unitPos = new Position(
                runwayPos.getLine() + 3,
                runwayPos.getColumn() + getAIUnits().size() % 10
            );
            
            if (!map.isfull(unitPos) && !map.isOnBorder(unitPos)) {
                mobileManager.putPlane(unitPos, aiPlayer);
            }
        }
    }
    
    private void buildBarracks() {
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        int startLine ;
    	int startColumn ;
        if(number==1) {
        	startLine = basePos.getLine() + 4;
        	startColumn = basePos.getColumn() - 6;
        }else {
        	startLine = basePos.getLine() -10;
        	startColumn = basePos.getColumn() -11;
        }
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Position pos = new Position(startLine + i, startColumn + j);
                
                ArrayList<Position> listPosition = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 4; l++) {
                        listPosition.add(new Position(pos.getLine() + k, pos.getColumn() + l));
                    }
                }
                boolean validZone = true;
                for (Position p : listPosition) {
                    if (map.isfull(p) || map.isOnBorder(p)) {
                        validZone = false;
                        break;
                    }
                }
                
                if (validZone) {
                    Zone zone = new Zone(listPosition);
                    mobileManager.putBuilding(zone, "barracks", aiPlayer.getRace().getName());
                    return;
                }
            }
        }
    }
    
    private void buildArchery() {
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        int startLine ;
        int startColumn ;
        if(number==1) {
        	startLine = basePos.getLine() +13;
            startColumn = basePos.getColumn() ;
        }else {
        	startLine = basePos.getLine() +2;
            startColumn = basePos.getColumn()-22 ;
        }
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Position pos = new Position(startLine + i, startColumn + j);
                
                ArrayList<Position> listPosition = new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        listPosition.add(new Position(pos.getLine() + k, pos.getColumn() + l));
                    }
                }
                
                boolean validZone = true;
                for (Position p : listPosition) {
                    if (map.isfull(p) || map.isOnBorder(p)) {
                        validZone = false;
                        break;
                    }
                }
                
                if (validZone) {
                    Zone zone = new Zone(listPosition);
                    mobileManager.putBuilding(zone, "archery", aiPlayer.getRace().getName());
                    return;
                }
            }
        }
    }
    
    private void buildRunway() {
        Position basePos = aiPlayer.getStarterZone().getPositions().get(0);
        int startLine  ;
        int startColumn ;
        if(number==1) {
        	startLine = basePos.getLine()-4;
            startColumn = basePos.getColumn() -9;
        }else {
        	startLine = basePos.getLine()-5;
            startColumn = basePos.getColumn() +2;
        }
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Position pos = new Position(startLine + i, startColumn + j);
                
                ArrayList<Position> listPosition = new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        listPosition.add(new Position(pos.getLine() + k, pos.getColumn() + l));
                    }
                }
                
                boolean validZone = true;
                for (Position p : listPosition) {
                    if (map.isfull(p) || map.isOnBorder(p)) {
                        validZone = false;
                        break;
                    }
                }
                
                if (validZone) {
                    Zone zone = new Zone(listPosition);
                    mobileManager.putBuilding(zone, "runway", aiPlayer.getRace().getName());
                    return;
                }
            }
        }
    }
    
    
    private void positionDefensiveWarriors() {
        List<Unit> warriors = new ArrayList<>();
        List<Unit> bowmans = new ArrayList<>();
        List<Unit> wizards= new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals("warrior") && !unit.isUnderConstruction() && 
                !unit.isManuallyCommanded() && unit.getTargetUnit() == null && unit.getTargetBuilding() == null) {
                warriors.add(unit);
            }
            if (unit.getName().equals("bowman") && !unit.isUnderConstruction() && 
                    !unit.isManuallyCommanded() && unit.getTargetUnit() == null && unit.getTargetBuilding() == null) {
                    bowmans.add(unit);
            }
            if (unit.getName().equals("wizard") && !unit.isUnderConstruction() && 
                    !unit.isManuallyCommanded() && unit.getTargetUnit() == null && unit.getTargetBuilding() == null) {
                    wizards.add(unit);
            }
        }
        
        int totalWarriors = warriors.size();
        int maxDefendersWarriors = Math.min(10, totalWarriors);
        int totalBowmans = bowmans.size();
        int maxDefendersBowman = Math.min(10, totalBowmans);
        int totalWizards=wizards.size();
        int maxDefendersWizard = Math.min(10, totalWizards);
        // Formation diagonale
        for (int i = 0; i < maxDefendersWarriors; i++) {
            Unit warrior = warriors.get(i);
            Position defPos ;
            if(number==1) {
                defPos = new Position(
                    defensivePosition.getLine() + i, 
                    defensivePosition.getColumn() + i
                );
            } else {
                defPos = new Position(
                    defensivePosition.getLine() - i, 
                    defensivePosition.getColumn() + i
                );
            }
            
            if (!warrior.getZone().getPositions().get(0).equals(defPos)) {
                warrior.setTargetPosition(defPos);
                warrior.setTargetUnit(null);
                warrior.setTargetBuilding(null);
            }
        }
        for (int i = 0; i < maxDefendersBowman; i++) {
            Unit bowman = bowmans.get(i);
            Position defPos ;
            if(number==1) {
                defPos = new Position(
                    defensivePosition.getLine() + i-2, 
                    defensivePosition.getColumn() + i+2
                );
            } else {
                defPos = new Position(
                    defensivePosition.getLine() - i+2, 
                    defensivePosition.getColumn() + i+2
                );
            }
            
            if (!bowman.getZone().getPositions().get(0).equals(defPos)) {
                bowman.setTargetPosition(defPos);
                bowman.setTargetUnit(null);
                bowman.setTargetBuilding(null);
            }
        }
        if (maxDefendersWizard > 0) {
            int wizardCount = 0;
            for (int line = 0; line < 5 && wizardCount < maxDefendersWizard; line += 2) {
                for (int col = 0; col < 5 && wizardCount < maxDefendersWizard; col += 2) {
                    Unit wizard = wizards.get(wizardCount);
                    Position defPos;
                    if(number==1) {
                        defPos = new Position(
                            defensivePosition.getLine()+line+5,
                            defensivePosition.getColumn()+col-5  
                        );
                    } else {
                        defPos = new Position(
                            defensivePosition.getLine()-line-5,
                            defensivePosition.getColumn()-col-5  
                        );
                    }
                    
                    if (!wizard.getZone().getPositions().get(0).equals(defPos)) {
                        wizard.setTargetPosition(defPos);
                        wizard.setTargetUnit(null);
                        wizard.setTargetBuilding(null);
                    }
                    wizardCount++;
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
                	if(number==1) {
                		woodPos = woodPositions.get(woodPositions.size() - 12);
                	}else {
                		woodPos = woodPositions.get(woodPositions.size() - 1);

                	}
                    slave.setHarvestingResourceType("wood");  
                    mobileManager.startHarvesting(slave, woodPos, aiPlayer);
                    woodHarvesters++;
                    logger.info("Nouvel esclave assigné à la récolte de bois");
                }
            } else if (oreHarvesters < 2) {
                List<Position> magicOrePositions = map.getMagicOreLocations().getPositions();
                if (!magicOrePositions.isEmpty()) {
            		Position orePos = magicOrePositions.get(magicOrePositions.size() - 5);
                	if(number==1) {
                		orePos = magicOrePositions.get(magicOrePositions.size() - 5);
                	}else {
                		orePos = magicOrePositions.get(magicOrePositions.size() - 1);

                	}
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
    
    private void coordinatedAttack(int attackForceSize,String type) {
    	
        List<Unit> allWarriors = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals(type) && !unit.isUnderConstruction()) {
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
            List<Unit> enemyUnits = getEnemyUnitsByRace(targetRace);
            List<Building> enemyBuildings = getEnemyBuildingsByRace(targetRace);
            
            Position attackerBasePosition = aiPlayer.getStarterZone().getPositions().get(0);
            Unit nearestEnemyUnit = null;
            Building nearestEnemyBuilding = null;
            int minUnitDistance = Integer.MAX_VALUE;
            int minBuildingDistance = Integer.MAX_VALUE;
            
            for (Unit enemyUnit : enemyUnits) {
                int distance = mobileManager.calculateDistance(attackerBasePosition, enemyUnit.getZone().getPositions().get(0));
                if (distance < minUnitDistance && !enemyUnit.isUnderConstruction()) {
                    nearestEnemyUnit = enemyUnit;
                    minUnitDistance = distance;
                }
            }
            
            for (Building enemyBuilding : enemyBuildings) {
                int distance = mobileManager.calculateDistance(attackerBasePosition, enemyBuilding.getZone().getPositions().get(0));
                if (distance < minBuildingDistance && !enemyBuilding.isUnderConstruction()) {
                    nearestEnemyBuilding = enemyBuilding;
                    minBuildingDistance = distance;
                }
            }
            
            if (nearestEnemyUnit == null && nearestEnemyBuilding == null) {
                logger.info("Aucune cible de race " + targetRace + " trouvée, recherche d'une cible alternative");
                
                // Obtenir toutes les unités ennemies, quelle que soit leur race
                List<Unit> allEnemyUnits = new ArrayList<>();
                for (Unit unit : mobileManager.getAllUnits()) {
                    if (!unit.getRace().getName().equals(aiPlayer.getRace().getName())) {
                        allEnemyUnits.add(unit);
                    }
                }
                
                // Obtenir tous les bâtiments ennemis, quelle que soit leur race
                List<Building> allEnemyBuildings = new ArrayList<>();
                for (Building building : mobileManager.getBuildings()) {
                    if (!building.getRace().getName().equals(aiPlayer.getRace().getName())) {
                        allEnemyBuildings.add(building);
                    }
                }
                
                // Trouver la cible la plus proche parmi toutes les unités ennemies
                for (Unit enemyUnit : allEnemyUnits) {
                    int distance = mobileManager.calculateDistance(attackerBasePosition, enemyUnit.getZone().getPositions().get(0));
                    if (distance < minUnitDistance && !enemyUnit.isUnderConstruction()) {
                        nearestEnemyUnit = enemyUnit;
                        minUnitDistance = distance;
                    }
                }
                
                // Trouver la cible la plus proche parmi tous les bâtiments ennemis
                for (Building enemyBuilding : allEnemyBuildings) {
                    int distance = mobileManager.calculateDistance(attackerBasePosition, enemyBuilding.getZone().getPositions().get(0));
                    if (distance < minBuildingDistance && !enemyBuilding.isUnderConstruction()) {
                        nearestEnemyBuilding = enemyBuilding;
                        minBuildingDistance = distance;
                    }
                }
            }
            
            if (nearestEnemyUnit != null) {
                Position targetPos = nearestEnemyUnit.getZone().getPositions().get(0);
                
                logger.info("L'IA cible l'unité ennemie la plus proche à la position: " + targetPos + " avec " + attackForce.size() + " guerriers");
                
                for (Unit warrior : attackForce) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(nearestEnemyUnit);
                    warrior.setTargetBuilding(null);
                    warrior.setManuallyCommanded(true);
                }
            } 
            else if (nearestEnemyBuilding != null) {
                Position targetPos = nearestEnemyBuilding.getZone().getPositions().get(0);
                
                logger.info("L'IA cible le bâtiment ennemi le plus proche: " + nearestEnemyBuilding.getName() + " avec " + attackForce.size() + " guerriers");
                
                for (Unit warrior : attackForce) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(null);
                    warrior.setTargetBuilding(nearestEnemyBuilding);
                    warrior.setManuallyCommanded(true);
                }
            }
        }
    }
    
    private void coordinatedAttack2(int attackForceWarriorSize, int attackForceBowmanSize) {
        List<Unit> allWarriors = new ArrayList<>();
        List<Unit> allBowmans = new ArrayList<>();
        
        for (Unit unit : getAIUnits()) {
            if (unit.getName().equals("warrior") && !unit.isUnderConstruction()) {
                allWarriors.add(unit);
            }
            if (unit.getName().equals("bowman") && !unit.isUnderConstruction()) {
                allBowmans.add(unit);
            }
        }
        
        if (allWarriors.size() <= 10 || allBowmans.size() <= 10) {
            return;
        }
        
        List<Unit> attackForceWarriors = new ArrayList<>();
        List<Unit> attackForceBowmans = new ArrayList<>();
        
        for (int i = 0; i < attackForceWarriorSize && i < allWarriors.size() - 10; i++) {
            attackForceWarriors.add(allWarriors.get(allWarriors.size() - 1 - i));
        }
        
        for (int i = 0; i < attackForceBowmanSize && i < allBowmans.size() - 10; i++) {
            attackForceBowmans.add(allBowmans.get(allBowmans.size() - 1 - i));
        }
        
        if (!attackForceWarriors.isEmpty() || !attackForceBowmans.isEmpty()) {
            List<Unit> enemyUnits = new ArrayList<>();
            for (Unit unit : mobileManager.getAllUnits()) {
                if (!unit.getRace().getName().equals(aiPlayer.getRace().getName())) {
                    enemyUnits.add(unit);
                }
            }
            
            List<Building> enemyBuildings = new ArrayList<>();
            for (Building building : mobileManager.getBuildings()) {
                if (!building.getRace().getName().equals(aiPlayer.getRace().getName())) {
                    enemyBuildings.add(building);
                }
            }
            
            Position attackerBasePosition = aiPlayer.getStarterZone().getPositions().get(0);
            Unit nearestEnemyUnit = null;
            Building nearestEnemyBuilding = null;
            int minUnitDistance = Integer.MAX_VALUE;
            int minBuildingDistance = Integer.MAX_VALUE;
            
            for (Unit enemyUnit : enemyUnits) {
                int distance = mobileManager.calculateDistance(attackerBasePosition, enemyUnit.getZone().getPositions().get(0));
                if (distance < minUnitDistance && !enemyUnit.isUnderConstruction()) {
                    nearestEnemyUnit = enemyUnit;
                    minUnitDistance = distance;
                }
            }
            
            for (Building enemyBuilding : enemyBuildings) {
                int distance = mobileManager.calculateDistance(attackerBasePosition, enemyBuilding.getZone().getPositions().get(0));
                if (distance < minBuildingDistance && !enemyBuilding.isUnderConstruction()) {
                    nearestEnemyBuilding = enemyBuilding;
                    minBuildingDistance = distance;
                }
            }
            
            if (nearestEnemyUnit != null) {
                Position targetPos = nearestEnemyUnit.getZone().getPositions().get(0);
                
                logger.info("L'IA cible l'unité ennemie la plus proche à la position: " + targetPos + 
                           " avec " + attackForceWarriors.size() + " guerriers et " + 
                           attackForceBowmans.size() + " archers");
                
                for (Unit warrior : attackForceWarriors) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(nearestEnemyUnit);
                    warrior.setTargetBuilding(null);
                    warrior.setManuallyCommanded(true);
                }
                
                for (Unit bowman : attackForceBowmans) {
                    Position bowmanPos = new Position(
                        targetPos.getLine() + 3, 
                        targetPos.getColumn() + 3
                    );
                    bowman.setTargetPosition(bowmanPos);
                    bowman.setTargetUnit(nearestEnemyUnit);
                    bowman.setTargetBuilding(null);
                    bowman.setManuallyCommanded(true);
                }
            } 
            else if (nearestEnemyBuilding != null) {
                Position targetPos = nearestEnemyBuilding.getZone().getPositions().get(0);
                
                logger.info("L'IA cible le bâtiment ennemi le plus proche: " + nearestEnemyBuilding.getName() + 
                           " avec " + attackForceWarriors.size() + " guerriers et " + 
                           attackForceBowmans.size() + " archers");
                
                for (Unit warrior : attackForceWarriors) {
                    warrior.setTargetPosition(targetPos);
                    warrior.setTargetUnit(null);
                    warrior.setTargetBuilding(nearestEnemyBuilding);
                    warrior.setManuallyCommanded(true);
                }
                
                for (Unit bowman : attackForceBowmans) {
                    Position bowmanPos = new Position(
                        targetPos.getLine() + 3, 
                        targetPos.getColumn() + 3
                    );
                    bowman.setTargetPosition(bowmanPos);
                    bowman.setTargetUnit(null);
                    bowman.setTargetBuilding(nearestEnemyBuilding);
                    bowman.setManuallyCommanded(true);
                }
            }
        }
    }
    
    
    private void coordinatedAttack3() {
    	defense=false;
        List<Unit> allCombatUnits = new ArrayList<>();
        for (Unit unit : getAIUnits()) {
            if ((unit.getName().equals("warrior") || unit.getName().equals("bowman") || unit.getName().equals("wizard")) 
                && !unit.isUnderConstruction()) {
                allCombatUnits.add(unit);
            }
        }
        if (allCombatUnits.isEmpty()) {
            logger.info("L'IA ne peut pas lancer d'attaque totale: aucune unité de combat disponible");
            return;
        }
        
        logger.info("L'IA lance une attaque massive avec toutes les unités: " + allCombatUnits.size() + " unités de combat");
        
        List<Unit> enemyUnits = new ArrayList<>();
        for (Unit unit : mobileManager.getAllUnits()) {
            if (!unit.getRace().getName().equals(aiPlayer.getRace().getName())) {
                enemyUnits.add(unit);
            }
        }
        
        List<Building> enemyBuildings = new ArrayList<>();
        for (Building building : mobileManager.getBuildings()) {
            if (!building.getRace().getName().equals(aiPlayer.getRace().getName())) {
                enemyBuildings.add(building);
            }
        }
        
        Position attackerBasePosition = aiPlayer.getStarterZone().getPositions().get(0);
        Unit nearestEnemyUnit = null;
        Building nearestEnemyBuilding = null;
        int minUnitDistance = Integer.MAX_VALUE;
        int minBuildingDistance = Integer.MAX_VALUE;
        
        for (Unit enemyUnit : enemyUnits) {
            int distance = mobileManager.calculateDistance(attackerBasePosition, enemyUnit.getZone().getPositions().get(0));
            if (distance < minUnitDistance && !enemyUnit.isUnderConstruction()) {
                nearestEnemyUnit = enemyUnit;
                minUnitDistance = distance;
            }
        }
        
        for (Building enemyBuilding : enemyBuildings) {
            int distance = mobileManager.calculateDistance(attackerBasePosition, enemyBuilding.getZone().getPositions().get(0));
            if (distance < minBuildingDistance && !enemyBuilding.isUnderConstruction()) {
                nearestEnemyBuilding = enemyBuilding;
                minBuildingDistance = distance;
            }
        }
        
        if (nearestEnemyUnit != null) {
            Position targetPos = nearestEnemyUnit.getZone().getPositions().get(0);
            
            logger.info("L'IA cible l'unité ennemie la plus proche à la position: " + targetPos);
            
            for (Unit unit : allCombatUnits) {
                unit.setTargetPosition(targetPos);
                unit.setTargetUnit(nearestEnemyUnit);
                unit.setTargetBuilding(null);
                unit.setManuallyCommanded(true);
            }
        } 
        else if (nearestEnemyBuilding != null) {
            Position targetPos = nearestEnemyBuilding.getZone().getPositions().get(0);
            
            logger.info("L'IA cible le bâtiment ennemi le plus proche: " + nearestEnemyBuilding.getName());
            
            for (Unit unit : allCombatUnits) {
                unit.setTargetPosition(targetPos);
                unit.setTargetUnit(null);
                unit.setTargetBuilding(nearestEnemyBuilding);
                unit.setManuallyCommanded(true);
            }
        } 
        
    }
    
}