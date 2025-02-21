package engine.process;

import data.map.Zone;
import data.mobile.*;

public interface MobileInterface {

	
	void putBuilding(Zone zone);
	
	void putUnit(Zone zone);
	
	Building getBuilding();
	
	Unit getUnit();
	
	void moveUnit();
}
