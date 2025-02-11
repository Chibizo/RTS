package gui;


import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import config.GameConfiguration;
import engine.map.*;
import engine.process.*;
import engine.mobile.*;

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

		paintStrategy.paint(map, g);
		
		
		Building building=manager.getBuilding();
		if(building!=null) {
			paintStrategy.paint(building,g);
		}

		

	}
	
}
