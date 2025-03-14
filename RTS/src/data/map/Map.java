package data.map;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import data.mobile.*;

public class Map {

	private int lineCount;
	private int columnCount;
	private Position[][] block;
	
	private Zone woodLocations;
	private Zone magicOreLocations;
	
	private List<Zone> fullPosition=new ArrayList<Zone>();
	private List<Zone> fullUnitsPosition=new ArrayList<Zone>();
	
	
	public Map(int lineCount,int columnCount ) {
		System.out.println("Création de la carte avec dimensions : " + lineCount + "x" + columnCount);
		init(lineCount,columnCount);
		for(int lineIndex=0; lineIndex<lineCount;lineIndex++) {
			for(int columnIndex=0;columnIndex<columnCount;columnIndex++) {
				block[lineIndex][columnIndex]=new Position(lineIndex,columnIndex);
			}
		}
		intiRessources();

	}	
	public void init(int lineCount,int columnCount) {
		this.lineCount=lineCount;
		this.columnCount=columnCount;
		
		block=new Position[lineCount][columnCount];
	}
	
	public void intiRessources() {
		
		
		ArrayList<Position> magicOrePositions = new ArrayList<Position>();
		magicOrePositions.add(new Position(55, 24)); 
		magicOrePositions.add(new Position(56, 24)); 
		magicOrePositions.add(new Position(55, 23));
		magicOrePositions.add(new Position(56, 23));
		//IAPlayer magicore
		magicOrePositions.add(new Position(5, 93)); 
		magicOrePositions.add(new Position(6, 93)); 
		magicOrePositions.add(new Position(5, 92));
		magicOrePositions.add(new Position(6, 92));
	    magicOreLocations=new Zone(magicOrePositions);
	    
		
		ArrayList<Position> woodPositions=new ArrayList<Position>();
		 for (int lineIndex = 44; lineIndex <= 46; lineIndex++) {
		        for (int columnIndex = 6; columnIndex <= 8; columnIndex++) {
		            Position position = block[lineIndex][columnIndex];
		            woodPositions.add(position);
		        }
		 }
		 for (int lineIndex = 15; lineIndex <= 17; lineIndex++) {
		        for (int columnIndex = 112; columnIndex <= 114; columnIndex++) {
		            Position position = block[lineIndex][columnIndex];
		            woodPositions.add(position);
		        }
		 }
		woodLocations=new Zone(woodPositions);
		fullPosition.add(magicOreLocations);
		fullPosition.add(woodLocations);
		
	}
	
		
		
	
	public void addFullPosition(Zone zone) {
	    if (!fullPosition.contains(zone)) {
	        fullPosition.add(zone);  
	    }
	}
	
	public void addFullUnitsPosition(Zone zone) {
		if(!fullUnitsPosition.contains(zone)) {
			fullUnitsPosition.add(zone);
		}
	}
	
	public int getLineCount() {
		return lineCount;
	}
	public int getColumnCount() {
		return columnCount;
	}
	public Position[][] getBlock() {
		return block;
	}
	
	public Position getBlock(int line, int column) {
		return block[line][column];
	}

	public boolean isOnTop(Position position) {
		int line = position.getLine();
		return line == 0;
	}

	public boolean isOnBottom(Position position) {
		int line = position.getLine();
		return line == lineCount - 1;
	}

	public boolean isOnLeftBorder(Position position) {
		int column = position.getColumn();
		return column == 0;
	}

	public boolean isOnRightBorder(Position position) {
		int column = position.getColumn();
		return column == columnCount - 1;
	}
	
	public boolean isfull(Position position) {
		
		for(Zone zone : fullPosition) {
			for(Position pos : zone.getPositions()) {
				if(position.equals(pos)) {
					return true;
				}
			}
			
		}
		for(Zone zone : fullUnitsPosition) {
			for(Position pos : zone.getPositions()) {
				if(position.equals(pos)) {
					return true;
				}
			}
			
		}
		return false;
	}
	
	public synchronized boolean isfullUnits(Position position) {
		try {
			for(Zone zone : fullUnitsPosition) {
				for(Position pos : zone.getPositions()) {
					if(position.equals(pos)) {
						return true;
					}
				}
				
			}
			return false;
		}catch(ConcurrentModificationException e) {
			System.out.println("probleme de concurrence");
		}
		return true;
	}

	

	public void removeFullPosition(Position position) {
	    Iterator<Zone> iterator = fullPosition.iterator();
	    while (iterator.hasNext()) {
	        Zone zone = iterator.next();
	        for (Position pos : zone.getPositions()) {
	            if (pos.equals(position)) {
	                iterator.remove(); 
	                break; 
	            }
	        }
	    }
	}
	public void removeFullUnitsPosition(Position position) {
	    List<Zone> zonesToRemove = new ArrayList<>();
	    
	    for (Zone zone : fullPosition) {
	        for (Position pos : zone.getPositions()) {
	            if (pos.equals(position)) {
	                zonesToRemove.add(zone);
	                break; 
	            }
	        }
	    }
	    
	    fullUnitsPosition.removeAll(zonesToRemove);
	}

	public boolean isOnBorder(Position position) {
		return isOnTop(position) || isOnBottom(position) || isOnLeftBorder(position) || isOnRightBorder(position);
	}
	public Zone getWoodLocations() {
		return woodLocations;
	}
	public Zone getMagicOreLocations() {
		return magicOreLocations;
	}
	
	public void setWoodLocations(Zone woodLocations) {
		this.woodLocations = woodLocations;
	}
	public void setMagicOreLocations(Zone magicOreLocations) {
		this.magicOreLocations = magicOreLocations;
	}
	
	public List<Zone> getFullPosition() {
		return fullPosition;
	}
	
	public List<Zone> getFullUnitsPosition() {
		return fullUnitsPosition;
	}
	
	
	
		
	
	
	
}
