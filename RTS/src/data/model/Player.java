package data.model;

import data.map.*;
import java.util.ArrayList;
import data.mobile.*;

public class Player {
	private int wood;
	private int magicOre;
	private int slave;
	private int maxSlaves;
	private Race race;
	private Building base ;
	private Zone starterZone;
	private ArrayList<MobileElement> elements=new ArrayList<MobileElement>();
	
	public Player(int wood,int magicOre,Race race,Zone zone) {
		 this.wood=wood;
		 this.magicOre=magicOre;
		 this.race=race;
		 starterZone=zone;
		 base=new Building(zone,0,0,0,0,0,0,race);
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


	public ArrayList<MobileElement> getElements() {
		return elements;
	}


	public void setWood(int wood) {
		this.wood = wood;
	}


	public void setMagicOre(int magicOre) {
		this.magicOre = magicOre;
	}
	
	
	
	
}
