package gui;

import data.map.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	

	public GameDisplay(Map map,MobileInterface manager) {
		this.map=map;
		this.manager=manager;
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;


		paintStrategy.paint(map, g,g2);
		
		
		Building building=manager.getBuilding();
		if(building!=null) {
			paintStrategy.paint(building,g2);
		}
		
		Unit unit=manager.getUnit();
		if(unit!=null) {
			paintStrategy.paint(unit,g2);
			
	
		
		}

	}
	
}
