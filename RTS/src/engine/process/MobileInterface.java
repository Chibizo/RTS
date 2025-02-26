package engine.process;

import data.map.*;
import data.mobile.*;
import data.model.Player;

public interface MobileInterface {

	
	void putBuilding(Zone zone);
	
	void putUnit(Zone zone);
	void putUnit(Position position);
	
	Building getBuilding();
	
	Unit getUnit();
	
	void moveUnit();
	
	boolean correctPosition();
	
	public Player getMainPlayer();
}
