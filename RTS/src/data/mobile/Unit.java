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
	private boolean selected = false;
	private long attackCooldown;
	private long lastAttackTime;

	
	
	public Unit (Zone zone,String type,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race,String name,int attackDamage) {
		super(zone,maxHealth,currentHealth,wood,magicOre,constructTime,race,name);
		this.type=type;
		this.targetPosition=zone.getPositions().get(0);
	}
	
	public Zone getZone() {
		return super.getZone();
	}
	
	public boolean isSelected() {
	    return selected;
	}
	
	public void setSelected(boolean selected) {
	    this.selected = selected;
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

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public String getType() {
		return type;
	}

	public float getSpeed() {
		return speed;
	}

	public float getAttackSpeed() {
		return attackSpeed;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public long getAttackCooldown() {
		return attackCooldown;
	}

	public void setAttackCooldown(long attackCooldown) {
		this.attackCooldown = attackCooldown;
	}

	public long getLastAttackTime() {
		return lastAttackTime;
	}

	public void setLastAttackTime(long lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}
	
	
	
}
