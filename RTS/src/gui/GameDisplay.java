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
	
	// Selection rectangle points
	private Point selectionStart = null;
	private Point selectionEnd = null;

	public GameDisplay(Map map, MobileInterface manager) {
		this.map = map;
		this.manager = manager;
	}
	
	public void setSelectionRectangle(Point start, Point end) {
		this.selectionStart = start;
		this.selectionEnd = end;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		paintStrategy.paint(map, g, g2);
		
		HashMap<String,Building> buildings = manager.getBuildings();
		for (String typeBuilding : buildings.keySet()) {
			if(typeBuilding=="barracks") {
				paintStrategy.paint(buildings.get(typeBuilding),"barracks", g2);
			} 
			else if (typeBuilding=="base") {
				paintStrategy.paint(buildings.get(typeBuilding),"base", g2);
			}
		}
		
		for (Unit unit : manager.getAllUnits()) {
			if(unit instanceof Slave) {
				paintStrategy.paint((Slave)unit, g2);
			}
			else {
		   }
		}
		
		// Draw selection rectangle if active
		if (selectionStart != null && selectionEnd != null) {
			drawSelectionRectangle(g2);
		}
	}
	
	private void drawSelectionRectangle(Graphics2D g2) {
		int x = Math.min(selectionStart.x, selectionEnd.x);
		int y = Math.min(selectionStart.y, selectionEnd.y);
		int width = Math.abs(selectionEnd.x - selectionStart.x);
		int height = Math.abs(selectionEnd.y - selectionStart.y);
		
		g2.setColor(new Color(0, 255, 0, 80)); // Semi-transparent green
		g2.fillRect(x, y, width, height);
		
		g2.setColor(Color.GREEN);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(x, y, width, height);
	}
}