package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import config.GameConfiguration;
import engine.map.Map;



public class PaintStrategy {
	public void paint(Map map, Graphics graphics) {
		
		graphics.setColor(Color.GRAY);
		
		int width=GameConfiguration.WINDOW_WIDTH / map.getWidth();
		int height=GameConfiguration.WINDOW_HEIGHT / map.getHeight();
		for (int x = 0; x < map.getWidth(); x++) {
	        for (int y = 0; y < map.getHeight(); y++) {
	            graphics.drawRect(
	                x * width, 
	                y * height, 
	                width, 
	                height
	            );
	        }
	    }
		
	}

}
