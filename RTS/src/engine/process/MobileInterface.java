package engine.process;

import java.util.HashMap;
import java.util.List;
import data.map.*;
import data.mobile.*;
import data.model.Player;

public interface MobileInterface {

	
	void putBuilding(Zone zone,String type);
	
	void putUnit(Zone zone);
	void putUnit(Position position);
	
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
	
	public void putSlave(Zone zone);
	public void putSlave(Position position);
	
	public List<Unit> getSelectedUnits();

}