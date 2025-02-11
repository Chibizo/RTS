package gui;


import engine.mobile.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import config.GameConfiguration;
import engine.map.*;



public class PaintStrategy {
	
	
	public void paint(Map map, Graphics graphics) {
		int blockSize=GameConfiguration.BLOCK_SIZE;
		Position[][] block=map.getBlock();
		
		for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
			for(int columnIndex=0 ;columnIndex<map.getColumnCount();columnIndex++) {
				Position position=block[lineIndex][columnIndex];
				graphics.setColor(new Color(34, 139, 34));
				
				graphics.fillRect(position.getColumn() * blockSize, position.getLine() * blockSize, blockSize, blockSize);	

			}
		}
		
	}
	
	public void paint(Building building,Graphics graphics) {
		int blockSize=GameConfiguration.BLOCK_SIZE;		
		for(Position position : building.getZone().getPositions()) {
			int x=position.getLine();
			int y=position.getColumn();
			
			
			graphics.setColor(Color.BLACK);
			graphics.fillRect(x*blockSize, y*blockSize, blockSize, blockSize);
		}
	}

}
