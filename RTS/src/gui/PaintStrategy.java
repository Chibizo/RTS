package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.mobile.*;
import engine.process.GameBuilder;
import data.map.*;



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
					g2.drawImage(GameBuilder.readImage("src/images/medievalTile_48v2.png"),
							position.getColumn() * GameConfiguration.BLOCK_SIZE -(imageSize - GameConfiguration.BLOCK_SIZE)/2 ,
							position.getLine() * GameConfiguration.BLOCK_SIZE -(imageSize - GameConfiguration.BLOCK_SIZE)/2,   
							imageSize,
					        imageSize,
					        null);
				}
				if (position.getLine() == 12 && position.getColumn() == 50 ) {
		                g2.drawImage(GameBuilder.readImage("src/images/medievalStructure_02.png"),
		                        position.getLine() * GameConfiguration.BLOCK_SIZE - 15,
		                        position.getColumn() * GameConfiguration.BLOCK_SIZE - 30,
		                        imageSize, imageSize, null);
		        }
				
				if((position.getColumn()>=59 && position.getColumn()<=62) && (position.getLine()>=26 && position.getLine()<=30) 
						&&((lineIndex+columnIndex)%2==0)) {
	                g2.drawImage(GameBuilder.readImage("src/images/medievalEnvironment_11.png"),
	                        position.getLine() * GameConfiguration.BLOCK_SIZE -15,
	                        position.getColumn() * GameConfiguration.BLOCK_SIZE -30,
	                        imageSize, imageSize, null);
				}
		
			}
		}
		
		
		
	}
	
	
	
	public void paint(Building building, Graphics2D g2) {
	    Position position = building.getZone().getPositions().get(0);
	    int imageSize = GameConfiguration.BLOCK_SIZE*4;
	    g2.drawImage(GameBuilder.readImage("src/images/medievalStructure_02.png"),
	    	position.getColumn() * GameConfiguration.BLOCK_SIZE-20,   
	        position.getLine() * GameConfiguration.BLOCK_SIZE-35, 
	        imageSize,
	        imageSize,
	        null);
	    
	}
	
	public void paint(Unit unit,Graphics g2) {
		Position position = unit.getZone().getPositions().get(0);
	    int imageSize = GameConfiguration.BLOCK_SIZE*4;
	    g2.drawImage(GameBuilder.readImage("src/images/medievalUnit_08.png"),
	    	position.getColumn() * GameConfiguration.BLOCK_SIZE-20,   
	        position.getLine() * GameConfiguration.BLOCK_SIZE-35, 
	        imageSize,
	        imageSize,
	        null);
	}
	
	

}
