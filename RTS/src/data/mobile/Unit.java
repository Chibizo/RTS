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
	private Unit targetUnit;
	private Building targetBuilding;
	private boolean manuallyCommanded = false;

	
	
	
	
	public Unit (Zone zone,String type,int maxHealth,int currentHealth,int wood,int magicOre,int constructTime,Race race,String name,int attackDamage,int attackRange) {
		super(zone,maxHealth,currentHealth,wood,magicOre,constructTime,race,name);
		this.type=type;
		this.targetPosition=zone.getPositions().get(0);
		this.attackDamage=attackDamage;
		this.attackRange=attackRange;
		
		
	}
	
	public Zone getZone() {
		return super.getZone();
	}
	
	public boolean isManuallyCommanded() {
	    return manuallyCommanded;
	}

	public void setManuallyCommanded(boolean manuallyCommanded) {
	    this.manuallyCommanded = manuallyCommanded;
	}
	
	public Unit getTargetUnit() {
	    return targetUnit;
	}

	public void setTargetUnit(Unit targetUnit) {
	    this.targetUnit = targetUnit;
	}

	public Building getTargetBuilding() {
	    return targetBuilding;
	}

	public void setTargetBuilding(Building targetBuilding) {
	    this.targetBuilding = targetBuilding;
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
