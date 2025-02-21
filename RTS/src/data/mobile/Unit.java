package data.mobile;

import data.model.*;
import data.map.*;

public class Unit extends MobileElement{

	private String type;
	private float speed;
	private float attackSpeed;
	private int attackDamage;
	private int attackRange;
	
	public Unit (Zone zone,String type,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race) {
		super(zone,maxHealth,currentHealth,wood,magicOre,constructTime,race);
		this.type=type;
	}
	
	public Zone getZone() {
		return super.getZone();
	}
	
	public void setPosition(Position position) {
		super.setPosition(position);

	}
}
