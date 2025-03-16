package engine.process;


import data.mobile.Slave;
import data.mobile.Unit;
import data.model.Player;
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

    public UnitStepper(Unit unit, int speed, Map map, ElementManager elementManager,Player player) {
        this.unit = unit;
        this.speed = speed;
        this.map = map;
        this.elementManager = elementManager;
        this.player=player;
    }

    public UnitStepper(Unit unit, Map map, ElementManager elementManager,Player player) {
        this(unit, 100, map, elementManager,player);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (unit.isUnderConstruction()) {
                sleep();
                continue;
            }

            if (!elementManager.correctPosition(unit)) {
                moveUnit();
            }
            if (unit instanceof Slave) {
                handleHarvesting((Slave) unit);
            }

            sleep();
        }
    }

    public void moveUnit() {
        Position oldPosition = new Position(
            unit.getZone().getPositions().get(0).getLine(),
            unit.getZone().getPositions().get(0).getColumn()
        );
        
        
        elementManager.moveUnitOneStep(unit);

        if (!oldPosition.equals(unit.getZone().getPositions().get(0))) {
            map.removeFullUnitsPosition(oldPosition);
            map.addFullUnitsPosition(unit.getZone());
        }
    }

    public void handleHarvesting(Slave slave) {
        if (slave.isHarvesting() || slave.isReturning()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastHarvestTime > GameConfiguration.HARVEST_TIME) {
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
