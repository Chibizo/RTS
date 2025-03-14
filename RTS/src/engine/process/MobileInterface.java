package engine.process;

import java.util.HashMap;
import java.util.List;
import data.map.*;
import data.mobile.*;
import data.model.Player;

public interface MobileInterface {

	
	void putBuilding(Zone zone,String type,Player player);
	
	void putWarrior(Zone zone,Player player);
	void putWarrior(Position position,Player player);
	
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
	
	public void putSlave(Zone zone,Player player);
	public void putSlave(Position position,Player player);
	
	public void updateConstruction();
	
	public List<Unit> getSelectedUnits();
	
	public void checkCombat();
	
	public boolean areUnitsClose(Unit unit1, Unit unit2);
	
	public void attack(Unit attacker, Unit defender);
	
	public void removeUnit(Unit unit);

}