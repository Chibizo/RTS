package data.mobile;

import data.model.*;
import data.map.*;

public class Unit extends MobileElement{

	private String type;
	private float speed;
	private float attackSpeed;
	private int attackDamage;
	private int attackRange;
	private Position targetPosition;
	
	public Unit (Zone zone,String type,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race) {
		super(zone,maxHealth,currentHealth,wood,magicOre,constructTime,race);
		this.type=type;
		this.targetPosition=zone.getPositions().get(0);
	}
	
	public Zone getZone() {
		return super.getZone();
	}
	
	public void setPosition(Position position) {
		super.setPosition(position);

	}
	
	public Position getTargetPosition() {
		return targetPosition;
	}
	
	public void setTargetPosition(Position position) {
		targetPosition=position;
	}
}
