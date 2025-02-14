package data.map;

import java.util.ArrayList;

public class Zone {

	private ArrayList<Position> positions;
	
	public Zone(ArrayList<Position> zone) {
		this.positions=zone;
	}

	public ArrayList<Position> getPositions() {
		return positions;
	}
	
}
