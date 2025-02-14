package gui;

import java.awt.Graphics2D;
import engine.mobile.*;
import engine.process.GameBuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import config.GameConfiguration;
import engine.map.*;



public class PaintStrategy {
	

	
	public void paint(Map map, Graphics graphics, Graphics2D g2) {
		int blockSize=GameConfiguration.BLOCK_SIZE;
		Position[][] block=map.getBlock();
		
		for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
			for(int columnIndex=0 ;columnIndex<map.getColumnCount();columnIndex++) {
				int imageSize = GameConfiguration.BLOCK_SIZE*4;
				Position position=block[lineIndex][columnIndex];
				graphics.setColor(new Color(34, 139, 34));
				graphics.fillRect(position.getColumn() * blockSize, position.getLine() * blockSize, blockSize, blockSize);	

			}
		}
		for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
			for(int columnIndex=0 ;columnIndex<map.getColumnCount();columnIndex++) {
				int imageSize = GameConfiguration.BLOCK_SIZE*4;
				Position position=block[lineIndex][columnIndex];
				if(map.isOnBorder(position) && (lineIndex+columnIndex)%3==0) {
					g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalTile_48v2.png"),
							position.getColumn() * GameConfiguration.BLOCK_SIZE -(imageSize - GameConfiguration.BLOCK_SIZE)/2 ,
							position.getLine() * GameConfiguration.BLOCK_SIZE -(imageSize - GameConfiguration.BLOCK_SIZE)/2,   
							imageSize,
					        imageSize,
					        null);
				}
				if (position.getLine() == 9 && position.getColumn() == 43) {
		                g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_02.png"),
		                        position.getLine() * GameConfiguration.BLOCK_SIZE - 15,
		                        position.getColumn() * GameConfiguration.BLOCK_SIZE - 30,
		                        imageSize, imageSize, null);
		        }
		
			}
		}
		
		
		
	}
	
	
	
	public void paint(Building building, Graphics2D g2) {
	    Position position = building.getZone().getPositions().get(0);
	    int imageSize = GameConfiguration.BLOCK_SIZE*4;
	    g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_02.png"),
	    	position.getLine() * GameConfiguration.BLOCK_SIZE-15,   
	        position.getColumn() * GameConfiguration.BLOCK_SIZE-30, 
	        imageSize,
	        imageSize,
	        null);
	}

}
