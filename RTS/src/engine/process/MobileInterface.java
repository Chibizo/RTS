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
	
	HashMap<String,Building> getBuildings();
	
	Unit getUnit();
		
	void moveUnitOneStep(Unit unit);
	
	boolean correctPosition(Unit unit);
	
	public List<Unit> getAllUnits();
	
	public void selectMostRecentUnit();
	
	public Player getMainPlayer();
	
	public String getResourceTypeAt(Position position);
	
	public void startHarvesting(Slave slave, Position resourcePosition);
	
	public void harvestResource(Slave slave);
	
	public void putSlave(Zone zone,Player player);
	public void putSlave(Position position,Player player);
	
	public void updateConstruction();
	
	public List<Unit> getSelectedUnits();

}