package engine.process;

import java.util.HashMap;
import java.util.List;
import data.map.*;
import data.mobile.*;
import data.model.Player;

public interface MobileInterface {

	
	void putBuilding(Zone zone,String type,Player player);
	
	public void  putWarrior(Zone zone,Player player);
	public void  putWarrior(Position position,Player player);
	
	public void  putWizard(Zone zone,Player player);
	public void  putWizard(Position position,Player player);
	
	public void  putBowman(Zone zone,Player player);
	public void  putBowman(Position position,Player player);
	
	public List<Building> getBuildings();
	
	public HashMap<String,Building> getBuildingsMainPlayer();
	public HashMap<String,Building> getBuildingsAIPlayer();
	
	Unit getUnit();
		
	void moveUnitOneStep(Unit unit);
	
	boolean correctPosition(Unit unit);
	
	public List<Unit> getAllUnits();
	
	public void selectMostRecentUnit();
	
	public Player getMainPlayer();
	
	public String getResourceTypeAt(Position position);
	
	public void startHarvesting(Slave slave, Position resourcePosition,Player player);
	
	public void harvestResource(Slave slave,Player player);
	
	public  void putSlave(Zone zone,Player player);
	public  void putSlave(Position position,Player player);
	
	public void updateConstruction();
	
	public List<Unit> getSelectedUnits();
	
	public void checkCombat();
	
	public void checkCloseEnemy();
	
	public int calculateDistance(Position p1, Position p2);
		
	public boolean attack(Unit attacker, Unit defender);
	
	public void removeUnit(Unit unit);
	
	public void clearPreviousTargeting();
	
	public void attackWithSelectedUnits(Position targetPosition, Unit targetUnit, Building targetBuilding);
	
	public void checkUnitReachedDestination(Unit unit);
	
	public void upgradeBuilding(Building building,Player player);
	
	public void terminateGame() ;
	
	public Position findPositionAtRange(Position currentPos, Position targetPos, int range);
}