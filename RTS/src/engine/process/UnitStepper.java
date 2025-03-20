package engine.process;

import org.apache.log4j.Logger;

import data.map.Map;
import data.map.Position;
import data.mobile.Unit;
import log.LoggerUtility;

import java.util.ArrayList;
import java.util.List;

import data.mobile.Slave;
import data.mobile.Unit;
import data.model.Player;
import log.LoggerUtility;
import data.map.Map;
import data.map.Position;
import config.GameConfiguration;

public class UnitStepper implements Runnable {
    private Unit unit;
    private volatile boolean running = true;
    private int speed;
    private long lastHarvestTime = 0;
    private Map map;
    private ElementManager elementManager;
    private Player player;
    
    private static Logger logger = LoggerUtility.getLogger(UnitStepper.class, "html");


    public UnitStepper(Unit unit, int speed, Map map, ElementManager elementManager,Player player) {
        this.unit = unit;
        this.speed = speed;
        this.map = map;
        this.elementManager = elementManager;
        this.player=player;
        logger.debug("Stepper créé pour " + unit.getName() + " du joueur " + player.getRace().getName());

    }

    public UnitStepper(Unit unit, Map map, ElementManager elementManager,Player player) {
        this(unit, 100, map, elementManager,player);
    }

    public void stop() {
        logger.debug("Arrêt du thread pour " + unit.getName());
        running = false;
    }

    @Override
    public void run() {
        logger.debug("Thread démarré pour " + unit.getName());
        while (running) {
            if (unit.isUnderConstruction()) {
                sleep();
                continue;
            }

            if (!elementManager.correctPosition(unit)) {
                moveUnit();
            }else {
            	elementManager.checkUnitReachedDestination(unit);
            }
            if (unit instanceof Slave) {
                handleHarvesting((Slave) unit);
            }

            sleep();
        }
        logger.debug("Thread arrêté pour " + unit.getName());
    }

    public void moveUnit() {
        Position oldPosition = new Position(
            unit.getZone().getPositions().get(0).getLine(),
            unit.getZone().getPositions().get(0).getColumn()
        );
        
        
        elementManager.moveUnitOneStep(unit);

        if (!oldPosition.equals(unit.getZone().getPositions().get(0))) {
        	logger.debug(unit.getName() + " s'est déplacé de " + oldPosition + 
                    " à " + unit.getZone().getPositions().get(0));
            map.removeFullUnitsPosition(oldPosition);
            map.addFullUnitsPosition(unit.getZone());
        }
    }

    public void handleHarvesting(Slave slave) {
        if (slave.isHarvesting() || slave.isReturning()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastHarvestTime > GameConfiguration.HARVEST_TIME) {
                logger.debug("Esclave récolte des ressources");
                elementManager.harvestResource(slave,player);
                lastHarvestTime = currentTime;
            }
        }
    }

    public void sleep() {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
