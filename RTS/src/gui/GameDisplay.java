package gui;


import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import config.GameConfiguration;
import engine.map.Map;

public class GameDisplay extends JPanel {

	private static final long serialVersionUID = 1L;

	private Map map;
	
	private PaintStrategy paintStrategy = new PaintStrategy();


	public GameDisplay(Map map) {
		this.map=map;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		paintStrategy.paint(map, g);

	}


}
