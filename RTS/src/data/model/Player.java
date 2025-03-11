package data.model;

import data.map.*;
import java.util.ArrayList;
import java.util.List;

import data.mobile.*;

public class Player {
	private int wood;
	private int magicOre;
	private int slave;
	private int maxSlaves;
	private Race race;
	private Building base ;
	private Zone starterZone;
	private List<Building> buildings=new ArrayList<Building>();
	
	public Player(int wood,int magicOre,Race race,Zone zone) {
		 this.wood=wood;
		 this.magicOre=magicOre;
		 this.race=race;
		 starterZone=zone;
		 base=new Building(zone,0,0,0,0,0,0,race,"base");
	}
	
	
	public Zone getStarterZone() {
		return starterZone;
	}


	public int getWood() {
		return wood;
	}


	public int getMagicOre() {
		return magicOre;
	}


	public int getSlave() {
		return slave;
	}


	public int getMaxSlaves() {
		return maxSlaves;
	}


	public Race getRace() {
		return race;
	}


	public Building getBase() {
		return base;
	}


	
	
	public List<Building> getBuildings() {
		return buildings;
	}
	
	public Building getBuildings(String name) {
		for(Building build : buildings) {
			if(name==build.getName()) {
				return build;
			}
		}
		return null;
	}

	public void addBuilding(Building building) {
		buildings.add(building);
	}


	public void setWood(int wood) {
		this.wood = wood;
	}


	public void setMagicOre(int magicOre) {
		this.magicOre = magicOre;
	}
	
	
	
	
	
	
}
