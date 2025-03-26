package gui;

import data.map.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import config.GameConfiguration;
import data.map.Map;
import data.mobile.*;
import engine.process.MobileInterface;

public class GameDisplay extends JPanel {

	private static final long serialVersionUID = 1L;

	private Map map;
	
	private PaintStrategy paintStrategy = new PaintStrategy();
	
	private MobileInterface manager;
	
 	private Point selectionStart = null;
	private Point selectionEnd = null;
	private String previewBuildingType = "";
	private Point previewMousePosition = new Point(0, 0);

	public GameDisplay(Map map, MobileInterface manager) {
		this.map = map;
		this.manager = manager;
	}
	
	public void setSelectionRectangle(Point start, Point end) {
		this.selectionStart = start;
		this.selectionEnd = end;
	}
	
	public void setPreviewBuilding(String type, Point mousePos) {
	    this.previewBuildingType = type;
	    this.previewMousePosition = mousePos;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		paintStrategy.paint(map, g, g2);
		
		HashMap<String,Building> buildings = manager.getBuildingsMainPlayer();
		List<String> buildingKeys = new ArrayList<>(buildings.keySet());
		
		for (String typeBuilding : buildingKeys) {
		    Building building = buildings.get(typeBuilding);
		    if (building != null) {  
		        if(typeBuilding=="barracks") {
		            paintStrategy.paint(building, "barracks", g2);
		        }else if(typeBuilding=="runway") {
		        	paintStrategy.paint(building,"runway", g2);
		        
		        }else if(typeBuilding=="archery") {
		        	paintStrategy.paint(building,"archery", g2);
		        	
		        }else if (typeBuilding=="base") {
		            paintStrategy.paint(building, "base", g2);
		        }
		    }
		}
		HashMap<String,Building> buildings2 = manager.getBuildingsAIPlayer();
		List<String> buildingKeys2 = new ArrayList<>(buildings2.keySet());
		
		for (String typeBuilding : buildingKeys2) {
		    Building building = buildings2.get(typeBuilding);
		    if (building != null) { 	
		        if(typeBuilding=="barracks") {
		            paintStrategy.paint(building, "barracks", g2);
		        }else if(typeBuilding=="runway") {
		        	paintStrategy.paint(building,"runway", g2);
		        	
		    	}else if(typeBuilding=="archery") {
		        	paintStrategy.paint(building,"archery", g2);
		    		
		        }else if (typeBuilding=="base") {
		            paintStrategy.paint(building, "base", g2);
		        }
		    }
		}
		if(previewBuildingType != null && !previewBuildingType.isEmpty()) {
			paintStrategy.paint(map,previewBuildingType,previewMousePosition, g2);
		}
		
		List<Unit> allUnits = new ArrayList<>(manager.getAllUnits());
		
		for (Unit unit : allUnits) {
			if(unit instanceof Slave) {
				paintStrategy.paint((Slave)unit, g2);
			}
			else if(unit.getName()=="warrior"){
				paintStrategy.paint(unit, "warrior", g2);
		    }else if(unit.getName()=="wizard") {
		    	paintStrategy.paint(unit,"wizard", g2);
		    }else if(unit.getName()=="bowman") {
		    	paintStrategy.paint(unit,"bowman", g2);
		    }
		}
		
		if (selectionStart != null && selectionEnd != null) {
			paintStrategy.drawSelectionRectangle(selectionStart,selectionEnd,g2);
		}
	}
	

}