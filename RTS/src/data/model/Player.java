package data.model;

import java.util.ArrayList;
import data.mobile.*;

public class Player {
	private int wood;
	private int magicOre;
	private int slave;
	private int maxSlaves;
	private Race race;
	private ArrayList<MobileElement> elements=new ArrayList<MobileElement>();
	
	public Player(int wood,int magicOre,Race race) {
		 this.wood=wood;
		 this.magicOre=magicOre;
		 this.race=race;
	}
}
